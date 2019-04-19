//分销提现
var app = getApp()
var util = require("../../../utils/util"),
    constant = require('../../../constant'),
    dialog = require("../../../utils/dialog");
/**
 * 获取初始化数据
 */
var getInitData = function (that, memberId) {
  var sendData = {
    loading:true
  };
  //获取可提现金额 
  app.distributionApi.getLeftCashOut(sendData).then(res => {
    that.setData({
      aliveAccount: res.data
    })
  })
}
/**
 * 登录初始化数据
 */
var loginGetInitData = function (that) {
  //登录成功后
  app.loginAuthorizeFun('', function (id) {
    that.setData({
      memberId:id
    })
    getInitData(that, id);
  })
}
/**
 * 获取时间的周几
 */
var getDay = function(){
  var day = new Date().getDay();
  return day
}
Page({
  data: {
    memberId:'',                 //会员id
    aliveAccount: '',          //可提现金额 
    inputAccount: '',        //输入框的值
    isWithdraw: false         //可提现金额 
  },
  onLoad:function(options){
    var that = this,
        headUrl = options.headUrl,
        nickName = options.nickName,
        count = options.count;
    that.setData({
      headUrl: headUrl,
      nickName: nickName,
      aliveAccount: count
    })
  },
  onShow:function(){
    var that = this;
    //获取数据
    //loginGetInitData(that);
  },
  //跳转到提现成功页面
  gotoWithdrawSuccess: function (money, createTime, userName) {
    var param = "?money=" + money + "?createTime=" + createTime + "?userName=" + userName,
        url = "../withdrawBonusSuccess/withdrawBonusSuccess" + param;
    app.common.judgeNavigateTo(url);
  },
  //保存输入框的金额
  bindKeyInput:function(e){
    var value = e.detail.value;
    this.setData({
      inputAccount:value
    })
    console.log("value", value);
    if (!value){
      console.log();
      this.setData({
        isWithdraw: false
      })
    }else{
      this.setData({
        isWithdraw: true
      })
    }
    var dayNumber = getDay();
    console.log("dayNumber", dayNumber);
    //输入金额＜500时且时间等于周三则此功能可点击，否则不可点击
    if ((dayNumber == 3 && value < 500 && value > 0) || value >= 500){
      this.setData({
        isWithdraw:true
      })
    }else{
      this.setData({
        isWithdraw: false
      })
    }
  },
  //确认提现
  confirmWithdrawFunc:function(){
    var that = this;
    var inputAccount = parseFloat(this.data.inputAccount),
        aliveAccount = parseFloat(this.data.aliveAccount);
    if (!inputAccount) {
      dialog.toastError("请输入可提现金额");
      return
    }
    if (inputAccount > aliveAccount){
      dialog.toastError("您的可提现金额不足！");
      return
    }
    var sendData = {
      data: {
        operMoney: inputAccount
      }
    };
    console.log("sendData", sendData);
    //网络判断
    app.common.judgeNetwork(function(){
      app.distributionApi.toWithdrawCash(sendData).then(res => {
        //dialog.toastError("提现处理成功");
        var data = res.data,
            money = data.money,
            createTime = data.createTime,
            userName = data.userName;
        console.log("提现", data);    
        that.gotoWithdrawSuccess(money, createTime, userName);
      })
    })
  },
  //全部提现
  setWithdrawNumber:function(){
    this.setData({
      inputAccount: this.data.aliveAccount
    })
    var value = this.data.aliveAccount,
        dayNumber = getDay();
    //输入金额＜500时且时间等于周三则此功能可点击，否则不可点击
    if ((dayNumber == 3 && value < 500 && value > 0) || value >= 500) {
      this.setData({
        isWithdraw: true
      })
    } else {
      this.setData({
        isWithdraw: false
      })
    }
  }
})