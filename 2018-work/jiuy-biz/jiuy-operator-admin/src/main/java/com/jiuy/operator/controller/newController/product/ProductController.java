package com.jiuy.operator.controller.newController.product;


import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.model.product.ProductAuditRbQuery;
import com.jiuy.rb.service.product.IProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品相关的controller
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/12 19:19
 * @Copyright 玖远网络
 */
@Controller
@ResponseBody
@RequestMapping("/admin/")
public class ProductController {

    @Resource(name = "productService")
    private  IProductService productService;


    /**
     * 获取某条商品通过id
     * @param productId 商品id
     * @author Aison
     * @date 2018/6/21 13:54
     */
    @RequestMapping("getNormalProductById")
    public ResponseResult getNormalProductById(Long productId) {

        return ResponseResult.instance().success(productService.getById(productId));
    }

    /**
     * 获取某条商品通过id
     * @param productIds 商品ids
     * @author Aison
     * @date 2018/6/21 13:54
     */
    @RequestMapping("verifyProductIds")
    public ResponseResult verifyProductIds(String productIds) {
        return ResponseResult.instance().success(productService.verifyProductIds (productIds));
    }

    /**
     * 获取某条商品通过id
     * @param productIds 商品ids
     * @author Aison
     * @date 2018/6/21 13:54
     */
    @RequestMapping("verifyActivityProductIds")
    public ResponseResult verifyActivityProductIds(String productIds) {
        return ResponseResult.instance().success(productService.verifyActivityProductIds (productIds));
    }

    /**
     * 获取活动商品
     * @param productId 商品id
     * @author Aison
     * @date 2018/6/21 13:54
     */
    @RequestMapping("getActivityProductById")
    public ResponseResult getActivityProductById(Long productId) {

        return ResponseResult.instance().success(productService.getActivityProductById(productId));
    }


    /**
     * 审核商品通过
     *
     * @param productAuditId 审核表id
     * @author Aison
     * @date 2018/6/13 9:27
     */
    @RequestMapping("productAuditPass")
    public ResponseResult productAuditPass(Long productAuditId) {

        productService.auditProduct(productAuditId,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }

    /**
     * 审核商品不通过
     *
     * @param productAuditId 审核表id
     * @param noPassReason 不通过原因
     * @author Aison
     * @date 2018/6/13 9:28
     */
    @RequestMapping("productAuditNoPass")
    public ResponseResult productAuditNoPass(Long productAuditId,String noPassReason) {

        productService.productAuditNoPass(productAuditId,noPassReason,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }


    /**
     * 列表查询
     *
     * @param query query
     * @author Aison
     * @date 2018/6/13 15:32
     */
    @RequestMapping("getSearchProductAuditList")
    public ResponseResult productAuditList(ProductAuditRbQuery query) {

        return ResponseResult.instance().success(productService.productAuditList(query));
    }

    /**
     * 获取某个商品的审核信息
     *
     * @param productAuditId 审核表id
     * @author Aison
     * @date 2018/6/13 15:05
     */
    @RequestMapping("getProductAuditInfo")
    public ResponseResult getProductAuditInfo(Long productAuditId) {

        return ResponseResult.instance().success(productService.getProductAuditInfo(productAuditId));
    }

}
