package com.jiuyuan.dao.mapper.store;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;
import com.yujj.entity.product.YjjMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 会员表
 * Create by hyf on 2018/8/13
 */
@DBMaster
public interface YjjMemberMapper extends BaseMapper<YjjMember>{


    /**
     * 更新会员账号过期时间
     *
     * @param id 会员表主键
     * @param memberPackageType 会员类型
     * @param endTime 更新的时间
     * @param totalMoney 需要累加的购买金额
     * @param validTimeQueue 更新后的时间
     * @param historyValidTimeQueue 历史时间
     * @return int
     * @author Charlie
     * @date 2018/8/16 9:30
     */
    int updateEndTime(@Param( "id" ) Long id,
                      @Param( "memberPackageType" ) Integer memberPackageType,
                      @Param( "endTime" ) Long endTime,
                      @Param( "totalMoney" ) Double totalMoney,
                      @Param( "validTimeQueue" ) String validTimeQueue,
                      @Param( "historyValidTimeQueue" ) String historyValidTimeQueue
    );
}
