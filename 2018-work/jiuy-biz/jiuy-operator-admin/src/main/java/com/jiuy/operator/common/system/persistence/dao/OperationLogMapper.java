package com.jiuy.operator.common.system.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuy.operator.common.system.persistence.model.OperationLog;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 操作日志 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}