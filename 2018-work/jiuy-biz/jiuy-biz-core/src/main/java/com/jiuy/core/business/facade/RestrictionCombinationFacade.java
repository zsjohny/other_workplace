package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.RestrictionCombinationDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.RestrictionCombinationVO;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.CollectionUtil;

@Service
public class RestrictionCombinationFacade {
	
	@Autowired
	private RestrictionCombinationDao restrictionCombinationDao;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductSKUService productSKUService;

	public List<RestrictionCombinationVO> search(PageQuery pageQuery, String name) {
		List<RestrictionCombination> restrictionCombinations = restrictionCombinationDao.search(pageQuery, name);
		
		Set<Long> restrictionIds = new HashSet<Long>();
		for(RestrictionCombination restrictionCombination : restrictionCombinations) {
			restrictionIds.add(restrictionCombination.getId());
		}
		
		//获取到Map<restrictId, productIds> & 获取所有productIds
		Set<Long> allProductIds = new HashSet<Long>();
		Map<Long, List<Long>> productIdsOfRestrictId = getProductsOfRestrictId(restrictionIds, allProductIds);
		
//		获取Map<productId, remainCount>
		Map<Long, Integer> remainCountOfProductId = productSKUService.remainCountOfProducts(allProductIds);
		
		//获取Map<restrictId, productNumber>
		Map<Long, Integer> productCountOfRestrictId = getProductCountOfRestrictIdMap(productIdsOfRestrictId, remainCountOfProductId);
		
        // 获取productIdsString Map<restrictId, productIdsString>
        Map<Long, String> proIdsStringByIds = clothesNosOfId(restrictionIds);

		List<RestrictionCombinationVO> combinationVOs = new ArrayList<RestrictionCombinationVO>();
 		for(RestrictionCombination restrictionCombination : restrictionCombinations) {
			RestrictionCombinationVO restrictionCombinationVO = new RestrictionCombinationVO();
			combinationVOs.add(restrictionCombinationVO);
			
			BeanUtils.copyProperties(restrictionCombination, restrictionCombinationVO);
			long restrictId = restrictionCombination.getId();
			restrictionCombinationVO.setProductCount(productCountOfRestrictId.get(restrictId) == null ? 0 : productCountOfRestrictId.get(restrictId));
            restrictionCombinationVO.setProductsToString(proIdsStringByIds.get(restrictionCombination.getId()));
        }
		
		return combinationVOs;
	}

    private Map<Long, Integer> getProductCountOfRestrictIdMap(Map<Long, List<Long>> productIdsOfRestrictId,
			Map<Long, Integer> remainCountOfProductId) {
		Map<Long, Integer> productNumOfRestrictId = new HashMap<Long, Integer>();
		for(Map.Entry<Long, List<Long>> entry : productIdsOfRestrictId.entrySet()) {
			long restrictId = entry.getKey();
			List<Long> productIds = entry.getValue();
			int totalCount = 0;
			for(Long productId : productIds) {
				Integer remainCount = remainCountOfProductId.get(productId);
				if(remainCount != null) {
					totalCount += remainCount;
				}
			}
			productNumOfRestrictId.put(restrictId, totalCount);
		}
		return productNumOfRestrictId;
	}

	public Map<Long, List<Long>> getProductsOfRestrictId(Collection<Long> restrictionIds, Set<Long> allProductIds) {
		Map<Long, List<Long>> productIdsOfRestrictId = new HashMap<Long, List<Long>>();
		Map<Long, List<Product>> productsOfRestrictId = productService.productsOfRestrctIdsMap(restrictionIds);
		for(Map.Entry<Long, List<Product>> entry : productsOfRestrictId.entrySet()) {
			long restrictId = entry.getKey();
			List<Product> products = entry.getValue();
			
			List<Long> productIds = new ArrayList<Long>();
			for(Product product : products) {
				long productId = product.getId();

				productIds.add(productId);
				allProductIds.add(productId);
			}
			productIdsOfRestrictId.put(restrictId, productIds);
		}
		
		return productIdsOfRestrictId;
	}

	public String clothesNosOfId(long restrictId) {
		List<Product> products = productMapper.productsOfRestrctIds(CollectionUtil.createList(restrictId));

        return assembleClothesNos(products);
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

    private Map<Long, String> clothesNosOfId(Collection<Long> restrictionIds) {
        Map<Long, List<Product>> products = productService.productsOfRestrctIdsMap(restrictionIds);

        Map<Long, String> clothesNoById = new HashMap<Long, String>();
        for (Map.Entry<Long, List<Product>> entry : products.entrySet()) {
            String clothesNos = assembleClothesNos(entry.getValue());
            clothesNoById.put(entry.getKey(), clothesNos);
        }

        return clothesNoById;
    }

	public int searchCount(String name) {
		return restrictionCombinationDao.searchCount(name);
	}
	
}
