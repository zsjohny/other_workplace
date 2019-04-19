package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserInvite;
import com.jiuyuan.entity.account.UserInviteRecord;
import com.yujj.entity.account.User;

@DBMaster
public interface UserInviteMapper {

    UserInvite getUserInvite(@Param("userId") long userId);

    UserInvite getUserInviteByCode(@Param("inviteCode") String inviteCode);

    int addUserInvite(UserInvite userInvite);

    int incrUserInviteCount(@Param("userId") long userId);

    int addUserInviteRecord(UserInviteRecord record);

    UserInviteRecord getByInvitedUserId(@Param("userId") long userId);
    
    

}
