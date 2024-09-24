package org.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.seckill.entity.SeckillOrder;
import org.seckill.entity.User;
import org.seckill.rabbitmq.MQSender;
import org.seckill.rabbitmq.SeckillMessage;
import org.seckill.redis.GoodsKey;
import org.seckill.redis.RedisService;
import org.seckill.result.CodeMsg;
import org.seckill.result.Result;
import org.seckill.service.GoodsService;
import org.seckill.service.OrderService;
import org.seckill.service.SeckillService;
import org.seckill.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Resource
    GoodsService goodsService;

    @Resource
    OrderService orderService;

    @Resource
    SeckillService seckillService;

    @Resource
    RedisService redisService;

    @Resource
    MQSender sender;

    /*基于令牌桶算法的限流实现类*/
    RateLimiter rateLimiter = RateLimiter.create(10);

    /*做标记，判断该商品是否被处理过了*/
    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /*将同步下单改为异步下单*/
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId") long goodsId) {

        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);//10
        if (stock < 0) {
            afterPropertiesSet();
            long stock2 = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);//10
            if (stock2 < 0) {
                localOverMap.put(goodsId, true);
                return Result.error(CodeMsg.SECKILL_OVER);
            }
        }
        //判断重复秒杀
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendSeckillMessage(message);
        return Result.success(0);//排队中
    }

    /*系统初始化,将商品信息加载到redis和本地内存*/
    @Override
    public void afterPropertiesSet() {
        List<GoodsVO> goodsVOList = goodsService.listGoodsVO();
        if (goodsVOList == null) {
            return;
        }
        for (GoodsVO goods : goodsVOList) {
            redisService.set(GoodsKey.getGoodsStock, "" + goods.getId(), goods.getStockCount());
            //初始化商品都是没有处理过的
            localOverMap.put(goods.getId(), false);
        }
    }

    /*
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }
}
