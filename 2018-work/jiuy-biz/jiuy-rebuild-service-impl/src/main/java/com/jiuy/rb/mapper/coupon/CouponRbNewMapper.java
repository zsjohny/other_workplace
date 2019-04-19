package com.jiuy.rb.mapper.coupon; 
 
import com.jiuy.base.model.Query;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 
 * 优惠券表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年08月06日 下午 04:50:35
 * @Copyright 玖远网络 
 */
public interface CouponRbNewMapper extends BaseMapper<CouponRbNew>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 删除优惠券
     *
     * @param id id
     * @author Aison
     * @date 2018/8/6 16:32
     * @return int
     */
    int deleteCoupon(Long id);

    /**
     * 查询订单能使用的优惠券
     *
     * @param orderPrice orderPrice
     * @param storeId storeId
     * @param memberId memberId
     * @param sysType 系统类型
     * @author Aison
     * @date 2018/8/6 18:06
     * @return java.util.List<com.jiuy.rb.model.coupon.CouponRbNewQuery>
     */
    List<CouponRbNewQuery> selectOrderCoupon(@Param("orderPrice") BigDecimal orderPrice,@Param("storeId") Long storeId, @Param("memberId") Long memberId,
                                             @Param("sysType") Integer sysType,@Param("pageCurrentbak") Integer pageCurrent,@Param("pageSizebak")Integer pageSize,
                                             @Param("stringTime")String stringTime);

    /**
     * 查询核销信息
     *
     * @param query query
     * @author Aison
     * @date 2018/8/7 17:03
     * @return java.util.Map<java.lang.String,java.lang.Long>
     */
    Map<String,Object> selectHxInfo(Query query);


    /**
     * 查询优惠券使用情况
     *
     * @param param 参数
     * @author Aison
     * @date 2018/8/8 10:57
     * @return java.util.Map<java.lang.Long,java.lang.Long>
     */
    @MapKey("tempStatus")
    Map<String,Map<String,Long>> selectCouponGroup(Map<String,Object> param);


}
