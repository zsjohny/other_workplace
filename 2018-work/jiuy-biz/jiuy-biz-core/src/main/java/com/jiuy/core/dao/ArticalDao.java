package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.artical.Artical;
import com.jiuy.core.meta.artical.ArticalVO;
import com.jiuyuan.entity.query.PageQuery;

public interface ArticalDao extends DomainDao<Artical, Long>{



	public List<ArticalVO> searchArtical(PageQuery query, String content, long aRCategoryId);

	public int searchArticalCount(String content, long aRCategoryId);

	public Artical addArtical(Artical artical);

	public int updateArtical(Artical artical);

	public ArticalVO loadCatById(long id);

	public int remove(long l);

	public int getCatARCount(Long cat);

	public List<ArticalVO> searchArticalByCat(PageQuery pageQuery, long aRCategoryId);

	public int searchArticalCountByCat(long aRCategoryId);

}
