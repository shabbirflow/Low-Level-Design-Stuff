package org.example;

public class RateLimiter {

    private final RateLimitPolicy policy;

    public RateLimiter(RateLimitPolicy policy) {
        this.policy = policy;
    }

    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        return policy.allowRequest(key, now);
    }
}

