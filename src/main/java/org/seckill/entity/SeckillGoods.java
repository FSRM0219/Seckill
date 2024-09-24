package org.seckill.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SeckillGoods {

    private Long id;

    private Long goodsId;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    private int version;

}
