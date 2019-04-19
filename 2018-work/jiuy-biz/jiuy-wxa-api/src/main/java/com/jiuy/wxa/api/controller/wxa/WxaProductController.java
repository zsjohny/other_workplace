package com.jiuy.wxa.api.controller.wxa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jiuy.base.exception.BizException;
import com.jiuy.base.model.Query;
import com.jiuy.base.util.HttpUtils;
import com.jiuy.rb.enums.ShopActivityKindEnum;
import com.jiuy.rb.mapper.user.ShopMemberRbMapper;
import com.jiuy.rb.mapper.user.StoreBusinessRbMapper;
import com.jiuy.rb.model.user.StoreBusinessRb;
import com.jiuy.rb.model.user.StoreBusinessRbQuery;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuyuan.service.common.area.ActivityInfoCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.WxaMemberKeywordMapper;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.WxaMemberKeyword;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.WxaHomePageShopProductNewFacade;
import com.store.service.WxaProductService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* 小程序商品Controller
* @author Qiuyuefan
*/
@Controller
//@Login
@RequestMapping("/miniapp/product")
public class WxaProductController {
	
	private static final Log logger = LogFactory.get();
    @Autowired
	private StoreBusinessRbMapper storeBusinessRbMapper;
    @Autowired
    private WxaProductService wxaProductService;
    @Autowired
	 private WxaHomePageShopProductNewFacade wxaHomePageShopProductNewFacade;
    @Autowired
	private WxaMemberKeywordMapper wxaMemberKeywordMapper;

    @Resource( name = "shopProductServiceRb" )
	private IShopProductService shopProductService;


    /**
	  * 查询商品
	  * @param keyword：搜索词
	  * @param current	当前是第几页
	  * @param size	每页显示条数
	  * @return
	  */
	 @RequestMapping("/searchProductList")
	 @ResponseBody
	 public JsonResponse searchProductList(
//	 		ShopDetail shopDetail,MemberDetail memberDetail,

		     @RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId,
			 @RequestParam("keyword")String keyword,
			 @RequestParam(value="current",required=false,defaultValue="1")Integer current,
			 @RequestParam(value="size",required=false,defaultValue="20")Integer size) {
		 JsonResponse jsonResponse = new JsonResponse();
		try {
//			 checkStore(shopDetail);
			 if(StringUtils.isEmpty(keyword)){
		   	 	throw new RuntimeException("请填写搜索词");
		   	 }
			 keyword.trim();
//			 long memberId = memberDetail.getId();
//			 long storeId = shopDetail.getId();
			 SmallPage data =  wxaHomePageShopProductNewFacade.searchProductList(keyword, new Page<>(current, size),storeId,memberId);
			
			 
			 return jsonResponse.setSuccessful().setData(data);
		 } catch (Exception e) {
			 e.printStackTrace();
			 logger.error("查询商品"+e.getMessage());
			 return new JsonResponse().setError(e.getMessage());
		 }
		 
	 }
	 
	

	

	/**
	  * 获取会员搜索词列表
	  * @return
	  */
	 @RequestMapping("/getSearchKeywordList")
	 @ResponseBody
	 public JsonResponse getSearchKeywordList(
			 @RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId,
			 ShopDetail shopDetail,MemberDetail memberDetail) {
		 JsonResponse jsonResponse = new JsonResponse();
		try {
//			 checkStore(shopDetail);
//			 checkMember(memberDetail);
//			 long storeId = shopDetail.getId();
//			 long memberId = memberDetail.getId();
			 List<WxaMemberKeyword> data = new ArrayList<WxaMemberKeyword>();
			 if(memberId > 0){
				 Wrapper<WxaMemberKeyword> wrapper = new EntityWrapper<WxaMemberKeyword>().eq("member_id", memberId).eq("store_id", storeId).orderBy("create_time",false);
				 data = wxaMemberKeywordMapper.selectPage(new Page<ShopProduct>(1,10), wrapper);
			 }
			 return jsonResponse.setSuccessful().setData(data);
		 } catch (Exception e) {
			 e.printStackTrace();
			 logger.error("查询商品"+e.getMessage());
			 return new JsonResponse().setError(e.getMessage());
		 }
		 
	 }
	 
