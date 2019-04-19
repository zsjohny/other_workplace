var re_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/, //手机校验
	re_null = /\S/;   //空
/**
 * 获取url参数
 */
function getUrlParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) return decodeURI(r[2]);
    return null;
}
/**
 * 登录修改密码,定时隐藏格式校验提示框函数
 */
function timingHidePrompt(){
	 setTimeout(function(){
	    $(".prompt-wrap").removeClass("zoomIn").hide();
	    $(".prompt-wrap").find(".text").html("");
	 },2000)
}
/**
 * 登录和修改密码时，定时隐藏提交后返回错误的提示框
 */
function timingHideError(){
	 setTimeout(function(){
	    $(".prompt-mod").hide().find(".error p").html("");
	 },2000)
}
/*
 *  简单的弹出框
 * @param 调用方法 comfirmBox("show","msg",btnHtml);
 * @param show-方式有 show 、fadeIn
 * @param msg-提示的文字
 * @param btnHtml-默认为1只显示一个按钮，btnHtml为2时显示两个按钮，还自定义按钮代码
 */  
function comfirmBox(fadetype, msg, btnHtml) {
    var boxHTML = "<div class='xbox'><div class='bg'></div><div class='info-box'><div class='info'></div><div class='J-btn'><span class='true'>确定</span></div></div></div>";
    $("body").append(boxHTML);
    $(".xbox").find(".info").html(msg);
    console.log(btnHtml);
    if(btnHtml == 1){
        $(".xbox").find(".J-btn").html("<span class='true'>确定</span>");
    }else if(btnHtml == 2){
        $(".xbox").find(".J-btn").html("<span class='close'>取消</span><span class='sure'>确定</span>");
    }else{//自定义
        $(".xbox").find(".J-btn").html(btnHtml);
    }
    var $fade = fadetype;
    //显示效果方式
    if($fade == "fadeIn"){
        $(".xbox").fadeIn(50);  
    }else if($fade == "show"){
        $(".xbox").show();
    }
    //关闭弹出层
    $(".J-btn .true,.bg,.close").click(function(){
        $(".xbox").hide();
        $("body").find(".xbox").remove();
    })  
}
//跳转链接，closeLink--关闭页面时跳转的url
function comfirmBoxLink(fadetype, msg, btnHtml,closeUrl) {
    var boxHTML = "<div class='xbox'><div class='bg'></div><div class='info-box'><div class='info'></div><div class='J-btn'></div></div></div>";
    $("body").append(boxHTML);
    $(".xbox").find(".info").html(msg);
    $(".xbox").find(".J-btn").html(btnHtml);
    var $fade = fadetype;
    //显示效果方式
    if($fade == "fadeIn"){
        $(".xbox").fadeIn(50);  
    }else if($fade == "show"){
        $(".xbox").show();
    }
    //关闭弹出层,然后跳转至相关页面
    $(".bg").click(function(){
        $(".xbox").hide();
        $("body").find(".xbox").remove();
        window.location.href = closeUrl;
    })  
}