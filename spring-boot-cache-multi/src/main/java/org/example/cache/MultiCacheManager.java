package org.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiCacheManager implements CacheManager {

    CacheManager localCacheManager;
    CacheManager remoteCacheManager;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public MultiCacheManager(CacheManager localCacheManager, CacheManager remoteCacheManager) {
        this.localCacheManager = localCacheManager;
        this.remoteCacheManager = remoteCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, cacheName -> {
            Cache localCache = localCacheManager.getCache(cacheName);
            Cache remoteCache = remoteCacheManager.getCache(cacheName);
            return new MultiCache(localCache, remoteCache, applicationContext, stringRedisTemplate);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        Set<String> names = new LinkedHashSet<>();
        names.addAll(localCacheManager.getCacheNames());
        names.addAll(remoteCacheManager.getCacheNames());
        return names;
    }

}
