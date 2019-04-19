package com.yujj.business.facade;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.yunxin.UserYxRelation;
import com.jiuyuan.entity.yunxin.YxAudioMessage;
import com.jiuyuan.entity.yunxin.YxImageMessage;
import com.jiuyuan.entity.yunxin.YxTextMessage;
import com.jiuyuan.entity.yunxin.YxVideoMessage;
import com.jiuyuan.service.common.YunXinSmsService;
import com.yujj.business.service.YunXinUserRelationService;

@Service
public class ChatFacade {

	@Autowired
	private YunXinUserRelationService yunXinUserRelationService;

	@Autowired
	private YunXinSmsService yunXinSmsService;

	/**
	 * 注册云信帐号
	 * 
	 * @param userYxRelation
	 * @return
	 */
	public Boolean initYunXinAccount(UserYxRelation userYxRelation) {

		Long userId=userYxRelation.getUserId();
		
		Map<Long,UserYxRelation> userRelationMap=yunXinUserRelationService.getUserYxRelations(Arrays.asList(userId));
		
		UserYxRelation dbYxRelation=userRelationMap.get(userId);
		
		if(dbYxRelation==null){
			if (!yunXinSmsService.createYunxinUser(userYxRelation)) {
				return false;
			}
			yunXinUserRelationService.createYxUser(userYxRelation);
		}else{
			if(dbYxRelation.getStatus()==-1){
				return false;
			}

			userYxRelation.setPassword(dbYxRelation.getPassword());
			userYxRelation.setUsername(dbYxRelation.getUsername());
			userYxRelation.setNickname(dbYxRelation.getNickname());
			userYxRelation.setIcon(dbYxRelation.getIcon());
		}
		
		return true;
	}

	/**
	 * 更新云信帐号
	 * 
	 * @param userYxRelation
	 * @return
	 */
	public Boolean updateYunXinAccount(UserYxRelation userYxRelation) {

		if (!yunXinSmsService.updateYunxinUserInfo(userYxRelation)) {
			return false;
		}

		yunXinUserRelationService.updateYxUser(userYxRelation);

		return true;
	}

	public UserYxRelation getYunXinAccount(Long userId) {

		Map<Long, UserYxRelation> map = yunXinUserRelationService
				.getUserYxRelations(Arrays.asList(userId));

		return map.get(userId);
	}

	/**
	 * 刷新云信token
	 * 
	 * @param userId
	 * @return
	 */
	public Boolean refreshToken(UserYxRelation userYxRelation) {

		yunXinSmsService.refreshToken(userYxRelation);

		yunXinUserRelationService.updateToken(userYxRelation.getUserId(),
				userYxRelation.getPassword());

		return true;
	}

	public Boolean sendMessage(YxTextMessage textMessage) {

		return yunXinSmsService.sendText(textMessage);
	}

	public Boolean sendMessage(YxImageMessage imageMessage) {

		return yunXinSmsService.sendImage(imageMessage);
	}

	public Boolean sendMessage(YxAudioMessage audioMessage) {

		return yunXinSmsService.sendAudio(audioMessage);
	}

	public Boolean sendMessage(YxVideoMessage videoMessage) {

		return yunXinSmsService.sendVideo(videoMessage);
	}
}
