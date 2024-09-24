package org.seckill.controller;

import org.seckill.entity.OrderInfo;
import org.seckill.entity.User;
import org.seckill.result.CodeMsg;
import org.seckill.result.Result;
import org.seckill.service.GoodsService;
import org.seckill.service.OrderService;
import org.seckill.vo.GoodsVO;
import org.seckill.vo.OrderDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }

}
