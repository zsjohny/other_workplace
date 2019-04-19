package com.jiuy.store.tool.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.annotations.TableField;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.util.VersionUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.HomeFacade;
import com.store.service.MemberService;
import com.store.service.NJShopMemberOrderService;
import com.store.service.OrderService;
import com.store.service.ShopProductService;
import com.store.service.StoreAuditServiceShop;
import com.store.service.StoreUserService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;



/**
 * 商家
 * @author Administrator
 *
 */
@Controller
@Login
@RequestMapping("/mobile/member")
@SuppressWarnings("unused")
public class ShopMemberInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(ShopMemberInfoController.class);
    Log log = LogFactory.get();

    @Autowired
	private StoreUserService storeUserService;
	
	@Autowired
	private HomeFacade homeFacade;
	
	@Autowired
	private StoreAuditServiceShop storeAuditService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShopGlobalSettingService globalSettingService;
	
	@Autowired
	private NJShopMemberOrderService njShopMemberOrderService;
	
	 
	 @Autowired
	 private MemberService memberService;

	 @Autowired
     private ShopProductService shopProductService;
	 
	 @Autowired
	 private StoreBusinessNewService storeBusinessNewService;
    
    /**
     * 获取商家信息
     * @param userDetail
     * @param request
     * @return
     */
    @RequestMapping("/myMemberInfo/auth")
    @ResponseBody
	public JsonResponse storeInfo(UserDetail<StoreBusiness> userDetail, HttpServletRequest request) {
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        Map<String, Object> data = new HashMap<String, Object>(); 
        
        //销售订单
        long storeId = userDetail.getId();
        Map<String, Integer> memberOrderMap =  njShopMemberOrderService.getMemberOrderStatusCount(storeId);
        data.put("memberOrderMap", memberOrderMap);
        
        //文章管理状态
        StoreBusiness business = storeBusinessNewService.getById(storeId);
        data.put("articleStatus", business.getWxaArticleShow());//1:开启  0：关闭
        data.put("wxaType", business.getWxaType());//小程序类型：0个人、1企业小程序
        data.put("isOpenWxa", business.getIsOpenWxa());//是否开通小程序：0未开通，1已开通
         
        
        //在售商品数量
        int zaiShouShangPinCount = shopProductService.getZaiShouShangPinCount(storeId);
        data.put("zaiShouShangPinCount", zaiShouShangPinCount);
        
        //客户总数量
        int memberTotalCount =  memberService.getMemberTotalCount(storeId);
        data.put("memberTotalCount", memberTotalCount);
        
        //未读消息数量
		int noReadMessageCount = memberService.getAllNoReadCount(storeId);
        data.put("noReadMessageCount", noReadMessageCount);
        
        //用户的同步开关状态和倍率
        Map<String,Object> synchronousButtonStatusAndRate = storeUserService.getSynchronousButtonStatusAndRate(storeId);
        data.put("synchronousButtonStatusAndRate", synchronousButtonStatusAndRate);
        
        //门店头图,英文逗号分隔
        StoreBusiness store = storeUserService.getStoreBusinessById(storeId);
        data.put("storeDisplayImages", store.getStoreDisplayImages());
        
        //小程序预约试穿开关：0：关闭；1：开启
        data.put("shopReservationsOrderSwitch", storeBusiness.getShopReservationsOrderSwitch());
        //客服热线
        data.put("hasHotonline", storeBusiness.getHasHotonline());
        data.put("hotOnline", storeBusiness.getHotOnline());

        
        String onlineWxaVersion = business.getOnlineWxaVersion();//"3.4";


        logger.info("线上小程序版本onlineWxaVersion:"+onlineWxaVersion+",business.getId():"+business.getId());
        int isShowReservationsOrder = 0;
        int isShowSubscribeOrder = 0;
        if(VersionUtil.ge(onlineWxaVersion, "3.5")){//大于等于3.5显示预约试穿菜单
        	isShowReservationsOrder = 1;
        	isShowSubscribeOrder = 0;
        	logger.info("大于等于3.5显示预约试穿菜单isShowReservationsOrder:"+isShowReservationsOrder+",isShowSubscribeOrder:"+isShowSubscribeOrder);
        }else{
        	isShowReservationsOrder = 0;
        	isShowSubscribeOrder = 1;
        	logger.info("小于3.5显示预约试穿菜单isShowReservationsOrder:"+isShowReservationsOrder+",isShowSubscribeOrder:"+isShowSubscribeOrder);
        }
        logger.info("isShowReservationsOrder:"+isShowReservationsOrder+",isShowSubscribeOrder:"+isShowSubscribeOrder);
        data.put("isShowSubscribeOrder", isShowSubscribeOrder);//是否显示想要菜单:0不显示、1显示
        data.put("isShowReservationsOrder", isShowReservationsOrder);//是否显示预约试穿菜单:0不显示、1显示
        
        
       
        return new JsonResponse().setSuccessful().setData(data);
    }
    
    
    
    
     
    
    
}