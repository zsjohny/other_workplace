package com.jiuy.core.service.admin;

import java.util.List;

import com.jiuy.core.meta.admin.AdminLog;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.query.PageQuery;

public interface AdminLogService {

	int addAdminLog(AdminUser adminLog, String operateModel, String operateContent,String ip);

	List<AdminLog> searchAdminLog(AdminLog adminLog, long startTime, long endTime, PageQuery query);

	int searchAdminLogCount(AdminLog adminUser, long startTime, long endTime);
}
