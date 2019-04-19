package com.jiuy.rb.service.product;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.product.ProductAuditRbQuery;
import com.jiuy.rb.model.product.ProductRb;
import com.jiuy.rb.model.product.ProductRbQuery;
import com.jiuy.rb.model.product.RestrictionActivityProductRb;
import com.jiuyuan.entity.Product;

import java.util.List;
import java.util.Map;

/**
 *
 * 商品业务的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 9:44
 * @Copyright 玖远网络
 */
public interface IProductService {

    /**
     * 通过id获取一个商品
     *
     * @param productId productId
     * @author Aison
     * @date 2018/6/21 13:52
     * @return ProductRb
     */
    ProductRb getById(Long productId);

    /**
     *  查询一个 list
     *
     * @param query query
     * @author Aison
     * @date 2018/7/6 15:24
     * @return java.util.List<com.jiuy.rb.model.product.ProductRb>
     */
    List<ProductRb> getList(ProductRbQuery query);


    /**
     * 获取活动商品
     *
     * @param activityId activityId
     * @author Aison
     * @date 2018/6/21 13:58
     * @return RestrictionActivityProductRb
     */
     RestrictionActivityProductRb getActivityProductById(Long activityId);

    /**
     * 审核通过商品
     *
     * @param productAuditId 审核主键
     * @param userSession 用户
     * @author Aison
     * @date 2018/6/13 9:46
     * @return MyLog<Long> 返回日志
     */
     MyLog<Long> auditProduct(Long productAuditId, UserSession userSession);


    /**
     * 审核不通过
     *
     * @param productAuditId 审核id
     * @param noPassReason 审核原因
     * @author Aison
     * @return MyLog 返回日志
     * @date 2018/6/13 13:29
     */
     MyLog<Long> productAuditNoPass(Long productAuditId, String noPassReason,UserSession userSession);


    /**
     * 获取审核详情（展示详情）
     *
     * @param productAuditId productAuditId
     * @author Aison
     * @date 2018/6/13 15:06
     */
    ProductAuditRbQuery getProductAuditInfo(Long productAuditId);

    /**
     * 审核数据列表
     *
     * @param productAuditRbQuery
     * @author Aison
     * @date 2018/6/13 15:33
     */
    MyPage<ProductAuditRbQuery> productAuditList(ProductAuditRbQuery productAuditRbQuery);


    /**
     * 获取供应商id
     *
     * @param brandId brandId
     * @author Aison
     * @date 2018/8/6 10:41
     * @return java.lang.Long
     */
    Long getSupplierId(Long brandId);

    /**
     * 增加商品销量
     *
     * @param productId productId
     * @param count 增加的销量
     * @return int
     * @author Charlie
     * @date 2018/8/6 9:20
     */
    int addSaleTotalCount(Long productId, Integer count);

    /**
     *描述 根据供应商id获取供应商下面的产品
     ** @param ids 供应商ids
     * @author hyq
     * @date 2018/8/13 17:14
     * @return java.util.List<com.jiuy.rb.model.product.ProductRbQuery>
     */
    Map<String,Object> getProductByBrandId(ProductRbQuery query);

    /**
     *描述 通过关键字查询优惠券里面的指定商品
     ** @param query
     * @author hyq
     * @date 2018/8/14 18:02
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String,Object> getProductByKeyWords(ProductRbQuery query);

    /**
     * 验证商品是否合法
     *
     * @param productIds productIds
     * @author Charlie
     * @date 2018/9/4 9:58
     */
    Map<String, Object> verifyProductIds(String productIds);

    /**
     * 验证活动是否合法
     *
     * @param productIds productIds
     * @author Charlie
     * @date 2018/9/4 9:58
     */
    Map<String, Object> verifyActivityProductIds(String productIds);

    /**
     * 查找所有图片
     * @param productId
     * @return
     */
    String findSummaryImages(Long productId);
}
