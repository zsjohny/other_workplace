package com.store.service;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.store.dao.mapper.MemberMapper;
import com.store.dao.mapper.ShopMemberDayReportMapper;
import com.store.dao.mapper.StoreBusinessMapper;
import com.store.dao.mapper.WxaMemberFavoriteMapper;
import com.store.entity.ShopMemberDayReport;
import com.store.entity.ShopMemberFavorite;
import com.store.entity.member.ShopMember;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* @author QiuYuefan
*/

@Service
public class ShopMemberDayReportService{
	private static final Log logger = LogFactory.get("ShopMemberDayReportService");
	
	@Autowired
	private ShopMemberDayReportMapper shopMemberDayReportMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private StoreBusinessMapper storeBusinessMapper;
	
	@Autowired
	private WxaMemberFavoriteMapper wxaMemberFavoriteMapper;
	
	public void insertShopMemberDayReport() {
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long newTime = cal.getTimeInMillis();
        long oldTime = newTime-24*60*60*1000;
		List<StoreBusiness> storeBusinessList = storeBusinessMapper.getAll();
		Random random = new Random();
		for (StoreBusiness storeBusiness : storeBusinessList) {
			ShopMemberDayReport shopMemberDayReport = new ShopMemberDayReport();
			long storeId = storeBusiness.getId();
			shopMemberDayReport.setStoreId(storeId);
			shopMemberDayReport.setPvCount(random.nextInt(100));

			Wrapper<ShopMember> registerWrapper = new EntityWrapper<ShopMember>().eq("store_id", storeId).eq("status", 0).ge("update_time", oldTime)
					.le("update_time", newTime);
			int registerCount = memberMapper.selectCount(registerWrapper);
//			int registerCount = memberMapper.selectDayRegisterCount(storeId,newTime,oldTime);
			shopMemberDayReport.setRegisterCount(registerCount);
			
			Wrapper<ShopMemberFavorite> favoriteWrapper = new EntityWrapper<ShopMemberFavorite>().eq("store_id", storeId).eq("status", 0).ge("update_time", oldTime)
					.le("update_time", newTime);
			int favoriteCount = wxaMemberFavoriteMapper.selectCount(favoriteWrapper);
			shopMemberDayReport.setCollectionCount(favoriteCount);
			
			long createTime = System.currentTimeMillis();
			shopMemberDayReport.setCreateTime(createTime);
			shopMemberDayReport.setUpdateTime(createTime);
			
			int insertCount = shopMemberDayReportMapper.insert(shopMemberDayReport);
			if(insertCount!=1){
				logger.error("ShopMemberDayReportService:"+storeId+"该门店每日报告插入失败");
			}
		}
	}
}