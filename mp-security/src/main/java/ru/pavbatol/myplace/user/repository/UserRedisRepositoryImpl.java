package ru.pavbatol.myplace.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.config.RedisKey;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserRedisRepositoryImpl implements UserRedisRepository<UserDtoUnverified> {
    private static final String KEY_PREFIX = RedisKey.USERS_UNVERIFIED_EMAIL.getKey() + ":";
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.ttl-sec-unverified:180}")
    private long ttl;

    @Override
    public boolean save(String hashKey, UserDtoUnverified obj) throws RedisException {
        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(composeKey(hashKey), obj, ttl, TimeUnit.SECONDS);
        if (isSet != null) {
            return isSet;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    public Optional<Boolean> remove(String hashKey) {
        return Optional.ofNullable(redisTemplate.delete(composeKey(hashKey)));
    }

    @Override
    public Optional<UserDtoUnverified> findByHashKey(String hashKey) {
        return Optional.ofNullable((UserDtoUnverified) redisTemplate.opsForValue().get(composeKey(hashKey)));
    }

    private static String composeKey(String hashKey) {
        return KEY_PREFIX + hashKey;
    }
}
