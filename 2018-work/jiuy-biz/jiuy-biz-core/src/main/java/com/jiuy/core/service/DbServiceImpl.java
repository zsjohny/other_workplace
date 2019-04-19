package com.jiuy.core.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.DbDao;

@Service
public class DbServiceImpl implements DbService {
	Logger logger = Logger.getLogger(DbServiceImpl.class);
	@Resource
	private DbDao dbDao;
	

	@Override
	public void db() {
		yjj_ShoppingCart();//测试完成
		yjj_UserSign();//测试完成
		yjj_UserLike();//测试完成
		yjj_InviteGiftShareLog();//测试完成
		yjj_UserVisitHistory();
	}
	/**
	 * 7.2	用户购物车yjj_ShoppingCart
	 */
	public void yjj_ShoppingCart(){
		//1、获取手机账号和微信账号对应关系
		List<Map<String,Long>> userIdMapList = userIdMapList = dbDao.getUserInfo();
		for(Map<String, Long> map : userIdMapList){
			Long phoneUserId = map.get("phoneUserId");
			Long weixinUserId = map.get("weixinUserId");
			//2、获取手机账号的签到记录列表
			List<Map<String,Object>> phoneList = dbDao.getShoppingCartList(phoneUserId);
			//3、获得微信账号的签到记录列表
			List<Map<String,Object>> weixinList = dbDao.getShoppingCartList(weixinUserId);
			//4、修改微信账号购物车记录userId为手机账号
			for(Map<String, Object> weixinMap : weixinList){
				long shoppingCartId = (Long) weixinMap.get("Id");
				long weixinSkuId  = (Long) weixinMap.get("SkuId");
				boolean isHave = false;
				for(Map<String, Object> phoneMap : phoneList){
					long phoneSkuId  = (Long) phoneMap.get("SkuId");
					if(phoneSkuId == weixinSkuId){
						isHave = true;
						break;
					}
				}
				if(!isHave){
					//将微信签到改为手机签到
					logger.info("shoppingCartId:"+shoppingCartId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinSkuId:"+weixinSkuId+"商品购物车中不存在，需进行迁移记录");
					dbDao.updShoppingCartId(shoppingCartId,phoneUserId);
				}else{
					//将微信签到改为手机签到
				//	logger.info("shoppingCartId:"+shoppingCartId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinSkuId:"+weixinSkuId+"商品购物车中已存在");
				}
			}
		}
		logger.info("用户购物车yjj_ShoppingCart处理完成！");
	}
	/**
	 *7.3	用户签到yjj_UserSign 测试完成
	 */
	public void yjj_UserSign(){
		//1、获取手机账号和微信账号对应关系
		List<Map<String,Long>> userIdMapList = userIdMapList = dbDao.getUserInfo();
		for(Map<String, Long> map : userIdMapList){
			Long phoneUserId = map.get("phoneUserId");
			Long weixinUserId = map.get("weixinUserId");
			//2、获取手机账号的签到记录列表
			List<Map<String,Object>> phoneList = dbDao.getSignList(phoneUserId);
			//3、获得微信账号的签到记录列表
			List<Map<String,Object>> weixinList = dbDao.getSignList(weixinUserId);
			//4、修改微信账号签名记录userId为手机账号
			for(Map<String, Object> weixinMap : weixinList){
				long signId = (Long) weixinMap.get("Id");
				int weixinDayTime = (Integer) weixinMap.get("DayTime");
				boolean isHave = false;
				for(Map<String, Object> phoneMap : phoneList){
					int phoneDayTime = (Integer)phoneMap.get("DayTime");
					if(phoneDayTime == weixinDayTime){
						isHave = true;
						break;
					}
				}
				if(!isHave){
					//将微信签到改为手机签到
					logger.info("signId:"+signId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinDayTime:"+weixinDayTime+"签到不存在，需进行迁移记录");
					dbDao.updSignUserId(signId,phoneUserId);
				}else{
					//将微信签到改为手机签到
				//	logger.info("signid:"+signId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinDayTime:"+weixinDayTime+"签到已存在");
				}
			}
		}

		
		
		logger.info("用户签到yjj_UserSign处理完成！");
	}
	/**
	 * 7.4	点赞yjj_UserLike  RelatedId
	 */
	public void yjj_UserLike(){
		//1、获取手机账号和微信账号对应关系
				List<Map<String,Long>> userIdMapList = userIdMapList = dbDao.getUserInfo();
				for(Map<String, Long> map : userIdMapList){
					Long phoneUserId = map.get("phoneUserId");
					Long weixinUserId = map.get("weixinUserId");
					//2、获取手机账号的签到记录列表
					List<Map<String,Object>> phoneList = dbDao.getUserLikeList(phoneUserId);
					//3、获得微信账号的签到记录列表
					List<Map<String,Object>> weixinList = dbDao.getUserLikeList(weixinUserId);
					//4、修改微信账号签名记录userId为手机账号
					for(Map<String, Object> weixinMap : weixinList){
						long userLikeId = (Long) weixinMap.get("Id");
						long wRelatedId = (Long) weixinMap.get("RelatedId");
						boolean isHave = false;
						for(Map<String, Object> phoneMap : phoneList){
							long pRelatedId = (Long)phoneMap.get("RelatedId");
							if(pRelatedId == wRelatedId){
								isHave = true;
								break;
							}
						}
						if(!isHave){
							//将微信签到改为手机签到
							logger.info("userLikeId:"+userLikeId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinDayTime:"+wRelatedId+"点赞不存在，需进行迁移记录");
							dbDao.updUserLikeId(userLikeId,phoneUserId);
						}else{
							//将微信签到改为手机签到
					//		logger.info("userLikeId:"+userLikeId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinDayTime:"+wRelatedId+"点赞已存在");
						}
					}
				}
				logger.info("点赞yjj_UserLike处理完成！");
	}
	
	/**
	 * 7.6	邀请有奖分享记录yjj_InviteGiftShareLog 
	 */
	public void yjj_InviteGiftShareLog(){
		//1、获取手机账号和微信账号对应关系
				List<Map<String,Long>> userIdMapList = userIdMapList = dbDao.getUserInfo();
				for(Map<String, Long> map : userIdMapList){
					Long phoneUserId = map.get("phoneUserId");
					Long weixinUserId = map.get("weixinUserId");
					//2、获取手机账号的签到记录列表
					List<Map<String,Object>> phoneList = dbDao.getLogList(phoneUserId);
					//3、获得微信账号的签到记录列表
					List<Map<String,Object>> weixinList = dbDao.getLogList(weixinUserId);
					//4、修改微信账号签名记录userId为手机账号
					for(Map<String, Object> weixinMap : weixinList){
						long logId = (Long) weixinMap.get("Id");
						int weixinType = (Integer) weixinMap.get("Type");
						int weixinCount = (Integer) weixinMap.get("Count");
						boolean isUpd = false;
						for(Map<String, Object> phoneMap : phoneList){
							int phoneType = (Integer)phoneMap.get("Type");
							int phoneCount = (Integer) phoneMap.get("Count");
							if(weixinType == phoneType){
								if(weixinCount > phoneCount){
									isUpd = true;	
									break;
								}
							}
						}
						if(isUpd){
							//将微信签到改为手机签到
							logger.info("logId:"+logId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinType:"+weixinType+"数量需要更改为"+weixinCount+"，需进行迁移记录");
							dbDao.updLogId(logId,weixinCount);
						}else{
							//将微信签到改为手机签到
					//		logger.info("logId:"+logId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinType:"+weixinType+"数量无需更改");
						}
					}
				}
				logger.info("邀请有奖分享记录yjj_InviteGiftShareLog处理完成！");
	}
	
	/**
	 * 7.8	用户访问记录表yjj_UserVisitHistory
	 */
	public void yjj_UserVisitHistory(){
		//1、获取手机账号和微信账号对应关系
		List<Map<String,Long>> userIdMapList = userIdMapList = dbDao.getUserInfo();
		for(Map<String, Long> map : userIdMapList){
			Long phoneUserId = map.get("phoneUserId");
			Long weixinUserId = map.get("weixinUserId");
			//2、获取手机账号的签到记录列表
			List<Map<String,Object>> phoneList = dbDao.getHistoryList(phoneUserId);
			//3、获得微信账号的签到记录列表
			List<Map<String,Object>> weixinList = dbDao.getHistoryList(weixinUserId);
			//4、修改微信账号签名记录userId为手机账号
			for(Map<String, Object> weixinMap : weixinList){
				long logId = (Long) weixinMap.get("Id");
				long weixinRelatedId = (Long) weixinMap.get("RelatedId");
				boolean isHave = false;
				int count = 0;
				for(Map<String, Object> phoneMap : phoneList){
					long phoneRelatedId = (Long)phoneMap.get("RelatedId");
					if(weixinRelatedId == phoneRelatedId){
						isHave = true;	
						break;
					}
				}
				if(!isHave){
					//将微信签到改为手机签到
					logger.info("logId:"+logId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinRelatedId:"+weixinRelatedId+"不存在，需进行迁移记录");
					dbDao.updHistoryUserId(logId,phoneUserId);
				}else{
					//将微信签到改为手机签到
				//	logger.info("logId:"+logId+",phoneUserId:"+phoneUserId+",weixinUserId:"+weixinUserId+",weixinRelatedId:"+weixinRelatedId+"已存在，无需更改");
				}
			}
		}
		logger.info("用户访问记录表yjj_UserVisitHistory处理完成！");
	}
}
