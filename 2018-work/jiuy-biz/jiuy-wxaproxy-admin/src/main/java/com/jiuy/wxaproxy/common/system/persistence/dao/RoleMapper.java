package com.jiuy.wxaproxy.common.system.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuy.wxaproxy.common.system.persistence.model.Role;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface RoleMapper extends BaseMapper<Role> {

}