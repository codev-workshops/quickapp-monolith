package com.quickapp.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CustomerClient {

    private static final Logger logger = LoggerFactory.getLogger(CustomerClient.class);
    private final RestTemplate restTemplate;
    private final String customerServiceUrl;

    public CustomerClient(RestTemplate restTemplate,
                          @Value("${app.services.customer-url}") String customerServiceUrl) {
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
    }

    @SuppressWarnings("unchecked")
    public String getCustomerName(int customerId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    customerServiceUrl + "/api/customers/" + customerId,
                    HttpMethod.GET, entity, Map.class);
            if (response.getBody() != null) {
                return (String) response.getBody().get("name");
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch customer name for id {}: {}", customerId, e.getMessage());
        }
        return null;
    }
}
