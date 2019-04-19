package com.yujj.web.controller.test;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.yunxin.ClientMessageParams;
import com.jiuyuan.entity.yunxin.YunXinUserParams;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.test.util.YjjAbstractUnitilsTest;
import com.yujj.web.controller.delegate.ChatDelegator;

public class LoginControllerTest extends YjjAbstractUnitilsTest {

	@Autowired
	private ChatDelegator chatDelegator;

	@Test
	@Ignore
	public void testInitYunXinAccount() {
		YunXinUserParams params = new YunXinUserParams();
		params.setNickname("小黑");
		params.setIcon("http://wx.qlogo.cn/mmopen/iaTD4jRARvyOegqv4nY0zfNkjIGn1BnG8ibTQ27PhIbniamO6okKv6e8A9XD55NwDr92uSsUd0ORF8I8ocW4HnLhESHPZq4BFIc/0");
		JsonResponse response = chatDelegator.initYunXinAccount(2L, params);
	}

	@Test
	@Ignore
	public void testUpdateYunXinAccount() {
		YunXinUserParams params = new YunXinUserParams();
		params.setNickname("小黑");
		params.setIcon("http://wx.qlogo.cn/mmopen/iaTD4jRARvyOegqv4nY0zfNkjIGn1BnG8ibTQ27PhIbniamO6okKv6e8A9XD55NwDr92uSsUd0ORF8I8ocW4HnLhESHPZq4BFIc/0");
		JsonResponse response = chatDelegator.updateYunXinAccount(1L, params);
	}

	@Test
	@Ignore
	public void testRefreshToken() {
		YunXinUserParams params = new YunXinUserParams();
		chatDelegator.refreshToken(1L, params);
	}

	@Test
	@Ignore
	public void testSendYunXinMessage() {

		ClientMessageParams params = new ClientMessageParams();

		params.setUserId("2");
		params.setMessageType(0);
		params.setType(0);
		params.setMsg("测试发云信");
		chatDelegator.sendYunXinMessage(1L, params);
	}

	@Test
	@Ignore
	public void testGetQuestionList() {
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPage(1);
		pageQuery.setPageSize(20);
		List<Article> questionList = chatDelegator.getQuestionList(0, pageQuery);
	}
}
