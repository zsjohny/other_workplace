package com.yujj.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.product.Tag;
import com.yujj.dao.mapper.TagMapper;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;


    
    public List<Tag> getPropertyNamesListByIds(Collection<Long> ids) {
    	
    	return tagMapper.getTagListByIds(ids);
    	
    	
    }
}
