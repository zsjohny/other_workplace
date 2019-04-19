package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.RestrictionActivityProductRb; 
import com.jiuy.base.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/** 
 * 限购活动商品表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月15日 下午 05:06:25
 * @Copyright 玖远网络 
 */
public interface RestrictionActivityProductRbMapper extends BaseMapper<RestrictionActivityProductRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 查询活动商品
     *
     * @param param param
     * @author Aison
     * @date 2018/6/22 9:59
     */
    List<RestrictionActivityProductRb> selectFilterProduct(Map<String,Object> param);


}