package org.example;

class TokenBucket {
    double tokens;
    long lastRefillTimestamp;

    TokenBucket(double tokens, long lastRefillTimestamp) {
        this.tokens = tokens;
        this.lastRefillTimestamp = lastRefillTimestamp;
    }
}

