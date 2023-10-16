package ru.pavbatol.myplace.user.repository;

import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.repository.RedisRepository;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

public interface UnverifiedUserRedisRepository extends RedisRepository<UserDtoUnverified> {
    boolean addLogin(String login, String email) throws RedisException;

    void addAtomicLoginAndEmailKeys(String login, String email, UserDtoUnverified unverifiedUser) throws RedisException;

    void removeLoginSilently(String login);

    void removeSilently(String key);
}
