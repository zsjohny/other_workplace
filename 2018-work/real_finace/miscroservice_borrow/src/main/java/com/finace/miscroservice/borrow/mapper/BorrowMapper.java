package com.finace.miscroservice.borrow.mapper;


import com.finace.miscroservice.borrow.entity.response.DataCollectionResponse;
import com.finace.miscroservice.borrow.entity.response.UserProportion;
import com.finace.miscroservice.borrow.po.BorrowPO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BorrowMapper {

    /**
     * 根据用户id获取标的信息
     * @param userId
     * @return
     */
    BorrowPO getBorrowByUserId(@Param("userId") int userId);

    /**
     *根据id获取标的信息
     * @param id
     * @return
     */
    BorrowPO getBorrowById(@Param("id") int id);

    /**
     * 根据状态和id获取标的信息
     * @param map
     * @return
     */
    BorrowPO getBorrowByStatusId(Map<String, String> map);

    /**
     * 根据id获取标的和借款人信息
     * @param id
     * @return
     */
    BorrowPO getBorrowUserById(@Param("id") int id);

    /**
     * 存管审核通过标的 自动上标查找
     * @param borrowGroup
     * @return
     */
    BorrowPO getBorrowByAutoAdd(String borrowGroup);

    /**
     * 修改标的状态
     * @param map
     */
    void updateBorrowStatusById(Map<String, String> map);

    /**
     *
     * @param map
     * @return
     */
    List<BorrowPO> getShowFinaceList(Map<String, Object> map);

    /**
     *
     * @param id
     * @return
     */
    BorrowPO getShowFinaceById(@Param("id") int id);

    /**
     *
     * @param borrowPO
     */
    void updateBorrow(BorrowPO borrowPO);


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
    List<BorrowPO> getBorrowByIndex(@Param("type") String type,@Param("tab") String tab);

	/**
	 * 获取正在售卖的标的
	 * @param borrowGroup
	 * @return
	 */
	public List<BorrowPO> getBorrowPOBySellOut(@Param("borrowGroup") String borrowGroup);
	
	/**
	 * 在售标的卖完后查找可以自动审核的标的
	 * @param borrowGroup
	 * @return
	 */
	public BorrowPO getBorrowPOByAutoAdd(@Param("borrowGroup") String borrowGroup);
	
	/**
	 * 自动审核标的
	 * @param
	 */
	public int updateAutoCheckBorrow(BorrowPO borrowPO);

    DataCollectionResponse getDatas();

    Integer getLjcjbs();

    Integer getLjcjrzs();

    Double getBndcjje(@Param("date")String date);

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
    Double getUserAmtMoneyInTime(@Param("userId")Integer userId, @Param("starttime")String starttime, @Param("endtime")String endtime);

    /**
     * 首页九十天标的
     * @return
     */
    List<BorrowPO> getBorrowNinetyByIndex();
    /**
     * 协议支付 查询 分组待审标的
     * @param borrowGroup
     * @return
     */
    BorrowPO getBorrowAgreeByAutoAdd(@Param("borrowGroup")String borrowGroup);
    /**
     * 根据标签查找标的
     * @param tab
     * @return
     */
    List<BorrowPO> getBorrowByTab(@Param("tab")String tab);
}
