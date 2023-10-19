package ru.pavbatol.myplace.user.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.exception.BadRequestException;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.app.exception.RegistrationException;
import ru.pavbatol.myplace.email.service.EmailService;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.role.model.RoleName;
import ru.pavbatol.myplace.role.repository.RoleRepository;
import ru.pavbatol.myplace.user.client.ProfileClient;
import ru.pavbatol.myplace.user.dto.UserDtoConfirm;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.model.UserUnverified;
import ru.pavbatol.myplace.user.dto.UserDtoUpdatePassword;
import ru.pavbatol.myplace.user.mapper.UserMapper;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.model.UserAuthenticationPrincipal;
import ru.pavbatol.myplace.user.repository.UnverifiedUserRedisRepository;
import ru.pavbatol.myplace.user.repository.UserJpaRepository;
import ru.pavbatol.myplace.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String ENTITY_SIMPLE_NAME = User.class.getSimpleName();
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private final UnverifiedUserRedisRepository userRedisRepository;
    private final UserJpaRepository userJpaRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final ProfileClient profileClient;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Transactional
    @Override
    public void register(HttpServletRequest servletRequest, UserDtoRegistry dto) {
        final String email = dto.getEmail();
        final String login = dto.getLogin();
        final String code = generateCode();
        final String encodedCode = passwordEncoder.encode(code);
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
        final UserUnverified dtoUnverified = mapper.toDtoUnverified(dto, encodedCode, encodedPassword);

        assert dtoUnverified.getEmail().equals(email) : "email should not change";
        assert dtoUnverified.getLogin().equals(login) : "login should not change";

        userRedisRepository.addByAtomicLoginAndEmailKeys(dtoUnverified);
        log.debug("Unverified {} saved to Redis with email: {}, login: {}, password and code are hidden for security",
                ENTITY_SIMPLE_NAME, dtoUnverified.getEmail(), dtoUnverified.getLogin());
        log.debug("Login of unverified {} saved to Redis, login: {},", ENTITY_SIMPLE_NAME, dtoUnverified.getLogin());

        try {
            if (userJpaRepository.existsByLogin(login)) {
                throw new RegistrationException("A user with this login is already registered and verified: " + dto.getLogin());
            }

            if (profileClient.existsByEmail(email)) {
                throw new RegistrationException("A user with this email is already registered and verified: " + dto.getEmail());
            }
        } catch (Exception e) {
            userRedisRepository.removeSilently(email);
            userRedisRepository.removeLoginKeySilently(login);
            throw new RegistrationException("Failed registering: " + e.getMessage());
        }

        String text = String.format("You have received this email because your email-address was specified " +
                        "during registration on '%s'\nYour confirmation code:\n%s",
                servletRequest.getServerName(), code);

        // TODO: 04.10.2023 Connect GreenMail or JavaMailMock for testing
        /**
         * Temporary: The code below is commented out for testing without specifying email
         */
//      emailService.sendSimpleMessage(dto.getEmail(), "Confirmation code", text);
//      --

        /**
         * Temporary: The following code is intended for direct saving of the user, without confirmation by mail.
         * This is for testing the application if you don't specify an email for sending.
         */
        log.debug("Data for confirmation: email: {}, code: {}", dto.getEmail(), code);
//      ...
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void confirmRegistration(UserDtoConfirm dtoConfirm) {
        UserUnverified userUnverified = userRedisRepository.find(dtoConfirm.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not confirmed.", "Email not found."));

        if (!passwordEncoder.matches(dtoConfirm.getCode(), userUnverified.getCode())) {
            throw new RegistrationException("Invalid confirmation code.");
        }

        Role role = getNonNullRoleFromDB(RoleName.USER);
        User user = new User()
                .setUuid(UUID.randomUUID())
                .setPassword(userUnverified.getPassword())
                .setLogin(userUnverified.getLogin())
                .setRoles(Set.of(role))
                .setDeleted(false);

        User savedUser = userJpaRepository.save(user);
        profileClient.createProfile(savedUser.getId(), dtoConfirm.getEmail());

        userRedisRepository.removeSilently(dtoConfirm.getEmail());
        userRedisRepository.removeLoginKeySilently(userUnverified.getLogin());

        log.debug("{} with email: {} confirmed with code: {}", ENTITY_SIMPLE_NAME, dtoConfirm.getEmail(), dtoConfirm.getCode());
        log.debug("{} created with id: {}, uuid: {}, login: {}, deleted: {}, roles {}, password: hidden for security",
                ENTITY_SIMPLE_NAME, savedUser.getId(), savedUser.getUuid(), savedUser.getLogin(), savedUser.getDeleted(), savedUser.getRoles());
        log.debug("Profile created in profile service with userId: {}, email: {}", savedUser.getId(), dtoConfirm.getEmail());
    }

    @Override
    public void changePassword(UUID userUuid, UserDtoUpdatePassword dto) {
        User origUser = userJpaRepository.findByUuid(userUuid).orElseThrow(() ->
                new NotFoundException(String.format("%s with UUID: %s not found.", ENTITY_SIMPLE_NAME, userUuid))
        );
        checkUuidOwnership(userUuid, "You can only edit your own data.");
        origUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        userJpaRepository.save(origUser);
        log.debug("{} with UUID: {} is updated", ENTITY_SIMPLE_NAME, userUuid);
    }

    @Override
    public Long getIdByUuid(UUID userUuid) {
        checkUuidOwnership(userUuid, "You do not have access to other user's data.");
        Long userId = userJpaRepository.getIdByUuid(userUuid).orElseThrow(() ->
                new NotFoundException(String.format("%s with UUID: %s not found.", ENTITY_SIMPLE_NAME, userUuid))
        );
        log.debug("Obtained {} id: {} by UUID: {}", ENTITY_SIMPLE_NAME, userId, userUuid);
        return userId;
    }

    private String generateCode() {
        final int codeLength = 5;
        final String[] source = {UPPER, LOWER, DIGITS};
        final String alphabet = String.join("", source);
        final StringBuilder builder = new StringBuilder();
        final Random random = new Random();

        for (int i = 0; i < codeLength - source.length; i++) {
            int index = random.nextInt(alphabet.length());
            builder.append(alphabet.charAt(index));
        }

        for (String str : source) {
            int index = random.nextInt(str.length());
            builder.insert(random.nextInt(builder.length() + 1), str.charAt(index));
        }

        return builder.toString();
    }

    private Role getNonNullRoleFromDB(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    return new NotFoundException(String.format("%s not found by %s.",
                            Role.class.getSimpleName(), roleName));
                });
    }

    private void checkUuidOwnership(@NonNull UUID userUuid, String message) {
        UserAuthenticationPrincipal principal = checkAndGetAuthenticationPrincipal();

        if (!userUuid.equals(principal.getUuid())) {
            throw new IllegalArgumentException(message);
        }
    }

    private UserAuthenticationPrincipal checkAndGetAuthenticationPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : null;

        if (!(principal instanceof UserAuthenticationPrincipal)) {
            throw new IllegalArgumentException("Authentication principal is invalid.");
        }

        return (UserAuthenticationPrincipal) principal;
    }
}
