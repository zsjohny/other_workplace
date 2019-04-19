package com.jiuy.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.core.business.assemble.Assembler;
import com.jiuy.core.dao.ProductDao;
import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductPropertyMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.product.ProductVO;
import com.jiuy.core.service.admin.AdminLogService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.core.service.notifacation.NotifacationService;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.category.CategoryType;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.ProductV1;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.ProductWindow;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.store.entity.ProductCategoryVO;
import com.store.entity.ProductPropVO;

@Service
@SuppressWarnings("unused")
public class ProductService {
	@Resource
	private StoreBusinessService storeBusinessService;
	@Autowired
	private NotifacationService notificationService;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ProductSkuNewMapper productSKUNewMapper;
	@Autowired
	private ProductNewMapper productNewMapper;
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductPropertyMapper productPropertyMapper;

	@Autowired
	private ProductSKUMapper productSKUMapper;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private ProductCategoryMapper productCategoryMapper;

	@Autowired
	private Assembler<ProductPropName> productPropNameAssembler;

	@Autowired
	private Assembler<ProductPropValue> productPropValueAssembler;
	@Resource
	public AdminLogService alService;
	@Autowired
	private MemcachedService memcachedService;

	private static final Logger logger = Logger.getLogger(ProductService.class);

	public Product getProductById(long productId) {
		Product product = productMapper.getProductById(productId);
		return product;
	}

	public ProductShare getProductShareByProId(long productId) {
		ProductShare productShare = productMapper.getProductShareByProId(productId);
		return productShare;
	}

	/**
	 * 添加新产品
	 * 
	 * @param newProductVO
	 * @return
	 * 
	 * 		deprecated on 2015/12/27 content: 兼容旧的产品增加接口，后续被替代后不再建议使用
	 */
	@Deprecated
	@Transactional(rollbackFor = Exception.class)
	public long addProduct(ProductVO newProductVO) {
		Product product = newProductVO.getProduct();
		long prodid = productMapper.add(product);
		prodid = product.getId();
		// logger.debug("new product id:" + prodid);
		if (prodid > 0) {
			// 更新基本属性
			List<ProductPropVO> basics = newProductVO.getBaseProperties();
			for (ProductPropVO ppvo : basics) {
				ppvo.setProductId(prodid);
			}
			productPropertyMapper.batchAdd(basics);
			// 更新SKU属性
			List<ProductSKUVO> skus = newProductVO.getProductSKUs();
			for (ProductSKUVO pskuvo : skus) {
				pskuvo.setProductId(prodid);
			}
			productSKUMapper.batchAdd(skus);
			// 更新分类
			List<ProductCategoryVO> pcs = newProductVO.getProductCategories();
			for (ProductCategoryVO pc : pcs) {
				pc.setProductId(prodid);
			}
			productCategoryMapper.batchAdd(pcs);
		}
		return prodid;
	}

	public List<Category> getCategoryDefinitions(boolean needTreeRelation) {
		if (needTreeRelation) {
			return categoryService.getCategoryTree(categoryService.search(CategoryType.NORMAL.getIntValue()));
		}
		return categoryService.getCategories();
	}

	public List<Product> loadProductList(PageQuery query, boolean bContainsInvalid) {
		Integer prodStatus = null;
		if (!bContainsInvalid) {
			prodStatus = 0;
		}
		List<Product> productList = productMapper.loadList(query, prodStatus);
		return productList;
	}

	public int getProductListCount(PageQuery query, boolean bContainsInvalid) {
		Integer prodStatus = null;
		if (!bContainsInvalid) {
			prodStatus = 0;
		}
		return productMapper.getProductCount(query, prodStatus);
	}

	public int soldoutProductList(String[] productIds) {
		Collection<Long> ids = new ArrayList<Long>();
		for (String id : productIds) {
			ids.add(Long.parseLong(id));
		}
		return productMapper.deactivateProductByIds(ids);
	}

