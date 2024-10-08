package org.seckill.rabbitmq;

import org.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RabbitTemplate→AmqpTemplate具体实现;<br>
 * AmqpTemplate适用于与多种AMQP消息代理进行交互场景;<br>
 * RabbitTemplate适用于RabbitMQ交互场景,提供RabbitMQ的特定功能，例如支持RabbitMQ消息确认,事务和回调;
 */
@Service
public class MQSender {

    private static final Logger log = LoggerFactory.getLogger(MQSender.class);

    @Resource
    RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(SeckillMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("send message:{}", msg);
        rabbitTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }
}