	 /**
	  * 清除会员搜索词
	  * @return
	  */
	 @RequestMapping("/clearSearchKeyword")
	 @ResponseBody
	 public JsonResponse clearSearchKeyword(
			 @RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId,
	 		ShopDetail shopDetail,MemberDetail memberDetail) {
		 JsonResponse jsonResponse = new JsonResponse();
		try {
//			 checkStore(shopDetail);
//			 checkMember(memberDetail);
//			 long storeId = shopDetail.getId();
//			 long memberId = memberDetail.getId();
			 List<WxaMemberKeyword> data = new ArrayList<WxaMemberKeyword>();
			 if(memberId > 0){
				 Wrapper<WxaMemberKeyword> wrapper = new EntityWrapper<WxaMemberKeyword>().eq("member_id", memberId).eq("store_id", storeId);
				 wxaMemberKeywordMapper.delete(wrapper);
			 }
			 return jsonResponse.setSuccessful();
		 } catch (Exception e) {
			 e.printStackTrace();
			 logger.error("查询商品"+e.getMessage());
			 return new JsonResponse().setError(e.getMessage());
		 }
		 
	 }
	 
	 /**
	  * 获取所有商品
	  * @param current	当前是第几页
	  * @param size	每页显示条数
	  * @return
	  */
	 @RequestMapping("/allProductList")
	 @ResponseBody
	 public JsonResponse allProductList(ShopDetail shopDetail,MemberDetail memberDetail,
										@RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId,
										@RequestParam(value="current",required=false,defaultValue="1")Integer current,
			 @RequestParam(value="size",required=false,defaultValue="20")Integer size) {
		 JsonResponse jsonResponse = new JsonResponse();
		try {
//			 checkStore(shopDetail);
//			 long storeId = shopDetail.getId();
			 logger.info("获取所有商品allProductList   storeId:"+storeId);
			 SmallPage data =  wxaHomePageShopProductNewFacade.allProductList(new Page<>(current, size),storeId);
			 logger.info("获取所有商品allProductList   data:"+JSON.toJSONString(data));
			 return jsonResponse.setSuccessful().setData(data);
		 } catch (Exception e) {
			 e.printStackTrace();
			 logger.error("获取所有商品"+e.getMessage());
			 return new JsonResponse().setError(e.getMessage());
		 }
		 
	 }



    
	/**
     * 查询商品详情
	 *
	 * @param productId productId
	 * @param activityId 活动id
	 * @param targetType 活动类型, 1团购,2秒杀
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/8/9 16:20
	 */
    @RequestMapping("/productitem")
    @ResponseBody
    public JsonResponse getProductItem(
//    		ShopDetail shopDetail,MemberDetail memberDetail,
			@RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId,
    		@RequestParam("productId")Long productId,
			@RequestParam(value = "activityId", required = false)Long activityId,
			@RequestParam(value = "targetType", required = false, defaultValue = "0")Integer targetType,
			HttpServletRequest request
			) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
//    		checkStore(shopDetail);
//   	 		checkMember(memberDetail);
   	 		logger.info("查询商品详情");
   	 		//版本
			Integer version = HttpUtils.wxVersion (request.getHeader ("version"), 140);
			//放入活动和version,做版本兼容
			ActivityInfoCache.createInstance (activityId, targetType, version);

//			StoreBusinessRbQuery query = new StoreBusinessRbQuery();
//			query.setId(storeId);
//			StoreBusinessRb store = storeBusinessRbMapper.selectOne(query);
//			if (store == null) {
//				return jsonResponse.setError("没有商家信息");
//			}
			Map<String, Object> data = wxaProductService.getProductItem (productId, memberId, storeId);
//    		data.put("shopReservationsOrderSwitch", store.getShopReservationsOrderSwitch());
//    		data.put("name", store.getBusinessName());
			//先返回空,前端应该没用到
    		data.put("name", "");
    		//将活动放到缓存
			Long teamId = (Long) data.get ("teamId");
			if (teamId != null && teamId > 0) {
				shopProductService.putActivityInCacheIfNoExist (storeId, teamId , ShopActivityKindEnum.TEAM);
			}
			Long secondId = (Long) data.get ("secondId");
			if (secondId != null && secondId >0) {
				shopProductService.putActivityInCacheIfNoExist (storeId, secondId, ShopActivityKindEnum.SECOND);
			}
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			if (e instanceof BizException) {
				return new JsonResponse().setError(((BizException) e).getMsg ());
			}
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		} finally {
    		ActivityInfoCache.clear ();
		}
    }



}
