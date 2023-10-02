package ru.pavbatol.myplace.user.repository;

import java.util.Optional;

public interface UserRedisRepository<T> {
    boolean save(String hashKey, T obj);

    boolean remove(String hashKey);

    Optional<T> findByHashKey(String hashKey);
}
