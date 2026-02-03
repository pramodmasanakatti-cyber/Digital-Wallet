package com.digitalwallet.controller;

import com.digitalwallet.client.UserClientImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
private final UserClientImpl userClientImpl;

    public HealthController(UserClientImpl userClientImpl) {
        this.userClientImpl = userClientImpl;
    }

    @GetMapping("/health/{id}")
    public String healthCheck(@PathVariable Integer id) {
       userClientImpl.validateUser(id);
       return "User exist";
    }
}
