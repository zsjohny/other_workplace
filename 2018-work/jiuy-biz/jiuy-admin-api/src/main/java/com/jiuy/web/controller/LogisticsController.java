package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.LogisticsFacade;
import com.jiuy.core.dao.LOLocationDao;
import com.jiuy.core.meta.logistics.LOLPostageVO;
import com.jiuy.core.service.logistics.LOLocationService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.logistics.LOLocation;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@Login
@RequestMapping("/logistics")
public class LogisticsController {
	
	@Autowired
	private LogisticsFacade logisticsFacade;
	
	@Autowired
	private LOWarehouseService lOWarehouseService;
	
	@Autowired
	private LOLocationService lOLocationService;
	
	@Autowired
	private LOLocationDao lOLocationDao;
	
	/***************************************************************
	* 地理位置管理(城市)
	***************************************************************/
	@RequestMapping(value = "/srchlocation", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse srcLocation(@RequestParam(value = "type", required = false, defaultValue = "0")int type) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<LOLocation> locations = lOLocationService.search(type);
		data.put("locations", locations);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	
	/***************************************************************
	* 物流管理
	***************************************************************/
    @AdminOperationLog
	@RequestMapping(value = "/postageindex", method =RequestMethod.GET)
	public String postageIndex() {
		return "page/backend/logisticspostage";
	}
	
	@RequestMapping("/srchlogistics")
	@ResponseBody
	public JsonResponse srchLogistics(@RequestParam(value = "deliveryId")int deliveryLocation) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<LOLPostageVO> locations = logisticsFacade.srchLogistics(deliveryLocation);
		
		data.put("locations", locations);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/savepostage", method = RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse savePostage(@RequestBody String postageJson) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = logisticsFacade.savePostage(postageJson);
		
		return jsonResponse.setResultCode(resultCode);
	}
	
	/***************************************************************
	* 仓库管理
	***************************************************************/
    @AdminOperationLog
	@RequestMapping(value = "/warehouseindex", method = RequestMethod.GET)
	public String warehouseIndex() {
		return "page/backend/logisticswarehouse";
	}
	//2017-10-12
	@RequestMapping(value = "/warehouse/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse loadWarehouseById(@PathVariable("id") long id) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		LOWarehouse warehouse = lOWarehouseService.loadById(id);
		long deliveryLocation = warehouse.getDeliveryLocation();
		LOLocation loLocation = lOLocationDao.getById(deliveryLocation);
		warehouse.setCityName(loLocation.getCityName());
		warehouse.setProvinceName(loLocation.getProvinceName());
		
 		data.put("warehouse", warehouse);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	//2017-10-12
	@RequestMapping(value = "/srchwarehouse", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse srchWarehouse(@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "page_size", required = false, defaultValue = "20")int page_size,
			@RequestParam(value = "name", required = false, defaultValue = "")String name) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		PageQuery pageQuery = new PageQuery(page, page_size);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		List<LOWarehouse> warehouses = logisticsFacade.srchWarehouse(pageQuery, name);
		int count = logisticsFacade.srcWarehouseCount(name);
		pageQueryResult.setRecordCount(count);
		
		data.put("warehouses", warehouses);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	//2017-10-12
    /**
     * 
     * @param name
     * @param city
     * @param province
     * @param description
     * @return
     */
    @AdminOperationLog
	@RequestMapping(value = "/addwarehouse")
	@ResponseBody
	public JsonResponse addWarehouse(@RequestParam("name") String name,
			                         @RequestParam("cityName") String city,
			                         @RequestParam("provinceName") String province,
			                         @RequestParam("description") String description) {
		JsonResponse jsonResponse = new JsonResponse();
		LOWarehouse warehouse = new LOWarehouse();
		warehouse.setName(name);
		warehouse.setCityName(city);
		warehouse.setProvinceName(province);
		warehouse.setDescription(description);
		if(isSpaceOrComma(warehouse.getName())){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("仓库名不允许有空格或逗号");
		}
		if(logisticsFacade.equalWarehouseCount(warehouse.getName())>0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("仓库名不允许重名");
		}
		//添加LoLocation
		LOLocation loLocation = new LOLocation();
		loLocation.setCityName(warehouse.getCityName());
		loLocation.setProvinceName(warehouse.getProvinceName());
		loLocation.setType(warehouse.getType());
		loLocation.setCreateTime(System.currentTimeMillis());
		loLocation.setUpdateTime(System.currentTimeMillis());
		lOLocationDao.OnDuplicateKeyUpd(loLocation);
		warehouse.setDeliveryLocation((int)loLocation.getId());
		ResultCode resultCode = logisticsFacade.addWarehouse(warehouse);
		
		return jsonResponse.setResultCode(resultCode);
	}
    //2017-10-12
    @AdminOperationLog
	@RequestMapping(value = "/updwarehouse")
	@ResponseBody
	public JsonResponse updateWarehouse(@RequestParam("id") long id,
			                            @RequestParam("name") String name,
			                            @RequestParam(value = "type",required = false, defaultValue = "0")int type,
			                            @RequestParam("provinceName") String provinceName,
			                            @RequestParam("cityName") String cityName,
			                            @RequestParam(value = "description",required = false, defaultValue = "") String description) {
		JsonResponse jsonResponse = new JsonResponse();
		LOWarehouse warehouse = new LOWarehouse();
		warehouse.setId(id);
		warehouse.setName(name);
		warehouse.setType(type);
		warehouse.setCityName(cityName);
		warehouse.setProvinceName(provinceName);
		warehouse.setDescription(description);
		if(isSpaceOrComma(warehouse.getName())){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("仓库名不允许有空格或逗号");
		}
		if(logisticsFacade.updateEqualWarehouseCount(warehouse.getId(),warehouse.getName())>0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("仓库名不允许重名");
		}
		//添加LoLocation
		LOLocation loLocation = new LOLocation();
		loLocation.setCityName(cityName);
		loLocation.setProvinceName(provinceName);
		loLocation.setType(type);
		loLocation.setCreateTime(System.currentTimeMillis());
		loLocation.setUpdateTime(System.currentTimeMillis());
		lOLocationDao.OnDuplicateKeyUpd(loLocation);
		warehouse.setDeliveryLocation((int)loLocation.getId());
		ResultCode resultCode = logisticsFacade.updateWarehouse(warehouse);
		
		return jsonResponse.setResultCode(resultCode);
	}

    @AdminOperationLog
	@RequestMapping(value = "/rmwarehouse", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse removeWarehouse(@RequestParam(value = "ids") Long[] ids) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = logisticsFacade.removeWarehouse(ids);
		
		return jsonResponse.setResultCode(resultCode);
	}
    
    public boolean isSpaceOrComma (String name){
    	if(name.indexOf(" ")>0 || name.indexOf(",")>0 || name.indexOf("，")>0 || name.startsWith(",") || name.startsWith("，")){
    		return true;
    	}
		return false;
    }

}
