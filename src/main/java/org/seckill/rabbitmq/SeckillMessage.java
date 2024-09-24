package org.seckill.rabbitmq;

import org.seckill.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeckillMessage {

    private User user;

    private long goodsId;

}
