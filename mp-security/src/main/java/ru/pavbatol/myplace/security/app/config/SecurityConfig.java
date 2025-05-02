package ru.pavbatol.myplace.security.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Qualifier("JWtFilter")
    private final Filter jWtFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    private String[] publicPaths;
    private String[] adminPaths;
    private String[] userPaths;

    @PostConstruct
    protected void init() {
        publicPaths = new String[]{
                "/auth/**",
                apiPrefix + "/auth/**",
                "/v3/api-docs.yaml",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/docs/**",
                "/h2-console/**"
        };

        adminPaths = new String[]{
                "/admin/**",
                apiPrefix + "/admin/**"
        };

        userPaths = new String[]{
                "/users/**",
                apiPrefix + "/users/**"
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", getCorsConfiguration());
        return httpSecurity
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .antMatchers(adminPaths).hasRole("ADMIN")
                                .antMatchers(userPaths).hasRole("USER")
                                .antMatchers(publicPaths).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jWtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private CorsConfiguration getCorsConfiguration() {
        List<String> strings = List.of(allowedOrigins.split(","));
        List<String> methods = List.of(allowedMethods.split(","));
        List<String> headers = List.of(
                "Content-Type",
                "Authorization",
                "Origin",
                "Accept",
                "Cache-Control",
                "X-User-Uuid",
                "X-User-Id"
        );

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(strings);
        corsConfig.setAllowedHeaders(headers);
        corsConfig.setAllowedMethods(methods);

        return corsConfig;
    }
}
