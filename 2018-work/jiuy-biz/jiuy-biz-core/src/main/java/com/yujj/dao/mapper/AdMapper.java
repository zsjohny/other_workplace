package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.ad.AdEnum;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.AdConfig;

@DBMaster
public interface AdMapper {

    List<AdConfig> getAdsByType(@Param("adType") AdEnum adType);
}
