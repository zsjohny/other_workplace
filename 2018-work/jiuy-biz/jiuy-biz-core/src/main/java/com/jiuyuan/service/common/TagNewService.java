/**
 * 
 */
package com.jiuyuan.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.supplier.TagNewMapper;
import com.jiuyuan.entity.newentity.TagNew;

/**
 */

@Service
public class TagNewService implements ITagNewService  {
	private static final Logger logger = LoggerFactory.getLogger(TagNewService.class);
	@Autowired
	private TagNewMapper tagNewMapper;
	@Override
	public TagNew selectById(long tagId) {
		return tagNewMapper.selectById(tagId);
	}
	
}