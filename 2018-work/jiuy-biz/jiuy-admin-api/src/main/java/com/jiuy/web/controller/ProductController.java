/**
 * 
 */
package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.business.facade.ProductFacade;
import com.jiuy.core.business.facade.ProductTagFacade;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.admin.AdminLogService;
import com.jiuy.core.service.tag.TagService;
import com.jiuy.web.delegate.ProductDelegator;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SaleStatus;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.product.ProductWindow;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.tag.TagVO;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/product")
@Controller
@Login
public class ProductController{
	
	private static Logger logger = Logger.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductFacade productFacade;
    
    @Autowired
    private ProductDelegator productDelegator;
    
    @Autowired
    private ProductTagFacade productTagFacade;
    
    @Autowired
    private TagService tagService;
	@Resource
	public AdminLogService alService;
    @Autowired
    private ProductMapper productMapper;
    
	@RequestMapping("/mainview")
	public String getProductList() {
		return "page/backend/undercarriage";
	}
	
	@RequestMapping("/{id}")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse load(@PathVariable("id") long id) {
         JsonResponse jsonResponse = new JsonResponse();
         
         Map<String, Object> data = new HashMap<String, Object>();
         data.put("product_info", productDelegator.loadInfo(id));
         
         return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/wholesale/{id}")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse loadWholeSale(@PathVariable("id") long id){
		JsonResponse jsonResponse = new JsonResponse();
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("product_info", productDelegator.loadWholeSaleProductInfo(id));
        
        return jsonResponse.setSuccessful().setData(data);
	}
	
	 @AdminOperationLog
	 @RequestMapping("/update")
	 @ResponseBody
	 public JsonResponse updatet(HttpServletRequest request, @RequestBody Product prod) {
		 JsonResponse jsonResponse = new JsonResponse();
		
		 if(productService.chkProductClothesNum(prod.getClothesNumber()).size() > 1) {
			 return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("重复款号");
		 }
		 productFacade.updateProduct(prod);
		
		 return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	 }
	
	 @AdminOperationLog
	 @RequestMapping("/updateproduct")
	 @ResponseBody
	 @Transactional(rollbackFor = Exception.class)
	 public JsonResponse updateProduct(HttpServletRequest request, @RequestBody Product prod) {
		 System.out.println(prod);
		 logger.info("编辑商品summaryImages:"+prod.getSummaryImages()+","+prod.getSummaryImageArray());
		 logger.info("编辑商品sizeTableImage:"+prod.getSizeTableImage()+","+prod.getSizeTableImageArray());
		 JsonResponse jsonResponse = new JsonResponse();
		
		 if(productService.chkProductClothesNum(prod.getClothesNumber()).size() > 1) {
			 return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("重复款号");
		 }
		 productFacade.updateProduct(prod);
		 
		 return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	 }
    
