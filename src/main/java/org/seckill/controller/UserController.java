package org.seckill.controller;

import org.seckill.entity.User;
import org.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(User user) {
        return Result.success(user);
    }
}