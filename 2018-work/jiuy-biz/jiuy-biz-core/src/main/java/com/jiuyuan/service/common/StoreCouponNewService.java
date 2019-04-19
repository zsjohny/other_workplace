package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Status;
import com.jiuyuan.dao.mapper.supplier.StoreCouponNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponTemplateNewMapper;
import com.jiuyuan.dao.mapper.supplier.StoreCouponUseLogNewMapper;
import com.jiuyuan.entity.newentity.StoreCouponNew;
import com.jiuyuan.entity.newentity.StoreCouponTemplateNew;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.DoubleUtil;
import com.jiuyuan.util.TipsMessageException;
import com.xiaoleilu.hutool.db.Entity;

@Service
public class StoreCouponNewService implements IStoreCouponNewService {
	private static final Logger logger = LoggerFactory.getLogger(StoreCouponNewService.class);
	
	@Autowired
	private StoreCouponTemplateNewMapper storeCouponTemplateNewMapper;
	
	@Autowired
	private StoreCouponNewMapper storeCouponNewMapper;
	
	@Autowired
	private StoreCouponUseLogNewMapper storeCouponUseLogNewMapper;
    
	/**
	 * 优惠券模板列表
	 * @throws ParseException 
	 */
	@Override
	public List<Map<String, Object>> list(String couponName, Double minMoney, Double maxMoney, String minValidStartTime,
			String maxValidEndTime, Integer minValidTotalCount, Integer maxValidTotalCount, Double minValidTotalAmount,
			Double maxValidTotalAmount, Integer publishStatus, Long supplierId, Page<Map<String,Object>> page) throws ParseException {
		long currentTime = System.currentTimeMillis();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		//开始搜索
		Wrapper<StoreCouponTemplateNew> wrapper = new EntityWrapper<StoreCouponTemplateNew>();
		//优惠券模板名称
		if(couponName != null && !couponName.equals("")){
			wrapper.like("Name", couponName);
		}
		//面值上下限
		if(minMoney != null){
			wrapper.ge("Money", minMoney);
		}
		if(maxMoney != null){
			wrapper.le("Money", maxMoney);
		}
		//使用有效期上下限
		if(minValidStartTime != null && !minValidStartTime.equals("")){
			long minValidityStartTime = DateUtil.parseStrTime2Long(minValidStartTime);
			wrapper.le("ValidityStartTime", minValidityStartTime);
			wrapper.ge("ValidityEndTime", minValidityStartTime);
		}
		if(maxValidEndTime != null && !minValidStartTime.equals("")){
			long maxValidityEndTime = DateUtil.parseStrTime2Long(maxValidEndTime);
			wrapper.le("ValidityStartTime", maxValidityEndTime);
			wrapper.ge("ValidityEndTime", maxValidityEndTime);
		}
		//券数量
		if(minValidTotalCount != null){
			wrapper.ge("valid_total_count", minValidTotalCount);
		}
		if(maxValidTotalCount != null){
			wrapper.le("valid_total_count", maxValidTotalCount);
		}
		//可用券总额
		if(minValidTotalAmount != null){
			wrapper.ge("valid_total_amount", minValidTotalAmount);
		}
		if(maxValidTotalAmount != null){
				wrapper.le("valid_total_amount", maxValidTotalAmount);
		}
		//发布状态
		if(publishStatus != null && publishStatus != -1){
			if(publishStatus == 4){
				wrapper.le("ValidityEndTime", currentTime);
			}else{
				wrapper.eq("publish_status", publishStatus);
			}
		}
		//供应商
		wrapper.eq("supplier_id", supplierId);
		//状态
		wrapper.eq("Status", Status.NORMAL.getIntValue());
		wrapper.orderBy("Id",false);
		List<Map<String,Object>> list= storeCouponTemplateNewMapper.selectMapsPage(page, wrapper);
		for(Map<String,Object> map : list){
			Map<String,Object> resultMap = new HashMap<String, Object>();
			resultMap.put("Id", map.get("Id"));//优惠券模板ID
			resultMap.put("LimitMoney", map.get("LimitMoney"));//满足的条件金额
			resultMap.put("Money", map.get("Money"));//优惠金额
			resultMap.put("Name", map.get("Name"));//优惠券名称
			resultMap.put("RangeType", map.get("RangeType"));//优惠券使用范围类型 0：通用 4：邮费
			resultMap.put("Status", map.get("Status"));//优惠券状态 -1：删除 0：正常
			resultMap.put("Type", map.get("Type"));//优惠券类型 0：代金券
			long validityStartTime = (long)map.get("ValidityStartTime");
			long validityEndTime = (long)map.get("ValidityEndTime");
			long createTime = (long) map.get("CreateTime");
			long drawStartTime = (long) map.get("drawStartTime");
			long drawEndTime = (long) map.get("drawEndTime");
			long updateTime = (long) map.get("UpdateTime");
			int validTotalCount = (Integer) map.get("validTotalCount");
			int usedCount = (Integer) map.get("usedCount");
			int totalCount = validTotalCount+usedCount;
			resultMap.put("UpdateTime", DateUtil.parseLongTime2Str(updateTime));//更新时间
			resultMap.put("ValidityStartTime", DateUtil.parseLongTime2Str(validityStartTime));//使用有效期开始时间
			resultMap.put("ValidityEndTime", DateUtil.parseLongTime2Str(validityEndTime));//使用有效期结束时间
			resultMap.put("drawStartTime", DateUtil.parseLongTime2Str(drawStartTime));//领取开始时间
			resultMap.put("drawEndTime", DateUtil.parseLongTime2Str(drawEndTime));//领取结束时间
			resultMap.put("CreateTime", DateUtil.parseLongTime2Str(createTime));//创建时间
			resultMap.put("drawStatus", map.get("drawStatus"));//领取状态
			resultMap.put("limitDraw", map.get("limitDraw"));//优惠券限领个数
			resultMap.put("cancelCount", map.get("cancelCount"));//优惠券作废个数
			resultMap.put("usedCount", map.get("usedCount"));//优惠券已使用个数
			resultMap.put("supplierId", map.get("supplierId"));//供应商ID
			if(validityEndTime >= currentTime){
				resultMap.put("validTotalAmount", DoubleUtil.mul(((BigDecimal) map.get("Money")).doubleValue(), totalCount));//有用优惠券总额
				resultMap.put("validTotalCount", totalCount);//有用优惠券总数
				resultMap.put("overtimeCount", 0);//优惠券过期个数
				resultMap.put("isOvertime", 0);//未过期
			}else{
				resultMap.put("validTotalAmount", DoubleUtil.mul(((BigDecimal) map.get("Money")).doubleValue(), totalCount));//有用优惠券总额
				resultMap.put("validTotalCount", totalCount);//有用优惠券总数
				resultMap.put("overtimeCount", map.get("validTotalCount"));//优惠券过期个数
				resultMap.put("isOvertime", 1);//已过期
			}
			resultMap.put("publishStatus", map.get("publishStatus"));//发放状态 -1：全部 0：未发放 1：发放中 2：已停止 3：已作废 4:已过期
			resultMap.put("publisher", map.get("publisher"));//发行商
			
			int canDelOrUpdate = canDelOrUpdate((Long)map.get("Id"),(Long) map.get("supplierId"));
			resultMap.put("canDelOrUpdate", canDelOrUpdate);// 0:能够删除和编辑  1:不能删除和编辑
			
			result.add(resultMap);
		}
		return result;
	}
	private int canDelOrUpdate(Long TemplateId, Long supplierId) {
		Wrapper<StoreCouponNew> wrapper = new EntityWrapper<StoreCouponNew>();
		wrapper.eq("CouponTemplateId", TemplateId)
		       .eq("supplier_id", supplierId);
		Integer count = storeCouponNewMapper.selectCount(wrapper);
		if(count > 0){
			return 1;
		}
		return 0;
	}
	/**
	 * 添加优惠券模板
	 */
	@Override
	public void add(StoreCouponTemplateNew storeCouponTemplateNew) {
		//校验参数
		checkParam(storeCouponTemplateNew);
		//添加优惠券模板
		addSupplierCouponTemplate(storeCouponTemplateNew);
		
		
	}
	/**
	 * 编辑优惠券模板
	 */
    @Override
	public void update(StoreCouponTemplateNew storeCouponTemplateNew) {
    	//校验该优惠券是否能够编辑
    	checkUpdate(storeCouponTemplateNew);
    	//校验该优惠券参数
    	checkParam(storeCouponTemplateNew);
    	//更新优惠券模板
    	updateSupplierCouponTemplate(storeCouponTemplateNew);
	}
    
