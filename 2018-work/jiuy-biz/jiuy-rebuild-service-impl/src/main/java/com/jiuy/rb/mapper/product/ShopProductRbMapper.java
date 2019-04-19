package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.ShopProductRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.product.ShopProductRbQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 商品信息表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月06日 下午 01:55:46
 * @Copyright 玖远网络 
 */
public interface ShopProductRbMapper extends BaseMapper<ShopProductRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    List<ShopProductRb> selectByIds(@Param("ids") List<String> ids);

    List<ShopProductRb> selectByIds(ShopProductRbQuery shopProductRbQuery);

    ShopProductRbQuery selectQuery(ShopProductRbQuery productQuery);
}
