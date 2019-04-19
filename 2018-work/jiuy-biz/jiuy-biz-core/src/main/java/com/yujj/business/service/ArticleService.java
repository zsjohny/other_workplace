package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.article.ArticleVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.ArticleMapper;

@Service
public class ArticleService {

	@Autowired
	private ArticleMapper articleMapper;
	
    @Autowired
    private MemcachedService memcachedService;
	
	public Article getArticleById(long id) {
		String groupKey = MemcachedKey.GROUP_KEY_ARTICAL;
		
		String key = id + "";
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
        	return (Article) obj;
        } else {
        	Article article = articleMapper.getArticleById(id);	
        	if (article != null)
            	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, article);
        	
        	return article;
        }
	}

	@SuppressWarnings("unchecked")
	public List<Article> getQuestions(long categoryId, PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_ARTICAL;
        
        String key = pageQuery.getPage() + "";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<Article>) obj;
        } else {
        	List<Article> articles = articleMapper.getQuestionList(categoryId, pageQuery);	
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, articles);
        	
        	return articles;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<ArticleVO> getUserFavoriteArticleVOList(long userId, PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_ARTICAL;
		
		String key = userId + "" + pageQuery.getPage();
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (List<ArticleVO>) obj;
		} else {
			List<ArticleVO> articles = articleMapper.getUserFavoriteArticleVOList(userId, pageQuery);	
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, articles);
			
			return articles;
		}
	}

	public int addPageView(long id) {
		String groupKey = MemcachedKey.GROUP_KEY_ARTICAL;
        
        String key = "artical_count";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (int) obj;
        } else {
        	int count = articleMapper.addPageView(id);
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, count);
        	
        	return count;
        }
	}
	public int getFavoriteArticleCount(long userId) {
		return articleMapper.getFavoriteArticleCount(userId);
	}

	public List<Article> getCommunityArticals(PageQuery pageQuery, Long arCategoryId) {
		return articleMapper.getCommunityArticals(pageQuery, arCategoryId);
	}

	public int getCommunityArticalsCount(Long arCategoryId) {
		return articleMapper.getCommunityArticalsCount(arCategoryId);
	}
}
