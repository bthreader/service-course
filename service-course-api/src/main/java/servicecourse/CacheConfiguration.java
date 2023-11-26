package servicecourse;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {
    public static final String BIKE_BRANDS = "bikeBrands";

    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager(BIKE_BRANDS);
    }
}
