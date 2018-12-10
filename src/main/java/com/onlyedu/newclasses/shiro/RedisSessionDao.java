package com.onlyedu.newclasses.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 * @date 2018/12/6 9:02
 */

public class RedisSessionDao extends AbstractSessionDAO {

    private Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);
    private RedisTemplate redisTemplate;
    private long expireTime;

    public RedisSessionDao(RedisTemplate redisTemplate,long expireTime){
        this.redisTemplate = redisTemplate;
        this.expireTime = expireTime;
    }

    @Override
    protected Serializable doCreate(Session session) {

        logger.info("RedisSessionDao.doCreate ");

        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        logger.info("RedisSessionDao.doReadSession");
        if(null == sessionId){
            return null;
        }
        return (Session) redisTemplate.opsForValue().get(sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {

        logger.info("RedisSessionDao.update");
        if(null == session){
            return;
        }
        session.setTimeout(expireTime);
        redisTemplate.opsForValue().set(session.getId(),session,expireTime,TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(Session session) {
        if(null == session){
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
