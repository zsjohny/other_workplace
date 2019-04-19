package com.jiuyuan.dao.mapper.operator;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.store.entity.BusinessInformation;

/**
 * Create by hyf on 2018/8/20
 */
@DBMaster
public interface OperatorBusinessIMMapper  extends BaseMapper<BusinessInformation> {
}
