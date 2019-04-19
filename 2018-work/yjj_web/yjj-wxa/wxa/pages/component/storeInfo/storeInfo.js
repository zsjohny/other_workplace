// 门店信息
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
//获取门店名称信息
var getStoreInfo = function (that) {
  var param = {
    data:{
      storeId: that.data.storeId,
      memberId: that.data.memberId
    },
    loading:true
  }
  app.facade.getShopInfo(param).then(res => {
    that.setData({
      storeData: res.data.businessInfo
    })
  })
}
Page({
  data: {
    storeId: '',      //门店id
    memberId:'',      //会员id
    storeData:{}      //门店信息数据
  },
  onLoad: function () {
    var that = this,
        storeId = wx.getStorageSync('storeId'),
        memberId = wx.getStorageSync('id');
    that.setData({
      memberId: memberId,
      storeId: storeId
    })
    getStoreInfo(that);
  },
  onShow: function () {
    
  },
  //拨打电话
  makePhone:function(e){
    var phone_Number = e.currentTarget.dataset.phone;
    console.log(phone_Number);
    if (wx.makePhoneCall) {
      wx.makePhoneCall({
        phoneNumber: phone_Number 
      })
    } else {
      // 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
      wx.showModal({
        title: '提示',
        content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。'
      })
    }
  }
})