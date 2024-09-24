package org.seckill.rabbitmq;

import org.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RabbitTemplate是AmqpTemplate的一个具体实现;
 * AmqpTemplate适用于需要与多种AMQP消息代理进行交互的场景;
 * RabbitTemplate适用于专门与RabbitMQ进行交互的场景,提供RabbitMQ的特定功能，例如支持RabbitMQ消息确认,事务和回调;
 */
@Service
public class MQSender {

    private static final Logger log = LoggerFactory.getLogger(MQSender.class);

    /*@Autowired*/
    @Resource
    RabbitTemplate rabbitTemplate;
    /*AmqpTemplate amqpTemplate;*/

    public void sendSeckillMessage(SeckillMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("send message:{}", msg);
        rabbitTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }
}
