package ru.pavbatol.myplace.auth.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.RedisKey;
import ru.pavbatol.myplace.app.redis.repository.AbstractRedisRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
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
    public void deleteAllByKeyStartsWith(@NotNull String keyStartWith) {
        String pattern = composeKey(keyStartWith) + "*";
        Set<String> keys = scanForKeys(pattern);
        if (!keys.isEmpty()) {
            log.debug("Found {} keys to delete by keyStartWith: {}", keys.size(), keyStartWith);
            redisTemplate.delete(keys);
            log.debug("Deleted all for keyStartWith {}", keyStartWith);
        } else {
            log.debug("Not found keys to delete by keyStartWith: {}", keyStartWith);
        }
    }

    /**
     * The method is intended for use in infrequent operations such as "Get all active sessions",
     * "Log out the user on all devices", and similar tasks. For more frequent operations, it is recommended to make
     * a choice in favor of storing additional copies of keys per user in a Set in the same Redis, so that the original
     * keys remain with the TTL set.
     * @param pattern  key pattern string
     */
    @NotNull
    private Set<String> scanForKeys(String pattern) {
        int count = 1000;
        Set<String> keys = new HashSet<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : "no massage";
            log.error(message, e);
            throw new RedisException("Scanning error", message);
        }

        return keys;
    }
}
