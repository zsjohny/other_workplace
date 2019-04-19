//app.js
var Promise = require('./utils/es6-promise.min.js').Promise;
var util = require("./utils/util");
var constant = require('./constant');
var com = require('./common/common');  //共用js
var api = require('./service/facadeApi'),                     //老的api接口
    distributionApi = require('./service/distributionApi'),   //新的分销api接口
    publicApi = require('./service/publicApi'),   //新的所有接口
    orderApi = require('./service/orderApi');   //售后和订单api接口

App({
  onLaunch: function () {
    //调用API从本地缓存中获取数据
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs);
    this.globalData.isReadState = true;
    this.globalData.isShowConponNotice = true;
  },
  getUserInfo: function (successFun, failFun) {
    var that = this
    if (this.globalData.userInfo) {
      typeof successFun == "function" && successFun(this.globalData.userInfo)
    } else {
      //调用登录授权接口
      //loginAuthorize(that, successFun, failFun);
    }
  },
  facade: api,
  distributionApi:distributionApi,
  orderApi: orderApi,
  publicApi: publicApi,
  common: com,
  util: util,
  constant: constant,
  globalData: {
    userInfo: null,
    userMemberId: null,
    isReadState: false,          //打开小程序清楚未读消息状态
    isShowConponNotice: false,   //首页优惠券提醒
    isShowIndex: false,          //从首页、打开商品详情返回首页是否重载数据  false-刷新、true-不刷新
    isRefleshProductList: false,  //我的颜值分页面、商城、喜欢的、标签、搜索列表打开商品详情返回页面是否刷新数据  false-刷新、true-不刷新
  },
  //登录successFun-成功回调
  loginAuthorizeFun: function (fromValue,successFun) {
    var sessionId = wx.getStorageSync("sessionId");
    if (sessionId) {
      wx.checkSession({
        success: function () {
          //session 未过期，并且在本生命周期一直有效
          console.log("checkSession未过期");
          //获取会员信息
          var id = wx.getStorageSync('id');
          successFun(id)
        },
        fail: function () {
          //登录态过期,调用登录接口
          loginAuthorize(fromValue,function(id){
            console.log("checkSession过期");
            successFun(id)
          });
        }
      })
    } else {
      //登录态过期,调用登录接口
      loginAuthorize(fromValue,function (id) {
        console.log("sessionId过期");
        successFun(id)
      });
    }
  },
  //只登陆
  loginUnauthorizedFun: function (callback) {
    loginUnauthorized(callback)
  },
  //获取第三方自定义字段
  getThirdDefineField: function () {
    getThirdDefineData()
  }
})
//获取第三方自定义字段
function getThirdDefineData() {
  let extConfig = wx.getExtConfigSync ? wx.getExtConfigSync() : {}
  if (extConfig) {
    console.log(extConfig,extConfig.storeId, extConfig.appId);
    wx.setStorageSync('extConfig', extConfig);
    wx.setStorageSync('storeId', extConfig.storeId);
    wx.setStorageSync('appId', extConfig.appId);
  }
}
/**
 * 登录
 * @date 2018-07-12
 * @param fromValue-确认用户来源:
 * (0：是自主注册， 1：是邀请注册)
 */
var loginUrl = constant.loginUrl + "/third/wxa/user/login",  //登录
    infoUrl = constant.loginUrl + "/third/wxa/user/info";    //授权   
function loginAuthorize(fromValue, successFun) {
  //获取第三方数据
  getThirdDefineData();
  var app_id = wx.getStorageSync('appId');
  var store_id = wx.getStorageSync('storeId');
  console.log('loginAuthorize--->');
  new Promise(function (resolve) {
    // 显示加载中
    wx.showToast({
      title: "loading",
      icon: 'loading'
    });
    wx.login({
      success: function (res) {
        console.log('wx.login success', res)
        resolve(res);
      },
      fail: function (res) {
        console.log(res);
      }
    });
  }).then(function (xdata) {
    console.log('xdata', xdata);
    var fromVal = fromValue ? fromValue : 0;
    return new Promise(function (resolve, reject) { //登录
      console.log(app_id, store_id, xdata.code, loginUrl);
      var sendData = {
        code: xdata.code,
        appId: app_id,
        storeId: store_id,
        from: fromVal
      };
      console.log(sendData);
      var sign = util.MD5(util.paramConcat(sendData));
      wx.request({
        url: loginUrl,
        data: sendData,
        header: {
          'version': constant.version,
          'sign': sign
        },
        success: function (res) {
          var data = res.data;
          console.log(data);
          //console.log(data, data.sessionId, data.memberInfo.id);
          if (data.errcode !== 500) {
            wx.setStorageSync("sessionId", data.sessionId);
            wx.setStorageSync("id", data.memberInfo.id);
            wx.setStorageSync("nickName", data.memberInfo.userNickname);
            var appInstance = getApp();
            appInstance.globalData.userMemberId = data.memberInfo.id;
            //console.log(appInstance.globalData.userMemberId);
            console.log("data.memberInfo.id", data.memberInfo.id);
            successFun(data.memberInfo.id);
            resolve(res);
          } else {
            reject(data.memberInfo.id);
          }
        }
      })
    });
  });
}
//只登录不授权
function loginUnauthorized(cb) {
  //获取第三方数据
  getThirdDefineData();
  new Promise(function (resolve) {
    // 显示加载中
    wx.showToast({
      title: "loading",
      icon: 'loading'
    });
    wx.login({
      success: function (res) {
        resolve(res);
      }
    });
  }).then(function (xdata) {
    //登录
    var sendData = {
      code: xdata.code,
      appId: wx.getStorageSync('appId'),
      storeId: wx.getStorageSync('storeId'),
      from:0
    };
    var sign = util.MD5(util.paramConcat(sendData));
    wx.request({
      url: loginUrl,
      header: {
        'version': constant.version,
        'sign': sign
      },
      data: sendData,
      success: function (res) {
        var data = res.data;
        if (data.errcode !== 500) {
          wx.setStorageSync("sessionId", data.sessionId);
          wx.setStorageSync("id", data.memberInfo.id);
          console.log("登录成功不授权", data);
          var member_id = data.memberInfo.id,
              sessionId = data.sessionId;
          cb(member_id, sessionId);
        }
      }
    })
  }).then(function (xdata) {
    wx.hideToast();
    cb();
  })
}