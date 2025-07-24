package org.example.cache;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Callable;

@Slf4j
public class MultiCache implements Cache {

    Cache localCache;
    Cache remoteCache;
    ApplicationContext applicationContext;
    StringRedisTemplate stringRedisTemplate;

    public MultiCache(Cache localCache, Cache remoteCache,
                      ApplicationContext applicationContext,
                      StringRedisTemplate stringRedisTemplate) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
        this.applicationContext = applicationContext;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String getName() {
        return localCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper wrapper = localCache.get(key);
        log.info("从本地缓存中获取数据：{}", wrapper);
        if (wrapper != null) {
            return wrapper;
        }

        wrapper = remoteCache.get(key);
        log.info("从远程缓存中获取数据：{}", wrapper);
        if (wrapper != null) {
            Object value = wrapper.get();
            localCache.put(key, value);
            log.info("从远程缓存中获取数据成功，写入本地缓存");
        }
        return wrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = localCache.get(key, type);
        log.info("从本地缓存中获取数据：{}", value);
        if (value != null) {
            return value;
        }

        value = remoteCache.get(key, type);
        log.info("从远程缓存中获取数据：{}", value);
        if (value != null) {
            localCache.put(key, value);
            log.info("从远程缓存中获取数据成功，写入本地缓存");
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return localCache.get(key, () -> {
            log.info("从本地缓存中获取数据失败，从远程缓存中获取数据");
            return remoteCache.get(key, valueLoader);
        });
    }

    @Override
    public void put(Object key, Object value) {
        log.info("写入远程缓存");
        remoteCache.put(key, value);
        log.info("写入本地缓存");
        localCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        evictInternal(key);

        CacheChangeMessage cacheChangeMessage = new CacheChangeMessage();
        cacheChangeMessage.setPublisher(applicationContext.getId());
        cacheChangeMessage.setCacheName(getName());
        cacheChangeMessage.setKey((String) key);
        cacheChangeMessage.setClear(false);
        stringRedisTemplate.convertAndSend("cacheChange", JSON.toJSONString(cacheChangeMessage));
    }

    public void evictInternal(Object key) {
        log.info("删除远程缓存");
        remoteCache.evict(key);
        log.info("删除本地缓存");
        localCache.evict(key);
    }

    @Override
    public void clear() {
        clearInternal();

        CacheChangeMessage cacheChangeMessage = new CacheChangeMessage();
        cacheChangeMessage.setPublisher(applicationContext.getId());
        cacheChangeMessage.setCacheName(getName());
        cacheChangeMessage.setClear(true);
        stringRedisTemplate.convertAndSend("cacheChange", JSON.toJSONString(cacheChangeMessage));
    }

    public void clearInternal() {
        log.info("清理远程缓存");
        remoteCache.clear();
        log.info("清理本地缓存");
        localCache.clear();
    }

}
