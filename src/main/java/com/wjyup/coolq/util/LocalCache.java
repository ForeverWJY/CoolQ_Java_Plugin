package com.wjyup.coolq.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class LocalCache {
    private static final Logger log = LogManager.getLogger(LocalCache.class);

    private static LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(60L, TimeUnit.MINUTES)
            .build(CacheLoader.from(key -> key));

    public static void addCache(String name, Object value) {
        cache.put(name, value);
    }

    public static Object getCache(String name) {
        return cache.asMap().get(name);
    }
}
