package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Fixed Window Rate Limiter ===");
        RateLimiter fixedWindowLimiter = new RateLimiter(
                new FixedWindowPolicy(3, 5000) // 3 requests per 5 seconds
        );

        String key = "user-1";

        for (int i = 1; i <= 5; i++) {
            System.out.println("Request " + i + ": " +
                    fixedWindowLimiter.allowRequest(key));
        }

        Thread.sleep(5000); // wait for window reset

        System.out.println("After window reset:");
        System.out.println("Request 6: " +
                fixedWindowLimiter.allowRequest(key));

        System.out.println("\n=== Token Bucket Rate Limiter ===");
        RateLimiter tokenBucketLimiter = new RateLimiter(
                new TokenBucketPolicy(3, 1.0) // capacity 3, refill 1 token/sec
        );

        for (int i = 1; i <= 5; i++) {
            System.out.println("Request " + i + ": " +
                    tokenBucketLimiter.allowRequest(key));
        }

        Thread.sleep(3000); // allow refill

        System.out.println("After refill:");
        for (int i = 6; i <= 8; i++) {
            System.out.println("Request " + i + ": " +
                    tokenBucketLimiter.allowRequest(key));
        }
    }
}
