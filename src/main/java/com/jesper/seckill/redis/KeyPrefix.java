package com.jesper.seckill.redis;

/**
 * expireSeconds()<br>getPrefix()
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