	public List<ProductSKUVO> getProductSKUsByProduct(long productId) {
		Product p = productMapper.getProductById(productId);
		if (null == p) {
			return new ArrayList<ProductSKUVO>(0);
		}
		List<ProductSKU> skus = productSKUMapper.getProductSKUs(productId, "");
		List<ProductSKUVO> skuvos = null;
		if (null != skus) {
			skuvos = new ArrayList<ProductSKUVO>();
			for (ProductSKU sku : skus) {
				ProductSKUVO skuvo = new ProductSKUVO();
				BeanUtils.copyProperties(sku, skuvo);
				List<ProductPropVO> propsWithSKUIds = sku.getProductProps();
				List<ProductPropVO> propsWithSKUObjs = new ArrayList<ProductPropVO>();
				for (ProductPropVO ppvo : propsWithSKUIds) {
					ProductPropName bppn = new ProductPropName();
					bppn.setId(ppvo.getPropertyNameId());
					ProductPropValue bppv = new ProductPropValue();
					bppv.setId(ppvo.getPropertyValueId());
					ppvo.assemble(productPropNameAssembler.assemble(bppn));
					ppvo.assemble(productPropValueAssembler.assemble(bppv));
					propsWithSKUObjs.add(ppvo);
				}
				skuvo.setSkuProperties(propsWithSKUObjs);
				skuvos.add(skuvo);
			}
		}
		return skuvos;
	}

	public int updateProductSKU(long skuId, int totalCount) {
		return productSKUMapper.addProductSKUCountById(skuId, totalCount);
	}

	public int updateProductSKUsByProduct(long productid, String[] skus) {
		if (productid < 0) {
			return ResultCode.PRODUCT_ERROR_NOT_EXSIT.getIntValue();
		}
		if (null == skus || skus.length == 0) {
			return ResultCode.PRODUCT_INVALID_SKU_DEFINE.getIntValue();
		}
		return updateProductSKU(productid, skus);
	}

	private int updateProductSKU(long productid, String[] skus) {
		// 需要新增的库存类型
		List<ProductSKUVO> skuvosForInsert = new ArrayList<ProductSKUVO>();
		// 需要更新的库存类型
		List<ProductSKUVO> skuvosForUpdate = new ArrayList<ProductSKUVO>();
		for (String sku : skus) {
			processSKU(productid, sku, skuvosForInsert, skuvosForUpdate);
		}
		productSKUMapper.batchAdd(skuvosForInsert);
		productSKUMapper.batchUpdate(skuvosForUpdate);
		return 0;
	}

	private void processSKU(long productid, String sku, List<ProductSKUVO> skuvosForInsert,
			List<ProductSKUVO> skuvosForUpdate) {
		if (null == skuvosForInsert || null == skuvosForUpdate || StringUtils.isBlank(sku)) {
			return;
		}
		String[] skuFields = StringUtils.split(sku, '_');
		if (skuFields.length != 6) {// 处理更新的情况
			if (skuFields.length != 4) { // 处理新增情况
				return;
			}
		}
		ProductSKUVO psskuvo = new ProductSKUVO();
		StringBuffer sbSkuDesc = new StringBuffer();
		Product p = productMapper.getProductById(productid);
		if (skuFields.length == 6) {
			List<ProductSKU> curSKUS = productSKUMapper.getProductSKUs(productid, "");
			sbSkuDesc.append(skuFields[0]);
			sbSkuDesc.append(":");
			sbSkuDesc.append(skuFields[1]);
			sbSkuDesc.append(";");
			sbSkuDesc.append(skuFields[2]);
			sbSkuDesc.append(":");
			sbSkuDesc.append(skuFields[3]);
			for (ProductSKU ps : curSKUS) {
				if (ps.getPropertyIds().equals(sbSkuDesc.toString())) {
					BeanUtils.copyProperties(ps, psskuvo);
					psskuvo.setRemainCount(Integer.parseInt(skuFields[4]));
					psskuvo.setSkuNo(Long.parseLong(skuFields[5]));
					break;
				}
			}
			skuvosForUpdate.add(psskuvo);
		} else {
			String chnColor = skuFields[0];
			String chnSize = skuFields[1];
			ProductPropValue colorProperty = propertyService.getPropertyValue(chnColor);
			ProductPropValue sizeProperty = propertyService.getPropertyValue(chnSize);
			if (null != colorProperty && null != sizeProperty) {
				sbSkuDesc.append(colorProperty.getPropertyNameId());
				sbSkuDesc.append(":");
				sbSkuDesc.append(colorProperty.getId());
				sbSkuDesc.append(";");
				sbSkuDesc.append(sizeProperty.getPropertyNameId());
				sbSkuDesc.append(":");
				sbSkuDesc.append(sizeProperty.getId());
				psskuvo.setProductId(productid);
				psskuvo.setPropertyIds(sbSkuDesc.toString());
				psskuvo.setRemainCount(Integer.parseInt(skuFields[2]));
				psskuvo.setSkuNo(Long.parseLong(skuFields[3]));
				psskuvo.setCreateTime(new Date().getTime());
				psskuvo.setPrice(p.getPrice());
				psskuvo.setStatus(0);
				psskuvo.setUpdateTime(new Date().getTime());
				skuvosForInsert.add(psskuvo);
			}
		}
	}

