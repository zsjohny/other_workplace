package com.jiuy.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private AdminUserDao adminUserDao;
	

	@Override
	public AdminUser login(AdminUser user) {
		return adminUserDao.getByProperty(user);
	}

    @Override
    public int updateUserPassword(long userId, String oldPassword, String newPassword) {
        return adminUserDao.updateUserPassword(userId, oldPassword, newPassword);
    }

	@Override
	public int getRegisterNum() {
		return adminUserDao.getRegisterNum();
	}

	@Override
	public int registerCount(long startTime, long endTime) {
		return adminUserDao.registerCount(startTime, endTime);
	}

	@Override
	public int getPhoneRegisterNumByTime(long startTime, long endTime) {
		return adminUserDao.getPhoneRegisterNumByTime(startTime, endTime);
	}

	@Override
	public int getWeixinUserNumByTime(long startTime, long endTime) {
		return adminUserDao.getWeixinUserNumByTime(startTime, endTime);
	}

//	@Override
//	public List<Long> ShenZhouUser() {
//		return adminUserDao.ShenZhouUser();
//	}

	@Override
	public List<AdminUser> search() {
		return adminUserDao.search();
	}

	@Override
	public long searchStoreId(long serviceId) {
		return adminUserDao.searchStoreId(serviceId);
	}

	@Override
	public int searchCountStoreId(long serviceId) {
		return adminUserDao.searchCountStoreId(serviceId);
	}

	@Override
	public User getByYjjNumber(Long yjjNumber) {
		return adminUserDao.getByYjjNumber(yjjNumber);
	}

	@Override
	public long getYjjNumberById(long userId) {
		return adminUserDao.getYjjNumberById(userId);
	}

}
