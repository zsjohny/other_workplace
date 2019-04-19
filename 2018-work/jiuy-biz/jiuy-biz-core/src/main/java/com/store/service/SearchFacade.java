/**
 * 
 */
package com.store.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.SearchConstants;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.product.ProductTagVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.search.SearchProductVO;
import com.jiuyuan.entity.search.SearchWeight;
import com.jiuyuan.entity.visit.UserVisitHistory;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.ProductUtil;
import com.store.entity.Brand;
import com.store.entity.ProductCategoryVO;
import com.store.entity.StoreProductSKUsPropVO;
import com.store.service.brand.ShopBrandService;
import com.yujj.ext.search.LuceneHolder;







//@Service
public class SearchFacade {
	
	@Autowired
	private LuceneHolder luceneHolder;

	@Autowired
	private ProductServiceShop productService;
	

	@Autowired
	private ProductSKUService productSKUService;

	@Autowired
	private PropertyServiceShop propertyService; 
	
	@Autowired
	private ProductPropertyService productPropertyService; 
	
	@Autowired
	private ShopBrandService shopBrandService;
	
	@Autowired
	private ProductTagService productTagService;

    @Autowired
    private StoreSearchLogService storeSearchLogService;

    @Autowired
    private SearchKeywordService searchKeywordService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private VisitService visitService;
    
    @Autowired
    private StoreBusinessServiceShop storeBusinessService;
	
	@Autowired
	ShopGlobalSettingService globalSettingService;
		
//	public List<SearchProductVO> search(String keyWords, int pageNo, int pageSize,
//			double begin_price, double end_price, ScoreDoc after, Sort sort, SearchWeight weight){
//		
//		return luceneHolder.search(keyWords, pageNo, pageSize, begin_price, end_price, after, sort, weight);
//	}
		
	public Map<String, Object> getSearchRecommendProduct(UserDetail userDetail) {
    	Map<String, Object> map = new HashMap<String, Object>();
//    	List<ProductVO> productList = dataminingAdapter.getBuyGuessProduct186(userDetail, new PageQuery(1, 4));
    	
//    	map.put("title", "猜你喜欢");
//    	map.put("productList", ProductUtil.getProductSimpleList(productList));
    	
    	return map;
    }
	
