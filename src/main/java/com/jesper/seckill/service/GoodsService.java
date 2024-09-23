package com.jesper.seckill.service;

import com.jesper.seckill.entity.SeckillGoods;
import com.jesper.seckill.mapper.GoodsMapper;
import com.jesper.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Slf4j
@Service
public class GoodsService {

    /**
     * 乐观锁冲突最大重试次数
     */
    private static final int DEFAULT_MAX_RETRIES = 5;

    /*@Autowired*/
    @Resource
    GoodsMapper goodsMapper;

    /**
     * 查询商品列表
     */
    public List<GoodsVO> listGoodsVO() {
        return goodsMapper.listGoodsVO();
    }

    /**
     * 根据id查询指定商品
     */
    public GoodsVO getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 减少库存，每次减一
     */
    public boolean reduceStock(GoodsVO goods) {
        int numAttempts = 0;
        int ret = 0;
        SeckillGoods sg = new SeckillGoods();
        sg.setGoodsId(goods.getId());
        sg.setVersion(goods.getVersion());
        do {
            numAttempts++;
            try {
                sg.setVersion(goodsMapper.getVersionByGoodsId(goods.getId()));
                ret = goodsMapper.reduceStockByVersion(sg);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (ret != 0)
                break;
        } while (numAttempts < DEFAULT_MAX_RETRIES);
        return ret > 0;
    }
}
