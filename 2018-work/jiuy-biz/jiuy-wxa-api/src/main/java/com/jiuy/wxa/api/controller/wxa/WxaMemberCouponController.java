package com.jiuy.wxa.api.controller.wxa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.model.order.ShopMemberOrderRbQuery;
import com.jiuy.rb.model.product.ProductRb;
import com.jiuy.rb.model.product.ProductRbQuery;
import com.jiuy.rb.model.product.ShopProductRb;
import com.jiuy.rb.model.product.ShopProductRbQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.order.IOrderService;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuy.rb.util.CouponAcceptVo;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.CouponNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.yujj.entity.order.Coupon;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.QRCodeUtil;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.entity.coupon.ShopCouponTemplate;
import com.store.entity.coupon.ShopMemberCoupon;
import com.store.service.coupon.ShopCouponTemplateService;
import com.store.service.coupon.ShopMemberCouponService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 会员优惠券
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/miniapp/coupon")
public class WxaMemberCouponController {
    private static final Log logger = LogFactory.get("小程序优惠券");


    @Autowired
    private ShopMemberCouponService shopMemberCouponService;

    @Autowired
    private ShopCouponTemplateService shopCouponTemplateService;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private IShopProductService iShopProductService;

    @Resource(name = "productService")
    private IProductService productService;

