package com.jiuy.supplier.common.system.persistence.dao;

import com.jiuy.supplier.common.system.persistence.model.Notice;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMirror;

/**
 * <p>
 * 通知表 Mapper 接口
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@DBMirror
public interface NoticeMapper extends BaseMapper<Notice> {

}