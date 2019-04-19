package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.ShopProductRbTempInfo; 
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/** 
 * 商品临时信息表,用来存放小程序商品草稿状态时临时信息 的mapper 文件
 
 * @author Think
 * @version V1.0 
 * @date 2018年09月06日 下午 04:02:56
 * @Copyright 玖远网络 
 */
public interface ShopProductRbTempInfoMapper extends BaseMapper<ShopProductRbTempInfo>{

    // @Costom
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    int deleteByShopProductId(@Param ("shopProductId") Long shopProductId);
}