    /**
     * 全部领取商家优惠券
     *
     * @return
     */
    @RequestMapping("/getAllShopCouponTemplate")
    @ResponseBody
    public JsonResponse getAllShopCouponTemplate(@RequestParam(required = true) String shopCouponTemplateIds,
                                                 Long storeId, Long memberId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            if (StringUtils.isEmpty(shopCouponTemplateIds)) {
                throw new RuntimeException("优惠券模板ID集合为必填");
            }
            //获取数据
            String[] ids = shopCouponTemplateIds.split(",");
            int i = 0;
            for (String id : ids) {
                try {
                    //获取数据
                    CouponAcceptVo accept = new CouponAcceptVo(memberId, storeId, null, CouponSysEnum.WXA, CouponSendEnum.RECEIVE_SELF, CouponStateEnum.NOT_USE);
                    accept.setTempId(Long.valueOf(id));
                    couponServerNew.grant(accept);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Map<String, String> data = new HashMap<String, String>(1);
            data.put("getSuccessCount", String.valueOf(i));
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    @Autowired
    private ICouponServerNew couponServerNew;


    /**
     * 领取优惠券
     *
     * @param tempId   tempId
     * @param storeId  shopDetail
     * @param memberId memberDetail
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Aison
     * @date 2018/8/9 10:12
     */
    @RequestMapping("/getShopCouponTemplate")
    @ResponseBody
    public JsonResponse getShopCouponTemplate(@RequestParam(required = true) Long tempId, Long storeId, Long memberId) {

        checkLogin(storeId, memberId);
        JsonResponse jsonResponse = new JsonResponse();
        try {

            if (Biz.isEmpty(tempId)) {
                throw new RuntimeException("优惠券模板ID为必填");
            }
            //获取数据
            CouponAcceptVo accept = new CouponAcceptVo(memberId, storeId, null, CouponSysEnum.WXA, CouponSendEnum.RECEIVE_SELF, CouponStateEnum.NOT_USE);
            accept.setTempId(tempId);
            couponServerNew.grant(accept);
            Map<String, String> data = new HashMap<>();
            data.put("getSuccessCount", String.valueOf(1));
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 获取待领取商家优惠券模板列表
     */
    @RequestMapping("/getWaitGetShopCouponTemplateList")
    @ResponseBody
    public JsonResponse getWaitGetShopCouponTemplateList(Long storeId, Long memberId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            Map<String, Object> param = new HashMap<>(4);
            param.put("memberId", memberId);
            param.put("sysType", CouponSysEnum.WXA.getCode());
            param.put("sendType", CouponSendEnum.RECEIVE_SELF.getCode());
            param.put("publishUserId", storeId);
            List<CouponTemplateNew> temps = couponServerNew.waitGetCoupon(param);
            logger.info("获取待领取商家优惠券模板列表 storeId={},memberId={}, size={}", storeId, memberId, temps.size());

            List<Map<String, Object>> retMaps = new ArrayList<>(20);
            for (CouponTemplateNew temp : temps) {

//                long storeId = memberDetail.getMember().getStoreId().longValue();
                String getRule = temp.getGetRule();
                JSONObject sas = (JSONObject) JSONObject.parse(getRule);
                Object storeIdss = sas.get("storeIds");

                if (storeIdss == null) {
                    //平台发布
                    if (temp.getPublishUserId().longValue() == 0) {

                    } else {
                        if (temp.getPublishUserId().longValue() != storeId) {
                            //只能本店领取
                            continue;
                        }
                    }
                } else {
                    if (storeId != Long.parseLong(storeIdss.toString())) {
                        //只能指定门店领取
                        continue;
                    }
                }

                Map<String, Object> map = new HashMap<>();
                String name = temp.getName();
                map.put("id", temp.getId());
                map.put("storeId", storeId);
                map.put("name", name);
                map.put("fillinName", Biz.isEmpty(name) ? 0 : 1);
                map.put("money", temp.getPrice());
                map.put("discount", temp.getDiscount());
                map.put("couponType", temp.getCouponType());
                map.put("status", temp.getStatus());
                if (temp.getDeadlineType().intValue() == 1) {
                    map.put("validityStartTimeStr", Biz.formatDate(temp.getCreateTime(), "yyyy-MM-dd"));
                    map.put("validityEndTimeStr", Biz.formatDate(Biz.addDate(temp.getCreateTime(), temp.getDeadlineCount().intValue() * 24), "yyyy-MM-dd"));
                } else {
                    map.put("validityStartTimeStr", Biz.formatDate(temp.getDeadlineBegin(), "yyyy-MM-dd"));
                    map.put("validityEndTimeStr", Biz.formatDate(temp.getDeadlineEnd(), "yyyy-MM-dd"));
                }

                map.put("grantCount", temp.getIssueCount());
                map.put("limitMoney", temp.getLimitMoney());
                retMaps.add(map);
            }
            //获取数据
            return jsonResponse.setSuccessful().setData(retMaps);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 获取会员优惠券列表
     *
     * @param isValidityEnd 状态:’0：历史，1：可用
     * @return
     */
    @RequestMapping("/listbak")
    @ResponseBody
    public JsonResponse getMemberCouponListbak(Long storeId, Long memberId,
                                               @RequestParam("isValidityEnd") int isValidityEnd,
                                               @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                               @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            logger.info("会员优惠券列表isValidityEnd：" + isValidityEnd);
            if (!(isValidityEnd == 0 || isValidityEnd == 1)) {
                throw new RuntimeException("参数值错误isValidityEnd：" + isValidityEnd);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            SmallPage smallPage = shopMemberCouponService.getMemberCouponList(isValidityEnd, storeId, memberId,
                    new Page<ShopMemberCoupon>(current, size));
            logger.info("会员优惠券列表memberCouponList：" + smallPage);
            data.put("memberCouponList", smallPage);
            if (isValidityEnd == 1) {
                data.put("noUseCount", shopMemberCouponService.getMemberCouponListSize(isValidityEnd, storeId, memberId));
            } else {
                data.put("noUseCount", 0);
            }
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }

    }


    /**
     * 获取会员优惠券列表数量
     *
     * @return
     */
    @RequestMapping("/listCountNumber")
    @ResponseBody
    public JsonResponse listCountNumber(Long storeId, Long memberId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);


            Map<String, Integer> listMap = new HashMap<>();
            CouponRbNewQuery query = new CouponRbNewQuery();
            query.setMemberId(memberId);
            // 0 是已失效 1 可用的 2是已经使用的
            query.setAliveType(1);
            query.setStatus(0);

            int noUser = couponServerNew.listCountNumber(query);
            listMap.put("noUser", noUser);

            query.setAliveType(2);
            query.setStatus(null);
            int alreadyUser = couponServerNew.listCountNumber(query);
            listMap.put("alreadyUser", alreadyUser);

            query.setAliveType(0);
            int outTime = couponServerNew.listCountNumber(query);
            listMap.put("outTime", outTime);

            return jsonResponse.setSuccessful().setData(listMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }

    }


    /**
     * 获取会员优惠券列表
     *
     * @param isValidityEnd 状态: 0 是已失效 1 可用的 2是已经使用的
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResponse getMemberCouponList(Long storeId, Long memberId,
                                            @RequestParam("isValidityEnd") int isValidityEnd,
                                            @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            logger.info("会员优惠券列表isValidityEnd：" + isValidityEnd);
            CouponRbNewQuery query = new CouponRbNewQuery();
            query.setMemberId(memberId);
            query.setLimit(size);
            query.setOffset((current - 1) * size);
            // 0 是已失效 1 可用的 2是已经使用的
            query.setAliveType(isValidityEnd);
            if (isValidityEnd == 1) {
                query.setStatus(0);
            }
            Map<String, Object> restData = couponServerNew.myCouponListWxa(query);
            return jsonResponse.setSuccessful().setData(restData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }

    }

    /**
     * 描述 根据优惠券id。拿到指定的商品
     * * @param shopCouponId 红包ID
     *
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyq
     * @date 2018/8/13 13:38
     */
    @RequestMapping(value = "/getProductByCouponId")
    @ResponseBody
    public JsonResponse getProductByCouponId(@RequestParam(value = "shopCouponId") long shopCouponId, String keyword,
                                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

        List<String> ids = getRangIds(shopCouponId);

        ShopProductRbQuery query = new ShopProductRbQuery();
        query.setKeyword(keyword);
        query.setIds(ids);
        query.setLimit(pageSize);
        query.setSoldOut(1);
        query.setStatus(0);
        query.setOffset((page - 1) * pageSize);

        List<ShopProductRb> shopProductRbs = iShopProductService.selectByIds(query);

        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> retMap = new ArrayList<>();

        shopProductRbs.stream().forEach(shopProductRb -> {
            Map<String, Object> proMap = new HashMap<>();

            if (shopProductRb.getProductId() == 0) {
                JSONArray array = JSONObject.parseArray(shopProductRb.getSummaryImages());
                proMap.put("img", (array == null || array.size() < 1) ? "" : array.get(0).toString());
            } else {
//                ProductRb productRb = productService.getById(shopProductRb.getProductId());
                String summaryImages = productService.findSummaryImages(shopProductRb.getProductId());
                JSONArray array = JSONObject.parseArray(summaryImages);
                proMap.put("img", (array == null || array.size() < 1) ? "" : array.get(0).toString());
            }

            proMap.put("name", shopProductRb.getName());
            proMap.put("price", shopProductRb.getPrice());
            proMap.put("id", shopProductRb.getId());
            retMap.add(proMap);
        });

        resMap.put("productList", retMap);

        return new JsonResponse().setSuccessful().setData(resMap);
    }


    @RequestMapping(value = "/searchProduct")
    @ResponseBody
    public JsonResponse searchProduct(long shopCouponId, String keywords, Integer page, Integer pageSize) {

        List<String> ids = getRangIds(shopCouponId);

        ShopProductRbQuery query = new ShopProductRbQuery();
        query.setKeyword(keywords);
        query.setIds(ids);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);

        List<ShopProductRb> shopProductRbs = iShopProductService.selectByIds(query);

        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> retMap = new ArrayList<>();

        shopProductRbs.forEach(shopProductRb -> {
            Map<String, Object> proMap = new HashMap<>();

            proMap.put("img", JSONObject.parseArray(shopProductRb.getSummaryImages()).get(0).toString());
            proMap.put("name", shopProductRb.getName());
            proMap.put("price", shopProductRb.getPrice());
            proMap.put("id", shopProductRb.getId());
            retMap.add(proMap);
        });

        resMap.put("productList", retMap);

        return new JsonResponse().setSuccessful().setData(resMap);
    }


    private List<String> getRangIds(long shopCouponId) {
        //获取优惠券
        CouponRbNewQuery couponRbNewQuery = new CouponRbNewQuery();
        couponRbNewQuery.setId(shopCouponId);
        CouponRbNew oneCoupon = couponServerNew.getOneCoupon(couponRbNewQuery);

        if (oneCoupon == null) {
            return new ArrayList<String>();
        }

        //获取优惠券模板
        CouponTemplateNewQuery ct = new CouponTemplateNewQuery();
        ct.setId(oneCoupon.getTemplateId());
        CouponTemplateNew oneTemp = couponServerNew.getOneTemp(ct);

        String rangeIds = oneTemp.getRangeIds();

        String[] rangList = rangeIds.split(",");

        List<String> ids = new ArrayList<String>();

        for (String rangId : rangList) {
            if (rangId == null) {
                continue;
            }
            ids.add(rangId);
        }

        return ids;
    }

    /**
     * 获取会员优惠券信息
     *
     * @param id 优惠券id
     * @return
     */
    @RequestMapping("/getMemberCouponInfo")
    @ResponseBody
    public JsonResponse getMemberCouponInfo(Long storeId, Long memberId,
                                            @RequestParam("id") Long id) {
        logger.info("会员优惠券信息storeId:::::::::::::::" + storeId);
        JsonResponse jsonResponse = new JsonResponse();

        try {
            checkLogin(storeId, memberId);
            logger.info("会员优惠券信息id：" + id);
            if (id <= 0) {
                throw new RuntimeException("参数值错误id：" + id);
            }
            Map<String, Object> data = new HashMap<String, Object>();

            CouponRbNewQuery query = new CouponRbNewQuery();
            query.setId(id);
            CouponRbNew couponRbNew = couponServerNew.getOneCoupon(query);
            CouponTemplateNewQuery templateNewQuery = new CouponTemplateNewQuery();
            templateNewQuery.setId(couponRbNew.getTemplateId());
            CouponTemplateNew oneTemp = couponServerNew.getOneTemp(templateNewQuery);

            // ShopMemberCoupon shopMemberCoupon = shopMemberCouponService.getMemberCouponInfo(id);

            logger.info("生成二维码接口缓存key::::::::::::::::::::::::" + MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME);
            memcachedService.setCommon(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME, id + "", 3 * DateConstants.SECONDS_PER_MINUTE, couponRbNew);
            logger.info("生成二维码接口memcachedService:shopMemberCoupon:" + couponRbNew);
            logger.info("生成二维码接口memcachedService:shopMemberCoupon:" + memcachedService.getCommon(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME,
                    id + ""));
            Map<String, String> shopMemberCouponMap = new HashMap<String, String>();
            shopMemberCouponMap.put("id", couponRbNew.getId() + "");
            shopMemberCouponMap.put("money", couponRbNew.getPrice().toString());
            shopMemberCouponMap.put("discount", couponRbNew.getDiscount().toString());
            shopMemberCouponMap.put("couponType", oneTemp.getCouponType().toString());
            shopMemberCouponMap.put("name", couponRbNew.getTemplateName());
            String limitText = "";
            BigDecimal limitMoney = couponRbNew.getLimitMoney();
            if (limitMoney == null || limitMoney.compareTo(BigDecimal.ZERO) == 0) {
                limitText += "本券使用时不限订单金额";
            } else {
                limitText = limitText + "本券凡满订单总额" + limitMoney.toString() + "元可使用";
            }
            shopMemberCouponMap.put("limitText", limitText);
            data.put("shopMemberCoupon", shopMemberCouponMap);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 生成二维码接口http://dev.yujiejie.com:31080/miniapp/coupon/qrcode/src.do?storeId=1&memberId=1&memberCouponId=1&width=400&height=400
     *
     * @param response
     * @param request
     * @param memberCouponId
     * @param width
     * @param height
     */
    @RequestMapping("/qrcode/src")
    @ResponseBody
    public void qrcode(HttpServletResponse response, HttpServletRequest request, Long storeId, Long memberId,
                       @RequestParam("memberCouponId") long memberCouponId, @RequestParam("width") int width, @RequestParam("height") int height) {
        logger.info("会员优惠券信息storeId:::::::::::::::" + storeId);
        checkLogin(storeId, memberId);
        String content = Constants.SERVER_URL + "/index.html" + "?memberCouponId=" + memberCouponId;
        content += "&memberId=" + memberId + "&storeId=" + storeId;
        ShopMemberCoupon shopMemberCoupon = shopMemberCouponService.getMemberCouponInfo(memberCouponId);
        logger.info("生成二维码接口缓存key::::::::::::::::::::::::" + MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME);
        memcachedService.setCommon(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME, memberCouponId + "", 3 * DateConstants.SECONDS_PER_MINUTE, shopMemberCoupon);
        logger.info("生成二维码接口shopMemberCoupon:" + shopMemberCoupon);
        logger.info("生成二维码接口memcachedService:shopMemberCoupon:" + memcachedService.getCommon(MemcachedKey.GROUP_KEY_MEMBER_COUPON_EXPIRATION_TIME,
                memberCouponId + ""));
        logger.info("生成优惠券二维码，content+" + content);
        QRCodeUtil.getFile(request, response, content, "IMG" + memberId + "_" + memberCouponId, width, height, null);
    }

    /**
     * 获取订单可用的优惠券列表
     *
     * @param totalExpressAndMoney
     * @param current
     * @param size
     * @return
     */
    @RequestMapping("/getAvailableMemberCouponList")
    @ResponseBody
    public JsonResponse getAvailableMemberCouponList(@RequestParam("totalExpressAndMoney") double totalExpressAndMoney,
                                                     @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                     String productId,
                                                     Long storeId, Long memberId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            checkLogin(storeId, memberId);
            logger.info("获取订单可用的优惠券列表:getAvailableMemberCouponList;totalExpressAndMoney-" + totalExpressAndMoney + ";storeId-" + storeId
                    + ";memberId-" + memberId);
            Map<String, Object> data = new HashMap<>();
            data.put("wxaType", 1);
            data.put("productId", productId);
            data.put("price", totalExpressAndMoney + "");

            if (productId.split(",").length > 1) {
                couponServerNew.fillCoupon(data,storeId, memberId, CouponSysEnum.WXA, true, current, size);
            } else {
                couponServerNew.fillCoupon(data,storeId,memberId, CouponSysEnum.WXA, false, current, size);
            }


            // 没有多少优惠券 不用分页了
            Map<String, Object> page = new HashMap<>();
            page.put("current", 1);
            page.put("total", 1);
            page.put("pages", 1);
            page.put("offset", 0);
            page.put("size", 20);
            page.put("records", data.get("coupons"));
            page.put("limit", 20);
            page.put("isMore", 0);
            return jsonResponse.setSuccessful().setData(page);
        } catch (Exception e) {
            logger.error("获取订单可用的优惠券列表:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 检验是否登陆
     */
    private void checkLogin(Long storeId, Long memberId) {
        if (storeId == null || memberId == null) {
            logger.warn("登陆参数为空storeId={}，storeId={}", storeId, memberId);
            throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
        }
    }
}