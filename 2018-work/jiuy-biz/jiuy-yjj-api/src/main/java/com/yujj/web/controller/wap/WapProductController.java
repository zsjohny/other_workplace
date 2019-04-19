/**
 * 
 */
package com.yujj.web.controller.wap;


import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ProductFacade;

import com.yujj.business.service.ProductService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Product;

/**
 * @author czy
 * @since 2017/04/05
 */
@Controller
@RequestMapping("/wap/product")
public class WapProductController {

    private static final Logger logger = Logger.getLogger(WapProductController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductFacade productFacade;
    


    /**
     * 根据产品id获取当前产品详情 示例：/mobile/product/2.json?req=12345678909876
     * 
     * @param productId
     * @return
     */
    @RequestMapping(value = "/new/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getProduct18(@PathVariable("productId") long productId, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	// get product main information
    	Product product = productService.getProductById(productId);
    	if (product == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	Map<String, Object> data = productFacade.getProduct18(product, userDetail);
    	// ProductFacade中ObjectMapper无法autowired，在此处理一下数据
    	
		try {
			data.put("skuMap", objectMapper.writeValueAsString(data.get("skuMap")));
		} catch (JsonProcessingException e) {
			logger.error("parsing sku map error!");
		}
    
    	return jsonResponse.setData(data).setSuccessful();
    }
  }
    
    
