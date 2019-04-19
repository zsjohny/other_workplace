package com.jiuy.core.service.admin;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.query.PageQuery;

public interface AdminUserService {


	int update(long userId, long roleId, String userPhone);

	int resetPassword(long userId);

	List<AdminUser> search(PageQuery query, Long userId, String userName, Long roleId, String userPhone);

	int searchCount(Long userId, String userName, Long roleId, String userPhone);

	Map<Long, Integer> getCountOfRole();

	int add(AdminUser adminUser);

	int remove(long userId);

	int updateHttpUrl(String primaryKey, long id, String columnName, String newUrl, String tableName);

}
