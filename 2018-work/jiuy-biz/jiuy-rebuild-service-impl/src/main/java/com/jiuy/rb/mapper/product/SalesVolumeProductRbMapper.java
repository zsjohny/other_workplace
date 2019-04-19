package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.SalesVolumeProductRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.product.SalesVolumeProductRbQuery;
import org.apache.ibatis.annotations.Param;

/** 
 * 商品销量 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月15日 下午 07:34:35
 * @Copyright 玖远网络 
 */
public interface SalesVolumeProductRbMapper extends BaseMapper<SalesVolumeProductRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 添加销量
     *
     * @param salesVolume salesVolume
     * @param productId productId
     * @param productType productType
     * @author Aison
     * @date 2018/6/15 19:53
     */
    int addSalesVolume(@Param("salesVolume") Long salesVolume,@Param("productType") Integer productType,@Param("productId") Long productId );

    /**
     * 增加销量,收藏数,评论数,浏览数...
     * <p>null则不操作</p>
     *
     * @param query 修改信息封装
     * @return int
     * @author Charlie
     * @date 2018/8/6 9:00
     */
    int safeAddCount(SalesVolumeProductRb query);
}
