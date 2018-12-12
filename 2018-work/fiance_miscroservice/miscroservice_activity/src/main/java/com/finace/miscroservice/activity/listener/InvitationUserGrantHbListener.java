package com.finace.miscroservice.activity.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.service.UserJiangPinService;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.enums.ActiveGiftEnums;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.commons.utils.tools.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 被邀请人投资，送邀请人红包
 */
@Component
public class InvitationUserGrantHbListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(InvitationUserGrantHbListener.class);

    @Autowired
    private UserRedPacketsService userRedPacketsService;
	@Autowired
    private UserJiangPinService userJiangPinService;

	public static final Integer isNotSend=0;
	public static final Integer isSend=1;
	@Override
	protected void transferTo(String transferData) {
		logger.info("被邀请人投资,送邀请人红包,接受数据={}", transferData);
		if(StringUtils.isEmpty(transferData) && "0".equals(transferData)) {
			logger.warn("被邀请人投资,送邀请人红包,接受数据失败", transferData);
			return;
		}

		JSONObject jsonData = JSONObject.parseObject(transferData);
		Double amt = Double.valueOf(jsonData.getString("buyamt"));  //首投金额
		Integer userid = Integer.valueOf(jsonData.getString("userid"));  //用户id
		Integer inviter = Integer.valueOf(jsonData.getString("inviter"));  //邀请人id
		Integer code = Integer.valueOf(jsonData.getString("code"));  //类型
		//荐面奖
		if (code== ActiveGiftEnums.SING_UP_GIRT.getCode()){
			logger.info("荐面奖--->被邀请人投资,送邀请人红包amt={},userid={},inviter={}", amt, userid, inviter);
			try {
//				if( amt >= 2000 && amt < 4500 ){
//					userRedPacketsService.grantFlq(inviter, userid, "346", amt);
//				}else if( amt >= 4500 && amt < 10000){
				userRedPacketsService.grantFlq(inviter, userid, "392", amt);
				userJiangPinService.addUserAward(userid,inviter,"50元低门槛投资红包", DateUtils.getNowDateStr(),"50",code,isSend);
//				}else if( amt >= 10000 && amt < 15000){
//					userRedPacketsService.grantFlq(inviter, userid, "348", amt);
//				}else if( amt >= 15000 && amt < 25000){
//					userRedPacketsService.grantFlq(inviter, userid, "349", amt);
//				}else if( amt >= 25000 ){
//					userRedPacketsService.grantFlq(inviter, userid, "350", amt);
//				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("邀请投资送红包异常：", e);
			}
			return;
		}
		//佣金奖
		if (code== ActiveGiftEnums.INVITATION_GIRT.getCode()){
			logger.info("佣金奖--->被邀请人投资,送邀请人红包amt={},userid={},inviter={}", amt, userid, inviter);
			List<UserJiangPinPO> list= userJiangPinService.findUserAward(userid,inviter,code);

			if ((amt<30000d&&amt>=20000d&&list.size()==0)||(amt>=30000d&&list.size()==1)){
				for (int i=0;i<2;i++){
					userJiangPinService.addUserAward(userid,inviter,"100元京东卡", DateUtils.getNowDateStr(),"100",code,isNotSend);
				}
			}else if (amt>=30000d&&list.size()==0){
				for (int i=0;i<3;i++){
					userJiangPinService.addUserAward(userid,inviter,"100元京东卡", DateUtils.getNowDateStr(),"100",code,isNotSend);
				}
			}else if ((amt>=10000d&&list.size()==0)||(amt<30000d&&amt>=20000d&&list.size()==1)||(amt>=30000d&&list.size()==2)){
				userJiangPinService.addUserAward(userid,inviter,"100元京东卡", DateUtils.getNowDateStr(),"100",code,isNotSend);
			}
			return;
		}
		//人脉奖
		if (code== ActiveGiftEnums.TEAM_GIRT.getCode()){
			logger.info("人脉奖--->被邀请人投资,送邀请人红包amt={},userid={},inviter={}", amt, userid, inviter);
			userJiangPinService.addUserAward(userid,inviter,"200元京东卡", DateUtils.getNowDateStr(),"200",code,isNotSend);
			return;
		}

		return;
	}

}
