package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.withdraw.WithDrawApply;
import com.jiuyuan.entity.withdraw.WithDrawApplyVO;

public interface BusinessWithDrawDao {
	
	int searchCount(WithDrawApplyVO bwd);

	List<WithDrawApply> search(PageQuery pageQuery, WithDrawApplyVO businessWithDraw);

	int updateWithDraw(WithDrawApply businessWithDraw);

	WithDrawApply getById(long id);
}
