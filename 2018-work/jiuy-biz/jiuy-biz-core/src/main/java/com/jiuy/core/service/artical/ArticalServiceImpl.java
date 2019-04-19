package com.jiuy.core.service.artical;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.ArticalDao;
import com.jiuy.core.meta.artical.Artical;
import com.jiuy.core.meta.artical.ArticalVO;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.IProductNewService;

@Service
public class ArticalServiceImpl implements ArticalService{

	@Resource
	private ArticalDao articalDaoSqlImpl;
	
	
	@Resource
	private IProductNewService productNewService;
	
	@Override
	public List<ArticalVO> searchArtical(PageQuery query, String content, long aRCategoryId) {
		return articalDaoSqlImpl.searchArtical(query, content, aRCategoryId);
	}

	@Override
	public int searchArticalCount(String content, long aRCategoryId) {
		return articalDaoSqlImpl.searchArticalCount(content, aRCategoryId);
	}

	@Override
	public ResultCode addArtical(Artical artical) {
		long createTime = System.currentTimeMillis();
		
		artical.setCreateTime(createTime);
		artical.setUpdateTime(createTime);
		
		articalDaoSqlImpl.addArtical(artical);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ResultCode updateArtical(Artical artical) {
		long updateTime = System.currentTimeMillis();
		artical.setUpdateTime(updateTime);
		articalDaoSqlImpl.updateArtical(artical);
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ArticalVO loadCatById(long id) {
		return articalDaoSqlImpl.loadCatById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultCode remove(long[] ids) {
		for(int i = 0; i < ids.length ; i++) {
			articalDaoSqlImpl.remove(ids[i]);
		}
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public int getCatARCount(List<Long> catIds) {
		int sum = 0;
		for(Long catId : catIds) {
			int count = articalDaoSqlImpl.getCatARCount(catId);
			sum += count;
		}
		return sum;
	}

	@Override
	public List<ArticalVO> searchArticalByCat(PageQuery pageQuery, long aRCategoryId) {
		return articalDaoSqlImpl.searchArticalByCat(pageQuery, aRCategoryId);
	}

	@Override
	public int searchArticalCountByCat(long aRCategoryId) {
		return articalDaoSqlImpl.searchArticalCountByCat(aRCategoryId);
	}
	
}
