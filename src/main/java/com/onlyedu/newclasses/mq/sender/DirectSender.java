package com.onlyedu.newclasses.mq.sender;

import com.onlyedu.newclasses.model.User;
import com.onlyedu.newclasses.mq.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(User user) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, "direct.pwl", user);
    }
}
