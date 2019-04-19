package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.NewStatisticsFacade;
//import com.jiuy.core.service.OrderService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.UserService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/statistics/new")
@Login
public class StatisticsNewController {
	// @Resource
	// private OrderService oService;

	@Resource
	private UserService userService;

	@Autowired
	private NewStatisticsFacade statisticsFacade;

	@Autowired
	private UserManageService userManageService;

	@RequestMapping
	@AdminOperationLog
	public String statistics() {
		return "page/backend/statistics";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse search(
			@RequestParam(value = "start_time", required = false, defaultValue = "1970-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "") String end_time) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		long startTime = 0;
		long endTime = 0;

		try {
			if (!StringUtils.equals(end_time, ""))
				endTime = DateUtil.convertToMSEL(end_time);
			else
				endTime = System.currentTimeMillis();
			startTime = DateUtil.convertToMSEL(start_time);
		} catch (ParseException e) {
			e.printStackTrace();
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		}

		data = statisticsFacade.getData(startTime, endTime);

		return jsonResponse.setSuccessful().setData(data);
	}

	/*
	 * 各大分类里的销售排名
	 */
	@RequestMapping(value = "/category/hotsale")
	@AdminOperationLog
	@ResponseBody
	public void categoryHotSale(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "1970-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "") String end_time,
			@RequestParam(value = "count", required = false, defaultValue = "20") int count) throws IOException {
		long startTime = 0;
		long endTime = 0;

		try {
			if (!StringUtils.equals(end_time, ""))
				endTime = DateUtil.convertToMSEL(end_time);
			else
				endTime = System.currentTimeMillis();
			startTime = DateUtil.convertToMSEL(start_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Map<String, Object>> list = statisticsFacade.hotSaleOfCategory(startTime, endTime, count);

		String columnNames[] = { "分类id", "分类名", "商品名", "商品款号", "销售件数", "开始时间", "结束时间", "库存" };// 列名
		String keys[] = { "categoryId", "categoryName", "Name", "ClothesNumber", "TotalCount", "startTime", "endTime",
				"remainCount" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "hotSale");
	}

	/*
	 * SKU销售排名
	 */
	@RequestMapping(value = "/rank/sku/hotsale")
	@AdminOperationLog
	@ResponseBody
	public void rankSKUHotsale(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-12-19 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-12-26 00:00:00") String end_time)
			throws IOException {
		long startTime = 0;
		long endTime = 0;

		try {
			if (!StringUtils.equals(end_time, ""))
				endTime = DateUtil.convertToMSEL(end_time);
			else
				endTime = System.currentTimeMillis();
			startTime = DateUtil.convertToMSEL(start_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Map<String, Object>> list = statisticsFacade.hotSaleOfActivity(startTime, endTime);

		String columnNames[] = { "排名", "sku编号", "款号", "商品名称", "销售件数", "销售额", "颜色", "尺寸", "开始时间", "结束时间" };// 列名
		String keys[] = { "rank", "skuNo", "clothesNum", "name", "TotalCount", "totalMoney", "color", "size",
				"startTime", "endTime" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "activityHotSale");
	}

	/*
	 * Product销售排名(支持筛选季节)
	 */
	@RequestMapping(value = "/rank/product/hotsale")
	@AdminOperationLog
	@ResponseBody
	public void rankProductHotsale(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-11-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-11-09 00:00:00") String end_time)
			throws IOException {
		long startTime = 0;
		long endTime = 0;

		Set<Long> seasonIds = new HashSet<>();
		// seasonIds.add(430L);
		// seasonIds.add(431L);
		// seasonIds.add(432L);
		seasonIds.add(433L);

		try {
			if (!StringUtils.equals(end_time, ""))
				endTime = DateUtil.convertToMSEL(end_time);
			else
				endTime = System.currentTimeMillis();
			startTime = DateUtil.convertToMSEL(start_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Map<String, Object>> list = statisticsFacade.rankProductHotsale(startTime, endTime, seasonIds);

		String columnNames[] = { "排名", "款号", "商品名称", "销售件数", "开始时间", "结束时间" };// 列名
		String keys[] = { "rank", "clothesNum", "name", "TotalCount", "startTime", "endTime" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "activityHotSale");
	}

	/*
	 * 每日独立登录量
	 */
	@RequestMapping(value = "/perday/login")
	@AdminOperationLog
	@ResponseBody
	public void loginPerDay(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-05-16 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-07-25 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.loginPerDay(startTime, endTime);

		String columnNames[] = { "时间", "登陆量" };// 列名
		String keys[] = { "day", "login" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "login");
	}

	/*
	 * erp有的，本地没有的商品
	 */
	@RequestMapping(value = "/local/notexist/erp")
	@AdminOperationLog
	@ResponseBody
	public void notExistInLocal(HttpServletResponse response) throws IOException, ParseException {

		List<Map<String, Object>> list = statisticsFacade.notExistInLocal();

		String columnNames[] = { "款号" };// 列名
		String keys[] = { "clothesNo" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "erpError");
	}

	/*
	 * 每日注册量、订单量、销售量、销售额
	 */
	@RequestMapping(value = "/perday/platform/basic")
	@AdminOperationLog
	@ResponseBody
	public void basicDataPerDay(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-12-27 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.basicDataPerDay(startTime, endTime);

		String columnNames[] = { "日期", "注册量", "订单量", "销售量", "销售额" };// 列名
		String keys[] = { "day", "register", "order_count", "product_count", "order_money" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "basicDataPerDay");
	}

	/*
	 * 商品销售排名
	 */
	@RequestMapping(value = "/rank/sale/basic")
	@AdminOperationLog
	@ResponseBody
	public void rankSale(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-05-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-09-20 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.rankSale(startTime, endTime);

		String columnNames[] = { "排名", "商品名称", "商品款号", "商品总销售量", "品牌名称", "品牌总销售量", "分类名称", "分类总销售量", "上装分类", "上装分类总销售量",
				"裤装分类", "裤装分类总销售量", "裙装分类", "裙装分类总销售量", "连衣裙分类", "连衣裙分类总销售量" };// 列名
		String keys[] = { "rank", "ClothesNumber", "product", "product_count", "brand", "brand_count", "category",
				"category_count", "top_category", "top_category_count", "pants", "pants_count", "skirt", "skirt_count",
				"dress", "dress_count" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "rankSale_" + start_time + "-" + end_time);
	}

	/*
	 * 地区成交量排名
	 */
	@RequestMapping(value = "/rank/sale/location/order")
	@AdminOperationLog
	@ResponseBody
	public void rankSaleLocation(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-08-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.rankSaleLocation(startTime, endTime);

		String columnNames[] = { "排名", "省", "市", "成交量", "成交量用户数", "成交金额", "统计开始时间", "统计结束时间" };// 列名
		String keys[] = { "rank", "ProvinceName", "CityName", "count", "userCount", "money", "startTime", "endTime" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "rankSaleLocation_" + start_time + "-" + end_time);
	}

	/*
	 * 地区购成交用户数排名
	 */
	@RequestMapping(value = "/rank/sale/location/user")
	@AdminOperationLog
	@ResponseBody
	public void rankSaleLocationUser(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-06-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-08-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.rankSaleLocationUser(startTime, endTime);

		String columnNames[] = { "排名", "省", "市", "成交用户数", "统计开始时间", "统计结束时间" };// 列名
		String keys[] = { "rank", "ProvinceName", "CityName", "count", "startTime", "endTime" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "rankSaleLocationUser_" + start_time + "-" + end_time);
	}

	/*
	 * 用户购买力排名
	 */
	@RequestMapping(value = "/rank/buyer")
	@AdminOperationLog
	@ResponseBody
	public void rankBuyer(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-04-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-11-08 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.rankBuyers(startTime, endTime);

		String columnNames[] = { "排名", "用户名称", "俞姐姐号", "绑定手机号", "所在地区", "成交订单量", "成交金额", "首次成交时间", "末次成交时间", "统计开始时间",
				"统计结束时间" };// 列名
		String keys[] = { "rank", "username", "yjj_number", "bind_phone", "distinct", "count", "money", "first", "last",
				"startTime", "endTime" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "rankbuyer_" + start_time + "-" + end_time);
	}

	/*
	 * 统计某个玖币区间的用户人数
	 */
	@RequestMapping(value = "/jiucoin/user")
	@AdminOperationLog
	@ResponseBody
	public void jiuCoinUser(HttpServletResponse response,
			@RequestParam(value = "min_jiucoin", required = false) Integer minJiuCoin,
			@RequestParam(value = "max_jiucoin", required = false, defaultValue = "500") Integer maxJiuCoin,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-04-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-10-09 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = userManageService.search(startTime, endTime, minJiuCoin, maxJiuCoin);

		String columnNames[] = { "用户名称", "俞姐姐号", "绑定手机号", "玖币数", "统计开始时间", "统计结束时间" };// 列名
		String keys[] = { "username", "yjj_number", "bind_phone", "jiuCoin", "startTime", "endTime" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "rankbuyer_" + start_time + "-" + end_time);
	}

	/*
	 * warehouse-SKU信息 (当前：筛选为已上架 有库存)
	 */
	@RequestMapping(value = "/sku/warehouse")
	@ResponseBody
	public void skuInfoOfWarehouse(HttpServletResponse response) throws IOException, ParseException {

		List<Map<String, Object>> list = statisticsFacade.skuOfWarehouse(AdminConstants.OTHER_WAREHOUSE_ID_LIST);

		String columnNames[] = { "SkuNo", "商品名称", "商品款号", "款号Id", "颜色", "尺码", "库存", "市场价", "品牌" };// 列名
		String keys[] = { "skuNo", "productName", "clothesNum", "productId", "color", "size", "remainCount",
				"marketPrice", "brandName" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "skuInfo");
	}

	/*
	 * brand-SKU信息
	 */
	@RequestMapping(value = "/sku/brand")
	@ResponseBody
	public void skuInfoOfBrand(HttpServletResponse response) throws IOException, ParseException {
		List<Long> brandIds = new ArrayList<>();
		brandIds.add(593L);

		boolean checkOnSale = true;
		List<Map<String, Object>> list = statisticsFacade.skuInfoOfBrand(brandIds, checkOnSale);

		String columnNames[] = { "SkuNo", "商品名称", "商品款号", "款号Id", "颜色", "尺码", "库存", "市场价" };// 列名
		String keys[] = { "skuNo", "productName", "clothesNum", "productId", "color", "size", "remainCount",
				"marketPrice" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "skuInfo");
	}

	/*
	 * 连续签到
	 */
	@RequestMapping(value = "/signin/uninterrupted")
	@ResponseBody
	public void uninterruptedSignIn(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-08-15 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-08-22 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.uninterruptedSignIn(startTime, endTime);

		String columnNames[] = { "编号", "俞姐姐号", "微信名", "手机号", "邮箱", "收件人姓名", "收件地址", "收件人联系方式" };// 列名
		String keys[] = { "no", "YJJNumber", "weixin", "phone", "email", "receiver", "receiveAddress",
				"receiverPhone" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "rankSaleLocationUser_" + start_time + "-" + end_time);
	}

	/*
	 * 每天以SKU为单位的销售额统计
	 */
	@RequestMapping(value = "/perday/sku/sales/amount")
	@ResponseBody
	public void salesAmountOfSKU(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2017-01-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.salesAmountOfSKU(startTime, endTime);

		String columnNames[] = statisticsFacade.salesAmountOfSKUColumnNames(startTime, endTime);
		String keys[] = statisticsFacade.salesAmountOfSKUColumnKeys(startTime, endTime);

		ExcelUtil.exportExcel(response, list, keys, columnNames,
				"perdaySalesAmountOfSKU" + start_time + "-" + end_time);
	}

	/*
	 * 每天以SKU为单位的销售量统计
	 */
	@RequestMapping(value = "/perday/sku/sales/volume")
	@ResponseBody
	public void salesVolumeOfSKU(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2017-01-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.salesVolumeOfSKU(startTime, endTime);

		String columnNames[] = statisticsFacade.salesVolumeOfSKUColumnNames(startTime, endTime);
		String keys[] = statisticsFacade.salesVolumeOfSKUColumnKeys(startTime, endTime);

		ExcelUtil.exportExcel(response, list, keys, columnNames,
				"perdaySalesAmountOfSKU" + start_time + "-" + end_time);
	}

	/*
	 * APP发布至今的每月用户注册数据
	 */
	@RequestMapping(value = "/permonth/register/user")
	@ResponseBody
	public void perMonthRegister(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2015-04-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-12-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.perMonthRegister(startTime, endTime);

		String columnNames[] = { "时间", "用户注册增量", "用户注册总量" };
		String keys[] = { "dateTimes", "addCount", "count" };

		ExcelUtil.exportExcel(response, list, keys, columnNames, "perMonthRegister" + start_time + "-" + end_time);
	}

	/*
	 * APP发布至今的销售数据.按照自然月为时间节点（每月1号开始）统计数据。数据需求内容为当月销售额，当月销售件数，总销售件数，总销售额
	 */
	@RequestMapping(value = "/permonth/sale")
	@ResponseBody
	public void perMonthSale(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2015-04-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2016-12-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.perMonthSale(startTime, endTime);

		String columnNames[] = { "时间", "销售件数", "销售额", "总销售件数", "总销售额" };
		String keys[] = { "dateTimeStr", "buyCount", "total", "totalSaleCount", "totalSaleCash" };

		ExcelUtil.exportExcel(response, list, keys, columnNames, "perMonthRegister" + start_time + "-" + end_time);
	}

	/*
	 * 每日总注册量、手机注册量、微信注册量、订单量、销售量
	 */
	@RequestMapping(value = "/perday/all/basic")
	@AdminOperationLog
	@ResponseBody
	public void basicAllDataPerDay(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2017-01-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<Map<String, Object>> list = statisticsFacade.basicAllDataPerDay(startTime, endTime);

		String columnNames[] = { "日期", "当日注册量", "手机注册量", "微信注册量", "其他注册量", "新用户当日成交订单数", "新用户当日成交商品件数", "当日成交总订单数",
				"当日成交商品总件数", "当日活跃用户数（当日登陆）" };// 列名
		String keys[] = { "day", "allRegister", "phoneRegister", "wxRegister", "otherRegister", "user_order_count",
				"user_product_count", "order_count", "product_count", "login_day" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "2016年数据统计");
	}

	/*
	 * 用户下单的月份、省、数量、金额统计
	 */
	@RequestMapping(value = "/order")
	@AdminOperationLog
	@ResponseBody
	public void getOrderData(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2017-01-01 00:00:00") String end_time)
			throws IOException {
		long startTime = 0;
		long endTime = 0;

		try {
			if (!StringUtils.equals(end_time, ""))
				endTime = DateUtil.convertToMSEL(end_time);
			else
				endTime = System.currentTimeMillis();
			startTime = DateUtil.convertToMSEL(start_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Map<String, Object>> list = statisticsFacade.getOrderDatas(startTime, endTime);

		String columnNames[] = { "订单编号", "下单时间", "下单地区（省）", "订单金额", "订单件数", "俞姐姐号" };// 列名
		String keys[] = { "orderNo", "day", "address", "order_money", "count", "yjjNumber" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "2016订单数据统计");
	}

	/*
	 * 每日sku销量
	 */
	@RequestMapping(value = "/perday/sku/sale")
	@AdminOperationLog
	@ResponseBody
	public void skuSalePerDay(HttpServletResponse response,
			@RequestParam(value = "start_time", required = false, defaultValue = "2016-01-01 00:00:00") String start_time,
			@RequestParam(value = "end_time", required = false, defaultValue = "2017-01-01 00:00:00") String end_time)
			throws IOException, ParseException {
		long startTime = DateUtil.convertToMSEL(start_time);
		long endTime = DateUtil.convertToMSEL(end_time);

		List<String> columnNames = new ArrayList<>();
		List<Map<String, Object>> list = statisticsFacade.getSkuSalePerDayPerDay(startTime, endTime, columnNames);

		// String columnNames[] = { "日期", "当日注册量", "手机注册量" , "微信注册量" , "其他注册量" ,
		// "新用户当日成交订单数" , "新用户当日成交商品件数" , "当日成交总订单数", "当日成交商品总件数",
		// "当日活跃用户数（当日登陆）"};// 列名
		// String keys[] = { "day", "allRegister", "phoneRegister",
		// "wxRegister", "otherRegister", "user_order_count" ,
		// "user_product_count" , "order_count", "product_count"
		// ,"login_day"};// map中的key

		// ExcelUtil.exportExcel(response, list, keys, columnNames,
		// "2016年数据统计");
	}

	/*
	 * 导出Product销售相关信息 销售价格 成本价格 库存数据 已销售量 (支持筛选季节)
	 */
	@RequestMapping(value = "/export/productsale")
	@AdminOperationLog
	@ResponseBody
	public void exportProductSale(HttpServletResponse response) throws IOException {

		Set<Long> seasonIds = new HashSet<>();
		// seasonIds.add(430L);
		// seasonIds.add(431L);
		// seasonIds.add(432L);
		seasonIds.add(433L);

		List<Map<String, Object>> list = statisticsFacade.exportProductSale(seasonIds);

		String columnNames[] = { "ProductId", "款号", "商品名称", "已销售量", "销售价格", "库存数据" };// 列名
		String keys[] = { "ProductId", "ClothesNumber", "Name", "TotalCount", "Cash", "RemainCount" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "WinterProductSale");
	}

	/*
	 * 导出ProductSku自建仓现在上架状态的冬装数据，（款号，品名，颜色，尺码，已销售量，销售价格，库存数量)
	 */
	@RequestMapping(value = "/export/productskusale")
	@AdminOperationLog
	@ResponseBody
	public void exportProductSkuSale(HttpServletResponse response) throws IOException {

		Set<Long> seasonIds = new HashSet<>();
		// seasonIds.add(430L);
		// seasonIds.add(431L);
		// seasonIds.add(432L);
		seasonIds.add(433L); // 冬季

		List<Map<String, Object>> list = statisticsFacade.exportProductSkuSale("14, 17", seasonIds);

		String columnNames[] = { "SkuNo", "款号", "品牌", "商品名称", "颜色", "尺码", "已销售量", "销售价格", "库存数量" };// 列名
		String keys[] = { "SkuNo", "ClothesNumber", "BrandName", "Name", "Color", "Size", "TotalCount", "Cash",
				"RemainCount" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "WinterProductSale");
	}

	/*
	 * 导出门店版商家收益信息数据
	 */
	@RequestMapping(value = "/export/storebusiness/income")
	@AdminOperationLog
	@ResponseBody
	public void exportStoreBusinessIncome(HttpServletResponse response) throws IOException {

		List<Map<String, Object>> list = statisticsFacade.exportStoreBusinessIncome();

		String columnNames[] = { "商家ID", "收益余额", "收益可提现余额", "收益提现中总金额", "注册手机号码", "注册人姓名" };// 列名
		String keys[] = { "businessNumber", "cash_balance", "approve_withdraw", "on_withdraw", "phoneNumber",
				"lagalPerson" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "商家提现余额名单");
	}

	/*
	 * SKU信息 (当前：筛选为已上架 有库存)
	 */
	@RequestMapping(value = "/sku/info")
	@ResponseBody
	public void skuInfo(HttpServletResponse response) throws IOException, ParseException {

		List<Map<String, Object>> list = statisticsFacade.skuInfo();

		String columnNames[] = { "SkuNo", "商品名称", "商品款号", "款号Id", "颜色", "尺码", "库存", "市场价", "品牌" };// 列名
		String keys[] = { "skuNo", "productName", "clothesNum", "productId", "color", "size", "remainCount",
				"marketPrice", "brandName" };// map中的key

		ExcelUtil.exportExcel(response, list, keys, columnNames, "skuInfo");
	}
}
