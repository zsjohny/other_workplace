//物流详情
var app = getApp()
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var expressUrl = constant.devUrl + "/express/expressInfo.json";   //物流
var orderUrl = constant.devUrl + "/mobile/memberOrder/getStatusAndImg.json"; //订单
//初始化物流数据
function initExpressData(that, orderId){
  var store_id = wx.getStorageSync('storeId'),
      member_id = wx.getStorageSync('id');
  var sendData = {
    storeId: store_id,
    memberId: member_id,
    shopMemberOrderId: orderId
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  dialog.loading();
  wx.request({
    url: expressUrl, 
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      //console.log("物流详情：", res);
      var data = res.data.data;
      if (res.data.successful) {
         that.setData({
           expressData: data
         })
      } 
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 500)
    }
  })
}
//初始化订单数据
function initOrderData(that, orderId) {
  var store_id = wx.getStorageSync('storeId'),
      member_id = wx.getStorageSync('id');
  var sendData = {
    storeId: store_id,
    memberId: member_id,
    shopMemberOrderId: orderId
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  //dialog.loading();
  wx.request({
    url: orderUrl, 
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      console.log("订单数据：", res);
      var data = res.data.data;
      if (res.data.successful) {
        that.setData({
          orderData: data
        })
      }
    }
  })
}
Page({
  data: {
    orderId:'',        //订单id
    expressList:'',    //物流内容
    orderData: ''    //订单数据
  },
  onLoad: function (options){
    var that = this;
    var orderId = options.orderId;
    console.log("订单id", orderId);
    that.setData({ 
      orderId: orderId
    })
    //initExpressData(that, orderId);
    //initOrderData(that, orderId);
  },
  onShow:function(){
    var that = this;
    initOrderData(that, that.data.orderId);
    initExpressData(that, that.data.orderId);
  },
  //返回订单详细页面
  backOrderDetail:function(){
    wx.navigateBack({
      delta:1
    })
  },
  //下拉刷新
  onPullDownRefresh: function () {
    //获取数据
    initOrderData(that, that.data.orderId);
    initExpressData(that, that.data.orderId);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  }
})