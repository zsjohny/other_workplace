package com.jiuy.wxaproxy.common.system.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuy.wxaproxy.common.system.persistence.model.LoginLog;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 登录记录 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}