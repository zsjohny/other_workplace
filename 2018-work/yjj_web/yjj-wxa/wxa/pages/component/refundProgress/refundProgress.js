// 退款进度
var app = getApp()
/**
 * 初始化数据
 */
var initData = function (that, saleId) {
  var sendData = {
    afterSaleId: saleId
  }
  var param = {};
  param.data = sendData;
  param.loading = true;
  //console.log(sendData);
  app.orderApi.getRefundProgress(param).then(res => {
    var data = res.data;
    console.log("数据", data);
    that.setData({
      stateData: data
    })
  })
}
Page({
  data: {
    storeId: '',             //门店id
    memberId: '',            //会员id
    saleId:'',               //售后单号
    stateData: {}
  },
  onLoad: function (option) {
    var that = this,
        saleId = option.saleId;
    that.setData({
      saleId: saleId
    })    
  },
  onShow: function () {
    var that = this;
    initData(that, that.data.saleId);
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    initData(that, that.data.saleId);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)  
  }
})