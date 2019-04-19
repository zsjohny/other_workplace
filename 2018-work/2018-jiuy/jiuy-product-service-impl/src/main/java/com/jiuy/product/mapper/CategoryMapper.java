package com.jiuy.product.mapper; 
 
import com.jiuy.product.model.Category; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.product.model.CategoryQuery;

/** 
 * 商品分类表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月05日 下午 10:36:31
 * @Copyright 玖远网络 
 */
public interface CategoryMapper extends BaseMapper<Category>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 查询最大的code
     * @author Aison
     * @date 2018/6/5 18:54
     */
    String selectMaxCode(CategoryQuery query);

}
