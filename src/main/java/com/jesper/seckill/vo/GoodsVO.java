package com.jesper.seckill.vo;

import com.jesper.seckill.entity.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * seckillPrice<br>stockCount
 * */
@Setter
@Getter
public class GoodsVO extends Goods {

    private Double seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    private Integer version;

}