	/**
	 * 修改商品页面(下面部分，不包括库存信息),目前主要修改"商品名","商品展示图片","概要图","价格" + 季节 品牌 年份
	 * 
	 * @param prod
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResultCode updateProduct(Product prod) {

		String season = prod.getSeason();
		String classification = prod.getClassification();

		if (prod.getlOWarehouseId() == 0 || prod.getBrandId() == 0 || StringUtils.equals(null, season)
				|| StringUtils.equals(null, classification)) {
			return ResultCode.COMMON_ERROR_BAD_PARAMETER;
		}

		Date date = new Date();
		long time = date.getTime();

		try {
			updateProductProperty(season, prod);
			updateProductCategory(classification, prod, time);

			// 更新旧表
			updateProductBrand(prod);

			productMapper.updateProduct(prod);
		} catch (Exception e) {
			logger.error("updateProduct failed. params: " + prod.toString(), e);
		}

		productSKUMapper.updatePushTime(CollectionUtil.createList(prod.getClothesNumber()), 0);

		return ResultCode.COMMON_SUCCESS;
	}

	/**
	 * 修改商品分享信息
	 * 
	 * @param prod
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResultCode updateProductShare(ProductShare productShare) {

		try {
			productMapper.updateProductShare(productShare);
		} catch (Exception e) {
			logger.error("updateProductShare failed. params: " + productShare.toString(), e);
		}

		return ResultCode.COMMON_SUCCESS;
	}

	/**
	 * 修改商品批发详情页面 目前只有批发价和商品分类可修改
	 * 
	 * @param prod
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResultCode updateWholeSaleProduct(Product prod) {
		String classification = prod.getClassification();
		if (StringUtils.equals(null, classification)) {
			return ResultCode.COMMON_ERROR_BAD_PARAMETER;
		}

		Date date = new Date();
		long time = date.getTime();

		try {
			// 修改对应商品的批发价
			productMapper.updateProductWholeSaleCash(prod);
			// 修改批发关系表
			updateWholeSaleCategory(classification, prod, time);
		} catch (Exception e) {
			logger.error("updateProduct failed. params: " + prod.toString(), e);
		}

		return ResultCode.COMMON_SUCCESS;
	}

	private void updateProductCategory(String classification, Product prod, long createTime) {
		long[] classificationArrayInt = null;

		String[] classificationArrayString = classification.split("\\|");
		classificationArrayInt = transformArrayString2Int(classificationArrayString, classificationArrayString.length);

		try {
			productCategoryMapper.deleteProductCategory(prod.getId());
			productCategoryMapper.addProductCategory(prod.getId(), classificationArrayInt, createTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateWholeSaleCategory(String classification, Product prod, long createTime) {
		long[] classificationArrayInt = null;

		String[] classificationArrayString = classification.split("\\|");
		classificationArrayInt = transformArrayString2Int(classificationArrayString, classificationArrayString.length);

		try {
			productCategoryMapper.deleteWholeCategory(prod.getId());
			productCategoryMapper.addWholeSaleCategory(prod.getId(), classificationArrayInt, createTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int updateProductBrand(Product prod) {
		if (prod == null) {
			return 0;
		}

		int count = 0;
		int ppmId = productPropertyMapper.getPropertyByIds(prod.getId(), PropertyName.BRAND.getValue());

		if (ppmId != -1) {
			count = productPropertyMapper.updateProductBrand(ppmId, prod.getBrandId());
		} else {
			count = productPropertyMapper.addProductBrand(prod.getId(), PropertyName.BRAND.getValue(),
					prod.getBrandId());
		}

		return count;
	}

	/**
	 * 根据获取的字符串进行解析后,存入yjj_ProductProperty表
	 * 
	 * @param season
	 * @param prod
	 */
	private void updateProductProperty(String property, Product prod) {
		String[] propertyArray = property.split("\\|");

		for (String s : propertyArray) {
			if (s.contains(":")) {
				long[] seasonArrayInt = null;
				String[] seasonArray2 = s.split(":");
				seasonArrayInt = transformArrayString2Int(seasonArray2, seasonArray2.length);
				int productPropertyId = productPropertyMapper.getPropertyByIds(prod.getId(), seasonArrayInt[0]);

				if (productPropertyId != -1) {
					productPropertyMapper.updateProductProperty(productPropertyId, seasonArrayInt);
				} else {
					productPropertyMapper.addProductProperty(prod.getId(), seasonArrayInt);
				}
			}
		}

	}

