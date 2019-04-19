package com.jiuy.core.service.comment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.CommentDao;
import com.jiuy.core.meta.comment.Comment;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Resource
	private CommentDao commentDao;

	@Override
	public int removeComment(long orderid) {
		return commentDao.removeComment(orderid);
	}

	@Override
	public int updateStatus(Long orderId, int status) {
		return commentDao.updateStatus(orderId, status);
	}

	@Override
	public List<Comment> search(PageQuery pageQuery, Comment comment) {
		return commentDao.search(pageQuery, comment);
	}

	@Override
	public int searchCount(Comment comment) {
		return commentDao.searchCount(comment);
	}
	
}
