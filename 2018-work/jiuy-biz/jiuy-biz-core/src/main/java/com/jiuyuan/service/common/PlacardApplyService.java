/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.PlacardApplyAuditMapper;
import com.jiuyuan.dao.mapper.supplier.PlacardApplyMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierPlacardMapper;
import com.jiuyuan.entity.newentity.PlacardApply;
import com.jiuyuan.entity.newentity.PlacardApplyAudit;
import com.jiuyuan.entity.newentity.SupplierPlacard;

/**
 * 新商品SKU服务
 */
@Service
public class PlacardApplyService implements IPlacardApplyService  {
	private static final Logger logger = LoggerFactory.getLogger(PlacardApplyService.class);
	
	@Autowired
	private PlacardApplyMapper placardApplyMapper;
	@Autowired
	private SupplierPlacardMapper supplierPlacardMapper;
	@Autowired
	private PlacardApplyAuditMapper placardApplyAuditMapper;
	
	/**
	 * 	获取供应商公告申请记录
	 */
	public PlacardApply getPlacardApply(long supplierPlacardId, long supplierId){
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		wrapper.eq("placard_id", supplierPlacardId);
		wrapper.eq("supplier_id", supplierId);
		wrapper.orderBy("id", false);
		List<PlacardApply> placardApplyList = placardApplyMapper.selectList(wrapper);
		if(placardApplyList.size() >0 ){
			return placardApplyList.get(0);
		}else{
			return null;
		}
	}
	
	
	public List<PlacardApply> getPlacardApplyList(long placardId){
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		wrapper.eq("placard_id", placardId);
		List<PlacardApply> placardApplyList = placardApplyMapper.selectList(wrapper);
		return placardApplyList;
	}
	
	
	public PlacardApply getPlacardApplyinfo(long placardApplyId){
		return  placardApplyMapper.selectById(placardApplyId);
	}
	
	public void auditPlacardApply(long placardApplyId, int state, String titleNote,String applyUserName,long applyUserNameId){
		PlacardApply placardApply= placardApplyMapper.selectById(placardApplyId);
		int stateOld = placardApply.getState();
//		if(stateOld != 0){
//			throw new  RuntimeException("审核失败，审核状态不为待审核！");
//		}
		//修改申请审核信息
		PlacardApply placardApplyNew = new  PlacardApply();
		placardApplyNew.setId(placardApplyId);
		placardApplyNew.setState(state);
		placardApplyNew.setAuditTime(System.currentTimeMillis());
		placardApplyNew.setTitleNote(titleNote);
		placardApplyMapper.updateById(placardApplyNew);
		
		//添加报名审核记录
		PlacardApplyAudit placardApplyAudit =  new PlacardApplyAudit();
		placardApplyAudit.setPlacardApplyId(placardApply.getId());//公告申请ID
		placardApplyAudit.setPlacardId(placardApply.getPlacardId());// 公告ID
		placardApplyAudit.setSupplierId(placardApply.getSupplierId());//供应商ID
		placardApplyAudit.setState(state);//公告申请状态：0待审核、1已通过、2已拒绝
		placardApplyAudit.setTitleNote(titleNote);//审核说明
		placardApplyAudit.setAuditTime(System.currentTimeMillis());//审核时间
		placardApplyAudit.setApplyCoutent(placardApply.getCoutent());//报名信息
		placardApplyAudit.setApplyTime(placardApply.getApplyTime());//申请时间
		placardApplyAudit.setApplyUserName(applyUserName);//审核人
		placardApplyAudit.setApplyUserNameId(applyUserNameId);//审核人ID
		placardApplyAuditMapper.insert(placardApplyAudit);
	}
	
	
	
	public List<PlacardApply> getPlacardApplyList(Page<Map<String, String>> page, long supplierPlacardId, String title,
			String supplierName, String brandName, long applyTimeBegin, long applyTimeEnd, long applyEndTimeBegin,
			long applyEndTimeEnd, int auditState, String coutent, String titleNote){
		
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		if(supplierPlacardId != 0){
			wrapper.eq("placard_id", supplierPlacardId);
		}
		if(StringUtils.isNotEmpty(title)){
			wrapper.like("placard_title", title);
		}
		if(StringUtils.isNotEmpty(supplierName)){
			wrapper.like("supplier_name", supplierName);
		}
		if(StringUtils.isNotEmpty(brandName)){
			wrapper.like("brand_name", brandName);
		}
		if(applyTimeBegin != 0){
			wrapper.gt("apply_time", applyTimeBegin);
		}
		if(applyTimeEnd != 0){
			wrapper.lt("apply_time", applyTimeEnd);
		}
		if(applyEndTimeBegin != 0){
			wrapper.gt("apply_end_time", applyEndTimeBegin);
		}
		if(applyEndTimeEnd != 0){
			wrapper.lt("apply_end_time", applyEndTimeEnd);
		}
		if(StringUtils.isNotEmpty(coutent)){
			wrapper.like("coutent", coutent);
		}
		if(StringUtils.isNotEmpty(titleNote)){
			wrapper.like("title_note", titleNote);
		}
		if(auditState != -1){
			wrapper.eq("state", auditState);
		}
		wrapper.orderBy("apply_time", false);
		List<PlacardApply> list = placardApplyMapper.selectPage(page,wrapper);
		return list;
	}
	
