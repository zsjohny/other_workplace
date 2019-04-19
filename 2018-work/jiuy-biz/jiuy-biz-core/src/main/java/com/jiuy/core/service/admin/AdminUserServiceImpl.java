package com.jiuy.core.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class AdminUserServiceImpl implements AdminUserService {
	
	//初始化密码
	public final String DEFAUT_PASSWORD = "123456";
	
	@Resource
	private AdminUserDao adminUserDaoSqlImpl;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(AdminUser adminUser) {
		long currentTime = System.currentTimeMillis();
		
		adminUser.setUserPassword(DigestUtils.md5Hex(DEFAUT_PASSWORD));
		adminUser.setCreateTime(currentTime);
		adminUser.setUpdateTime(currentTime);
		
		return adminUserDaoSqlImpl.add(adminUser);
	}

	@Override
	public int update(long userId, long roleId, String userPhone) {
		return adminUserDaoSqlImpl.update(userId, roleId, userPhone);
	}

	@Override
	public int remove(long userId) {
		return adminUserDaoSqlImpl.remove(userId);
	}

	@Override
	public int resetPassword(long userId) {
		String password = DigestUtils.md5Hex(DEFAUT_PASSWORD);
		
		return adminUserDaoSqlImpl.resetPassword(userId, password);
	}

	@Override
	public List<AdminUser> search(PageQuery query, Long userId, String userName, Long roleId, String userPhone) {
		AdminUser adminUser = new AdminUser();

        adminUser.setUserId(userId);
        adminUser.setUserName(userName);
        adminUser.setRoleId(roleId);
        adminUser.setUserPhone(userPhone);

        return adminUserDaoSqlImpl.search(adminUser, query);
	}

	@Override
	public int searchCount(Long userId, String userName, Long roleId, String userPhone) {
		AdminUser adminUser = new AdminUser();

        adminUser.setUserId(userId);
        adminUser.setUserName(userName);
        adminUser.setRoleId(roleId);
        adminUser.setUserPhone(userPhone);
        
		return adminUserDaoSqlImpl.searchCount(adminUser);
	}

	@Override
	public Map<Long, Integer> getCountOfRole() {
		Map<Long, Integer> countOfRole = new HashMap<>();
		
		List<Map<String, Object>> list = adminUserDaoSqlImpl.getCountOfRole();
		for (Map<String, Object> map : list) {
			Integer count = Integer.parseInt(map.get("count").toString());
			Long roleId = Long.parseLong(map.get("RoleId").toString());
			countOfRole.put(roleId, count);
		}
		return countOfRole;
	}

	@Override
	public int updateHttpUrl(String primaryKey, long id, String columnName, String newUrl, String tableName) {
		Map<String, String> map = new HashMap<>();
		map.put("primaryKey", primaryKey);
		map.put("id", String.valueOf(id));
		map.put("columnName", columnName);
		map.put("newUrl", newUrl);
		map.put("tableName", tableName);
		return adminUserDaoSqlImpl.updateHttpUrl(map);
	}
}
