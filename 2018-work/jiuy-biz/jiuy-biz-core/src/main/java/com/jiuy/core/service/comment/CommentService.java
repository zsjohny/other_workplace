package com.jiuy.core.service.comment;

import java.util.List;

import com.jiuy.core.meta.comment.Comment;
import com.jiuyuan.entity.query.PageQuery;

public interface CommentService {

	int removeComment(long orderid);

	int updateStatus(Long orderId, int status);

	List<Comment> search(PageQuery pageQuery, Comment comment);

	int searchCount(Comment comment);

}
