package ru.pavbatol.myplace.security.auth.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.security.app.redis.RedisKey;
import ru.pavbatol.myplace.security.auth.model.AccessTokenDetails;
import ru.pavbatol.myplace.security.auth.repository.AbstractTokenRedisRepository;
import ru.pavbatol.myplace.security.auth.repository.AccessTokenRedisRepository;

@Slf4j
@Component
public class AccessTokenRedisRepositoryImpl extends AbstractTokenRedisRepository<AccessTokenDetails> implements AccessTokenRedisRepository {

    @Autowired
    protected AccessTokenRedisRepositoryImpl(Environment environment) {
        super(
                RedisKey.ACCESS_TOKEN,
                Long.parseLong(environment.getProperty("app.jwt.access.life-seconds", "300"))
        );
    }

    @Override
    protected Class<AccessTokenDetails> getType() {
        return AccessTokenDetails.class;
    }
}
