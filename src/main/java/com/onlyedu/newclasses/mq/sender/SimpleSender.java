package com.onlyedu.newclasses.mq.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.onlyedu.newclasses.mq.config.RabbitConfig.QUEUE;

/**
 * @author Andy
 * @date 2018/11/21 15:51
 */
@Component
public class SimpleSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(){
        String message = "hello word!";
        rabbitTemplate.convertAndSend(QUEUE,message.getBytes());
    }
}