	public Map<String,Object> search(String keyWords, int pageNo, int pageSize, long userId, SortType iSort, int guideFlag) {
		if (StringUtils.isBlank(keyWords))	
			return null;
		
		SearchWeight weight = globalSettingService.getBean(GlobalSettingName.SEARCH_WEIGHT_SETTING, SearchWeight.class);
				
		storeSearchLogService.addUserSearchLog(keyWords, userId);

		Map<String,Object> data = null;
        
		try {		
			
			double percent = 0;
			if (userId != 0) {
				percent =  storeBusinessService.getPercentById(userId);
			}
			
			Map<String, String> filterMaps = new HashMap<String, String>();			
			if (guideFlag != 1) {
				String brandIds = productService.getStoreAvailableBrandStr(userId);
				filterMaps.put("brandIds", brandIds);
			}
			
			data = luceneHolder.search(keyWords, pageNo*pageSize, pageSize, getSort(iSort), weight);
//			data = luceneHolder.search(keyWords, pageNo*pageSize, pageSize, getSort(iSort), weight, percent, filterMaps, guideFlag);  //store
			
		} catch (IOException | ParseException | InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		searchKeywordService.addSearchKeyword(StringUtils.split(keyWords, " "), (int)data.get("totalHits"), 0);
		return data;
	}
	
	private Sort getSort(SortType iSort) {
		
		if (iSort == null) return null;
		
		SortField[] sortField = null;
		SortField weightFieldDesc = new SortField(SearchConstants.FWEIGHT, Type.INT, true);
		SortField weightFieldAsc = new SortField(SearchConstants.FWEIGHT, Type.INT, false);
		switch (iSort) {
		case CREATE_TIME_DESC: sortField = new SortField[]{weightFieldDesc,new SortField(SearchConstants.FCREATETIME, Type.LONG, true)};break;
		case CREATE_TIME_ASC: sortField = new SortField[]{weightFieldDesc,new SortField(SearchConstants.FCREATETIME, Type.LONG, false)};break;
		case PRICE_DESC: sortField = new SortField[]{new SortField(SearchConstants.FCASH, Type.DOUBLE, true),weightFieldDesc};break;
		case PRICE_ASC: sortField = new SortField[]{new SortField(SearchConstants.FCASH, Type.DOUBLE, false),weightFieldDesc};break;
		case WEIGHT_DESC: sortField = new SortField[]{weightFieldDesc};break;
		case WEIGHT_ASC: sortField = new SortField[]{weightFieldAsc};break;
		case CREATE_DESC: sortField = new SortField[]{new SortField(SearchConstants.FCREATETIME, Type.LONG, true),weightFieldDesc};break;
		case CREATE_ASC: sortField = new SortField[]{new SortField(SearchConstants.FCREATETIME, Type.LONG, false),weightFieldDesc};break;
		case VISIT_DESC: sortField = new SortField[]{new SortField(SearchConstants.FVISITCOUNT, Type.INT, true),weightFieldDesc};break;
		case VISIT_ASC: sortField = new SortField[]{new SortField(SearchConstants.FVISITCOUNT, Type.INT, false),weightFieldDesc};break;
		case SALES_DESC: sortField = new SortField[]{new SortField(SearchConstants.FSALETOTALCOUNT, Type.INT, true),weightFieldDesc};break;
		case SALES_ASC: sortField = new SortField[]{new SortField(SearchConstants.FSALETOTALCOUNT, Type.INT, false),weightFieldDesc};break;
		default:
			return null;
		}
		
		return new Sort(sortField);		
	}

//	@PostConstruct
	public boolean ReCreateIndex() {

		luceneHolder.deleteAllIndex(true);
        
		return luceneHolder.writeIndex(CreateSourceData());
	}
	
	private List<SearchProductVO> CreateSourceData() {
		List<SearchProductVO> list = new ArrayList<SearchProductVO>();		
		
		List<StoreProductSKUsPropVO> productProps = productSKUService.getProductSKUsPropVO();
		Map<Long, Product> productMaps = productService.getProducts();
		Map<Long, Brand> brandMaps = shopBrandService.getBrands();
		Map<Long, ProductPropValue> propValueMaps = propertyService.getValues();
		Map<Long, ProductProp> seasonPropMaps = productPropertyService.getSeasonProps();
		Map<Long, ProductTagVO> productTagMaps = productTagService.getProductTagNames();
		Map<Long, ProductCategoryVO> productCategoryMaps = productCategoryService.getProductCategorys();
		Map<Long, UserVisitHistory> visitHistoryMaps = visitService.getVisits();
		
		for (StoreProductSKUsPropVO propVO : productProps) {
			long productId = propVO.getProductId();
			
			SearchProductVO vo = new SearchProductVO();
			
			Product product = productMaps.get(productId);
			if (product == null) continue;
			Map<String, String> propValues = propVO.getPropValues(propValueMaps);
			
			vo.setId(product.getId());
			vo.setBrandId(product.getBrandId());
			String[] images = product.getDetailImageArray();
			String image = images != null && images.length > 0 ? images[0] : "";
			vo.setImage(image);
			vo.setName(product.getName());
			vo.setMarketPrice(product.getMarketPrice());
			vo.setSubscriptLogo(product.getSubscriptLogo());
			vo.setCash(product.getCash());
			vo.setCurrentCash(product.getCurrenCash());
			vo.setIsPromotion(product.getIsPromotion());
			vo.setPromotionCash(product.getPromotionCash());
			vo.setJiuCoin(product.getJiuCoin());
			vo.setCreateTime(product.getCreateTime());
			vo.setWeight(product.getWeight());
			vo.setSaleTotalCount(product.getSaleTotalCount());
			vo.setType(product.getType());

			UserVisitHistory productVisitHistory = visitHistoryMaps.get(productId);
			if (productVisitHistory != null) {
				vo.setVisitCount(productVisitHistory.getCount()+product.getPromotionVisitCount());

		//		System.out.println("categoryVo.getCategoryNames():"+categoryVo.getCategoryNames());
			}
			vo.setfTitle(product.getName());
			Brand brand = brandMaps.get(product.getBrandId());
			if (brand != null) {
				vo.setfBrandName(brand.getBrandName());
				vo.setfBrandCnName(brand.getCnName());
				vo.setfBrandWeight(brand.getWeight());
			//	System.out.println("brand.getBrandName():"+brand.getBrandName() + " brand.getWeight():" + brand.getWeight());
			};			
			vo.setfColor(propValues.get("propColors"));
			vo.setfSize(propValues.get("propSizes"));

			ProductCategoryVO categoryVo = productCategoryMaps.get(productId);
			if (categoryVo != null) {
				vo.setfCategoryName(categoryVo.getCategoryNames());
				vo.setfCategoryWeight(categoryVo.getMaxWeight());
	//			System.out.println("categoryVo.getCategoryNames():"+categoryVo.getCategoryNames() + " categoryVo.getMaxWeight():" + categoryVo.getMaxWeight());
			}

			
			ProductTagVO productTagVO = productTagMaps.get(productId);
			vo.setfTag(productTagVO == null ? "" : productTagVO.getNames());
			
			ProductProp propertyValue = seasonPropMaps.get(productId);
			
			if (propertyValue != null) {
				long propertyValueId = propertyValue.getPropertyValueId();				
				vo.setfSeason(propValueMaps.get(propertyValueId).getPropertyValue());
			}
			
//			System.out.println(vo.toString());
			
			list.add(vo);
		}
		
		return list;
	}
	
}
