package com.jiuyuan.service.common;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.dao.mapper.supplier.GroundDayReportMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.dao.mapper.supplier.GroundWithdrawCashRecordMapper;
import com.jiuyuan.entity.GroundUserMonthCost;
import com.jiuyuan.entity.newentity.GroundConditionRule;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.GroundWithdrawCashRecord;
import com.jiuyuan.util.DateUtil;

/**
 * <p>
 * 地推人员提现申请表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */

@Service
public class GroundWithdrawCashRecordService implements IGroundWithdrawCashRecordService{
	private static final Logger logger = LoggerFactory.getLogger(GroundWithdrawCashRecordService.class);
	@Autowired
	private GroundWithdrawCashRecordMapper groundWithdrawCashRecordMapper;
	
	@Autowired
    private GroundDayReportMapper groundDayReportMapper;
    
    @Autowired
    private GroundUserMapper groundUserMapper;
    
    @Autowired
	private GroundConditionRuleMapper groundConditionRuleMapper;
	/**
	 * 自动提现
	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
    public void execute() {
    try{
	List<GroundUserMonthCost> groundUserMonthCostList = groundDayReportMapper.getGroundUserMonthCost(DateUtil.getBeforeFirstMonthdate(), DateUtil.getBeforeLastMonthdate());
//		List<GroundUserMonthCost> groundUserMonthCostList = groundDayReportMapper.getGroundUserMonthCost(20171101, 20171130);	
		for (GroundUserMonthCost groundUserMonthCost : groundUserMonthCostList) {
    		Long groundUserId = groundUserMonthCost.getGroundUserId();
    		GroundUser groundUser = groundUserMapper.selectById(groundUserId);
			double costCount = groundUserMonthCost.getCostCount();
			logger.info("自动提现-----sql执行-----");
			logger.info("自动提现-----sql执行-----");
			logger.info("groundUserId："+groundUserId+" costCount:"+costCount);
			groundUserMapper.setAvailableBalance(groundUserId, costCount);
//			组装提现记录
    		GroundWithdrawCashRecord groundWithdrawCashRecord=new GroundWithdrawCashRecord();
    		groundWithdrawCashRecord.setApplyNo(DateUtil.getRandomNumber());
    		groundWithdrawCashRecord.setApplyTime(Calendar.getInstance().getTimeInMillis());
    		groundWithdrawCashRecord.setBankAccountName(groundUser.getBankAccountName());
    		groundWithdrawCashRecord.setBankAccountNo(groundUser.getBankAccountNo());
    		groundWithdrawCashRecord.setBankName(groundUser.getBankName());
    		groundWithdrawCashRecord.setFinishTime(Calendar.getInstance().getTimeInMillis());
    		groundWithdrawCashRecord.setGroundUserId(groundUserId);
			groundWithdrawCashRecord.setGroundUserName(groundUser.getName());
			groundWithdrawCashRecord.setGroundUserPhone(groundUser.getPhone());
			groundWithdrawCashRecord.setApplyNo(DateUtil.getRandomNumber());
			groundWithdrawCashRecord.setWithdrawCashAmount(costCount);
//			1：禁用
			if (groundUser.getStatus()==1) {
				groundWithdrawCashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE);
			}else{
				groundWithdrawCashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED);
			}
			groundWithdrawCashRecord.setTransactionNo("");
			groundWithdrawCashRecord.setWithdrawType(1);
			groundWithdrawCashRecord.setRemark("系统自动");
			logger.info("groundUserId："+groundUserId+"costCount:"+costCount+"正在插入该地推人员的提现记录");
			groundWithdrawCashRecordMapper.insert(groundWithdrawCashRecord);
			logger.info("自动提现-----插入提现记录成功------");
			}
		} catch (Exception e) {
			logger.error("自动提现e:"+e.getMessage());
		}
    }
    
	/**
	 * 获取提现申请记录
	 * @param type
	 * @param grandUserId
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String,Object>> getMyWithdrawalList(long applyNo,Integer type, long grandUserId,Page<GroundWithdrawCashRecord> page) throws ParseException {
			Wrapper<GroundWithdrawCashRecord> wrapper = new EntityWrapper<GroundWithdrawCashRecord>();
			//
			if (type!=null) {
				wrapper.eq("withdraw_cash_status",GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING);
			}
			if (applyNo>0) {
				wrapper.like("apply_no", String.valueOf(applyNo));
			}
			wrapper.eq("ground_user_id",grandUserId);
			wrapper.orderBy("apply_time",false);
			
			List<GroundWithdrawCashRecord> groundWithdrawCashRecordList = groundWithdrawCashRecordMapper.selectPage(page, wrapper);
			
//			List<GroundWithdrawCashRecord> groundWithdrawCashRecordList = groundWithdrawCashRecordMapper.selectList(wrapper);
			List<Map<String,Object>> withdrawalList = new ArrayList<Map<String,Object>>();
			for (GroundWithdrawCashRecord groundWithdrawCashRecord : groundWithdrawCashRecordList) {
				long applyTime = groundWithdrawCashRecord.getApplyTime();
				Map<String,Object> withdrawal = new HashMap<String,Object>();
				withdrawal.put("groundWithdrawCashRecordId", groundWithdrawCashRecord.getId());
				String todayOrYestoday = getJZtian(applyTime);
				if(todayOrYestoday!=null){
					withdrawal.put("timeDay", todayOrYestoday);
				}else{
					withdrawal.put("timeDay", DateUtil.parseLongTime2Str2(applyTime));
				}
				DecimalFormat df =new DecimalFormat("#0.00");
				withdrawal.put("timeHours",new SimpleDateFormat("HH:mm:ss").format(new Date(applyTime)));
				withdrawal.put("withdrawalMoney", df.format(groundWithdrawCashRecord.getWithdrawCashAmount()));
				withdrawal.put("withdrawalNumber", groundWithdrawCashRecord.getApplyNo());
				Integer withdrawCashStatus = groundWithdrawCashRecord.getWithdrawCashStatus();
				String  withdrawCashStatusStr="";
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING) {
					withdrawCashStatusStr ="处理中";
				}
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED) {
					withdrawCashStatusStr ="已完成";
				}
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_REFUSED) {
					withdrawCashStatusStr ="已拒绝";
				}
				if (withdrawCashStatus==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE) {
					withdrawCashStatusStr ="已冻结";
				}
				withdrawal.put("status", withdrawCashStatusStr);
				withdrawalList.add(withdrawal);
			}
			Map<String,Object> pageMap = new HashMap<String,Object>();
			//设置分页信息
			pageMap.put("total", page.getTotal());
			pageMap.put("pages", page.getPages());
			withdrawalList.add(pageMap);
			return withdrawalList;
	}
	
	/**
	 * 获取处理中提现记录数
	 * @param type
	 * @param grandUserId
	 * @return
	 */
	public int getUnWithdrawalCount(long grandUserId) {
			Wrapper<GroundWithdrawCashRecord> wrapper = new EntityWrapper<GroundWithdrawCashRecord>();
			wrapper.eq("withdraw_cash_status",GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING);
			wrapper.eq("ground_user_id",grandUserId);
			Integer unWithdrawalCount = groundWithdrawCashRecordMapper.selectCount(wrapper);
			return unWithdrawalCount;
	}

