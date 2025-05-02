package ru.pavbatol.myplace.security.auth.repository;

import ru.pavbatol.myplace.security.app.redis.repository.RedisRepository;

public interface TokenRedisRepository<T> extends RedisRepository<T> {

    void removeAllByKeyStartsWith(String keyStartWith);
}
