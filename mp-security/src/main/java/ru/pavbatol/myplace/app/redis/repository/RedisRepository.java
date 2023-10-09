package ru.pavbatol.myplace.app.redis.repository;

import ru.pavbatol.myplace.app.exception.RedisException;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface RedisRepository<T> {

    boolean create(@NotNull String key, @NotNull T obj) throws RedisException;

    void createOrUpdate(@NotNull String key, @NotNull T obj);

    boolean delete(@NotNull String key) throws RedisException;

    Optional<T> find(@NotNull String key);
}
