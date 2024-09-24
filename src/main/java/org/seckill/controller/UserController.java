package org.seckill.controller;

import org.seckill.entity.User;
import org.seckill.redis.RedisService;
import org.seckill.result.Result;
import org.seckill.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(Model model, User user) {
        return Result.success(user);
    }
}