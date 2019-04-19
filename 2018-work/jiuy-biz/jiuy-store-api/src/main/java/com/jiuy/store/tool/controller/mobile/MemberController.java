package com.jiuy.store.tool.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
//import com.jiuy.core.meta.member.Member;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.web.help.JsonResponse;
import com.store.enumerate.Member48MarkEnum;
import com.store.service.MemberService;
import com.store.service.ProductSKUService;
import com.store.service.ShopProductService;
import com.store.service.StoreBusinessServiceShop;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 小程序会员表 前端控制器
 * </p>
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/mobile/member")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    Log log = LogFactory.get();
	
    @Autowired
    MemberService memberService;
    
    @Autowired
    StoreBusinessServiceShop storeBusinessService;
    
    @Autowired
    ProductSKUService productSKUservice;
    
    @Autowired
    ShopProductService shopProductService;
    /**
     * 会员列表
     * @param mark 筛选条件：0（全部）、1（48小时内）、2（48小时前） 
     * @param searchWord 搜索词
     * @param current	非必填  当前页数
     * @param size	非必填  每页条数  默认每页10条
     * @return
     */
    @RequestMapping("/list/auth")
    @ResponseBody
    public JsonResponse list(@RequestParam(required = true) int mark,String searchWord,int current, int size , 
			UserDetail<StoreBusiness> userDetail, HttpServletResponse response, @ClientIp String ip,
			ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	long storeBusinessId = userDetail.getId();
   	 	if(storeBusinessId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	    StoreBusiness storeBusiness= userDetail.getUserDetail();
    	
   	 	//获取数据
   	 	SmallPage smallPage = memberService.list( Member48MarkEnum.getEnum(mark),searchWord,storeBusiness,new Page(current,size),storeBusiness.getId());
   	 	//返回数据
   	 	return jsonResponse.setSuccessful().setData(smallPage);
    }



    /**
     * 获取会员备注信息
     * @param id 会员Id
     * @return
     */
    @RequestMapping("/getMemberMemo/auth")
    @ResponseBody
    public JsonResponse getMemberMemo(@RequestParam("id") Long id,
    		UserDetail userDetail) {
   	 	return memberService.getMemberMemo(id, userDetail);
    }
    
    /**
     * 修改我的信息
     * @param id 会员Id
     * @return
     */
    @RequestMapping("/updateMemberInfo/auth")
    @ResponseBody
    public JsonResponse getMineMemberInfo(@RequestParam("id") Long id,
    		@RequestParam("memo_name") String memo_name,
    		@RequestParam("memo_phone") String memo_phone,
    		@RequestParam("memo_gender") String memo_gender,
    		@RequestParam("memo_career") String memo_career,
    		@RequestParam("memo_earning") String memo_earning,
    		@RequestParam("memo_character") String memo_character,
    		@RequestParam("memo_province_name") String memo_province_name,
    		@RequestParam("memo_city_name") String memo_city_name,
    		@RequestParam("district_name") String district_name,
    		@RequestParam("memo_address_detail") String memo_address_detail,
    		@RequestParam("memo_content") String memo_content,
    		@RequestParam("birthday_year") String birthday_year,
    		@RequestParam("birthday_month") String birthday_month,
    		@RequestParam("birthday_day") String birthday_day,
    		@RequestParam("bust_size") String bust_size,
    		@RequestParam("waist_size") String waist_size,
    		@RequestParam("hip_size") String hip_size,
    		@RequestParam("height") String height,
    		@RequestParam("weight") String weight,
    		UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
    	return memberService.updateMemberMemo(id,
    			 memo_name, memo_phone, memo_gender, memo_career, memo_earning, memo_character,
    			 memo_province_name, memo_city_name, district_name, memo_address_detail, memo_content, birthday_year, birthday_month,
    			 birthday_day,bust_size,waist_size, hip_size, height, weight, userDetail);
    }
    
    /**
     * 保存倍率
     * 1~10，小数点保留1位
     */
    @RequestMapping("/saveRate/auth")
    @ResponseBody
    public JsonResponse saveRate(@RequestParam(value="rate",required=false,defaultValue = "0.0") double rate,UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	try{
    		checkStoreId(storeId);
    		//保存倍率
    		storeBusinessService.saveRate(rate,storeId);
    	}catch (Exception e) {
    		logger.info(e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    	return jsonResponse.setSuccessful();
    }
    
    /**
     * 同步上新按钮
     * synchronousButtonStatus 0:按钮关闭  1:按钮打开
     */
    @RequestMapping("/closeOrOpenButton/auth")
    @ResponseBody
    public JsonResponse closeOrOpenButton(@RequestParam(value="synchronousButtonStatus",required=false,defaultValue = "0") int synchronousButtonStatus,UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	try{
    		checkStoreId(storeId);
    		//保存同步上新按钮状态
    		storeBusinessService.updateButtonStatus(synchronousButtonStatus,storeId);
    		return jsonResponse.setSuccessful(); 
    	}catch (Exception e) {
    		logger.info(e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 获取上新的商品数目，排除用户上下架的自己的商品
     * synchronousButtonStatus 同步上新按钮状态 0：关闭 1：开启
     */
    @RequestMapping("/getSaleStartProductNums/auth")
    @ResponseBody
    public JsonResponse getSaleStartProductNums(@RequestParam("synchronousButtonStatus") int synchronousButtonStatus,
    		                                    UserDetail userDetail){
    	logger.info("获取上新的商品数目getSaleStartProductNums:synchronousButtonStatus:"+synchronousButtonStatus);
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	try{
    		checkStoreId(storeId);
    		int count = 0;
    		Map<String,Object> data = new HashMap<String,Object>();
    		if(synchronousButtonStatus == 1){
    			count = productSKUservice.getSaleStartProductNums(storeId);
    		}
    		data.put("count", count);
    		logger.info("获取上新的商品数目getSaleStartProductNums:data:"+data.toString());
    		return jsonResponse.setSuccessful().setData(data);
    	}catch (Exception e) {
    		logger.info(e.getMessage());
    		e.printStackTrace();
    		return jsonResponse.setError(e.getMessage());
		}
    	//获取上新的商品数目
    }
    /**
     * 一键同步上新商家商品
     * rate  倍率
     * synchronousButtonStatus 同步上新按钮状态 0:按钮关闭 1:按钮打开
     */
    @RequestMapping("/synchronousUpdateProduct/auth")
    @ResponseBody
    public JsonResponse synchronousUpdateProduct(@RequestParam("rate") double rate,
    		                                     @RequestParam("synchronousButtonStatus") int synchronousButtonStatus,
			UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	try{
    		checkStoreId(storeId);
    		logger.info("一键同步上新商家商品开始");
    		//开启子线程
    		Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					shopProductService.synchronousUpdateProduct(userDetail.getUserDetail(),rate,synchronousButtonStatus);
				}
			});
    		thread.start();
    		//异步响应
    		return jsonResponse.setSuccessful().setData("正在同步上新");
    	}catch (Exception e) {
    		e.printStackTrace();
    		logger.info(e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    	
    	//上传商家的商品
    }

	private void checkStoreId(long storeId) {
		if(storeId == 0){
			throw new RuntimeException("商家信息不能为空，该接口需要登陆，请排除问题！");
   	 	}
	}
    
//    /**
//     * 修改我的头像/昵称
//     * @param id	用户Id
//     * @param key	想要修改的字段 userNickname:用户昵称 userIcon:用户头像
//     * @param value	想要修改的字段对应的值
//     * @return
//     */
//    @RequestMapping("/updatefieldvalue")
//    @ResponseBody
//    public JsonResponse updateFieldValue(ShopDetail shopDetail,MemberDetail memberDetail,
//    		@RequestParam("id") Long id,@RequestParam("key") String key,@RequestParam("value") String value) {
//    	JsonResponse jsonResponse = new JsonResponse();
//		try {
//			memberService.updateFieldValue(memberDetail.getId(),key,value);
//   	 		return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			return jsonResponse.setError(e.getMessage());
//		}
//    }
}