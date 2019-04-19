package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.common.constant.factory.PageFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.core.business.assemble.CategoryAssemble;
import com.jiuy.core.dao.DiscountInfoDao;
import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.category.CategoryVO;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.DiscountInfoService;
import com.jiuy.core.service.ProductCategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.DiscountType;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.category.CategoryType;
import com.jiuyuan.dao.mapper.supplier.CategoryNewMapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyGroup;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.service.common.IDynamicPropertyCategoryService;

@Service
public class CategoryFacade {
	
	private static Logger logger = Logger.getLogger(CategoryFacade.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryAssemble categoryAssemble;
	
    @Autowired
    private DiscountInfoDao discountInfoDao;
    
    @Autowired
    private DiscountInfoService discountInfoService;
    
    @Autowired
	private IDynamicPropertyCategoryService dynamicPropertyCategoryService;
    
    @Autowired
    private CategoryNewMapper categoryNewMapper;
    
	@Transactional(rollbackFor = Exception.class)
	public ResultCode rmCategory(Long[] ids) {
		List<Long> list = new ArrayList<Long>();
		Collections.addAll(list, ids);
		
		//删除分类下是否还有子类
		for(Long id : list) {
			List<Category> categories = categoryService.getSubCat(id);
			if(categories.size() > 0) {
				return ResultCode.DELETE_ERROR_SUB_EXIST;
			}
		}
		//分类是否关联动态属性
		for (Long id : ids) {
			Wrapper<DynamicPropertyCategory> dynamicPropertyCategoryWrapper = new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", id);
			List<DynamicPropertyCategory> dynaPropCategoryList = dynamicPropertyCategoryService.selectList(dynamicPropertyCategoryWrapper);
			if (dynaPropCategoryList.size()>0) {
				return ResultCode.DELETE_ERROR_DYNAPROP_EXIST;
			}
		}
		
		//分类下是否包含商品
//		List<Long> productIds = productCategoryMapper.productsOfCategory(list);
		List<Long> productIds = productService.productsOfCategory(list);
		if(productIds.size() > 0) {
			return ResultCode.DELETE_ERROR_SUB_EXIST;
		}
		
		
		categoryService.rmCategoty(list);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public List<Map<String, Object>> getCategory(PageQuery query, int categoryType, String categoryName) {
		List<Map<String, Object>> caList = categoryService.getCategory(query, categoryType, categoryName);
		
		Set<Long> categoryIds = new HashSet<Long>();
		for(Map<String, Object> caMap : caList) {
			categoryIds.add((long)caMap.get("Id"));
		}
		
		Map<Long, List<ProductCategory>> productCatMap = productCategoryService.itemsOfCategoryIdMap(categoryIds);
		Map<Long, List<DiscountInfo>> discountOfCatMap = discountInfoService.itemsOfRelatedIdTypeMap(DiscountType.CATEGORY.getIntValue(), categoryIds);
		
		List<ProductCategory> items = productCategoryService.itemsOfCategoryIds(categoryIds);
		Set<Long> allProductIds = new HashSet<>();
		for(ProductCategory item : items) {
			allProductIds.add(item.getProductId());
		}
		
		for(Map<String, Object> caMap : caList) {
			long categoryId = (long)caMap.get("Id");

			List<ProductCategory> productCategories = productCatMap.get(categoryId);
			
			StringBuilder stringBuilder = new StringBuilder();
			if(productCategories != null) {
				for(ProductCategory productCategory : productCategories) {
					stringBuilder.append(productCategory.getProductId() + ",");
				}
			}
			
			if(stringBuilder.length() > 0) {
				stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
			}
			
			String virtualProducts = stringBuilder.toString();
			caMap.put("virtualProducts", virtualProducts);

            List<DiscountInfo> discountInfos = discountOfCatMap.get(categoryId);
            caMap.put("discount_info", discountInfos);
		}
		
		return caList;
	}

	public int getCategoryListCount(int categoryType, String categoryName) {
		return categoryService.getCategoryListCount(categoryType, categoryName);
	}

	public ResultCode hideShowCategory(long id) {
		int status = categoryService.getCategoryStatus(id) == 0? 1:0;
		
		categoryService.hideShowCategory(id, status);
		
		return ResultCode.COMMON_SUCCESS;
	}

    @Transactional(rollbackFor = Exception.class)
    public void addCategory(Category category, String discountInfos) {
    	
        categoryService.addCategory(category);

        //如果是虚拟分类，有优惠信息
        List<DiscountInfo> multipleDiscounts = JSON.parseArray(discountInfos, DiscountInfo.class);
        if(category.getCategoryType() == CategoryType.VIRTUAL.getIntValue()) {
        	for(DiscountInfo discountInfo : multipleDiscounts) {
                if (!isValidate(discountInfo)) {
                    throw new ParameterErrorException("满减不符合规则");
                }
        		discountInfo.setRelatedId(category.getId());
        	}
        	
        	discountInfoService.batchAdd(multipleDiscounts);
        }

        if (category.getCategoryType() == CategoryType.VIRTUAL.getIntValue() && multipleDiscounts.size() > 0) {
            category.setIsDiscount(1);
            category.setExceedMoney(multipleDiscounts.get(0).getFull());
            category.setMinusMoney(multipleDiscounts.get(0).getMinus());
        }

        categoryService.updateCategory(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultCode updateCategory(Category category, String discountInfos) {
    	

        List<DiscountInfo> multipleDiscounts = JSON.parseArray(discountInfos, DiscountInfo.class);
        if (category.getCategoryType() == CategoryType.VIRTUAL.getIntValue()) {
            discountInfoDao.delete(DiscountType.CATEGORY.getIntValue(), category.getId());
            for (DiscountInfo discountInfo : multipleDiscounts) {
                if (!isValidate(discountInfo)) {
                    throw new ParameterErrorException("满减不符合规则");
                }
            }
            discountInfoService.batchAdd(multipleDiscounts);
        }

        if (category.getCategoryType() == CategoryType.VIRTUAL.getIntValue() && multipleDiscounts.size() > 0) {
            category.setIsDiscount(1);
            category.setExceedMoney(multipleDiscounts.get(0).getFull());
            category.setMinusMoney(multipleDiscounts.get(0).getMinus());
        } else {
            category.setIsDiscount(1);
        }
        category.setUpdateTime(System.currentTimeMillis());

        categoryService.updateCategory(category);

		return ResultCode.COMMON_SUCCESS;
	}

    public boolean isValidate(DiscountInfo discountInfo) {
        
        double full = discountInfo.getFull();
        double minus = discountInfo.getMinus();
        
        if (full < minus) {
            return false;
        }
        
        if (full < 0 || minus < 0) {
            return false;
        }

        return true;
    }

	public List<Map<String, Object>> getTopCat(int categoryType) {
		List<Map<String, Object>> topCat = categoryService.getTopCat(categoryType);
		//返回没有绑定动态属性的分类
			 for (int i = 0; i < topCat.size(); i++) {
				 Wrapper<DynamicPropertyCategory> dynamicPropertyCategoryWrapper = new EntityWrapper<DynamicPropertyCategory>().eq("cate_id", (Long)topCat.get(i).get("Id"));
				 List<DynamicPropertyCategory> dynaPropCategoryList = dynamicPropertyCategoryService.selectList(dynamicPropertyCategoryWrapper);
		            if (dynaPropCategoryList.size()>0) {
		            	topCat.remove(i);
					}
		        }
		return topCat;
	}

	public ResultCode addVirtualProduct(long categoryId, String virtualProduct) {
		//批量逻辑删除原来的商品与虚拟分类的关联
        productCategoryMapper.rmRelatedProducts(categoryId);
		
		String[] productIdsStrs = StringUtils.split(virtualProduct, ",");
		Set<Long> productIds = new HashSet<Long>();
		for (String productIdStr : productIdsStrs) {
			Long productId = Long.parseLong(productIdStr);
			productIds.add(productId);
		}
		List<Product> products = productService.productOfIds(productIds);

		
		if(productIds.size() != products.size()) {
			
			return ResultCode.COMMON_ERROR_BAD_PARAMETER;
		}
		
		//插入新的关联数据,存在即更新Status
        if (productIds.size() > 0) {
            productCategoryMapper.addVirtualProduct(categoryId, productIds);
        }

		productService.batchDeleteVCategory(categoryId);
        productService.batchUpdate(null, categoryId, null, productIds);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public CategoryVO getCategoryById(long id) {
		CategoryVO categoryVO = new CategoryVO();
		StringBuilder virtualProducts = new StringBuilder();
		
		Category category = categoryAssemble.getCategoryById(id);
        if (category == null) {
            throw new ParameterErrorException("分类id 不存在!");
        }
		BeanUtils.copyProperties(category, categoryVO);
		
		Category parentCategory = categoryService.getCategoryById(category.getParentId());
		categoryVO.setParentName(parentCategory == null ? "" : parentCategory.getCategoryName());
		
		if(category.getCategoryType() == CategoryType.VIRTUAL.getIntValue()) {
			List<Long> productIds = new ArrayList<Long>(); 
			productIds = productCategoryMapper.productsOfCategory(CollectionUtil.createList(id));
			if(productIds.size() > 0) {
				List<Product> products = productService.productOfIds(productIds);
				for(Product product : products) {
					virtualProducts.append("," +product.getId());
				}
				//去掉首款号的逗号
				categoryVO.setVirtualProducts(virtualProducts.substring(1));
			} else {
				categoryVO.setVirtualProducts("");
			}
		}
		
		List<DiscountInfo> discountInfos = discountInfoDao.itemsOfRelatedIdType(DiscountType.CATEGORY.getIntValue(), CollectionUtil.createList(id));
		categoryVO.setDiscountInfo(discountInfos);
		
		return categoryVO;
	}

	public List<Category> getCategoryByType(Integer categoryType) {
		return categoryService.search(categoryType);
	}

	public Map<String, Object> underCouponSearch(long id) {
		Category category = categoryService.getCategoryById(id);
		if (category == null) {
			throw new ParameterErrorException("未找到分类，id为" + id);
		}
		
		long parentId = category.getParentId();
		Category parentCategory = null;
		if (parentId != 0) {
			parentCategory = categoryService.getCategoryById(parentId);
			parentCategory.getChildCategories().add(category);
		} else {
			parentCategory = category;
		}
    	Map<String, Object> data = new HashMap<>();
    	data.put("is_top", parentId == 0 ? "YES" : "NO");
    	data.put("list", parentCategory);
    	
    	return data;
	}
	/**
	 * 获取分类列表(新)
	 * @param page
	 * @param pageSize
	 * @param categoryId
	 * @param categoryId2 
	 * @param pageSize 
	 * @param categoryName
	 * @param categoryLevel
	 * @param parentCategoryId
	 * @param status
	 * @param weight
	 * @return
	 */
	public Map<String, Object> getCategoryList(int page, int pageSize, long categoryId, String categoryName,
			int categoryLevel, long parentCategoryId, int status, int weightMin, int weightMax) {
		boolean flag = false;
		Wrapper<CategoryNew> wrapper = new EntityWrapper<CategoryNew>().eq("CategoryType", 0).gt("Status", -1);//状态不能为删除
		if(categoryId>0){
			flag = true;
			wrapper.eq("Id", categoryId);
		}
		if(!StringUtils.isEmpty(categoryName)){
			flag = true;
			wrapper.like("CategoryName", categoryName);
		}
		if(categoryLevel > -1){
			flag = true;
			wrapper.eq("categoryLevel", categoryLevel);
		}
		if(parentCategoryId>-1){
			flag = true;
			wrapper.eq("ParentId", parentCategoryId);
		}
		if(status>-2){
			flag = true;
			wrapper.eq("Status", status);
		}
		if(weightMin>0){
			flag = true;
			wrapper.ge("Weight", weightMin);
		}
		if(weightMax>0){
			flag = true;
			wrapper.le("Weight", weightMax);
		}
		
		if(flag){//如果有条件则返回对应的分类列表
			
			return getConformingCategoryList(new Page(page,pageSize),wrapper);
		}else{//没有条件则返回所有分类列表
			return getTreeCategoryList(wrapper);
		}
	}

	/**
	 * 获取树状的分类列表
	 * @param wrapper
	 * @return
	 */
	private Map<String, Object> getTreeCategoryList(Wrapper<CategoryNew> wrapper) {
//		long beginTime = System.currentTimeMillis();
		Map<String,Object> data = new HashMap<String,Object>();
		List<CategoryNew> categoryList = categoryService.getCategoryList(wrapper.orderBy("Weight",false));//获取所有分类
		List<Map<String,Object>> categoryInfoList = this.encapsulationConformingCategoryList(categoryList);//分装所有分类
		List<Map<String,Object>> categoryFirstList = new ArrayList<Map<String,Object>>();//一级分类集合
		List<Map<String,Object>> secondCategoryList = new ArrayList<Map<String,Object>>();//二级分类集合
		List<Map<String,Object>> thirdCategoryList = new ArrayList<Map<String,Object>>();//三级分类集合
		for (Map<String, Object> categoryMap : categoryInfoList) {//遍历所有分类
			int categoryLevel = (int) categoryMap.get("categoryLevel");
			if(categoryLevel==1){//一级放入一级分类集合
				categoryFirstList.add(categoryMap);
			}else if(categoryLevel==2){//二级放入二级分类集合
				secondCategoryList.add(categoryMap);
			}else if(categoryLevel==3){//三级放入三级分类集合
				thirdCategoryList.add(categoryMap);
			}else if(categoryLevel==0){//等级未知
				logger.error("获取树状的分类列表:当前分类级别未知categoryId:"+categoryMap.get("categoryId"));
//				throw new RuntimeException("获取树状的分类列表:当前分类级别未知");
			}
		}
		Map<Long,Object> thirdCategoryMap = this.getChildCategoryMapOfParent(thirdCategoryList);//将三级根据父级id封装
		for (Map<String, Object> secondCategory : secondCategoryList) {//遍历所有的二级分类,将三级分类放入二级分类
			long categoryId = (long) secondCategory.get("categoryId");
			List<Map<String,Object>> thirdCategory = (List<Map<String, Object>>) thirdCategoryMap.get(categoryId);
			if(thirdCategory!=null && thirdCategory.size()>0){
				secondCategory.put("hasNextCategory", 1);//有下一级
			}else{
				secondCategory.put("hasNextCategory", 0);//没有下一级
			}
			secondCategory.put("thirdCategoryList", thirdCategory);
		}
		Map<Long,Object> secondCategoryMap = this.getChildCategoryMapOfParent(secondCategoryList);//将二级分类根据父级id进行封装
		for (Map<String, Object> firstCategory : categoryFirstList) {//遍历所有的一级分类,将二级分类放入一级分类
			long categoryId = (long) firstCategory.get("categoryId");
			List<Map<String,Object>> secondCategory = (List<Map<String, Object>>) secondCategoryMap.get(categoryId);
			if(secondCategory!=null && secondCategory.size()>0){
				firstCategory.put("hasNextCategory", 1);//有下一级
			}else{
				firstCategory.put("hasNextCategory", 0);//没有下一级
			}
			firstCategory.put("secondCategoryList", secondCategory);
		}
		data.put("categoryList", categoryFirstList);
//		long endTime = System.currentTimeMillis();
//		System.out.println(endTime-beginTime);
		return data;
	}

	/**
	 * 将分类列表根据上级分类id进行分装
	 * @param secondCategoryList
	 * @return
	 */
	private Map<Long, Object> getChildCategoryMapOfParent(List<Map<String, Object>> categoryList) {
		Map<Long,Object> categoryMap = new HashMap<Long,Object>();
		for (Map<String, Object> categoryInfo : categoryList) {
			long parentCategoryId = (long)categoryInfo.get("parentCategoryId");
			List<Map<String,Object>> categoryNewList = (List<Map<String, Object>>) categoryMap.get(parentCategoryId);
			if(categoryNewList==null){
				categoryNewList = new ArrayList<Map<String,Object>>();
				categoryNewList.add(categoryInfo);
				categoryMap.put(parentCategoryId, categoryNewList);
			}else{
				categoryNewList.add(categoryInfo);
				categoryMap.put(parentCategoryId, categoryNewList);
			}
		}
		return categoryMap;
	}

	/**
	 * 获取对应条件的分类列表
	 * @param page 
	 * @param wrapper
	 * @return
	 */
	private Map<String, Object> getConformingCategoryList(Page page, Wrapper<CategoryNew> wrapper) {
		Map<String,Object> data = new HashMap<String,Object>();
		int recordCount = categoryService.getConformingCategoryListCount(wrapper);
		List<CategoryNew> categoryList = categoryService.getConformingCategoryList(page,wrapper.orderBy("CreateTime", false).orderBy("Weight",false));
		List<Map<String,Object>> categoryInfoList = this.encapsulationConformingCategoryList(categoryList);
		data.put("categoryList", categoryInfoList);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(new PageQuery(page.getCurrent(), page.getSize()), queryResult);
		queryResult.setRecordCount(recordCount);
		data.put("total", queryResult);
		return data;
	}

	/**
	 * 封装对应的分类列表信息
	 * @param categoryList
	 * @return
	 */
	private List<Map<String, Object>> encapsulationConformingCategoryList(List<CategoryNew> categoryList) {
		List<Map<String,Object>> categoryInfoList = new ArrayList<Map<String,Object>>();
		Map<Long,String> parentCategoryMap = new HashMap<Long,String>();
		for (CategoryNew categoryNew : categoryList) {
			parentCategoryMap.put(categoryNew.getId(), categoryNew.getCategoryName());
		}
		for (CategoryNew categoryNew : categoryList) {
			Map<String,Object> categoryInfo = new HashMap<String,Object>();
			categoryInfo.put("categoryId", categoryNew.getId());//分类Id
			categoryInfo.put("categoryName", categoryNew.getCategoryName());//分类名称
			categoryInfo.put("categoryLevel", categoryNew.getCategoryLevel());//分类等级:0:未知;1:一级;2:二级;3:三级;
			long parentId = categoryNew.getParentId();
			categoryInfo.put("parentCategoryId", parentId);//上级分类Id
			CategoryNew categoryNew2 = categoryNewMapper.selectById(parentId);
			if(categoryNew2 != null){
			    categoryInfo.put("parentCategoryName", categoryNew2.getCategoryName());
			}else{
				categoryInfo.put("parentCategoryName", "");
			}
			categoryInfo.put("weight", categoryNew.getWeight());//分类权重
			categoryInfo.put("description", categoryNew.getDescription());//分类描述
			categoryInfo.put("status", categoryNew.getStatus());//状态:-1删除，0正常，1隐藏
			categoryInfoList.add(categoryInfo);
		}
		Map<Long,Object> categoryMap = this.getChildCategoryMapOfParent(categoryInfoList);//将三级根据父级id封装
		for (Map<String, Object> map : categoryInfoList) {
			List<Map<String,Object>> childCategoryList = (List<Map<String, Object>>) categoryMap.get((Long)map.get("categoryId"));
			if(childCategoryList==null || childCategoryList.size()<1){
        		map.put("noSon", 1);
        	}else{
        		map.put("noSon", 0);
        	}
		}
		return categoryInfoList;
	}

	public void addCategoryNew(int categoryLevel, long parentId, String categoryName, int status, String description,
			int weight) {
		CategoryNew category = new CategoryNew();
        category.setCategoryLevel(categoryLevel);
        category.setParentId(parentId);
        category.setCategoryName(categoryName);
        category.setStatus(status);
        category.setDescription(description);
        category.setWeight(weight);
        category.setCreateTime(System.currentTimeMillis());
        category.setUpdateTime(category.getCreateTime());
        categoryService.addCategoryNew(category);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateCategoryNew(long categoryId, long parentId, String categoryName, int status, String description,
			int weight) {
		CategoryNew categoryById = categoryService.getCategoryByIdNew(categoryId);
		long parentIdOld = categoryById.getParentId();
		CategoryNew category = new CategoryNew();
		category.setId(categoryId);
        category.setParentId(parentId);
        category.setCategoryName(categoryName);
        category.setStatus(status);
        category.setDescription(description);
        category.setWeight(weight);
        category.setUpdateTime(System.currentTimeMillis());
        categoryService.updateCategoryNew(category);
        //修改商品绑定的分类信息
        categoryService.updateProductCateGoryInfo(categoryId, categoryName);
       
        if(parentId!=parentIdOld){//上级分类修改了
        	CategoryNew categoryParent = categoryService.getCategoryByIdNew(parentId);
        	long categoryParentId = categoryParent.getId();
        	int categoryLevel = categoryById.getCategoryLevel();
            if(categoryLevel==2){//如果是二级分类则修改对应的一级分类
            	categoryService.updateProductFirstCategoryInfo(categoryParentId,categoryParent.getCategoryName(),categoryId);
            }else if(categoryLevel==3){//如果是三级分类则修改对应的二级分类和一级分类
            	long firstParentCategoryId = categoryParent.getParentId();
            	categoryService.updateProductSecondCategoryInfo(categoryParentId,categoryParent.getCategoryName(),categoryId);//修改二级
            	categoryParent = categoryService.getCategoryByIdNew(firstParentCategoryId);
            	categoryService.updateProductFirstCategoryInfo(categoryParent.getId(),categoryParent.getCategoryName(),firstParentCategoryId);//修改三级
            }
        }
	}

//	/**
//	 * 获取分类信息
//	 * @param categoryId
//	 * @return
//	 */
//	public Map<String, Object> getCategoryInfo(long categoryId) {
//		Map<String,Object> data = new HashMap<String,Object>();
//		CategoryNew categoryNew = categoryService.getCategoryByIdNew(categoryId);
//		Map<String,Object> categoryInfo = new HashMap<String,Object>();
//		categoryInfo.put("categoryId", categoryNew.getId());//分类Id
//		categoryInfo.put("categoryName", categoryNew.getCategoryName());//分类名称
//		categoryInfo.put("categoryLevel", categoryNew.getCategoryLevel());//分类等级:0:未知;1:一级;2:二级;3:三级;
//		long parentId = categoryNew.getParentId();
//		categoryInfo.put("parentCategoryId", parentId);//上级分类Id
//		categoryInfo.put("weight", categoryNew.getWeight());//分类权重
//		categoryInfo.put("status", categoryNew.getStatus());//状态:-1删除，0正常，1隐藏
//		data.put("categoryInfo", categoryInfo);
//		return data;
//	}

	/**
	 * 获取对应等级id和名称
	 * @param categoryLevel
	 * @return
	 */
	public Map<String, Object> getParentCategoryId(int categoryLevel) {
		Map<String,Object> data = new HashMap<String,Object>();
		Wrapper<CategoryNew> wrapper = new EntityWrapper<CategoryNew>().eq("categoryLevel", categoryLevel).eq("Status", 0);
		List<CategoryNew> categoryNewList = categoryService.getCategoryList(wrapper);
		List<Map<String,Object>> categoryIdList = new ArrayList<Map<String,Object>>();
		for (CategoryNew categoryNew : categoryNewList) {
			Map<String,Object> categoryId = new HashMap<String,Object>();
			categoryId.put("categoryId", categoryNew.getId());
			categoryId.put("categoryName", categoryNew.getCategoryName());
			categoryIdList.add(categoryId);
		}
		data.put("categoryIdList", categoryIdList);
		return data;
	}

}