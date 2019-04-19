package com.jiuy.core.service.artical;

import java.util.List;

import com.jiuy.core.meta.artical.Artical;
import com.jiuy.core.meta.artical.ArticalVO;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;

public interface ArticalService {

	List<ArticalVO> searchArtical(PageQuery query, String content, long aRCategory);

	int searchArticalCount(String content, long aRCategory);

	ResultCode addArtical(Artical artical);

	ResultCode updateArtical(Artical artical);

	ArticalVO loadCatById(long id);

	ResultCode remove(long[] ids);

	int getCatARCount(List<Long> cats);

	List<ArticalVO> searchArticalByCat(PageQuery pageQuery, long aRCategoryId);

	int searchArticalCountByCat(long aRCategoryId);

}
