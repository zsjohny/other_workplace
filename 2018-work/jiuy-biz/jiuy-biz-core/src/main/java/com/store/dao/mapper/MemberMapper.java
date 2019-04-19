package com.store.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.store.entity.member.ShopMember;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 小程序会员表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-22
 */
public interface MemberMapper extends BaseMapper<ShopMember> {

	int increaseNotReadMessageCount(long memberId);


    /**
     * 查询我的信息
     * @param memberId
     * @return
     */
    ShopMember findMyInformationById(@Param("memberId") long memberId);

    /**
     * 查询会员信息
     * @param memberId
     * @return
     */
    ShopMember findMemberById(Long memberId);
}