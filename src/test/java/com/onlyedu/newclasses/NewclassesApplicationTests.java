package com.onlyedu.newclasses;

import com.onlyedu.newclasses.testModule.dao.UserDao;
import com.onlyedu.newclasses.model.User;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewclassesApplicationTests {

    @Test
    public void contextLoads() {

    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisTest(){
        redisTemplate.opsForValue().set("aa","bb");
        String result = (String) redisTemplate.opsForValue().get("aa");
        Assert.assertEquals("bb",result);
    }


    @Autowired
    private UserDao userDao;
   /* @Test
    public void mysqlTest(){

        User user = new User();
        user.setUserName("Andy");
        user.setPassWord("123456");
        user.setPhone("18816904369");

        int num = userDao.insert(user);

        Assert.assertEquals("插入数据失败",1,num);

        List<User> list = userDao.selectAllUser();

        if(list == null || list.size() == 0){
            throw new RuntimeException("查询数据失败");
        }
    }*/



}
