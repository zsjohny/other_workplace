package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;


import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.GroundUser;

public interface IGroundUserService {

	/**
	 * 加盐参数
	 */
	String hashAlgorithmName = "MD5";
	/**
	 * 循环次数
	 */
	int hashIterations = 1024;

	List<Map<String, Object>> listPage(Page<Map<String, Object>> page, String name,long id, String phone, int userType,
			String province, String city, String district, String pname, String pphone, Integer minClientCount,
			Integer maxClientCount, Double minTotalSales, Double maxTotalSales, String managerId);

	void add(int status, String name, String phone, int userType, long pid, String pphone, String ppname, int puserType, String province,
			String city, String district, String bankAccountNo, String bankAccountName, String bankName);

	//获取随机盐值
	String getRandomSalt(int length);

	Map<String, Object> getSupervisorInfo(int userType, String phone);
	
	GroundUser getGroundUserByPhone(String phone);
	
	GroundUser getGroundUserByPhoneWithAllStatus(String phone);

	List<Map<String, Object>> count(String name,long id , String phone, int userType, String province, String city,
			String district, String pname, String pphone, Integer minClientCount, Integer maxClientCount,
			Double minTotalSales, Double maxTotalSales, String managerId);


}