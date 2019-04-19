package com.jiuyuan.dao.mapper.operator;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.Badge;

/**
 * <p>
  * 角标表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-22
 */
@DBMaster
public interface BadgeMapper extends BaseMapper<Badge> {

	int updateProductBadge(@Param("badgeId")long badgeId, @Param("badgeName")String badgeName, @Param("badgeImage")String badgeImage, @Param("list")List<Map<String, Object>> list);

}