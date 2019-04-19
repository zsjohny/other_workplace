//立即提现
var app = getApp()
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var store_id = wx.getStorageSync('storeId');
//获取初始化数据
var getInitData = function (that, memberId) {
  var sendData = { 
      data: {
        memberId: memberId //会员id
    }
  };
  var param = {
    data: {
      memberId: memberId //会员id
    },
    loading:true
  };
  //最小提现颜值分数量
  app.facade.getMinCashOutCoins(param).then(res =>{
    that.setData({
      minCashOutCoins:res.data
    })
  })
  //颜值分对应人民币的比例
  app.facade.getCoins2rmbRadio(sendData).then(res => {
    that.setData({
      coins2rmbRadio: res.data
    })
  })
  //剩余提现金额
  app.facade.getLeftCashOut(sendData).then(res => {
    that.setData({
      leftCashOut: res.data
    })
  })
  //查询某个用户的待入账颜值分
  app.facade.getWaitInCoins(sendData).then(res => {
    that.setData({
      waitInCoins: res.data
    })
  })
  //获取可提现颜值分数量
  app.common.getCoinAccountData("", function (data) {
    console.log("获取可提现颜值分数量",data);
    that.setData({
      aliveCoins: data.aliveCoins
    })
  })
}
//登录初始化数据
var loginGetInitData = function (that) {
  //登录成功后
  app.loginAuthorizeFun('', function (id) {
    
    that.setData({
      memberId:id
    })
    getInitData(that, id);
    
  })
}
Page({
  data: {
    memberId:'',                 //会员id
    minCashOutCoins: '',          //最小提现颜值分数量
    coins2rmbRadio: '',           //颜值分与人民币的比列
    leftCashOut: '',             //剩余可用提现金额
    waitInCoins: '',              //用户的待入账颜值分
    aliveCoins:'',             //可提现颜值分数量
    withdrawError:false,         //提现额度错误提示
    inputCoinValue: '',         //输入框的值
    equivalentMoney: '',        //等值金额（元）
    showMoneyState: false     //控制等值金额显示的状态
  },
  onLoad:function(){

  },
  onShow:function(){
    var that = this;
    //获取数据
    loginGetInitData(that);
  },
  //跳转到提现成功页面
  gotoWithdrawSuccess: function () {
    var url = "/pages/component/withdrawSuccess/withdrawSuccess";
    app.common.judgeNavigateTo(url);
  },
  //跳转至提现规则页面
  gotoWithdrawRule:function(){
    var url = "/pages/component/withdrawRule/withdrawRule";
    app.common.judgeNavigateTo(url)
  },
  //输入框输入时显示等值金额
  bindKeyInput:function(e){
    var value = e.detail.value,
        coins2rmbRadio = this.data.coins2rmbRadio,
        equivalentMoney = '';
    if (coins2rmbRadio){
      equivalentMoney = (parseInt(value) / coins2rmbRadio).toFixed(2);
    }
    //当输入框输入有值时，才显示等值金额    
    if (value){
      this.setData({
        showMoneyState: true,
        inputCoinValue: value,
        withdrawError: true,
        equivalentMoney:equivalentMoney
      })
    }else{
      this.setData({
        showMoneyState: false,
        withdrawError:false
      })
    }
    var minCashOutCoins = parseInt(this.data.minCashOutCoins);
    //小于10000时，显示错误提示
    if (value < minCashOutCoins){
      this.setData({
        withdrawError: true
      })
    }else{
      this.setData({
        withdrawError: false
      })
    }
  },
  //确认提现
  confirmWithdrawFunc:function(){
    var that = this;
    var inputCoinValue = parseInt(this.data.inputCoinValue),
        aliveCoins = parseInt(this.data.aliveCoins),
        minCashOutCoins = parseInt(this.data.minCashOutCoins);
    if (inputCoinValue < minCashOutCoins || !inputCoinValue){
      dialog.toastError("提现颜值分数量不足" + minCashOutCoins);
      return
    }
    if (inputCoinValue > aliveCoins){
      dialog.toastError("您的可提现颜值分不足！");
      return
    }
    var sendData = {
      data: {
        memberId: this.data.memberId, //会员id
        count: inputCoinValue
      }
    };
    console.log("sendData", sendData);
    //网络判断
    app.common.judgeNetwork(function(){
      app.facade.confirmWithdraw(sendData).then(res => {
        //dialog.toastError("提现处理成功");
        that.gotoWithdrawSuccess();
      })
    })
  }
})