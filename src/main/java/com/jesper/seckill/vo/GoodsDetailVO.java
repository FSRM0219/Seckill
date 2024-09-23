package com.jesper.seckill.vo;

import com.jesper.seckill.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoodsDetailVO {

    private int seckillStatus = 0;

    private int remainSeconds = 0;

    private GoodsVO goods ;

    private User user;

}
