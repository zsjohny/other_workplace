package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.dao.mapper.CommentMapper;
import com.yujj.entity.comment.Comment;

/**
 * @author jeff.zhan
 * @version 2016年12月17日 上午10:31:16
 * 
 */

@Service
public class CommentService {
	
	@Autowired
	private CommentMapper commentMapper;

	public Comment getById(long id) {
		return commentMapper.getById(id);
	}
}
