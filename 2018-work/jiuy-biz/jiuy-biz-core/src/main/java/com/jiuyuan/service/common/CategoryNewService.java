package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.Status;
import com.jiuyuan.dao.mapper.supplier.CategoryNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductCategoryNewMapper;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.entity.newentity.ProductCategoryNew;
import com.jiuyuan.util.A;
import com.jiuyuan.util.IdsToStringUtil;
import com.store.entity.ShopCategory;

@Service
public class CategoryNewService implements ICategoryNewService {
	private static final int ONE_CATEGORY = 0;
	
	private static final int PRODUCT_CATEGORY = 0;
   @Autowired
   private CategoryNewMapper categoryNewMapper;
   
   @Autowired
   private ProductCategoryNewMapper productCategoryNewMapper;
   
   
   @Override
   public List<CategoryNew> getProductCategoryList() {
	    Wrapper<CategoryNew> wrapper = new EntityWrapper<CategoryNew>();
	    wrapper.eq("Status",0);//状态:-1删除，0正常，1隐藏
	    wrapper.eq("CategoryType",0);//分类类型：0产品分类，1商家分类，2：虚拟分类
	    wrapper.orderBy("Weight", false);//分类权重,相同的parentId情况下,根据权重来排序
		List<CategoryNew> list = categoryNewMapper.selectList(wrapper);
		return list;
	}
   
   /**
    * 获取一级分类列表
    */
   public List<CategoryNew> getOneCategoryList() {
	    Wrapper<CategoryNew> wrapper = new EntityWrapper<CategoryNew>();
	    wrapper.eq("ParentId",0);//父ID
	    wrapper.eq("Status",0);//状态:-1删除，0正常，1隐藏
	    wrapper.eq("CategoryType",0);//分类类型：0产品分类，1商家分类，2：虚拟分类
	    wrapper.orderBy("Weight", false);//分类权重,相同的parentId情况下,根据权重来排序
		List<CategoryNew> list = categoryNewMapper.selectList(wrapper);
		return list;
   }

/* (non-Javadoc)
 * @see com.jiuyuan.service.common.ICategoryNewService#getPotentialCategoryList(java.lang.String)
 */
@Override
public List<Map<String, Object>> getPotentialCategoryList(String productId) {
	//获取所有隐藏分类，只获取一级分类
	Wrapper<CategoryNew> categoryNewwrapper = new EntityWrapper<CategoryNew>();
	categoryNewwrapper.eq("Status", Status.HIDE.getIntValue()).eq("ParentId", ONE_CATEGORY ).eq("CategoryType", PRODUCT_CATEGORY);
	List<CategoryNew> list = categoryNewMapper.selectList(categoryNewwrapper);
	//获取被选中的该商品分类
	Wrapper<ProductCategoryNew> productCategoryNewWrapper = new EntityWrapper<ProductCategoryNew>();
	productCategoryNewWrapper.eq("ProductId", productId).eq("Status", Status.NORMAL.getIntValue());
	List<ProductCategoryNew> selectedList = productCategoryNewMapper.selectList(productCategoryNewWrapper);
	List<Long> selectedIdList = new ArrayList<Long>();
	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
	//找出隐藏分类ID
	for(ProductCategoryNew productCategoryNew : selectedList){
		Long categoryId = productCategoryNew.getCategoryId();
		selectedIdList.add(categoryId);
	}
	//所有的隐藏分类ID
	for(CategoryNew categoryNew : list){
		long categoryId = categoryNew.getId();
		String categoryName = categoryNew.getCategoryName();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("categoryName", categoryName);
		map.put("categoryId", categoryId);
		if(selectedIdList.contains(categoryId)){
			map.put("selected", true);
		}else{
			map.put("selected", false);
		}
		resultList.add(map);
	}
	
	return resultList;
}
/* (non-Javadoc)
 * @see com.jiuyuan.service.common.ICategoryNewService#updatePotentialCategory(java.lang.String, java.lang.String)
 */
@Override
@Transactional(rollbackFor = Exception.class)
public void updatePotentialCategory(String productId, String categoryIds) {
	//先删除当前商品分类关系表中，该商品的隐藏分类
	//获取所有隐藏分类,只获取一级分类
	Wrapper<CategoryNew> categoryNewwrapper = new EntityWrapper<CategoryNew>();
	categoryNewwrapper.eq("Status", Status.HIDE.getIntValue()).eq("ParentId", ONE_CATEGORY ).eq("CategoryType", PRODUCT_CATEGORY);
	List<CategoryNew> list = categoryNewMapper.selectList(categoryNewwrapper);
	List<Long> idList = new ArrayList<Long>();
	for(CategoryNew categoryNew:list){
		idList.add(categoryNew.getId());
	}
	//删除该商品的一级分类
	Wrapper<ProductCategoryNew> productCategoryNewWrapper = new EntityWrapper<ProductCategoryNew>();
	productCategoryNewWrapper.in("CategoryId", idList).eq("ProductId", Long.parseLong(productId));
	int i = productCategoryNewMapper.delete(productCategoryNewWrapper);
	//添加当前商品隐藏分类
	List<String> categoryIdList = IdsToStringUtil.getIdsToListNoKomma(categoryIds);
	if(categoryIdList == null || categoryIdList.size() == 0){
		return;
	}
	for(String categoryId :categoryIdList){
		ProductCategoryNew productCategoryNew = new ProductCategoryNew();
		productCategoryNew.setProductId(Long.parseLong(productId));
		productCategoryNew.setCategoryId(Long.parseLong(categoryId));
		productCategoryNew.setCreateTime(System.currentTimeMillis());
		productCategoryNew.setUpdateTime(System.currentTimeMillis());
		productCategoryNew.setStatus(Status.NORMAL.getIntValue());
		productCategoryNewMapper.insert(productCategoryNew);
	}
}

	@Override
	public CategoryNew getById(long targetId) {
		return categoryNewMapper.selectById(targetId);
	}

}
