package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class RateLimit implements HandlerInterceptor {

    @Value("${rate-limit.algo}")
    private String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    private String rateLimitRPM;

    private ConcurrentHashMap<String, Object> requestMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/internal")) {
            return true;
        }
        if (!isAllowed(clientIp)) {
            response.setStatus(429);
            response.setHeader("X-Rate-Limit-Remaining", "0");
            response.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(getRetryAfterSeconds(clientIp)));
            return false;
        }

        response.setStatus(200);
        response.setHeader("X-Rate-Limit-Remaining", String.valueOf(getNumberOfRemainingRequests(clientIp)));
        return true;
    }


    private boolean isAllowed(String clientIp) {
        if ("moving".equalsIgnoreCase(rateLimitAlgo)) {
            return isAllowedSliding(clientIp);
        } else if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            return isAllowedFixed(clientIp);
        } else {
            throw new IllegalArgumentException("Unknown rate limiting algorithm: " + rateLimitAlgo);
        }
    }

    private synchronized boolean isAllowedSliding(String clientIp) {
        long currTime = System.currentTimeMillis();
        long timeWindow = 60000;
        int maxRequests = Integer.parseInt(rateLimitRPM);

        requestMap.putIfAbsent(clientIp, new LinkedList<Long>());
        LinkedList<Long> timestamps = (LinkedList<Long>) requestMap.get(clientIp);

        while (!timestamps.isEmpty() && currTime - timestamps.getFirst() > timeWindow) {
            timestamps.removeFirst();
        }

        timestamps.addLast(currTime);
        if (timestamps.size() <= maxRequests) {
            return true;
        }

        return false;
    }



    private synchronized boolean isAllowedFixed(String clientIp){
        long currTime = System.currentTimeMillis();
        long minMills = 60000;
        requestMap.putIfAbsent(clientIp, new FixedInterval(currTime,0));
        FixedInterval fixedInterval = (FixedInterval) requestMap.get(clientIp);

        if(currTime - fixedInterval.startTime >= minMills){
            fixedInterval.requestCount = 1;
            fixedInterval.startTime = currTime;
            return true;
        }
        else{
            if(fixedInterval.requestCount < Integer.parseInt(rateLimitRPM)){
                fixedInterval.requestCount++;
                return true;
            }
            else
                return false;
        }
    }

    private int getNumberOfRemainingRequests(String clientIp) {
        if ("moving".equalsIgnoreCase(rateLimitAlgo)) {
            return calculateRemainingSliding(clientIp);
        } else if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            return calculateRemainingFixed(clientIp);
        } else {
            throw new IllegalArgumentException("Unknown rate limiting algorithm: " + rateLimitAlgo);
        }
    }

    private int calculateRemainingFixed(String clientIp) {
        int maxRequests = Integer.parseInt(rateLimitRPM);

        if (!requestMap.containsKey(clientIp)) {
            return maxRequests;
        }

        FixedInterval fixedInterval = (FixedInterval) requestMap.get(clientIp);
        long currTime = System.currentTimeMillis();
        long windowDuration = 60000;

        if (currTime - fixedInterval.startTime >= windowDuration) {
            return maxRequests;
        }

        return Math.max(0, maxRequests - fixedInterval.requestCount);
    }




    private int calculateRemainingSliding(String clientIp) {
        long currTime = System.currentTimeMillis();
        long timeWindow = 60000; // 1-minute window
        int maxRequests = Integer.parseInt(rateLimitRPM);

        // If the client IP does not exist, they have not made any requests
        if (!requestMap.containsKey(clientIp)) {
            return maxRequests;
        }

        // Retrieve the existing list of timestamps
        LinkedList<Long> timestamps = (LinkedList<Long>) requestMap.get(clientIp);

        // Remove expired timestamps (but don't clear the list entirely)
        // timestamps.removeIf(timestamp -> currTime - timestamp > timeWindow);

        // Calculate the number of used requests
        int usedRequests = timestamps.size();

        // Return the number of remaining requests
        return Math.max(0, maxRequests - usedRequests);
    }





    private long getRetryAfterSeconds(String clientIp) {
        if ("moving".equalsIgnoreCase(rateLimitAlgo)) {
            return getRetryAfterSliding(clientIp);
        } else if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            return getRetryAfterFixed(clientIp);
        } else {
            throw new IllegalArgumentException("Unknown rate limiting algorithm: " + rateLimitAlgo);
        }
    }

    private long getRetryAfterSliding(String clientIp) {
        long currTime = System.currentTimeMillis();
        long timeWindow = 60000;
        int maxRequests = Integer.parseInt(rateLimitRPM);

        if (!requestMap.containsKey(clientIp)) {
            return 0;
        }

        LinkedList<Long> timestamps = (LinkedList<Long>) requestMap.get(clientIp);

        if (timestamps.size() >= maxRequests) {
            long timeLeftInWindow = timeWindow - (currTime - timestamps.getFirst());
            return Math.max(0, timeLeftInWindow / 60000); // Convert to seconds
        }

        return 0;
    }


    private long getRetryAfterFixed(String clientIp) {
        FixedInterval fixedInterval = (FixedInterval) requestMap.get(clientIp);
        if (fixedInterval == null) return 0;
        long currTime = System.currentTimeMillis();
        long minMills = 60000;

        return Math.max(0,(fixedInterval.startTime + minMills - currTime)/1000);

    }

    private static class FixedInterval{
        long startTime;
        int requestCount;

        public FixedInterval(long startTime, int requestCount){
            this.startTime = startTime;
            this.requestCount = requestCount;
        }
    }
    @Configuration
    public class RequestInterceptorConfig implements WebMvcConfigurer {

        @Value("${rate-limit.enabled}")
        private String rateLimitEnabled;

        private final RateLimit rateLimit;

        public RequestInterceptorConfig(RateLimit rateLimit) {
            this.rateLimit = rateLimit;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            if (Objects.equals(rateLimitEnabled, "true")) {
                registry.addInterceptor(rateLimit);
            }
        }
    }

}