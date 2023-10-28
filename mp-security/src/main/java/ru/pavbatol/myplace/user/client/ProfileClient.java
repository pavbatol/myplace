package ru.pavbatol.myplace.user.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.app.exception.ExternalServerException;

import java.util.List;

@Slf4j
@Component
public class ProfileClient {
    private static final String PROFILE_PATH = "/profiles";
    private final RestTemplate restTemplate;

    @Autowired
    public ProfileClient(RestTemplateBuilder builder, @Value("${app.profile-server-url}") String profileServerUrl) {
        assert profileServerUrl != null : "The 'profileServerUrl' variable is not set";
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(profileServerUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void createProfile(Long userId, String email) throws RuntimeException {
        log.debug("Trying to send request for creating profile");
        ProfileDtoCreate profileDto = new ProfileDtoCreate(userId, email);
        HttpEntity<ProfileDtoCreate> httpEntity = new HttpEntity<>(profileDto, defaultHeaders());

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build(); // TODO: 05.10.2023 Remove mocking when profile service is done
//        ResponseEntity<Object> response = restTemplate.exchange(
//                PROFILE_PATH,
//                HttpMethod.POST,
//                httpEntity,
//                Object.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new ExternalServerException("Failed to create Profile, HttpStatus: " + response.getStatusCodeValue());
        }
    }

    public boolean existsByEmail(String email) throws RuntimeException {
        log.debug("Trying to send request for checking email for existing");
        HttpEntity<Object> httpEntity = new HttpEntity<>(defaultHeaders());
        String path = PROFILE_PATH + "/check-email?email={email}";

        ResponseEntity<Boolean> response = ResponseEntity.ok(false); // TODO: 05.10.2023 Remove mocking when profile service is done
//        ResponseEntity<Boolean> response = restTemplate.exchange(
//                path,
//                HttpMethod.GET,
//                httpEntity,
//                Boolean.class,
//                email);

        if (response.getStatusCode() == HttpStatus.OK) {
            Boolean exists = response.getBody();
            if (exists != null) {
                return exists;
            } else {
                throw new ExternalServerException("Server returned null value for email existence");
            }
        } else {
            throw new ExternalServerException("Server returned non-OK status: " + response.getStatusCodeValue());
        }
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
