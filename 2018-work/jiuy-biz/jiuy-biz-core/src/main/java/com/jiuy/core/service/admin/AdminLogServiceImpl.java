package com.jiuy.core.service.admin;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.AdminLogDao;
import com.jiuy.core.meta.admin.AdminLog;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.query.PageQuery;


@Service
public class AdminLogServiceImpl implements AdminLogService {
		
	@Resource
	private AdminLogDao adminLogDaoSqlImpl;

	@Override
	public int addAdminLog(AdminUser adminUser, String operateModel, String operateContent,  String ip) {
		AdminLog adminLog = new AdminLog();
		long currentTime = System.currentTimeMillis();
		
		adminLog.setUserId(adminUser.getUserId());
		adminLog.setUserName(adminUser.getUserName());
		adminLog.setOperateModel(operateModel);
		adminLog.setOperateContent(operateContent);
		adminLog.setCreateTime(currentTime);
		adminLog.setIp("127.0.0.1");
		
		if(ip != null && ip.length() > 0){
			adminLog.setIp(ip);
			
		}
		return adminLogDaoSqlImpl.addAdminLog(adminLog);
	}

	@Override
	public List<AdminLog> searchAdminLog(AdminLog adminLog, long startTime, long endTime, PageQuery query) {

		return adminLogDaoSqlImpl.searchAdminLog(adminLog, startTime, endTime, query);
	}

	@Override
	public int searchAdminLogCount(AdminLog adminLog, long startTime, long endTime) {

		return adminLogDaoSqlImpl.searchAdminLogCount(adminLog, startTime, endTime);
	}

}
