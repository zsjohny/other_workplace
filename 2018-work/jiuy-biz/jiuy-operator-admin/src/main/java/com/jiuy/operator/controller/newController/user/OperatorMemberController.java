package com.jiuy.operator.controller.newController.user;

import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.service.common.IYjjMemberService;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.member.MemberOperatorRequest;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 会员开通
 * @author hyf
 * @version V1.0
 * @date 2018/9/3 19:03
 * @Copyright 玖远网络
 */
@RequestMapping("operator/member")
@Controller
public class OperatorMemberController extends BaseController {

    Logger logger = LoggerFactory.getLogger(OperatorMemberController.class);

    @Autowired
    private IYjjMemberService memberService;


    /**
     *  添加修改会员
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/9/3 19:21
     */
    @RequestMapping("change")
    @ResponseBody
    public JsonResponse addMember(MemberOperatorRequest memberOperatorRequest){
        logger.info("添加会员 name={}，phone={}",memberOperatorRequest.getName(),memberOperatorRequest.getPhone());

        return memberService.addMember(memberOperatorRequest);
    }
    /**
     * 查找会员
     * @param membersFindRequest membersFindRequest
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/9/4 9:14
     */
    @ResponseBody
    @RequestMapping("members")
    public JsonResponse findMembers(MembersFindRequest membersFindRequest){
        Page<MemberOperatorResponse> page = memberService.findAllMembers(membersFindRequest);
        return JsonResponse.successful(super.packForBT(page));
    }
}
