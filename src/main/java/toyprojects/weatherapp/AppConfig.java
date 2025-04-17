package toyprojects.weatherapp;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> caffeineCacheBuilder = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("currentWeather", "forecastWeather");
        cacheManager.setCaffeine(caffeineCacheBuilder);
        return cacheManager;
    }

}
