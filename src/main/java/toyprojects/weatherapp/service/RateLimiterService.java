package toyprojects.weatherapp.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Component
public class RateLimiterService {

    private final Map<String, Bucket> rateLimitBuckets = new ConcurrentHashMap<>();

    public Bucket getBucket(String clientIp) {
        return rateLimitBuckets.computeIfAbsent(clientIp, (ip)
                -> Bucket.builder()
                        .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(2))))
                        .build());
    }
}
