package com.jiuyuan.dao.mapper.store;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.SalesVolumeProduct;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;

/**
 * <p>
  * 商品销量 Mapper 接口
 * </p>
 *
 * @author Aison
 * @since 2018-06-19
 */
@DBMaster
public interface SalesVolumeProductMapper extends BaseMapper<SalesVolumeProduct> {

    /**
     * 获取一批商品的统计信息
     *
     * @param param param
     * @author Aison
     * @date 2018/6/19 22:15
     * @return Map
     */
    @MapKey("productId")
    Map<Long,SalesVolumeProduct> selectProductMonitor(Map<String,Object> param);

}