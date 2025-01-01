package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimit implements HandlerInterceptor {

    @Value("${rate-limit.algo}")
    public String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    public int rateLimitRPM;

    private final ConcurrentHashMap<String, Object> clientRequests = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();

        // Skip rate limiting for /internal endpoint
        if ("/internal".equals(request.getRequestURI())) {
            return true;
        }

        boolean isAllowed;
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            isAllowed = isAllowedFixed(clientIp);
        } else {
            isAllowed = isAllowedSliding(clientIp);
        }

        if (!isAllowed) {
            response.setHeader("X-Rate-Limit-Remaining", "0");
            response.setHeader("X-Rate-Limit-Retry-After-Seconds", "60"); // Example value
            response.setStatus(429); // Too Many Requests
            return false;
        }

        response.setHeader("X-Rate-Limit-Remaining", Integer.toString(getRemainingRequests(clientIp)));
        return true;
    }

    public boolean isAllowedFixed(String clientIp) {
        long currentTime = System.currentTimeMillis();
        RateLimitState state = (RateLimitState) clientRequests.computeIfAbsent(clientIp, k -> new RateLimitState());

        synchronized (state) {
            // Reset count if the current window has passed
            if (currentTime - state.lastResetTime >= 60000) {
                state.requestCount = 0;
                state.lastResetTime = currentTime;
            }

            if (state.requestCount < rateLimitRPM) {
                state.requestCount++;
                return true;
            }
            return false;
        }
    }

    public boolean isAllowedSliding(String clientIp) {
        long currentTime = System.currentTimeMillis();
        CircularBuffer buffer = (CircularBuffer) clientRequests.computeIfAbsent(clientIp, k -> new CircularBuffer(rateLimitRPM));

        synchronized (buffer) {
            // Remove requests older than 1 minute
            buffer.removeOldEntries(currentTime - 60000);

            if (buffer.size() < rateLimitRPM) {
                buffer.add(currentTime); // Record the new request
                return true;
            }
            return false;
        }
    }

    public int getRemainingRequests(String clientIp) {
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            RateLimitState state = (RateLimitState) clientRequests.get(clientIp);
            return state == null ? rateLimitRPM : Math.max(0, rateLimitRPM - state.requestCount);
        } else {
            CircularBuffer buffer = (CircularBuffer) clientRequests.get(clientIp);
            return buffer == null ? rateLimitRPM : Math.max(0, rateLimitRPM - buffer.size());
        }
    }

    private static class RateLimitState {
        int requestCount = 0;
        long lastResetTime = System.currentTimeMillis();
    }

    public class CircularBuffer {
        private final long[] buffer;
        private int head;
        private int tail;
        private int size;
        private final int capacity;

        public CircularBuffer(int capacity) {
            this.buffer = new long[capacity];
            this.head = 0;
            this.tail = 0;
            this.size = 0;
            this.capacity = capacity;
        }

        // Add a new timestamp to the buffer
        public void add(long timestamp) {
            buffer[head] = timestamp;
            head = (head + 1) % capacity;
            if (size == capacity) {
                // If buffer is full, the tail moves forward to overwrite the oldest element
                tail = (tail + 1) % capacity;
            } else {
                size++;
            }
        }

        // Remove entries older than the given threshold in O(1)
        public void removeOldEntries(long threshold) {
            // Check if the oldest entry is older than the threshold
            if (size > 0 && buffer[tail] < threshold) {
                // Move the tail pointer forward without iteration
                tail = (tail + 1) % capacity;
                size--;
            }
        }

        public int size() {
            return size;
        }

        public long getOldestTimestamp() {
            return size > 0 ? buffer[tail] : -1;
        }
    }

}