    /**
     * 校验该优惠券是否能够编辑
     */
	private void checkUpdate(StoreCouponTemplateNew storeCouponTemplateNew) {
		long templateId = storeCouponTemplateNew.getId();
		StoreCouponTemplateNew templateNew = storeCouponTemplateNewMapper.selectById(templateId);
		int publishStatus = templateNew.getPublishStatus();
		//未曾领取过的优惠券模板才能编辑
		// TODO
//		if(publishStatus != StoreCouponTemplateNew.NO_PUBLISH){
//			logger.info("只有未发放的优惠券才能编辑，该优惠券并不是出于该状态！"+",templateId:"+templateId);
//			throw new TipsMessageException("只有未发放的优惠券才能编辑，该优惠券并不是出于该状态！");
//		}
		List<StoreCouponNew> list = getStoreCouponByTemplateId(templateId, storeCouponTemplateNew.getSupplierId());
		if(list.size() > 0){
			logger.info("该优惠券已经有客户领取，无法编辑！"+",templateId"+templateId);
			throw new TipsMessageException("保存失败！优惠券已经发放");
		}
		int status = templateNew.getStatus();
		if(status != Status.NORMAL.getIntValue()){
			logger.info("保存失败！优惠券不存在或已被删除"+",templateId:"+templateId);
			throw new TipsMessageException("保存失败！优惠券不存在或已被删除");
		}
		if(templateNew.getPublishStatus() == StoreCouponTemplateNew.STOPED){
			logger.info("保存失败！优惠券已停止领取"+",templateId:"+templateId);
			throw new TipsMessageException("保存失败！优惠券已停止领取");
		}
		if(!templateNew.getSupplierId().equals(storeCouponTemplateNew.getSupplierId()) ){
			logger.info("该供应商没有权限编辑该优惠券！"+",templateId:"+templateId);
			throw new TipsMessageException("该供应商没有权限编辑该优惠券！");
		}
	}
	
