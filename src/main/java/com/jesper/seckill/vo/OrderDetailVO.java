package com.jesper.seckill.vo;

import com.jesper.seckill.entity.OrderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品订单视图
 */
@Setter
@Getter
public class OrderDetailVO {

    private GoodsVO goods;

    private OrderInfo order;

}
