package com.jiuy.core.service;

import java.util.List;

import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;

public interface UserService {
	
	public AdminUser login(AdminUser user);
	
	public int updateUserPassword(long userId, String oldPassword, String newPassword);
	/**
	 * 获取总注册人数
	 * @return
	 */
	public int getRegisterNum();
	
	public int registerCount(long startTime, long endTime);
	
	public int getPhoneRegisterNumByTime(long startTime, long endTime);
	
	public int getWeixinUserNumByTime(long startTime, long endTime);
	
//	public List<Long> ShenZhouUser();

	public List<AdminUser> search();

	public long searchStoreId(long serviceId);
	
	public int searchCountStoreId(long serviceId);

	public User getByYjjNumber(Long getyJJNumber);

	public long getYjjNumberById(long userId);

}
