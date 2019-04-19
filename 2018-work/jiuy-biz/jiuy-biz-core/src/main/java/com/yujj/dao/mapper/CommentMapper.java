package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.comment.Comment;
import com.yujj.entity.order.OrderItem;

@DBMaster
public interface CommentMapper {
	public List<Comment> getComments(@Param("userId") Long userId, @Param("productId") Long productId, @Param("pageQuery") PageQuery pageQuery);
	
	public int addComment(Comment comment);
	
    public int addComments(@Param("time") Long time, @Param("orderItems") List<OrderItem> orderItems);	
	
    public int updateComment(Comment comment);
	
    public int deleteComment(@Param("id") Long id);
	
    public int countComment(@Param("userId") Long userId, @Param("productId") Long productId);
	
    public Comment getById(@Param("id") long id);
}
