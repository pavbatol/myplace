package ru.pavbatol.myplace.security.app.redis.repository;

import ru.pavbatol.myplace.security.app.exception.RedisException;

import java.util.Optional;

public interface RedisRepository<T> {

    boolean add(String key, T obj) throws RedisException;

    void set(String key, T obj);

    boolean remove(String key) throws RedisException;

    Optional<T> find(String key);
}
