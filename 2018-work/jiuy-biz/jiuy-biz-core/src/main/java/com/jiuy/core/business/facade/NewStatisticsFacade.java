package com.jiuy.core.business.facade;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.OrderItemDao;
//import com.jiuy.core.dao.OrderLogDao;
import com.jiuy.core.dao.OrderNewLogDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.service.AddressService;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.order.OrderNewLogService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.util.JsonUtil;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.constant.category.CategoryType;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.withdraw.WithDrawApply;
//import com.jiuyuan.entity.Address;
import com.jiuyuan.util.WdtSkuUtil;

@Service
public class NewStatisticsFacade {

	@Autowired
	private UserService userService;

	@Autowired
	private UserManageService userManageService;

	@Autowired
	private OrderNewLogDao orderNewLogDao;

	// @Autowired
	// private OrderLogDao oldOrderLogDao;

	@Autowired
	private OrderNewLogService orderLogService;

	@Autowired
	private OrderNewLogService oldOrderLogService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductSKUService productSKUService;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private AddressService addressService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OrderOldService orderNewService;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private ProductSKUMapper productSKUMapper;

	@Autowired
	private StoreBusinessDao storeBusinessDao;

	public Map<String, Object> getData(long startTime, long endTime) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 总注册量
		data.put("total", userService.registerCount(startTime, endTime));
		// 手机注册量
		data.put("phone", userService.getPhoneRegisterNumByTime(startTime, endTime));
		// 微信注册量
		data.put("weixin", userService.getWeixinUserNumByTime(startTime, endTime));
		// 近段时间新用户的购买数
		data.put("newUserOrder", orderNewLogDao.getNewUserBuyCountByTime(startTime, endTime));
		// 近几天销售量
		data.put("totalOrder", orderNewLogDao.getSaleNumByTime(startTime, endTime));
		// 近几天卖出去的商品件数
		data.put("totalProduct", orderNewLogDao.getSaleProductCountByTime(startTime, endTime));