	private List<StoreCouponNew> getStoreCouponByTemplateId(long templateId, Long supplierId) {
		Wrapper<StoreCouponNew> wrapper = new EntityWrapper<StoreCouponNew>();
		wrapper.eq("CouponTemplateId", templateId)
		       .eq("supplier_id", supplierId);
		List<StoreCouponNew> list = storeCouponNewMapper.selectList(wrapper);
		return list;
		
		
	}
	/**
	 * 更新优惠券模板
	 */
	private void updateSupplierCouponTemplate(StoreCouponTemplateNew storeCouponTemplateNew) {
		long currentTime = System.currentTimeMillis();
		Integer validTotalCount = storeCouponTemplateNew.getValidTotalCount();
		BigDecimal money = storeCouponTemplateNew.getMoney();
		BigDecimal validTotalAmount = money.multiply(new BigDecimal(validTotalCount));
		validTotalAmount = validTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		StoreCouponTemplateNew templateNew = storeCouponTemplateNewMapper.selectById(storeCouponTemplateNew.getId());
		
		templateNew.setName(storeCouponTemplateNew.getName());//优惠券模板名称
		templateNew.setLimitMoney(storeCouponTemplateNew.getLimitMoney());//优惠券满足条件
		templateNew.setMoney(money);//优惠金额
		
		templateNew.setValidityStartTime(storeCouponTemplateNew.getValidityStartTime());//优惠券有效期开始时间
		templateNew.setValidityEndTime(storeCouponTemplateNew.getValidityEndTime());//优惠券有效期结束时间
		templateNew.setDrawStartTime(storeCouponTemplateNew.getDrawStartTime());//优惠券模板领取开始时间
		templateNew.setDrawEndTime(storeCouponTemplateNew.getDrawEndTime());//优惠券模板领取结束时间
		
		templateNew.setValidTotalCount(storeCouponTemplateNew.getValidTotalCount());//优惠券可用优惠券的总数
		templateNew.setLimitDraw(storeCouponTemplateNew.getLimitDraw());//限领个数
		templateNew.setValidTotalAmount(validTotalAmount);//优惠券可用总额
		templateNew.setUpdateTime(currentTime);//优惠券模板更新时间
		
		templateNew.setRangeType(storeCouponTemplateNew.getRangeType());//优惠券模板应用范围
		storeCouponTemplateNewMapper.updateById(templateNew);
	}
	/**
     * 添加优惠券模板
     */
	private void addSupplierCouponTemplate(StoreCouponTemplateNew storeCouponTemplateNew) {
		long currentTime = System.currentTimeMillis();
		Integer validTotalCount = storeCouponTemplateNew.getValidTotalCount();
		BigDecimal money = storeCouponTemplateNew.getMoney();
		BigDecimal validTotalAmount = money.multiply(new BigDecimal(validTotalCount));
		validTotalAmount = validTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		storeCouponTemplateNew.setType(StoreCouponTemplateNew.TYPE_COUPON);//优惠券
		storeCouponTemplateNew.setStatus(Status.NORMAL.getIntValue());//优惠券状态
		storeCouponTemplateNew.setCreateTime(currentTime);//优惠券模板创建时间
		storeCouponTemplateNew.setUpdateTime(currentTime);//优惠券模板更新时间
		//可用券总和
		storeCouponTemplateNew.setValidTotalAmount(validTotalAmount);//优惠券可用总数
		storeCouponTemplateNew.setUsedCount(0);//优惠券已使用数目
		storeCouponTemplateNew.setCancelCount(0);//优惠券作废数目
		storeCouponTemplateNew.setDrawStatus(0);//优惠券领取状态
		storeCouponTemplateNew.setPublisher(storeCouponTemplateNew.getPublisher());//发行商
		storeCouponTemplateNew.setPublishStatus(StoreCouponTemplateNew.PUBLISHING);//发布中
		
		logger.info(storeCouponTemplateNew.toString());
		storeCouponTemplateNewMapper.insert(storeCouponTemplateNew);
		
	}

