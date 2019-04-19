//收支详情
var dialog = require("../../../utils/dialog")
var app = getApp()
/**
 * 获取初始化数据
 */
var getInitData = function (that, memberId) {
  var param = {
    data:{
      id: that.data.id
    },
    loading:true
  };
  app.distributionApi.getAccountDetails(param).then(res => {
    that.setData({
      detailData: res.data,
      skuVoList: res.data.orderItemSkuVoList
    })
  })
}
Page({
  data: {
    memberId: ''    //会员id
  },
  onLoad:function(options){
    var that = this,
        memberId = wx.getStorageSync('id');
    console.log("id,flag",options.id, options.flag);    
    that.setData({
      memberId: memberId,
      id: options.id,
      inOutType: options.inOutType,
      flag: options.flag
    })
  },
  onShow:function(){
    var that = this;
    getInitData(that, that.data.memberId);
  },
  //复制订单号
  clipboardOrderNo:function(e){
    var orderNo = e.currentTarget.dataset.no;
    wx.setClipboardData({
      data: orderNo,
      success:function(){
        dialog.toastText("复制成功！")
      }
    })
  }
})