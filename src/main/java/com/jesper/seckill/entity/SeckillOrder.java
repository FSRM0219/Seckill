package com.jesper.seckill.entity;

import lombok.Builder;
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
@Builder
@Getter
public class SeckillOrder {

    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

}
