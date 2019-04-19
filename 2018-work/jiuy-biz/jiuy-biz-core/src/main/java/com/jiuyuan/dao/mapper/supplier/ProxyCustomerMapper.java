package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProxyCustomer;

@DBMaster
public interface ProxyCustomerMapper extends BaseMapper<ProxyCustomer> {

	List<ProxyCustomer> list(@Param("customerName")String customerName, 
			                       @Param("customerPhone")String customerPhone, 
			                       @Param("status")int status, 
			                       @Param("maxSurplusDays")Long maxSurplusDays,
			                       @Param("minSurplusDays")Long minSurplusDays, 
			                       @Param("proxyUserId")long proxyUserId, 
			                       @Param("page")Page<Map<String,Object>> page);

	double getTotalConsumeAmount(@Param("storeId")long storeId, @Param("productCloseTime")long productCloseTime);

	Integer getProxyCustomerCount(@Param("proxyUserNum")String proxyUserNum,
			                  @Param("proxyUserName")String proxyUserName, 
			                  @Param("proxyUserFullName")String proxyUserFullName,
			                  @Param("proxyUserPhone")String proxyUserPhone, 
			                  @Param("idCardNo")String idCardNo,
			                  @Param("proxyState")int proxyState);
	
	
	
}