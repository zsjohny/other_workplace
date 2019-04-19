package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.SupplierPlacard;
import com.jiuyuan.entity.query.PageQuery;
/**
 * 供应商公告
 */
public interface ISupplierPlacardService {

	public List<SupplierPlacard> getSupplierPlacardList(Page<Map<String,String>> page, long supplierPlacardId, String title,String content,
			long publishTimeBegin, long publishTimeEnd, int state, int type);
	public List<SupplierPlacard> supplierGetPlacardList(Page<Map<String,String>> page, 
			String title, int type, long publishTimeBegin, long publishTimeEnd, int applyState,int readState,long supplierId);

	public SupplierPlacard getSupplierPlacardInfo(long supplierPlacardId);
	public void addSupplierPlacard(long adminId, String title, String content, int type,int isSendAdvice, int publishType,long publishTime,long applyEndTime);

	public void updateSupplierPlacard(long supplierPlacardId, String title, String content, int type,int isSendAdvice, int publishType, long publishTime, long applyEndTime);
	public void stopSupplierPlacard(long supplierPlacardId);
	/**
	 * 公告总数
	 * @return
	 */
	public int getTotalCount();
	/**
	 * 通知中公告总数
	 * @return
	 */
	public int getNotifyCount();
	/**
	 * 获取未读公告数量
	 * @return
	 */
	public int getNoReadCount(long supplierId);
	/**
	 * 获取未读公告列表
	 * @param supplierId
	 * @return
	 */
	public List<SupplierPlacard> getHomePageNoReadPlacardListTop5(long supplierId);

	/**
	 * 获取最近未读公告
	 * @param supplierId
	 * @return
	 */
	public Map<String,String> getNoReadPlacard(long supplierId);
	/**
	 * 增加公告阅读记录数
	 * @param supplierId
	 * @return
	 */
	public void increaseReadCount(long placardId);
	
	
	/**
	 * 增加公告通知数
	 * @param placardId
	 */
	public void increaseNotifyCount(long placardId);



	
}