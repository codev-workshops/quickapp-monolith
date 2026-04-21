package com.quickapp.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AuthClient {

    private static final Logger logger = LoggerFactory.getLogger(AuthClient.class);
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${app.services.auth-url:http://localhost:8081}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    @SuppressWarnings("unchecked")
    public String getUserFullName(String cashierId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List> usersResponse = restTemplate.exchange(
                    authServiceUrl + "/api/auth/users",
                    HttpMethod.GET, entity, List.class);
            if (usersResponse.getBody() != null) {
                for (Object obj : usersResponse.getBody()) {
                    Map<String, Object> user = (Map<String, Object>) obj;
                    String id = (String) user.get("id");
                    String userName = (String) user.get("userName");
                    if (cashierId.equals(id) || cashierId.equals(userName)) {
                        return (String) user.get("fullName");
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch user name for cashier {}: {}", cashierId, e.getMessage());
        }
        return null;
    }
}
