package com.yujj.web.controller.delegate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.UserQuestionService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.community.UserQuestion;

@Service
public class UserQuestionDelegator {

    @Autowired
    private UserQuestionService userQuestionService;
    
    @Autowired
    private MemcachedService memcachedService;
    
    
    public JsonResponse addQuestion(String content, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	 String groupKey = MemcachedKey.GROUP_KEY_USER_QUESTION;
         
         String key = userDetail.getUserId() + "";
         Object obj = memcachedService.get(groupKey, key);
         String  contentTemp ="";
         if (obj != null) {
        	 contentTemp = (String) obj;
        	 return jsonResponse.setResultCode(ResultCode.USER_QUESTION_FREQUENTLY);
         } else {
        	 contentTemp = content;
        	 userQuestionService.addQuestion(content, userDetail);
        	 memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE * 3, content);
         }
    	
    	
    	return jsonResponse.setSuccessful();
    }
    
    public Map<String, Object> getUserQuestionList(long userId, PageQuery pageQuery) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
//    	favoriteService.getFavoriteArticleList(userId, pageQuery);
    	List<UserQuestion> userQuestionList = userQuestionService.getUserQuestionList(userId, pageQuery);
    	result.put("userQuestionList", userQuestionList);
    	return result;
    }

	
}
