/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ProductFacade;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.ShoppingCartDelegator;

/**
 * @author LWS
 *
 */
@Controller
@Login
@RequestMapping("/mobile/cart")
public class MobileShoppingCartController {

	private static final Logger logger = LoggerFactory.getLogger(MobileShoppingCartController.class);
	
	@Resource
    private ShoppingCartDelegator shoppingCartDelegator;
    
    @Autowired
    private ProductFacade productFacade;
    
    @Deprecated
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse queryCart(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String,Object> data = shoppingCartDelegator.getCart(userDetail);
        return jsonResponse.setSuccessful().setData(data);
    }
    
    @Deprecated
    @RequestMapping(value = "/list15", method = RequestMethod.GET)
    @ResponseBody
    @NoLogin
    public JsonResponse queryCart15(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        
        Map<String,Object> data = shoppingCartDelegator.getCart15(userDetail);
        
        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/list17", method = RequestMethod.GET)
    @ResponseBody
    @NoLogin
    public JsonResponse queryCart17(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        
        Map<String,Object> data = shoppingCartDelegator.getCart17(userDetail);
        
        return jsonResponse.setSuccessful().setData(data);
    }	
    
    @RequestMapping(value = "/list185", method = RequestMethod.GET)
    @ResponseBody
    @NoLogin
    public JsonResponse queryCart185(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String,Object> data = shoppingCartDelegator.getCart185(userDetail);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }	

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addCart(@RequestParam("product_id") long productId, @RequestParam("sku_id") long skuId, 
                                @RequestParam("count") int count, 
                                @RequestParam(value = "statistics_id", required = false, defaultValue = "0") long statisticsId,
                                @RequestParam(value = "logIds", required = false, defaultValue = "") String logIds,
                                UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
        
        //限购校验
        productCountMap = shoppingCartDelegator.getProductCount(userDetail.getUserId(), productId, count);
    	List<RestrictProductVO> products = productFacade.getRestrictInfo(userDetail.getUserId(), productCountMap);
    	if(products.size() > 0) {
        	String description = productFacade.restrictDetail(products.get(0));
        	return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
        }
        
        ResultCode resultCode = shoppingCartDelegator.addCart(productId, skuId, count, userDetail, statisticsId, logIds);
        
        return jsonResponse.setResultCode(resultCode);
    }

//    @RequestMapping(value = "/remove", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse removeCart(@RequestParam("product_id") long productId, @RequestParam("sku_id") long skuId,
//                                   @RequestParam("count") int count, UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//        boolean success = shoppingCartDelegator.removeCart(productId, skuId, count, userDetail);
//        return jsonResponse.setResultCode(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
//    }
    
    /*********************Modified By Jeff.Zhan *****************************/
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse deleteCart(@RequestParam("id") long id, UserDetail userDetail) {
    	logger.info("delete cartItems!! ");
        JsonResponse jsonResponse = new JsonResponse();
        boolean success = shoppingCartDelegator.deleteCart(userDetail.getUserId(), id);
        return jsonResponse.setResultCode(success ? ResultCode.COMMON_SUCCESS : ResultCode.COMMON_ERROR_UNKNOWN);
    }
    
    /**
     * 在购物车页面点击"保存"按钮，保存购物车内的产品信息，购买数量，isSelected
     * @param cartItems
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse saveProductsInCart(@RequestParam("cartItems") String cartItems, UserDetail userDetail) {
        logger.info("cartItems :{}", cartItems);

        return shoppingCartDelegator.saveProductsInCart(cartItems, userDetail);
        
    }
    
}

