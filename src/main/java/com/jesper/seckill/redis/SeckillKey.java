package com.jesper.seckill.redis;

public class SeckillKey extends BasePrefix {

    private SeckillKey(String prefix) {
        super(prefix);
    }

    /**
     * SeckillKey静态实例
     * */
    public static SeckillKey isGoodsOver = new SeckillKey("over");
}
