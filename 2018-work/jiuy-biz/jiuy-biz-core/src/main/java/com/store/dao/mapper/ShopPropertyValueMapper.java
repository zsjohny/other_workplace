package com.store.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.ShopPropertyValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @author QiuYuefan
 *
 */
@DBMaster
public interface ShopPropertyValueMapper extends BaseMapper<ShopPropertyValue>{
}