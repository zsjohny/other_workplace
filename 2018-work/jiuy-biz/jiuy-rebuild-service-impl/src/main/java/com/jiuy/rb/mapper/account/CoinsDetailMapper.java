package com.jiuy.rb.mapper.account; 
 
import com.jiuy.base.model.Query;
import com.jiuy.rb.model.account.CoinsDetail;
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 玖币明细表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月27日 上午 09:56:42
 * @Copyright 玖远网络 
 */
public interface CoinsDetailMapper extends BaseMapper<CoinsDetail>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 查询出需要释放的订单购买记录
     *
     * @param releaseTime releaseTime
     * @author Aison
     * @date 2018/7/18 11:31
     * @return java.util.List<com.jiuy.rb.model.account.CoinsDetail>
     */
    List<CoinsDetail> selectReleaseOrderCoins(@Param("releaseTime") Long releaseTime);


    /**
     * 某条资金明细，将资金明细的的状态从 status 修改到 toStatus
     * 通过id修改
     *
     * @param id id
     * @param status 原来的status
     * @param toStatus 目标status
     * @author Aison
     * @date 2018/7/18 13:50
     * @return int
     */
    int updateCoinsDetail(@Param("id") Long id , @Param("status") Integer status, @Param("toStatus") Integer toStatus);

    /**
     * 查询某些玖币记录的总和
     *
     * @param query query
     * @author Aison
     * @date 2018/7/18 18:42
     * @return java.lang.Long
     */
    Long sumCount(Query query);

}
