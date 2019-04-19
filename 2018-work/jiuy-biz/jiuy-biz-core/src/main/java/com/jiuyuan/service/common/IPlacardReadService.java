package com.jiuyuan.service.common;

public interface IPlacardReadService {
	/**
	 * 设置公告为已读
	 * @param placardId
	 * @param supplierId
	 */
	public void setPlacardIsRead(long placardId, long supplierId);
	
	/**
	 * 获取公告读取状态（阅读状态：0未读、1已读）
	 * @param placardId
	 * @param supplierId
	 * @return
	 */
	public int getReadState(long placardId, long supplierId);
}