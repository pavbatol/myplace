package ru.pavbatol.myplace.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import ru.pavbatol.myplace.app.config.RedisKey;
import ru.pavbatol.myplace.app.exception.RedisException;

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

    abstract Class<T> getType();

    protected String composeKey(String key) {
        return redisKey.getKey() + ":" + key;
    }

    @Override
    public boolean create(@NotNull String key, @NotNull T obj) throws RedisException {
        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(composeKey(key), obj, ttl, TimeUnit.SECONDS);
        if (isSet != null) {
            return isSet;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    public void createOrUpdate(@NotNull String key, @NotNull T obj) {
        redisTemplate.opsForValue().set(composeKey(key), obj, ttl, TimeUnit.SECONDS);
    }

    @Override
    public boolean delete(@NotNull String key) throws RedisException {
        Boolean isDeleted = redisTemplate.delete(composeKey(key));
        if (isDeleted != null) {
            return isDeleted;
        } else {
            throw new RedisException("Unknown result: DB returned null");
        }
    }

    @Override
    public Optional<Boolean> deleteWithoutException(@NotNull String key) {
        try {
            return Optional.ofNullable(redisTemplate.delete(composeKey(key)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> find(@NotNull String key) {
        Object object = redisTemplate.opsForValue().get(composeKey(key));
        Class<T> type = getType();
        if (object == null || type.isInstance(object)) {
            return Optional.ofNullable(type.cast(object));
        } else {
            throw new ClassCastException(
                    String.format("Under the hashKey %s there is an object that does not match the type %s",
                            composeKey(key), getType().getSimpleName()));
        }
    }
}
