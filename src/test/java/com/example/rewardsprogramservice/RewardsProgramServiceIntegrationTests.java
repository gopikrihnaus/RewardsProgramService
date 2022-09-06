package com.example.rewardsprogramservice;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.entity.Purchase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RewardsProgramServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardsProgramServiceIntegrationTests {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    private static final String API_PREFIX = "/api/v1";

    @Test
    public void testGetRewardsFromDB() {
        ResponseEntity<Map<String, Integer>> responseEntity =
                restTemplate.exchange(createURLWithPort(API_PREFIX + "/rewards/1/lastThreeMonthRewards"),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<Map<String, Integer>>() {
                        });
        Map<String, Integer> body = responseEntity.getBody();

        assertTrue(body.size() == 4);
    }

    @Test
    public void testGetCustomersFromDB() {
        ResponseEntity<List<Customer>> responseEntity =
                restTemplate.exchange(createURLWithPort(API_PREFIX + "/customer/"),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Customer>>() {
                        });

        List<Customer> body = responseEntity.getBody();

        assertTrue(body.size() == 4);
    }

    @Test
    public void testGetPurchasesFromDB() {
        ResponseEntity<List<Purchase>> responseEntity =
                restTemplate.exchange(createURLWithPort(API_PREFIX + "/customer/"),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Purchase>>() {
                        });

        List<Purchase> body = responseEntity.getBody();

        assertTrue(body.size() == 4);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}