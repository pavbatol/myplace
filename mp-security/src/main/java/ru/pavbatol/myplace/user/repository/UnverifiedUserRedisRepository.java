package ru.pavbatol.myplace.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.config.RedisKeys;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

import java.util.Optional;

@Component
public class UnverifiedUserRedisRepository extends AbstractRedisRepository<UserDtoUnverified> {

    @Autowired
    protected UnverifiedUserRedisRepository(Environment environment) {
        super(
                RedisKeys.USERS_UNVERIFIED,
                Long.parseLong(environment.getProperty("redis.ttl-sec-unverified", "180"))
        );
    }

    @Override
    public Class<UserDtoUnverified> getType() {
        return UserDtoUnverified.class;
    }

    public boolean save(String key, UserDtoUnverified dtoUnverified) {
        return create(key, dtoUnverified);
    }

    public void remove(String key) {
        delete(key);
    }

    public Optional<UserDtoUnverified> findByHashKey(String email) {
        return find(email);
    }
}
