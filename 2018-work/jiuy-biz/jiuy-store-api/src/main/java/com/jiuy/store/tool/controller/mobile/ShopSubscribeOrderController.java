package com.jiuy.store.tool.controller.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.SubscribeOrder;
import com.store.entity.message.GroupMessage;
import com.store.service.ShopOrderService;
import com.store.service.ShopSubscribeOrderService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 预约订单列表
* 作者赵兴林
* 
* 预约订单接口
*/
@Controller
@RequestMapping("/shop/subscribeOrder")
public class ShopSubscribeOrderController {

	private static final Log logger = LogFactory.get();
    
	@Autowired
	private ShopSubscribeOrderService shopSubscribeOrderService;
	
    /**
     * 
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/getList/auth")
    @ResponseBody
    public JsonResponse getList(int current, int size , UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   		Page<SubscribeOrder> page = shopSubscribeOrderService.getList(new Page(current,size),storeId);
   	 	SmallPage smallPage = new SmallPage(page);
   	 	List<Map<String,String>>  returnList = new ArrayList<Map<String,String>>();
   	    List<SubscribeOrder>  list = smallPage.getRecords();
   	    for(SubscribeOrder subscribeOrder :  list){
   	    	Map<String,String> map = new HashMap<String,String>(); 
   	    	map.put("id", String.valueOf(subscribeOrder.getId()));
   	    	map.put("time",DateUtil.parseLongTime2Str(subscribeOrder.getCreateTime()) );
   	    	map.put("name", subscribeOrder.getName());
   	    	map.put("phone", subscribeOrder.getPhone());
   	    	map.put("address", subscribeOrder.getAddress());
   	    	map.put("shopProductId", String.valueOf(subscribeOrder.getShopProductId()));
   	    	map.put("shopProductName", String.valueOf(subscribeOrder.getShopProductName()));
   	    	returnList.add(map);
   	    }
   	    smallPage.setRecords(returnList);
   	 	return jsonResponse.setSuccessful().setData(smallPage);
    }
}