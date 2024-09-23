package com.jesper.seckill.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeckillOrder {

    private Long id;

    private Long userId;

    private Long  orderId;

    private Long goodsId;

}
