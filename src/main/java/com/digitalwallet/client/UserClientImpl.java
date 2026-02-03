package com.digitalwallet.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class UserClientImpl implements UserClient{
    public UserClientImpl(RestClient restClient, @Value("${user-service.base-url}") String baseUrl) {

        this.restClient = restClient.mutate()
                .baseUrl(baseUrl)
                .build();

    }

    private final RestClient restClient;

    public void validateUser(Integer userId) {
        try {
             restClient.get()
                     .uri("/api/users/internal/{id}",userId)
                     .retrieve()
                     .toBodilessEntity();
        } catch (HttpClientErrorException exception) {
            throw exception;
        }
    }

}
