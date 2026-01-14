package com.digitalwallet.client;

import com.digitalwallet.exception.UserNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class UserClient {
    public UserClient(RestClient restClient, @Value("${user-service.base-url}") String baseUrl) {
        this.restClient = restClient.mutate()
                .baseUrl(baseUrl)
                .build();
    }

    private final RestClient restClient;

    public void validateUser(Integer userId) {
        try {
             restClient.get()
                     .uri("/api/users/{id}",userId)
                     .retrieve()
                     .toBodilessEntity();
        } catch (HttpClientErrorException exception) {
            throw new UserNotFoundException("User not found for the id: " + userId);
        }

    }

}
