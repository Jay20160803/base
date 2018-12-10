package com.onlyedu.newclasses.shiro;

import com.onlyedu.newclasses.util.SerializeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * @author Andy
 * @date 2018/12/4 15:57
 */

public class ShiroRedisCache<K,V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(ShiroRedisCache.class);
    private RedisTemplate<K,V> redisTemplate;
    private String prefix = "shiro_redis:";

    public ShiroRedisCache(RedisTemplate<K,V> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public ShiroRedisCache(RedisTemplate<K,V> redisTemplate,String prefix){
        this(redisTemplate);
        this.prefix = prefix;
    }

    @Override
    public V get(K key) throws CacheException {

        logger.debug("key:{}",key);
        if(key == null){
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {

        logger.debug("put-key:{},value:{}",key,value);
        if(key == null || value == null){
            return null;
        }
        redisTemplate.opsForValue().set(key,value);
        return null;
    }

    @Override
    public V remove(K key) throws CacheException {

        logger.debug("remove-key:{},",key);
        if(key == null){
            return null;
        }
        V value = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return value;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Override
    public int size() {
        Long lenth = redisTemplate.getConnectionFactory().getConnection().dbSize();
        return lenth.intValue();
    }

    @Override
    public Set keys() {
        String bkey = prefix + "*";
        Set<K> set = redisTemplate.keys((K) bkey);
        Set<K> result = new HashSet<>();

        if (CollectionUtils.isEmpty(set)) {
            return Collections.emptySet();
        }

        for (K key : set) {
            result.add(key);
        }
        return result;
    }

    @Override
    public Collection values() {
        Set<K> keys = keys();
        List<V> values = new ArrayList<>(keys.size());
        for (K k : keys) {

            values.add(redisTemplate.opsForValue().get(k));
        }
        return values;
    }

    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.prefix + key;
            return preKey.getBytes();
        } else {
            return SerializeUtils.serialize(key);
        }
    }
}
