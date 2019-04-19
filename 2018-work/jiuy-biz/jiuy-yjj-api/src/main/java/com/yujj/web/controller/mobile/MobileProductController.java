/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.DataDictionaryService;
import com.jiuyuan.util.NumberUtil;
import com.jiuyuan.util.NumberUtils;
import com.yujj.business.service.*;
import com.yujj.entity.product.*;
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
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.comment.CommentVO;
import com.jiuyuan.entity.product.CategoryV0;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.QRCodeUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.adapter.DataminingAdapter;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.facade.CommentFacade;
import com.yujj.business.facade.ProductFacade;
import com.yujj.entity.Brand;
import com.yujj.entity.account.UserDetail;

/**
 * @author LWS
 * @since 2015/07/31
 */
@Controller
@RequestMapping("/mobile/product")
public class MobileProductController {

    private static final Logger logger = Logger.getLogger(MobileProductController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private ProductPropertyService productPropertyService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ProductPropAssembler productPropAssembler;

    @Autowired
    private ProductFacade productFacade;
    
    @Autowired
    private BrandService brandService;
    
    @Autowired
    private CommentFacade commentFacade;
    
    @Autowired
    private DataminingAdapter dataminingAdapter;

    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private DataDictionaryService dataDictionaryService;
    /**
     * 根据产品id获取当前产品详情 示例：/mobile/product/2.json?req=12345678909876
     * 
     * @param productId
     * @return
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getProduct(@PathVariable("productId") long productId, UserDetail userDetail) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonResponse jsonResponse = new JsonResponse();
        // get product main information
        Product product = productService.getProductById(productId);
        if (product == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }
        List<ProductProp> baseProps = productPropertyService.getOrderedProductProperties(productId);
        List<ProductPropVO> basePropVOs = productFacade.loadProductPropVOs(baseProps);
        data.put("baseProps", basePropVOs);

        List<ProductSKU> skus = productSKUService.getProductSKUsOfProduct(productId);
        Map<String, ProductSKU> skuMap = new HashMap<String, ProductSKU>();
        List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
        for (ProductSKU sku : skus) {
            skuMap.put(sku.getPropertyIds(), sku);
            productPropVOs.addAll(sku.getProductProps());
        }
        try {
            data.put("skuMap", objectMapper.writeValueAsString(skuMap));
        } catch (JsonProcessingException e) {
            logger.error("parsing sku map error!");
        }

        productPropAssembler.assemble(productPropVOs);

        List<ProductPropNameValuesPair> skuProps = new ArrayList<ProductPropNameValuesPair>();
        Map<Long, ProductPropNameValuesPair> skuPropMap = new HashMap<Long, ProductPropNameValuesPair>();
        for (ProductPropVO propVO : productPropVOs) {
            ProductPropName propName = propVO.getPropName();
            ProductPropNameValuesPair skuProp = skuPropMap.get(propName.getId());
            if (skuProp == null) {
                skuProp = new ProductPropNameValuesPair(propName);
                skuPropMap.put(propName.getId(), skuProp);
                skuProps.add(skuProp);
            }
            skuProp.add(propVO.getPropValue());
        }
        data.put("skuProps", skuProps);
        data.put("product", product);
        

        long userId = userDetail.getUserId();
        if (userId > 0) {
            data.put("userConfig", buildUserMap(userDetail.getUserId(), productId));
            data.put("restrictInfo", productFacade.restrictInfoOfProduct(userId, CollectionUtil.createSet(productId)).get(productId));
        }
        
        Brand brand = brandService.getBrand(product.getBrandId());
        data.put("brand", brand);
       
        List<CategoryV0> categoryV0s = productCategoryService.getProductVirtualCategoryList(CollectionUtil.createList(productId));
        if(categoryV0s.size() > 0) {
            double exceedMoney = categoryV0s.get(categoryV0s.size()-1).getExceedMoney();
            StringBuilder string = new StringBuilder();
            if (exceedMoney - (int) exceedMoney < 0.001) {
                string.append(String.format("满%d", (int) exceedMoney));
            } else {
            	string.append(String.format("满%.2f", exceedMoney));
            }

            double minusMoney = categoryV0s.get(categoryV0s.size()-1).getMinusMoney();
            if (minusMoney - (int) minusMoney < 0.001) {
            	string.append(String.format("减%d", (int) minusMoney));
            } else {
            	string.append(String.format("减%.2f", minusMoney));
            }

        	data.put("virtualCategory", categoryV0s.get(0));
        	if(categoryV0s.get(0).getIsDiscount() == 1) {
                data.put("discountDesc", string);
        	}
        } else {
            double exceedMoney = brand.getExceedMoney();
            StringBuilder string = new StringBuilder();
            if (exceedMoney - (int) exceedMoney < 0.001) {
                string.append(String.format("满%d", (int) exceedMoney));
            } else {
            	string.append(String.format("满%.2f", exceedMoney));
            }

            double minusMoney = brand.getMinusMoney();
            if (minusMoney - (int) minusMoney < 0.001) {
            	string.append(String.format("减%d", (int) minusMoney));
            } else {
            	string.append(String.format("减%.2f", minusMoney));
            }

        	data.put("virtualCategory", null);
        	if(brand.getIsDiscount() == 1) {
                data.put("discountDesc", string);
        	}
        }
        
        if(product.getRestrictDayBuy() != -1 || product.getRestrictHistoryBuy() != -1) {
        	String historyDesc = product.getRestrictHistoryBuy() == -1 ? "" : product.getRestrictCycle() + "天限购" + product.getRestrictHistoryBuy() + "件";
        	String dayDesc = product.getRestrictDayBuy() == -1 ? "" : "单日限购" + product.getRestrictDayBuy() + "件";
        	data.put("restrictDesc", historyDesc + " " + dayDesc);
        }
        
        int aveLiker = 0;
        List<CommentVO> comments = commentFacade.getCommentList(productId, new PageQuery(1, 1));
		for (CommentVO commentVO : comments) {
			aveLiker += commentVO.getLiker();
		}
		if (comments.size() != 0)
			aveLiker /= comments.size();
		data.put("aveLiker", aveLiker);
        data.put("comments", comments);

        data.put("buyGuessProduct", productService.getProductList15(dataminingAdapter.getBuyGuessProduct186(userId > 0 ? userId : 0, new PageQuery(1, 4))));
        data.put("seeAgainProduct", productService.getProductList15(dataminingAdapter.getSeeAgainProduct186(userId > 0 ? userId : 0, new PageQuery(1, 4))));
        
        return jsonResponse.setData(data).setSuccessful();
    }
    
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
    
    private Map<String, Object> buildUserMap(long userId, long productId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("favorite", favoriteService.getFavorite(userId, productId) != null);
        return map;
    }
    
	@RequestMapping("/qrcode/src")
	@ResponseBody
	public void qrcode(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "yjj_number", required = false, defaultValue = "-1") Long yJJNumber,  
			@RequestParam("product_id") long productId, @RequestParam("file_name") String fileName, 
			@RequestParam("width") int width, @RequestParam("height") int height) {
		String content = Constants.SERVER_URL + "/static/app/ProductDetailsUser.html" + "?product_id=" + productId;
    	if (yJJNumber != null && yJJNumber != -1) {
    		content += "&yjj_number=" + yJJNumber;
		}
		QRCodeUtil.getFile(request, response, content, fileName, width, height);
	}
	
	
	   /**
     * 根据产品id获取当前产品详情 示例：/mobile/product/2.json?req=12345678909876
     * 
     * @param productId
     * @return
     */
    @RequestMapping(value = "/brand/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getProductList(
    		@RequestParam(value="type", required=false, defaultValue = "0") int type,
    		@RequestParam(value = "search_brand", required = false) String searchBrand, UserDetail userDetail, PageQuery pageQuery) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String, Object> data = productFacade.getBrandList(searchBrand, userDetail, pageQuery, type);
    	// ProductFacade中ObjectMapper无法autowired，在此处理一下数据
//		try {
//			data.put("skuMap", objectMapper.writeValueAsString(data.get("skuMap")));
//		} catch (JsonProcessingException e) {
//			logger.error("parsing sku map error!");
//		}
    
    	return jsonResponse.setData(data).setSuccessful();
    }
//    /**
//     * 根据产品id获取当前产品详情 示例：/mobile/product/2.json?req=12345678909876
//     * 
//     * @param productId
//     * @return
//     */
//    @RequestMapping(value = "/brand/detail", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getProductDetail(@RequestParam(value = "brandId") long brandId, UserDetail userDetail) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	
//    	Map<String, Object> data = productFacade.getBrandDetail(brandId, userDetail);
//    	return jsonResponse.setData(data).setSuccessful();
//    }
    
    /**
     * @param type 0:热卖 1：推荐 2：最近浏览 3：采购记录
     * @return
     */
    @RequestMapping(value = "/brand/datamining", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getBrandProductHotList( @RequestParam(value = "type") int type, UserDetail userDetail, PageQuery pageQuery) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String, Object> data = productFacade.getBrandList("", userDetail, pageQuery, type);
    
    	return jsonResponse.setData(data).setSuccessful();
    }

}