		return data;
	}

	public List<Map<String, Object>> hotSaleOfCategory(long startTime, long endTime, int count) {
		List<Category> categories = categoryService.getTopCategory(0);
		List<Map<String, Object>> hotSaleMaps = new ArrayList<Map<String, Object>>();

		for (Category category : categories) {
			List<Map<String, Object>> maps = orderLogService.hotSaleCategory(startTime, endTime, count,
					category.getId());
			for (Map<String, Object> map : maps) {
				long productId = (Long) map.get("ProductId");
				int remainCount = productSKUService.getRemainCount(productId);

				map.put("categoryName", category.getCategoryName());
				map.put("categoryId", category.getId());
				map.put("startTime", DateUtil.convertMSEL(startTime));
				map.put("endTime", DateUtil.convertMSEL(endTime));
				map.put("remainCount", remainCount);
			}
			hotSaleMaps.addAll(maps);
		}

		return hotSaleMaps;
	}

	public List<Map<String, Object>> hotSaleOfActivity(long startTime, long endTime) {
		Set<Long> skuIds = new HashSet<Long>();

		Set<Long> propertyNameIds = new HashSet<Long>();
		propertyNameIds.add(7L);
		propertyNameIds.add(8L);

		Map<Long, ProductPropValue> propertyValueMap = propertyService.getValueMap(propertyNameIds);

		List<Map<String, Object>> hotsales = orderLogService.hotSaleOfActivity(startTime, endTime);
		for (Map<String, Object> map : hotsales) {
			skuIds.add((Long) map.get("SkuId"));
		}

		Map<Long, Map<String, Object>> hotsaleMap = productSKUService.getHotSaleMapOfIds(skuIds);
		int i = 0;
		for (Map<String, Object> map : hotsales) {
			long skuId = (Long) map.get("SkuId");
			Map<String, Object> skuMap = hotsaleMap.get(skuId);
			if (skuMap == null) {
				map.put("rank", ++i);
				map.put("skuNo", "");
				map.put("name", "");
				map.put("clothesNum", "");
				map.put("startTime", DateUtil.convertMSEL(startTime));
				map.put("endTime", DateUtil.convertMSEL(endTime));
				map.put("totalMoney", 0);
				map.put("color", "");
				map.put("size", "");
			} else {
				String skuNo = skuMap.get("SkuNo").toString();
				String name = (String) skuMap.get("Name");
				String clothesNumber = (String) skuMap.get("ClothesNumber");
				String propertyIds = (String) skuMap.get("PropertyIds");
				BigDecimal cash = (BigDecimal) (skuMap.get("Cash"));
				BigDecimal count = (BigDecimal) map.get("TotalCount");
				map.put("totalMoney", cash.multiply(count));
				map.put("rank", ++i);
				map.put("skuNo", skuNo);
				map.put("name", name);
				map.put("clothesNum", clothesNumber);
				map.put("startTime", DateUtil.convertMSEL(startTime));
				map.put("endTime", DateUtil.convertMSEL(endTime));

				String[] propPairs = StringUtils.split(propertyIds, ";");
				String colorStr = propPairs[0].split(":")[1];
				String sizeStr = propPairs[1].split(":")[1];

				long colorId = Long.parseLong(colorStr);
				long sizeId = Long.parseLong(sizeStr);

				map.put("color", propertyValueMap.get(colorId).getPropertyValue());
				map.put("size", propertyValueMap.get(sizeId).getPropertyValue());
			}

		}

		return hotsales;
	}

	public List<Map<String, Object>> basicDataPerDay(long startTime, long endTime) {
		DateTime begin = new DateTime(startTime);
		DateTime end = new DateTime(endTime);
		Period p = new Period(begin, end, PeriodType.days());
		int days = p.getDays();

		Map<String, Object> registerMap = userManageService.registerPerDay(startTime, endTime);
		Map<String, Object> orderCountMap = orderLogService.saleOrderCountPerDay(startTime, endTime);
		Map<String, Object> productCountMap = orderLogService.saleProductCountPerDay(startTime, endTime);
		Map<String, Object> orderMoneyMap = orderLogService.saleOrderMoneytPerDay(startTime, endTime);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < days; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String dayTime = begin.plusDays(i).toString("yyyy-MM-dd");

			map.put("day", dayTime);
			map.put("register", registerMap.get(dayTime));
			map.put("order_count", orderCountMap.get(dayTime));
			map.put("product_count", productCountMap.get(dayTime));
			map.put("order_money", orderMoneyMap.get(dayTime));
			list.add(map);
		}

		return list;
	}

	public List<Map<String, Object>> loginPerDay(long startTime, long endTime) {
		return userManageService.loginPerDay(startTime, endTime);
	}

	public List<Map<String, Object>> notExistInLocal() {
		Set<String> erpClothes = (Set<String>) synchronizationCount();
		List<Product> localProducts = productMapper.getByWarehouse(AdminConstants.ERP_WAREHOUSE_ID_LIST);

		Set<String> localClothes = new HashSet<String>();

		for (Product product : localProducts) {
			String clothesNum = product.getClothesNumber();
			if (clothesNum == null)
				continue;
			String patternString = "^[A-Z|a-z]+";
			String templateString = clothesNum;
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(templateString);
			String resultString1 = matcher.replaceFirst("");

			localClothes.add(resultString1);
		}

		List<String> erpList = new ArrayList<>();
		for (String s : erpClothes) {
			if (s == null)
				continue;
			String patternString = "^[A-Z|a-z]+";
			String templateString = s;
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(templateString);
			String resultString1 = matcher.replaceFirst("");

			erpList.add(resultString1);
		}

		erpList.removeAll(localClothes);

		System.out.println(erpList.size());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String erp : erpList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("clothesNo", erp);
			list.add(map);
		}

		return list;
	}

	public Collection<String> synchronizationCount() {
		int pageSize = AdminConstants.WDT_STOCK_QUERY_PAGE_SIZE;

		Map<String, String> map = new HashMap<String, String>();

		map.put("page_no", "0");
		map.put("page_size", pageSize + "");

		String result = WdtSkuUtil.send(AdminConstants.WDT_STOCK_QUERY_URL, map);
		Object object = JsonUtil.getValue(result, "total_count");
		String totalCountStr = (String) object;
		int totalCount = Integer.parseInt(totalCountStr);
		int pageNo = totalCount / pageSize;
		pageNo = totalCount % pageSize == 0 ? pageNo : pageNo + 1;

		Set<String> clothesNos = new HashSet<String>();
		for (int i = 0; i < pageNo; i++) {
			map.put("page_no", i + "");
			String skuInfo = WdtSkuUtil.send(AdminConstants.WDT_STOCK_QUERY_URL, map);

			JSONArray jsonArray = (JSONArray) JsonUtil.getValue(skuInfo, "stocks");
			Iterator<Object> iterator = jsonArray.iterator();

			String lastSpecNo = "";
			int total = 0;
			while (iterator.hasNext()) {
				JSONObject jsonObject = (JSONObject) iterator.next();
				// 货品编号-款号
				String spec_no = (String) jsonObject.get("goods_no");
				// 库存量
				int stock_num = (int) Double.parseDouble((String) jsonObject.get("stock_num"));

				if (lastSpecNo.equals(spec_no)) {
					total += stock_num;
				} else {
					if (total > 0) {
						clothesNos.add(lastSpecNo);
					}
					total = stock_num;
					lastSpecNo = spec_no;
				}

			}
		}
		return clothesNos;
	}

	public List<Map<String, Object>> rankSale(long startTime, long endTime) {
		List<Category> topCategories = categoryService.search("上装", CategoryType.NORMAL.getIntValue(), 0, 0);
		List<Category> pantsCategories = categoryService.search("裤装", CategoryType.NORMAL.getIntValue(), 0, 0);
		List<Category> skirtCategories = categoryService.search("裙装", CategoryType.NORMAL.getIntValue(), 0, 0);
		List<Category> dressCategories = categoryService.search("连衣裙", CategoryType.NORMAL.getIntValue(), 0, 0);
		long topId = topCategories.get(0).getId();
		long pantsId = pantsCategories.get(0).getId();
		long skirtId = skirtCategories.get(0).getId();
		long dressId = dressCategories.get(0).getId();

		List<Map<String, Object>> productCountList = orderNewLogDao.saleCountPerProduct(startTime, endTime);
		List<Map<String, Object>> brandCountList = orderNewLogDao.saleCountPerBrand(startTime, endTime);

		List<Map<String, Object>> categoryCountList = orderNewLogDao.saleCountPerCategory(startTime, endTime);
		List<Map<String, Object>> topCatMapList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> pantsCatMapList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> skirtCatMapList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dressCatMapList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : categoryCountList) {
			long parentId = (long) map.get("ParentId");
			if (parentId == topId) {
				topCatMapList.add(map);
			} else if (parentId == pantsId) {
				pantsCatMapList.add(map);
			} else if (parentId == skirtId) {
				skirtCatMapList.add(map);
			} else if (parentId == dressId) {
				dressCatMapList.add(map);
			}
			System.out.println(map.get("CategoryName") + " " + map.get("count2"));
		}

		for (int i = 0; i < productCountList.size(); i++) {
			Map<String, Object> map = productCountList.get(i);

			map.put("rank", i + 1);
			map.put("product", map.get("Name"));
			map.put("product_count", map.get("count"));

			if (i < brandCountList.size()) {
				map.put("brand", brandCountList.get(i).get("BrandName"));
				map.put("brand_count", brandCountList.get(i).get("count"));
			}
			if (i < categoryCountList.size()) {
				map.put("category", categoryCountList.get(i).get("CategoryName"));
				map.put("category_count", categoryCountList.get(i).get("count2"));
			}
			if (i < topCatMapList.size()) {
				map.put("top_category", topCatMapList.get(i).get("CategoryName"));
				map.put("top_category_count", topCatMapList.get(i).get("count2"));
			}
			if (i < pantsCatMapList.size()) {
				map.put("pants", pantsCatMapList.get(i).get("CategoryName"));
				map.put("pants_count", pantsCatMapList.get(i).get("count2"));
			}
			if (i < skirtCatMapList.size()) {
				map.put("skirt", skirtCatMapList.get(i).get("CategoryName"));
				map.put("skirt_count", skirtCatMapList.get(i).get("count2"));
			}
			if (i < dressCatMapList.size()) {
				map.put("dress", dressCatMapList.get(i).get("CategoryName"));
				map.put("dress_count", dressCatMapList.get(i).get("count2"));
			}
		}

		return productCountList;
	}

	public List<Map<String, Object>> rankSaleLocation(long startTime, long endTime) {
		List<Map<String, Object>> list = orderNewLogDao.rankSaleLocation(startTime, endTime);
		int i = 0;
		for (Map<String, Object> map : list) {
			i++;
			map.put("rank", i);
			map.put("startTime", DateUtil.convertMSEL(startTime));
			map.put("endTime", DateUtil.convertMSEL(endTime));
			map.put("money", Double.parseDouble(map.get("sumMoney").toString())
					+ Double.parseDouble(map.get("sumExpressMoney").toString()));
		}
		return list;
	}

	public List<Map<String, Object>> rankSaleLocationUser(long startTime, long endTime) {
		List<Map<String, Object>> list = orderNewLogDao.rankSaleLocationUser(startTime, endTime);
		int i = 0;
		for (Map<String, Object> map : list) {
			i++;
			map.put("rank", i);
			map.put("startTime", DateUtil.convertMSEL(startTime));
			map.put("endTime", DateUtil.convertMSEL(endTime));
		}
		return list;
	}

	public List<Map<String, Object>> rankBuyers(long startTime, long endTime) {
		List<Map<String, Object>> list = orderNewLogDao.rankBuyers(startTime, endTime);
		int i = 0;
		Set<Long> userIds = new HashSet<Long>();
		for (Map<String, Object> map : list) {
			i++;
			map.put("rank", i);
			map.put("startTime", DateUtil.convertMSEL(startTime));
			map.put("endTime", DateUtil.convertMSEL(endTime));
			map.put("money", Double.parseDouble(map.get("sumMoney").toString())
					+ Double.parseDouble(map.get("sumExpressMoney").toString()));
			userIds.add(Long.parseLong(map.get("UserId").toString()));
		}

		List<Map<String, Object>> firstRecords = orderNewLogDao.rankBuyersRecordsTime(startTime, endTime, 0);
		List<Map<String, Object>> lastRecords = orderNewLogDao.rankBuyersRecordsTime(startTime, endTime, 1);

		Map<Long, Object> firstRecordMap = new HashMap<Long, Object>();
		for (Map<String, Object> map : firstRecords) {
			firstRecordMap.put(Long.parseLong(map.get("UserId").toString()),
					DateUtil.convertMSEL(Long.parseLong(map.get("CreateTime").toString())));
		}

		Map<Long, Object> lastRecordMap = new HashMap<Long, Object>();
		for (Map<String, Object> map : lastRecords) {
			lastRecordMap.put(Long.parseLong(map.get("UserId").toString()),
					DateUtil.convertMSEL(Long.parseLong(map.get("CreateTime").toString())));
		}

		Map<Long, User> userMap = userManageService.usersMapOfIds(userIds);
		Map<Long, List<Address>> addressMap = addressService.addressMapOfUserIds(userIds);

		for (Map<String, Object> map : list) {
			long userId = Long.parseLong(map.get("UserId").toString());

			map.put("first", firstRecordMap.get(userId));
			map.put("last", lastRecordMap.get(userId));

			User user = userMap.get(userId);
			if (user == null) {
				// System.out.println(userId);
			} else {
				map.put("username", user.getUserName());
				map.put("yjj_number", user.getyJJNumber());
				map.put("bind_phone", user.getBindPhone());
			}

			Address address = addressMap.get(userId).get(0);
			if (address == null) {
				// System.out.println(userId + "...");
			} else {
				map.put("distinct", address.getProvinceName() + address.getCityName());
			}
		}

		return list;
	}

	public List<Map<String, Object>> skuOfWarehouse(List<Long> warehouseIds) {
		List<ProductSKU> skus = productSKUService.skusOfWarehouse2(warehouseIds);
		return assembleSkuInfo(skus);
	}

	public List<Map<String, Object>> skuInfo() {
		List<ProductSKU> skus = productSKUService.skusInfo();
		return assembleSkuInfo(skus);
	}

	public List<Map<String, Object>> skuInfoOfBrand(List<Long> brandIds, boolean checkOnSale) {
		List<ProductSKU> skus = null;
		skus = productSKUService.search(brandIds, checkOnSale);
		return assembleSkuInfo(skus);
	}

	public List<Map<String, Object>> assembleSkuInfo(List<ProductSKU> skus) {

		Set<Long> propertyNameIds = new HashSet<Long>();
		propertyNameIds.add(PropertyName.SIZE.getValue());
		propertyNameIds.add(PropertyName.COLOR.getValue());

		Map<Long, ProductPropValue> propertyValueMap = propertyService.getValueMap(propertyNameIds);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ProductSKU sku : skus) {
			// if (sku.getStatus() < 0) {
			// continue;
			// }
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("skuNo", sku.getSkuNo());
			map.put("productName", sku.getName());
			map.put("clothesNum", sku.getClothesNumber());
			map.put("productId", sku.getProductId());
			map.put("remainCount", sku.getRemainCount());
			map.put("marketPrice", sku.getMarketPrice());
			map.put("brandName", sku.getBrandName());
			map.put("color", propertyValueMap.get(sku.getPropertyMap().get(PropertyName.COLOR.getValue() + ""))
					.getPropertyValue());
			map.put("size", propertyValueMap.get(sku.getPropertyMap().get(PropertyName.SIZE.getValue() + ""))
					.getPropertyValue());

			list.add(map);
		}

		return list;
	}

	public List<Map<String, Object>> uninterruptedSignIn(long startTime, long endTime) {
		DateTime begin = new DateTime(startTime);
		DateTime end = new DateTime(endTime);
		Period p = new Period(begin, end, PeriodType.days());
		int days = p.getDays();

		List<Map<String, Object>> list = userManageService.uninterruptedSignIn(startTime, endTime, days);
		Set<Long> userIds = new HashSet<Long>();
		int i = 0;
		for (Map<String, Object> map : list) {
			i++;
			map.put("no", i);
			map.put("YJJNumber", map.get("YJJNumber"));
			if (Integer.parseInt(map.get("UserType").toString()) == UserType.PHONE.getIntValue()) {
				map.put("phone", map.get("UserRelatedName"));
			} else if (Integer.parseInt(map.get("UserType").toString()) == UserType.WEIXIN.getIntValue()) {
				map.put("weixin", map.get("UserRelatedName"));
			} else if (Integer.parseInt(map.get("UserType").toString()) == UserType.EMAIL.getIntValue()) {
				map.put("email", map.get("UserRelatedName"));
			}
			userIds.add(Long.parseLong(map.get("UserId").toString()));
		}
		Map<Long, List<Address>> addressMap = addressService.addressMapOfUserIds(userIds);
		for (Map<String, Object> map : list) {
			long userId = Long.parseLong(map.get("UserId").toString());
			List<Address> addresses = addressMap.get(userId);
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				map.put("receiver", address.getReceiverName());
				map.put("receiveAddress", address.getAddrFull());
				map.put("receiverPhone", address.getTelephone());
			}
		}

		return list;
	}

	public List<Map<String, Object>> salesAmountOfSKU(long startTime, long endTime) {
		Map<String, Double> allMap = new HashMap<>();
		Map<Long, List<Map<String, Object>>> saleAmountByDay = orderItemService.getPerdaySalesAmount(startTime, endTime,
				allMap);

		List<Map<String, Object>> list = new ArrayList<>();
		DateTime begin = new DateTime(startTime);
		int days = DateUtil.getPeriodDays(startTime, endTime);

		Set<Long> skuNos = new HashSet<>();
		for (Map.Entry<Long, List<Map<String, Object>>> entry : saleAmountByDay.entrySet()) {
			skuNos.add(entry.getKey());
		}

		List<ProductSKU> skus = productSKUService.skuOfNo(skuNos);
		List<Map<String, Object>> map = assembleSkuInfo(skus);
		Map<Long, Map<String, Object>> resultMap = asemebleMap(map);

		for (Map.Entry<Long, List<Map<String, Object>>> entry : saleAmountByDay.entrySet()) {
			Map<String, Object> subMap = new HashMap<>();
			Long skuNo = entry.getKey();
			subMap.put("SkuNo", skuNo);

			Map<String, Object> desc_map = resultMap.get(skuNo);
			subMap.put("productName", desc_map.get("productName"));
			subMap.put("productId", desc_map.get("productId"));
			subMap.put("size", desc_map.get("size"));
			subMap.put("color", desc_map.get("color"));

			double total = 0.00;
			int validateAmount = 0;
			for (int i = 0; i < days; i++) {
				String timeStr = begin.plusDays(i).toString("yyyy-MM-dd");

				Double pay = allMap.get(timeStr + skuNo);
				if (pay == null) {
					pay = 0.00;
				} else {
					validateAmount++;
					total += pay;
				}

				subMap.put(timeStr, pay);
			}
			subMap.put("total", String.format("%.2f", total));
			subMap.put("average", String.format("%.2f", total / (validateAmount == 0 ? 1 : validateAmount)));

			list.add(subMap);
		}

		return list;
	}

	public String[] salesAmountOfSKUColumnNames(long startTime, long endTime) {
		List<String> list = new ArrayList<>();
		list.add("SKUID");
		list.add("商品名称");
		list.add("款号ID");
		list.add("尺码");
		list.add("颜色");

		DateTime begin = new DateTime(startTime);
		int days = DateUtil.getPeriodDays(startTime, endTime);
		for (int i = 0; i < days; i++) {
			String timeStr = begin.plusDays(i).toString("yyyy-MM-dd");
			list.add(timeStr);
		}
		list.add("均值（单位元）");
		list.add("合计（单位元）");

		return list.toArray(new String[list.size()]);
	}

	public String[] salesAmountOfSKUColumnKeys(long startTime, long endTime) {
		List<String> list = new ArrayList<>();
		list.add("SkuNo");
		list.add("productName");
		list.add("productId");
		list.add("size");
		list.add("color");

		DateTime begin = new DateTime(startTime);
		int days = DateUtil.getPeriodDays(startTime, endTime);
		for (int i = 0; i < days; i++) {
			String timeStr = begin.plusDays(i).toString("yyyy-MM-dd");
			list.add(timeStr);
		}

		list.add("average");
		list.add("total");

		return list.toArray(new String[list.size()]);
	}

	public List<Map<String, Object>> salesVolumeOfSKU(long startTime, long endTime) {
		Map<String, Integer> allMap = new HashMap<>();
		Map<Long, List<Map<String, Object>>> saleAmountByDay = orderItemService.getPerdaySalesVolume(startTime, endTime,
				allMap);

		List<Map<String, Object>> list = new ArrayList<>();
		DateTime begin = new DateTime(startTime);
		int days = DateUtil.getPeriodDays(startTime, endTime);

		Set<Long> skuNos = new HashSet<>();
		for (Map.Entry<Long, List<Map<String, Object>>> entry : saleAmountByDay.entrySet()) {
			skuNos.add(entry.getKey());
		}

		List<ProductSKU> skus = productSKUService.skuOfNo(skuNos);
		List<Map<String, Object>> map = assembleSkuInfo(skus);
		Map<Long, Map<String, Object>> resultMap = asemebleMap(map);

		for (Map.Entry<Long, List<Map<String, Object>>> entry : saleAmountByDay.entrySet()) {
			Map<String, Object> subMap = new HashMap<>();
			Long skuNo = entry.getKey();
			subMap.put("SkuNo", skuNo);

			Map<String, Object> desc_map = resultMap.get(skuNo);
			subMap.put("productName", desc_map.get("productName"));
			subMap.put("productId", desc_map.get("productId"));
			subMap.put("size", desc_map.get("size"));
			subMap.put("color", desc_map.get("color"));

			Integer total = 0;
			for (int i = 0; i < days; i++) {
				String timeStr = begin.plusDays(i).toString("yyyy-MM-dd");

				Integer volume = allMap.get(timeStr + skuNo);
				if (volume == null) {
					volume = 0;
				}
				total += volume;

				subMap.put(timeStr, volume);
			}
			subMap.put("total", total);
			subMap.put("average", String.format("%.2f", Double.valueOf("" + total) / days));

			list.add(subMap);
		}

		return list;
	}

	private Map<Long, Map<String, Object>> asemebleMap(List<Map<String, Object>> params_map) {
		Map<Long, Map<String, Object>> result = new HashMap<>();
		for (Map<String, Object> map : params_map) {
			result.put(Long.parseLong(map.get("skuNo").toString()), map);
		}
		return result;
	}

	public String[] salesVolumeOfSKUColumnNames(long startTime, long endTime) {
		List<String> list = new ArrayList<>();
		list.add("SKUID");
		list.add("商品名称");
		list.add("款号ID");
		list.add("尺码");
		list.add("颜色");

		DateTime begin = new DateTime(startTime);
		int days = DateUtil.getPeriodDays(startTime, endTime);
		for (int i = 0; i < days; i++) {
			String timeStr = begin.plusDays(i).toString("yyyy-MM-dd");
			list.add(timeStr);
		}
		list.add("均值");
		list.add("合计");

		return list.toArray(new String[list.size()]);
	}

	public String[] salesVolumeOfSKUColumnKeys(long startTime, long endTime) {
		List<String> list = new ArrayList<>();
		list.add("SkuNo");
		list.add("productName");
		list.add("productId");
		list.add("size");
		list.add("color");

		DateTime begin = new DateTime(startTime);
		int days = DateUtil.getPeriodDays(startTime, endTime);
		for (int i = 0; i < days; i++) {
			String timeStr = begin.plusDays(i).toString("yyyy-MM-dd");
			list.add(timeStr);
		}

		list.add("average");
		list.add("total");

		return list.toArray(new String[list.size()]);
	}

	public List<Map<String, Object>> rankProductHotsale(long startTime, long endTime, Collection<Long> seasonIds) {
		List<Map<String, Object>> products_hot_sale = orderLogService.rankProductHotsale(startTime, endTime, seasonIds);
		Set<Long> productIds = assembleProductIds(products_hot_sale);
		Map<Long, Product> productOfId = productService.productMapOfIds(productIds);

		int i = 0;
		for (Map<String, Object> map : products_hot_sale) {
			Product product = productOfId.get(Long.parseLong(map.get("ProductId").toString()));
			map.put("rank", ++i);
			map.put("name", product.getName());
			map.put("clothesNum", product.getClothesNumber());
			map.put("startTime", DateUtil.convertMSEL(startTime));
			map.put("endTime", DateUtil.convertMSEL(endTime));
		}
		return products_hot_sale;
	}

	private Set<Long> assembleProductIds(List<Map<String, Object>> products_hot_sale) {
		Set<Long> productIds = new HashSet<>();
		for (Map<String, Object> map : products_hot_sale) {
			productIds.add(Long.parseLong(map.get("ProductId").toString()));
		}
		return productIds;
	}

	public List<Map<String, Object>> perMonthRegister(long startTime, long endTime) {
		List<Map<String, Object>> resultList = userDao.perMonthRegister(startTime, endTime);
		int last_count = 0;
		for (Map<String, Object> map : resultList) {
			Integer count = Integer.parseInt(map.get("count").toString());
			map.put("addCount", count - last_count);
			last_count = count;
		}
		return resultList;
	}

	public List<Map<String, Object>> perMonthSale(long startTime, long endTime) {
		List<Map<String, Object>> monthSales = orderItemDao.getMonthSales(startTime, endTime);

		int totalSaleCount = 0;
		double totalSaleCash = 0.0;
		for (Map<String, Object> map : monthSales) {
			Double total = Double.parseDouble(map.get("total").toString());
			Integer buyCount = Integer.parseInt(map.get("buyCount").toString());

			totalSaleCount += buyCount;
			map.put("totalSaleCount", totalSaleCount);
			totalSaleCash += total;
			map.put("totalSaleCash", totalSaleCash);
		}

		return monthSales;
	}

	public List<Map<String, Object>> basicAllDataPerDay(long startTime, long endTime) {
		DateTime begin = new DateTime(startTime);
		DateTime end = new DateTime(endTime);
		Period p = new Period(begin, end, PeriodType.days());
		int days = p.getDays();

		Map<String, Object> allRegisterMap = userManageService.registerForTypePerDay(startTime, endTime, -1);
		Map<String, Object> phoneRegisterMap = userManageService.registerForTypePerDay(startTime, endTime, 1);
		Map<String, Object> wxRegisterMap = userManageService.registerForTypePerDay(startTime, endTime, 2);
		Map<String, Object> otherRegisterMap = userManageService.registerForTypePerDay(startTime, endTime, 0);
		// Map<String, Object> orderCountMap =
		// orderLogService.saleOrderCountPerDay(startTime, endTime);
		// Map<String, Object> orderCountMap =
		// oldOrderLogService.saleOrderCountPerDay(startTime, endTime);
		// Map<String, Object> productCountMap =
		// orderLogService.saleProductCountPerDay(startTime, endTime);
		// Map<String, Object> productCountMap =
		// oldOrderLogService.saleProductCountPerDay(startTime, endTime);
		Map<String, Object> loginDay = userManageService.loginDay(startTime, endTime);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < days; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String dayTime = begin.plusDays(i).toString("yyyy-MM-dd");
			long newUserStartTime = 0;
			long newUserEndTime = 0;
			try {
				newUserStartTime = DateUtil.convertToMSEL(dayTime + " 00:00:00");
				newUserEndTime = DateUtil.convertToMSEL(dayTime + " 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// int newUserOrderCount =
			// orderLogService.getNewUserOrderCountPerDay(newUserStartTime,newUserEndTime);
			int newUserProductCount = orderLogService.getNewUserProductCountPerDay(newUserStartTime, newUserEndTime);
			// 旧表删除orderLogDao换成了orderNewLogDao
			int newUserOrderCount = orderNewLogDao.getNewUserBuyCountByTime(newUserStartTime, newUserEndTime);
			map.put("day", dayTime);
			map.put("allRegister", allRegisterMap.get(dayTime));
			map.put("phoneRegister", phoneRegisterMap.get(dayTime));
			map.put("wxRegister", wxRegisterMap.get(dayTime));
			map.put("otherRegister", otherRegisterMap.get(dayTime));
			map.put("user_order_count", newUserOrderCount);
			map.put("user_product_count", newUserProductCount);
			map.put("order_count", orderNewLogDao.getSaleNumByTime(newUserStartTime, newUserEndTime));
			map.put("product_count", orderNewLogDao.getSaleProductCountByTime(newUserStartTime, newUserEndTime));
			map.put("login_day", loginDay.get(dayTime));
			list.add(map);
		}
		return list;
	}

	public List<Map<String, Object>> getOrderDatas(long startTime, long endTime) {
		List<OrderNew> orderList = orderNewService.getOrderNewOfTime(startTime, endTime);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (OrderNew orderNew : orderList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderNo", orderNew.getOrderNo());
			map.put("day", orderNew.getDay());
			map.put("address", orderNew.getProvinceName());
			map.put("order_money", orderNew.getTotalPay() + orderNew.getTotalExpressMoney());
			map.put("count", orderNew.getBuyCounts());
			map.put("yjjNumber", "" + orderNew.getYjjNumber());
			list.add(map);
		}
		return list;
	}

	public List<Map<String, Object>> getSkuSalePerDayPerDay(long startTime, long endTime, List<String> columnNames) {
		List<Map<String, Map<Long, Integer>>> results = new ArrayList<>();

		List<Long> skuNos = productSKUMapper.getSaleSkuNos(startTime, endTime);

		List<Map<String, Map<Long, Integer>>> list = null;

		DateTime begin = new DateTime(startTime);
		for (Map<String, Map<Long, Integer>> map : list) {
			Map<Long, Integer> sub_result = new HashMap<>();
			Map<Long, Integer> skuMap = map.get(begin.toString("yyyy-MM-dd"));
			Map<String, Map<Long, Integer>> result = new HashMap<>();
			result.put(begin.toString("yyyy-MM-dd"), sub_result);
			for (Entry<Long, Integer> entry : skuMap.entrySet()) {
				Long key = entry.getKey();
				Integer value = entry.getValue();
				sub_result.put(key, value);
			}

			begin = begin.plusDays(1);
		}
		return null;
	}

	public List<Map<String, Object>> exportProductSale(Collection<Long> seasonIds) {
		Map<Long, Map<String, Object>> productsSaleMap = orderLogService.productSale(seasonIds);
		List<Map<String, Object>> productMaps = productService.productMap(seasonIds);

		for (Map<String, Object> map : productMaps) {
			Map<String, Object> productSaleMap = productsSaleMap.get(Long.parseLong(map.get("ProductId").toString()));

			if (productSaleMap != null)
				map.put("TotalCount", productSaleMap.get("TotalCount"));
		}
		return productMaps;
	}

	public List<Map<String, Object>> exportProductSkuSale(String warehouseIds, Collection<Long> seasonIds) {
		Set<Long> propertyNameIds = new HashSet<Long>();
		propertyNameIds.add(7L);
		propertyNameIds.add(8L);

		Map<Long, ProductPropValue> propertyValueMap = propertyService.getValueMap(propertyNameIds);

		Map<Long, Map<String, Object>> productsSaleMap = orderLogService.productSkuSale(seasonIds);
		List<Map<String, Object>> productSkuMaps = productSKUService.exportskudata(warehouseIds, seasonIds);

		for (Map<String, Object> skuMap : productSkuMaps) {
			Map<String, Object> productSaleMap = productsSaleMap.get(Long.parseLong(skuMap.get("Id").toString()));

			if (productSaleMap != null)
				skuMap.put("TotalCount", productSaleMap.get("TotalCount"));

			String propertyIds = (String) skuMap.get("PropertyIds");

			String[] propPairs = StringUtils.split(propertyIds, ";");
			String colorStr = propPairs[0].split(":")[1];
			String sizeStr = propPairs[1].split(":")[1];

			long colorId = Long.parseLong(colorStr);
			long sizeId = Long.parseLong(sizeStr);

			ProductPropValue colorP = propertyValueMap.get(colorId);
			ProductPropValue sizeP = propertyValueMap.get(sizeId);
			if (colorP != null) {
				skuMap.put("Color", colorP.getPropertyValue());
			} else {
				System.out.println("propertyIds colorId:" + colorId);
			}

			if (sizeP != null) {
				skuMap.put("Size", sizeP.getPropertyValue());
			} else {
				System.out.println("propertyIds sizeId:" + sizeId);
			}
		}

		return productSkuMaps;
	}

	public List<Map<String, Object>> exportStoreBusinessIncome() {
		List<Map<String, Object>> list = new ArrayList<>();
		List<StoreBusiness> storeBusinesses = storeBusinessDao.getStoreIncomeIds();
		if (storeBusinesses != null && storeBusinesses.size() > 0) {
			for (StoreBusiness storeBusiness : storeBusinesses) {
				Map<String, Object> map = new HashMap<>();
				map.put("businessNumber", storeBusiness.getBusinessNumber());
				map.put("cash_balance", String.format("%.2f", storeBusiness.getCashIncome()));
				map.put("approve_withdraw", String.format("%.2f", storeBusiness.getAvailableBalance()));
				map.put("on_withdraw",
						String.format("%.2f", sumMoney(storeBusinessDao.getOnProcess(storeBusiness.getId(), 0))));
				map.put("phoneNumber", storeBusiness.getPhoneNumber());
				map.put("lagalPerson", storeBusiness.getLegalPerson());
				list.add(map);
			}
		}
		return list;
	}

	private double sumMoney(List<WithDrawApply> wList) {

		double money = 0.0;
		for (WithDrawApply withdrawApply : wList) {
			money += withdrawApply.getApplyMoney();
		}
		return money;
	}
}