package com.jiuy.operator.controller.newController.coupon;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.model.product.ProductRb;
import com.jiuy.rb.model.product.ShopProductRb;
import com.jiuy.rb.model.user.StoreBusinessRb;
import com.jiuy.rb.model.user.SupplierUserRb;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuy.rb.service.user.IUserService;
import com.jiuy.rb.util.CouponAcceptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 优惠券controller
 *
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/8/3 9:11
 */
@Controller
@ResponseBody
@RequestMapping("/admin/coupon")
public class CouponController{


    @Resource(name = "couponServerNew")
    private ICouponServerNew  couponServerNew;

    @Resource(name = "userService")
    private IUserService iUserService;

    @Resource(name = "productService")
    private IProductService iProductService;

    @Resource(name = "shopProductServiceRb")
    private IShopProductService iShopProductService;


    /**
     *  添加优惠券模板
     *
     * @param couponTemplate couponTemplate
     * @author Aison
     * @date 2018/8/3 9:13
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/addCouponTemplate")
    public ResponseResult addCouponTemplate(CouponTemplateNew couponTemplate) {

        couponServerNew.addCouponTemplate(couponTemplate);
        return ResponseResult.SUCCESS;
    }

    /**
     *描述 新运营平台模板查询
     ** @param couponTemplate  优惠券模板参数
     * @author hyq
     * @date 2018/8/17 15:36
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/selectCouponTemplate")
    public ResponseResult selectCouponTemplate(CouponTemplateNewQuery couponTemplate) {

//        CouponTemplateNewQuery couponTemplateNew = new CouponTemplateNewQuery();
//        String name = couponTemplate.getName();
//        Integer sendType = couponTemplate.getSendType();
//        Integer sysType = couponTemplate.getSysType();
//        Integer status = couponTemplate.getStatus();
//        BigDecimal price = couponTemplate.getPrice();
//        BigDecimal discount = couponTemplate.getDiscount();
//        Date deadlineBegin = couponTemplate.getDeadlineBegin();
//        Date deadlineEnd = couponTemplate.getDeadlineEnd();
//        if(StringUtils.isNotEmpty(name)){
//            name
//        }

        return ResponseResult.instance().success(couponServerNew.selectCouponTemplateList(couponTemplate));
    }

    /**
     *描述 根据供应商ID查询供应商
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:53
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/getSupplierById")
    public ResponseResult getSupplierById(String id) {

        SupplierUserRb supplierUser = iUserService.getSupplierUser(Integer.parseInt(id));
        if(supplierUser==null){
            return ResponseResult.instance().failed().setMsg("未找到该商家");
        }

        return ResponseResult.instance().success(supplierUser);
    }

    /**
     *描述 根据店铺ID查询店铺
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:54
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/getStoreById")
    public ResponseResult getStoreById(long id) {

        StoreBusinessRb storeBusinessById = iUserService.getStoreBusinessByIdNumber(id,id+"");
        if(storeBusinessById==null){
            return ResponseResult.instance().failed().setMsg("未找到该商家");
        }
        return ResponseResult.instance().success(storeBusinessById);
    }

    /**
     *描述 获取店铺商品
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:54
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/getStoreProductById")
    public ResponseResult getStoreProductById(long id) {

        ProductRb byId = iProductService.getById(id);
        if(byId==null){
            return ResponseResult.instance().failed().setMsg("未找到该商品");
        }
        return ResponseResult.instance().success(byId);
    }

    /**
     *描述 获取小程序商品
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:54
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/getWxsProductById")
    public ResponseResult getWxsProductById(long id) {
        ShopProductRb byId = iShopProductService.getById(id);
        if(byId==null){
            return ResponseResult.instance().failed().setMsg("未找到该商品");
        }
        return ResponseResult.instance().success(byId);
    }

    /**
     *描述 优惠券首页
     ** @param couponRbNewQuery
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/selectSendCouponInfo")
    public  ResponseResult selectSendCouponInfo(CouponRbNewQuery couponRbNewQuery){

        return ResponseResult.instance().success(couponServerNew.selectSendCouponInfo(couponRbNewQuery));
    }

    /**
     *描述 获取优惠券发送记录
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/selectSendCouponInfoCollect")
    public  ResponseResult selectSendCouponInfoCollect(Long id){

        return ResponseResult.instance().success(couponServerNew.selectSendCouponInfoCollect(id));
    }


    /**
     *  自主发放
     *
     * @param templateId templateId
     * @author Aison
     * @date 2018/8/3 9:14
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("sendCoupon")
    public ResponseResult sendCoupon(Long templateId,Integer sys,Long storeId,String orderNo,Integer sendType,Long memberId) {

        CouponAcceptVo accept = new CouponAcceptVo(memberId,storeId,orderNo,
                null,null,CouponStateEnum.NOT_USE);


        accept.setSendEnum(CouponSendEnum.get(sendType));
        accept.setTempId(templateId);
        accept.setStatus(0);
        accept.setSysEnum(CouponSysEnum.get(sys));

        couponServerNew.grant(accept);
        return ResponseResult.SUCCESS;
    }


}