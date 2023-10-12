package ru.pavbatol.myplace.app.redis.repository;

import ru.pavbatol.myplace.app.exception.RedisException;

import java.util.Optional;

public interface RedisRepository<T> {

    boolean create(String key, T obj) throws RedisException;

    void createOrUpdate(String key, T obj);

    boolean delete(String key) throws RedisException;

    Optional<T> find(String key);
}
