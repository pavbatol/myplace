package ru.pavbatol.myplace.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.RedisKey;
import ru.pavbatol.myplace.app.redis.repository.AbstractRedisRepository;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UnverifiedUserRedisRepositoryImpl extends AbstractRedisRepository<UserDtoUnverified> implements UnverifiedUserRedisRepository {

    private final RedisKey loginRedisKey = RedisKey.USERS_UNVERIFIED_LOGIN;

    @Autowired
    protected UnverifiedUserRedisRepositoryImpl(Environment environment) {
        super(
                RedisKey.USERS_UNVERIFIED_EMAIL,
                Long.parseLong(environment.getProperty("redis.ttl-sec-unverified-user", "180"))
        );
    }

    @Override
    public boolean addLogin(@NotNull String login, @NotNull String email) throws RedisException {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(composeLoginKey(login), email, ttl, TimeUnit.SECONDS);
        if (result != null) {
            return result;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    @Transactional
    public void addAtomicLoginAndEmailKeys(String login, String email, UserDtoUnverified unverifiedUser) throws RedisException {
        String loginKey = composeLoginKey(login);
        String emailKey = composeKey(email);

        try {
            try {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(loginKey)) || Boolean.FALSE.equals(addLogin(login, email))) {
                    throw new IllegalArgumentException("An unverified user with such login already exists, login: " + login);
                }
            } catch (RedisException ignored) {
            }

            try {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(emailKey)) || Boolean.FALSE.equals(add(email, unverifiedUser))) {
                    throw new IllegalArgumentException("An unverified user with such email already exists, email: " + email);
                }
            } catch (RedisException ignored) {
            }

        } catch (Exception e) {
            throw new RedisException("Failed to create login and email keys. Reason: "
                    + (e.getMessage() != null ? e.getMessage() : "no message"));
        }
    }

    @Override
    public void removeLoginSilently(String login) {
        try {
            redisTemplate.delete(composeLoginKey(login));
        } catch (Exception ignored) {
        }
    }

    @Override
    public void removeSilently(String key) {
        try {
            redisTemplate.delete(composeKey(key));
        } catch (Exception ignored) {
        }
    }

    @Override
    protected Class<UserDtoUnverified> getType() {
        return UserDtoUnverified.class;
    }

    private String composeLoginKey(@NotNull String key) {
        return loginRedisKey.getKey() + key;
    }
}
