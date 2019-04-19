//my.js
//获取应用实例
var app = getApp()
var util = require('../../utils/util.js')
var constant = require('../../constant')
var dialog = require("../../utils/dialog")
/**
 * 获取我的信息
 */
var getUserData = function (that, memberId, store_id){
  dialog.loading();
  console.log(memberId, store_id);
  var sendData = {};
  //是否有会员id
  sendData = {
    memberId: memberId, //会员id
    storeId: store_id
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: constant.devUrl +'/miniapp/member/myInfo.json', //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': wx.getStorageSync("sessionId"),
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      console.log("个人中心数据：",res);
      var memberInfo = res.data.data.memberInfo;
      //console.log(memberInfo.userNickname, memberInfo.userIcon);
      if (res.data.successful && memberInfo) {
        that.setData({
          nickName: memberInfo.userNickname,
          headUrl: memberInfo.userIcon,
          favoriteCount: memberInfo.myFavoriteCount
        })
        //如果是没有头像时，弹窗提示重新登录授权
        if (memberInfo.userIcon == '' || memberInfo.userIcon == null){
           loginAuthorize(that)
        }
        //存储数据
        wx.setStorageSync('id', memberInfo.id);
        wx.setStorageSync('headUrl', memberInfo.userIcon);
        wx.setStorageSync('nickName', memberInfo.userNickname);
      }else {
          dialog.toastError(res.data.error)
      }
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 1000)
    }
  })
}
/**
 * 获取相关数量的数据
 */
var getMyFavoriteCount = function (that, memberId, store_id){
  var sendData = {
    memberId: memberId, //会员id
    storeId: store_id
  }
  var sign = util.MD5(util.paramConcat(sendData));
  wx.request({
    url: constant.devUrl + '/miniapp/member/myInfo.json', //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': wx.getStorageSync("sessionId"),
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      //console.log(res);
      var memberInfo = res.data.data.memberInfo,
          orderCount = res.data.data.orderCount;
      if (res.data.successful && (memberInfo || orderCount)) {
        that.setData({
          favoriteCount: memberInfo.myFavoriteCount,
          myCouponCount: memberInfo.myUsableCouponCount,        //我的优惠券
          waitGetShopCouponTemplateCount: memberInfo.waitGetShopCouponTemplateCount,  //优惠券中心
          allCount: orderCount.allCount,
          waitTeamActivityCount: memberInfo.waitTeamActivityCount,  //我的团购
          pendingPaymentCount: orderCount.pendingPaymentCount,
          alreadyShippedCount: orderCount.newAlreadyShippedCount,  //待收货订单
          pendingDeliveryCount: orderCount.pendingDeliveryCount,
          newalreadyCompletedCount: orderCount.newalreadyCompletedCount  //已完成
        })
      }else {
        dialog.toastError(res.data.error)
      }
    }
  })
}
/**
 * 获取门店信息-个人、企业
 */
var getStoreInfo = function (that, member_id, store_id) {
  var param = {
    data: {
      storeId: store_id,
      memberId: member_id
    },
    loading: true
  }
  app.facade.getShopInfo(param).then(res => {
    if (res.data.wxaType == 1) {
      that.setData({
        wxaType: true
      })
    } else if (res.data.wxaType == 0) {
      that.setData({
        wxaType: false
      })
    }
  })
}
//登录获取用户信息
var loginGetUserData = function (that) {
  app.loginAuthorizeFun(0, function (id) {
    var id = wx.getStorageSync('id'); //获取会员id
    var store_id = wx.getStorageSync('storeId');
    that.setData({
      memberId: id
    })
    getMyFavoriteCount(that, id, store_id);
    getUserData(that, id, store_id);
    getStoreInfo(that, id, store_id);
    getMyData(that, id, store_id);
  })
}
//重新登录授权
var loginAuthorize = function (that) {
  app.loginAuthorizeFun(0, function (id) {
    that.setData({
      isShowAuthorizePopup: true  //显示授权弹窗
    })
  })
}
var msgTimer;
/**
 * 获取我的收益、粉丝、金额、团队、邀请人数据
 */
