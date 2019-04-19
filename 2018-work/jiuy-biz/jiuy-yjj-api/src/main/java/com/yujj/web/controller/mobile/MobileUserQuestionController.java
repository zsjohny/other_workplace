package com.yujj.web.controller.mobile;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.UserQuestionDelegator;

@Controller
@Login
@RequestMapping("/mobile/user/question")
public class MobileUserQuestionController {

    @Autowired
    private UserQuestionDelegator userQuestionDelegator;

    
    @RequestMapping(value = "/addQuestion" )
    @ResponseBody
    public JsonResponse addQuestion(@RequestParam("content") String content, UserDetail userDetail) {
    	return userQuestionDelegator.addQuestion(content, userDetail);
    }
    
    
    
    @RequestMapping("/myQuestionList")
    @ResponseBody
    public JsonResponse myQuestionList(PageQuery pageQuery, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> result = userQuestionDelegator.getUserQuestionList(userDetail.getUserId(), pageQuery);
        
    	
    	return jsonResponse.setSuccessful().setData(result);
    }


}
