package com.jesper.seckill.service;

import com.jesper.seckill.entity.OrderInfo;
import com.jesper.seckill.entity.SeckillOrder;
import com.jesper.seckill.entity.User;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.redis.SeckillKey;
import com.jesper.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SeckillService {

    @Resource
    GoodsService goodsService;

    @Resource
    OrderService orderService;

    @Resource
    RedisService redisService;

    /**
     * 减少商品库存,如果库存减少成功,创建订单,返回订单信息<br>
     * 如果库存减少失败,标记该商品秒杀已结束,返回null
     */
    @Transactional
    public OrderInfo seckill(User user, GoodsVO goods) {
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 获取用户秒杀结果,根据用户ID和商品ID查询订单<br>如果找到订单，返回订单ID;
     * 否则检查该商品秒杀是否已结束<br>如果商品的秒杀已结束，返回-1,否则返回0,表示秒杀进行中。
     */
    public long getSeckillResult(long userId, long goodsId) {
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }
}
