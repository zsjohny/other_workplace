package com.jiuy.store.api.tool.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.service.common.express.IExpressModelService;
import com.jiuyuan.service.common.vedio.MainVedioService;
import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.GetuiUtil;
import com.store.entity.vedio.MainVedio;
import com.store.service.ProductServiceShop;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.HomeMenu;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.IHomeMenuService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.HomeFacade;
import com.store.service.MemberService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("home")
public class HomeController {
    private static final Log logger = LogFactory.get();
    @Autowired
    private IHomeMenuService homeMenuService;

    @Autowired
    private MainVedioService mainVedioService;

    @Autowired
    private HomeFacade homeFacade;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private IExpressModelService expressModelService;

    @Autowired
    private IProductMonitorService productMonitorService;

    /**
     * 获取app首页菜单列表
     */
    @RequestMapping(value = "/getHomeMenuList")
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse getHomeMenuList() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            List<HomeMenu> list = homeMenuService.getHomeMenuList();
            return jsonResponse.setSuccessful().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError("获取功能设置列表e：" + e.getMessage());
        }
    }

    /**
     * 首页模板数据加载
     *
     * @param pageQuery
     * @return
     */
    @RequestMapping("/homeFloor/auth")
    @Login
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse homeFloor(PageQuery pageQuery, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        List<JSONObject> modules = new ArrayList<JSONObject>();

//			int totalCount = homeFacade.getHomeFloorCount187(FloorType.ACTIVITY_PLACE, 0L);
//			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//			data.put("pageQuery", pageQueryResult);

        //首页模板楼层列表
        modules = homeFacade.getJsonList187(pageQuery, FloorType.SHOP_INDEX, userDetail);
        data.put("modules", modules);

        //首页门店二维码
        data.put("qrCode", homeFacade.getQRCode(userDetail));

        //首页未读消息数量，
        int msgNum = memberService.getAllNoReadCount(userDetail.getId());
        data.put("msgNum", msgNum);
//			System.out.println("首页未读消息数量:" + msgNum );
        return jsonResponse.setData(data);
    }

    /**
     * 获取首页的视频列表
     * @param request
     * @date:   2018/4/23 16:43
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/mainVedioList")
    @Cacheable("cache")
    public JsonResponse vedionMain(HttpServletRequest request) {
        try {
            List<MainVedio> mainVedios = mainVedioService.mainVedioList(getRequestMap(request));
            return new JsonResponse().setSuccessful().setData(mainVedios);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 每日上新
     * @param firstQueryTime 第一次请求时间
     * @param current
     * @param size
     * @date:   2018/4/23 18:56
     * @author: Aison
     */
    @RequestMapping({"/dailyNew","/dailyNew/auth"})
    @ResponseBody
    @Cacheable("cache")
    public JsonResponse productNew(Integer current,Integer size,Long firstQueryTime) {
        try{
            current = current == null || current <1  ? 1 : current;
            size = size == null || size <1 ? 20 : size;
            Page<ProductNew> products = productService.newProducts(new Page<>(current,size),firstQueryTime);
            return new JsonResponse().setSuccessful().setData(productMonitorService.fillPageProduct(products));
        }catch (Exception e) {
           return BizUtil.exceptionHandler(e);
        }
    }

    /**
     *
     * @param productId
     * @date:   2018/5/9 13:43
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/productExpress")
    public JsonResponse productExpress(Long productId) {

        try{
            BigDecimal express = expressModelService.countProductExpress(productId);
            return new JsonResponse().setSuccessful().setData(express);
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    @RequestMapping("/pushGexin")
    @ResponseBody
    public JsonResponse pushGexin(String cid){

       try{
           GetuiUtil.pushGeTui(cid,"[俞姐姐门店宝]您在[ 测试]采购的美丽的衣服[订单号：test]已经发货成功了。", "", "orderNo", "", 9+"" , System.currentTimeMillis()+"");
           return new JsonResponse().setSuccessful();
       }catch (Exception e) {
           return BizUtil.exceptionHandler(e);
       }
    }



    public Map<String, Object> getRequestMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> maps = new HashMap<String, Object>();
        String[] vals = null;
        for (Map.Entry<String, String[]> en : map.entrySet()) {
            vals = en.getValue();
            if (vals != null && vals.length > 0) {
                // 单个属性
                if (vals.length == 1 && !StringUtils.isBlank(vals[0])) {
                    maps.put(en.getKey(), BizUtil.filterSqlString(vals[0]));
                }
                // 数组属性
                if (vals.length > 1) {
                    maps.put(en.getKey().replace("[]", ""), en.getValue());
                }
            }
        }
        return maps;
    }
}