	/**
	 * 字符串数组转int数组
	 * 
	 * @param seasonArrayString
	 * @param length
	 */
	private long[] transformArrayString2Int(String[] seasonArrayString, int length) {
		long[] seasonArrayInt = new long[length];
		for (int i = 0; i < length; i++)
			seasonArrayInt[i] = Integer.parseInt(seasonArrayString[i]);
		return seasonArrayInt;
	}

	/**
	 * 校验衣服款号的唯一性 author:Jeff.Zhan
	 * 
	 * @param clothesNumber
	 * @return
	 */
	public List<Integer> chkProductClothesNum(String clothesNumber) {
		return productMapper.chkProductClothesNum(clothesNumber);
	}

	/**
	 * 在修改产品页面 根据商品id 获得其所属分类 author:Jeff.Zhan
	 * 
	 * @param productid
	 * @return
	 */
	public List<Map<String, Object>> getCategoriesInfo(long productid) {
		return productMapper.getCategoriesInfo(productid);
	}

	/**
	 * 在修改产品页面 根据商品id 获得其上架年份 author:Jeff.Zhan
	 * 
	 * @param productid
	 * @return
	 */
	public Map<String, Object> getPropertyInfo(long productid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brand", new HashMap<String, Object>());
		map.put("season", new HashMap<String, Object>());
		map.put("year", new HashMap<String, Object>());
		map.put("showStatus", new HashMap<String, Object>());

		List<Map<String, Object>> list = productMapper.getPropertyInfo(productid);
		for (Map<String, Object> param : list) {
			long propertyNameId = (long) param.get("PropertyNameId");

			if (PropertyName.BRAND.getValue() == propertyNameId)
				map.put("brand", param);
			else if (PropertyName.SEASON.getValue() == propertyNameId)
				map.put("season", param);
			else if (PropertyName.YEAR.getValue() == propertyNameId)
				map.put("year", param);
		}

		Product prod = productMapper.getProductById(productid);
		map.put("showStatus", prod.getShowStatus());

		return map;
	}

