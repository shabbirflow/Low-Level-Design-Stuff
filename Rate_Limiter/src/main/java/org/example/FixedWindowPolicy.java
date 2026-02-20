package org.example;

import java.util.HashMap;
import java.util.Map;

class FixedWindowPolicy implements RateLimitPolicy {

    private final int maxRequests;
    private final long windowSizeMillis;
    private final Map<String, WindowCounter> counters = new HashMap<>();

    public FixedWindowPolicy(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    @Override
    public boolean allowRequest(String key, long now) {
        long windowStart = now - (now % windowSizeMillis);

        WindowCounter counter = counters.get(key);
        if (counter == null || counter.windowStart != windowStart) {
            counters.put(key, new WindowCounter(windowStart, 1));
            return true;
        }

        if (counter.count < maxRequests) {
            counter.count++;
            return true;
        }

        return false;
    }
}
