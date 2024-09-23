package com.jesper.seckill.vo;

import com.jesper.seckill.entity.OrderInfo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDetailVO {

    private GoodsVO goods;

    private OrderInfo order;

}
