package com.jiuy.rb.service.coupon;

import com.jiuy.base.model.MyPage;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.util.CouponAcceptVo;
import com.jiuy.rb.util.CouponSendList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 新的优惠券接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/2 13:50
 * @Copyright 玖远网络
 */
public interface ICouponServerNew {

    /**
     * 添加优惠券模板
     *
     * @param couponTemplateNew couponTemplateNew
     * @author Aison
     * @date 2018/8/2 13:53
     *
     */
    void addCouponTemplate(CouponTemplateNew couponTemplateNew);


    /**
     * 获取优惠券的地方
     *
     * @param accept 优惠券获取类型
     * @author Aison
     * @date 2018/8/2 15:01
     */
    void grant(CouponAcceptVo accept) ;

    /**
     * app 订单回调发放优惠券
     *
     * @param orderNo 订单号
     * @author Aison
     * @date 2018/8/3 18:20
     */
    void grantOrder(String orderNo);

    /**
     * wxa 订单回调发放优惠券
     *
     * @param orderNo 订单号
     * @author hyq
     * @date 2018/8/24 18:20
     */
    void grantWxaOrder(String orderNo);


    /**
     * 品牌列表 判断是否有优惠券
     *
     * @param param param
     * @author Aison
     * @date 2018/8/6 9:29
     */
    void hasCoupon(Map<String,Object> param);

    /**
     * 查询可以领取的优惠券 app
     *
     * @param supplierId supplierId
     * @author Aison
     * @date 2018/8/6 10:30
     * @return java.util.List<com.jiuy.rb.model.coupon.CouponTemplateNew>
     */
    List<Map<String,Object>>  appCouponList(Long supplierId);


    /**
     * 回滚优惠券,将其设为可用
     *
     * @param couponId 优惠券id
     * @param orderId 订单id
     * @param orderNo 订单编号
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/3 17:07
     */
    Boolean rollbackCoupon2Available(Long couponId,Long orderId,String orderNo);

    /**
     * 只针对回滚优惠券,将其设为可用
     *
     * @param couponId 优惠券id
     * @param orderId 订单id
     * @param orderNo 订单编号
     * @return 是否成功
     * @author hyq
     * @date 2018/8/27 17:07
     */
    Boolean rollbackCoupon(String orderNo);


    /**
     * 查询我的优惠券
     *
     * @param query 查询对象
     * @author Aison
     * @date 2018/8/6 15:15
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    Map<String,Object> myCouponList(CouponRbNewQuery query);

    /**
     *描述 新运营平台模板查询
     ** @param query 优惠券模板参数
     * @author hyq
     * @date 2018/8/17 15:37
     * @return com.jiuy.base.model.MyPage<java.util.Map<java.lang.String,java.lang.Object>>
     */
    MyPage<Map<String,Object>> selectCouponTemplateList(CouponTemplateNewQuery query);

    /**
     *描述 优惠券首页
     ** @param couponRbNewQuery
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    MyPage<CouponSendList> selectSendCouponInfo(CouponRbNewQuery query);

    /**
     *描述 获取优惠券发送记录
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    Map<String,Object> selectSendCouponInfoCollect(Long id);


    /**
     * 查询我的优惠券 小程序
     *
     * @param query 查询对象
     * @author Aison
     * @date 2018/8/6 15:15
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    Map<String,Object> myCouponListWxa(CouponRbNewQuery query);

    int listCountNumber(CouponRbNewQuery query);

    /**
     * 修改优惠券
     *
     * @param couponId couponId
     * @author Aison
     * @date 2018/8/6 16:27
     * @return int
     */
    int delCoupon(Long couponId);


    /**
     *  新的优惠券填充
     *
     * @param data data
     * @param storeId 门店id
     * @param memberId 小程序id
     * @param sysEnum 系统枚举
     * @author Aison
     * @date 2018/8/6 17:15
     */
    void fillCoupon(Map<String, Object> data,Long storeId,Long memberId,CouponSysEnum sysEnum,Boolean isManyPro,Integer current,Integer size);


    /**
     * 核销列表
     *
     * @param query
     * @author Aison
     * @date 2018/8/7 17:05
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
     Map<String, Object> appHxList(CouponRbNewQuery query);


    /**
     * 修改优惠券
     *
     * @param couponRbNew couponRbNew
     * @author Aison
     * @date 2018/8/7 17:41
     * @return int
     */
     int updateCoupon(CouponRbNew couponRbNew);

    /**
     * 核销优惠券
     *
     * @param id id
     * @param storeId storeId
     * @author Aison
     * @date 2018/8/7 17:46
     * @return java.util.Map<com.sun.org.apache.xpath.internal.operations.String,com.sun.org.apache.xpath.internal.operations.String>
     */
    Map<String,String> hx(Long id,Long storeId);

    /**
     * 获取模板分页
     *
     * @param query query
     * @author Aison
     * @date 2018/8/7 18:17
     * @return java.util.Map<java.lang.String,org.omg.CORBA.Object>
     */
    Map<String,Object> tempPageApp(CouponTemplateNewQuery query);

    /**
     * 停止发放优惠券
     *
     * @param tempId tempId
     * @param publishId 发布方id
     * @param status 状态
     * @author Aison
     * @date 2018/8/8 14:26
     * @return int
     */
    int stopTempSend(Long tempId,Long publishId,Integer status);


    /**
     *
     *  等待领取的优惠券
     *
     * @param param param
     * @author Aison
     * @date 2018/8/8 15:09
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    List<CouponTemplateNew> waitGetCoupon(Map<String,Object> param);

    /**
     * 获取可用优惠券
     * @param query query
     * @author Aison
     * @date 2018/8/8 16:40
     * @return int
     */
    int useAbleCouponCount(CouponRbNewQuery query);


    /**
     * 供应商后台的优惠券模板分页
     *
     * @param query query
     * @author Aison
     * @date 2018/8/9 13:48
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String,Object> tempPageSupplier(CouponTemplateNewQuery query) ;


    /**
     * 通过id获取temp
     *
     * @param query query
     * @author Aison
     * @date 2018/8/9 16:34
     * @return com.jiuy.rb.model.coupon.CouponTemplateNew
     */
    CouponTemplateNew getOneTemp(CouponTemplateNewQuery query);

    /**
     * 修改模板
     *
     * @param templateNew templateNew
     * @author Aison
     * @date 2018/8/9 17:34
     * @return int
     */
    int updateTemp(CouponTemplateNew templateNew);


    /**
     * 统计每个模板的使用情况
     *
     * @param param param
     * @author Aison
     * @date 2018/8/9 17:57
     * @return java.math.BigDecimal
     */
    BigDecimal sumTempPrice(Map<String,Object> param);


    /**
     * 获取单个优惠券
     *
     * @param query query
     * @author Aison
     * @date 2018/8/10 14:43
     * @return com.jiuy.rb.model.coupon.CouponRbNew
     */
    CouponRbNew getOneCoupon(CouponRbNewQuery query);

}
