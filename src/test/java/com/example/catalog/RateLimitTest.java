package com.example.catalog;

import com.example.catalog.interceptors.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String API_ENDPOINT = "/";
    private static final String INTERNAL_ENDPOINT = "/internal";
    private static final String XRateLimitRetryAfterSecondsHeader = "X-Rate-Limit-Retry-After-Seconds";
    private static final String XRateLimitRemaining = "X-Rate-Limit-Remaining";
    private RateLimit rateLimit;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        rateLimit = new RateLimit();
        ReflectionTestUtils.setField(rateLimit, "rateLimitRPM", 10); // 5 requests per minute
        ReflectionTestUtils.setField(rateLimit, "rateLimitAlgo", "sliding");
        // Mock HTTP request and response
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }


    @Test
    void testIsAllowedSliding_WithinLimit() {
        String clientIp = "192.168.1.1";

        // Simulate 10 requests within the allowed limit
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedSliding(clientIp), "Request should be allowed");
        }
    }

    @Test
    void testIsAllowedSliding_ExceedLimit() {
        String clientIp = "192.168.1.2";

        // Simulate 10 allowed requests
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedSliding(clientIp), "Request should be allowed");
        }

        // Simulate the 6th request, which should be blocked
        assertFalse(rateLimit.isAllowedSliding(clientIp), "Request should be blocked");
    }

    @Test
    void testIsAllowedSliding_RemoveOldEntries() throws InterruptedException {
        String clientIp = "192.168.1.3";

        // Simulate 5 allowed requests
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedSliding(clientIp), "Request should be allowed");
        }

        // Wait for a little over a minute to ensure old entries are removed
        Thread.sleep(61000);

        // Now another request should be allowed
        assertTrue(rateLimit.isAllowedSliding(clientIp), "Request should be allowed after window reset");
    }

    @Test
    void testIsAllowedSliding_MultipleClients() {
        String clientIp1 = "192.168.1.4";
        String clientIp2 = "192.168.1.5";

        // Simulate 10 requests for client 1
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedSliding(clientIp1), "Request for client 1 should be allowed");
        }

        // Simulate 10 requests for client 2
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedSliding(clientIp2), "Request for client 2 should be allowed");
        }

        // Both should now be blocked on the 6th request
        assertFalse(rateLimit.isAllowedSliding(clientIp1), "Request for client 1 should be blocked");
        assertFalse(rateLimit.isAllowedSliding(clientIp2), "Request for client 2 should be blocked");
    }

    @Test
    public void testRateLimiterEnforcesLimits() throws InterruptedException {
        int allowedRequests = 10;
        int extraRequests = 5;

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)), "Expected status code to be 200 for the first 10 requests");

            String remainingRequests = String.valueOf(allowedRequests - (i + 1));
            assertEquals(remainingRequests, response.getHeaders().get(XRateLimitRemaining).get(0), "Expected " + XRateLimitRemaining + " header to be " + remainingRequests + " after " + i + 1 + " requests");
        }

        for (int i = 0; i < extraRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(429)));
            int retryAfter = Integer.parseInt(response.getHeaders().get(XRateLimitRetryAfterSecondsHeader).get(0));
            assertTrue(retryAfter > 0);
        }
    }

    @Test
    public void testRateLimiterBypassesInternalEndpoint() {
        int totalRequests = 15;

        for (int i = 0; i < totalRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(INTERNAL_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)));
            assertFalse(response.getHeaders().containsKey(XRateLimitRemaining));
        }
    }

    @Test
    public void testPreHandleWithFixedRateLimitExceeded() throws Exception {
        // Simulate a client IP and excessive requests
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");
        when(request.getRequestURI()).thenReturn("/someEndpoint");

        // Simulate that the client has made 10 requests
        for (int i = 0; i < 10; i++) {
            rateLimit.preHandle(request, response, new Object());
        }

        // The next request should exceed the rate limit
        boolean result = rateLimit.preHandle(request, response, new Object());

        // Verify the response for rate-limited client
        assertFalse(result);

    }

    @Test
    public void testPreHandleWithFixedRateLimitAllowed() throws Exception {
        // Simulate a client IP and allowed requests
        when(request.getRemoteAddr()).thenReturn("192.168.1.2");
        when(request.getRequestURI()).thenReturn("/someEndpoint");

        // Simulate that the client has made fewer than 10 requests
        for (int i = 0; i < 5; i++) {
            rateLimit.preHandle(request, response, new Object());
        }


    }
    @Test
    public void testWithinRateLimit() {
        String clientIp = "192.168.1.1";

        // Simulate requests within the limit
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedFixed(clientIp), "Request should be allowed within the limit");
        }
    }
    @Test
    public void testExceedingRateLimit() {
        String clientIp = "192.168.1.1";

        // Simulate requests exceeding the limit
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedFixed(clientIp), "Request should be allowed within the limit");
        }
        assertFalse(rateLimit.isAllowedFixed(clientIp), "Request should not be allowed after exceeding the limit");
    }
    @Test
    public void testRateLimitResetAfterTimeWindow() throws InterruptedException {
        String clientIp = "192.168.1.1";

        // Simulate requests within the limit
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.isAllowedFixed(clientIp), "Request should be allowed within the limit");
        }
        assertFalse(rateLimit.isAllowedFixed(clientIp), "Request should not be allowed after exceeding the limit");

        // Simulate waiting for the time window to pass (more than 60 seconds)
        Thread.sleep(61000);

        // After reset, requests should be allowed again
        assertTrue(rateLimit.isAllowedFixed(clientIp), "Request should be allowed after time window reset");
    }
    @Test
    public void testGetRemainingRequestsFixedAlgo_NewClient() {
        rateLimit.rateLimitAlgo = "fixed";
        String clientIp = "192.168.1.1";

        // New client should have full remaining requests
        int remaining = rateLimit.getRemainingRequests(clientIp);
        assertEquals(10, remaining, "Remaining requests should be equal to rateLimitRPM for new clients");
    }



    @Test
    public void testGetRemainingRequestsSlidingAlgo_NewClient() {
        rateLimit.rateLimitAlgo = "sliding";
        String clientIp = "192.168.1.1";

        // New client should have full remaining requests
        int remaining = rateLimit.getRemainingRequests(clientIp);
        assertEquals(10, remaining, "Remaining requests should be equal to rateLimitRPM for new clients");
    }

    @Test
    public void testGetRemainingRequestsSlidingAlgo_ExistingClient() {
        rateLimit.rateLimitAlgo = "sliding";
        String clientIp = "192.168.1.1";

        // Simulate some requests
        rateLimit.isAllowedSliding(clientIp);
        rateLimit.isAllowedSliding(clientIp);

        // Remaining requests should decrease
        int remaining = rateLimit.getRemainingRequests(clientIp);
        assertEquals(8, remaining, "Remaining requests should reflect the number of requests already made");
    }

    @Test
    public void testGetRemainingRequestsNoRequests() {
        rateLimit.rateLimitAlgo = "fixed";
        String clientIp = "192.168.1.1";

        // No requests made yet
        int remaining = rateLimit.getRemainingRequests(clientIp);
        assertEquals(rateLimit.rateLimitRPM, remaining, "Remaining requests should equal rateLimitRPM for a new client");
    }


}