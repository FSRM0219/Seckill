package org.seckill.controller;

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
import org.seckill.util.LeakyBucketRateLimiter;
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

    LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(10, 1);

    // 标记商品是否被秒杀
    private final HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    // 异步下单
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId") long goodsId) {

        if (!rateLimiter.tryAcquire()) {
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);

        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        // TODO 代码逻辑有问题，如何判断双写不一致
        long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);//10
        if (stock < 0) {
            afterPropertiesSet();
            long stock1 = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);//10
            if (stock1 < 0) {
                localOverMap.put(goodsId, true); // true秒杀完成
                return Result.error(CodeMsg.SECKILL_OVER);
            }
        }

        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }

        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendSeckillMessage(message);
        return Result.success(0);
    }

    @Override
    public void afterPropertiesSet() {
        List<GoodsVO> goodsVOList = goodsService.listGoodsVO();
        if (goodsVOList == null) {
            return;
        }
        for (GoodsVO goods : goodsVOList) {
            redisService.set(GoodsKey.getGoodsStock, "" + goods.getId(), goods.getStockCount());
            // 标记已秒杀商品
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * @param model SpringMVC,用于视图中传递数据
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user); // 将用户对象添加到模型
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // -1秒杀已结束; 0秒杀进行中
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }
}
