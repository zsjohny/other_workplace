package com.e_commerce.miscroservice.distribution.mapper;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/16 17:17
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberOrderDstbRecordMapper{



    /**
     * 新增
     *
     * @param orderRecord 实体
     * @return int
     * @author Charlie
     * @date 2018/10/16 17:25
     */
    int insertSelective(ShopMemberOrderDstbRecord orderRecord);


    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/10/16 19:02
     */
    int updateByPrimaryKeySelective(ShopMemberOrderDstbRecord updInfo);
}
