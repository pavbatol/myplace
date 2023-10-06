package ru.pavbatol.myplace.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.config.RedisKey;
import ru.pavbatol.myplace.user.dto.UserDtoUnverified;

@Component
public class UnverifiedUserRedisRepository extends AbstractRedisRepository<UserDtoUnverified> {

    @Autowired
    protected UnverifiedUserRedisRepository(Environment environment) {
        super(
                RedisKey.USERS_UNVERIFIED,
                Long.parseLong(environment.getProperty("redis.ttl-sec-unverified", "180"))
        );
    }

    @Override
    protected Class<UserDtoUnverified> getType() {
        return UserDtoUnverified.class;
    }
}
