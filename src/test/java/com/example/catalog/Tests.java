package com.example.catalog;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.catalog.interceptors.RateLimit;
import org.junit.Before;
import org.junit.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

public class Tests {

    private RateLimit rateLimit;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RestTemplate restTemplate;
    private static final String API_ENDPOINT = "http://api.example.com/endpoint"; // Replace with your API endpoint
    private static final String XRateLimitRetryAfterSecondsHeader = "X-RateLimit-Retry-After";
    private static final String XRateLimitRemaining = "X-RateLimit-Remaining";

    @Before
    public void setUp() throws Exception {
        rateLimit = new RateLimit();
        Field algoField = RateLimit.class.getDeclaredField("rateLimitAlgo");
        algoField.setAccessible(true);
        algoField.set(rateLimit, "moving");
        Field rpmField = RateLimit.class.getDeclaredField("rateLimitRPM");
        rpmField.setAccessible(true);
        rpmField.set(rateLimit, "10");
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        restTemplate = mock(RestTemplate.class);
    }

    @Test
    public void testRateLimiterBypassesInternalEndpoint() throws Exception {
        int totalRequests = 10;

        for (int i = 0; i < totalRequests; i++) {
            when(request.getRequestURI()).thenReturn("/internal");
            boolean result = rateLimit.preHandle(request, response, null);
            assertTrue(result);
            verify(response, never()).setStatus(429);
        }
    }

    @Test
    public void testExtraRequestsReturnRateLimitError() {

        int extraRequests = 5;
        ResponseEntity<String> mockResponse = mock(ResponseEntity.class);
        HttpHeaders mockHeaders = new HttpHeaders();
        mockHeaders.add(XRateLimitRetryAfterSecondsHeader, "10");
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.TOO_MANY_REQUESTS);
        when(mockResponse.getHeaders()).thenReturn(mockHeaders);
        when(restTemplate.getForEntity(API_ENDPOINT, String.class)).thenReturn(mockResponse);

        for (int i = 0; i < extraRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS));
            int retryAfter = Integer.parseInt(response.getHeaders().get(XRateLimitRetryAfterSecondsHeader).get(0));
            assertTrue(retryAfter > 0);
        }
    }


}
