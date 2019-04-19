package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.store.entity.ShopCategory;


@DBMaster
public interface CategoryNewMapper extends BaseMapper<CategoryNew> {

	List<ShopCategory> getOneShopCategoryList();
	



	
}
