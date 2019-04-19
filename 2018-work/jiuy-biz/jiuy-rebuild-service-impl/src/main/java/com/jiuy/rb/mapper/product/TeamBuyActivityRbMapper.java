package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.TeamBuyActivityRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.product.TeamBuyActivityRbQuery;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * 门店团购活动 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月11日 上午 11:48:50
 * @Copyright 玖远网络 
 */
public interface TeamBuyActivityRbMapper extends BaseMapper<TeamBuyActivityRb>{

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
     *    需要: gtActivityEndTime 系统当前时间
     * @return java.util.List<com.jiuyuan.entity.newentity.TeamBuyActivity>
     * @author Charlie
     * @date 2018/7/30 9:17
     */
    List<TeamBuyActivityRb> listTeamBuyActivity(TeamBuyActivityRbQuery query);

    /**
     * 更新活动参与人数或者下单件数数量
     *
     * @param id teamId
     * @param conditionType conditionType
     * @param productCount 下单件数
     * @param userCount 用户人数
     * @return int
     * @author Charlie
     * @date 2018/8/3 18:52
     */
    int updateJoinUserOrProduct(@Param ("id") Long id,
                                @Param ("conditionType") Integer conditionType,
                                @Param ("count") Integer productCount,
                                @Param ("userCount") Integer userCount);

    /**
     * 查询当前进行中的活动
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
    List<TeamBuyActivityRb> selectAvailableTeamOrderByEndTime(TeamBuyActivityRbQuery query);

    /**
     * 查询当前进行中的活动
     *      <p>按照结束时间降排</p>
     * @param teamQuery
     * @return
     */
    List<TeamBuyActivityRb> findAvailableTeamOrderByEndTime(TeamBuyActivityRbQuery teamQuery);
}