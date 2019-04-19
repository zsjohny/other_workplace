package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.member.MemberOperatorRequest;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;
import com.yujj.entity.product.YjjMember;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/14 15:21
 * @Copyright 玖远网络
 */
public interface IYjjMemberService{



    /**
     * 根据用户id查询 会员信息
     * <p>
     *     如果会员已过期,已删除,返回null
     * </p>
     */
    YjjMember findValidMemberByUserId(SystemPlatform systemPlatform, Long userId) ;

    /**
     * 根据用户id查询 会员信息
     *
     * @param systemPlatform systemPlatform
     * @param userId userId
     * @return com.yujj.entity.product.YjjMember
     * @author Charlie
     * @date 2018/8/16 10:45
     */
    YjjMember findMemberByUserId(SystemPlatform systemPlatform, Long userId);



    /**
     * 用户成功购买会员套餐
     *
     * @param code 系统平台
     * @param userId 用户id
     * @param totalMoney 支付总额
     * @param memberPackageType 套餐类型
     * @param orderNo 订单编号
     * @author Charlie
     * @date 2018/8/16 7:37
     */
    void buyMemberPackageOK(Integer code, Long userId, Double totalMoney, Integer memberPackageType, String orderNo);

    /**
     * 添加会员
     * @return
     * @author hyf
     * @date 2018/9/3 19:42
     */
    JsonResponse addMember(MemberOperatorRequest memberOperatorRequest);

    /**
     * 查找会员
     * @param membersFindRequest membersFindRequest
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/9/4 9:14
     */
    Page<MemberOperatorResponse> findAllMembers(MembersFindRequest membersFindRequest);
}
