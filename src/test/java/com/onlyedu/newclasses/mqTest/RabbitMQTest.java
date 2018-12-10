package com.onlyedu.newclasses.mqTest;

import com.onlyedu.newclasses.model.User;
import com.onlyedu.newclasses.mq.sender.DirectSender;
import com.onlyedu.newclasses.mq.sender.FanoutSender;
import com.onlyedu.newclasses.mq.sender.SimpleSender;
import com.onlyedu.newclasses.mq.sender.TopicSender;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author Andy
 * @date 2018/11/21 12:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private DirectSender directSender;
    @Autowired
    private SimpleSender simpleSender;

    @Test
    public void testSimple() throws Exception{
        simpleSender.send();
        Thread.sleep(5*1000);
    }


    /**
     * Fanout测试
     * @throws Exception
     */
    @Test
    public void testFanout() throws Exception {
        User user=new User();
        user.setPassWord("1");
        user.setUserName("pwl");
        fanoutSender.send(user);
        Thread.sleep(5*1000);
    }



    /**
     * TOPIC测试
     * @throws Exception
     */
    @Test
    public void testTopic() throws Exception {
        User user=new User();
        user.setPassWord("1");
        user.setUserName("pwl");
        topicSender.send(user);

        Thread.sleep(5*1000);
    }

    /**
     * DIRECT测试
     * @throws Exception
     */
    @Test
    public void testDirect() throws Exception {
        User user=new User();
        user.setPassWord("1");
        user.setUserName("pwl");
        directSender.send(user);

        Thread.sleep(5*1000);
    }

    @Test
    public void testMQ(){


    }

    private final static String QUEUE_NAME = "hello1";
    private void send(){

        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("101.132.137.11");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            String message = "Hello Word!";

            //发布消息持久化
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            channel.close();
            connection.close();
        }catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }

    }

    private void receive(){

        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("101.132.137.11");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            //queuee 持久化
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);

            //在消费完消息前，一次你拿一条消息
            channel.basicQos(1);

            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    try{

                    }finally {
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            };

            //不是用自动消息确认
            channel.basicConsume(QUEUE_NAME,false,consumer);

            Thread.sleep(5*1000);
        }catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }

    }
}
