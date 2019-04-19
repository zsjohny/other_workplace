package com.store.dao.mapper;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.store.entity.BusinessInformation;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 商家信息表 Mapper 接口
 * </p>
 *
 * @author Hyf
 * @since 2018-08-15
 */
@DBMaster
public interface BusinessInformationMapper extends BaseMapper<BusinessInformation> {


    BusinessInformation selectByUserId(@Param("userId")Long userId);

    StoreBusiness selectStoreBusiness(@Param("storeId")Long storeId);
}