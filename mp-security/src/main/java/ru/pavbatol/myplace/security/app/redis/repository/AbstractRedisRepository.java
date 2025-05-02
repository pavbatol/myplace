package ru.pavbatol.myplace.security.app.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import ru.pavbatol.myplace.security.app.redis.RedisKey;
import ru.pavbatol.myplace.security.app.exception.RedisException;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisRepository<T> implements RedisRepository<T> {
    protected final RedisKey redisKey;
    protected final long ttl;
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    protected AbstractRedisRepository(RedisKey redisKey, long ttl) {
        this.redisKey = redisKey;
        this.ttl = ttl;
    }

    protected abstract Class<T> getType();

    protected String composeKey(@NotNull String key) {
        return redisKey.getKey() + key;
    }

    @Override
    public boolean add(@NotNull String key, @NotNull T obj) throws RedisException {
        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(composeKey(key), obj, ttl, TimeUnit.SECONDS);
        if (isSet != null) {
            return isSet;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    public void set(@NotNull String key, @NotNull T obj) {
        redisTemplate.opsForValue().set(composeKey(key), obj, ttl, TimeUnit.SECONDS);
    }

    @Override
    public boolean remove(@NotNull String key) throws RedisException {
        Boolean isDeleted = redisTemplate.delete(composeKey(key));
        if (isDeleted != null) {
            return isDeleted;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    public Optional<T> find(@NotNull String key) {
        String composedKey = composeKey(key);
        Object object = redisTemplate.opsForValue().get(composedKey);
        Class<T> type = getType();
        if (object == null || type.isInstance(object)) {
            return Optional.ofNullable(type.cast(object));
        } else {
            throw new ClassCastException(
                    String.format("Under the key %s there is an object that does not match the type %s",
                            composedKey, getType().getSimpleName()));
        }
    }
}
