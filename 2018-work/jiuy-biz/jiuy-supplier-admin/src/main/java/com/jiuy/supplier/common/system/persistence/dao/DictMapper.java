package com.jiuy.supplier.common.system.persistence.dao;

import com.jiuy.supplier.common.system.persistence.model.Dict;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface DictMapper extends BaseMapper<Dict> {

}