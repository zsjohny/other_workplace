package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.Tag;


@DBMaster
public interface TagMapper {
    
    List<Tag> getTagListByIds(@Param("ids") Collection<Long> ids);

	List<Tag> getTopTagList();

	List<Map<String,Object>> getTagsByPriority(@Param("tagNums") int tagNums);
}
