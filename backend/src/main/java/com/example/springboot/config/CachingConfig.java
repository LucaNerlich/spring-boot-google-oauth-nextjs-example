package com.example.springboot.config;

import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

@Slf4j
@CacheConfig
@Configuration
@EnableCaching
public class CachingConfig {

    private final CacheManager cacheManager;

    public CachingConfig(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Method to clear the cache at a fixed interval
     * <p>
     * This method is scheduled to be executed at a fixed rate of 5000 milliseconds.
     * It retrieves all the cache names from the cache manager and clears each cache.
     */
    @Scheduled(fixedRate = 5000)
    public void clearCache() {
        cacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    // https://stackoverflow.com/questions/77598934/can-i-use-enum-in-cacheable
    @FieldNameConstants(onlyExplicitlyIncluded = true)
    public enum CacheName {
        @FieldNameConstants.Include SOME_EXAMPLE_CACHE
    }
}
