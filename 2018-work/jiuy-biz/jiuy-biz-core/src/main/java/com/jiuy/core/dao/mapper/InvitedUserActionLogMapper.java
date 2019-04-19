package com.jiuy.core.dao.mapper;

import com.jiuyuan.entity.InvitedUserActionLog;

/**
 * @author jeff.zhan
 * @version 2016年12月21日 下午4:08:11
 * 
 */

public interface InvitedUserActionLogMapper {

	InvitedUserActionLog getByUserId(long userId, int action);

}
