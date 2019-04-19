package com.store.service;

import java.util.ArrayList;
import java.util.List;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.query.PageQuery;
import com.store.dao.mapper.StoreWxaMapper;
import com.store.entity.HomeFloorVOShop;



@Service
public class HomeFacade {
	
	@Autowired
	private HomeFloorService homeFloorService;
	
	@Autowired
	private StoreWxaService storeWxaService;
	
	@Autowired
	private StoreWxaMapper storeWxaMapper;

	@Autowired
	private IProductMonitorService productMonitorService;

	public List<JSONObject> getJsonList187(PageQuery pageQuery, FloorType floorType, UserDetail userDetail) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		
		List<HomeFloorVOShop> homeFloors;
		
		homeFloors = homeFloorService.getHomeFloors187(pageQuery, floorType, userDetail);	
		
		try {
			for (HomeFloorVOShop homeFloor : homeFloors) {
				JSONObject floor = new JSONObject();
//				floor.put("templateName", homeFloor.getTemplateName());
//				floor.put("name", homeFloor.getName());
//				floor.put("description", homeFloor.getDescription());
//				floor.put("showName", homeFloor.getShowName());
//				floor.put("hasSpacing", homeFloor.getHasSpacing());

				JSONArray jsonArray = JSON.parseArray(homeFloor.getContent());
				floor.put("content", jsonArray);
				jsonList.add(floor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return jsonList;
	}
	
	public List<JSONObject> getJsonList187(PageQuery pageQuery, FloorType floorType, Long relatedId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		
		List<HomeFloorVOShop> homeFloors;
		homeFloors = homeFloorService.getHomeFloors187(pageQuery, floorType, relatedId);	
		
		try {
			for (HomeFloorVOShop homeFloor : homeFloors) {
				JSONObject floor = new JSONObject();
				floor.put("templateName", homeFloor.getTemplateName());
				floor.put("name", homeFloor.getName());
				floor.put("description", homeFloor.getDescription());
				floor.put("showName", homeFloor.getShowName());
				floor.put("hasSpacing", homeFloor.getHasSpacing());

				JSONArray jsonArray = JSON.parseArray(homeFloor.getContent());
				floor.put("content", jsonArray);
				jsonList.add(floor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return jsonList;
	}
	
	public String getQRCode(UserDetail userDetail) {
		
//		StoreWxa storeWxa = storeWxaMapper.selectById(userDetail.getId());
		
		return storeWxaService.getQRCode(userDetail.getId());
		
	}

	public int getHomeFloorCount187(FloorType floorType, Long relatedId) {
		// TODO Auto-generated method stub
		return homeFloorService.getHomeFloorCount187(floorType, relatedId);
	}

	public List<JSONObject> getJsonList188(PageQuery pageQuery, FloorType floorType, Long relatedId, UserDetail userDetail) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		List<HomeFloorVOShop> homeFloors = homeFloorService.getHomeFloors188(pageQuery, floorType, relatedId, userDetail);
		//boolean result = false;
		try {
			for (HomeFloorVOShop homeFloor : homeFloors) {
				JSONObject floor = new JSONObject();
				floor.put("id", homeFloor.getId());
				floor.put("templateName", homeFloor.getTemplateName());
				floor.put("name", homeFloor.getName());
				floor.put("description", homeFloor.getDescription());
				floor.put("showName", homeFloor.getShowName());
				floor.put("hasSpacing", homeFloor.getHasSpacing());

				JSONArray jsonArray = JSON.parseArray(homeFloor.getContent());
				productMonitorService.fillTemplateProduct(jsonArray);
				floor.put("content", jsonArray);
				jsonList.add(floor);
//				if("模板8-21".equals(homeFloor.getTemplateName())){
//					result = true;
//				}
			}
//			JSONObject floor = new JSONObject();
//			floor.put("haveTag", result);
//			jsonList.add(floor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonList;
	}

	public boolean getHasTag(FloorType floorType, Long relatedId, UserDetail userDetail) {
		return homeFloorService.getHasTag(floorType, relatedId, userDetail);
	}
}