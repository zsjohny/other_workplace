package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.BrandFacade;
import com.jiuy.core.dao.BrandDao;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.brand.BrandLogoCommissionPercentage;
import com.jiuyuan.entity.brand.BrandLogoVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 此处是针对品牌的管理(包含LOGO、名字),和品牌馆不同概念
 * @author Jeff.Zhan
 *
 */
@RequestMapping("/brandinner")
@Controller
@Login
public class BrandManageController {
	
	private static final Logger logger = LoggerFactory.getLogger(BrandManageController.class);
	
	@Autowired
	private BrandLogoServiceImpl brandLogoService;
	
	@Autowired
	private BrandFacade brandFacade;
	
	@Autowired
	private BrandDao brandDaoSqlImpl;
	
	@Autowired
	private IProductNewService productNewService;
	
    @AdminOperationLog
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "page/backend/brandinner";
	}

	//@RequestMapping(value = "/search", method = RequestMethod.GET)
	//@ResponseBody
	public JsonResponse getBrandList(@RequestParam(value = "name", defaultValue = "", required = false)String name,
			@RequestParam(value = "page_size", required = false, defaultValue = "4") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
        List<BrandLogoVO> brands = brandFacade.search(name, pageQuery);
		int count = brandLogoService.searchCount(name);
		pageQueryResult.setRecordCount(count);
		
		data.put("brands", brands);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	//2017-10-11
	/**
	 * 
	 * @param pageSize
	 * @param page
	 * @param status 状态:1:禁用,0:正常,-1:删除,-100:全部（包含禁用和正常）
	 * @param keywords 中英文名
	 * @return
	 */
		@RequestMapping(value = "/search")
		@ResponseBody
		public JsonResponse getBrandList(@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize,
				                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
				                         @RequestParam(value = "status",required = false, defaultValue = "-100") int status,
				                         @RequestParam(value = "brand_type",required = false, defaultValue = "-1") int brandType,
				                         @RequestParam(value = "keywords",required = false,defaultValue = "") String keywords) {
			JsonResponse jsonResponse = new JsonResponse();
			Map<String, Object> data = new HashMap<String, Object>();
			PageQuery pageQuery = new PageQuery(page, pageSize);
			PageQueryResult pageQueryResult = new PageQueryResult();
			BeanUtils.copyProperties(pageQuery, pageQueryResult);
			
	        List<BrandLogoVO> brands = brandFacade.search(status,keywords, pageQuery, brandType);
			int count = brandLogoService.searchCount(status,keywords,brandType);
			pageQueryResult.setRecordCount(count);
			
			data.put("brands", brands);
			data.put("total", pageQueryResult);
			
			return jsonResponse.setSuccessful().setData(data);
		}


	
	@RequestMapping("/{brand_id}")
	@ResponseBody
	public JsonResponse getBrand(@PathVariable(value = "brand_id") long brandId) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		BrandLogoVO brandLogoVO = null;
		
		try {
			brandLogoVO = brandFacade.getBrandVOById(brandId);
		} catch (Exception e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		data.put("brand", brandLogoVO);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse addBrand(@RequestParam(value = "brand_name") String brandName,
			@RequestParam(value = "brand_type") int brandType,
			@RequestParam(value = "brand_promotion_img") String brandPromotionImg,  
            @RequestParam(value = "logo") String logo,
            @RequestParam(value = "cn_name") String cnName,
            @RequestParam(value = "description",required = false,defaultValue = "") String description,
            @RequestParam(value = "weight") int weight,
            @RequestParam(value = "brand_identity", required = false, defaultValue = "") String brandIdentity,
            @RequestParam(value = "discount_info", required = false, defaultValue = "[]") String discountInfo,
            @RequestParam(value = "cloth_number_prefix")String clothNumberPrefix) {
		JsonResponse jsonResponse = new JsonResponse();
		
		BrandLogo brandLogo = new BrandLogo(0, 0, brandName.trim(), logo, cnName.trim(), description, weight, brandIdentity, brandType, brandPromotionImg);
		brandLogo.setClothNumberPrefix(clothNumberPrefix.trim());
        try {
        	List<Long> idList = brandDaoSqlImpl.searchIdsByClothNumberPrefix(clothNumberPrefix.trim());
        	if(idList!=null&&idList.size()>0){
        		throw new RuntimeException("品牌商品款号前缀重复！");
        	}
            brandFacade.addBrand(brandLogo, PropertyName.BRAND.getValue(), discountInfo);
        } catch (Exception e) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
        }
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	 
	@RequestMapping(value = "/update")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateBrand(@RequestParam(value = "id") int id,
			@RequestParam(value = "brand_id") long brandId,
			@RequestParam(value = "brand_name") String brandName,
			@RequestParam(value = "brand_type") int brandType,
			@RequestParam(value = "brand_promotion_img") String brandPromotionImg, 			
            @RequestParam(value = "logo") String logo,
            @RequestParam(value = "cn_name") String cnName,
            @RequestParam(value = "description",required = false,defaultValue = "") String description,
            @RequestParam(value = "weight") int weight,
            @RequestParam(value = "brand_identity", required = false, defaultValue = "") String brandIdentity,
            @RequestParam(value = "discount_info", required = false, defaultValue = "[]") String discountInfo,
            @RequestParam(value = "cloth_number_prefix",required = false,defaultValue = "") String clothNumberPrefix) {
		JsonResponse jsonResponse = new JsonResponse();
		
		BrandLogo brandLogo = new BrandLogo(id, brandId, brandName.trim(), logo, cnName.trim(), description, weight, brandIdentity, brandType, brandPromotionImg);
		brandLogo.setClothNumberPrefix(clothNumberPrefix.trim());
        try {
        	//
        	List<Long> idList = brandDaoSqlImpl.searchIdsByClothNumberPrefix(clothNumberPrefix.trim());
        	//
        	if(idList !=null&&idList.size()>0&&idList.get(0) != id){
        		throw new RuntimeException("品牌商品款号前缀重复！");
        	}
        	if(idList !=null&&idList.size()>1){
        		throw new RuntimeException("品牌商品款号前缀重复！");
        	}
             brandFacade.updateBrand(brandLogo, discountInfo);
             
             
        } catch (Exception e) {
            return jsonResponse.setError(e.getMessage());
        }
		
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 
	 * @param ids id指的是brandId（propertyValueId）
	 * @return
	 */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
    public JsonResponse removeArray(@RequestParam(value = "ids") Long[] ids) {
        JsonResponse jsonResponse = new JsonResponse();

        brandFacade.remove(ids);
		 
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	 }
    /**
     * 更改状态(0：启用或1：禁用)
     * @param name
     * @return
     */
    @RequestMapping(value="/updateStatus")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updateStatus(@RequestParam(value = "status",required = true ,defaultValue = "0")int status,
    		                         @RequestParam(value = "brand_id")long brandId){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		brandFacade.updateStatus(status,brandId);
    		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
			
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    @RequestMapping(value = "/searchbrand", method = RequestMethod.GET)
   	@ResponseBody
   	public JsonResponse searchBrand(@RequestParam(value = "name", defaultValue = "", required = false)String name) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
		List<BrandLogo> brands = brandLogoService.search(name, new PageQuery(1, 1));
		int count = brandLogoService.searchCount(name);
		
		data.put("brand", brands == null ? null : brands.get(0));
		data.put("total", count);
   		 
   		return jsonResponse.setSuccessful().setData(data);
   	 }
	
	@RequestMapping(value = "/getbrandlist")
	public String getBrands(HttpServletRequest request, ModelMap modelMap) {
		List<BrandLogo> brands = brandLogoService.getBrands();
		
		modelMap.put("brandlist", brands);
		
		return "json";
	}
	
	@RequestMapping(value = "/getbrandlist/commissionpercentage")
   	public String getBrandListWithCommissionPercentage(HttpServletRequest request, ModelMap modelMap){
		List<BrandLogoCommissionPercentage> brandLogosWithCommissionPercentage = brandLogoService.getBrandLogosWithCommissionPercentage();
		modelMap.put("brandlist", brandLogosWithCommissionPercentage);
		
		return "json";
	}
	
}
