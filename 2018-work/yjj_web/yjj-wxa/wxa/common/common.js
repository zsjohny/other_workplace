/**
 * 通用、共用js
 */
var constant = require('../constant');
var util = require('../utils/util.js');
var dialog = require("../utils/dialog");
var requestUtil = require('../service/request.js');
var myRequest = requestUtil.myRequest,
    httpRequest = requestUtil.httpRequest,
    newRequest = requestUtil.newRequest,
    apiDomain = constant.apiUrl,
    devUrl = constant.devUrl;
/**
 * 判断当前是否有网络连接，跳转到应用内的某个页面wx.navigateTo()
 */
function judgeNavigateTo(url){
  console.log(url);
  wx.getNetworkType({
    success: function(res) {
      // 返回网络类型, 有效值：wifi/2g/3g/4g/unknown(Android下不常见的网络类型)/none(无网络)
      var networkType = res.networkType;
      if (networkType == 'none') {
        dialog.toastError("网络异常，请稍后尝试")
      } else {
        //有网络连接时才跳转
        wx.navigateTo({
          url: url
        })
      }
    }
  })
}

/**
 * 判断当前是否有网络连接
 * successback-有网络时的回调函数
 * failback -无网络时的回调函数
 */
function judgeNetwork(successback, failback) {
  wx.getNetworkType({
    success: function (res) {
      // 返回网络类型, 有效值：wifi/2g/3g/4g/unknown(Android下不常见的网络类型)/none(无网络)
      var networkType = res.networkType;
      if (networkType == 'none') {
        dialog.toastError("网络异常，请稍后尝试");
        failback();
      } else {
        successback()
      }
    }
  })
}

/**
 * 分享函数
 * @param:
 *  memberId	 -用户id
 *  shareType - 1 分享活动 2分享商品 3分享优惠券
 *  targetId  -被分享的id 商品id/活动Id/优惠券id
 */
function shareFunc(param, callback) {
  var param = {
    data: param,
    isSign:true
  }
  var url = apiDomain + "/activity/activity/share/doShare";
  httpRequest(url,param).then(res => {
    console.log("分享接口返回数据", res);
    var data = res.data;
    callback(data)
  });
}

/**
 * 查询玖币账户数据
 */
function getCoinAccountData(param, callback) {
  var param = {
    data: param
  }
  var url = "/wxa/wxaAccount.do";
  myRequest(url, param).then(res => {
    console.log("玖币用户数据", res);
    var data = res.data;
    callback(data)
  });
}

//输出接口
module.exports = {
  judgeNavigateTo:judgeNavigateTo,
  judgeNetwork: judgeNetwork,
  shareFunc: shareFunc,
  getCoinAccountData: getCoinAccountData
}