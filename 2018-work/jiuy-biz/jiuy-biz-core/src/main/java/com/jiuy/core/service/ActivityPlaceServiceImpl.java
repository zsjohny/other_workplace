package com.jiuy.core.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ActivityPlaceDao;
import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class ActivityPlaceServiceImpl implements ActivityPlaceService{
	private static Logger logger = Logger.getLogger(ActivityPlaceServiceImpl.class);
	private final String LINK_PRE_URL = "www.yujiejie.com/page/";
	
	@Autowired
	private ActivityPlaceDao activityPlaceDao;
	
	@Override
	public int add(String name, String description) {
		long currentTime = System.currentTimeMillis();
		
		ActivityPlace activityPlace = activityPlaceDao.add(name, description, currentTime);
		long id = activityPlace.getId();
		String url = LINK_PRE_URL + id;
		
		return activityPlaceDao.updateUrl(id, url);
	}

	@Override
	public int update(long id, String name, String description) {
		long currentTime = System.currentTimeMillis();
		
		return activityPlaceDao.update(id, name, description,currentTime);
	}

	@Override
	public List<ActivityPlace> search(String name,int type,  PageQuery pageQuery) {
		return activityPlaceDao.search(name, type,pageQuery);
	}

	@Override
	public int searchCount(String name, int type) {
		return activityPlaceDao.searchCount(name,type);
	}

	/**
	 * 删除专场
	 */
	@Override
	public int delete(long activityPlaceId) {
		ActivityPlace activityPlace =activityPlaceDao.getActivityPlaceById(activityPlaceId);
		if(activityPlace.getStatus()==-1){
			throw new RuntimeException("删除专场：这个专场无法删除activityPlaceId:"+activityPlaceId+",status:"+activityPlace.getStatus());
		}
		int type = activityPlace.getType();
		if(type==1){
			return activityPlaceDao.updateActivityPlaceType(activityPlaceId);
		}else if(type==2){
			return activityPlaceDao.delete(activityPlaceId);
		}else{
			throw new RuntimeException("删除专场：这个专场类型无法删除type:"+type);
		}
	}
	/**
	 * 恢复专场
	 */
	@Override
	public int restore(long activityPlaceId) {
		ActivityPlace activityPlace =activityPlaceDao.getActivityPlaceById(activityPlaceId);
		if(activityPlace.getStatus()==-1){
			throw new RuntimeException("这个专场已经彻底删除");
		}
		int type = activityPlace.getType();
		if(type==2){
			return activityPlaceDao.restore(activityPlaceId);
		}else{
			throw new RuntimeException("恢复专场：这个专场类型无法恢复");
		}
	}
	
}
