package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.SubscriptServiceImpl;
import com.jiuy.core.service.product.ProductTagService;
import com.jiuy.core.service.tag.TagService;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.product.ProductTag;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.subscript.SubscriptVO;

@Service
public class SubscriptFacade {
	@Autowired
	private SubscriptServiceImpl subscriptService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ProductTagService productTagService;
	
	/**
	 * 批量添加到商品
	 * @param subscriptId
	 * @param productIdsString
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int batchUpdateSubscriptId(long subscriptId, String productIdsString) {
		//取消关联的所有商品
		int count = productService.batchRemoveSubscriptId(subscriptId);
		if(StringUtils.equals("", productIdsString)){
			return count;
		}
		
		Set<Long> productIds = transClothesStrToList(productIdsString);
		//修改ProductSum
		subscriptService.updateProductSum(subscriptId, productIds.size());
		
		return productService.batchUpdateSubscriptId(subscriptId, productIds);
	}

	private Set<Long> transClothesStrToList(String productIdsString) {
		String [] productIdsStr = productIdsString.split(",");
		
		Set<Long> productIds = new HashSet<Long>();
		for(String productId : productIdsStr) {
			productIds.add(Long.parseLong(productId));
		}
		
		return productIds;
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param pageQuery
	 */
	public List<SubscriptVO> search(String name, String description, Long productId, PageQuery pageQuery) {
		//根据name、description搜索到的subscript集合
		List<Subscript> subscripts = subscriptService.search(name, description, productId, pageQuery);
		
		Set<Long> subscriptIds = new HashSet<Long>();
		for(Subscript subscript : subscripts){
			subscriptIds.add(subscript.getId());
		}
		
		Map<Long, Integer> productCountOfSubscriptIdMap = getProductCountOfSubscriptIdMap(subscriptIds);
		
		// 获取productIdsString Map<restrictId, productIdsString>
        Map<Long, String> proIdsStringByIds = clothesNosOfId(subscriptIds);
        
        ArrayList<SubscriptVO> subscriptVOs = new ArrayList<SubscriptVO>();
        for(Subscript subscript : subscripts){
        	SubscriptVO subscriptVO = new SubscriptVO();
        	subscriptVOs.add(subscriptVO);
        	
        	BeanUtils.copyProperties(subscript, subscriptVO);
        	
        	long subscriptId = subscript.getId();
        	subscriptVO.setProductIdsToString(proIdsStringByIds.get(subscriptId));
        	subscriptVO.setProductSum(productCountOfSubscriptIdMap.get(subscriptId)==null ? 0 : productCountOfSubscriptIdMap.get(subscriptId));
        }
		
		return subscriptVOs;
	}
	
	
	private Map<Long, Integer> getProductCountOfSubscriptIdMap(Collection<Long> subscriptIds) {
		Map<Long, List<Product>> products = productService.productsOfSubscriptIdsMap(subscriptIds);
		Map<Long, Integer> productNumOfSubscriptId = new HashMap<Long, Integer>();
		
		for(Map.Entry<Long, List<Product>> entry : products.entrySet()){
			long subscriptId = entry.getKey();
			List<Product> productsOfsubscriptId = entry.getValue();
			
			productNumOfSubscriptId.put(subscriptId, productsOfsubscriptId==null ? 0 : productsOfsubscriptId.size());
			
		}
		return productNumOfSubscriptId;
	}
	
	/**
	 * 返回map key=SubscriptId value=SubscriptId所对应的相关联的商品的id集合
	 * @param restrictionIds
	 * @param allProductIds
	 * @return
	 */
	public Map<Long, List<Long>> getProductsOfSubscriptId(Collection<Long> subscriptIds, Set<Long> allProductIds) {
		Map<Long, List<Long>> productIdsOfSubscriptId = new HashMap<Long, List<Long>>();
		Map<Long, List<Product>> productsOfSubscriptId = productService.productsOfSubscriptIdsMap(subscriptIds);
		for(Map.Entry<Long, List<Product>> entry : productsOfSubscriptId.entrySet()) {
			long restrictId = entry.getKey();
			List<Product> products = entry.getValue();
			
			List<Long> productIds = new ArrayList<Long>();
			for(Product product : products) {
				long productId = product.getId();

				productIds.add(productId);
				allProductIds.add(productId);
			}
			productIdsOfSubscriptId.put(restrictId, productIds);
		}
		
		return productIdsOfSubscriptId;
	}
	
	/**
	 * 返回map key=SubscriptId value=SubscriptId所对应的相关联的商品的id字符串   字符串形式为：220,221,223
	 * @param subscriptIds
	 * @return
	 */
	private Map<Long, String> clothesNosOfId(Collection<Long> subscriptIds) {
        Map<Long, List<Product>> products = productService.productsOfSubscriptIdsMap(subscriptIds);

        Map<Long, String> clothesNoById = new HashMap<Long, String>();
        for (Map.Entry<Long, List<Product>> entry : products.entrySet()) {
            String clothesNos = assembleClothesNos(entry.getValue());
            clothesNoById.put(entry.getKey(), clothesNos);
        }

        return clothesNoById;
    }
	
	private String assembleClothesNos(List<Product> products) {
        if (products.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Product product : products) {
            builder.append(product.getId() + ",");
        }

        int index = builder.lastIndexOf(",");
        if (index < 0) {
            return "";
        }

        return builder.substring(0, index);
    }

	public void applyToProduct(long subscriptId, Long topCatId, Long subCatId, Long brandId, Long topTagId,
			Long subTagId) {
		Set<Long> productIds = new HashSet<>();
		if (brandId != null) {
			aseembleWithBrandId(productIds, brandId);
		}
		
		if (topCatId != null) {
			aseembleWithCategoryId(productIds, topCatId, subCatId);
		} 
		
		if (topTagId != null) {
			aseembleWithTagId(productIds, topTagId, subTagId);
		}
		
		productService.batchUpdate(null, null, subscriptId, productIds);
	}

	private void aseembleWithTagId(Set<Long> productIds, Long topTagId, Long subTagId) {
		Set<Long> tagIds = new HashSet<>();
		if (subTagId != null) {
			tagIds.add(subTagId);
		} else {
			List<Tag> tags = tagService.search(topTagId, null,0);
			for (Tag tag : tags) {
				tagIds.add(tag.getId());
			}
		}
		
		List<ProductTag> productTags = productTagService.search(tagIds);
		for (ProductTag productTag : productTags) {
			productIds.add(productTag.getProductId());
		}
	}

	private void aseembleWithCategoryId(Set<Long> productIds, Long topCatId, Long subCatId) {
		Set<Long> categoryIds = new HashSet<>();
		if (subCatId != null) {
			categoryIds.add(subCatId);
		} else {
			List<Category> categories = categoryService.getSubCat(topCatId);
			for (Category category : categories) {
				categoryIds.add(category.getId());
			}
		}
		
		List<ProductCategory> productCategories = productCategoryMapper.itemsOfCategoryIds(categoryIds);
		for (ProductCategory productCategory : productCategories) {
			productIds.add(productCategory.getProductId());
		}
	}

	private void aseembleWithBrandId(Set<Long> productIds, Long brandId) {
		List<Product> products = productService.search(null, brandId);
		for (Product product : products) {
			productIds.add(product.getId());
		}
	}
}
