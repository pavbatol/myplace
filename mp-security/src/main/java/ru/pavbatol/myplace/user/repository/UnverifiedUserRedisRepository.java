package ru.pavbatol.myplace.user.repository;

import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.repository.RedisRepository;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

import javax.validation.constraints.NotNull;

public interface UnverifiedUserRedisRepository extends RedisRepository<UserDtoUnverified> {
    boolean createLogin(@NotNull String login, @NotNull String email) throws RedisException;

    void deleteLoginSilently(@NotNull String login);

    void deleteSilently(@NotNull String key);
}
