package com.jiuyuan.service.common;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.GroundWithdrawCashRecord;

/**
 * <p>
 * 地推人员提现申请表 服务类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */
public interface IGroundWithdrawCashRecordService  {
	 void execute();
	 List<Map<String,Object>> getMyWithdrawalList(long applyNo,Integer type, long grandUserId,Page<GroundWithdrawCashRecord> page) throws ParseException ;
	 int getUnWithdrawalCount(long grandUserId);
	 Map<String, Object> getMyWithdrawalInfo(long groundWithdrawCashRecordId);
	 double applicationWithdrawal(GroundUser groundUser, double withdrawalMoney);
	 double minWithdrawal();

}
