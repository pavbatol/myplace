package ru.pavbatol.myplace.user.repository;

import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.repository.RedisRepository;
import ru.pavbatol.myplace.user.model.UserUnverified;

public interface UnverifiedUserRedisRepository extends RedisRepository<UserUnverified> {
    boolean addByLoginKey(String login, String anyValue) throws RedisException;

    void addByAtomicLoginAndEmailKeys(UserUnverified value) throws RedisException;

    void removeLoginKeySilently(String login);

    void removeSilently(String key);
}
