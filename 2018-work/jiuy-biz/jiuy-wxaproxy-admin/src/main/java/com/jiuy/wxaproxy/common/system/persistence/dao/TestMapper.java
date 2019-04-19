package com.jiuy.wxaproxy.common.system.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuy.wxaproxy.common.system.persistence.model.Test;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface TestMapper extends BaseMapper<Test> {

}