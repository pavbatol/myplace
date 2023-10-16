package ru.pavbatol.myplace.auth.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
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
    public void deleteAllByKeyStartWith(@NotNull String keyStartWith) {
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

    @NotNull
    private Set<String> scanForKeys(String pattern) {
        int count = 1000;
        Set<String> keys = new HashSet<>();
        RedisSerializer<?> serializer = redisTemplate.getKeySerializer();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();

        try (Cursor<byte[]> cursor = redisTemplate.execute(connection -> connection.scan(scanOptions), true);) {
            if (cursor == null) {
                throw new RedisException("Failed obtaining Cursor<byte[]>");
            }
            while (cursor.hasNext()) {
                keys.add(String.valueOf(serializer.deserialize(cursor.next())));
            }
        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : "no massage";
            log.error(message, e);
            throw new RedisException("Scanning error", message);
        }

        return keys;
    }
}
