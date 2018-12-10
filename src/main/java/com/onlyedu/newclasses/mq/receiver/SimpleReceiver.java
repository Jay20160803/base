package com.onlyedu.newclasses.mq.receiver;

import com.onlyedu.newclasses.mq.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Andy
 * @date 2018/11/21 15:55
 */
@Component
public class SimpleReceiver {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receiver(String message){
        System.out.println("receiver message = "+message);
    }
}
