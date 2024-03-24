package ru.pavbatol.myplace.auth.repository;

import ru.pavbatol.myplace.app.redis.repository.RedisRepository;

public interface TokenRedisRepository<T> extends RedisRepository<T> {

    void removeAllByKeyStartsWith(String keyStartWith);
}
