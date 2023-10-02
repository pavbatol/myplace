package ru.pavbatol.myplace.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.config.RedisKeys;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRedisRepositoryImpl implements UserRedisRepository<UserDtoRegistry> {
    private static final String KEY = RedisKeys.USERS_UNVERIFIED.getKey();
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean save(UserDtoRegistry dtoRegistry) {
        return redisTemplate.opsForHash().putIfAbsent(KEY, dtoRegistry.getEmail(), dtoRegistry);
    }

    @Override
    public boolean remove(String hashKey) {
        return redisTemplate.opsForHash().delete(KEY, hashKey) == 1;
    }

    @Override
    public Optional<UserDtoRegistry> findByHashKey(String hashKey) {
        return Optional.ofNullable((UserDtoRegistry) redisTemplate.opsForHash().get(KEY, hashKey));
    }
}
