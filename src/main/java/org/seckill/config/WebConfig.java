package org.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定参数解析器<br>
 * 作用：改变SpringMVC的Controller传入参数，实现可以User替换Token做为参数从登陆页面传到商品列表页面
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Resource
    UserArgumentResolver userArgumentResolver;

    /*SpringMVC框架回调addArgumentResolvers，然后给Controller的参数赋值*/
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }
}