	public List<PlacardApply> supplierGetPlacardApplyList(Page<Map<String, String>> page, String title, int auditState,long applyTimeBegin, long applyTimeEnd){
		
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		
		if(StringUtils.isNotEmpty(title)){
			wrapper.like("placard_title", title);
		}
		if(auditState != -1){
			wrapper.eq("state", auditState);
		}
		if(applyTimeBegin != 0){
			wrapper.gt("apply_time", applyTimeBegin);
		}
		if(applyTimeEnd != 0){
			wrapper.lt("apply_time", applyTimeEnd);
		}
		wrapper.orderBy("apply_time", false);
		List<PlacardApply> list = placardApplyMapper.selectPage(page,wrapper);
		return list;
	}



	@Override
	public void supplierGetPlacardApply(long supplierPlacardId, String applyContent,long supplierId,String supplierName,long brandId, String brandName) {
		logger.info("开始添加公告申请，supplierPlacardId："+supplierPlacardId+",applyContent:"+applyContent);
		SupplierPlacard supplierPlacard = supplierPlacardMapper.selectById(supplierPlacardId);
		
		
		//
		PlacardApply placardApplyObj = getPlacardApply(supplierPlacardId,supplierId);
		if(placardApplyObj == null){//没有报名申请
			
			PlacardApply placardApply = new PlacardApply();
			placardApply.setPlacardId(supplierPlacardId);//
			placardApply.setPlacardTitle(supplierPlacard.getTitle());//
			placardApply.setCoutent(applyContent);//
			placardApply.setSupplierId(supplierId);//
			placardApply.setSupplierName(supplierName);//
			placardApply.setBrandId(brandId);//
			placardApply.setBrandName(brandName);//
			placardApply.setState(PlacardApply.state_wait_audit);//
			placardApply.setApplyTime(System.currentTimeMillis());//申请时间
			placardApply.setApplyEndTime(supplierPlacard.getApplyEndTime());//申请时间
			placardApplyMapper.insert(placardApply);
			logger.info("开始添加公告申请完成,placardApply:"+JSON.toJSONString(placardApply));
			
			
		}else{//已经有报名申请则修改报名状态
			
			int state = placardApplyObj.getState();//公告申请状态：0待审核、1已通过、2已拒绝
			if(state == PlacardApply.state_wait_audit){
				throw new RuntimeException("有待审核申请，不能再次申请");
			}else if(state == PlacardApply.state_audit_yes){
				throw new RuntimeException("有已通过申请，不用再次申请");
			}
			
			PlacardApply placardApply = new PlacardApply();
			placardApply.setId(placardApplyObj.getId());
			placardApply.setPlacardId(supplierPlacardId);//
			placardApply.setPlacardTitle(supplierPlacard.getTitle());//
			placardApply.setCoutent(applyContent);//
			placardApply.setSupplierId(supplierId);//
			placardApply.setSupplierName(supplierName);//
			placardApply.setBrandId(brandId);//
			placardApply.setBrandName(brandName);//
			placardApply.setState(PlacardApply.state_wait_audit);//
			placardApply.setApplyTime(System.currentTimeMillis());//申请时间
			placardApplyMapper.updateById(placardApply);
			

		}
		
		
		
	}


	
	
	public int getPlacardApplyTotalCount(){
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		return placardApplyMapper.selectCount(wrapper);
	}

	public int getPlacardApplyWaitAuditCount(){
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		wrapper.eq("state", PlacardApply.state_wait_audit);
		return placardApplyMapper.selectCount(wrapper);
	}
	public int getApplyCountByState(int applyState, long supplierId){
		Wrapper<PlacardApply> wrapper = new EntityWrapper<PlacardApply>();
		if(applyState == 0 || applyState == 1 || applyState == 2){
			wrapper.eq("state", applyState);
		}
		wrapper.eq("supplier_id", supplierId);
		return placardApplyMapper.selectCount(wrapper);
	}
}