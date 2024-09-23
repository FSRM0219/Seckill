package com.jesper.seckill.service;

import com.jesper.seckill.entity.OrderInfo;
import com.jesper.seckill.entity.SeckillOrder;
import com.jesper.seckill.entity.User;
import com.jesper.seckill.mapper.OrderMapper;
import com.jesper.seckill.redis.OrderKey;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
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
        OrderInfo orderInfo = OrderInfo.builder()
                .createDate(new Date())
                .deliveryAddrId(0L)
                .goodsCount(1)
                .goodsId(goods.getId())
                .goodsName(goods.getGoodsName())
                .goodsPrice(goods.getGoodsPrice())
                .orderChannel(1)
                .status(0)
                .build();

        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = SeckillOrder.builder()
                .goodsId(goods.getId())
                .orderId(orderInfo.getId())
                .userId(user.getId())
                .build();

        orderMapper.insertSeckillOrder(seckillOrder);
        redisService.set(OrderKey.getSeckillOrderByUidGid, user.getId() + "_" + goods.getId(), seckillOrder);

        return orderInfo;
    }
}
