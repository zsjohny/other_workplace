package com.jiuy.wxaproxy.common.system.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.wxaproxy.common.system.service.IPushWithdrawService;
import com.jiuy.wxaproxy.core.shiro.ShiroKit;
import com.jiuy.wxaproxy.core.shiro.ShiroUser;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.dao.mapper.supplier.GroundWithdrawCashRecordMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.GroundWithdrawCashRecord;
import com.jiuyuan.util.DateUtil;
/**
 * 
 * 
 *
 */
@Service
public class PushWithdrawServiceImpl implements IPushWithdrawService {
	
	private static final Logger logger = LoggerFactory.getLogger(PushWithdrawServiceImpl.class);
	@Autowired
	private GroundWithdrawCashRecordMapper groundWithdrawCashRecordMapper;
    
    @Autowired
    private GroundUserMapper groundUserMapper;
    
	/**
	 * 获取地推提现记录列表
	 * @throws ParseException 
	 */
	public List<Map<String,Object>>  listPage(Page<Map<String, Object>> page,long groundWithdrawCashRecordId, String transactionNo, long groundUserId,String applyBeginTime,
			String applyEndTime, String dealBeginTime, String dealEndTime, double transactionAmountMin,
			double transactionAmountMax, double withdrawCashAmountMin, double withdrawCashAmountMax,Integer status) {
			Wrapper<GroundWithdrawCashRecord> wrapper = new EntityWrapper<GroundWithdrawCashRecord>();
			try{
			if (groundWithdrawCashRecordId!=-1) {
				wrapper.like("id", groundWithdrawCashRecordId+"");
			}
			if (groundUserId !=-1) {
				wrapper.eq("ground_user_id", groundUserId); 
			}
			if (!StringUtils.isEmpty(transactionNo)) {
				wrapper.like("transaction_no", transactionNo);			
			}
			if (!StringUtils.isEmpty(applyBeginTime)) {
				wrapper.ge("apply_time", DateUtil.parseStrTime2Long2(applyBeginTime));
			}
			if (!StringUtils.isEmpty(applyEndTime)) {
				wrapper.le("apply_time",DateUtil.getLongEndTime( DateUtil.parseStrTime2Long2(applyEndTime)) );
			}
			if (!StringUtils.isEmpty(dealBeginTime)) {
				wrapper.ge("finish_time", DateUtil.parseStrTime2Long2(dealBeginTime));
				wrapper.isNotNull("finish_time");
			}
			if (!StringUtils.isEmpty(dealEndTime)) {
				wrapper.le("finish_time", DateUtil.getLongEndTime(DateUtil.parseStrTime2Long2(dealEndTime)));
			}
			if (transactionAmountMin!=-1) {
				wrapper.ge("transaction_amount", transactionAmountMin);
			}
			if (transactionAmountMax!=-1) {
				wrapper.le("transaction_amount", transactionAmountMax);
			}
			if (withdrawCashAmountMin!=-1) {
				wrapper.ge("withdraw_cash_amount", withdrawCashAmountMin);
			}
			if (withdrawCashAmountMax!=-1) {
				wrapper.le("withdraw_cash_amount", withdrawCashAmountMax);
			}
			if (status!=null) {
				wrapper.eq("withdraw_cash_status", status);
			}
			wrapper.orderBy("apply_time",false);
			List<GroundWithdrawCashRecord> groundWithdrawCashRecordList = groundWithdrawCashRecordMapper.selectPage(page, wrapper);
			List<Map<String,Object>> withdrawalList = new ArrayList<Map<String,Object>>();
			DecimalFormat df =new DecimalFormat("#0.00");  
			for (GroundWithdrawCashRecord groundWithdrawCashRecord : groundWithdrawCashRecordList) {
				Map<String,Object> withdrawal = new HashMap<String,Object>();
				withdrawal.put("groundWithdrawCashRecordId", groundWithdrawCashRecord.getId());
				withdrawal.put("groundUserId", groundWithdrawCashRecord.getGroundUserId());
				if (groundWithdrawCashRecord.getTransactionAmount()==null) {
					withdrawal.put("transactionAmount", df.format(0.00));
				}else{
					withdrawal.put("transactionAmount", df.format(groundWithdrawCashRecord.getTransactionAmount()));
				}
				withdrawal.put("withdrawCashAmount", df.format(groundWithdrawCashRecord.getWithdrawCashAmount()));
				withdrawal.put("transactionWay", "银行汇款");
				withdrawal.put("applyTime", DateUtil.parseLongTime2Str(groundWithdrawCashRecord.getApplyTime()));
				Integer withdrawCashStatus = groundWithdrawCashRecord.getWithdrawCashStatus();
				if (groundWithdrawCashRecord.getTransactionNo()!=null) {
					withdrawal.put("transactionNo", groundWithdrawCashRecord.getTransactionNo());
				}else{
					withdrawal.put("transactionNo", "无");
				}
				String  withdrawCashStatusStr="";
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING) {
					withdrawCashStatusStr ="未处理";
					withdrawal.put("finishTime", "无");
				}
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED) {
					withdrawCashStatusStr ="已处理";
					withdrawal.put("finishTime", DateUtil.parseLongTime2Str(groundWithdrawCashRecord.getFinishTime()));
				}
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_REFUSED) {
					withdrawCashStatusStr ="已拒绝";
					withdrawal.put("finishTime", "无");
				}
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE) {
					withdrawCashStatusStr ="已冻结";
					withdrawal.put("finishTime", "无");
				}
