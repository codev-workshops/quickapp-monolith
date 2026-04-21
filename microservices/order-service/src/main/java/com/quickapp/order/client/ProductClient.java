package com.quickapp.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ProductClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductClient.class);
    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${app.services.product-url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    @SuppressWarnings("unchecked")
    public String getProductName(int productId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    productServiceUrl + "/api/products/" + productId,
                    HttpMethod.GET, entity, Map.class);
            if (response.getBody() != null) {
                return (String) response.getBody().get("name");
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch product name for id {}: {}", productId, e.getMessage());
        }
        return null;
    }
}
