package com.jiuy.operator.modular.statisticsManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.store.service.StoreLoginDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import com.jiuyuan.entity.newentity.StoreBusiness;

/**
 * 门店用户数据导出控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 09:49:33
 */
@Controller
@RequestMapping("/storeUserData")
public class StoreUserDataController extends BaseController {

	private String PREFIX = "/statisticsManage/storeUserData/";
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;

	/**
	 * 跳转到门店用户数据导出首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "storeUserData.html";
	}

	/**
	 * 跳转到添加门店用户数据导出
	 */
	@RequestMapping("/storeUserData_add")
	public String storeUserDataAdd() {
		return PREFIX + "storeUserData_add.html";
	}

	/**
	 * 跳转到修改门店用户数据导出
	 */
	@RequestMapping("/storeUserData_update/{storeUserDataId}")
	public String storeUserDataUpdate(@PathVariable Integer storeUserDataId, Model model) {
		return PREFIX + "storeUserData_edit.html";
	}

	/**
	 * 获取门店用户数据导出列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(String condition) {
		return null;
	}

	/**
	 * 新增门店用户数据导出
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 删除门店用户数据导出
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() {
		return SUCCESS_TIP;
	}

	/**
	 * 修改门店用户数据导出
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 门店用户数据导出详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail() {
		return null;
	}

	/**
	 * 获取门店用户数据导出列表
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportUserData")
	@ResponseBody
	public Object exportStoreUserData(HttpServletResponse response, String columnNamesIds, long beginTime, long endTime)
			throws IOException {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			// 该日期的起始时间+24*60*60*1000
			endTime = endTime + 24 * 60 * 60 * 1000;
			List<Map<String, Object>> list = storeBusinessNewService.exportUserData(beginTime, endTime);
			List<String> columnNamesList = new ArrayList<>();
			List<String> keysList = new ArrayList<>();
			String[] columnNamesIdArr = columnNamesIds.trim().split(",");
			for (String columnNamesId : columnNamesIdArr) {
				switch (Integer.valueOf(columnNamesId)) {
				case 1:
					columnNamesList.add("用户id");
					keysList.add("id");
					break;
				case 2:
					columnNamesList.add("注册手机号");
					keysList.add("registerPhone");
					break;
				case 3:
					columnNamesList.add("注册店铺名称");
					keysList.add("registerBusinessname");
					break;
				case 4:
					columnNamesList.add("注册人");
					keysList.add("legalperson");
					break;
				case 5:
					columnNamesList.add("注册时间");
					keysList.add("registerTime");
					break;
				case 6:
					columnNamesList.add("所在省份");
					keysList.add("province");
					break;
				case 7:
					columnNamesList.add("所在区县");
					keysList.add("country");
					break;
				case 8:
					columnNamesList.add("所在城市");
					keysList.add("city");
					break;
				case 9:
					columnNamesList.add("注册地址");
					keysList.add("registerAddress");
					break;
				case 10:
					columnNamesList.add("审核状态");
					keysList.add("status");
					break;
				case 11:
					columnNamesList.add("第一次访问时间");
					keysList.add("firstVisitTime");
					break;
				case 12:
					columnNamesList.add("最后一次访问时间");
					keysList.add("lastVisitTime");
					break;
				case 13:
					columnNamesList.add("商品访问总次数");
					keysList.add("productTotalVisitCount");
					break;
				case 14:
					columnNamesList.add("推荐人手机号码");
					keysList.add("groundUserPhone");
					break;
				case 15:
					columnNamesList.add("推荐人姓名");
					keysList.add("groundUserName");
					break;
				default:
					break;
				}
			}
			ExcelUtil.exportExcel(response, list, keysList.toArray(new String[0]),columnNamesList.toArray(new String[0]), "门店用户数据导出");
			return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("导出门店用户数据失败！");
		}
	}


}
