package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.comment.Comment;
import com.jiuyuan.entity.query.PageQuery;

public interface CommentDao extends DomainDao<Comment, Long>{

	int removeComment(long orderid);

	int updateStatus(Long orderId, int status);

	List<Comment> search(PageQuery pageQuery, Comment comment);

	int searchCount(Comment comment);

}
