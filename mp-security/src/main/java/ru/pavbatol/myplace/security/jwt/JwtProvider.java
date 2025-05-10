package ru.pavbatol.myplace.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.security.role.model.Role;
import ru.pavbatol.myplace.security.role.model.RoleName;
import ru.pavbatol.myplace.security.user.model.User;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {
    private static final String ROLES_CLAIM = "roles";
    private static final String UUID_CLAIM = "uuid";
    private final SecretKey jwtAccessKey;
    private final SecretKey jwtRefreshKey;
    private final long jwtAccessLifeSeconds;
    private final long jwtRefreshLifeSeconds;

    @Autowired
    public JwtProvider(@Value("${app.jwt.access.key:}") String accessSecretStr,
                       @Value("${app.jwt.refresh.key:}") String refreshSecretStr,
                       @Value("${app.jwt.access.life-seconds}") long jwtAccessLifeSeconds,
                       @Value("${app.jwt.refresh.life-seconds}") long jwtRefreshLifeSeconds) {
        this.jwtAccessKey = accessSecretStr != null && !accessSecretStr.isEmpty()
                ? Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretStr))
                : Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtRefreshKey = refreshSecretStr != null && !refreshSecretStr.isEmpty()
                ? Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretStr))
                : Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtAccessLifeSeconds = jwtAccessLifeSeconds;
        this.jwtRefreshLifeSeconds = jwtRefreshLifeSeconds;

        log.info("Constructor: jwtAccessLifeSeconds: {}", this.jwtAccessLifeSeconds);
        log.info("Constructor: jwtRefreshLifeSeconds: {}", this.jwtRefreshLifeSeconds);
    }

    public String createAccessToken(User user) {
        final Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .map(RoleName::name)
                .collect(Collectors.toSet());
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtAccessLifeSeconds * 1000L);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setSubject(user.getLogin())
                .claim(ROLES_CLAIM, roles)
                .claim(UUID_CLAIM, user.getUuid())
                .signWith(jwtAccessKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(User user) {
        final Instant refreshExpirationInstant = LocalDateTime.now().plusSeconds(jwtRefreshLifeSeconds)
                .atZone(ZoneId.systemDefault()).toInstant();
        final Date expiryDate = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(expiryDate)
                .setSubject(user.getLogin())
                .signWith(jwtRefreshKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Optional<String> resolveToken(@NonNull HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        return resolveToken(bearerToken);
    }

    public Optional<String> resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshKey);
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessKey);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshKey);
    }

    public String getRefreshTokenUsername(String token) {
        return getRefreshClaims(token).getSubject();
    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(@NonNull String token, @NonNull SecretKey secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            log.debug("Token validation successful!");
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("There is an error with the signature of you token ");
        }
        return false;
    }

    public List<String> extractRoles(String token) {
        Claims claims = getAccessClaims(token);

        log.debug("Extracting roles from token. Claims: {}", claims);
        Object rolesClaim = claims.get(ROLES_CLAIM);

        if (rolesClaim instanceof List) {
            return ((List<?>) rolesClaim).stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        if (rolesClaim instanceof String) {
            return Arrays.asList(((String) rolesClaim).split(",\\s*"));
        }

        return Collections.emptyList();
    }
}
