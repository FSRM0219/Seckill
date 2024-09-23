package com.jesper.seckill.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * public class SeckillOrder {<br>
 * ID<br>
 * 用户ID<br>
 * 订单ID<br>
 * 商品ID<br>
 * }
 */
@Setter
@Getter
public class SeckillOrder {

    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

}
