package com.store.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.product.Tag;
import com.store.dao.mapper.TagMapper;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;
    
    public List<Tag> getPropertyNamesListByIds(Collection<Long> ids) {
    	return tagMapper.getTagListByIds(ids);
    }

	public List<Tag> getTopTagList() {
		return tagMapper.getTopTagList();
	}

	public List<Map<String, Object>> getTagsByPriority(int tagNums) {
		return tagMapper.getTagsByPriority(tagNums);
	}
}