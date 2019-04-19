/**
 * @date   2017-06-02
 * @des    获取语言验证码
 */
var re_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/,
	re_null = /\S/,
	nowSendTime = 0,
	ajaxUrl = "",
	sendTypeData ="",
	smsSendTime = 0;
//phoneValue——手机号码的值; 
function getVoiceCode(phoneValue){
	if((parseInt(new Date().getTime())-nowSendTime) < parseInt(60000)){
		$("#LoginStatus").show().find("span").show().text("获取太频繁啦，请休息一下");
		hidePrompt();
		return 
	}
	nowSendTime = parseInt(new Date().getTime());
	if(phoneValue){
		//需要传送手机号
		ajaxUrl = "/mobile/login/ucpaas/send.json?phone="+phoneValue+"&sendType=voice"; 
	}else{
		ajaxUrl = "/mobile/user/ucpaas/send.json?sendType=voice"; 
	}
	$.ajax({
		type:'POST',
		url:ajaxUrl,
		dataType:'json',
		success:function(data){
			if (data.successful){
				$("#LoginStatus").show().find("span").show().text("验证码已发出，请注意接听"); 
				sendTypeData = "voice";
				hidePrompt();
			}
		},error: function(textStatus) {
			$("#LoginStatus").show().find("span").show().text(textStatus);
			hidePrompt();
		}
	})
}
//自动隐藏提示信息
function hidePrompt(){
	setTimeout(function(){
		$("#LoginStatus").hide()
	},3000)
}
//获取语音验证码,不需要传手机号,当先点击获取验证码后，60秒后方可点击语音验证码
function bindVoice(nowTime,sendFlag){
	//console.log(nowTime,sendFlag,parseInt(new Date().getTime())-nowTime);
	if(sendFlag == 1){
		if((parseInt(new Date().getTime())-nowTime) < parseInt(60000)){	
			$("#LoginStatus").show().find("span").show().text("获取太频繁啦，请休息一下");
			hidePrompt();
		}else{
			console.log((parseInt(new Date().getTime())-nowTime));
			getVoiceCode()
		}
	}else{
		getVoiceCode()
	}
}
//需要传手机号
function regBindVoice(nowTime,sendFlag,phoneValue){
	if(sendFlag == 1){
		if((parseInt(new Date().getTime())-nowTime) < parseInt(60000)){	
			$("#LoginStatus").show().find("span").show().text("获取太频繁啦，请休息一下");
			hidePrompt();
		}else{
			getVoiceCode(phoneValue)
		}
	}else{
		getVoiceCode(phoneValue)
	}
}


