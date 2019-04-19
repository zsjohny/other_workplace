package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductUpdownsoldLog;

/**
 * 商品上下架日志
 * @author Administrator
 *
 */
@DBMaster
public interface ProductUpdownsoldLogMapper extends BaseMapper<ProductUpdownsoldLog> {
	
}
