package ru.pavbatol.myplace.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.user.client.ProfileClient;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.repository.UserRedisRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String ENTITY_SIMPLE_NAME = User.class.getSimpleName();
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private final UserRedisRepository<UserDtoUnverified> redisRepository;
    private final ProfileClient profileClient;

    @Override
    public void register(HttpServletRequest servletRequest, UserDtoRegistry dto) {
        String key = dto.getEmail();
        String code = generateCode();
        UserDtoUnverified dtoUnverified = new UserDtoUnverified(
                dto.getEmail(),
                dto.getLogin(),
                dto.getPassword(),
                code
        );
        if (redisRepository.save(key, dtoUnverified)) {
            if (!profileClient.existsByEmail(dto.getEmail())) {



                return;
            } else {
                redisRepository.remove(key);
            }
        }

        throw new RuntimeException("A user with this email is already registered");
    }


    @Override
    public void confirmRegistration(String code) {

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

//    public static String encode(String code) {
//        private static final Charset CHARSET = StandardCharsets.UTF_8;
//        return new String(Base64.getEncoder().encode(code.getBytes(CHARSET)));
//    }

//    public static String decode(String encodedCode) {
//        return new String(Base64.getDecoder().decode(encodedCode), CHARSET);
//    }

}
