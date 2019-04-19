package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.base.model.MyPage;
import com.jiuy.rb.enums.CouponPlatEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.model.product.BrandRb;
import com.jiuy.rb.model.product.ProductRbQuery;
import com.jiuy.rb.model.user.SupplierUserRb;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.product.IBrandService;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.user.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IStoreCouponNewService;
import com.jiuyuan.service.common.StoreCouponNewService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.ShopCoupon;
import com.store.service.ShopCouponService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 门店优惠券Controller
 *
 * @author Qiuyuefan
 */
@Controller
@RequestMapping("/shop/coupon")
@Login
public class ShopCouponController {

    private static final Log logger = LogFactory.get("ShopCouponController");

    @Autowired
    private ShopCouponService shopCouponService;


    @Autowired
    private ICouponServerNew couponServerNew;

    @Autowired
    private IBrandService iBrandService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IUserService iUserService;


    /**
     * 获取可用的优惠券
     *
     * @param userDetail userDetail
     * @param query      query
     * @param page       page
     * @param pageSize   pageSize
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Aison
     * @date 2018/8/6 16:24
     */
    @RequestMapping(value = "/getUnusedShopCouponList/auth")
    @ResponseBody
    public JsonResponse getUnusedShopCouponList(UserDetail<StoreBusiness> userDetail, CouponRbNewQuery query, Integer page, Integer pageSize) {


        query.setMemberId(null);
        query.setStatus(CouponStateEnum.NOT_USE.getCode());
        query.setStoreId(userDetail.getId());
        query.setUserType(CouponSysEnum.APP.getCode());
        query.setLimit(pageSize);
        query.setAliveType(1);
        query.setOffset((page - 1) * pageSize);
        return new JsonResponse().setSuccessful().setData(couponServerNew.myCouponList(query));

    }