    //处理商品批发价
    @AdminOperationLog
	@RequestMapping("/updatewholesaleproduct")
	@ResponseBody
    public JsonResponse updateWholeSaleProduct(HttpServletRequest request, @RequestBody Product prod){
    	JsonResponse jsonResponse = new JsonResponse();
    	if(productService.chkProductClothesNum(prod.getClothesNumber()).size() > 1) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("重复款号");
		}
    	productFacade.updateWholeSaleProduct(prod);
    	return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }
	
    @AdminOperationLog
	@RequestMapping(value = "/updatewt", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse updateProWeight(@RequestParam(value="weight") Integer weight, 
			@RequestParam(value="product_id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		if(weight < 0) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("权重必须大于0");
		}
		productService.update(id, null, weight, null, null);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
    
	@RequestMapping(value = "/srchoverview", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchOverview(@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "page_size", required = false, defaultValue = "1")int pageSize, 
			@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "clothes_no", required = false, defaultValue = "")String clothesNo,
			@RequestParam(value = "sort", required = false, defaultValue = "1")int sortType,
			@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,
			@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		List<Map<String, Object>> maps = productFacade.searchOverview(brandName, clothesNo, pageQuery, SortType.getName(sortType), SaleStatus.getSql(saleStatus), skuStatus);
		int count = productFacade.searchOverviewCount(brandName, clothesNo, SaleStatus.getSql(saleStatus), skuStatus);
		pageQueryResult.setRecordCount(count);
		
		data.put("products", maps);
		data.put("total", pageQueryResult);

		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 根据款号获取对应的商品
	 * @param clothesNum
	 * @param brandId
	 * @return
	 */
    @RequestMapping(value = "/searchclothesnum", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse searchByClothesNum(@RequestParam(value = "clothes_no") String clothesNum,
    		@RequestParam(value = "brand_id", required = false) Long brandId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	ProductWindow product = productService.searchProWinByClothesNum(clothesNum);
    	
    	if(product == null) {
    		return jsonResponse.setError("款号不存在");
    	}
    	if(brandId != null && product.getBrandId() != brandId) {
    		return jsonResponse.setError("品牌有误");
    	}
    	
    	int remainCount = productService.getRemainCountById(product.getId());
    	
    	product.setRemainCount(remainCount);
    	
    	return jsonResponse.setSuccessful().setData(product);
    }
    
    /**
     * 根据多个款号获取对应的多个商品
     * @param clothesNums
     * @param brandId
     * @return
     */
    @RequestMapping(value = "/searchclothesnums")
    @ResponseBody
    public JsonResponse searchByClothesNums(@RequestParam(value = "clothes_nos") String clothesNums,
    		@RequestParam(value = "brand_id", required = false) Long brandId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	String[] clothesNumArrays = clothesNums.split(",");
    	List<ProductWindow> productList = new ArrayList<ProductWindow>();
    	for (String clothesNum : clothesNumArrays) {
    		ProductWindow product = productService.searchProWinByClothesNum(clothesNum);
        	
        	if(product == null) {
        		return jsonResponse.setError("款号不存在");
        	}
        	if(brandId != null && product.getBrandId() != brandId) {
        		return jsonResponse.setError("品牌有误");
        	}
        	
        	int remainCount = productService.getRemainCountById(product.getId());
        	
        	product.setRemainCount(remainCount);
        	productList.add(product);
		}
    	
    	
    	return jsonResponse.setSuccessful().setData(productList);
    }
    
    @RequestMapping(value = "/searchproductid", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse searchByProductId(@RequestParam(value = "product_id") long productId,
    		@RequestParam(value = "promotion_setting", required = false) Integer promoSetting){
    	JsonResponse jsonResponse = new JsonResponse();
    	String setting = "";
    	if(promoSetting != null){
    		setting = promoSetting.intValue() + "";
    	}
    	Product product = productMapper.getPromotionProductById(productId, setting);
    	if(product == null){
    		return jsonResponse.setError("款号id不存在");
    	}
    	
    	return jsonResponse.setSuccessful().setData(product);
    }
    
    /**
     * 
     * @param productId 
     * @param tagIds 例如:7,8,9
     * @return
     */
    @RequestMapping("/tag/add")
    @ResponseBody
    public JsonResponse addTag(@RequestParam("product_id") Long productId,
    		@RequestParam("tag_ids") String tagIds) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	productTagFacade.addTag(productId, tagIds);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
    @RequestMapping("/{id}/tag")
    @ResponseBody
    public JsonResponse tagInfo(@PathVariable("id") Long productId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	data.put("product_info", productService.getProductById(productId));
    	//获取标签
    	Set<TagVO> tagVOs = productTagFacade.tagsOfProductId(productId);
    	data.put("selected_tags", tagVOs);
    	//获取所有标签，标签组到标签
    	Map<Long, TagVO> tagVOMap = tagService.tagTreeMap();
    	data.put("tag_info", tagVOMap.values());
    	//获取相关标签，与分类相关联的标签
    	Map<String, Object> tagVOsByCategoryName = productTagFacade.getRelatedTags(productId, tagVOMap);
    	data.put("related_tags", tagVOsByCategoryName);
//    	data.put("related_tags", null);
    	logger.info("data:"+JSON.toJSONString(data));
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/clothesnum/{clothesnum}")
    @ResponseBody
    public JsonResponse getByClothesNum(@PathVariable("clothesnum") String clothesNum) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	data.put("product", productService.searchOne(clothesNum));
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    //平台强制下架供应商发布的商品
    @AdminOperationLog
   	@RequestMapping("/ids/soldout")
    @ResponseBody
   	public JsonResponse soldoutIds(HttpServletRequest request,@RequestParam(value = "productIds",required = true)String productIds,@RequestParam(value = "remark",required=true)String remark) {
       	JsonResponse jsonResponse = new JsonResponse();
   		Map<String, Object> data = new HashMap<>();
   		try {
   			String[] productIdarr = productIds.split(",");
   			int count = 0;
   			if (null != productIdarr && productIdarr.length != 0) {
   				for (String productIdStr : productIdarr) {
   					long productId = Long.parseLong(productIdStr);
   					count += productService.soldoutProductList(productId,remark,request);
   				}
   			}
   			data.put("count", count);
   			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			return jsonResponse.setError("平台下架供应商商品e："+e.getMessage());
		}
      }
    
    /**
     * 判断所选商品是否都已下架
     * @param productIds
     * @return
     */
    @AdminOperationLog
   	@RequestMapping("/ids/soldoutcheck")
    @ResponseBody
   	public JsonResponse soldoutCheckIds(String productIds) {
       	JsonResponse jsonResponse = new JsonResponse();
   		Map<String, Object> data = new HashMap<>();
   		try {
   			String[] productIdarr = productIds.split(",");
   			int count = 0;
   			if (null != productIdarr && productIdarr.length != 0) {
   				for (String productIdStr : productIdarr) {
   					long productId = Long.parseLong(productIdStr);
   					count += productService.soldoutCheckIds(productId);
   				}
   			}
   			if (count > 0) {
   				data.put("notAllSoldOut", 1);
			}else{
				data.put("notAllSoldOut", 0);
			}
   			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			return jsonResponse.setError("平台下架供应商商品检测e："+e.getMessage());
		}
      }
	
}
