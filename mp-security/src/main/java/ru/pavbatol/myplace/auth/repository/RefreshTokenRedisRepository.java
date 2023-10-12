package ru.pavbatol.myplace.auth.repository;

import ru.pavbatol.myplace.app.redis.repository.RedisRepository;

public interface RefreshTokenRedisRepository extends RedisRepository<String> {

    void deleteAllByKey(String key);
}
