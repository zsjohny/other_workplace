package com.jiuy.wxa.api.controller.wxa;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.product.SecondBuyActivityRbQuery;
import com.jiuy.rb.model.product.TeamBuyActivityRbQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuyuan.entity.query.PageQuery;
import com.store.dao.mapper.MemberMapper;
import com.store.entity.member.ShopMember;
import com.store.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
//import com.jiuy.web.helper.JsonResponse;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.ShopTagNavigation;
import com.jiuyuan.entity.newentity.StoreArticle;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.entity.coupon.ShopCouponTemplate;
import com.store.service.ShopTagNavigationService;
import com.store.service.StoreArticleService;
import com.store.service.WxaHomePageShopProductNewFacade;
import com.store.service.coupon.ShopCouponTemplateService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

import static com.store.service.MessageReceiveService.uuidRelationMap;


/**
 * @author xxx
 */
@Controller
@RequestMapping("/miniapp/homepage")
public class WxaHomepageController {
    private static final Log logger = LogFactory.get("小程序首页");

    @Autowired
    private ShopCouponTemplateService shopCouponTemplateService;

    @Autowired
    private WxaHomePageShopProductNewFacade wxaHomePageShopProductNewFacade;

    @Autowired
    private StoreBusinessNewService storeBusinessNewService;
    @Autowired
    private StoreArticleService storeArticleService;
    @Autowired
    private ShopTagNavigationService shopTagNavigationService;

    @Resource(name = "shopProductServiceRb")
    private IShopProductService shopProductService;

