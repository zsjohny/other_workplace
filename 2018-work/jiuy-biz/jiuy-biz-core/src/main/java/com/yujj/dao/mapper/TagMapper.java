package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.Tag;

@DBMaster
public interface TagMapper {
    
    List<Tag> getTagListByIds(@Param("ids") Collection<Long> ids);
}
