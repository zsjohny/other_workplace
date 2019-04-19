package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.BusinessWithDrawDao;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.withdraw.WithDrawApply;
import com.jiuyuan.entity.withdraw.WithDrawApplyVO;

@Repository
public class BusinessWithDrawDaoSqlImpl  implements BusinessWithDrawDao{
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int searchCount(WithDrawApplyVO bwd) {

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", bwd);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BusinessWithDrawDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<WithDrawApply> search(PageQuery pageQuery, WithDrawApplyVO businessWithDraw) {
	    Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("params", businessWithDraw);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.BusinessWithDrawDaoSqlImpl.search", params);
	}

	@Override
	public int updateWithDraw(WithDrawApply businessWithDraw) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("params", businessWithDraw);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.BusinessWithDrawDaoSqlImpl.updateWithDraw", params);
	}

	@Override
	public WithDrawApply getById(long id) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.BusinessWithDrawDaoSqlImpl.getById", params);
	}

}
