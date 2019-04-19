package com.jiuy.web.delegate;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.QianMiFacade;
import com.jiuy.core.dao.mapper.QMCategoryDao;
import com.jiuy.core.dao.mapper.QMOrderDao;
import com.jiuy.core.dao.mapper.QMOrderItemDao;
import com.jiuy.core.dao.mapper.QMProductSKUDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.ProductCategoryService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.core.service.qianmi.QMExpressInfoService;
import com.jiuy.core.service.qianmi.QMOrderService;
import com.jiuy.core.util.QianMiUtil;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.QMExpress;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.qianmi.QMCategory;
import com.jiuyuan.entity.qianmi.QMExpressInfo;
import com.jiuyuan.entity.qianmi.QMOrder;
import com.jiuyuan.entity.qianmi.QMOrderItem;
import com.jiuyuan.entity.qianmi.QMProductSKU;
import com.jiuyuan.util.Base64Util;
import com.jiuyuan.util.CollectionUtil;
import com.qianmi.open.api.ApiException;
import com.qianmi.open.api.domain.cloudshop.Item;
import com.qianmi.open.api.domain.cloudshop.ItemCat;
import com.qianmi.open.api.domain.cloudshop.SellerCat;
import com.qianmi.open.api.domain.cloudshop.Sku;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class QianMiDelegator {
	
	private static final Logger logger = LoggerFactory.getLogger("QianMiLogger");
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ProductSKUMapper productSKUMapper;
	
	@Autowired
	private BrandLogoServiceImpl brandLogoServiceImpl;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private QMOrderDao qMOrderDao;
	
	@Autowired
	private QMOrderItemDao qMOrderItemDao;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private QMOrderService qMOrderService;
	
	@Autowired
	private QMExpressInfoService qmExpressInfoService;
	
	@Autowired
	private QianMiFacade qianMiFacade;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private QMCategoryDao qmCategoryDao;
	
	@Autowired
	private QMProductSKUDao qMProductSKUDao;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	public void batchAddProduct() throws ApiException {
		List<Product> products = productMapper.getQianmiProduct();
		List<SellerCat> sellerCats = QianMiUtil.getSellercat(getAccessToken());
		Map<String, SellerCat> sellercaToptMap = assembleTopMap(sellerCats);
		for (Product product : products) {
			try {
				addProduct(product.getId(), sellerCats, sellercaToptMap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addProduct(Long id, List<SellerCat> sellerCats, Map<String, SellerCat> sellercaToptMap) throws ApiException, MalformedURLException {
		Product product = productMapper.getProductById(id);
		Map<String, String> productInfoMap = assembleQMProduct(product);
		
		Item item = QianMiUtil.addProduct(getAccessToken(), productInfoMap.get("fields"), productInfoMap.get("title"), productInfoMap.get("outer_id"), productInfoMap.get("unit"), 
				productInfoMap.get("skus_json"), productInfoMap.get("image"), productInfoMap.get("desc"), productInfoMap.get("brand_id"), productInfoMap.get("brand_name"));
		
		if (item == null) {
//			throw new ParameterErrorException("item为空！");
			return;
		}

		//千米生成的sku记录到本地
		recordQMProductSKU(item, id, gatherSkuIds(item));
		
		//更新千米商品的重量、市场价
		updateQMSkuAttr(item, gatherSkuIds(item), product);
		
		//更新千米的展示分类
		if (sellerCats == null || sellercaToptMap == null) {
			sellerCats = QianMiUtil.getSellercat(getAccessToken());
			sellercaToptMap = assembleTopMap(sellerCats);
		}
		updateQMSellercat(item, product, sellerCats, sellercaToptMap);
		
		//更新千米的详情
		updateQMDetail(item, product);
		
		String num_iid = item.getNumIid();
		for (String detailImg : product.getDetailImageArray()) {
			String response = QianMiUtil.uploadProdImg(getAccessToken(), num_iid, transImage(detailImg));
			checkResponse(response, id);
		}
	}
	
	private void updateQMDetail(Item item, Product product) throws ApiException {
		if (item.getSkus().size() <= 0) {
			return;
		}
		String num_iid = item.getSkus().get(0).getNumIid();
		String details = assembleDetails(product);
		QianMiUtil.updateProdDetail(getAccessToken(), num_iid, details);
	}

	private String assembleDetails(Product product) {
		StringBuilder builder = new StringBuilder("");
		builder.append(product.getRemark());
		
		for (String imageUrl : product.getSizeTableImageArray()) {
			builder.append("<p><img src=\"");
			builder.append(imageUrl);
			builder.append("\"/></p>");
		}
		for (String imageUrl : product.getSummaryImageArray()) {
			builder.append("<p><img src=\"");
			builder.append(imageUrl);
			builder.append("\"/></p>");
		}
		return builder.toString();
	}

	private void updateQMSellercat(Item item, Product product, List<SellerCat> sellerCats, Map<String, SellerCat> sellercaToptMap) throws ApiException {
		if (item.getSkus().size() <= 0) {
			return;
		}
		String num_iid = item.getSkus().get(0).getNumIid();
		String seller_cids = assembleSellerCids(product, sellerCats, sellercaToptMap);
		
		if (StringUtils.equals("", seller_cids)) {
			return;
		}
		QianMiUtil.updateProdSellerCat(getAccessToken(), num_iid, seller_cids);
	}

	private String assembleSellerCids(Product product, List<SellerCat> sellerCats,
			Map<String, SellerCat> sellercaToptMap) {
		String cid = getTopSellercatCid(product, sellercaToptMap);
		List<Category> categories = getCategoriesOfProduct(product.getId());
		
		StringBuilder builder = new StringBuilder("");
		for (Category category : categories) {
			if (category.getParentId() == 0) {
				continue;
			}
			
			String name = category.getCategoryName();
			for (SellerCat sellerCat : sellerCats) {
				if (sellerCat.getpSellerCid().contains(cid) && StringUtils.equals(sellerCat.getName(), name)) {
					builder.append(sellerCat.getSellerCid() + ",");
				}
			}
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

	private List<Category> getCategoriesOfProduct(long productId) {
		List<ProductCategory> productCategories = productCategoryService.search(productId);
		List<Long> ids = gatherCategoryIds(productCategories);
		List<Category> categories = categoryService.search(0, ids);
		
		return categories;
	}

	private String getTopSellercatCid(Product product, Map<String, SellerCat> sellercaToptMap) {
		Long brandId = product.getBrandId();
		BrandLogo brandLogo = brandLogoServiceImpl.getByBrandId(brandId);
		String brandName = brandLogo.getCnName();
		SellerCat topSellerCat = sellercaToptMap.get(brandName);
		String cid = topSellerCat.getSellerCid();
		
		return cid;
	}

	private List<Long> gatherCategoryIds(List<ProductCategory> productCategories) {
		List<Long> categoryIds = new ArrayList<>();
		for (ProductCategory productCategory : productCategories) {
			categoryIds.add(productCategory.getCategoryId());
		}
		return categoryIds;
	}

	private Map<String, SellerCat> assembleTopMap(List<SellerCat> sellerCats) {
		Map<String, SellerCat> map = new HashMap<>();
		for (SellerCat sellerCat : sellerCats) {
			if (StringUtils.equals(sellerCat.getpSellerCid(), "0")) {
				map.put(sellerCat.getName(), sellerCat);
			}
		}
		return map;
	}

	private Set<Long> gatherSkuIds(Item item) {
		Set<Long> skuIds = new HashSet<>();
		for (Sku sku : item.getSkus()) {
			skuIds.add(Long.parseLong(sku.getOuterId()));
		}
		return skuIds;
	}

	private void updateQMSkuAttr(Item item, Set<Long> skuIds, Product product) throws ApiException {
		Map<Long, ProductSKU> skuMap = productSKUService.skuMapOfIds(skuIds);
		for (Sku sku : item.getSkus()) {
			String sku_id = sku.getSkuId();
			String num_iid = sku.getNumIid();
			ProductSKU productSKU = skuMap.get(Long.parseLong(sku.getOuterId()));
			QianMiUtil.updateSku(getAccessToken(), sku_id, num_iid, productSKU.getWeight(), product.getCash());
		}
	}

	private List<QMProductSKU> recordQMProductSKU(Item item, Long productId, Set<Long> skuIds) {
		Map<Long, ProductSKU> skuMap = productSKUService.skuMapOfIds(skuIds);
		
		List<QMProductSKU> qmProductSKUs = new ArrayList<>();
		long time = System.currentTimeMillis();
		for (Sku sku : item.getSkus()) {
			ProductSKU productSKU = skuMap.get(Long.parseLong(sku.getOuterId()));
			
			QMProductSKU qmProductSKU = new QMProductSKU();
			qmProductSKU.setCostPrice(Double.parseDouble(sku.getCostPrice()));
			qmProductSKU.setCreateTime(time);
			qmProductSKU.setMarketPrice(productSKU.getMarketPrice());
			qmProductSKU.setNumIid(sku.getNumIid());
			qmProductSKU.setPrice(Double.parseDouble(sku.getPrice()));
			qmProductSKU.setProductId(productId);
			qmProductSKU.setqMSkuId(sku.getSkuId());
			qmProductSKU.setSkuId(Long.parseLong(sku.getOuterId()));
			qmProductSKU.setUpdateTime(time);
			qmProductSKU.setWeight(productSKU.getWeight());
			qmProductSKU.setClothesNumber(item.getOuterId());
			
			qmProductSKUs.add(qmProductSKU);
		}
		qMProductSKUDao.batchAdd(qmProductSKUs);
		
		return qmProductSKUs;
	}

	private Map<String, String> assembleQMProduct(Product product) throws MalformedURLException {
		Map<String, String> resultMap = new HashMap<>();
		
		List<ProductSKU> skus = productSKUMapper.search(product.getId(), true);
		if (skus.size() < 1) {
			logger.error("com.jiuy.web.delegate.QianMiDelegator ERROR:id为\"" + product.getId() + "\"的商品无sku！");
//			throw new ParameterErrorException("该商品无sku！");
		}
		resultMap.put("fields", "num_iid,title,price,skus,outer_id");
		resultMap.put("title", product.getName());
		resultMap.put("outer_id", product.getClothesNumber());
		resultMap.put("unit", "件");
		resultMap.put("image", product.getFirstImage() == null ? "" : transImage(product.getFirstImage()));
		resultMap.put("desc", product.getRemark());
		resultMap.put("brand_id", "");
		resultMap.put("brand_name", getBrandName(product.getBrandId()));
		resultMap.put("skus_json", assembleSkusJson(skus, product));
		
		return resultMap;
	}

	private void checkResponse(String result, Long id) {
		if (result.contains("error_response")) {
//			throw new ParameterErrorException(result);
		}
	}
	
	private String transImage(String imageUrl) throws MalformedURLException {
		URL url = new URL(imageUrl);
		return "png@" + Base64Util.encodeImgageToBase64(url);
	}
	
	private String assembleSkusJson(List<ProductSKU> skus, Product product) {
		JSONArray jsonArray = new JSONArray();
		for (ProductSKU productSKU : skus) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("outer_id", productSKU.getId());
			jsonObject.put("price", (int) (product.getCash() * 0.7));
			jsonObject.put("quantity", productSKU.getRemainCount());
			jsonObject.put("sku_props_values", assembleProps(productSKU.getPropertyNameMap()));
//			jsonObject.put("cost_price", product.getWholeSaleCash());
			
			jsonArray.add(jsonObject);
		}
		return jsonArray.toJSONString();
	}

	private JSONObject assembleProps(Map<String, Long> map) {
		Map<Long, ProductPropValue> propValueMap = propertyService.getPropertyValues();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("尺码", propValueMap.get(map.get("size")).getPropertyValue());
		jsonObject.put("颜色分类", propValueMap.get(map.get("color")).getPropertyValue());
		return jsonObject;
	}

	private String getBrandName(long brandId) {
		Map<Long, BrandLogo> brandMap = brandLogoServiceImpl.getBrandMap();
		BrandLogo brandLogo = brandMap.get(brandId);
		if (brandLogo != null) {
			return brandLogo.getCnName();
		}
		return "";
	}
	
	private String getAccessToken() {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.QIANMI_TOKEN);
		return jsonObject.getString("accessToken");
	}

	
	public void pullOrders(String startTime, String endTime) throws ApiException, ParseException {
		String tradesJson = null;
		tradesJson = QianMiUtil.getOrders(getAccessToken(), startTime, endTime, 0, 100);
		JSONObject jsonObject = JSON.parseObject(tradesJson);
		JSONObject response = jsonObject.getJSONObject("d2p_trades_sold_get_response");
		
		recordQMOrdes(tradesJson);

		Integer pageCount = response.getInteger("total_results") / 100;
		if (response.getInteger("total_results") % 100 > 0) {
			pageCount ++;
		}
		
		for (int page_no = 1; page_no < pageCount; page_no++) {
			tradesJson = QianMiUtil.getOrders(getAccessToken(), "2016-08-12 12:20:20", "2016-12-12 12:20:20", page_no, 100);
			recordQMOrdes(tradesJson);
		}
	}

	private void recordQMOrdes(String tradesJson) throws ParseException {
		JSONObject jsonObject = JSON.parseObject(tradesJson);
		JSONObject response = jsonObject.getJSONObject("d2p_trades_sold_get_response");
		JSONObject trades = response.getJSONObject("trades");
		JSONArray trade = trades.getJSONArray("trade");
		for (Object object : trade) {
			JSONObject trade_item = (JSONObject)object;
			String tid = trade_item.getString("tid");
			if (!StringUtils.equals(tid, "T20161113201810834")) {
				continue;
			}
			List<QMOrderItem> qmOrderItems = assembleQMOrderItems(trade_item, tid);
			
			if (qmOrderItems.size() > 0) {
				QMOrder qmOrder = assembleQMOrder(trade_item);
				setPayment(qmOrder, qmOrderItems);
				qMOrderDao.add(qmOrder);
				qMOrderItemDao.batchAdd(qmOrderItems);
			}
		}
	}

	private void setPayment(QMOrder qmOrder, List<QMOrderItem> qmOrderItems) {
		double totalMoney = 0.0;
		double totalPay = 0.0;
		for (QMOrderItem qmOrderItem : qmOrderItems) {
			totalMoney += qmOrderItem.getTotalMoney();
			totalPay += qmOrderItem.getTotalPay();
		}
		qmOrder.setTotalMoney(totalMoney);
		qmOrder.setTotalPay(totalPay);
	}

	private List<QMOrderItem> assembleQMOrderItems(JSONObject trade_item, String tid) {
		List<QMOrderItem> qmOrderItems = new ArrayList<>();
		JSONObject orders = trade_item.getJSONObject("orders");
		JSONArray order = orders.getJSONArray("order");
		for (Object object : order) {
			JSONObject order_item = (JSONObject) object;
			Long out_sku_id = order_item.getLong("outer_sku_id");
			if (isBaiShiHuiTong(out_sku_id)) {
				QMOrderItem qmOrderItem = assembleQMOrderItem(order_item, tid);
				qmOrderItems.add(qmOrderItem);
			}
		}
		return qmOrderItems;
	}

	private QMOrderItem assembleQMOrderItem(JSONObject order_item, String tid) {
		QMOrderItem qmOrderItem = new QMOrderItem();
		long time = System.currentTimeMillis();
		qmOrderItem.setBuyCount(order_item.getInteger("num"));
		qmOrderItem.setBuyerNick(order_item.getString("buyer_nick"));
		qmOrderItem.setCreateTime(time);
		qmOrderItem.setNumIid(order_item.getString("num_iid"));
		qmOrderItem.setOid(order_item.getString("oid"));
		qmOrderItem.setTid(tid);
		qmOrderItem.setqMSkuId(order_item.getString("sku_id"));
		qmOrderItem.setSkuId(order_item.getLong("outer_sku_id"));
		qmOrderItem.setStatus(0);
		qmOrderItem.setTotalMoney(order_item.getDouble("total_fee"));
		qmOrderItem.setTotalPay(order_item.getDouble("payment"));
		qmOrderItem.setUpdateTime(time);
		return qmOrderItem;
	}

	private boolean isBaiShiHuiTong(Long skuId) {
		ProductSKU productSKU = productSKUService.searchById(skuId);
		if (AdminConstants.ERP_WAREHOUSE_ID_LIST.contains(productSKU.getLOWarehouseId())) {
			return true;
		}
		return false;
	}

	private QMOrder assembleQMOrder(JSONObject trade_item) throws ParseException {
		QMOrder qmOrder = new QMOrder();
		long time = System.currentTimeMillis();
		qmOrder.setBuyerNick(trade_item.getString("buyer_nick"));
		qmOrder.setExpressInfo(assembleExpressInfo(trade_item));
		qmOrder.setlOWarehouseId(0L);
		qmOrder.setMergedId(0L);
		qmOrder.setMobile(trade_item.getString("reciver_mobile"));
		qmOrder.setOrderStatus(10);
		qmOrder.setPhone(trade_item.getString("reciver_phone"));
		qmOrder.setTid(trade_item.getString("tid"));
		qmOrder.setqMCreateTime(DateUtil.convertToMSEL(trade_item.getString("created")));
		qmOrder.setqMUpdateTime(DateUtil.convertToMSEL(trade_item.getString("modified")));
		qmOrder.setCreateTime(time);
		qmOrder.setUpdateTime(time);
		return qmOrder;
	}

	private String assembleExpressInfo(JSONObject trade_item) {
		return trade_item.getString("reciver_state") + "-" + trade_item.getString("reciver_city") + "-" 
				+ trade_item.getString("reciver_district") + "-" + trade_item.getString("reciver_address") + "," 
				+ trade_item.getString("reciver_name") + "," + trade_item.getString("reciver_mobile");
	}

	public void syncLogisticsToQianMi(Set<Long> orderNos) throws ApiException {
		List<Map<String, String>> sendInfos = assembleSendInfos(orderNos);
		for (Map<String, String> map : sendInfos) {
			QianMiUtil.send(getAccessToken(), map.get("tid"), map.get("packItems"), map.get("companyId"), map.get("outSid"), map.get("postFee"), 
					map.get("shipTypeId"), map.get("companyName"), map.get("deliverTime"));
		}
	}

	private List<Map<String, String>> assembleSendInfos(Set<Long> orderNos) {
		List<Map<String, String>> sendInfos = new ArrayList<>();
		
		Map<Long, QMOrder> qmMap = qMOrderService.qMOrderOfNos(orderNos);
		Map<Long, QMExpressInfo> qmExpressInfoMap = qmExpressInfoService.expressInfoOfNos(orderNos);
		
		for (QMOrder qmOrder : qmMap.values()) {
			QMExpressInfo qmExpressInfo = qmExpressInfoMap.get(qmOrder.getOrderNo());
			
			if (qmOrder.getMergedId() == -1) {
				List<QMOrder> qmOrders = qMOrderDao.search(CollectionUtil.createList(qmOrder.getOrderNo()), null);
				for (QMOrder sub_qmOrder : qmOrders) {
					sendInfos.add(assembleSendInfo(sub_qmOrder, qmExpressInfo));
				}
			} else {
				sendInfos.add(assembleSendInfo(qmOrder, qmExpressInfo));
			}
		}
		return sendInfos;
	}

	private Map<String, String> assembleSendInfo(QMOrder qmOrder, QMExpressInfo qmExpressInfo) {
		QMExpress qmExpress = QMExpress.getByLocalName(qmExpressInfo.getExpressSupplier());
		Map<String, String> sendInfoMap = new HashMap<>();
		sendInfoMap.put("tid", qmOrder.getTid());
		sendInfoMap.put("packItems", packItemd(qmOrder.getTid()));
		sendInfoMap.put("companyId", qmExpress.getId());
		sendInfoMap.put("companyName", qmExpress.getQmName());
		sendInfoMap.put("outSid", qmExpressInfo.getExpressNo());
		sendInfoMap.put("postFee", "0.00");
		sendInfoMap.put("shipTypeId", "logistics");
		sendInfoMap.put("deliverTime", DateUtil.convertMSEL(System.currentTimeMillis() + DateUtils.MILLIS_PER_HOUR));
		return sendInfoMap;
	}

	//oid:num;oid:num 例子： O20151022161017028:2;O20151103131017867:4
	private String packItemd(String tid) {
		List<QMOrderItem> qmOrderItems = qMOrderItemDao.search(tid);
		StringBuilder builder = new StringBuilder();
		for (QMOrderItem qmOrderItem : qmOrderItems) {
			builder.append(qmOrderItem.getOid() + ":" + qmOrderItem.getBuyCount() + ";");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}

	public void writeBackLogistics() throws ApiException {
//		物流同步
		String result = qianMiFacade.getLogisticsFromErp();
		if(StringUtils.equals(result, "")) {
			throw new ParameterErrorException("no data!");
		}
		
		Set<Integer> rec_ids = new HashSet<Integer>();
		Set<Long> orderNos = new HashSet<>();
		List<Map<String, Object>> list = qianMiFacade.putInCollection(result, rec_ids, orderNos);

//		物流同步
		qianMiFacade.updateLogisticso(list);
//		回调给ERP
		qianMiFacade.syncLogisticsToERP(rec_ids);
//		回调给千米
		syncLogisticsToQianMi(orderNos);
	}

	@Transactional(rollbackFor = Exception.class)
	@Deprecated
	public void addCategories() throws ApiException {
		List<Category> categories = categoryService.search(0, 0L);
		List<Category> parent_categories = new ArrayList<>();
		List<Category> children_categories = new ArrayList<>();
		Set<Long> success_cat_ids = new HashSet<>();
		
		separateCategories(categories, parent_categories, children_categories);
		recordQMCategories(parent_categories, true, success_cat_ids);
		recordQMCategories(children_categories, false, success_cat_ids);
		
		categoryService.batchUpdate(success_cat_ids, System.currentTimeMillis());
	}

	private void recordQMCategories(List<Category> parent_categories, boolean is_parent, Set<Long> success_cat_ids) throws ApiException {
		for (Category category : parent_categories) {
			ItemCat itemCat = null;
			if (is_parent) {
				itemCat = QianMiUtil.addItemCats(getAccessToken(), category.getCategoryName(), "0");
			} else {
				QMCategory qCategory = qmCategoryDao.search(category.getParentId());
				itemCat = QianMiUtil.addItemCats(getAccessToken(), category.getCategoryName(), qCategory.getCid());
			}
			
			if (itemCat != null) {
				success_cat_ids.add(category.getId());
			} else {
				logger.error("QianMiDelegator.recordQMCategories(): 添加到千米的分类失败: 分类id为" + category.getId());
				continue;
			}
			
			long time = System.currentTimeMillis();
			QMCategory qmCategory = new QMCategory();
			qmCategory.setCategoryId(category.getId());
			qmCategory.setParentId(category.getParentId());
			qmCategory.setName(category.getCategoryName());
			qmCategory.setCreateTime(time);
			qmCategory.setUpdateTime(time);
			qmCategory.setParentCid(itemCat.getParentCid());
			qmCategory.setCid(itemCat.getCid());
			qmCategory.setDepth(itemCat.getDepth());
			
			qmCategoryDao.add(qmCategory);
		}
	}

	private void separateCategories(List<Category> categories, List<Category> parent_categories,
			List<Category> children_categories) {
		for (Category category : categories) {
			if (category.getParentId() == 0) {
				parent_categories.add(category);
			} else {
				children_categories.add(category);
			}
		}
	}


}
