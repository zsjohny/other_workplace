package org.dream.utils.jms;

import java.sql.Timestamp;
import java.util.Map;

import org.dream.utils.log.TimeTaskLogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 系统定时任务的顶级类，系统中所有定时任务的实现类均应实现此类
 * 
 * @author wangd
 *
 */
public abstract class TimedTaskListener implements Listener {

	@Autowired
	private MongoTemplate mongoTemplate;
	private Logger logger=LoggerFactory.getLogger(TimedTaskListener.class);
	@Override
	public void onMessage(String message) {
		
		JSONObject msg=JSON.parseObject(message);
		Integer taskId =  msg.getInteger("taskId");
		String parameters = msg.getString("parameters");
		Long triggerTime= msg.getLong("triggerTime");
		logger.info("schedule task is running!,taskid : {},param : {} ",taskId,parameters);
		// Map<String, Object> map = (Map<String, Object>) JSON.parse(message);
		TimeTaskLogInfo info = new TimeTaskLogInfo();
		info.setTaskId(taskId);
		info.setExecutionTime(new Timestamp(System.currentTimeMillis()));
		try {
			if(triggerTime-System.currentTimeMillis()<60000){//60000到时由系统参数来控制
				this.onTaskMessage(parameters);
				info.setIsSucess(true);
				info.setHasException(false);
				info.setSleepDead(false);
			}else{
				info.setIsSucess(false);
				info.setHasException(false);
				info.setSleepDead(true);
			}
			mongoTemplate.save(info);
			logger.info("schedule task finished!,taskid : {},param : {} ",taskId,parameters);
		} catch (Exception e) {
			logger.info("schedule task has exception!,taskid : {},param : {} ",taskId,parameters);
			info.setIsSucess(false);
			info.setHasException(true);
			info.setSleepDead(false);
			mongoTemplate.save(info);
		}

	}

	public abstract void onTaskMessage(String message);
}
