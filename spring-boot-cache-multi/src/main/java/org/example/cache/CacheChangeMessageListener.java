package org.example.cache;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheChangeMessageListener implements MessageListener {

    @Autowired
    CacheManager cacheManager;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        CacheChangeMessage cacheChangeMessage = JSON.parseObject(body, CacheChangeMessage.class);

        log.info("缓存变更消息,{}", cacheChangeMessage);

        // 当前应用发布的消息，不用处理，已经清理过
        if (applicationContext.getId().equals(cacheChangeMessage.getPublisher())) {
            return;
        }

        Cache cache = cacheManager.getCache(cacheChangeMessage.getCacheName());
        if (cache == null) {
            return;
        }

        if (cache instanceof MultiCache multiCache) {
            if (cacheChangeMessage.getClear()) {
                multiCache.clearInternal();
            } else {
                multiCache.evictInternal(cacheChangeMessage.getKey());
            }
        }
    }

}
