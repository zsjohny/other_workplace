package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.query.PageQuery;

public interface IPlacardApplyService {

	public List<PlacardApply> supplierGetPlacardApplyList(Page<Map<String, String>> page, String title, int auditState,
			long applyTimeBegin, long applyTimeEnd);

	public PlacardApply getPlacardApply(long supplierPlacardId, long supplierId);


	
	public void supplierGetPlacardApply(long supplierPlacardId, String applyContent,long supplierId,String supplierName,long brandId, String brandName);

	public List<PlacardApply> getPlacardApplyList(Page<Map<String, String>> page, long supplierPlacardId, String title,
			String supplierName, String brandName, long applyTimeBegin, long applyTimeEnd, long applyEndTimeBegin,
			long applyEndTimeEnd, int auditState, String coutent, String titleNote);

	

	public PlacardApply getPlacardApplyinfo(long placardApplyId);

	public void auditPlacardApply(long placardApplyId, int state, String titleNote,String applyUserName,long applyUserNameId);

	public int getPlacardApplyTotalCount();

	public int getPlacardApplyWaitAuditCount();


	public int getApplyCountByState(int applyState, long supplierId);

	public List<PlacardApply> getPlacardApplyList(long placardId);



	
	
}