package com.jiuy.store.tool.controller.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ShopTagNavigation;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ShopTagNavigationMapper;
import com.store.service.ShopTagNavigationService;
import com.store.service.ShopTagProductService;

/**
 * <p>
 * 小程序标签导航表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-11
 */
@Controller
@RequestMapping("/shop/shopTagNavigation")
public class ShopTagNavigationController {
	private static final Logger logger = LoggerFactory.getLogger(ShopTagNavigationController.class);
	@Autowired
	private ShopTagNavigationService shopTagNavigationService;
	@Autowired
	private ShopTagNavigationMapper shopTagNavigationMapper;
	

	@Autowired
	private ShopTagProductService shopTagProductService;

	@RequestMapping("/save/auth")
	@ResponseBody
	public JsonResponse addTag(@RequestParam(value = "navigationName", required = true) String navigationName,
			@RequestParam(value = "navigationId", required = false, defaultValue = "0") long navigationId,
			@RequestParam(value = "navigationImage", required = true) String navigationImage,
			@RequestParam(value = "tagId", required = true) long tagId, UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		// 判断导航名是否重复
		boolean result = shopTagNavigationService.validate(navigationId,navigationName, storeId);
		if (result) {
			return jsonResponse.setError("导航名称已存在");
		}
		try {
			ShopTagNavigation shopTagNavigation = new ShopTagNavigation();
			
			shopTagNavigation.setUpdateTime(System.currentTimeMillis());
			shopTagNavigation.setNavigationName(navigationName);
			shopTagNavigation.setNavigationImage(navigationImage);
			shopTagNavigation.setTagId(tagId);
			
			if (navigationId == 0) {
				Wrapper<ShopTagNavigation> wrapper = new EntityWrapper<ShopTagNavigation>().eq("store_id", storeId).eq("status", 0);
				Integer count = shopTagNavigationMapper.selectCount(wrapper);
				if (count > 8) {
					return jsonResponse.setError("导航数量不能超过8个");
				}
				shopTagNavigation.setCreateTime(System.currentTimeMillis());
				shopTagNavigation.setStoreId(storeId);
				shopTagNavigation.setWeight(count + 1);
				shopTagNavigationService.addTagNavigation(shopTagNavigation);
			} else {
				shopTagNavigation.setId(navigationId);
				shopTagNavigationService.updateTagNavigation(shopTagNavigation);
			}
			return jsonResponse.setSuccessful().setData("ok");
		}catch (RuntimeException e) {
			return jsonResponse.setError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("保存标签导航失败！");
		}
	}

	@RequestMapping("/list/auth")
	@ResponseBody
	public JsonResponse list(UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			List<Map<String, String>>  listMap = new ArrayList<Map<String, String>>();//shopTagNavigationMapper.selectTagNavigationMap(storeId);
//			logger.info("获取标签导航列表，storeId："+storeId);
			List<ShopTagNavigation> shopNavigationList =  shopTagNavigationService.getShopNavigationList( storeId);
//			logger.info("获取标签导航列表，shopNavigationList："+JSON.toJSONString(shopNavigationList));
			for(ShopTagNavigation shopTagNavigation : shopNavigationList){
				long shopTagNavigationId = shopTagNavigation.getId();
				long tagId = shopTagNavigation.getTagId();
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", String.valueOf(shopTagNavigationId));
				map.put("tagId", String.valueOf(tagId));
				map.put("navigationName", shopTagNavigation.getNavigationName());
				map.put("navigationImage", shopTagNavigation.getNavigationImage());
				int productCount = shopTagProductService.getSoldOutProductCount(tagId,storeId);
				map.put("productCount", String.valueOf(productCount));
				listMap.add(map);
			}
//			logger.info("获取标签导航列表，listMap："+JSON.toJSONString(listMap));
			return jsonResponse.setSuccessful().setData(listMap);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取标签列表失败！");
		}
	}

	/**
	 * 保存标签导航排序
	 * 
	 * @param navigationIds
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/orderSave/auth")
	@ResponseBody
	public JsonResponse orderSave(@RequestParam(value = "navigationIds", required = true) String navigationIds,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			shopTagNavigationService.orderSave(navigationIds);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("保存标签导航排序失败！");
		}
	}
	/**
	 * 删除标签导航
	 * @param navigationId
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/deleteNavigation/auth")
	@ResponseBody
	public JsonResponse deleteNavigation(@RequestParam(value = "navigationId", required = true) long navigationId,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			shopTagNavigationService.deleteNavigation(navigationId, storeId);
			//对导航进行重新排序
			logger.info("对导航进行重新排序,storeId:"+storeId);
			List<ShopTagNavigation> shopNavigationList = shopTagNavigationService.getShopNavigationList(storeId);
			logger.info("对导航进行重新排序,shopNavigationList:"+JSON.toJSONString(shopNavigationList));
			StringBuffer navigationIds = new StringBuffer();
			for (ShopTagNavigation shopTagNavigation : shopNavigationList ) {
				navigationIds.append(shopTagNavigation.getId()+",");
			}
			logger.info("对导航进行重新排序,navigationIds:"+navigationIds);
			shopTagNavigationService.orderSave(navigationIds.toString());
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("删除标签导航失败！");
		}
	}

}
