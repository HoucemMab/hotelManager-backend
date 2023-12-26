package com.example.hotelmanagertool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {
    private final RestTemplate restTemplate;

    @Value("${app.token}") // Load this value from your application.properties or application.yml
    private String appToken;

    @Value("${app.secret}") // Load this value from your application.properties or application.yml
    private String appSecret;

    @Value("${payment.api.url}") // Define the API URL in your properties file
    private String paymentApiUrl;

    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> generatePayment(String amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request body
        String requestBody = "{"
                + "\"app_token\":\"" + appToken + "\","
                + "\"app_secret\":\"" + appSecret + "\","
                + "\"amount\":\""+amount+"\","
                + "\"accept_card\":\"true\","
                + "\"session_timeout_secs\":1200,"
                + "\"success_link\":\"http://localhost:3000/succes-payment\","
                + "\"fail_link\":\"http://localhost:3000/fail-payment\","
                + "\"developer_tracking_id\":\"your_internal_tracking_id\""
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(paymentApiUrl, entity, String.class);
    }
    public ResponseEntity<String> verifyPayment(String paymentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        System.out.println(appToken + " " + appSecret);
        headers.add("apppublic", "51b53832-e682-4d10-bf81-58e0a68cd0ce");
        headers.add("appsecret", appSecret);

        String apiUrl = "https://developers.flouci.com/api/verify_payment/" + paymentId;

        return restTemplate.exchange(apiUrl, HttpMethod.GET,new HttpEntity<>(headers), String.class);
    }
}

