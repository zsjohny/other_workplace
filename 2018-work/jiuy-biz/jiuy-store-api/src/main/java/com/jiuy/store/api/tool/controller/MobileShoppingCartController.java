package com.jiuy.store.api.tool.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.StoreShoppingCartDelegator;
import com.store.service.StoreShoppingCartFacade;

/**
 * @author chen
 * @version 2017年7月10日 上午
 * 
 */

@Controller
@RequestMapping("/mobile/cart")
public class MobileShoppingCartController {

	private static final Logger logger = LoggerFactory.getLogger(MobileShoppingCartController.class);

	@Autowired
	private StoreShoppingCartFacade shoppingCartFacade;

	@Resource
    private StoreShoppingCartDelegator shoppingCartDelegator;
    
    @RequestMapping(value = "/list/auth", method = RequestMethod.GET)
    @ResponseBody
    @NoLogin
	@Cacheable("cache")
    public JsonResponse queryCart185(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
	    	Map<String,Object> data = new HashMap<>();
//	    	logger.info("获取购物车列表开始");
	    	long time1 = System.currentTimeMillis();
			if(userDetail.getId() > 0 ) {
				data.put("list", shoppingCartDelegator.getCart(userDetail));
			}
			long time2 = System.currentTimeMillis();
			long time3 = time2 - time1;
    		logger.info("获取购物车列表结束，总耗时time3："+time3);
			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
		
	}
    @RequestMapping(value = "/add/auth")
    @ResponseBody
    public JsonResponse addCart(@RequestParam("cartItems") String cartItems, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		shoppingCartDelegator.add(cartItems, userDetail.getId());
        
        	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**获取用户购物车款号数量接口
     */
    @RequestMapping(value = "/getClothesNumberCount/auth")
    @ResponseBody
    public JsonResponse getClothesNumberCount(UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
	 	if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
    	try {
    		Map<String,Object> data = new HashMap<>();
    		int count = shoppingCartDelegator.getClothesNumberCount(storeId);
    		data.put("clothesNumberCount", count);
        	return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    @RequestMapping(value = "/delete/auth")//, method = RequestMethod.POST
    @ResponseBody
    public JsonResponse deleteCart(@RequestParam("id") long id, UserDetail userDetail) {
    	logger.info("delete cartItems!! ");
        JsonResponse jsonResponse = new JsonResponse();
    	try {
    		boolean success = shoppingCartDelegator.deleteCart(userDetail.getId(), id);
    		return jsonResponse.setResultCode(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    @RequestMapping(value = "/batch/delete/auth", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse batchDelete(@RequestParam("product_id") long productId, UserDetail userDetail) {
    	logger.info("delete cartItems!! ");
        JsonResponse jsonResponse = new JsonResponse();
    	try {
    		shoppingCartDelegator.batchDeleteCart(userDetail.getId(), productId);
    		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 在购物车页面点击"保存"按钮，保存购物车内的产品信息，购买数量，isSelected
     * @param cartItems
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/save/auth", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse saveProductsInCart(@RequestParam("cartItems") String cartItems, UserDetail userDetail) {
//        logger.info("cartItems :{}", cartItems);
        JsonResponse jsonResponse = new JsonResponse();
    	try {
    		return shoppingCartDelegator.saveProductsInCart(cartItems, userDetail.getId());
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
	
}
