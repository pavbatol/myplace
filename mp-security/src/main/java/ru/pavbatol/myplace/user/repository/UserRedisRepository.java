package ru.pavbatol.myplace.user.repository;

import ru.pavbatol.myplace.app.exception.RedisException;

import java.util.Optional;

public interface UserRedisRepository<T> {
    boolean save(String hashKey, T obj) throws RedisException;

    Optional<Boolean> remove(String hashKey);

    Optional<T> findByHashKey(String hashKey);
}
