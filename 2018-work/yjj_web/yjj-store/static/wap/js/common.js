/**
 * 获取URL中参数
 *
 * @param name	参数键值
 */
function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) return decodeURI(r[2]);
	return null;
}
/**
 * 提示弹窗
 *
 * @param btnText 自定义按钮文字	
 * @param msg-自定义提示文字
 */
function popupPrompt(msg,btnText) {
	var popupHTML = '<div class="ui-popup js-popup">'+
        	'<div class="bg js-close"></div>'+
			'<div class="box">'+
			    '<div class="title"></div>'+
				'<div class="btn js-close">知道了</div>'+
			'</div>'+
        '</div>';
    $("body").append(popupHTML);
	if(msg){
		$(".ui-popup .title").html(msg);
	}
    if(btnText){
        $(".btn").html(btnText);
    }else{
        $(".btn").html('知道了');
	}
    $(".js-close").on("click",function () {
		$("body .js-popup").remove();
    })
}

/**
 * 判断客户端是否是iOS或者Android
 */
function systemType() {
	var flag = '';
    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  //判断iPhone|iPad|iPod|iOS
        flag = "iOS";
    } else if (/(Android)/i.test(navigator.userAgent)) {   //判断Android
        flag = "Android";
    } else {  //pc

    };
    console.log(flag);
    return flag
}
var url = window.location.href,
    apiUrl = 'https://local.yujiejie.com/jstore';
if(url.indexOf("nessary.top") > 0 || url.indexOf("local.yujiejie") > 0){
    apiUrl = 'https://local.yujiejie.com/jstore';   //地址
}
