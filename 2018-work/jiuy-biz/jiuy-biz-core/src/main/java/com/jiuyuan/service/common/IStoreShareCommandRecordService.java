package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.StoreShareCommandRecord;

public interface IStoreShareCommandRecordService {

	List<StoreShareCommandRecord> getValidKey(String key);

	/**
	 * 插入分享口令
	 * @param shareCommandRecord
	 */
	void insertShareCommandRecord(StoreShareCommandRecord shareCommandRecord);

	/**
	 * 解析分享口令并回传商品相关信息
	 * @param key
	 * @return
	 */
	Map<String,Object> parseShareCommand(String key);
    
	/**
	 * 查询相关分享口令
	 * @param storeId
	 * @param productId
	 * @return
	 */
	StoreShareCommandRecord getShareCommandByStoreIdAndProductId(long storeId, long productId);
	
	/**
	 * 获取分享口令
	 * @param storeId
	 * @param productId
	 * @param deadline
	 * @return
	 */
	public String getShareCommand(long storeId, long productId, long validTime);

    /**
     * 添加分享口令打开者的日志   
     * @param shareCommandId
     */
	void insertShareCommandLog(long shareCommandId ,long storeId);

}