var getMyData = function (that, member_id, store_id) {
  var param = {
    loading: true
  }
  app.distributionApi.getMyInformation(param).then(res => {
    console.log("新的接口",res.data);
    var data = res.data;
    that.setData({
      grade: data.grade,
      myIncome: data.userIncomeStatistics,     //我的收益
      myTeam: data.countTeam,                  //我的团队
      myFans: data.countFollower,              //我的粉丝
      myHigher: data.higherInformation         //我的邀请人
    })
  })
}
Page({
  data: {
    nickName: '',    //昵称
    headUrl: '',     //头像
    memberId: '',     //会员id 
    favoriteCount: '', //想要的数量
    myCouponCount:'', //我的优惠券数量
    waitGetShopCouponTemplateCount:'',//领取中心数量
    wxstoreId: wx.getStorageSync('storeId'),
    wxappId: wx.getStorageSync('appId'),
    msgNumber: '',    //消息条数
    storeName:'',     //门店名字
    readFlag: false,   //是否清除未读消息
    wxaType:false,  //个人版和企业版
    isShowAuthorizePopup: false,  //是否显示授权弹窗
    showMyIncomeText:"展开",      //我的收益
    showMyIncomeState: false,     //我的收益显示控制
    showMyTeamText: "展开",      //我的团队
    showMyTeamState: false,     //我的团队显示控制
    showMyFansText: "展开",      //我的粉丝
    showMyFansState: false,     //我的粉丝显示控制
    showMyInviteState: false    //我的邀请人
  },
  onLoad: function () { 
    var that = this; 
  },
  onShow:function(){
    var that = this;
    var id = wx.getStorageSync('id');
    var sessionId = wx.getStorageSync('sessionId');
    //登录获取用户信息
    loginGetUserData(that);
    //console.log("会员id", id, "，登录sessionid", sessionId, "，门店id", wx.getStorageSync('storeId'));
    var store_id = wx.getStorageSync('storeId');
    //设置会员未读消息为已读
    var member_id = wx.getStorageSync('id');
    var readFlag = that.data.readFlag;
    if (readFlag) {
      util.setReadMsgNumber(that, store_id, member_id);
      that.setData({
        readFlag: false
      })
    }
    util.getPhoneNumber(that, store_id, member_id)
    var getMsgNumber = function () {
      util.timingGetMsgNumber(that, store_id, id);
      msgTimer = setTimeout(getMsgNumber, 3000);
    }
    getMsgNumber();
  },
  onHide: function () {
    clearTimeout(msgTimer)
  },
  onUnload:function(){
    clearTimeout(msgTimer)
  },
  //拨打电话
  makePhone: function (e) {
    var that = this;
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    util.getPhoneNumber(that, store_id, member_id)
    util.makePhone(e)
  },
  //点击设置未读消息为已读
  clearMsgNumber: function () {
    var that = this;
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    util.setReadMsgNumber(that, store_id, member_id);
    that.setData({
      msgNumber: 0,
      readFlag: true
    })
  },
  //获取用户并授权
  onGotUserInfo: function (e) {
    var that = this;
    var xdata = e.detail;
    var infoUrl = constant.loginUrl + "/third/wxa/user/infos";    //授权   
    console.log("用户信息", e);
    if (that.data.memberId) {
      var sendData = {
        rawData: xdata.rawData,
        signature: xdata.signature,
        encryptedData: xdata.encryptedData,
        iv: xdata.iv
      };
      console.log("授权原始入参", sendData)
      var sign = util.MD5(util.paramConcat(sendData));
      wx.request({
        url: infoUrl,
        data: {
          rawData: xdata.rawData,
          signature: xdata.signature,
          encryptedData: xdata.encryptedData,
          iv: xdata.iv
        },
        header: {
          'wxa-sessionid': wx.getStorageSync("sessionId"),
          'wxa-appid': wx.getStorageSync('appId'),
          'wxa-storeid': wx.getStorageSync('storeId'),
          'version': constant.version,
          'sign': sign
        },
        success: function (res) {
          console.log("授权成功后", res);
         
          that.setData({
            isShowAuthorizePopup: false
          })
          var storeId = wx.getStorageSync('storeId'),
            memberId = wx.getStorageSync('id');
          //重新获取授权后的头像等数据    
          getUserData(that, memberId, storeId);
          that.setData({
            nickName: res.data.userNickname,
            headUrl: res.data.userIcon
          })
        
        }
      })
    }
  },
  //关闭重新授权提示框
  cancelEvent: function () {
    var that = this;
    that.setData({
      isShowAuthorizePopup: false
    })
  },
  //跳转到想要页面
  gotoWant: function () {
    wx.navigateTo({
      url: '../want/want'
    })
  },
  //全部
  allOrderFunc: function () {
    wx.navigateTo({
      url: '../component/myOrder/myOrder?status=-1'
    })
  },
  //待付款
  pendingPaymentFunc: function () {
    wx.navigateTo({
      url: '../component/myOrder/myOrder?status=0'
    })
  },
  //待发货
  pendingDeliveryFunc: function () {
    wx.navigateTo({
      url: '../component/myOrder/myOrder?status=5'
    })
  },
  //待收货
  alreadyShippedFunc: function () {
    wx.navigateTo({
      url: '../component/myOrder/myOrder?status=6'
    })
  },
  //退款售后
  gotoRefundOrder: function () {
    wx.navigateTo({
      url: '../component/refundOrder/refundOrder'
    })
  },
  //跳转设置头像页面
  gotoUseredit: function () {
    var url = '../component/useredit/useredit';
    app.common.judgeNavigateTo(url)
  },
  //跳转到权益页面
  gotoLegalRight:function(){
    var url = '../component/legalRight/legalRight';
    app.common.judgeNavigateTo(url)
  },
  //跳转到我的团购
  gotoTeamBuy:function(){
    var url = '../component/myTeamBuy/myTeamBuy';
    //判断网络
    app.common.judgeNavigateTo(url)
  },
  //跳转至账户金额页面
  gotoAccountPage:function(){
    var url = '../component/accountAmount/accountAmount';
    app.common.judgeNavigateTo(url)
  },
  //跳转至账户金币页面
  gotoCoinPage: function () {
    var url = '../component/accountCoin/accountCoin';
    app.common.judgeNavigateTo(url)
  },
  //跳转至我的粉丝页面
  gotoMyFansPage: function () {
    var url = '../component/myFans/myFans';
    app.common.judgeNavigateTo(url)
  }, 
  //跳转至提现页面
  gotoWithdrawBonus: function () {
    var count = this.data.myIncome.already,
        headUrl = this.data.headUrl,
        nickName = this.data.nickName,
        url = '../component/withdrawBonus/withdrawBonus?count=' + count + "&headUrl=" + headUrl + "&nickName=" + nickName;
    app.common.judgeNavigateTo(url)
  },
  //跳转至团队订单页面
  gotoTeamOrder: function () {
    var url = '../component/teamOrderList/teamOrderList';
    app.common.judgeNavigateTo(url)
  }, 
  //显示说明
  showAmountExplain: function(){
    dialog.toastText("可用金额为您的所有已结算收入");
  },
  //显示子菜单模块函数
  showSubmenuFunc: function (stateField, textField) {
    var that = this,
        stateField = that.data.stateField;
    if (stateField) {
      that.setData({
        stateField: false,
        textField: '展开'
      })
    } else {
      that.setData({
        stateField: true,
        textField: '收起'
      })
    }
  },
  //显示我的收益信息
  showMyIncome: function () {
    var that = this,
        showMyIncomeState = that.data.showMyIncomeState;
    if (showMyIncomeState){
        that.setData({
          showMyIncomeState:false,
          showMyIncomeText:'展开'
        })
    }else{
      that.setData({
        showMyIncomeState: true,
        showMyIncomeText: '收起'
      })
    }
  },
  //显示我的团队
  showMyTeam: function () {
    var that = this,
      showMyTeamState = that.data.showMyTeamState;
    if (showMyTeamState) {
      that.setData({
        showMyTeamState: false,
        showMyTeamText: '展开'
      })
    } else {
      that.setData({
        showMyTeamState: true,
        showMyTeamText: '收起'
      })
    }
  },
  //显示我的粉丝
  gotoMyFans: function () {
    var that = this,
      showMyFansState = that.data.showMyFansState;
    if (showMyFansState) {
      that.setData({
        showMyFansState: false,
        showMyFansText: '展开'
      })
    } else {
      that.setData({
        showMyFansState: true,
        showMyFansText: '收起'
      })
    }
  },
  //显示我的邀请
  gotoMyInvite: function () {
    var that = this,
      showMyInviteState = that.data.showMyInviteState;
    if (showMyInviteState) {
      that.setData({
        showMyInviteState: false
      })
    } else {
      that.setData({
        showMyInviteState: true
      })
    }
  }
})
