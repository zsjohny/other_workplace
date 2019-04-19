package com.jiuyuan.service.common;

import java.util.List;

import com.jiuyuan.entity.newentity.PlacardApplyAudit;

public interface IPlacardApplyAuditService {

	public List<PlacardApplyAudit> getPlacardApplyAuditList(long supplierPlacardId, long supplierId);

	public PlacardApplyAudit getPlacardApplyAuditInfo(long placardApplyAuditId);
	
	
}