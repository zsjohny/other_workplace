package com.jiuy.store.tool.controller.mobile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.ShopTagService;

/**
 * <p>
 * 门店标签表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-09
 */
@Controller
@RequestMapping("/mobile/shopTag")
public class ShopTagController {

	private static final Logger logger = LoggerFactory.getLogger(ShopTagController.class);

	@Autowired
	private ShopTagService shopTagService;

	/**
	 * 保存标签
	 * 
	 * @param tagName
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/save/auth")
	@ResponseBody
	public JsonResponse addTag(@RequestParam(value = "tagName", required = true) String tagName,
			@RequestParam(value = "tagId", required = false, defaultValue = "0") long tagId,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		// 判断标签名是否重复
		boolean result = shopTagService.validate(tagName, storeId);
		if (!result) {
			return jsonResponse.setError("标签已存在");
		}
		try {
			if (tagId == 0) {
				shopTagService.addTag(tagName, storeId);
			} else {
				shopTagService.updateTag(tagId,tagName,storeId);
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("添加标签失败！");
		}
	}
	/**
	 * 标签列表
	 * @param userDetail
	 * @return
	 */
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
//			List<Map<String,Object>> map = shopTagService.getTagMap(storeId);
			List<Map<String,Object>> map = shopTagService.getTagMapV377(storeId, Arrays.asList(1));
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取标签列表失败！");
		}
	}
	/**
	 * 删除标签
	 * @param tagId
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/deleteTag/auth")
	@ResponseBody
	public JsonResponse deleteTag(@RequestParam(value = "tagId", required = true) long tagId,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		//判断标签是否与导航绑定，绑定则不能删
		boolean result = shopTagService.bindNavigation(tagId,storeId);
		if (result) {
			return jsonResponse.setError("该标签为导航标签，请先取消小程序导航关联标签，再操作删除标签");
		}
		try {
			shopTagService.deleteTag(tagId,storeId);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("删除标签失败！");
		}
	}
}
