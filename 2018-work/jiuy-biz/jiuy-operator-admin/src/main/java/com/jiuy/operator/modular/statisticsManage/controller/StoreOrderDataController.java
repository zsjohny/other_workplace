package com.jiuy.operator.modular.statisticsManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.web.help.JsonResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 门店订单数据导出控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 09:50:08
 */
@Controller
@RequestMapping("/storeOrderData")
public class StoreOrderDataController extends BaseController {

	private String PREFIX = "/statisticsManage/storeOrderData/";
	@Autowired
	private IStoreOrderNewService storeOrderNewService;

	/**
	 * 跳转到门店订单数据导出首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "storeOrderData.html";
	}

	/**
	 * 跳转到添加门店订单数据导出
	 */
	@RequestMapping("/storeOrderData_add")
	public String storeOrderDataAdd() {
		return PREFIX + "storeOrderData_add.html";
	}

	/**
	 * 跳转到修改门店订单数据导出
	 */
	@RequestMapping("/storeOrderData_update/{storeOrderDataId}")
	public String storeOrderDataUpdate(@PathVariable Integer storeOrderDataId, Model model) {
		return PREFIX + "storeOrderData_edit.html";
	}

	/**
	 * 获取门店订单数据导出列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(String condition) {
		return null;
	}

	/**
	 * 新增门店订单数据导出
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 删除门店订单数据导出
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() {
		return SUCCESS_TIP;
	}

	/**
	 * 修改门店订单数据导出
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 门店订单数据导出详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail() {
		return null;
	}

	/**
	 * 
	 * 获取门店订单数据导出列表
	 */
	@RequestMapping(value = "/exportOrderData")
	@ResponseBody
	public Object exportStoreUserData(HttpServletResponse response, String columnNamesIds,
			@RequestParam(value = "beginTime", required = false, defaultValue = "0") long beginTime,
			@RequestParam(value = "endTime", required = false, defaultValue = "0") long endTime) throws IOException {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			// 该日期的起始时间+24*60*60*1000
			endTime = endTime + 24 * 60 * 60 * 1000;

			List<Map<String, Object>> list = storeOrderNewService.exportOrderData(beginTime, endTime);
			List<String> columnNamesList = new ArrayList<>();
			List<String> keysList = new ArrayList<>();
			String[] columnNamesIdArr = columnNamesIds.trim().split(",");
			for (String columnNamesId : columnNamesIdArr) {
				switch (Integer.valueOf(columnNamesId)) {
				case 1:
					columnNamesList.add("用户ID");
					keysList.add("id");
					break;
				case 2:
					columnNamesList.add("订单编号");
					keysList.add("orderNo");
					break;
				case 3:
					columnNamesList.add("订单状态");
					keysList.add("orderStatus");
					break;
				case 4:
					columnNamesList.add("交易编号");
					keysList.add("paymentNo");
					break;
				case 5:
					columnNamesList.add("订单类型");
					keysList.add("orderType");
					break;
				case 6:
					columnNamesList.add("款号");
					keysList.add("clothesNumber");
					break;
				case 7:
					columnNamesList.add("商品主图");
					keysList.add("mainImg");
					break;
				case 8:
					columnNamesList.add("品牌");
					keysList.add("brandName");
					break;
				case 9:
					columnNamesList.add("物流单号");
					keysList.add("expressOrderNo");
					break;
				case 10:
					columnNamesList.add("快递公司");
					keysList.add("expressSupplier");
					break;
				case 11:
					columnNamesList.add("物流更新时间");
					keysList.add("expressInfoUpdateTime");
					break;
				case 12:
					columnNamesList.add("所属一级分类名称");
					keysList.add("oneCategoryName");
					break;
				case 13:
					columnNamesList.add("所属二级分类名称");
					keysList.add("twoCategoryName");
					break;
				case 14:
					columnNamesList.add("所属三级分类名称");
					keysList.add("threeCategoryName");
					break;
				case 15:
					columnNamesList.add("商家优惠金额");
					keysList.add("supplierActualdiscount");
					break;
				case 16:
					columnNamesList.add("商家优惠券名称");
					keysList.add("supplierTemplateName");
					break;
				case 17:
					columnNamesList.add("平台优惠金额");
					keysList.add("platformActualdiscount");
					break;
				case 18:
					columnNamesList.add("平台优惠券名称");
					keysList.add("platformTemplateName");
					break;
				case 19:
					columnNamesList.add("退款单号");
					keysList.add("refundOrderNo");
					break;
				case 20:
					columnNamesList.add("退款金额");
					keysList.add("refundCost");
					break;
				case 21:
					columnNamesList.add("售后状态");
					keysList.add("refundStatus");
					break;
				case 22:
					columnNamesList.add("退款原因");
					keysList.add("refundReason");
					break;
				case 23:
					columnNamesList.add("申请售后时间");
					keysList.add("applyTime");
					break;
				case 24:
					columnNamesList.add("供应商收货人");
					keysList.add("receiver");
					break;
				case 25:
					columnNamesList.add("供应商收货地址");
					keysList.add("supplierReceiveAddress");
					break;
				case 26:
					columnNamesList.add("供应商收货人电话");
					keysList.add("receiverPhone");
					break;
				case 27:
					columnNamesList.add("退款类型");
					keysList.add("refundType");
					break;
				case 28:
					columnNamesList.add("退款完成时间");
					keysList.add("refundTime");
					break;
				case 29:
					columnNamesList.add("退款说明");
					keysList.add("refundRemark");
					break;
				case 30:
					columnNamesList.add("所在省份");
					keysList.add("province");
					break;
				case 31:
					columnNamesList.add("所在城市");
					keysList.add("city");
					break;
				case 32:
					columnNamesList.add("所在区县");
					keysList.add("country");
					break;
				case 33:
					columnNamesList.add("注册手机号");
					keysList.add("registerPhoneNumber");
					break;
				case 34:
					columnNamesList.add("注册店铺名");
					keysList.add("registerBusinessName");
					break;
				case 35:
					columnNamesList.add("用户注册时间");
					keysList.add("registerTime");
					break;
				case 36:
					columnNamesList.add("用户注册地址");
					keysList.add("businessAddress");
					break;
				case 37:
					columnNamesList.add("下单时间");
					keysList.add("orderCreateTime");
					break;
				case 38:
					columnNamesList.add("付款时间");
					keysList.add("payTime");
					break;
				case 39:
					columnNamesList.add("订单确认收货时间");
					keysList.add("confirmSignedTime");
					break;
				case 40:
					columnNamesList.add("订单优惠前金额");
					keysList.add("totalMoney");
					break;
				case 41:
					columnNamesList.add("订单优惠后金额");
					keysList.add("totalPay");
					break;
				case 42:
					columnNamesList.add("订单件数");
					keysList.add("buyCount");
					break;
				case 43:
					columnNamesList.add("买家收货地址");
					keysList.add("ExpressInfo");
					break;
				case 44:
					columnNamesList.add("推荐人手机号码");
					keysList.add("groundUserPhone");
					break;
				case 45:
					columnNamesList.add("推荐人姓名");
					keysList.add("groundUserName");
					break;
				default:
					break;
				}
			}
			ExcelUtil.exportExcel(response, list, keysList.toArray(new String[0]),columnNamesList.toArray(new String[0]), "门店订单数据导出");
			return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("导出门店订单数据失败！");
		}
	}

}
