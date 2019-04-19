package com.jiuy.rb.mapper.product; 
 
import com.jiuy.base.model.MyPage;
import com.jiuy.rb.model.product.ProductRb;
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.product.ProductRbQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 
 *  的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月13日 下午 07:36:57
 * @Copyright 玖远网络 
 */
public interface ProductRbMapper extends BaseMapper<ProductRb>{

    // @Costom
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 获取供应商id
     *
     * @param brandId brandId
     * @author Aison
     * @date 2018/8/6 10:40
     * @return java.lang.Long
     */
    Long getSupplierId(Long brandId);

    /**
     * 增加商品销量
     *
     * @param id 商品主键
     * @param count 增加的销量
     * @return int
     * @author Charlie
     * @date 2018/8/6 9:20
     */
    int addSaleTotalCount(@Param ("id") Long id, @Param ("count") Integer count);


    /**
     *描述 根据供应商id获取供应商下面的产品
     ** @param ids 供应商ids
     * @author hyq
     * @date 2018/8/13 17:14
     * @return java.util.List<com.jiuy.rb.model.product.ProductRbQuery>
     */
    List<ProductRbQuery> getProductByBranIds(ProductRbQuery query);

    /**
     *描述 根据商品id获取的产品
     ** @param
     * @author hyq
     * @date 2018/8/13 17:14
     * @return java.util.List<com.jiuy.rb.model.product.ProductRbQuery>
     */
    List<ProductRbQuery> getProductByProductIds(ProductRbQuery query);

    /**
     *描述 关键字查询商品
     ** @param
     * @author hyq
     * @date 2018/8/13 17:14
     * @return java.util.List<com.jiuy.rb.model.product.ProductRbQuery>
     */
    List<ProductRbQuery> getProductByKeyWord(ProductRbQuery query);

    /**
     * 查询所有图片
     * @param productId
     * @return
     */
    String findSummaryImages(@Param("productId") Long productId);
}
