package ru.pavbatol.myplace.user.repository;

import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.repository.RedisRepository;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

public interface UnverifiedUserRedisRepository extends RedisRepository<UserDtoUnverified> {
    boolean createLogin(String login, String email) throws RedisException;

    void createAtomicLoginAndEmailKeys(String login, String email, UserDtoUnverified unverifiedUser) throws RedisException;

    void deleteLoginSilently(String login);

    void deleteSilently(String key);
}