	private void checkParam(StoreCouponTemplateNew storeCouponTemplateNew) {
		long supplierId = storeCouponTemplateNew.getSupplierId();
		long currentTime = System.currentTimeMillis();
		//检验优惠券名称
		if(storeCouponTemplateNew.getName().equals("") ){
			logger.info("优惠券名称必填！"+"supplierId:"+supplierId);
			throw new TipsMessageException("优惠券名称必填！");
		}
		if(storeCouponTemplateNew.getName().length() >= 45){
			logger.info("优惠券名称过长！"+"supplierId:"+supplierId);
			throw new TipsMessageException("优惠券名称过长！");
		}
		//时间的校验
		long drawStartTime = storeCouponTemplateNew.getDrawStartTime();
		long drawEndTime = storeCouponTemplateNew.getDrawEndTime();
		long validStartTime = storeCouponTemplateNew.getValidityStartTime();
		long validEndTime = storeCouponTemplateNew.getValidityEndTime();
		if(
			drawStartTime <= currentTime ||
			drawEndTime <= currentTime ||
			validStartTime <= currentTime ||				
			validEndTime <= currentTime 
				){
			logger.info("优惠券设置时间必须大于当前时间！"+"supplierId:"+supplierId);
			throw new TipsMessageException("优惠券设置时间必须大于当前时间！");
		}
		if(drawStartTime >= drawEndTime){
			logger.info("领取结束时间必须大于领取开始时间！"+"supplierId:"+supplierId);
			throw new TipsMessageException("领取结束时间必须大于领取开始时间！");
		}
		if(validStartTime >= validEndTime){
			logger.info("使用结束时间必须大于使用开始时间！"+"supplierId:"+supplierId);
			throw new TipsMessageException("使用结束时间必须大于使用开始时间！");
		}
		//校验数目大于0
		Integer validTotalCount = storeCouponTemplateNew.getValidTotalCount();
		Integer limitDraw = storeCouponTemplateNew.getLimitDraw();
		Double limitMoney = storeCouponTemplateNew.getLimitMoney().doubleValue();
		Double money = storeCouponTemplateNew.getMoney().doubleValue();
		if(limitDraw <= 0 ||
		   validTotalCount <= 0 ){
			logger.info("数目设置不合理！"+"supplierId:"+supplierId);
			throw new TipsMessageException("数目设置不合理"+"supplierId:"+supplierId);
		}
		if(limitMoney < 0 ||
		   money < 0 ){
			logger.info("金额设置不合理！"+"supplierId:"+supplierId);
			throw new TipsMessageException("金额设置不合理！");
		}
		
	}
	/**
	 * 获取当前可用优惠券总额，历史消费优惠券总额
	 */
	@Override
	public Map<String, Object> getStatistics(long userId) {
		long currentTime = System.currentTimeMillis();
		Map<String,Object> map = new HashMap<String,Object>();
		//获取当前可用优惠券总额
		Wrapper<StoreCouponTemplateNew> currentTotalAmountWrapper = new EntityWrapper<StoreCouponTemplateNew>();
		List<Integer> list = new ArrayList<Integer>();
		list.add(StoreCouponTemplateNew.NO_PUBLISH);
		list.add(StoreCouponTemplateNew.PUBLISHING);
		list.add(StoreCouponTemplateNew.STOPED);
		//publish_status -1：全部 0：未发放 1：发放中  2：已停止  3：已作废
		currentTotalAmountWrapper.in("publish_status", list)
		       .eq("supplier_id", userId)
		       .eq("Status", Status.NORMAL)
		       .ge("ValidityEndTime", currentTime);
		List<StoreCouponTemplateNew> templateList = storeCouponTemplateNewMapper.selectList(currentTotalAmountWrapper);
		BigDecimal currentTotalAmount = new BigDecimal(0);
		for(StoreCouponTemplateNew storeCouponTemplateNew : templateList){
			BigDecimal validTotalAmount = storeCouponTemplateNew.getValidTotalAmount();
			currentTotalAmount = currentTotalAmount.add(validTotalAmount);
		}

		//四舍五入保留小数点后两位
		currentTotalAmount = currentTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        //获取历史消费优惠券总额
		Wrapper<StoreCouponTemplateNew> historyUsedCouponAmountWrapper = new EntityWrapper<StoreCouponTemplateNew>();
		historyUsedCouponAmountWrapper.eq("supplier_id", userId)
		                              .eq("Status", Status.NORMAL);
		List<StoreCouponTemplateNew> historyList = storeCouponTemplateNewMapper.selectList(historyUsedCouponAmountWrapper);
		BigDecimal historyUsedCouponAmount = new BigDecimal(0);
		for(StoreCouponTemplateNew storeCouponTemplateNew : historyList){
			int usedCount = storeCouponTemplateNew.getUsedCount();
			BigDecimal money = storeCouponTemplateNew.getMoney();
			BigDecimal total = new BigDecimal(0);
			total = money.multiply(new BigDecimal(usedCount));
			historyUsedCouponAmount = historyUsedCouponAmount.add(total);
		}
		
		map.put("currentTotalAmount", currentTotalAmount);
		map.put("historyUsedCouponAmount", historyUsedCouponAmount);
		return map;
	}
	/**
	 * 删除优惠券模板
	 */
	@Override
	public void delete(long storeCouponTemplateId, long supplierId) {
		//获取
		StoreCouponTemplateNew storeCouponTemplateNew = storeCouponTemplateNewMapper.selectById(storeCouponTemplateId);
		//校验是否能够删除优惠券模板
		checkDelete(storeCouponTemplateNew, supplierId);
		//开始删除优惠券模板
		deleteSupplierCouponTemplate(storeCouponTemplateId);
		
	}
	private void deleteSupplierCouponTemplate(long storeCouponTemplateId) {
		StoreCouponTemplateNew storeCouponTemplateNew = new StoreCouponTemplateNew();
		storeCouponTemplateNew.setId(storeCouponTemplateId);
		storeCouponTemplateNew.setStatus(Status.DELETE.getIntValue());
		storeCouponTemplateNewMapper.updateById(storeCouponTemplateNew);
		
	}
	private void checkDelete(StoreCouponTemplateNew storeCouponTemplateNew, long supplierId) {
		//是否有权删除优惠券
		if(storeCouponTemplateNew.getSupplierId() != supplierId){
			logger.info("无权修改该优惠券！"+",supplierId"+supplierId);
			throw new TipsMessageException("无权修改该优惠券！"+",supplierId"+supplierId);
		}
		//是否能够删除
		List<StoreCouponNew> list = getStoreCouponByTemplateId(storeCouponTemplateNew.getId(), supplierId);
		if(list.size() > 0){
			logger.info("该优惠券已经有客户领取，无法删除！"+",templateId"+storeCouponTemplateNew.getId());
			throw new TipsMessageException("该优惠券已经有客户领取，无法删除！");
		}
		//是否已经删除
		if(storeCouponTemplateNew.getStatus() == Status.DELETE.getIntValue()){
			logger.info("该优惠券已经删除，请勿重复删除！"+",templateId"+storeCouponTemplateNew.getId());
			throw new TipsMessageException("该优惠券已经删除，请勿重复删除！");
		}
		
		
	}
	
