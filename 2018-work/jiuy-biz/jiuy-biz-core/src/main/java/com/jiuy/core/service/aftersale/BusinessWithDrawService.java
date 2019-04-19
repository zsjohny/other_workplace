package com.jiuy.core.service.aftersale;

import java.util.List;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.withdraw.WithDrawApply;
import com.jiuyuan.entity.withdraw.WithDrawApplyVO;

public interface BusinessWithDrawService {
	
	int searchCount(WithDrawApplyVO businessWithDraw);

	List<WithDrawApply> search(PageQuery pageQuery, WithDrawApplyVO businessWithDraw);

	int updateWithDraw(WithDrawApply businessWithDraw);

	WithDrawApply getWithDrawApplyById(long id);

	void withDrawConfirm(long id, long relatedId, double money, int type, String tradeNo, int tradeWay, String remark);

	
}
