package com.goldplusgold.td.sltp.core.mapper;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户止盈止损的记录映射表
 * Created by Ness on 2017/5/10.
 */
@Mapper
public interface UserSltpMapper {
    /**
     * 根据止盈止损的id查询具体止盈止损的详细信息
     *
     * @param uuid 用户止盈止损的id
     * @return
     */
    UserSltpRecord findSltpOneByUuid(String uuid);

    /**
     * 保存一条止盈止损记录
     *
     * @param userSltpRecord
     */
    boolean saveUserSltpOne(UserSltpRecord userSltpRecord);

    /**
     * 根据具体的条件更新止盈止损记录信息
     *
     * @param userSltpRecord
     */
    boolean updateUserSltpOneByUuid(UserSltpRecord userSltpRecord);

    /**
     * 根据用户Id查找所有用户的数据
     *
     * @param userId           用户的id
     * @param commissionResult 委托结果
     * @param start            开始的页数
     * @param pageCount        每页的数量
     * @return
     */
    List<UserSltpRecord> findSltpAllInfoByUserId(@Param("userId") String userId, @Param("commissionResult") Integer commissionResult, @Param("start") Integer start, @Param("pageCount") Integer pageCount);
}
