package org.seckill.rabbitmq;

import org.seckill.entity.SeckillOrder;
import org.seckill.entity.User;
import org.seckill.redis.RedisService;
import org.seckill.service.GoodsService;
import org.seckill.service.OrderService;
import org.seckill.service.SeckillService;
import org.seckill.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class MQReceiver {

    private static final Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Resource
    GoodsService goodsService;

    @Resource
    OrderService orderService;

    @Resource
    SeckillService seckillService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message:{}", message);
        SeckillMessage m = RedisService.stringToBean(message, SeckillMessage.class);
        User user = m.getUser();
        long goodsId = m.getGoodsId();

        GoodsVO goodsVO = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVO.getStockCount();
        if (stock <= 0) {
            return;
        }

        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        seckillService.seckill(user, goodsVO);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info(" topic  queue1 message:{}", message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info(" topic  queue2 message:{}", message);
    }
}
