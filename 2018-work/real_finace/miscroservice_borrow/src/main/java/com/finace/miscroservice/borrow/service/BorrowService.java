package com.finace.miscroservice.borrow.service;

import com.finace.miscroservice.borrow.entity.response.DataCollectionResponse;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.commons.utils.Response;

import java.util.List;
import java.util.Map;

/**
 * 标的service
 */
public interface BorrowService {

    /**
     * 根据id获取标的信息
     * @param id
     * @return
     */
    public BorrowPO getBorrowById(int id);


    /**
     * 获取理财列表
     * @param map
     * @return
     */
    public List<BorrowPO> getShowFinaceList(Map<String, Object> map, int page);

    /**
     *
     * @param id
     * @return
     */
    public  BorrowPO getShowFinaceById(int id);

    /**
     *
     * @param
     * @return
     */
    public Map<String, Map<String, Object>> getBorrowByIndex(Integer isxs);

	/**
	 * 获取正在售卖的标的
	 * @param borrowGroup
	 * @return
	 */
	public List<BorrowPO> getBorrowPOBySellOut(String borrowGroup);
	
	/**
	 * 在售标的卖完后查找可以自动审核的标的
	 * @param borrowGroup
	 * @return
	 */
	public BorrowPO getBorrowPOByAutoAdd(String borrowGroup);

	/**
	 * 根据订单号自动上标
	 * @param orderId
	 * @return
	 */
	public String autoUpBorrow(String orderId);

	/**
	 * 合同
	 * @param fbid
	 * @return
	 */
    Response showHt(int fbid);

	/**
	 * 投资购买界面展示合同
	 * @param borrowId
	 * @param buyAmt
	 * @return
	 */
	Response showTzHt(int borrowId, String buyAmt, String userId);

	/**
	 *
	 * @param page
	 * @param isxs
	 * @return
	 */
	Response financeList(Integer page, Integer isxs);

	/**
	 * 标的详情
	 * @param id
	 * @return
	 */
	Response getBorrow(Integer id);

	/**
	 * h5获取标的详情
	 * @param id
	 * @return
	 */
	Response getBorrowDetail(int id);

	/**
	 * 获取标的详情
	 * @param id
	 * @return
	 */
	public Response getBorrowDetail(Integer id);

	/**
	 *
	 * @return
	 */
    Response getDataCollection();
	/**
	 * 数据明细
	 * @return
	 */
	DataCollectionResponse dataCollectionChange();
	/**
	 * 根据用户id和时间查询该时间段内投资金额
	 * @param userId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
    Double getUserAmtMoneyInTime(Integer userId, String starttime, String endtime);
}


















