package com.jesper.seckill.controller;

import com.jesper.seckill.rabbitmq.MQSender;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.redis.UserKey;
import com.jesper.seckill.result.CodeMsg;
import com.jesper.seckill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