	/**
	 * 在修改页面获取商品规格
	 * 
	 * @param productid
	 * @return
	 */
	public List<String> getRemark(long productid) {
		return productMapper.getRemark(productid);
	}

	/**
	 * 在修改页面获取商品介绍
	 * 
	 * @param productid
	 * @return
	 */
	public List<String> getDescription(long productid) {
		return productMapper.getDescription(productid);
	}

	public List<Map<String, Object>> loadProductCategoryNames(PageQuery query, boolean bContainsInvalid) {
		Integer prodStatus = null;
		if (!bContainsInvalid) {
			prodStatus = 0;
		}
		List<Integer> productIdList = productMapper.loadproductIdList(query, prodStatus);
		List<Map<String, Object>> ProductCategoryNames = productCategoryMapper.loadProductCategoryNames(productIdList);
		return ProductCategoryNames;
	}

	/**
	 * 根据上下架时间修改状态
	 * 
	 * created on 2015/12/27
	 */
	public void updateProductStatus() {
		// 根据产品的到期时间修改上架状态
		productMapper.updateProductStatusOut();
	}

	/**
	 * 更新产品表的BrandId字段的数据
	 * 
	 * @param brandId
	 */
	public int updateProductBrandId(int brandId) {
		return productMapper.updateProductBrandId(brandId);
	}

	public ProductWindow searchProWinByClothesNum(String clothesNum) {
		return productMapper.searchProWinByClothesNum(clothesNum);
	}

	public int getRemainCountById(long id) {
		return productSKUMapper.getRemainCountById(id);
	}

	public List<Product> getIdsByRootPath(String oldImgRootPath) {
		return productMapper.getIdsByRootPath(oldImgRootPath);
	}

	public List<Map<String, Object>> searchOverview(long brandId, String clothesNo, PageQuery pageQuery,
			String orderSql, String saleStatusSql, int skuStatusIntValue) {
		return productMapper.searchOverview(brandId, clothesNo, pageQuery, orderSql, saleStatusSql, skuStatusIntValue);
	}

	public int searchOverviewCount(long brandId, String clothesNo, String saleStatusSql, int skuStatus) {
		return productMapper.searchOverviewCount(brandId, clothesNo, saleStatusSql, skuStatus);
	}

