/**
 * 
 * @date    2017-04-24 16:05:51
 * @des    获取手机验证码函数
 */
var re_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/,
	re_null = /\S/;
var nowTime = 0;
var sendFlag = 0;
function getCode(codeBtnId,phoneId,ajaxUrl,showErrorId){
	if((parseInt(new Date().getTime())-nowTime) < 1000){
		return 
	}
	nowTime = parseInt(new Date().getTime());
	sendFlag = 1;
	var phoneValue=$.trim($("#"+phoneId).val());
	if(!re_null.test(phoneValue)){
		$("#"+showErrorId).show().find("span").text("请输入手机号");  
	}else if(phoneValue!="" && !re_phone.test(phoneValue)){
		$("#"+showErrorId).show().find("span").text("请输入正确的手机号");				 
	}else{
	  if(phoneValue){
			var time=60;
			$.ajax({
				type:'POST',
				url:ajaxUrl,
				dataType:'json',
				data:{phone:phoneValue},
				success:function(data){
					if (data.code >= 0){
						(function(){
							if (time>0){
								time--;
								$("#"+codeBtnId).attr("disabled","true");
								$("#"+codeBtnId).val("再次获取("+time+")秒");
								setTimeout(arguments.callee,1000);
								sendTypeData = "sms";
							}else{
								$("#"+codeBtnId).val("获取验证码");
								if(phoneValue){
									$("#"+codeBtnId).removeAttr("disabled");
								}
							}
						})();
					}else{
						//alert(JSON.stringify(data.error))
						if(data.code =="-1214"){
							$(".reg-prompt-xbox").fadeIn(100);
							var $html = "<div class='ct'><p>"+data.error+"</p></div><input type='button' class='save-btn' value='知道了'>";
							$(".reg-prompt-xbox .content").html($html);
						}else if(data.code =="-1206"){
							$(".reg-prompt-xbox").fadeIn(100);
							var $html = "<div class='ct'><div class='icon phone-icon'></div><p class='phone-text'>"+data.error+"</p></div><input type='button' class='save-btn' value='知道了'>";
							$(".reg-prompt-xbox .content").html($html);
						}
						 //关闭提示弹窗
						$(".reg-prompt-xbox .bg,.save-btn").click(function(){
							$(".reg-prompt-xbox").fadeOut(100);
						})
					}
				},error: function(XMLHttpRequest, textStatus, errorThrown) {
					$("#"+showErrorId).show().find("span").text(textStatus);
                    $("#"+codeBtnId).removeAttr("disabled");
				}
			})
		}
	}
}



