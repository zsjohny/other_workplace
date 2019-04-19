package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.ExpressInfo;

@DBMaster
public interface ExpressInfoMapper {

    @MapKey("orderItemGroupId")
    Map<Long, ExpressInfo> getExpressInfoMap(@Param("orderItemGroupIds") Collection<Long> orderItemGroupIds);

    ExpressInfo getUserExpressInfo(@Param("userId") long userId, @Param("orderItemGroupId") long orderItemGroupId);
    
    ExpressInfo getUserExpressInfoByOrderNo(@Param("userId") long userId, @Param("orderNo") long orderNo);

}