	/**
	 * 获取提现记录详情
	 * @param withdrawalNumber
	 * @return
	 */
	public Map<String, Object> getMyWithdrawalInfo(long groundWithdrawCashRecordId) {
			GroundWithdrawCashRecord groundWithdrawCashRecord = groundWithdrawCashRecordMapper.selectById(groundWithdrawCashRecordId);
			if(groundWithdrawCashRecord==null){
				throw new RuntimeException("订单不存在请核实！");
			}
			String  withdrawCashStatusStr="";
			if (groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING) {
				withdrawCashStatusStr ="处理中";
			}
			if (groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED) {
				withdrawCashStatusStr ="已完成";
			}
			if (groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_REFUSED) {
				withdrawCashStatusStr ="已拒绝";
			}
			if (groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE) {
				withdrawCashStatusStr ="已冻结";
			}
			Map<String,Object> withdrawal = new HashMap<String,Object>();
			withdrawal.put("withdrawalNumber", groundWithdrawCashRecord.getApplyNo());	
			withdrawal.put("status", withdrawCashStatusStr);
			DecimalFormat df =new DecimalFormat("#0.00");
			withdrawal.put("withdrawalMoney", df.format(groundWithdrawCashRecord.getWithdrawCashAmount()));
//			根据页面显示，组装数据
			if (groundWithdrawCashRecord.getWithdrawCashStatus()==GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FINISHED) {
				withdrawal.put("dealType", "银行汇款");
				withdrawal.put("dealNumber", groundWithdrawCashRecord.getTransactionNo());
				withdrawal.put("finishTime", DateUtil.parseLongTime2Str(groundWithdrawCashRecord.getFinishTime()));
				withdrawal.put("remark", groundWithdrawCashRecord.getRemark());
			}
			withdrawal.put("withdrawalTime", DateUtil.parseLongTime2Str(groundWithdrawCashRecord.getApplyTime()));
			
			return withdrawal;
	}
	/**
	 * 获取最低提现额
	 * @return
	 */
	public double minWithdrawal() {
	 	Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>();
		wrapper.eq("rule_type", 4).eq("status", 0);
		List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
		String minWithdrawal1="";
		if (groundConditionRuleList.size()<1) {
			throw new RuntimeException("获取最低提现额失败！");
		}
		String minWithdrawal = groundConditionRuleList.get(0).getContent();
		DecimalFormat df =new DecimalFormat("#0.00");
		minWithdrawal1 = df.format(Double.valueOf(minWithdrawal));
		return Double.valueOf(minWithdrawal1);
	}
	
