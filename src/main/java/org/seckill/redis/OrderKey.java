package org.seckill.redis;

public class OrderKey extends BasePrefix {

    /**
     * 调用父类构造函数，expireSeconds默认为0
     */
    public OrderKey(String prefix) {
        super(prefix);
    }

    /**
     * OrderKey静态实例
     */
    public static OrderKey getSeckillOrderByUidGid = new OrderKey("seckill");

}
