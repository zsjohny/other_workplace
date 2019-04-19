package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.article.ArticleVO;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.entity.favorite.UserLike;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.business.service.ArticleService;
import com.yujj.business.service.UserFavoriteService;
import com.yujj.entity.account.UserDetail;

@Service
public class ArticleFacade {
	
	@Autowired
	private ArticleService articleService;

	@Autowired 
	private UserFavoriteService userFavoriteService;
	
	public Article getArticleById(long id) {
		return articleService.getArticleById(id);
	}

	public ResultCode addPageView(long id) {
		articleService.addPageView(id);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public List<ArticleVO> getCommunityArticals(PageQuery pageQuery, UserDetail userDetail, Long arCategoryId) {
		List<Article> articles = articleService.getCommunityArticals(pageQuery, arCategoryId);
		
		List<Long> articleIds = new ArrayList<>();
		for (Article article : articles) {
			articleIds.add(article.getId());
		}
		
		Map<Long, UserFavorite> favoriteCountMap = new HashMap<>();
		Map<Long, UserLike> userLikeMap = new HashMap<>();
		Map<Long, Integer> likeCountById = new HashMap<>();
		
		assembleMap(favoriteCountMap, userLikeMap, likeCountById, userDetail, articleIds);
		
		List<ArticleVO> articleVOs = new ArrayList<>();
		for (Article article : articles) {
			ArticleVO articleVO = new ArticleVO();
			assembleArticleVO(article, articleVO, favoriteCountMap, userLikeMap, likeCountById);
			
			articleVOs.add(articleVO);
		}
		
		return articleVOs;
	}
	
    public List<ArticleVO> getFavoriteArticleList(UserDetail userDetail, PageQuery pageQuery) {
    	
    	
//    	favoriteService.getFavoriteArticleList(userId, pageQuery);
    	List<ArticleVO> articleList = articleService.getUserFavoriteArticleVOList(userDetail.getUserId(), pageQuery);
    	
    
		for (ArticleVO articleVO : articleList) {
			if (articleVO.getAbstracts() == null || StringUtils.equals("", articleVO.getAbstracts())) {
				articleVO.setAbstracts(removeTag(StringEscapeUtils.unescapeHtml4(articleVO.getContent())));
			}
		}

    	
    	return articleList;
    }

	public ArticleVO getCommunityArticleById(UserDetail userDetail, Long id) {
		Article article = articleService.getArticleById(id);
		
		List<Long> articleIds = new ArrayList<>();
		articleIds.add(article.getId());
		
		Map<Long, UserFavorite> favoriteCountMap = new HashMap<>();
		Map<Long, UserLike> userLikeMap = new HashMap<>();
		Map<Long, Integer> likeCountById = new HashMap<>();
		assembleMap(favoriteCountMap, userLikeMap, likeCountById, userDetail, articleIds);
		
		ArticleVO articleVO = new ArticleVO();
		assembleArticleVO(article, articleVO, favoriteCountMap, userLikeMap, likeCountById);
		
		return articleVO;
	}

	private void assembleMap(Map<Long, UserFavorite> favoriteCountMap, Map<Long, UserLike> userLikeMap,
			Map<Long, Integer> likeCountById, UserDetail userDetail, List<Long> articleIds) {
		if (userDetail.getUser() != null) {
			favoriteCountMap.putAll(userFavoriteService.getUserFavoriteMap(articleIds, 2, userDetail.getUserId()));
			userLikeMap.putAll(userFavoriteService.getUserLikeMap(articleIds, userDetail.getUserId()));
		}
		likeCountById.putAll(userFavoriteService.getLikeCountById(articleIds));
	}
	
	private void assembleArticleVO(Article article, ArticleVO articleVO, Map<Long, UserFavorite> favoriteCountMap,
			Map<Long, UserLike> userLikeMap, Map<Long, Integer> likeCountById) {
		BeanUtils.copyProperties(article, articleVO);
		
		articleVO.setFavorite(false);
		articleVO.setLiked(false);
		articleVO.setLikeCount(0);
		
		if (articleVO.getAbstracts() == null || StringUtils.equals("", articleVO.getAbstracts())) {
			articleVO.setAbstracts(removeTag(StringEscapeUtils.unescapeHtml4(articleVO.getContent())));
		}
		
		if (favoriteCountMap.get(article.getId()) != null) {
			articleVO.setFavorite(true);
		}
		if (userLikeMap.get(article.getId()) != null) {
			articleVO.setLiked(true);
		}
		
		if (likeCountById.get(article.getId()) != null) {
			articleVO.setLikeCount(likeCountById.get(article.getId()));
		}
	}
	
	public static String removeTag(String htmlStr) {
		  String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // script
		  String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // style
		  String regEx_html = "<[^>]+>"; // HTML tag
		  String regEx_space = "\\s+|\t|\r|\n";// other characters

		  Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		  Matcher m_script = p_script.matcher(htmlStr);
		  htmlStr = m_script.replaceAll("");
		  Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		  Matcher m_style = p_style.matcher(htmlStr);
		  htmlStr = m_style.replaceAll("");
		  Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		  Matcher m_html = p_html.matcher(htmlStr);
		  htmlStr = m_html.replaceAll("");
		  Pattern p_space = Pattern
		    .compile(regEx_space, Pattern.CASE_INSENSITIVE);
		  Matcher m_space = p_space.matcher(htmlStr);
		  htmlStr = m_space.replaceAll(" ");
		  return htmlStr;
    }
}
