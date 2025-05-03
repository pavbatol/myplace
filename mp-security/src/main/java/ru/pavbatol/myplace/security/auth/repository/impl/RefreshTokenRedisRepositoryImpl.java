package ru.pavbatol.myplace.security.auth.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.security.app.redis.RedisKey;
import ru.pavbatol.myplace.security.auth.repository.AbstractTokenRedisRepository;
import ru.pavbatol.myplace.security.auth.repository.RefreshTokenRedisRepository;

@Slf4j
@Component
public class RefreshTokenRedisRepositoryImpl extends AbstractTokenRedisRepository<String> implements RefreshTokenRedisRepository {
    @Autowired
    protected RefreshTokenRedisRepositoryImpl(Environment environment) {
        super(
                RedisKey.REFRESH_TOKEN,
                Long.parseLong(environment.getProperty("app.jwt.refresh.life-seconds", "3600"))
        );
    }

    @Override
    protected Class<String> getType() {
        return String.class;
    }
}
