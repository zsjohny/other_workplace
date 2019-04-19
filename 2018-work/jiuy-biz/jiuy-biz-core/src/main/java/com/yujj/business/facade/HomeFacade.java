package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.entity.query.PageQuery;
//import com.store.entity.HomeFloorVO;
import com.yujj.business.service.HomeFloorService;
import com.yujj.entity.homepage.HomeFloorVO;


@Service
public class HomeFacade {
	
	@Autowired
	private HomeFloorService homeFloorService;

	public List<JSONObject> getJsonList(PageQuery pageQuery) {
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		List<HomeFloorVO> homeFloors = homeFloorService.getHomeFloors(pageQuery);
		try {

			for (HomeFloorVO homeFloor : homeFloors) {
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
	
	public int getHomeFloorCount() {
		
		return homeFloorService.getHomeFloorCount();
	}
	
	public List<JSONObject> getJsonListBefore185(PageQuery pageQuery) {
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		List<HomeFloorVO> homeFloors = homeFloorService.getHomeFloorsBefore185(pageQuery);
		try {
			
			for (HomeFloorVO homeFloor : homeFloors) {
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
	
	public int getHomeFloorCount185() {
		return homeFloorService.getHomeFloorCount185();
	}

	public int getHomeFloorCount186(Long activityPlaceId) {
		return homeFloorService.getHomeFloorCount186(activityPlaceId);
	}

	public List<JSONObject> getJsonList186(PageQuery pageQuery, Long activityPlaceId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		List<HomeFloorVO> homeFloors = homeFloorService.getHomeFloors186(pageQuery, activityPlaceId);
		try {

			for (HomeFloorVO homeFloor : homeFloors) {
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

	public int getHomeFloorCount187(FloorType floorType, Long relatedId) {
		return homeFloorService.getHomeFloorCount187(floorType, relatedId);
	}

	public List<JSONObject> getJsonList187(PageQuery pageQuery, FloorType floorType, Long relatedId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>() ;
		
		List<HomeFloorVO> homeFloors;
		homeFloors = homeFloorService.getHomeFloors187(pageQuery, floorType, relatedId);	
		
		try {
			for (HomeFloorVO homeFloor : homeFloors) {
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

}
