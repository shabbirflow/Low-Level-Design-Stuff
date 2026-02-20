package org.example;

interface RateLimitPolicy {
    boolean allowRequest(String key, long currentTimeMillis);
}
