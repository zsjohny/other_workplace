package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.CategoryRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.product.CategoryRbQuery;

/** 
 * 商品分类表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月14日 下午 03:54:04
 * @Copyright 玖远网络 
 */
public interface CategoryRbMapper extends BaseMapper<CategoryRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 查询最大的code
     *
     * @param query 查询实体
     * @author Aison
     * @date 2018/6/14 10:08
     * @return String
     */
    String selectMaxCode(CategoryRbQuery query);

}
