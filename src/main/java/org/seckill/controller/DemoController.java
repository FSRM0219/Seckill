package org.seckill.controller;

import org.seckill.rabbitmq.MQSender;
import org.seckill.redis.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Resource
    RedisService redisService;

    @Resource
    MQSender sender;

}