    /**
     * 获取已使用优惠券
     *
     * @param userDetail userDetail
     * @param query      query
     * @param page       page
     * @param pageSize   pageSize
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Aison
     * @date 2018/8/6 16:24
     */
    @RequestMapping(value = "/getUsedShopCouponList/auth")
    @ResponseBody
    public JsonResponse getUsedShopCouponList(UserDetail<StoreBusiness> userDetail, CouponRbNewQuery query, Integer page, Integer pageSize) {

        query.setMemberId(null);
        //query.setStatus(CouponStateEnum.USED.getCode());
        query.setStoreId(userDetail.getId());
        query.setUserType(CouponSysEnum.APP.getCode());
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);
        query.setAliveType(2);
        return new JsonResponse().setSuccessful().setData(couponServerNew.myCouponList(query));

    }

    /**
     * 获取已失效的优惠券
     * 未使用已失效的优惠券
     *
     * @param userDetail userDetail
     * @param query      query
     * @param page       page
     * @param pageSize   pageSize
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Aison
     * @date 2018/8/6 16:24
     */
    @RequestMapping(value = "/getPassShopCouponList/auth")
    @ResponseBody
    public JsonResponse getPassShopCouponList(UserDetail<StoreBusiness> userDetail, CouponRbNewQuery query, Integer page, Integer pageSize) {

        query.setMemberId(null);
        //query.setStatus(CouponStateEnum.NOT_USE.getCode());
        query.setUserType(CouponSysEnum.APP.getCode());
        query.setStoreId(userDetail.getId());
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);
        query.setAliveType(0);
        return new JsonResponse().setSuccessful().setData(couponServerNew.myCouponList(query));

    }

    /**
     * 删除优惠券
     *
     * @param shopCouponId shopCouponId
     * @param userDetail userDetail
     * @author Aison
     * @date 2018/8/6 16:25
     * @return com.jiuyuan.web.help.JsonResponse
     */
    @RequestMapping(value = "/deleteShopCoupon")
    @ResponseBody
    public JsonResponse deleteShopCoupon(@RequestParam(value = "shopCouponId") long shopCouponId, UserDetail<StoreBusiness> userDetail) {

        couponServerNew.delCoupon(shopCouponId);
        return JsonResponse.getInstance().setSuccessful();
    }



    /**
     *描述 根据优惠券id。拿到指定的商品列表
     ** @param shopCouponId 红包ID
    * @param userDetail
     * @author hyq
     * @date 2018/8/13 13:38
     * @return com.jiuyuan.web.help.JsonResponse
     */
    @RequestMapping(value = "/getBrandByCouponId/auth")
    @ResponseBody
    public JsonResponse getProductByCouponId(long shopCouponId,UserDetail<StoreBusiness> userDetail,Integer page, Integer pageSize) {

        //获取优惠券
        CouponRbNewQuery couponRbNewQuery = new CouponRbNewQuery();
        couponRbNewQuery.setId(shopCouponId);
        CouponRbNew oneCoupon = couponServerNew.getOneCoupon(couponRbNewQuery);

        if(oneCoupon ==null){
            return new JsonResponse().setResultCode(ResultCode.PRODUCT_ERROR_NOT_EXSIT);
        }

        //获取优惠券模板
        CouponTemplateNewQuery ct = new CouponTemplateNewQuery();
        ct.setId(oneCoupon.getTemplateId());
        CouponTemplateNew oneTemp = couponServerNew.getOneTemp(ct);

        Integer platformType = oneTemp.getPlatformType();

        ProductRbQuery query = new ProductRbQuery();

        Long brandId;
        if(CouponPlatEnum.SUPPLIER.isThis(platformType)){
            Long supplierId = oneTemp.getPublishUserId();
            SupplierUserRb supplierUser = iUserService.getSupplierUser(Integer.parseInt(supplierId+""));
            brandId = supplierUser.getBrandId();
            query.setBrandId(brandId);
        }else{
            String rangeIds = oneTemp.getRangeIds();

            String[] rangList = rangeIds.split(",");

            List<String> ids = new ArrayList<String>();
            for (String rangId : rangList){
                if(rangId == null){
                    continue;
                }
                ids.add(rangId);
            }
            query.setIds(ids);
        }
        query.setState(6);
        query.setDelState(0);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);

        Map<String, Object> productByBrandId = iProductService.getProductByBrandId(query);

        //获取品牌列表
        //List<BrandRb> brandRbs = iBrandService.selectByIds(ids);

        return new JsonResponse().setSuccessful().setData(productByBrandId);

    }

    /**
     *描述 APP优惠券指定商品查询
     ** @param shopCouponId
    * @param keywords
    * @param userDetail
    * @param page
    * @param pageSize
     * @author hyq
     * @date 2018/8/14 17:21
     * @return com.jiuyuan.web.help.JsonResponse
     */
    @RequestMapping(value = "/searchProduct")
    @ResponseBody
    public JsonResponse getProductByCouponId(long shopCouponId,String keywords,UserDetail<StoreBusiness> userDetail,Integer page, Integer pageSize) {

        if(StringUtils.isEmpty(keywords)){
            return new JsonResponse().setSuccessful().setData(null);
        }

        //获取优惠券
        CouponRbNewQuery couponRbNewQuery = new CouponRbNewQuery();
        couponRbNewQuery.setId(shopCouponId);
        CouponRbNew oneCoupon = couponServerNew.getOneCoupon(couponRbNewQuery);

        if(oneCoupon ==null){
            return new JsonResponse().setResultCode(ResultCode.PRODUCT_ERROR_NOT_EXSIT);
        }

        //获取优惠券模板
        CouponTemplateNewQuery ct = new CouponTemplateNewQuery();
        ct.setId(oneCoupon.getTemplateId());
        CouponTemplateNew oneTemp = couponServerNew.getOneTemp(ct);

        Integer platformType = oneTemp.getPlatformType();

        ProductRbQuery query = new ProductRbQuery();

        Long brandId;
        if(CouponPlatEnum.SUPPLIER.isThis(platformType)){
            Long supplierId = oneTemp.getPublishUserId();
            SupplierUserRb supplierUser = iUserService.getSupplierUser(Integer.parseInt(supplierId+""));
            brandId = supplierUser.getBrandId();
            query.setBrandId(brandId);
        }else{
            String rangeIds = oneTemp.getRangeIds();

            String[] rangList = rangeIds.split(",");

            List<String> ids = new ArrayList<String>();
            for (String rangId : rangList){
                if(rangId == null){
                    continue;
                }
                ids.add(rangId);
            }
            query.setIds(ids);
        }

        query.setKeyword(keywords);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);

        return new JsonResponse().setSuccessful().setData(iProductService.getProductByKeyWords(query));
    }

    /**
     *描述 获取优惠券下面的指定品牌
     ** @param shopCouponId
     * @author hyq
     * @date 2018/8/14 18:11
     * @return java.util.List<java.lang.String>
     */
    private List<String>  getRangIds(long shopCouponId){

        //获取优惠券
        CouponRbNewQuery couponRbNewQuery = new CouponRbNewQuery();
        couponRbNewQuery.setId(shopCouponId);
        CouponRbNew oneCoupon = couponServerNew.getOneCoupon(couponRbNewQuery);

        if(oneCoupon ==null){
            return new ArrayList<String>();
            //return new JsonResponse().setResultCode(ResultCode.PRODUCT_ERROR_NOT_EXSIT);
        }

        //获取优惠券模板
        CouponTemplateNewQuery ct = new CouponTemplateNewQuery();
        ct.setId(oneCoupon.getTemplateId());
        CouponTemplateNew oneTemp = couponServerNew.getOneTemp(ct);

        String rangeIds = oneTemp.getRangeIds();

        String[] rangList = rangeIds.split(",");

        List<String> ids = new ArrayList<String>();
        for (String rangId : rangList){
            if(rangId == null){
                continue;
            }
            ids.add(rangId);
        }
        return ids;
    }
















    /**
     * 获取可用的优惠券列表
     *
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/getUnusedShopCouponListBak/auth")
    @ResponseBody
    public JsonResponse getUnusedShopCouponListBak(UserDetail<StoreBusiness> userDetail, PageQuery pageQuery) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkUser(userDetail);
            logger.info("获取可用的优惠券列表门店ID:" + userDetail.getId());
            //取可用优惠券列表
            //范围类型 0: 通用, 1:分类, 2:限额订单, 4:免邮，  5：品牌
            int recordCount = shopCouponService.getUnusedShopCouponListCount(userDetail.getId());
            //获取可用的
            List<ShopCoupon> couponList = shopCouponService.getUnusedShopCouponList(userDetail.getId(), OrderCouponStatus.UNUSED, pageQuery);
            for (ShopCoupon shopCoupon : couponList) {
                String templateName = shopCoupon.getTemplateName();//优惠券名称
                if (shopCoupon.getSupplierId() != null &&
                        shopCoupon.getSupplierId() != 0) {
                    templateName = shopCoupon.getPublisher() + "-" + templateName;
                }
                shopCoupon.setTemplateName(templateName);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
            data.put("couponList", couponList);
            data.put("pageQuery", pageQueryResult);
            return jsonResponse.setSuccessful().setData(data);
        } catch (TipsMessageException e) {
            logger.info("获取可用的优惠券列表失败:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        } catch (Exception e) {
            logger.info("获取可用的优惠券列表失败:" + e.getMessage());
            return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
        }

    }


    /**
     * 获取失效的优惠券列表
     *
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/getUsedShopCouponListbak/auth")
    @ResponseBody
    public JsonResponse getUsedShopCouponListbak(UserDetail<StoreBusiness> userDetail, PageQuery pageQuery) {
        logger.info("获取失效的优惠券列表门店ID:" + userDetail.getId());
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkUser(userDetail);
            //获取失效的优惠券列表
            //范围类型 0: 通用, 1:分类, 2:限额订单, 4:免邮，  5：品牌
            int recordCount = shopCouponService.getUsedShopCouponListCount(userDetail.getId());
            List<ShopCoupon> usedCouponList = shopCouponService.getUsedShopCouponList(userDetail.getId(), OrderCouponStatus.USED, pageQuery);
            for (ShopCoupon shopCoupon : usedCouponList) {
                String templateName = shopCoupon.getTemplateName();//优惠券名称
                if (shopCoupon.getSupplierId() != null &&
                        shopCoupon.getSupplierId() != 0) {
                    templateName = shopCoupon.getPublisher() + "-" + templateName;
                }
                shopCoupon.setTemplateName(templateName);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("couponList", usedCouponList);
            PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
            data.put("pageQuery", pageQueryResult);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            logger.error("获取失效的优惠券列表失败:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }

    }

    /**
     * 删除优惠券
     *
     * @param shopCouponId
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/deleteShopCouponbak/auth")
    @ResponseBody
    public JsonResponse deleteShopCouponbak(@RequestParam(value = "shopCouponId") long shopCouponId, UserDetail<StoreBusiness> userDetail) {
        logger.info("删除优惠券shopCouponId:" + shopCouponId + "门店ID:" + userDetail.getId());
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkUser(userDetail);
            //删除优惠券
            int record = shopCouponService.deleteShopCoupon(shopCouponId);
            if (record == 1) {
                return jsonResponse.setSuccessful();
            } else {
                logger.error("删除优惠券失败shopCouponId:" + shopCouponId);
                return jsonResponse.setError("删除优惠券失败");
            }
        } catch (Exception e) {
            logger.error("删除优惠券失败:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }

    }

    private void checkUser(UserDetail<StoreBusiness> userDetail) {
        if (userDetail.getUserDetail().getId() <= 0 || userDetail.getUserDetail() == null) {
            throw new RuntimeException("未登录，请先登录");
        }
    }
}