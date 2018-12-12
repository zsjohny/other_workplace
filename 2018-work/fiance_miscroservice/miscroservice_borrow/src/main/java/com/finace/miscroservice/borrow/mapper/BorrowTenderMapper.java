package com.finace.miscroservice.borrow.mapper;

import com.finace.miscroservice.commons.entity.BorrowTender;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 汇付投资记录mapper
 */
@Mapper
public interface BorrowTenderMapper {

	/**
	 * 查询已完成收款的投标累计本金和累计收益
	 * 
	 * @param userId
	 * @return
	 */
	BorrowTender getUserTotalPast(@Param("userId") String userId);

	/**
	 * 当日投资总金额
	 * 
	 * @return
	 */
	public double getTenderNowDaySum();

	/**
	 * 当日投资人数 投资人数
	 * 
	 * @return
	 */
	public int getUserNowDayNum();

	/**
	 * 当日投资订单笔数
	 * 
	 * @param
	 * @return
	 */
	public int getTenderNowDay();

	/**
	 * 昨日首投笔数
	 * 
	 * @return
	 */
	public int tenderFirstNum();

	/**
	 * 当日回款笔数
	 * 
	 * @return
	 */
	public int getTenderRepayNowDay();

	/**
	 * 当日回款总金额总金额
	 * 
	 * @return
	 */
	public double getTendeRepayrNowDaySum();

	/**
	 * 昨日新用户投资富友笔数
	 * 
	 * @return
	 */
	public int getTenderofNewuser();

	/**
	 * 总待收
	 * 
	 * @return
	 */
	public double getAllCollectSum();

	/**
	 * 富友总待收
	 * 
	 * @return
	 */
	public double getAllCollectFidSum();

	/**
	 * 昨日投资笔数
	 * 
	 * @return
	 */
	public int getTenderNum();

	/**
	 * 获取用户汇付投资总额
	 * @return
	 */
	public Double getAllTenderByUserId(@Param("userId") Integer userId);

	/**
	 * 查询已完成回款的累计本金和累计收益加红包和回款笔数
	 */
	public BorrowTender getRepayInfoOfToday();

}
