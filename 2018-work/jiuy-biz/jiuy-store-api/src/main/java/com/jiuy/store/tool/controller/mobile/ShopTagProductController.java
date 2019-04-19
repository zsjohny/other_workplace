package com.jiuy.store.tool.controller.mobile;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ShopTag;
import com.jiuyuan.entity.newentity.ShopTagProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.ShopTagProductService;
import com.store.service.ShopTagService;

/**
 * <p>
 * 标签商品中间表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-09
 */
@Controller
@RequestMapping("/mobile/shopTagProduct")
public class ShopTagProductController {

	private static final Logger logger = LoggerFactory.getLogger(ShopTagProductController.class);

	@Autowired
	private ShopTagProductService shopTagProductService;
	@Autowired
	private ShopTagService shopTagService;

	/**
	 * 商品绑定标签
	 * 
	 * @param productId
	 * @param tagIds
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/bindTag/auth")
	@ResponseBody
	public JsonResponse bindTag(@RequestParam(value = "productId", required = true) long productId,
			@RequestParam(value = "tagIds", required = false, defaultValue = "") String tagIds,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			// 无论是否选中标签第一步清除标签
			shopTagProductService.clearTag(productId, storeId);
			if (StringUtils.isNotEmpty(tagIds)) {
				String[] tagArr = tagIds.split(",");
				for (String tag : tagArr) {
					// 商品绑定标签
					shopTagProductService.bindTag(Long.valueOf(tag), productId, storeId);
				}
			}
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("商品绑定标签失败！");
		}
	}

	@RequestMapping("/getProductBindTag/auth")
	@ResponseBody
	public JsonResponse getBindProductTag(@RequestParam(value = "productId", required = true) long productId,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if (storeId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			// 获取商品绑定的标签
			List<ShopTagProduct> shopTagProductList = shopTagProductService.getBindProductTagList(productId, storeId);
			// 获取所有标签
			List<Map<String, Object>> tagList = shopTagService.getTagList(storeId);
			// 设置选中标识
			for (Map<String, Object> map : tagList) {
				map.put("choosed", 0);
				for (ShopTagProduct shopTagProduct : shopTagProductList) {
					if ((long) map.get("id") == shopTagProduct.getTagId()) {
						map.put("choosed", 1);
					}
				}
			}
			return jsonResponse.setSuccessful().setData(tagList);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("获取绑定该商品的标签列表！");
		}
	}
}
