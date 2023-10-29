package ru.pavbatol.myplace.auth.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.app.redis.RedisKey;
import ru.pavbatol.myplace.app.redis.repository.AbstractRedisRepository;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class AbstractTokenRedisRepository<T> extends AbstractRedisRepository<T> implements TokenRedisRepository<T> {

    protected AbstractTokenRedisRepository(RedisKey redisKey, long ttl) {
        super(redisKey, ttl);
    }

    @Override
    public void removeAllByKeyStartsWith(@NotNull String keyStartWith) {
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
     *
     * @param pattern key pattern string
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
            String message = e.getMessage() != null ? e.getMessage() : "No massage.";
            log.error(message, e);
            throw new RedisException("Scanning error.", message);
        }

        return keys;
    }
}
