package ru.pavbatol.myplace.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.config.RedisKey;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class UnverifiedUserRedisRepository extends AbstractRedisRepository<UserDtoUnverified> {

    private final RedisKey loginRedisKey = RedisKey.USERS_UNVERIFIED_LOGIN;

    @Autowired
    protected UnverifiedUserRedisRepository(Environment environment) {
        super(
                RedisKey.USERS_UNVERIFIED_EMAIL,
                Long.parseLong(environment.getProperty("redis.ttl-sec-unverified", "180"))
        );
    }

    public boolean createLogin(@NotNull String login, @NotNull String email) throws RedisException {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(composeLoginKey(login), email, ttl, TimeUnit.SECONDS);
        if (result != null) {
            return result;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    public Optional<Boolean> deleteLoginWithoutException(@NotNull String login) {
        try {
            return Optional.ofNullable(redisTemplate.delete(composeLoginKey(login)));
        } catch (Exception e) {
            return Optional.empty();
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
