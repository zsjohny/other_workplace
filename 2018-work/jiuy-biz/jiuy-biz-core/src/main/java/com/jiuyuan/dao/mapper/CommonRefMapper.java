package com.jiuyuan.dao.mapper;

import com.jiuyuan.common.CouponRbRef;
import com.jiuyuan.dao.annotation.DBMaster;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 通用 mapper
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/7 9:57
 * @Copyright 玖远网络
 */

@DBMaster
public interface CommonRefMapper {


    /**
     * 查询优惠券
     *
     * @param id id
     * @param sysType 系统 1app 2小程序
     * @author Aison
     * @date 2018/8/7 10:20
     * @return com.jiuyuan.common.CouponRbRef
     */
    CouponRbRef selectCoupon(@Param("id") Long id, @Param("sysType") Integer sysType);

    /**
     * 更新优惠券到已使用状态
     *
     * @param id id
     * @param orderNo 订单号码
     * @param sourceStatus 原来的状态
     * @param targetStatus 新的状态
     * @param discountMoney 优惠金额
     * @author hyq
     * @date 2018/8/20 10:48
     * @return int
     */
    int updateCouponStatusMoney(@Param("id") Long id,@Param("orderNo") Long orderNo,@Param("sourceStatus") Integer sourceStatus,@Param("targetStatus") Integer targetStatus,@Param("orderMoney") BigDecimal orderMoney ,@Param("discountMoney") BigDecimal discountMoney );

    /**
     * 更新优惠券到已使用状态
     *
     * @param id id
     * @param orderNo 订单号码
     * @param sourceStatus 原来的状态
     * @param targetStatus 新的状态
     * @author Aison
     * @date 2018/8/7 10:48
     * @return int
     */
    int updateCouponStatus(@Param("id") Long id,@Param("orderNo") Long orderNo,@Param("sourceStatus") Integer sourceStatus,@Param("targetStatus") Integer targetStatus);


    /**
     *
     * 通过参数查询
     *
     * @param param 参数
     * @author Aison
     * @date 2018/8/7 13:48
     * @return com.jiuyuan.common.CouponRbRef
     */
    CouponRbRef selectCouponByParam(Map<String,Object> param);

}
