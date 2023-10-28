package ru.pavbatol.myplace.user.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.app.exception.RegistrationException;
import ru.pavbatol.myplace.app.exception.SendingMailException;
import ru.pavbatol.myplace.email.service.EmailService;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.role.model.RoleName;
import ru.pavbatol.myplace.role.repository.RoleRepository;
import ru.pavbatol.myplace.user.client.ProfileClient;
import ru.pavbatol.myplace.user.dto.*;
import ru.pavbatol.myplace.user.model.UserUnverified;
import ru.pavbatol.myplace.user.mapper.UserMapper;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.model.UserAuthenticationPrincipal;
import ru.pavbatol.myplace.user.repository.UnverifiedUserRedisRepository;
import ru.pavbatol.myplace.user.repository.UserJpaRepository;
import ru.pavbatol.myplace.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String ENTITY_SIMPLE_NAME = User.class.getSimpleName();
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String TEST_PROFILE = "test";
    private final UnverifiedUserRedisRepository userRedisRepository;
    private final UserJpaRepository userJpaRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final ProfileClient profileClient;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final Environment environment;


    @Override
    public void changePassword(UUID userUuid, UserDtoUpdatePassword dto) {
        User origUser = getNonNullUserByUuid(userUuid);
        checkUuidOwnership(userUuid, "You can only edit your own data.");
        origUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        userJpaRepository.save(origUser);
        log.debug("{} with UUID: {} password updated", ENTITY_SIMPLE_NAME, userUuid);
    }

    @Override
    public UserDtoResponse updateRoles(UUID userUuid, UserDtoUpdateRoles dto) {
        User origUser = getNonNullUserByUuid(userUuid);
        Set<Long> roleIds = dto.getRoleIds();

        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (newRoles.size() != roleIds.size()) {
            throw new NotFoundException(String.format("Out of %s role ids %s were found", roleIds.size(), newRoles.size()));
        }

        origUser.setRoles(newRoles);
        User saved = userJpaRepository.save(origUser);
        log.debug("{} with UUID: {} roles updated", ENTITY_SIMPLE_NAME, userUuid);

        return userMapper.toResponseDto(saved);
    }

    @Override
    public void delete(UUID userUuid) {
        User origUser = getNonNullUserByUuid(userUuid);
        origUser.setDeleted(true);
        userJpaRepository.save(origUser);
        log.debug("{} with UUID: {} marked as deleted", ENTITY_SIMPLE_NAME, userUuid);
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

    @Override
    public UserDtoResponse findByUuid(UUID userUuid) {
        User found = getNonNullUserByUuid(userUuid);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return userMapper.toResponseDto(found);
    }

    @Override
    public List<UserDtoResponse> findAll(Integer from, Integer size) {
        Sort sort = Sort.by("id");
        PageRequest pageRequest = PageRequest.of(from, size, sort);
        Slice<User> page = userJpaRepository.findAll(pageRequest);
        log.debug("Found number of {}'s: {} , number: {}, from: {}, size: {}, sort: {}", ENTITY_SIMPLE_NAME,
                page.getNumberOfElements(), page.getNumber(), page.getNumber(), page.getSize(), page.getSort());
        return userMapper.toResponseDtos(page.getContent());
    }

    @Transactional
    @Override
    public String register(HttpServletRequest servletRequest, UserDtoRegistry dto) {
        final String email = dto.getEmail();
        final String login = dto.getLogin();
        final String code = generateCode();
        final String encodedCode = passwordEncoder.encode(code);
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
        final UserUnverified userUnverified = userMapper.toUserUnverified(dto, encodedCode, encodedPassword);

        assert userUnverified.getEmail().equals(email) : "email should not change";
        assert userUnverified.getLogin().equals(login) : "login should not change";

        userRedisRepository.addByAtomicLoginAndEmailKeys(userUnverified);
        log.debug("Unverified {} saved to Redis with email: {}, login: {}, password and code are hidden for security",
                ENTITY_SIMPLE_NAME, userUnverified.getEmail(), userUnverified.getLogin());
        log.debug("Login of unverified {} saved to Redis, login: {},", ENTITY_SIMPLE_NAME, userUnverified.getLogin());

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

        try {
            emailService.sendSimpleMessage(dto.getEmail(), "Confirmation code", text);
        } catch (SendingMailException e) {
            if (hasNoMailSenderTest()) {
                log.error("{} {}", e.getMessage(), e.getReason());
            } else {
                throw new SendingMailException(e.getMessage(), e.getReason());
            }
        }

        log.debug("Data for confirmation: email: {}, code: {}", dto.getEmail(), code);

        return hasTestProfile() ? code : "Confirmation code has been sent to your email address.\nConfirm your email.";
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void confirmRegistration(UserDtoConfirm dtoConfirm) {
        UserUnverified userUnverified = userRedisRepository.find(dtoConfirm.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not confirmed.", "Email not found."));

        if (!passwordEncoder.matches(dtoConfirm.getCode(), userUnverified.getCode())) {
            throw new RegistrationException("Invalid confirmation code.");
        }

        Role role = getNonNullRoleByName(RoleName.USER);
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

    private Role getNonNullRoleByName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    return new NotFoundException(String.format("%s not found by role name:%s.",
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

    private User getNonNullUserByUuid(UUID userUuid) {
        return userJpaRepository.findByUuid(userUuid).orElseThrow(() ->
                new NotFoundException(String.format("%s with UUID: %s not found.", ENTITY_SIMPLE_NAME, userUuid))
        );
    }

    private boolean hasTestProfile() {
        return environment.matchesProfiles(TEST_PROFILE, "test-confirmation-code-reading");
    }

    private boolean hasNoMailSenderTest() {
        return environment.matchesProfiles("test-mail-sender-bypassing");
    }
}
