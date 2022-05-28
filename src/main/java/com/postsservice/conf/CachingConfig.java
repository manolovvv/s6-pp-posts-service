package com.postsservice.conf;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager("userdetails");
    }

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                cacheManager.setAllowNullValues(false);
            }
        };
    }

    public void evictCache() {
        CacheManager cacheManager = cacheManager();
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache("userdetails").clear());
    }

    @Scheduled(fixedRate = 30000)
    public void evictAllcachesAtIntervals() {
        evictCache();
    }
}
