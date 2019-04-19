package com.jiuy.operator.common.system.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuy.operator.common.system.persistence.model.Relation;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface RelationMapper extends BaseMapper<Relation> {

}