package com.jiuy.web.delegate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.business.facade.CategoryFacade;
import com.jiuy.core.dao.RestrictionCombinationDao;
import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.dao.modelv2.ProductPropertyMapper;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.category.CategoryType;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.RestrictionCombination;

@Service
public class ProductDelegator {



	@Autowired
	private CategoryFacade categoryFacade;
	
    @Autowired
    private BrandLogoServiceImpl brandLogoService;
	
	@Autowired
	private PropertyService propertyService; 
	
	@Autowired
	private ProductPropertyMapper productPropertyMapper;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;
	
	@Autowired
	private LOWarehouseService lOWarehouseService;
	
	@Autowired
	private RestrictionCombinationDao restrictionCombinationDao;
	
	public Map<String, Object> loadInfo(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		long startProductTime = System.currentTimeMillis();
		
		List<ProductSKUVO> skuVOs = productService.getProductSKUsByProduct(id);
		map.put("skus", skuVOs);
		Product product = productService.getProductById(id);
		ProductShare productShare = productService.getProductShareByProId(id);
		
		if(productShare != null && productShare.getShareDesc() != null){
			product.setShareDesc(productShare.getShareDesc());
		}
		
		if(productShare != null && productShare.getShareImg() != null){
			product.setShareImg(productShare.getShareImg());
		}
		
		if(productShare != null && productShare.getShareTitle() != null){
			product.setShareTitle(productShare.getShareTitle());
		}
		map.put("product", product);
		
		long midProductTime = System.currentTimeMillis();
		
		int saleStatus = -3;
		for(ProductSKUVO skuVO : skuVOs) {
			int status = skuVO.getStatus();
			if(status > saleStatus) {
				saleStatus = status;
			}
		}
		map.put("sale_status", saleStatus);
		map.put("max_weight", productService.getMaxWeight());
		
		long endProductTime = System.currentTimeMillis();
		
		Set<Long> propertyNameIds = new HashSet<Long>();
		propertyNameIds.add(PropertyName.SEASON.getValue());
		propertyNameIds.add(PropertyName.YEAR.getValue());
		
		Map<Long, List<ProductPropValue>> valueMap = propertyService.propertyValuesOfNameIdMap(propertyNameIds);
		map.put("years", valueMap.get(PropertyName.YEAR.getValue()));
		map.put("seasons", valueMap.get(PropertyName.SEASON.getValue()));
		map.put("brands", brandLogoService.getBrands());
		
		Map<Long, ProductProp> productPropMap = productPropertyMapper.valueOfNameIdMap(id); 
		ProductProp yearProp = productPropMap.get(PropertyName.YEAR.getValue());
		ProductProp seasonProp = productPropMap.get(PropertyName.SEASON.getValue());
		map.put("product_attr_year", yearProp == null ? "" : yearProp.getPropertyValueId());
		map.put("product_attr_season", seasonProp == null ? "" : seasonProp.getPropertyValueId());
		
		List<ProductCategory> productCategories = productCategoryMapper.itemsOfProductIds(CollectionUtil.createList(id));
		map.put("product_attr_category", productCategories);
		
		List<Category> caList = categoryFacade.getCategoryByType(CategoryType.NORMAL.getIntValue());
		List<Category> categories = categoryService.getCategoryTree(caList);
		map.put("categories", categories);
		
		List<RestrictionCombination> restrictionCombinations = restrictionCombinationDao.search(null, "");
		map.put("restrict_group", restrictionCombinations);
		
		List<Category> virtualCategories = categoryFacade.getCategoryByType(CategoryType.VIRTUAL.getIntValue());
		map.put("virtual_categories", virtualCategories);
		
		List<LOWarehouse> warehouses = lOWarehouseService.srchWarehouse(null, "", true);
		map.put("warehouses", warehouses);
		
		
		return map;
	}
	
	public Map<String, Object> loadWholeSaleProductInfo(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		long startProductTime = System.currentTimeMillis();
		
		List<ProductSKUVO> skuVOs = productService.getProductSKUsByProduct(id);
		map.put("skus", skuVOs);
		Product product = productService.getProductById(id);
		map.put("product", product);
		
		long midProductTime = System.currentTimeMillis();
		
		int saleStatus = -3;
		for(ProductSKUVO skuVO : skuVOs) {
			int status = skuVO.getStatus();
			if(status > saleStatus) {
				saleStatus = status;
			}
		}
		map.put("sale_status", saleStatus);
		
		long endProductTime = System.currentTimeMillis();
		
		//优先查找批发选择的关系表
		List<ProductCategory> productCategories = productCategoryMapper.itemsWholeSaleProductIds(CollectionUtil.createList(id));
		if(productCategories==null || productCategories.size()==0){
			productCategories = productCategoryMapper.itemsOfProductIds(CollectionUtil.createList(id));
		}
		
		map.put("product_attr_category", productCategories);
		//共用的分类列表
		List<Category> caList = categoryFacade.getCategoryByType(CategoryType.NORMAL.getIntValue());
		List<Category> categories = categoryService.getCategoryTree(caList);
		map.put("categories", categories);
		
		
		return map;
	}
}