	/**
	 * 申请提现
	 * @param withdrawalMoney 提现额
	 * @param withdrawalMoney2 
	 * @return availableBalance 余额
	 */
	 @Transactional(rollbackFor = Exception.class)
	public double applicationWithdrawal(GroundUser groundUser, double withdrawalMoney) {
			if(withdrawalMoney<minWithdrawal()){
				throw new RuntimeException("提现金额不足，最低申请提现金额为"+minWithdrawal()+"元！请重新输入再提交");
			}
			//获取可提现余额
			double availableBalance = groundUser.getAvailableBalance();
			//比较申请提现金额是否大于可提现余额			
			if (availableBalance < withdrawalMoney) {
				throw new RuntimeException("申请提现金额大于可提现余额！请重新输入再提交");
			}
			//组装提现申请记录
			GroundWithdrawCashRecord groundWithdrawCashRecord=new GroundWithdrawCashRecord();
			//设置地推用户id
			groundWithdrawCashRecord.setGroundUserId(groundUser.getId());
			//设置地推用户姓名
			groundWithdrawCashRecord.setGroundUserName(groundUser.getName());
			//设置地推用户手机号
			groundWithdrawCashRecord.setGroundUserPhone(groundUser.getPhone());
			//设置申请单号
			groundWithdrawCashRecord.setApplyNo(DateUtil.getRandomNumber());
			//设置申请提现额
			groundWithdrawCashRecord.setWithdrawCashAmount(withdrawalMoney);
			//设置提现申请状态：0处理中，1已完成，2已拒绝，3已冻结
			groundWithdrawCashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING);
			//设置申请时间
			groundWithdrawCashRecord.setApplyTime( Calendar.getInstance().getTimeInMillis());
			//设置交易方式：0银行汇款
			groundWithdrawCashRecord.setTransactionWay(0);
			groundWithdrawCashRecordMapper.insert(groundWithdrawCashRecord);
			
			groundUserMapper.setAvailableBalance(groundUser.getId(), withdrawalMoney);
			GroundUser ground = groundUserMapper.selectById(groundUser.getId());
			return ground.getAvailableBalance();
	}
	/**
	 * 获取昨天今天 
	 * @param applyTime
	 * @return
	 */
	private String getJZtian(long applyTime) {
		long todayStart = DateUtil.getTodayStart();
		long todayEnd = DateUtil.getTodayEnd();
		long yesTodayStart = DateUtil.getyesTodayStart();
		if (applyTime>=todayStart&&applyTime<=todayEnd) {
			return "今天";
		}
		if (applyTime>=yesTodayStart&&applyTime<todayStart) {
			return "昨天";
		}
		return null;
	}
    
    
}
