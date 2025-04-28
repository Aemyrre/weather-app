package toyprojects.weatherapp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.bucket4j.Bucket;
import toyprojects.weatherapp.service.RateLimiterService;

@SpringBootTest
public class RateLimiterServiceTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Test
    void contextLoads() {
        assertNotNull(rateLimiterService);
    }

    @Test
    public void testRateLimiterEnforcesLimits() {
        Bucket bucket = rateLimiterService.getBucket("127.0.0.1");

        for (int i = 0; i < 10; i++) {
            assertTrue(bucket.tryConsume(1));
        }

        assertFalse(bucket.tryConsume(1));
    }

}
