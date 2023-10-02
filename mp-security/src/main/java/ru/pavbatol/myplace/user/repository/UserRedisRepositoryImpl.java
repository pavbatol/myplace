package ru.pavbatol.myplace.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.config.RedisKeys;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserRedisRepositoryImpl implements UserRedisRepository<UserDtoRegistry> {
    private static final String KEY_PREFIX = RedisKeys.USERS_UNVERIFIED.getKey() + ":";
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.ttl-sec-unverified:180}")
    private Long ttl;

    @Override
    public boolean save(String hashKey, UserDtoRegistry dtoRegistry) {
        final String key = KEY_PREFIX + hashKey;
        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(key, dtoRegistry, ttl, TimeUnit.SECONDS);
        if (isSet != null) {
            return isSet;
        } else {
            throw new RuntimeException("Unknown result: DB returned null");
        }
    }

    @Override
    public boolean remove(String hashKey) {
        Boolean isDel = redisTemplate.delete(KEY_PREFIX + hashKey);
        if (isDel != null) {
            return isDel;
        } else {
            throw new RuntimeException("Unknown result: DB returned null");
        }
    }

    @Override
    public Optional<UserDtoRegistry> findByHashKey(String hashKey) {
        return Optional.ofNullable((UserDtoRegistry) redisTemplate.opsForValue().get(hashKey));
    }
}
