package com.yujj.web.controller.delegate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.yunxin.ClientMessageParams;
import com.jiuyuan.entity.yunxin.UserYxRelation;
import com.jiuyuan.entity.yunxin.YunXinUserParams;
import com.jiuyuan.entity.yunxin.YxAudioMessage;
import com.jiuyuan.entity.yunxin.YxImageMessage;
import com.jiuyuan.entity.yunxin.YxTextMessage;
import com.jiuyuan.entity.yunxin.YxVideoMessage;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ChatFacade;
import com.yujj.business.service.ArticleService;
import com.yujj.business.service.UserService;
import com.yujj.business.service.YunXinUserRelationService;
import com.yujj.entity.account.User;

@Service
public class ChatDelegator {

	@Autowired
	private ChatFacade chatFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private YunXinUserRelationService yunXinUserRelationService;

	@Autowired
	private ArticleService articleService;

	private static final String[] accounts = { "13675810819", "xiaoyuer" };

	public JsonResponse getClientAccount(Long userId) {

		JsonResponse response = new JsonResponse();

		int index = (int) (userId % 2);

//		User user = userService.getUserByUserName(accounts[index]);
		User user = userService.getUserByAllWay(accounts[index]);


		if (user == null) {
			return response.setResultCode(ResultCode.IM_ERROR_CLIENT_USER);
		}

		Map<Long, UserYxRelation> userMap = yunXinUserRelationService
				.getUserYxRelations(Arrays.asList(user.getUserId()));

		if (!userMap.containsKey(user.getUserId())) {
			return response.setResultCode(ResultCode.IM_ERROR_CLIENT_USER);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		UserYxRelation uyr = userMap.get(user.getUserId());
		uyr.setUsername(null);
		uyr.setPassword(null);
		data.put("user", uyr);

		return response.setData(data).setSuccessful();
	}

	public JsonResponse initYunXinAccount(Long userId,YunXinUserParams params) {
		
		JsonResponse response=new JsonResponse();
		
		UserYxRelation relation=new UserYxRelation();
		relation.setUserId(userId);
		String accid = DigestUtils.md5Hex(UUID.randomUUID().toString());
		relation.setUsername(accid);
		relation.setNickname(params.getNickname());
		relation.setIcon(params.getIcon());

		Boolean result = chatFacade.initYunXinAccount(relation);
		if (!result) {
			response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("yunxinUser", relation);

		return response.setSuccessful().setData(data);
	}

	public JsonResponse updateYunXinAccount(Long userId, YunXinUserParams params) {

		JsonResponse response = new JsonResponse();

		UserYxRelation relation = chatFacade.getYunXinAccount(userId);
		if (relation == null) {
			return response.setResultCode(ResultCode.IM_ERROR_USER_MISSING);
		}
		relation.setNickname(params.getNickname());
		relation.setIcon(params.getIcon());

		Boolean result = chatFacade.updateYunXinAccount(relation);
		if (!result) {
			response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("yunxinUser", relation);

		return response.setSuccessful().setData(data);

	}

	public JsonResponse refreshToken(Long userId, YunXinUserParams params) {

		JsonResponse response = new JsonResponse();


		Map<Long, UserYxRelation> userRelationMap = yunXinUserRelationService
				.getUserYxRelations(Arrays.asList(userId));
		if (!userRelationMap.containsKey(userId)) {
			return response
.setResultCode(ResultCode.IM_ERROR_USER_MISSING);
		}

		UserYxRelation relation = userRelationMap.get(userId);

		if (StringUtils.isNoneBlank(relation.getPassword())
				&& (System.currentTimeMillis() - relation.getUpdateTime() <= 30 * 60 * 60 * 1000)) {
			// 防止并发刷新token，引起无法发送消息
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("userRelation", relation);

			return response.setSuccessful().setData(data);
		}

		Boolean result = chatFacade.refreshToken(relation);
		if (!result) {
			response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("token", relation.getPassword());

		return response.setSuccessful().setData(data);
	}

	public JsonResponse sendYunXinMessage(Long userId,
			ClientMessageParams params) {

		JsonResponse response = new JsonResponse();

		// 检查帐号初始化信息

		Long toUserId = NumberUtils.toLong(params.getUserId());
		User user = userService.getUser(toUserId);

		if (user == null) {
			return response
					.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
		}

		Map<Long, UserYxRelation> yxMap = yunXinUserRelationService
				.getUserYxRelations(Arrays.asList(userId,
				toUserId));

		if (!yxMap.containsKey(toUserId) || !yxMap.containsKey(userId)) {
			return response.setResultCode(ResultCode.IM_ERROR_USER_MISSING);
		}

		UserYxRelation fromUser = yxMap.get(userId);

		UserYxRelation toUser = yxMap.get(toUserId);
		if (params.getType() == 0) {// 文本

			YxTextMessage textMessage = new YxTextMessage();
			textMessage.setFrom(fromUser.getUsername());
			textMessage.setTo(toUser.getUsername());
			textMessage.setOpe(params.getMessageType().toString());
			textMessage.setType(params.getType().toString());
			textMessage.setMsg(params.getMsg());

			if (!chatFacade.sendMessage(textMessage)) {
				return response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
			}

		} else if (params.getType() == 1) {// 图片

			YxImageMessage imageMessage = new YxImageMessage();
			imageMessage.setFrom(fromUser.getUsername());
			imageMessage.setTo(toUser.getUsername());
			imageMessage.setOpe(params.getMessageType().toString());
			imageMessage.setType(params.getType().toString());
			imageMessage.setName(params.getName());
			imageMessage.setUrl(params.getUrl());
			imageMessage.setMd5(params.getMd5());
			imageMessage.setW(params.getW());
			imageMessage.setH(params.getH());
			imageMessage.setSize(params.getSize());

			if (!chatFacade.sendMessage(imageMessage)) {
				return response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
			}

		} else if (params.getType() == 2) {// 音频

			YxAudioMessage audioMessage = new YxAudioMessage();
			audioMessage.setFrom(fromUser.getUsername());
			audioMessage.setTo(toUser.getUsername());
			audioMessage.setOpe(params.getMessageType().toString());
			audioMessage.setType(params.getType().toString());
			audioMessage.setUrl(params.getUrl());
			audioMessage.setDur(params.getDur());
			audioMessage.setMd5(params.getMd5());
			audioMessage.setExt(params.getExt());
			audioMessage.setSize(params.getSize());

			if (!chatFacade.sendMessage(audioMessage)) {
				return response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
			}

		} else if (params.getType() == 3) {// 视频

			YxVideoMessage videoMessage = new YxVideoMessage();
			videoMessage.setFrom(fromUser.getUsername());
			videoMessage.setTo(toUser.getUsername());
			videoMessage.setOpe(params.getMessageType().toString());
			videoMessage.setType(params.getType().toString());
			videoMessage.setUrl(params.getUrl());
			videoMessage.setDur(params.getDur());
			videoMessage.setMd5(params.getMd5());
			videoMessage.setExt(params.getExt());
			videoMessage.setSize(params.getSize());
			videoMessage.setW(params.getW());
			videoMessage.setH(params.getH());
			if (!chatFacade.sendMessage(videoMessage)) {
				return response.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
			}

		}

		return response;
	}

	public List<Article> getQuestionList(long categoryId, PageQuery pageQuery) {


		return articleService.getQuestions(categoryId, pageQuery);
	}
}
