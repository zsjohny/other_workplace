package com.jiuy.wxaproxy.common.system.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuy.wxaproxy.common.system.persistence.model.Menu;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface MenuMapper extends BaseMapper<Menu> {

}