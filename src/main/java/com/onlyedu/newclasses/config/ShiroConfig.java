package com.onlyedu.newclasses.config;


import com.onlyedu.newclasses.shiro.CustomRealm;
import com.onlyedu.newclasses.shiro.RedisSessionDao;
import com.onlyedu.newclasses.shiro.ShiroRedisCacheManager;
import com.onlyedu.newclasses.shiro.URLPermissionsFilter;
import com.onlyedu.newclasses.util.FastJsonRedisSerializer;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.filter.DelegatingFilterProxy;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.onlyedu.newclasses.shiro.Constant.GLOBAL_SESSION_TIMEOUT;
import static com.onlyedu.newclasses.shiro.Constant.SHIRO_REDIS_DATEBASE;
import static com.onlyedu.newclasses.shiro.Constant.SHIRO_REDIS_EXPIRE_TIME;


/**
 * @author Andy
 * @date 2018/11/23 17:02
 */

@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties redisProperties(){
        return new RedisProperties();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/unlogin");
        bean.setUnauthorizedUrl("/unauthor");

        Map<String, Filter> filters = new HashMap<>();
        filters.put("perms", new URLPermissionsFilter());
        filters.put("anon", new AnonymousFilter());
        bean.setFilters(filters);

        Map<String, String> chains = new HashMap<>();
        chains.put("/login", "anon");
        chains.put("/logout", "logout");
        chains.put("/test","authc");
        chains.put("/**", "authc,perms");
        chains.put("/**", "anon");       //暂时设置允许所有请求
        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 数据库认证的实现
        manager.setRealm(userRealm());
        // session 管理器
        manager.setSessionManager(sessionManager());
        // 缓存管理器
        manager.setCacheManager(redisCacheManager());
        return manager;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager  sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(GLOBAL_SESSION_TIMEOUT); //12小时
        sessionManager.setDeleteInvalidSessions(true);
        //关键在这里
        sessionManager.setSessionDAO(redisSessionDao());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(getSessionIdCookie());
        return sessionManager;
    }

    @Bean
    public RedisSessionDao redisSessionDao(){
        return new RedisSessionDao(shiroRedisTemplate(),SHIRO_REDIS_EXPIRE_TIME);
    }
    @Bean
    public SimpleCookie getSessionIdCookie(){
        return new SimpleCookie("jseesionId");

    }

    @Bean
    public CustomRealm userRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCacheManager(redisCacheManager());
        customRealm.setCachingEnabled(true);
        customRealm.setAuthenticationCachingEnabled(false);
        customRealm.setAuthorizationCachingEnabled(true);
        return customRealm;
    }

    @Bean(name = "shrioRedisCacheManager")
    public ShiroRedisCacheManager redisCacheManager() {
        ShiroRedisCacheManager cacheManager = new ShiroRedisCacheManager(shiroRedisTemplate());
        cacheManager.createCache("shiro_redis:");
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, Object> shiroRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory());
        return template;
    }


  private RedisConnectionFactory connectionFactory() {

        JedisConnectionFactory conn = new JedisConnectionFactory(getRedisStandaloneConfiguration(),getJedisClientConfiguration());
        logger.info("1.初始化Redis缓存服务器(登录用户Session及Shiro缓存管理)... ...");
        return conn;
    }

    private RedisStandaloneConfiguration getRedisStandaloneConfiguration(){

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(SHIRO_REDIS_DATEBASE);
        configuration.setHostName(redisProperties().getHost());
        configuration.setPassword(redisProperties().getPassword());
        configuration.setPort(redisProperties().getPort());
        return configuration;
    }

    private JedisClientConfiguration getJedisClientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        builder.usePooling().poolConfig(jedisPoolConfig());
        return builder.build();
    }



    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool pool = redisProperties().getJedis().getPool();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getMaxWait() != null) {
            config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        return config;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
