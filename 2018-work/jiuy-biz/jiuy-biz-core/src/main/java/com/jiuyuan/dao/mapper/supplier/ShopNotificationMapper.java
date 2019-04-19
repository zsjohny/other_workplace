package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ShopNotification;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2017-12-22
 */
@DBMaster
public interface ShopNotificationMapper extends BaseMapper<ShopNotification> {

	void addNotification(@Param("notification")ShopNotification notification);

}