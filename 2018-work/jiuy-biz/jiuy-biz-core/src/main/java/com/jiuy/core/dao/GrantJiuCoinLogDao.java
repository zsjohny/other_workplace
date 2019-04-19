package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.GrantJiuCoinLog;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月23日 下午5:54:16
*/
public interface GrantJiuCoinLogDao {

	public long addGrantJiuCoinLog(GrantJiuCoinLog grantJiuCoinLog);
	
	public List<GrantJiuCoinLog> getGrantJiuCoinLog(PageQuery pageQuery);
	
	public int getGrantJiuCoinLogCount();
}
