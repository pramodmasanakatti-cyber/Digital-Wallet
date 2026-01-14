package com.digitalwallet.controller;

import com.digitalwallet.client.UserClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
private final UserClient userClient;

    public HealthController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/health/{id}")
    public String healthCheck(@PathVariable Integer id) {
       userClient.validateUser(id);
       return "User exist";
    }
}
