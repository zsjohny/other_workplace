package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.SupplierPlacard;

@DBMaster
public interface SupplierPlacardMapper extends BaseMapper<SupplierPlacard> {
	
	public List<SupplierPlacard> getAdvicePlacardTop5(long supplierId);

	public List<SupplierPlacard> getNoReadPlacardTop5(long supplierId);

	public int getNoReadCount(long supplierId);

	public void increaseReadCount(long placardId);
	
	public void increaseNotifyCount(long placardId);
	
	
}