	/**
	 * 停止领取优惠券
	 */
	@Override
	public void stopDrawStoreCoupon(long supplierId, long storeTemplateId) {
		//获取该优惠券模板
		Wrapper<StoreCouponTemplateNew> wrapper = new EntityWrapper<StoreCouponTemplateNew>();
		wrapper.eq("id", storeTemplateId)
		       .eq("supplier_id", supplierId)
		       .eq("Status", Status.NORMAL.getIntValue())
		       .andNew("")
		       .eq("publish_status", StoreCouponTemplateNew.PUBLISHING);
		List<StoreCouponTemplateNew> storeCouponTemplateNewList = storeCouponTemplateNewMapper.selectList(wrapper);
		if(storeCouponTemplateNewList.size() <= 0){
			logger.info("该优惠券不存在,已删除或者已作废！"+"supplierId"+supplierId);
			throw new TipsMessageException("该优惠券不存在,已删除或者已作废！");
		}
		int drawStatus = storeCouponTemplateNewList.get(0).getDrawStatus();
		int status = storeCouponTemplateNewList.get(0).getStatus();
		if(drawStatus == StoreCouponTemplateNew.DRAW_STATUS_CANT ||
			status == StoreCouponTemplateNew.STOPED	){
			logger.info("该优惠券已经停止领取！"+"supplierId"+supplierId);
			throw new TipsMessageException("该优惠券已经停止领取！");
		}
		//更改领取状态
		StoreCouponTemplateNew storeCouponTemplateNew = new StoreCouponTemplateNew();
		storeCouponTemplateNew.setPublishStatus(StoreCouponTemplateNew.STOPED);//发放状态
		storeCouponTemplateNew.setId(storeTemplateId);
		storeCouponTemplateNew.setDrawStatus(StoreCouponTemplateNew.DRAW_STATUS_CANT);//领取状态
		storeCouponTemplateNew.setUpdateTime(System.currentTimeMillis());//更新时间
		storeCouponTemplateNewMapper.updateById(storeCouponTemplateNew);
		
		
	}
	
