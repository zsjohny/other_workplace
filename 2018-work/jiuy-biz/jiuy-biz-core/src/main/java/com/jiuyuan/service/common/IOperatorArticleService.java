package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.OperatorArticle;
import com.jiuyuan.entity.newentity.OperatorSeo;

public interface IOperatorArticleService {

	void add(String title, String abstracts, String previewImageUrl, String content, String seoTitle,
			String seoDescriptor, String seoKeywords, long operatorId);

	Map<String, Object> detail(long operatorArticleId);

	void update(String title, String abstracts, String previewImageUrl, String content, String seoTitle, String seoDescriptor, String seoKeywords, long operatorArticleId);

	void delete(long operatorArticleId, long operatorUserId);

	int homePage(long operatorUserId);

	List<Map<String,Object>> list(Page<Map<String, Object>> page, String articleTitle, String articleAbstracts);

	int getArticleCount();

	void generateArticleList(OperatorSeo defaultSeo);

}