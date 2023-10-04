package ru.pavbatol.myplace.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.app.exception.RegistrationException;
import ru.pavbatol.myplace.email.service.EmailService;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.role.model.RoleName;
import ru.pavbatol.myplace.user.client.ProfileClient;
import ru.pavbatol.myplace.user.dto.UserDtoConfirm;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;
import ru.pavbatol.myplace.user.mapper.UserMapper;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.repository.UserJpaRepository;
import ru.pavbatol.myplace.user.repository.UserRedisRepository;

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
    private final UserRedisRepository<UserDtoUnverified> userRedisRepository;
    private final UserJpaRepository userJpaRepository;
    private final EmailService emailService;
    private final ProfileClient profileClient;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Override
    public void register(HttpServletRequest servletRequest, UserDtoRegistry dto) {
        final String key = dto.getEmail();
        final String code = generateCode();
        final String encodedCode = passwordEncoder.encode(code);
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
        UserDtoUnverified dtoUnverified = mapper.toDtoUnverified(dto, encodedCode, encodedPassword);

        if (userRedisRepository.save(key, dtoUnverified)) {
            log.debug("unverified {} saved to Redis: {}", ENTITY_SIMPLE_NAME, dtoUnverified);
            if (!profileClient.existsByEmail(dto.getEmail())) {
                String text = String.format("You have received this email because your email-address was specified " +
                                "during registration on '%s'\nYour confirmation code:\n%s",
                        servletRequest.getServerName(), code);

                // TODO: 04.10.2023 Connect GreenMail or JavaMailMock for testing
                /**
                 * Temporary: The code below is commented out for testing without specifying email
                 */
//                emailService.sendSimpleMessage(dto.getEmail(), "Confirmation code", text);
//                --

                /**
                 * Temporary: The following code is intended for direct saving of the user, without confirmation by mail.
                 * This is for testing the application if you don't specify an email for sending.
                 */
                log.debug("Data for confirmation: email: {}, cd: {}", dto.getEmail(), code);
//                ...
//                ...

                return;
            } else {
                userRedisRepository.remove(key);
            }
        }

        throw new RuntimeException("A user with this email is already registered");
    }


    @Transactional
    @Override
    public void confirmRegistration(UserDtoConfirm dtoConfirm) {
        UserDtoUnverified dtoUnverified = userRedisRepository.findByHashKey(dtoConfirm.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not confirmed", "Email not found"));

        if (!passwordEncoder.matches(dtoConfirm.getCode(), dtoUnverified.getCode())) {
            throw new RegistrationException("Invalid confirmation code");
        }

        Role role = new Role().setRoleName(RoleName.USER);
        User user = new User()
                .setUuid(UUID.randomUUID())
                .setPassword(dtoUnverified.getPassword())
                .setLogin(dtoUnverified.getLogin())
                .setRoles(Set.of(role))
                .setDeleted(false);

        User savedUser = userJpaRepository.save(user);
        profileClient.createProfile(savedUser.getId(), dtoConfirm.getEmail());

        userRedisRepository.remove(dtoConfirm.getEmail());

        log.debug("{} with email: {} confirmed with code: {}", ENTITY_SIMPLE_NAME, dtoConfirm.getEmail(), dtoConfirm.getCode());
        log.debug("{} created: {}", ENTITY_SIMPLE_NAME, savedUser);
        log.debug("Profile created in profile service with userId: {},email: {}", savedUser.getId(), dtoConfirm.getEmail());
    }

    private String generateCode() {
        final Random random = new Random();
        final String symbols = UPPER + LOWER + DIGITS;
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(symbols.length());
            builder.append(symbols.charAt(index));
        }
        return builder.toString();
    }
}