	/**
	 * 品牌列表领取优惠券
	 */
	@Override
	public List<Map<String, Object>> brandListDrawCoupon(long brandId) {
		//获取该品牌列表
		List<Map<String,Object>> list = storeCouponTemplateNewMapper.getSupplierCouponTemplate(brandId);
		for(Map<String,Object> map :list){
			BigDecimal limitMoney = (BigDecimal)map.get("LimitMoney");
			String limitMoneyStr = limitMoney.toString();
			int dot = limitMoneyStr.indexOf(".");
			int lastZero = limitMoneyStr.lastIndexOf("0");
			if(dot != -1){
				if(lastZero > dot){
					while(limitMoneyStr.endsWith("0")){
						limitMoneyStr = limitMoneyStr.substring(0, limitMoneyStr.length()-1);
					}
					if(limitMoneyStr.endsWith(".")){
						limitMoneyStr = limitMoneyStr.substring(0,limitMoneyStr.length()-1);
					}
				}
			}
			long ValidityEndTime = (Long)map.get("ValidityEndTime");
			long ValidityStartTime = (Long)map.get("ValidityStartTime");
			StringBuffer preferentialCondition = new StringBuffer();
			if(limitMoney.doubleValue() <= 0){
				preferentialCondition.append("无门槛");
			}else{
				preferentialCondition.append("满")
				                  .append(limitMoneyStr)
				                  .append("元使用");
			}
			StringBuffer validTime = new StringBuffer("有效期:");
					validTime.append(DateUtil.getDateByLongTime(ValidityStartTime))
					         .append("～")
					         .append(DateUtil.getDateByLongTime(ValidityEndTime));
			map.put("preferentialCondition", preferentialCondition);
			map.put("validTime", validTime);
		}
		return list;
	}
	/**
	 * 领取优惠券
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void drawSupplierCouponTemplate(long supplierCouponTemplateId, long userId , long businessNumber) {
		//校验该优惠券模板是否能够领取
		checkDrawStoreCouponTemplate(supplierCouponTemplateId, userId);
		//开始领取优惠券
		drawSupplierCoupon(supplierCouponTemplateId, userId, businessNumber);
//		//该优惠券是否领取完
//		return checkCouponDrawedOver(supplierCouponTemplateId, userId);
	}
//	/**
//	 * 该优惠券是否领取完
//	 * @param supplierCouponTemplateId
//	 * @param userId
//	 */
//	private int checkCouponDrawedOver(long supplierCouponTemplateId, long userId) {
//		storeCouponTemplateNewMapper.checkCouponDrawedOver();
//	}
	/**
	 * 开始领取优惠券
	 * @param supplierCouponTemplateId
	 * @param userId
	 * @param businessNumber 
	 */
	private void drawSupplierCoupon(long supplierCouponTemplateId, long userId, long businessNumber) {
		StoreCouponTemplateNew storeCouponTemplateNew = storeCouponTemplateNewMapper.selectById(supplierCouponTemplateId);
		long currentTime = System.currentTimeMillis();
		//添加优惠券
		StoreCouponNew storeCouponNew = new StoreCouponNew();
		
		storeCouponNew.setCouponTemplateId(supplierCouponTemplateId);//模板ID
		storeCouponNew.setTemplateName(storeCouponTemplateNew.getName());//模板名称
		storeCouponNew.setType(StoreCouponNew.TYPE_GIVEN_USER);//指定用户
		storeCouponNew.setMoney(storeCouponTemplateNew.getMoney().doubleValue());//面值
		storeCouponNew.setRangeType(storeCouponTemplateNew.getRangeType());//使用范围
		storeCouponNew.setValidityStartTime(storeCouponTemplateNew.getValidityStartTime());//有效期开始时间
		storeCouponNew.setValidityEndTime(storeCouponTemplateNew.getValidityEndTime());//有效期结束时间
		storeCouponNew.setStoreId(userId);//用户ID
		storeCouponNew.setBusinessNumber(businessNumber);//用户商家号
		storeCouponNew.setStatus(Status.NORMAL.getIntValue());//优惠券状态
		storeCouponNew.setCreateTime(currentTime);//优惠券创建时间
		storeCouponNew.setUpdateTime(currentTime);//优惠券更新时间
		storeCouponNew.setPushStatus(-1);//优惠券推送状态
		storeCouponNew.setGetWay(1);//优惠券获取方式
		storeCouponNew.setLimitMoney(storeCouponTemplateNew.getLimitMoney());//优惠券订单限额
		
		storeCouponNew.setRangeTypeIds(storeCouponTemplateNew.getRangeTypeIds());//品牌IDs
		storeCouponNew.setRangeTypeNames(storeCouponTemplateNew.getRangeTypeNames());//品牌名称s
		
		storeCouponNew.setSupplierId(storeCouponTemplateNew.getSupplierId());//优惠券的发放供应商ID
		storeCouponNew.setDrawTime(currentTime);//优惠券领取时间
		storeCouponNew.setDrawStartTime(storeCouponTemplateNew.getDrawStartTime());//优惠券开始领取时间
		storeCouponNew.setDrawEndTime(storeCouponTemplateNew.getDrawEndTime());//优惠券结束领取时间
		storeCouponNew.setPublisher(storeCouponTemplateNew.getPublisher());//优惠券发放商
		
		storeCouponNewMapper.insert(storeCouponNew);
		
	}
	/**
	 * 校验该优惠券模板是否能够领取
	 * @param supplierCouponTemplateId
	 * @param userId
	 */
	private void checkDrawStoreCouponTemplate(long supplierCouponTemplateId, long userId) {
		long currentTime = System.currentTimeMillis();
		//校验该优惠券模板是否能够领券.
		StoreCouponTemplateNew storeCouponTemplateNew = storeCouponTemplateNewMapper.selectById(supplierCouponTemplateId);
		//检测该优惠券模板的状态
		int status = storeCouponTemplateNew.getStatus();
		if(status != Status.NORMAL.getIntValue()){
			logger.info("该优惠券已经被删除了！"+",supplierCouponTemplateId:"+supplierCouponTemplateId+",status:"+status);
			throw new TipsMessageException("该优惠券已经被删除了！");
		}
		//检测该优惠券模板的发布状态
		int publishStatus = storeCouponTemplateNew.getPublishStatus();
		if(publishStatus != StoreCouponTemplateNew.PUBLISHING ){
			if(publishStatus == StoreCouponTemplateNew.NO_PUBLISH){
				logger.info("该优惠券还没有发布，无法领取！"+",supplierCouponTemplateId:"+supplierCouponTemplateId+",publishStatus:"+publishStatus);
				throw new TipsMessageException("该优惠券还没有发布，无法领取！");
			}
			if(publishStatus == StoreCouponTemplateNew.STOPED){
				logger.info("该优惠券已经停止领取，无法领取！"+",supplierCouponTemplateId:"+supplierCouponTemplateId+",publishStatus:"+publishStatus);
				throw new TipsMessageException("该优惠券已经停止领取，无法领取！");
			}
			if(publishStatus == StoreCouponTemplateNew.CANCELL){
				logger.info("该优惠券已作废，无法领取！"+",supplierCouponTemplateId:"+supplierCouponTemplateId+",publishStatus:"+publishStatus);
				throw new TipsMessageException("该优惠券已作废，无法领取！");
			}
		}
		//检测是否处于停止领取优惠券的时候
		if(currentTime < storeCouponTemplateNew.getDrawStartTime() ||
		   currentTime > storeCouponTemplateNew.getDrawEndTime()){
			logger.info("当前时间不在领取该优惠券的时间段！"+",supplierCouponTemplateId:"+supplierCouponTemplateId);
			throw new TipsMessageException("当前时间不在领取该优惠券的时间段！");
		}
		//检测该优惠券模板的可用优惠券个数
		int validTotalCount = storeCouponTemplateNew.getValidTotalCount();

		//获取已经领取可用的优惠券数量
		int drawedCouponCount = this.getDrawedCouponCountByTemplateId(supplierCouponTemplateId);
		if(drawedCouponCount >= validTotalCount){
			logger.info("该优惠券已经领取完毕！"+",supplierCouponTemplateId:"+supplierCouponTemplateId);
			throw new TipsMessageException("该优惠券已经领取完毕！");
			
		}
		//检测该优惠券模板的限领个数
		int limitDraw = storeCouponTemplateNew.getLimitDraw();
		//获取该用户已经领取的优惠券数量
		int drawedCouponIndividualCount = this.DrawedCouponCountByTemplateIdAndUserId(supplierCouponTemplateId, userId);
		if(limitDraw <= drawedCouponIndividualCount){
			logger.info("该优惠券每人只能领取"+limitDraw+"张！您已经无法继续领取！"+",supplierCouponTemplateId:"+supplierCouponTemplateId+",userId:"+userId);
			throw new TipsMessageException("该优惠券每人只能领取"+limitDraw+"张！您已经无法继续领取！");
		}
		
		
	}
	
