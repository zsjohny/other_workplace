package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.PropertyService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/propertyMangement")
@Login
public class PropertyManagementController {
	
	 @Autowired
	 private PropertyService propertyService;
	 
	 @Autowired
	 private BrandLogoServiceImpl brandLogoService;

	 @RequestMapping("/page")
	 @AdminOperationLog
	 public String propertyManagementPage(ModelMap modelMap) {
		 List<Map<String, Object>> colors = propertyService.getColors();
		 List<BrandLogo> brands = brandLogoService.getBrands();
 		 
 		 modelMap.put("colors", colors);
 		 modelMap.put("brands", brands);
		 
		 return "page/backend/propertyMangement";
	 }
	 
	 @RequestMapping("/load")
	 @AdminOperationLog
	 @ResponseBody
	 public JsonResponse load() {
		 JsonResponse jsonResponse = new JsonResponse();
		 Map<String, Object> data = new HashMap<>();
		 List<Map<String, Object>> colors = propertyService.getColors();
		 List<BrandLogo> brands = brandLogoService.getBrands();
 		 
		 data.put("colors", colors);
		 data.put("brands", brands);
		 
		 return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	 }
	 
	 @RequestMapping("/search")
	 @ResponseBody
	 public JsonResponse search(@RequestParam("property_name_id") Long propertyNameId,
			 @RequestParam(value = "property_value", required = false) String propertyValue) {
		 JsonResponse jsonResponse = new JsonResponse();
		 
		 Map<String, Object> data = new HashMap<>();
		 List<ProductPropValue> productPropValues = propertyService.search(propertyNameId, propertyValue);
		 data.put("list", productPropValues);
		 
		 return jsonResponse.setSuccessful().setData(data);
	 }
	 
	 @RequestMapping(value="/add")
	 @ResponseBody
	 @AdminOperationLog
	 public JsonResponse addColor(@RequestParam(value = "property_value") String propertyValue,
			 @RequestParam(value = "property_name_id") Long propertyNameId) {
		 JsonResponse jsonResponse = new JsonResponse();
		 PropertyName propertyName = PropertyName.getByIntValue(propertyNameId);
		 ProductPropValue ppv = propertyService.add(propertyValue, propertyName);
		 
		 return jsonResponse.setSuccessful().setData(ppv);
	 }
	 

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @AdminOperationLog
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse remove(@RequestParam(value="id") Long id, @RequestParam(value="type_id", required=false, defaultValue="0") int type_id) {
		 JsonResponse jsonResponse = new JsonResponse();

		 if(type_id == PropertyName.BRAND.getValue()) {
			 brandLogoService.remove(CollectionUtil.createList(id));
		 }
		 propertyService.remove(CollectionUtil.createList(id));
		 
		 return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	 }
	 
    @RequestMapping(value = "/rmArray")
	@AdminOperationLog
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
    public JsonResponse removeArray(@RequestParam(value = "id") Long[] ids,
                                    @RequestParam(value = "type_id", required = false, defaultValue = "0") int type_id) {
        JsonResponse jsonResponse = new JsonResponse();


        Collection<Long> brandIds = Arrays.asList(ids);
        if (type_id == PropertyName.BRAND.getValue()) {
            brandLogoService.remove(brandIds);
        }
        propertyService.remove(brandIds);
		 
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	 }
	 
	 @RequestMapping(value = "/chkRepeat", method = RequestMethod.POST)
	 @ResponseBody
	 public JsonResponse chkRepeat(@RequestParam(value = "property_id") int propertyNameId, @RequestParam(value = "name") String name) {
		 JsonResponse jsonResponse = new JsonResponse();
		 
		 boolean unduplicated = propertyService.chkRepeat(propertyNameId, name);
		 
		 return jsonResponse.setSuccessful().setData(unduplicated);
	 }
	 
		@RequestMapping(value = "/getcolorlist")
		public String getColorList(HttpServletRequest request, ModelMap modelMap) {
			Map<String, ProductPropValue> ppMap = propertyService.getPropertyValueMap();
			Collection<ProductPropValue> values = ppMap.values();
			Collection<ProductPropValue> elementValues = getPropertyByNameId(PropertyName.COLOR.getValue(), values);

			modelMap.addAttribute("propertylist", elementValues);
			modelMap.addAttribute("colorPropertyId", PropertyName.COLOR.getValue());
			return "json";
		}

		@RequestMapping(value = "/getsizelist")
		public String getSizeList(HttpServletRequest request, ModelMap modelMap) {
			Map<String, ProductPropValue> ppMap = propertyService.getPropertyValueMap();
			Collection<ProductPropValue> values = ppMap.values();
			Collection<ProductPropValue> elementValues = getPropertyByNameId(PropertyName.SIZE.getValue(), values);

			modelMap.addAttribute("propertylist", elementValues);
			modelMap.addAttribute("sizePropertyId", PropertyName.SIZE.getValue());
			return "json";
		}

		@RequestMapping(value = "/getyearlist")
		public String getYearList(HttpServletRequest request, ModelMap modelMap) {
			Map<String, ProductPropValue> ppMap = propertyService.getPropertyValueMap();
			Collection<ProductPropValue> values = ppMap.values();
			Collection<ProductPropValue> elementValues = getPropertyByNameId(PropertyName.YEAR.getValue(), values);
			modelMap.addAttribute("fasionlist", elementValues);
			return "json";
		}

		@RequestMapping(value = "/getseasonlist")
		public String getSeasonList(HttpServletRequest request, ModelMap modelMap) {
			Map<String, ProductPropValue> ppMap = propertyService.getPropertyValueMap();
			Collection<ProductPropValue> values = ppMap.values();
			Collection<ProductPropValue> elementValues = getPropertyByNameId(PropertyName.SEASON.getValue(), values);
			modelMap.addAttribute("fasionlist", elementValues);
			return "json";
		}

		private Collection<ProductPropValue> getPropertyByNameId(long id, Collection<ProductPropValue> values) {
			Collection<ProductPropValue> subSet = new ArrayList<ProductPropValue>();
			if (null != values) {
				for (ProductPropValue ppv : values) {
					if (ppv.getPropertyNameId() == id) {
						subSet.add(ppv);
					}
				}
				return subSet;
			}
			return null;
		}
		
}
