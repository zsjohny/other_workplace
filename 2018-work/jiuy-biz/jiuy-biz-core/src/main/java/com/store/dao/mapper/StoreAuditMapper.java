package com.store.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.query.PageQuery;

/**
 * <p>
  * 门店审核
 * </p>
 *
 * @author 陈志勇
 * @since 2017-06-28
 */
public interface StoreAuditMapper extends BaseMapper<StoreAudit> {
	
	public List<StoreAudit> getAuditList(@Param("pageQuery") PageQuery pageQuery, @Param("status") int status);

	public int updateStoreAudit(@Param("storeId") long storeId, @Param("status") int status, @Param("auditId") long auditId);
	
	public int getAuditCount(@Param("storeId") long storeId, @Param("status") int status);

	public List<StoreAudit> getAuditByStoreId(@Param("storeId")long storeId);
}