	private int DrawedCouponCountByTemplateIdAndUserId(long supplierCouponTemplateId, long userId) {
		Wrapper<StoreCouponNew> wrapper = new EntityWrapper<StoreCouponNew>();
		wrapper.eq("CouponTemplateId" , supplierCouponTemplateId)
		       .eq("StoreId" , userId);
		return storeCouponNewMapper.selectCount(wrapper);
	}
	
	private int getDrawedCouponCountByTemplateId(long supplierCouponTemplateId) {
		Wrapper<StoreCouponNew> wrapper = new EntityWrapper<StoreCouponNew>();
		wrapper.eq("CouponTemplateId", supplierCouponTemplateId);
		return storeCouponNewMapper.selectCount(wrapper);
	}
	@Override
	public Map<String, Object> detail(long storeCouponTemplateId, long userId) {
		StoreCouponTemplateNew storeCouponTemplateNew = storeCouponTemplateNewMapper.selectById(storeCouponTemplateId);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("Id", storeCouponTemplateNew.getId());//优惠券模板ID
		resultMap.put("LimitMoney", storeCouponTemplateNew.getLimitMoney());//满足的条件金额
		resultMap.put("Money", storeCouponTemplateNew.getMoney());//优惠金额
		resultMap.put("Name", storeCouponTemplateNew.getName());//优惠券名称
		long validityStartTime = storeCouponTemplateNew.getValidityStartTime();
		long validityEndTime = storeCouponTemplateNew.getValidityEndTime();
		long drawStartTime = storeCouponTemplateNew.getDrawStartTime();
		long drawEndTime = storeCouponTemplateNew.getDrawEndTime();
		resultMap.put("ValidityStartTime", DateUtil.parseLongTime2Str(validityStartTime));//使用有效期开始时间
		resultMap.put("ValidityEndTime", DateUtil.parseLongTime2Str(validityEndTime));//使用有效期结束时间
		resultMap.put("drawStartTime", DateUtil.parseLongTime2Str(drawStartTime));//领取开始时间
		resultMap.put("drawEndTime", DateUtil.parseLongTime2Str(drawEndTime));//领取结束时间
		resultMap.put("supplierId", storeCouponTemplateNew.getSupplierId());//供应商ID
		resultMap.put("validTotalAmount", storeCouponTemplateNew.getValidTotalAmount());//有用优惠券总额
		resultMap.put("validTotalCount", storeCouponTemplateNew.getValidTotalCount());//有用优惠券总数
		resultMap.put("publisher", storeCouponTemplateNew.getPublisher());//发行商
		resultMap.put("limitDraw", storeCouponTemplateNew.getLimitDraw());//限领
		return resultMap;
	}
	@Override
	public void doStatisticsByCouponTemplateIdWhenUse(long couponTemplateId) {
		StoreCouponTemplateNew storeCouponTemplateNew = storeCouponTemplateNewMapper.selectById(couponTemplateId);
		Integer validTotalCount = storeCouponTemplateNew.getValidTotalCount();
		BigDecimal validTotalAmount = storeCouponTemplateNew.getValidTotalAmount();
		BigDecimal money = storeCouponTemplateNew.getMoney();
		Integer usedCount = storeCouponTemplateNew.getUsedCount();
		StoreCouponTemplateNew storeCouponTemplateNew2 = new StoreCouponTemplateNew();
		validTotalCount--;
		usedCount++;
		validTotalAmount = validTotalAmount.subtract(money);
		storeCouponTemplateNew2.setId(couponTemplateId);
		storeCouponTemplateNew2.setValidTotalCount(validTotalCount);
		storeCouponTemplateNew2.setValidTotalAmount(validTotalAmount);
		storeCouponTemplateNew2.setUsedCount(usedCount);
		storeCouponTemplateNewMapper.updateById(storeCouponTemplateNew2);
		
	}
	
	

}
