package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.BrandRb; 
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 品牌表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月14日 下午 04:29:18
 * @Copyright 玖远网络 
 */
public interface BrandRbMapper extends BaseMapper<BrandRb>{

    // @Costom
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 根据品牌ids查询
     *
     * @param brandIds 品牌ids
     * @param status 状态
     * @return java.util.List<com.jiuy.rb.model.product.BrandRb>
     * @author Charlie(唐静)
     * @date 2018/6/21 19:02
     */
    List<BrandRb> selectByIds(@Param("brandIds") List<Long> brandIds, @Param("status")int status);


    /**
     * 获取品牌信息
     * @param productId productId
     * @return
     * @author hyf
     * @date 2018/8/29 15:05
     */
    BrandRb findBrandByProductId(@Param("productId") Long productId);
}