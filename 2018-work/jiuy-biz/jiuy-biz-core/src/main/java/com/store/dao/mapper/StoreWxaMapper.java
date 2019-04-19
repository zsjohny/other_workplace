package com.store.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.store.StoreWxa;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 门店小程序信息记录表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-05
 */
@DBMaster
public interface StoreWxaMapper extends BaseMapper<StoreWxa> {

    List<StoreWxa> selectStoreWxa(@Param("storeId")Long storeId);

    List<StoreWxa> selectByAppId(@Param("appId")String appId);

}