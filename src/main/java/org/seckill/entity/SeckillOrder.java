package org.seckill.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户ID<br>
 * 订单ID<br>
 * 商品ID<br>
 */
@Setter
@Getter
public class SeckillOrder {

    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

}
