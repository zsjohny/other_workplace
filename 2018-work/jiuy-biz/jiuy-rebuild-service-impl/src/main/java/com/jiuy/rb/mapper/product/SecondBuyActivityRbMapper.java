package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.SecondBuyActivityRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.product.SecondBuyActivityRbQuery;
import com.jiuy.rb.model.product.TeamBuyActivityRb;
import com.jiuy.rb.model.product.TeamBuyActivityRbQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 门店秒杀活动 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月11日 上午 11:48:35
 * @Copyright 玖远网络 
 */
public interface SecondBuyActivityRbMapper extends BaseMapper<SecondBuyActivityRb>{

    // @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 门店用户的团购活动列表
     * <p>
     * 排序优先顺序依次按条件：
     *     活动状态为进行中的，
     *     活动状态为待开始的，
     *     活动状态为已结束的，
     *     距离活动即将结束时间最近的，
     *     距离活动开始时间最近的，
     *     距离活动已结束最近的
     * </p>
     * @param query
     *    需要: storeId 门店id
     *    需要: gtActivityEndTime 当前时间
     * @return java.util.List<com.jiuyuan.entity.newentity.TeamBuyActivity>
     * @author Charlie
     * @date 2018/7/30 9:17
     */
    List<SecondBuyActivityRb> listSecondBuyActivity(SecondBuyActivityRbQuery query);



    /**
     * 更新活动参与者数量
     *
     * @param secondId secondId
     * @param buyCount 购买数量
     * @param userCount 用户人数
     * @return int
     * @author Charlie
     * @date 2018/8/3 18:51
     */
    int updateUserAndProductCount(@Param ("secondId") Long secondId,
                                  @Param ("buyCount") Integer buyCount,
                                  @Param ("userCount") Integer userCount);


    /**
     * 查询当前执行中的活动
     * <p>按照结束时间降排</p>
     *
     * @param query
     *      需要 storeId 门店id
     *      需要 currentTime 当前系统时间
     *      最好加上分页参数
     * @return java.util.List<com.jiuy.rb.model.product.TeamBuyActivityRb>
     * @author Charlie
     * @date 2018/8/7 11:09
     */
    List<SecondBuyActivityRb> selectAvailableSecondOrderByEndTime(SecondBuyActivityRbQuery query);
}