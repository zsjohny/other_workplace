package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ArticleUserSubscribe;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.vas.subscrible.get response.
 *
 * @author auto
 * @since 2.0
 */
public class VasSubscribleGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 用户订购信息
	 */
	@ApiListField("article_user_subscribes")
	@ApiField("article_user_subscribe")
	private List<ArticleUserSubscribe> articleUserSubscribes;

	public void setArticleUserSubscribes(List<ArticleUserSubscribe> articleUserSubscribes) {
		this.articleUserSubscribes = articleUserSubscribes;
	}
	public List<ArticleUserSubscribe> getArticleUserSubscribes( ) {
		return this.articleUserSubscribes;
	}

}
