package org.seckill.rabbitmq;

import org.seckill.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息体<br>private User user;<br>private long goodsId;
 */
@Setter
@Getter
public class SeckillMessage {

    private User user;

    private long goodsId;

}