//				添加地推用户银行相关信息
				GroundUser groundUser = groundUserMapper.selectById(groundWithdrawCashRecord.getGroundUserId());
				if (groundUser==null) {
					throw new RuntimeException("地推用户不存在请核实！");
				}
				withdrawal.put("bankName", groundUser.getBankName());
				withdrawal.put("bankAcountName", groundUser.getBankAccountName());
				withdrawal.put("bankAcountNo", groundUser.getBankAccountNo());
				withdrawal.put("status", withdrawCashStatusStr);
				withdrawalList.add(withdrawal);
			}
			return withdrawalList;
			}catch (Exception e) {
				logger.error("地推提现申请列表e："+e.getMessage());
				throw new RuntimeException("地推提现申请列表e："+e.getMessage());
			}
	}
	/**
	 * 查看地推提现申请详情
	 */
	public Map<String, Object> getDetailById(long groundWithdrawCashRecordId) {
			GroundWithdrawCashRecord groundWithdrawCashRecord = groundWithdrawCashRecordMapper.selectById(groundWithdrawCashRecordId);
			if(groundWithdrawCashRecord == null){
				throw new RuntimeException("地推提现申请不存在，请核实！");
			}
			Map<String,Object> withdrawal = new HashMap<String,Object>();
			withdrawal.put("groundWithdrawCashRecordId", groundWithdrawCashRecord.getId());
			withdrawal.put("status", groundWithdrawCashRecord.getWithdrawCashStatus());
			DecimalFormat df =new DecimalFormat("#0.00");
			withdrawal.put("withdrawCashAmount", df.format(groundWithdrawCashRecord.getWithdrawCashAmount()));
			if (groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED) {
				withdrawal.put("transactionNo", groundWithdrawCashRecord.getTransactionNo());
				withdrawal.put("transactionAmount", df.format(groundWithdrawCashRecord.getTransactionAmount()));
				withdrawal.put("bankName", groundWithdrawCashRecord.getBankName());
				withdrawal.put("bankAcountName", groundWithdrawCashRecord.getBankAccountName());
				withdrawal.put("bankAcountNo", groundWithdrawCashRecord.getBankAccountNo());
				withdrawal.put("transactionWay", "银行汇款");
				withdrawal.put("remark", groundWithdrawCashRecord.getRemark());
			}
			withdrawal.put("groundUserId", groundWithdrawCashRecord.getGroundUserId());
			withdrawal.put("applyTime", DateUtil.parseLongTime2Str(groundWithdrawCashRecord.getApplyTime()));
			return withdrawal;
	}
	/** 
	 * 地推提现打款
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean playMoneyById(long groundWithdrawCashRecordId, String bankName, String bankAcountName,
			String bankAcountNo, double withdrawCashAmount, double transactionAmount, String transactionNo,
			String remark) {
			GroundWithdrawCashRecord groundWithdrawCashRecord = groundWithdrawCashRecordMapper.selectById(groundWithdrawCashRecordId);
			if(groundWithdrawCashRecord == null){
				throw new RuntimeException("订单不存在请核实！");
				}
			if(groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED){
				throw new RuntimeException("已打款请勿重复打款！");
				}
			if(groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE){
				throw new RuntimeException("已冻结请勿打款请核实！");
				}
			ShiroUser shiroUser = ShiroKit.getUser();
			groundWithdrawCashRecord.setAdminId(shiroUser.getId().longValue());
			groundWithdrawCashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED);
			groundWithdrawCashRecord.setTransactionNo(transactionNo);
			groundWithdrawCashRecord.setTransactionAmount(transactionAmount);
			groundWithdrawCashRecord.setFinishTime(Calendar.getInstance().getTimeInMillis());
			groundWithdrawCashRecord.setBankName(bankName);
			groundWithdrawCashRecord.setBankAccountName(bankAcountName);
			groundWithdrawCashRecord.setBankAccountNo(bankAcountNo); 
			groundWithdrawCashRecord.setRemark(remark);
			groundWithdrawCashRecordMapper.updateById(groundWithdrawCashRecord);
			return true;
	}
	
	
}

	
