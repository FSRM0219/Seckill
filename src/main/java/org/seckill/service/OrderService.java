package org.seckill.service;

import org.seckill.entity.OrderInfo;
import org.seckill.entity.SeckillOrder;
import org.seckill.entity.User;
import org.seckill.mapper.OrderMapper;
import org.seckill.redis.OrderKey;
import org.seckill.redis.RedisService;
import org.seckill.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 订单详情表和秒杀订单表需同时新增一条数据，需保证两个操作是一个事物
 */
@Service
public class OrderService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    RedisService redisService;

    /**
     * 获取缓存
     */
    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getSeckillOrderByUidGid, userId + "_" + goodsId, SeckillOrder.class);
    }

    /**
     * 根据订单ID查询OrderInfo
     */
    public OrderInfo getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVO goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);


        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());

        orderMapper.insertSeckillOrder(seckillOrder);
        redisService.set(OrderKey.getSeckillOrderByUidGid, user.getId() + "_" + goods.getId(), seckillOrder);

        return orderInfo;
    }
}
