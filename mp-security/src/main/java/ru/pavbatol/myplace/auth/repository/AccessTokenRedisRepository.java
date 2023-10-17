package ru.pavbatol.myplace.auth.repository;

import ru.pavbatol.myplace.auth.model.AccessTokenDetails;

public interface AccessTokenRedisRepository extends TokenRedisRepository<AccessTokenDetails> {
    boolean exists(String key);
}
