package com.jiuy.rb.mapper.product; 
 
import com.jiuy.rb.model.product.SalesVolumePlainDetailRb; 
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/** 
 * 刷量明细表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月19日 下午 01:22:06
 * @Copyright 玖远网络 
 */
public interface SalesVolumePlainDetailRbMapper extends BaseMapper<SalesVolumePlainDetailRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     *
     * 修改某条删单的剩余数量
     *
     * @param lastCount lastCount
     * @param count count
     * @param id id
     * @author Aison
     * @date 2018/6/19 9:24
     * @return int
     */
    int subLeftCount(@Param("lastCount") Long lastCount, @Param("count") Long count, @Param("id") Long id);

    /**
     * 重新初始化值
     *
     * @param plainId plainId
     * @param avgCount avgCount
     * @param newExceptCount 新的数值
     * @author Aison
     * @date 2018/6/19 10:51
     * @return int
     */
    int reInitDetail(@Param("plainId") Long plainId, @Param("avgCount") Double avgCount,@Param("newExceptCount") Long newExceptCount);


    /**
     * 查询某个策略已经刷了多少 未重复初始化的情况
     *
     * @param plainId plainId
     * @author Aison
     * @date 2018/6/19 12:05
     * @return long
     */
    Long selectPlainAddedCount(Long plainId);

    /**
     * 查询当前总的添加量
     *
     * @param plainId plainId
     * @author Aison
     * @date 2018/6/19 13:29
     * @return long
     */
    Long  selectAllAddedCount(Long plainId);

    /**
     * 重新初始化值
     *
     * @param plainDetailId plainDetailId
     * @param avgCount avgCount
     * @param newExceptCount 新的数值
     * @author Aison
     * @date 2018/6/19 10:51
     * @return int
     */
    int reInitDetailByDetailId(@Param("plainDetailId") Long plainDetailId, @Param("avgCount") Double avgCount,@Param("newExceptCount") Long newExceptCount);

    /**
     * 查询某个销量今天预计刷多少,已经刷了多少
     * <p>
     *     expectCount 今日预计刷量
     *     doneCount 今日已经刷量
     * </p>
     * @param plainId plainId
     * @author Aison
     * @date 2018/6/19 12:05
     * @return long
     */
    Map<String,Object> todayExpectCountAndDoneCount(Long plainId);
}
