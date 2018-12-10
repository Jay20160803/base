package com.onlyedu.newclasses.shiro;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Andy
 * @date 2018/12/4 16:47
 */

public class ShiroRedisCacheManager extends AbstractCacheManager {

    private RedisTemplate<String,Object> redisTemplate;

    public ShiroRedisCacheManager(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Cache createCache(String prefix) throws CacheException {
        return new ShiroRedisCache<String,Object>(redisTemplate,prefix);
    }
}
