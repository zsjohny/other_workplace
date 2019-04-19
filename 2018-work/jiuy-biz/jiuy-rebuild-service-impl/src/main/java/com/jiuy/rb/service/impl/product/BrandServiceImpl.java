package com.jiuy.rb.service.impl.product;

import com.jiuy.rb.mapper.product.BrandRbMapper;
import com.jiuy.rb.model.product.BrandRb;
import com.jiuy.rb.service.product.IBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 品牌相关的业务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/14 16:30
 * @Copyright 玖远网络
 */
@Service("brandService")
public class BrandServiceImpl implements IBrandService  {

    /**
     * 状态:1:禁用,0:正常,-1:删除
     */
    private static final int NORMAL_STATUS = 0;

    @Resource(name = "brandRbMapper")
    private BrandRbMapper brandRbMapper;

    /**
     * 根据品牌ids查询
     *
     * @param ids 品牌ids
     * @return java.util.List<com.jiuy.rb.model.product.BrandRb>
     * @author Charlie(唐静)
     * @date 2018/6/21 19:02
     */
    @Override
    public List<BrandRb> selectByIds(List<Long> ids) {
        return brandRbMapper.selectByIds(ids, NORMAL_STATUS);
    }
    /**
     * 根据商品id 获取品牌信息
     * @param productId
     * @return
     */
    @Override
    public BrandRb findBrandByProductId(Long productId) {
        return brandRbMapper.findBrandByProductId(productId);
    }
}
