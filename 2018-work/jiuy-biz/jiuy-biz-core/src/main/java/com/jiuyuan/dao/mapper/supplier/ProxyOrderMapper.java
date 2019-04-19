package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProxyCustomer;
import com.jiuyuan.entity.newentity.ProxyOrder;

@DBMaster
public interface ProxyOrderMapper extends BaseMapper<ProxyOrder> {

	Integer getSaleTotalCount(@Param("proxyUserNum")String proxyUserNum, @Param("proxyUserName")String proxyUserName, @Param("proxyUserFullName")String proxyUserFullName, @Param("proxyUserPhone")String proxyUserPhone, @Param("idCardNo")String idCardNo, @Param("proxyState")int proxyState);
	
}