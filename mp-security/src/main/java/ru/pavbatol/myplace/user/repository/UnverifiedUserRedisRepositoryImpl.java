package ru.pavbatol.myplace.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.redis.RedisKey;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.repository.AbstractRedisRepository;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Component
public class UnverifiedUserRedisRepositoryImpl extends AbstractRedisRepository<UserDtoUnverified> implements UnverifiedUserRedisRepository {

    private final RedisKey loginRedisKey = RedisKey.USERS_UNVERIFIED_LOGIN;

    @Autowired
    protected UnverifiedUserRedisRepositoryImpl(Environment environment) {
        super(
                RedisKey.USERS_UNVERIFIED_EMAIL,
                Long.parseLong(environment.getProperty("redis.ttl-sec-unverified", "180"))
        );
    }

    @Override
    public boolean createLogin(@NotNull String login, @NotNull String email) throws RedisException {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(composeLoginKey(login), email, ttl, TimeUnit.SECONDS);
        if (result != null) {
            return result;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    public void deleteLoginSilently(@NotNull String login) {
        try {
            redisTemplate.delete(composeLoginKey(login));
        } catch (Exception ignored) {
        }
    }

    @Override
    public void deleteSilently(@NotNull String key) {
        try {
            redisTemplate.delete(composeKey(key));
        } catch (Exception ignored) {
        }
    }

    @Override
    protected Class<UserDtoUnverified> getType() {
        return UserDtoUnverified.class;
    }

    private String composeLoginKey(String key) {
        return loginRedisKey.getKey() + key;
    }
}
