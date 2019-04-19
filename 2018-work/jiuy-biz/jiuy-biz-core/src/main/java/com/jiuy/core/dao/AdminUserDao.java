package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.query.PageQuery;

public interface AdminUserDao {
	
	public AdminUser getByProperty(AdminUser user);

    public AdminUser getUser(long userId);

    public int updateUserPassword(long userId, String oldPassword, String newPassword);

	public int getRegisterNum();

	public int registerCount(long startTime, long endTime);

	public int getPhoneRegisterNumByTime(long startTime, long endTime);

	public int getWeixinUserNumByTime(long startTime, long endTime);

	public int add(AdminUser adminUser);

	public int remove(long userId);

    public List<AdminUser> search(AdminUser adminUser, PageQuery query);

	public int resetPassword(long userId, String password);

	public int searchCount(AdminUser adminUser);

	public List<AdminUser> search();

	public List<Map<String, Object>> getCountOfRole();

	public int update(long userId, long roleId, String userPhone);
	
	public long searchStoreId(long serviceId);

	public int searchCountStoreId(long serviceId);

	public int updateHttpUrl(Map<String, String> map);

	public User getByYjjNumber(Long yjjNumber);

	public long getYjjNumberById(long userId);

}
