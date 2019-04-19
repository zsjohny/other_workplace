package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.CommentDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.comment.Comment;
import com.jiuyuan.entity.query.PageQuery;

public class CommentDaoSqlImpl extends DomainDaoSqlSupport<Comment, Long>  implements CommentDao {

	@Override
	public int removeComment(long orderid) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderId", orderid);
		
		return getSqlSession().update("CommentMapper.removeComment", params);
	}

	@Override
	public int updateStatus(Long orderId, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderId", orderId);
		params.put("status", status);
		
		return getSqlSession().update("CommentMapper.updateStatus", params);
	}

	@Override
	public List<Comment> search(PageQuery pageQuery, Comment comment) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("comment", comment);
		
		return getSqlSession().selectList("CommentMapper.search", params);
	}

	@Override
	public int searchCount(Comment comment) {
		return getSqlSession().selectOne("CommentMapper.searchCount", comment);
	}

}
