package com.jesper.seckill.rabbitmq;

import com.jesper.seckill.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息体：User和goodsID
 */
@Setter
@Getter
public class SeckillMessage {

    private User user;

    private long goodsId;

}
