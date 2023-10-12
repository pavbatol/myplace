package ru.pavbatol.myplace.auth.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.redis.RedisKey;
import ru.pavbatol.myplace.app.redis.repository.AbstractRedisRepository;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Slf4j
@Component
public class RefreshTokenRedisRepositoryImpl extends AbstractRedisRepository<String> implements RefreshTokenRedisRepository {
    @Autowired
    protected RefreshTokenRedisRepositoryImpl(Environment environment) {
        super(
                RedisKey.REFRESH_TOKEN,
                Long.parseLong(environment.getProperty("app.jwt.refresh.life-seconds", "600"))
        );
    }

    @Override
    protected Class<String> getType() {
        return String.class;
    }

    @Override
    public void deleteAllByKey(@NotNull String key) {
        String pattern = composeKey(key);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            log.debug("Found {} keys to delete by key: {}", keys.size(), key);
            redisTemplate.delete(keys);
            log.debug("{}: Deleted all for key {}", getClass().getSimpleName(), key);
        } else {
            log.debug("Not found keys to delete by key: {}", key);
        }
    }
}
