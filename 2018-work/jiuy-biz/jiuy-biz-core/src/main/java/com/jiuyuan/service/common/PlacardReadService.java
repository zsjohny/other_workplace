/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.PlacardApplyMapper;
import com.jiuyuan.dao.mapper.supplier.PlacardReadMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierPlacardMapper;
import com.jiuyuan.entity.newentity.PlacardRead;
import com.jiuyuan.entity.newentity.SupplierPlacard;

/**
 * 新商品SKU服务
 */

@Service
public class PlacardReadService implements IPlacardReadService  {
	private static final Logger logger = LoggerFactory.getLogger(PlacardReadService.class);
	@Autowired
	private PlacardReadMapper placardReadMapper;
	
	@Autowired
	private SupplierPlacardMapper supplierPlacardMapper;
	@Autowired
	private PlacardApplyMapper placardApplyMapper;
	/**
	 * 设置公告为已读
	 * @param placardId
	 * @param supplierId
	 */
	public void setPlacardIsRead(long placardId, long supplierId) {
		if(placardId > 0){
			Wrapper<PlacardRead> placardReadWrapper = new EntityWrapper<PlacardRead>();
			placardReadWrapper.eq("placard_id", placardId);
			placardReadWrapper.eq("supplier_id", supplierId);
			List<PlacardRead> placardReadList = placardReadMapper.selectList(placardReadWrapper);
			if(placardReadList.size() == 0){
				//添加中间表记录
				PlacardRead placardRead = new PlacardRead();
				placardRead.setPlacardId(placardId);//公告ID
				placardRead.setSupplierId(supplierId);//供应商ID
				placardRead.setReadTime(System.currentTimeMillis());//创建时间
				placardReadMapper.insert(placardRead);
				
//				修改公告已读字段
				SupplierPlacard supplierPlacard = supplierPlacardMapper.selectById(placardId);
				String isReadSupplierIds = supplierPlacard.getIsReadSupplierIds() ;
				if(StringUtils.isNotEmpty(isReadSupplierIds)){
					isReadSupplierIds = isReadSupplierIds + "," +supplierId;
				}else{
					isReadSupplierIds = String.valueOf(supplierId);
				}
				SupplierPlacard supplierPlacardUpd = new SupplierPlacard();
				supplierPlacardUpd.setId(placardId);
				supplierPlacardUpd.setIsReadSupplierIds(isReadSupplierIds);
				supplierPlacardMapper.updateById(supplierPlacardUpd);		
			}
		}
	}
	
	/**
	 * 获取公告读取状态（阅读状态：0未读、1已读）
	 * @param placardId
	 * @param supplierId
	 * @return
	 */
	public int getReadState(long placardId, long supplierId) {
		Wrapper<PlacardRead> placardReadWrapper = new EntityWrapper<PlacardRead>();
		placardReadWrapper.eq("placard_id", placardId);
		placardReadWrapper.eq("supplier_id", supplierId);
		List<PlacardRead> placardReadList = placardReadMapper.selectList(placardReadWrapper);
		if(placardReadList.size() == 0){
			return 0;//0未读
		}else{
			return 1;//1已读
		}
		
	}
	
}