	public int getMaxWeight() {
		String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;
		String key = "max_weight";
		Object obj = memcachedService.get(groupKey, key);

		if (obj != null) {
			return (Integer) obj;
		}

		Integer maxWeight = productMapper.getMaxWeight();
		memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, maxWeight);
		return maxWeight;
	}

	public boolean isOnSale(long productId) {
		return productMapper.isOnSale(productId);
	}

	public List<Product> getByClothesNums(Collection<String> clothesNums) {
		if (clothesNums.size() < 1) {
			return new ArrayList<Product>();
		}
		return productMapper.getByClothesNums(clothesNums);
	}

	public List<Product> productOfIds(Collection<Long> productIds) {
		if (productIds.size() < 1) {
			return new ArrayList<Product>();
		}
		return productMapper.productOfIds(productIds);
	}

	public Map<Long, Product> productMapOfIds(Collection<Long> productIds) {

		if (productIds == null) {
			Map<Long, Product> productMap = new HashMap<>();

			List<Product> products = productMapper.getAllProducts();
			for (Product product : products) {
				productMap.put(product.getId(), product);
			}

			return productMap;
		}

		if (productIds.size() < 1) {
			return new HashMap<Long, Product>();
		}

		Map<Long, Product> productMap = new HashMap<>();

		List<Product> products = productMapper.productOfIds(productIds);
		for (Product product : products) {
			productMap.put(product.getId(), product);
		}

		return productMap;
	}

	public long insertProduct(Product product) {
		return productMapper.insertProduct(product);
	}

	public Product getBySkuId(long skuId) {
		return productMapper.getBySkuId(skuId);
	}

	public Map<Long, List<Product>> productsOfRestrctIdsMap(Collection<Long> restrictionIds) {
		if (restrictionIds.size() < 1) {
			return new HashMap<Long, List<Product>>();
		}

		Map<Long, List<Product>> productsOfRestrctId = new HashMap<Long, List<Product>>();

		List<Product> products = productMapper.productsOfRestrctIds(restrictionIds);

		long lastRestrictId = 0;
		List<Product> list = null;
		for (Product product : products) {
			if (product.getRestrictId() != lastRestrictId) {
				lastRestrictId = product.getRestrictId();
				list = new ArrayList<Product>();
				productsOfRestrctId.put(lastRestrictId, list);
			}
			list.add(product);
		}

		return productsOfRestrctId;
	}

	public int batchUpdate(Long restrictId, Long vCategoryId, Long subscriptId, Collection<Long> productIds) {
		if (productIds.size() < 1) {
			return 0;
		}
		return productMapper.batchUpdate(restrictId, vCategoryId, subscriptId, productIds);
	}

	public int batchRemoveRestrictId(long restrictId) {
		return productMapper.batchRemoveRestrictId(restrictId);
	}

	public List<Product> search(String clothesNo, Long brandId) {
		return productMapper.search(clothesNo, brandId);
	}

	public int update(Long id, Integer status, Integer weight, Integer promotionSaleCount,
			Integer promotionVisitCount) {
		return productMapper.update(id, status, weight, promotionSaleCount, promotionVisitCount);
	}

	public int update(Long id, Integer status, Long saleStartTime, Long saleEndTime) {
		return productMapper.update(id, status, saleStartTime, saleEndTime);
	}

	public int batchUpdateSubscriptId(Long subscriptId, Collection<Long> productIds) {
		if (productIds.size() < 1) {
			return 0;
		}
		return productMapper.batchUpdateSubscriptId(subscriptId, productIds);
	}

	public int batchRemoveSubscriptId(long subscriptId) {
		return productMapper.batchRemoveSubscriptId(subscriptId);
	}

	/**
	 * 获取SubscriptId对应商品集合的 map
	 * 
	 * @param subscriptIds
	 * @return
	 */
	public Map<Long, List<Product>> productsOfSubscriptIdsMap(Collection<Long> subscriptIds) {
		if (subscriptIds.size() < 1) {
			return new HashMap<Long, List<Product>>();
		}

		Map<Long, List<Product>> productsOfSubscriptId = new HashMap<Long, List<Product>>();

		List<Product> products = productMapper.productsOfSubscriptIds(subscriptIds);

		long lastSubscriptId = 0;
		List<Product> list = null;
		for (Product product : products) {
			if (product.getSubscriptId() != lastSubscriptId) {
				lastSubscriptId = product.getSubscriptId();
				list = new ArrayList<Product>();
				productsOfSubscriptId.put(lastSubscriptId, list);
			}
			list.add(product);
		}

		return productsOfSubscriptId;
	}

	public Product searchOne(String clothesNum) {
		List<Product> products = productMapper.search(clothesNum, null);
		if (products.size() < 1) {
			return null;
		}
		return products.get(0);
	}

	/**
	 * @param categoryId
	 */
	public int batchDeleteVCategory(long categoryId) {
		return productMapper.batchDeleteVCategory(categoryId);
	}

	public List<Product> getByWarehouseId(Long warehouseId) {
		return productMapper.getByWarehouseId(warehouseId);
	}

	public List<Product> getByWarehouseIds(Collection<Long> warehouseIds) {
		return productMapper.getByWarehouseIds(warehouseIds);
	}

	public List<Product> getByNotInWarehouseIds(List<Long> warehouseIds) {
		return productMapper.getByNotInWarehouseIds(warehouseIds);
	}

	public List<Map<String, Object>> productMap(Collection<Long> seasonIds) {
		return productMapper.productMap(seasonIds);
	}

	public List<ShopProduct> getShopProductByProductId(long productId) {
		return productMapper.getShopProductByProductId(productId);
	}

	/*
	 * 判断该商品是否上架 其下有一个SKU上架则该商品上架，都为下架则该商品下架
	 */
	public boolean isOnShelf(long productId) {
		List<ProductSKU> productSKUs = productSKUMapper.getProductSKUByProductId(productId);
		for (ProductSKU productSKU : productSKUs) {
			if (productSKU.isOnShelf()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 下架商家商品
	 * 
	 * @param shopProductId
	 * @return
	 */
	public int soldOutShopProduct(Long shopProductId) {
		return productMapper.soldOutShopProduct(shopProductId);
	}

	/**
	 * 下架商品时同步更新商家商品并发送通知 注意需要在商品真正下架时才调用
	 */
	public void synSoldoutShopProduct(long productId) {

		// 检查改商品是否是

		logger.info("开始下架商家商品");

		List<ShopProduct> shopProductList = getShopProductByProductId(productId);
		logger.info("====shopProductList.size()：" + shopProductList.size());
		List<Long> storeIdList = new ArrayList<Long>();

		for (ShopProduct shopProduct : shopProductList) {
			if (shopProduct.getSoldOut() == ShopProduct.sold_out_up) {// 状态为上架
				soldOutShopProduct(shopProduct.getId());
				logger.info("====该商家商品为上架状态，执行下架操作完成");
				storeIdList.add(shopProduct.getStoreId());
			} else {
				logger.info("====该商家商品不为上架状态无需下架：");
			}
		}

		logger.info("完成下架商家商品,shopProductList.size()" + shopProductList.size());

		// if(storeIdList.size() > 0){
		// //2、发送系统消息
		// logger.info("开始发送同步更新商家商品系统通知"+",productId"+productId+",storeIdList:"+JSON.toJSONString(storeIdList));
		// notificationService.sendSoldoutProductSKUSendNotification(productId,storeIdList);
		// logger.info("完成发送同步更新商家商品系统通知"+",productId"+productId+",storeIdList:"+JSON.toJSONString(storeIdList));
		// }else{
		// logger.info("没有上架上架该商品无需发送同步更新商家商品系统通知"+",productId"+productId+",storeIdList:"+JSON.toJSONString(storeIdList));
		// }
	}

	/**
	 * 商品上架同步商家商品并发送系统通知
	 * 
	 * @param productSKUId
	 */
	public void synPutawayShopProduct(long productId) {

		logger.info("待实现，商品上架同步商家商品并发送系统通知");
		List<Long> storeIdList = new ArrayList<Long>();

		// TODO v2.3 待开发
		// 1、获取打开同步上新开关的商家
		List<StoreBusiness> storeList = storeBusinessService.getSynProductStoreList();
		logger.info("打开同步上新的门店数量storeList.size()" + storeList.size());
		// 2、检测商家是否已经同步该商品
		for (StoreBusiness storeBusiness : storeList) {
			long storeId = storeBusiness.getId();
			double rate = storeBusiness.getRate();
			ShopProduct shopProduct = getShopProduct(storeId, productId);
			if (shopProduct == null) {
				// 3、同步平台商品到商家商品
				logger.info("todo同步平台商品到商家商品");
				Product product = productMapper.getProductById(productId);
				ShopProduct newShopProduct = getShopProductFromProduct(product, rate, storeId);
				productMapper.addShopProduct(newShopProduct);
				logger.info("todo同步平台商品到商家商品完成,newShopProduct:" + JSON.toJSONString(newShopProduct));

				storeIdList.add(storeId);
			} else {
				logger.info("商家商品已存在，无需同步,shopProduct.getId():" + shopProduct.getId());
			}
		}

		// //4、发送上新系统通知
		// if(storeIdList.size() > 0){
		// logger.info("开始发送上新系统通知"+",productId"+productId+",storeIdList:"+JSON.toJSONString(storeIdList));
		// notificationService.putAwayProductSKUSendNotification(productId,storeIdList);
		// }else{
		// logger.info("没有商家需要发送上新通知"+",productId"+productId+",storeIdList:"+JSON.toJSONString(storeIdList));
		// }

	}

	private ShopProduct getShopProductFromProduct(Product product, double rate, long storeId) {
		long time = System.currentTimeMillis();
		ShopProduct shopProduct = new ShopProduct();
		shopProduct.setProductId(product.getId());
		// 状态:-1：删除，0：正常
		shopProduct.setStatus(0);
		// 0：已下单，1：未下单
		shopProduct.setTabType(1);
		// 0：草稿，1：上架， 2：下架
		shopProduct.setSoldOut(1);
		// 1是自有商品，0平台商品
		shopProduct.setOwn(0);
		shopProduct.setUpdateTime(time);
		shopProduct.setCreateTime(time);
		shopProduct.setGroundTime(time);
		shopProduct.setStoreId(storeId);
		// 计算倍率价格
		if (rate > 0) {
			BigDecimal wholeSaleCash = new BigDecimal(product.getWholeSaleCash());
			BigDecimal bigRate = new BigDecimal(rate);
			wholeSaleCash = wholeSaleCash.multiply(bigRate);
			shopProduct.setPrice(wholeSaleCash.doubleValue());
		}

		return shopProduct;

	}

	public ShopProduct getShopProduct(long storeId, long productId) {
		List<ShopProduct> shopProductList = productMapper.getShopProduct(storeId, productId);
		if (shopProductList.size() > 0) {
			return shopProductList.get(0);
		}
		return null;
	}

	public int updateProductSaleCount(long productId, int count) {
		return productMapper.updateProductSaleCount(productId, count);
	}

	/**
	 * 强制下架商品
	 * 
	 * @param productId
	 * @param remark
	 * @param request
	 */
	@Transactional(rollbackFor = Exception.class)
	public int soldoutProductList(long productId, String remark, HttpServletRequest request) {

		long time = System.currentTimeMillis();
		// 1、批量下架SKU
		// 获取商品上架SKU列表
		Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
		wrapper.eq("ProductId", productId);//
		wrapper.eq("Status", ProductSkuNew.up_sold);// 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
		List<ProductSkuNew> skuList = productSKUNewMapper.selectList(wrapper);
		// 循环修改为下架状态
		for (ProductSkuNew sku : skuList) {
			ProductSkuNew newSku = new ProductSkuNew();
			newSku.setId(sku.getId());
			newSku.setStatus(ProductSkuNew.down_sold);
			productSKUNewMapper.updateById(newSku);
			logger.info("商品SKU下架完成，productId" + productId + ",skuId:" + sku.getId());
		}
		logger.info("商品下架完成，productId" + productId);
		// 2、修改商品状态为下架
		ProductNew product = new ProductNew();
		product.setId(productId);
		product.setState(ProductNewStateEnum.audit_no_pass.getIntValue());
		product.setUpdateTime(time);
		product.setAuditNoPassReason(remark);//审核不通过原因
		productNewMapper.updateById(product);

		// 记录操作下架日志
		HttpSession session = request.getSession();
		AdminUser userinfo = (AdminUser) session.getAttribute("userinfo");
		if (userinfo != null) {
			alService.addAdminLog(userinfo, "com.jiuy.web.controller.ProductController",
					userinfo.getUserName() + "此次成功下架的商品id为" + productId, null);
		}
		return 1;//
	}

	/**
	 * 强制下架商品检测
	 * 
	 * @param productId
	 */
	public int soldoutCheckIds(long productId) {
		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("id", productId).eq("state", 6);
		List<ProductNew> productNews = productNewMapper.selectList(wrapper);
		if (productNews.size() < 1) {
			return 0;
		}
		return 1;
	}

	public List<Long> productsOfCategory(List<Long> list) {
		return productNewMapper.productsOfCategory(list);
	}

}
