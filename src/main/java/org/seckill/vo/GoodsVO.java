package org.seckill.vo;

import org.seckill.entity.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 秒杀价格、库存、开始截至时间、版本号视图
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