    @Autowired
    private ICouponServerNew couponServerNew;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;
    /**
     * 从1.2版本开始的首页商品列表接口（该接口作废可立刻删除）
     * 获取小程序首页商品
     *
     * @param current 当前是第几页
     * @param size    每页显示条数
     * @return
     */
    @RequestMapping("/homeProductList")
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse homeProductList(Long storeId,
                                        Integer type,
                                        @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

        try {
            return new JsonResponse().setSuccessful().setData(wxaHomePageShopProductNewFacade.
                    getHomeProductList(type, new Page<>(current, size), storeId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("小程序首页" + e.getMessage());
            return new JsonResponse().setError(e.getMessage());
        }

    }

    @RequestMapping("/navigationProductList")
    @ResponseBody
    public JsonResponse navigationProductList(Long storeId,
                                              long tagId,
                                              @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        JsonResponse jsonResponse = new JsonResponse();
        try {

            return jsonResponse.setSuccessful().setData(wxaHomePageShopProductNewFacade.
                    getNavigationProductList(tagId, new Page<>(current, size), storeId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("小程序标签商品列表" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }

    }

    /**
     * 获取客服热线按钮状态
     */
    @RequestMapping("/hasHotOnline")
    @ResponseBody
    public JsonResponse hasHotOnline(Long storeId) {
        JsonResponse jsonResponse = new JsonResponse ();
        try {
            StoreBusiness storeBusiness = shopDetailBack(storeId).getStoreBusiness();
            Map<String, Object> data = new HashMap<String, Object> ();
            if (null != storeBusiness) {
                data.put("hasHotonline", storeBusiness.getHasHotonline());//是否打开客服热线 0：关闭 1：开启
                data.put("hotOnline", storeBusiness.getHotOnline());//客服热线
            }
            return jsonResponse.setSuccessful().setData(data);
        } catch (TipsMessageException e) {
            logger.info(e.getFriendlyMsg());
            return jsonResponse.setError(e.getFriendlyMsg());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return jsonResponse.setError("服务器开小差，请稍后再试！");

        }
    }

    /**
     * 首页接口（包括轮播图模块、公告模块、文章模块）
     *
     * @return
     */
    @RequestMapping("/home")
    @ResponseBody
    public JsonResponse home(Long storeId,Long memberId) {
        long time1 = System.currentTimeMillis ();
        ShopDetail shopDetail = shopDetailBack(storeId);
        try {
            JsonResponse jsonResponse = new JsonResponse();
            StoreBusiness storeBusiness = shopDetail.getStoreBusiness();
            Map<String, Object> data = new HashMap<String, Object>();//wxaHomePageShopProductNewFacade.getHomePageProductList(0,new Page<ShopProduct>(current,size),storeBusiness);
            String storeNotice = "";
            List<String> storeDisplayImages = new ArrayList<String>();
            List<StoreArticle> articleList = new ArrayList<StoreArticle>();
            List<Map<String, String>> shopNavigationMap = new ArrayList<>();
            if (storeBusiness != null) {
                StoreBusiness storeBusinessNew = storeBusinessNewService.findHomeStoreById (storeBusiness.getId ());
                storeNotice = storeBusinessNew.getStoreNotice ();
                String images = storeBusinessNew.getStoreDisplayImages ();
                if (! StringUtils.isEmpty (images)) {
                    storeDisplayImages = Arrays.asList (images.split (","));
                }

                int wxaArticleShow = storeBusiness.getWxaArticleShow ();//小程序首页是否小时文章模块：0不显示、1显示
                if (wxaArticleShow == 1) {
//                    articleList = storeArticleService.getStoreArticleListByStoreId (storeBusiness.getId ());

                    StoreArticle query = new StoreArticle ();
                    query.setStoreId (storeBusinessNew.getId ());
                    query.setStatus (StoreArticle.article_status_normal);
                    query.setPublicState (1);
                    articleList = storeArticleService.listArticlesSort (query,  new PageQuery (1,5), false);
//
                }
                //获取标签导航
                shopNavigationMap = getShopNavigationMap (storeBusiness.getId ());
                data.put ("hasHotonline", storeBusinessNew.getHasHotonline ());//是否打开客服热线 0：关闭 1：开启
                data.put ("hotOnline", storeBusinessNew.getHotOnline ());//客服热线
            }
            data.put ("storeDisplayImages", storeDisplayImages);//门店头图
            data.put ("storeNotice", storeNotice);//商家公告
            data.put ("articleList", articleList);//文章列表
            data.put ("navigationList", shopNavigationMap);//标签导航列表

            long time2 = System.currentTimeMillis ();
            long time3 = time2 - time1;
//			 logger.info("小程序首页接口总耗时["+time3+"毫秒]");

//			 logger.info("小程序首页获取获取标签导航列表接口，data："+JSON.toJSONString(data));
            return jsonResponse.setSuccessful ().setData (data);
        } catch (Exception e) {
            e.printStackTrace ();
            logger.error ("小程序首页Controller:" + e.getMessage ());
            return new JsonResponse ().setError (e.getMessage ());
        }

    }

    /**
     * 获取标签导航列表
     *
     * @param storeId
     * @return
     */
    public List<Map<String, String>> getShopNavigationMap(long storeId) {
//			logger.info("小程序首页获取获取标签导航列表开始，storeId："+storeId);
        List<Map<String, String>> list = new ArrayList<Map<String, String>> ();//shopTagNavigationMapper.selectTagNavigationMap(storeId);
        List<ShopTagNavigation> shopNavigationList = shopTagNavigationService.findShopNavigationListById (storeId);
//        List<ShopTagNavigation> shopNavigationList = shopTagNavigationService.getShopNavigationList (storeId);
//			logger.info("小程序首页获取获取标签导航列表开始，shopNavigationList："+JSON.toJSONString(shopNavigationList));
        for (ShopTagNavigation shopTagNavigation : shopNavigationList) {
            long shopTagNavigationId = shopTagNavigation.getId ();
            long tagId = shopTagNavigation.getTagId ();
            Map<String, String> map = new HashMap<String, String> ();
            map.put ("id", String.valueOf (shopTagNavigationId));
            map.put ("tagId", String.valueOf (tagId));
            map.put ("navigationName", shopTagNavigation.getNavigationName ());
            map.put ("navigationImage", shopTagNavigation.getNavigationImage ());
            list.add (map);
        }
//			logger.info("小程序首页获取获取标签导航列表开始，list："+JSON.toJSONString(list));
        return list;
    }


    /**
     * 小程序首页活动
     *
     * @param shopDetail shopDetail
     * @param request    request
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie
     * @date 2018/8/7 15:39
     */
    @RequestMapping("/homeActivity")
    @ResponseBody
    public JsonResponse homeActivity379(
            @RequestParam("storeId")Long storeId,
            @RequestParam(value = "memberId",required = false)Long memberId,
            ShopDetail shopDetail, HttpServletRequest request) {
        if (memberId!=null){
            logger.info("发送消息 memberId={}, storeId={}", memberId, storeId);
            ShopMember user = memberMapper.findMemberById(memberId);
            if (user != null) {
                uuidRelationMap.put(user.getBindWeixin(), storeId);
            }
        }
//        checkStore(shopDetail);
        Map<String, Object> result;
        try {
//            StoreBusiness storeBusiness = shopDetail.getStoreBusiness();
            String wxVersion = request.getHeader("version");
            result = shopProductService.homeActivity(wxVersion, storeId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
        return JsonResponse.getInstance().setSuccessful().setData(result);
    }





    /**
     * 从1.2版本开始的首页商品列表接口
     *
     * @return
     */
    @RequestMapping("/waitGetShopCouponTemplateInfo")
    @ResponseBody
    public JsonResponse waitGetShopCouponTemplateInfo(
            @RequestParam("storeId")Long storeId,
            @RequestParam("memberId")Long memberId,
            ShopDetail shopDetail, MemberDetail memberDetail
    ) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            Map<String, Object> data = new HashMap<>(4);
            logger.info("待获取优惠券 storeId={},memberId={}", storeId, memberId);
            if (memberId != null && memberId > 0) {

                Map<String, Object> param = new HashMap<>(4);
                param.put("memberId", memberId);
                param.put("sysType", CouponSysEnum.WXA.getCode());
                param.put("publishUserId", storeId);
                List<CouponTemplateNew> templates = couponServerNew.waitGetCoupon(param);
                BigDecimal money = BigDecimal.ZERO;
                long lastCouponTemplateId = 0;

                int couponCount = 0;
                for (CouponTemplateNew template : templates) {

                    if (template == null) {
                        continue;
                    }
                    String getRule = template.getGetRule();
                    JSONObject sas = (JSONObject) JSONObject.parse(getRule);
                    Object storeIdss = sas.get("storeIds");

                    if (storeIdss == null) {
                        //平台发布
                        if (template.getPublishUserId().longValue() == 0) {

                        } else {
                            if (template.getPublishUserId().longValue() != storeId) {
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
                    couponCount++;

                    money = money.add(template.getPrice());
                    Long id = template.getId();
                    if (id > lastCouponTemplateId) {
                        lastCouponTemplateId = id;
                    }
                }
                data.put("money", money);
                data.put("count", couponCount);
                data.put("lastCouponTemplateId", lastCouponTemplateId);
            } else {
                logger.error("用户未登录,没有memberId");
                data.put("money", "0");
                data.put("count", "0");
                data.put("lastCouponTemplateId", "0");
            }
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 从1.2版本开始的首页商品列表接口
     * 获取小程序首页商品
     *
     * @param current 当前是第几页
     * @param size    每页显示条数
     * @return
     */
    @RequestMapping("/listnew")
    @ResponseBody
    public JsonResponse homepageListNew(ShopDetail shopDetail, MemberDetail memberDetail,
                                        @RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
                                        @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", required = false, defaultValue = "50") Integer size) {
        StoreBusiness store = shopDetail.getStoreBusiness();
        long storeId = store.getId();
        String businessName = store.getBusinessName();
        logger.info("1.2小程序首页接口，被调用说明还有小程序版本没有升级，请联系升级到最新版小程序，storeId:" + storeId + ",businessName:" + businessName + ",categoryId[" + categoryId + "]");
        long time1 = System.currentTimeMillis();
//        checkStore(shopDetail);
        try {
            JsonResponse jsonResponse = new JsonResponse();
            StoreBusiness storeBusiness = shopDetail.getStoreBusiness();
            //分类永远为零，经产品确认
            Map<String, Object> data = wxaHomePageShopProductNewFacade.getHomePageProductList(0, new Page<ShopProduct>(current, size), storeBusiness);


            if (storeBusiness != null) {
                StoreBusiness storeBusinessNew = storeBusinessNewService.getById(storeBusiness.getId());
                data.put("storeNotice", storeBusinessNew.getStoreNotice());
                String images = storeBusinessNew.getStoreDisplayImages();
                if (!StringUtils.isEmpty(images)) {
                    List<String> storeDisplayImages = Arrays.asList(images.split(","));
                    data.put("storeDisplayImages", storeDisplayImages);
                } else {
                    data.put("storeDisplayImages", "");
                }
            } else {
                data.put("storeNotice", "");
            }
            long time2 = System.currentTimeMillis();
            long time3 = time2 - time1;
            logger.info("小程序首页接口总耗时[" + time3 + "毫秒]");
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("小程序首页Controller:" + e.getMessage());
            return new JsonResponse().setError(e.getMessage());
        }

    }

    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    @RequestMapping("/getArticleDetail")
    @ResponseBody
    public JsonResponse getArticleDetail(Long storeId, Long memberId, @RequestParam(value = "articleId", required = true) long articleId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            if (storeId==null||memberId==null||storeId==0||memberId==0){
                return jsonResponse.setError("会员信息或者商家信息不能为空，该接口需要登陆，请排除问题");
            }
//            checkStore(shopDetail);
//            checkMember(memberDetail);
            StoreArticle storeArticle = storeArticleService.getArticleById(articleId);
            if (storeArticle == null) {
                return jsonResponse.setError("该文章不存在！");
            }
            try {
                String articleContext = storeArticle.getArticleContext();
                if (StringUtils.isNotBlank(articleContext)) {
                    storeArticle.setArticleContext(URLDecoder.decode(articleContext, "utf-8"));
                }
            } catch (UnsupportedEncodingException e) {
                logger.warn("文章内容解码失败 articleId[{}]", storeArticle.getId());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("title", storeArticle.getArticleTitle());
            map.put("context", storeArticle.getArticleContext());
            return jsonResponse.setSuccessful().setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取文章详情Controller:" + e.getMessage());
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 检验登陆状态
     */

    private void checkLogin(Long storeId, Long memberId) {
        if (storeId == null || memberId == null) {
            logger.warn("登陆参数为空storeId={}，memberId={}", storeId, memberId);
            throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
        }
    }


    private ShopDetail shopDetailBack(Long storeId) {
        ShopDetail shopDetail = new ShopDetail();
//        StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(storeId);
        StoreBusiness storeBusiness = storeBusinessNewService.findStoreBusinessById(storeId);
        if (storeBusiness != null) {
            shopDetail.setStoreBusiness(storeBusiness);
            return shopDetail;
        }else {
            throw new RuntimeException("店铺信息不存在");
        }
    }
    /**
     * 查询member信息
     * @param memberId
     * @return
     */
    private MemberDetail memberDetailBack(Long memberId) {
        MemberDetail memberDetail = new MemberDetail();
        ShopMember member = memberService.getMemberById(memberId);
        if (member != null) {
            memberDetail.setMember(member);
            return memberDetail;
        }else {
            throw new RuntimeException("会员信息不存在");
        }
    }




    /**
     * 团购活动列表
     *
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie
     * @date 2018/7/29 19:44
     */
    @RequestMapping("/teamActivityList")
    @ResponseBody
    public JsonResponse teamActivityList(TeamBuyActivityRbQuery query) {
        JsonResponse jsonResponse = JsonResponse.getInstance();
        try {
            return jsonResponse.setSuccessful().setData(shopProductService.listTeamBuyActivityWithOrder(query));
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
    }
    /**
     * 秒杀活动列表
     *
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie
     * @date 2018/7/29 19:44
     */
    @RequestMapping("/secondActivityList")
    @ResponseBody
    public JsonResponse secondActivityList(SecondBuyActivityRbQuery query) {
        JsonResponse jsonResponse = JsonResponse.getInstance();
        try {
            return jsonResponse.setSuccessful().setData(shopProductService.listSecondBuyActivityWithOrder(query));
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
    }
}
