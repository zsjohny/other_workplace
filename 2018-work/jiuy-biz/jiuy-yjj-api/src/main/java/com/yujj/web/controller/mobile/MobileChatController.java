package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.yunxin.ClientMessageParams;
import com.jiuyuan.entity.yunxin.YunXinUserParams;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.ChatDelegator;

@Login
@Controller
@RequestMapping("/mobile/chat")
public class MobileChatController {

	@Autowired
	private ChatDelegator chatDelegator;

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getClientAccount(UserDetail detail) {

		return chatDelegator.getClientAccount(detail.getUserId());
	}
	/**
	 * 初始化云信帐号信息
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "/init", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse initYunXinAccount(YunXinUserParams userParams,UserDetail detail) {

		return chatDelegator.initYunXinAccount(detail.getUserId(), userParams);
	}
	
	/**
	 * 更新云信用户信息
	 * 
	 * @param userParams
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse updateYunxinAccount(YunXinUserParams userParams,
			UserDetail detail) {
		return chatDelegator
				.updateYunXinAccount(detail.getUserId(), userParams);
	}

	/**
	 * 刷新token
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse refreshToken(UserDetail detail) {
		return chatDelegator.refreshToken(detail.getUserId(), null);
	}

	/**
	 * 问题列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/questionlist", method = RequestMethod.GET)
	@ResponseBody
	@NoLogin
	public JsonResponse questionList(@RequestParam(value="category_id", defaultValue="0", required=false) long categoryId, PageQuery pageQuery) {
		
		List<Article> questionList = chatDelegator.getQuestionList(categoryId, pageQuery);

		JsonResponse response = new JsonResponse();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", questionList);

		return response.setSuccessful().setData(data);
	}

	/**
	 * 发送云信消息
	 * 
	 * @param pageQuery
	 * @return
	 */
	@RequestMapping(value = "/sendmessage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse sendYunxinMessage(ClientMessageParams params,
			UserDetail detail) {

		return chatDelegator.sendYunXinMessage(detail.getUserId(), params);

	}

}