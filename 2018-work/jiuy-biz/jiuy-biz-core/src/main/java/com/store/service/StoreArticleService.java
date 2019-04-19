package com.store.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.StoreArticle;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.store.StoreWxa;
import com.store.dao.mapper.StoreArticleMapper;

/**
 * <p>
 * 门店文章表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-02
 */
@Service
public class StoreArticleService{

	@Autowired
	private StoreArticleMapper storeArticleMapper;
	
	
	public List<StoreArticle> getStoreArticleListByStoreId(long storeId) {
		Wrapper<StoreArticle> wrapper = new EntityWrapper<StoreArticle>()
				.eq("store_id", storeId)
				.eq("status", 1)
				.orderBy("update_time",false)
				.last ("LIMIT 5")
				;
		return storeArticleMapper.selectList(wrapper);
	}
	
	/**
	 * 添加
	 * @param storeArticle
	 */
	public void addArticle(StoreArticle storeArticle) {
		storeArticleMapper.insert(storeArticle);
	}

	/**
	 * 获取
	 * @param articleId
	 * @return
	 */
	public StoreArticle getArticleById(long articleId) {
//		storeArticleMapper.selectById(articleId);

		return storeArticleMapper.selectStoreArticle(articleId);
	}

	/**
	 * 删除
	 * @param storeArticle
	 */
	 @Transactional(rollbackFor = Exception.class)
	public void updateArticle(StoreArticle storeArticle) {
		storeArticleMapper.updateById(storeArticle);
	}
	
	public List<StoreArticle> selectList(Wrapper<StoreArticle> wrapper, PageQuery pageQuery) {
		RowBounds rowBounds = new RowBounds(pageQuery.getOffset(), pageQuery.getLimit());
		List<StoreArticle> list = storeArticleMapper.selectPage(rowBounds, wrapper);
		return list;
	}
	
	public int selectCount(Wrapper<StoreArticle> wrapper) {
		return storeArticleMapper.selectCount(wrapper);
	}
	/**
	 * 插入并获得主键
	 * @param storeArticle
	 * @return
	 */
	 @Transactional(rollbackFor = Exception.class)
	 @Deprecated
	public long insertAndGetId(StoreArticle storeArticle) {
		return storeArticleMapper.insertAndGetId(storeArticle);
	}

	public StoreArticle selectById(long id) {
		return storeArticleMapper.selectById(id);
		
	}


	/**
	 * 查询文章列表(排序后)
	 *
	 * <p>历史兼容,用他自己的排序</p>
	 * @param query query
	 * @param pageQuery pageQuery
	 * @param searchContext 是否包含文本字段, 开启后查询慢
	 * @return java.util.List<com.jiuyuan.entity.newentity.StoreArticle>
	 * @author Charlie
	 * @date 2018/8/8 20:06
	 */
    public List<StoreArticle> listArticlesSort(StoreArticle query, PageQuery pageQuery, Boolean searchContext) {
		return storeArticleMapper.listArticlesSort(query, pageQuery, searchContext);
    }
}
