package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProxyUser;

@DBMaster
public interface ProxyUserMapper extends BaseMapper<ProxyUser> {

	Integer getStockTotalCount(@Param("proxyUserNum")String proxyUserNum, @Param("proxyUserName")String proxyUserName, @Param("proxyUserFullName")String proxyUserFullName, @Param("proxyUserPhone")String proxyUserPhone, @Param("idCardNo")String idCardNo, @Param("proxyState")int proxyState);
	
	
	
}