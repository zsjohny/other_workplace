package com.jiuy.core.service.operationLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.OperationLogDao;
import com.jiuy.core.meta.operationLog.OperationLogVO;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class OperationLogServiceImpl implements OperationLogService{

	@Resource
	private OperationLogDao operationLogDaoSqlImpl;
	
	@Override
	public List<Map<String, Object>> search(PageQuery query,  OperationLogVO op, String startTime, String endTime) {
		long startTimeMillions = 0;
		long endTimeMillions = 0;
		
		try {
			if(endTime.equals("")) {
				endTimeMillions = System.currentTimeMillis();
				startTimeMillions = transform(startTime);
			} else {
				endTimeMillions = transform(endTime);
				startTimeMillions = transform(startTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		op.setEndTime(endTimeMillions);
		op.setStartTime(startTimeMillions);
		
		return operationLogDaoSqlImpl.search(query, op);
	}
	
	private long transform(String timeFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  sdf.parse(timeFormat).getTime();
	}

	@Override
	public int searchCount(OperationLogVO op, String startTime, String endTime) {
		long startTimeMillions = 0;
		long endTimeMillions = 0;
		
		try {
			if(endTime.equals("")) {
				endTimeMillions = System.currentTimeMillis();
				startTimeMillions = transform(startTime);
			} else {
				endTimeMillions = transform(endTime);
				startTimeMillions = transform(startTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		op.setEndTime(endTimeMillions);
		op.setStartTime(startTimeMillions);
		
		return operationLogDaoSqlImpl.searchCount(op);
	}

}
