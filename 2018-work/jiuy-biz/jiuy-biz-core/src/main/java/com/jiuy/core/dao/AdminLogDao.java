package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.admin.AdminLog;
import com.jiuyuan.entity.query.PageQuery;

public interface AdminLogDao {

	public List<AdminLog> loadAll();
	public int addAdminLog(AdminLog adminLog);
    public List<AdminLog> searchAdminLog(AdminLog adminUser, long startTime, long endTime, PageQuery query);
    public int searchAdminLogCount(AdminLog adminUser, long startTime, long endTime);

}
