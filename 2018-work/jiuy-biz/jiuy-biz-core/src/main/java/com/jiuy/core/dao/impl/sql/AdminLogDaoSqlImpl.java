package com.jiuy.core.dao.impl.sql;

import com.jiuy.core.dao.AdminLogDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.admin.AdminLog;
import com.jiuyuan.entity.query.PageQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminLogDaoSqlImpl extends SqlSupport implements AdminLogDao {

    @Override
    public List<AdminLog> loadAll() {
        return null;
    }

    @Override
    public int addAdminLog(AdminLog adminLog) {
        //TODO
        return 1;

    }

    @Override
    public List<AdminLog> searchAdminLog(AdminLog adminLog, long startTime, long endTime, PageQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userId", adminLog.getUserId());
        params.put("userName", adminLog.getUserName());
        params.put("operateModel", adminLog.getOperateModel());
        params.put("operateContent", adminLog.getOperateContent());
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("pageQuery", query);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.AdminLogDaoSqlImpl.searchAdminLog", params);
    }

    @Override
    public int searchAdminLogCount(AdminLog adminLog, long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userId", adminLog.getUserId());
        params.put("userName", adminLog.getUserName());
        params.put("operateModel", adminLog.getOperateModel());
        params.put("operateContent", adminLog.getOperateContent());
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.AdminLogDaoSqlImpl.searchAdminLogCount", params);
    }


}
