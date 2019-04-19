$(function() { //页面加载
		var windowWidth = $(window).width(); //屏幕宽度
		var windowHeight = $(window).height(); //屏幕高度
		var documentHeight = $(document).height(); //页面高度
		var countdown = 60; //倒计时的时长	
		$(function() {
			/*首页品牌图片固定宽高*/
			if($(".branList").length > 0) {
				$(".branList li .pic").height($(".branList li .pic").width())
				$(".productList li .pic").height($(".productList li .pic").width() / 0.772);
			}
			if($("#scroller").length > 0) {
				$(".mainContent").css("min-height", windowHeight - 50);
			}
			if($(".ProductORCodeBox").length > 0) {
				$(".ProductORCodeBox").height($(".ProductORCodeBox").width() + 30)
			}

		});

		/*规格控制器*/
		
//		$(document).on("click",".Controller",function(){
////		$(".Controller").each(function() {
//			var Controller = $(this);
//			var num = Controller.find("input").val();
//			var allNum = Controller.parent().prev().text();
//			if(num > 0) {
//				Controller.find(".reduce").removeClass("disable");
//			} else {
//				Controller.find(".reduce").addClass("disable")
//			}
//			if(num < allNum) {
//				Controller.find(".plus").removeClass("disable");
//			} else {
//				Controller.find(".plus").addClass("disable");
//			}
//			Controller.find(".plus").click(function() {
//				if(num < allNum) {
//					num++;
//					$(this).prev().val(num);
//					if(num == allNum) {
//						Controller.find(".plus").addClass("disable");
//					}
//					Controller.find(".reduce").removeClass("disable");
//				} else {
//					Controller.find(".plus").addClass("disable");
//				}
//			})
//			Controller.find(".reduce").click(function() {
//				if(num > 0) {
//					num--;
//					$(this).next().val(num);
//					if(num == 0) {
//						Controller.find(".reduce").addClass("disable");
//					}
//					Controller.find(".plus").removeClass("disable");
//				} else {
//					Controller.find(".reduce").addClass("disable");
//				}
//
//			})
//		});

//		$(".SpecificationsSelect ul li").click(function() {
//			$(this).toggleClass("active")
//		})
//
//		$(".PresentWhereabouts input[type='radio']").click(function() {
//			$(".PresentWhereabouts .name").removeClass("active");
//			$(this).parent().addClass("active");
//		})
//		$(".PresentWhereabouts input[type='checkbox']").click(function() {
//			$(this).parent().toggleClass("active");
//			$(this).parent().nextAll(".BankCard").toggle();
//			$("#AccountActivation").addClass("disable").attr("disabled", true);
//
//		})
//		$(".BankCard input").keyup(function() {
//			if(!$(this).val() == "") {
//				$("#AccountActivation").removeClass("disable").removeAttr("disabled");
//			}
//		})

		function AccountActivation() {
			if(VerificationIDCard($("#IDCard"))) {
				var currentStatus = false;
				$(".PresentWhereabouts input[type='checkbox']").each(function() {
					if($(this).is(":checked")) {
						if(!$(this).parent().nextAll(".BankCard").find("input").val() == "") {
							currentStatus = true;
						} else {
							currentStatus = false;
						}
					}
				})
				if(currentStatus) {
					console.log(11)
				}

			}
		}

		//判断是否可以点击"获取手机验证码"按钮
		$("#verifyPhone").keyup(function() {
			if($(this).val().length == 11) {
				$("#verifyPhoneBtn").removeAttr("disabled")
				$("#verifyPhoneBtn").removeClass("disable");

			}
		})

		/*输入框不为空时登录按钮变为可点*/
		$("#loginForm input[name='btnRelation']").keyup(function() {
			if(inputNotEmpty($("#loginForm input[name='btnRelation']"))) {
				$("#loginForm .loginBtnCom").removeClass("disable").removeAttr("disabled");
			} else {
				$("#loginForm .loginBtnCom").addClass("disable").attr("disabled", true);
				
			}
		});
		
		
		/*输入框不为空时登录按钮变为可点*/
		$("#newPassworForm input[name='btnRelation']").keyup(function() {
			if(inputNotEmpty($("#newPassworForm input[name='btnRelation']"))) {
				$("#newPassworForm .loginBtnCom").removeClass("disable");
				$("#newPassworForm .loginBtnCom").removeAttr("disabled")
			} else {
				$("#newPassworForm .loginBtnCom").addClass("disable");
				$("#newPassworForm .loginBtnCom").attr("disabled", true)
			}
		})

		$("input[type='number']").each(function() {
			$(this).onblur = function() {
				if(isNaN(Number(oInp.value))) { //当输入不是数字的时候，Number后返回的值是NaN;然后用isNaN判断。
				}
			}
		})

		/*获取手机验证码按钮*/
		$("#verifyPhoneBtn").click(function() {
			var isPhonenum = VerificationTel($("#verifyPhone"));
			if(isPhonenum) {
//				window.location = "VerificationCode.html";
			} else {
				$("#LoginStatus span").text("请输入正确的手机号码");
				$("#LoginStatus span").show();
				setTimeout(function() {
					$("#LoginStatus span").hide();
				}, 2000)
			}
		})

		/*修改密码完成按钮*/
		$("#ModifyCompleted").click(function() {
			if($("#password1").val() == $("#password2").val()) {
//
//				window.location = "login.html";
			} else {
				$("#LoginStatus span").text("密码不一致，请重新输入");
				$("#LoginStatus span").show();
				setTimeout(function() {
					$("#LoginStatus span").hide();
				}, 2000)
			}

		})

		/*修改提现密码*/
		$("#ModifyCompleted2").click(function() {
			if($("#password1").val() == $("#password2").val()) {
//
//				window.location = "PasswordSetting3.html";
			} else {
				$("#LoginStatus span").text("密码不一致，请重新输入");
				$("#LoginStatus span").show();
				setTimeout(function() {
					$("#LoginStatus span").hide();
				}, 2000)
			}

		})
		
		/*交易密码设置  */
		$("#ModifyCompleted3").click(function() {
			if($("#password1").val() == $("#password2").val()) {
//
//				window.location = "PasswordSetting3.html";
			} else {
//				$("#LoginStatus span").text("");
				$("#LoginStatus span").show();
				setTimeout(function() {
					$("#LoginStatus span").hide();
				}, 2000)
			}

		})

		/*提现提交按钮*/
//		$("#WithdrawalsSubmit").click(function() {
//			if($("#inputAmount").val().length > 0) {
//				var lableAmount = parseInt($("#lableAmount").text());
//				var inputAmount = parseInt($("#inputAmount").val());
//
//				if(lableAmount >= inputAmount) {
//					$('#WithdrawalsSuccess').modal('show')
//				} else {
//					$('#Withdrawalsfail').modal('show')
//				}
//			} else {
//				$('#Withdrawalsfail').modal('show')
//				$('#Withdrawalsfail .p3').text("请输入您要取现的金额")
//			}
//		})

		/*提现 输入提现密码完成*/
		var CauseFailure = 0; //0成功  1提现密码错误   2验证码错误   3验证码过期
		var FailureNum = 0;
		$("#EnterPassword").click(function() {
			if($("#InputVC").val().length > 0) {
				if($("#password1").val().length > 0) {
					if(CauseFailure == 1) {
						FailureMsg("提现密码错误");

					} else if(CauseFailure == 2) {
						FailureNum++;
						if(FailureNum == 5) {
							FailureNum = 0;
//							window.location = "login.html";
						} else {
							$("#password1").val("");
							$("#InputVC").val("");
							FailureMsg("提现密码已连续输错" + FailureNum + "次，再输错" + (5 - FailureNum) + "次该账户将被冻结");
						}
					} else if(CauseFailure == 3) {
						FailureMsg("验证码过期");
					} else {
						FailureNum = 0;
//						window.location=""
					}
				} else {
					FailureMsg("请输入提现密码");
				}
			} else {
				FailureMsg("请输入验证码");
			}

		})

		/*激活账号*/
		$("#AccountActivation").click(function() {
			//			console.log(111)
			if(VerificationIDCard($("#IDCard"))) {
//				window.location = "PasswordSetting4.html";
			}
			else
			{
				$("#LoginStatus span").show();
				setTimeout(function() {
					$("#LoginStatus span").hide();
				}, 2000)
//				FailureMsg("请输入正确身份证号码");
			}
			
		})

		$("#SetUpBtn1").click(function() {

			if(VerificationIDCard($("#IDCard"))) {
				if(VerificationTel($("#PhoneNum"))) {
//					window.location = "PasswordSetting2.html";
				}
			} else {
				$("#LoginStatus span").show();
				setTimeout(function() {
					$("#LoginStatus span").hide();
				}, 2000)
			}
		})

		function FailureMsg(text) {
			$("#LoginStatus span").text(text);
			$("#LoginStatus span").show();
			setTimeout(function() {
				$("#LoginStatus span").hide();
			}, 2000)
		}

		function inputNotEmpty(inputName) {
			var isNotEmpty = 0;
			var inputlength = 0;
			var returnbool = false
			inputName.each(function() {
				inputlength++;
				if(!($(this).val() == "")) {
					isNotEmpty++;
				}
			})
			returnbool = (isNotEmpty == inputlength) && (isNotEmpty > 0);
			return returnbool;
		}

		/*修改密码获取验证码*/
		$(".VerificationCodeBtn").click(function() {
				if((parseInt(new Date().getTime())-nowSendTime) < parseInt(60000)){
		    		$("#LoginStatus").show().find("span").show().text("获取太频繁啦，请休息一下");
		    		hidePrompt();
		    	}else{
		    		settime($(this));
		    	}	
		})
			/*发送验证码*/
		function settime(obj) {
			if(countdown == 0) {
				obj.removeAttr("disabled");
				obj.removeClass("disable")
				obj.val("发送验证码");
				countdown = 60;
				return;
			} else {
				obj.attr("disabled", true);
				obj.addClass("disable")
				obj.val("正在发送(" + countdown + ")")
				countdown--;
			}
			setTimeout(function() {
				settime(obj)
			}, 1000)
		}

		/*展开详情*/
		$(".showDetails .ToggleBtn").click(function() {
			$(this).parent().toggleClass("active");
		})

		/*商品展开详情*/
		$(".DescribeBox .ToggleBtn").click(function() {
			$(this).parent().toggleClass("active");
		})

//				<script type="text/javascript">
//		var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
//		if(!myreg.test($("#phone").val())) 
//		{ 
//		  alert('请输入有效的手机号码！'); 
//		  return false; 
//		} 
//		</script>

		function VerificationTel(phoneNum) {
			console.log(phoneNum);
			var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
			if(!myreg.test(phoneNum.val())) {
				return false;
			} else {
				return true;
			}
		}

		function VerificationIDCard(IDCardNum) {
			var isIDCard2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/;
			if(!isIDCard2.test(IDCardNum.val())) {
				return false;
			} else {
				return true;
			}
		}
	}) //页面加载闭合
		/**
		 * 获取URL中参数
		 * @param key	参数键值
		 * @returns
		 */
		function getUrlParam(key) {
			return getUrlParam(key,"");
		}
		/**
		 * 获取URL中参数
		 * @param key	参数键值
		 * @param defValue	参数键值
		 * @returns
		 */
		function getUrlParam(key,defValue) {
			var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)","i");
			var r = window.location.search.substr(1).match(reg);
			var value = defValue;
			if (r!=null) {
				value = r[2];
			}
			return value;
		}	
	