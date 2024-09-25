package org.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import org.seckill.entity.User;
import org.seckill.redis.GoodsKey;
import org.seckill.redis.RedisService;
import org.seckill.result.Result;
import org.seckill.service.GoodsService;
import org.seckill.vo.GoodsDetailVO;
import org.seckill.vo.GoodsVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <strong>返回类型:</strong><br>
 * detail2方法:返回类型为String,使用Thymeleaf模板引擎渲染商品详情页面，将生成的HTML缓存到Redis中;<br>
 * detail方法:不涉及HTML渲染,将商品详情封装在GoodsDetailVO对象中,以JSON格式返回;<br>
 * <strong>缓存机制:</strong><br>
 * detail2方法:返回HTML内容之前,检查Redis缓存中是否存在该商品的详情页面.如果存在，则直接返回缓存的HTML;
 * 如果不存在，则查询数据库获取商品详情,渲染HTML,将生成的HTML存入Redis;<br>
 * detail方法:直接从数据库查询商品详情返回JSON对象;<br>
 * <strong>模板渲染</strong><br>
 * detail2方法:使用SpringWebContext和Thymeleaf渲染商品详情页面，适用于返回完整的HTML页面;<br>
 * detail方法:直接返回商品详情的JSON数据，适合用于AJAX请求或前端框架如Vue.js、React.js进行数据渲染;<br>
 * <strong>适用场景</strong><br>
 * detail2方法:适合用于传统的服务器渲染的网页应用，用户访问商品详情页面时直接获取完整的HTML;<br>
 * detail方法:适合用于现代的单页应用SPA或需要动态加载数据的场景,前端可以通过AJAX请求获取商品详情数据<br>
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    RedisService redisService;

    @Resource
    GoodsService goodsService;

    @Resource
    ApplicationContext applicationContext;

    @Resource
    ThymeleafViewResolver thymeleafViewResolver;

    // 商品列表页面
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {

        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsList);

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);

        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    // 商品详情页面
    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 根据id查询商品详情
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        // 判断秒杀阶段
        if (now < startTime) {
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
        }

        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }

    // 商品详情页面
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> detail(Model model, User user, @PathVariable("goodsId") long goodsId) {

        // 根据id查询商品详情
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startTime) {
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
        }

        GoodsDetailVO vo = new GoodsDetailVO();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(seckillStatus);

        return Result.success(vo);
    }
}
