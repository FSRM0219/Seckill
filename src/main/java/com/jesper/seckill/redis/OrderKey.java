package com.jesper.seckill.redis;

/**
 * 创建OrderKey静态实例getSeckillOrderByUidGid
 */
public class OrderKey extends BasePrefix {

    /**
     * 调用父类构造函数，expireSeconds默认为0
     */
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("seckill");

}
