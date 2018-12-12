package com.finace.miscroservice.borrow.dao;

import com.finace.miscroservice.borrow.entity.response.DataCollectionResponse;
import com.finace.miscroservice.borrow.entity.response.UserProportion;
import com.finace.miscroservice.borrow.po.BorrowPO;

import java.util.List;
import java.util.Map;

/**
 * 标的Dao层
 */
public interface BorrowDao {


    /**
     * 根据用户id获取标的信息
     * @param userId
     * @return
     */
    public BorrowPO getBorrowByUserId(int userId);

    /**
     * 根据id获取标的信息
     * @param id
     * @return
     */
    public BorrowPO getBorrowById(int id);

    /**
     * 根据状态和id获取标的信息
     * @param borrowId
     * @param status
     * @return
     */
    BorrowPO getBorrowByStatusId(String borrowId, String status);

    /**
     * 根据id获取标的和借款人信息
     * @param id
     * @return
     */
    public BorrowPO getBorrowUserById(int id);

    /**
     * 修改标的状态
     * @param map
     */
    public void updateBorrowStatusById(Map<String, String> map);

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
     * @param borrowPO
     */
    public void updateBorrow(BorrowPO borrowPO);

    /**
     * 修改标的信息
     * @param borrowPO
     */
    void updateAllBorrow(BorrowPO borrowPO);

    /**
     *
     * @param type
     * @return
     */
    public List<BorrowPO> getBorrowByIndex(String type,String tab);

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
     * 存管审核通过标的 自动上标查找
     * @param borrowGroup
     * @return
     */
    BorrowPO getBorrowByAutoAdd(String borrowGroup);
	
	/**
	 * 自动审核标的
	 * @param
	 */
	public int updateAutoCheckBorrow(BorrowPO borrowPO);

    DataCollectionResponse getDatas();

    Integer getLjcjbs();

    Integer getLjcjrzs();

    Double getBndcjje(String s);

    Double dhje();

    Double getZddhcjyezb();

    Double getZdshtzcjzb();

    UserProportion userProportion();

    Integer dhjebs();

    Integer ljjkrzs();

    Integer dqjkrsl();

    Integer dqcjrsl();
    /**
     * 根据用户id和时间查询该时间段内投资金额
     * @param userId
     * @param starttime
     * @param endtime
     * @return
     */
    Double getUserAmtMoneyInTime(Integer userId, String starttime, String endtime);

    /**
     * 90天标
     * @return
     */
    List<BorrowPO> getBorrowNinetyByIndex();

    /**
     * 协议支付 查询 分组待审标的
     * @param borrowGroup
     * @return
     */
    BorrowPO getBorrowAgreeByAutoAdd(String borrowGroup);

    /**
     * 根据标签查找标的
     * @param tab
     * @return
     */
    List<BorrowPO> getBorrowByTab(String tab);
}
