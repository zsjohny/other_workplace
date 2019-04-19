package com.jiuy.rb.mapper.coupon; 
 
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.util.CouponSendList;
import com.jiuy.rb.util.CouponUser;
import com.jiuy.rb.util.CouponWho;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 * 优惠券模板表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年08月09日 下午 05:16:36
 * @Copyright 玖远网络 
 */
public interface CouponTemplateNewMapper extends BaseMapper<CouponTemplateNew>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 查询哪些用户可以获取优惠券
     * 适用于门店宝
     *
     * @param who who
     * @author Aison
     * @date 2018/8/2 17:10
     * @return java.util.List<com.jiuy.rb.model.user.StoreBusinessRb>
     */
    List<CouponUser> selectTargetUser(CouponWho who);


    /**
     * 查询哪些用户可以获取优惠券
     * 适用于小程序
     *
     * @param who who
     * @author Aison
     * @date 2018/8/2 17:10
     * @return java.util.List<com.jiuy.rb.model.user.StoreBusinessRb>
     */
    List<CouponUser> selectTargetUserWxa(CouponWho who);

    /**
     * 添加被领取的数量
     *
     * @param id id
     * @param count count
     * @author Aison
     * @date 2018/8/2 17:27
     * @return int
     */
    int addReceiveCount(@Param("id") Long id, @Param("count") Long count);

    /**
     *描述 获取优惠券发送记录
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    Map<String,Object> selectSendCouponInfoCollect(@Param("id") Long id);


    /**
     *  每个供应商能够领取的优惠券数量
     *
     * @param publishIds publishs
     * @author Aison
     * @date 2018/8/6 9:52
     * @return java.util.Map<java.lang.Long,java.util.Map<java.lang.Long,java.lang.Integer>>
     */
    @MapKey("publish")
    Map<Long,Map<String,Long>> hasCoupon(@Param("publishIds") Set<Long> publishIds);

    /**
     * 查询未被领用的优惠券模板
     *
     * @param param param
     * @author Aison
     * @date 2018/8/8 14:48
     * @return java.util.List<com.jiuy.rb.model.coupon.CouponTemplateNew>
     */
    List<CouponTemplateNew> selectNotReceivedTemplate(Map<String,Object> param);

    /**
     * 获取优惠券的使用统计
     *
     * @param param param
     * @author Aison
     * @date 2018/8/9 17:52
     * @return java.math.BigDecimal
     */
    BigDecimal sumPrice(Map<String,Object> param);

    /**
     *描述 新运营平台模板查询
     ** @param query 优惠券模板参数
     * @author hyq
     * @date 2018/8/17 15:37
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> selectCouponTemplateList(CouponTemplateNewQuery query);

    /**
     *描述 优惠券首页
     ** @param couponRbNewQuery
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    List<CouponSendList> selectSendCouponInfo(CouponRbNewQuery query);


}
