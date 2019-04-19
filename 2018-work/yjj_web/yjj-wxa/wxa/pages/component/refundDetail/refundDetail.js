// 申请售后详情
var dialog = require("../../../utils/dialog");
var app = getApp()
/**
 * 初始化数据
 */
var initData = function (that, saleId) {
  var param = {
    data:{
      afterSaleId: saleId
    },
    loading:true
  };
  //console.log(sendData);
  app.orderApi.getRefundDetail(param).then(data => {
    var data = data.data,
        photo = [],
        mationLenght = '',
        reasonLenght = '';
    console.log("数据", data);
    if (data.photo){
      photo = data.photo
    }
    if (data.mation != null || data.mation) {
      mationLenght = data.mation.length
    }
    if (data.refundReson != null || data.refundReson) {
      reasonLenght = data.refundReson.length
    }
    that.setData({
      detailData: data,
      mationLenght: mationLenght,
      reasonLenght: reasonLenght,
      refundListImg: photo
    })
    console.log("数据", that.data.detailData);
  })
}
Page({
  data: {
    storeId: '',             //门店id
    saleId: '',               //售后单号
    wholeShowState:false,        //退款状态的文字是否显示全部
    reasonWholeShowState: false, //退款原因的文字是否显示全部
    detailData: '',          //数据对象
    refundListImg: ''           //退款原因图片的数据对象
  },
  onLoad: function (option) {
    var that = this,
        saleId = option.saleId;
    that.setData({
      saleId: saleId
    })
    //获得确定框组件
    this.confirmPopup = this.selectComponent("#deletePopup");
  },
  onShow: function () {
    var that = this;
    var saleId = that.data.saleId,
        storeId = wx.getStorageSync('storeId');
    that.setData({
      storeId: storeId
    })
    initData(that, saleId);
  },
  //退款状态文字显示全部
  showWhole:function(){
    var wholeShowState = this.data.wholeShowState;
    this.setData({
      wholeShowState: !wholeShowState
    })
  },
  //退款原因文字显示全部
  showReasonWhole: function () {
    var reasonWholeShowState = this.data.reasonWholeShowState;
    this.setData({
      reasonWholeShowState: !reasonWholeShowState
    })
  },
  //图片预览
  previewImage:function(){
    wx.previewImage({
      current: '',        // 当前显示图片的http链接
      urls: []              // 需要预览的图片http链接列表
    })
  },
  //查看进度
  gotoRefundProgress:function(e){
    var saleId = e.currentTarget.dataset.saleid;
    wx.navigateTo({
      url: '/pages/component/refundProgress/refundProgress?saleId=' + saleId
    })
  },
  //删除售后单函数
  deleteOrder: function (saleid) {
    var listData = this.data.listData,
        that = this,
        param = {};
    param.data = {
      afterSaleId: saleid
    };      
    app.orderApi.deleteRefundOrder(param).then(res => {
      var data = res.data;
      that.closexbox();
      dialog.toast("删除成功");
      setTimeout(function(){
        wx.navigateBack({
          delta: 1
        })
      },1500)
    })
  },
  //确实按钮删除
  confirmDeleteFunc: function (e) {
    var saleid = this.data.detailData.refundAfterSaleId;
    console.log("saleid", saleid);
    this.deleteOrder(saleid);
  },
  //显示删除提示框
  showbox: function () {
    this.confirmPopup.showPopup();
  },
  //关闭删除提示框
  closexbox: function () {
    this.confirmPopup.hidePopup();
  },
  //再次申请函数
  applyFunc: function () {
    var orderId = this.data.detailData.orderId,
        skuId = this.data.detailData.skuId;
    wx.navigateTo({
      url: '/pages/component/refundApply/refundApply?orderId=' + orderId + "&skuId=" + skuId
    })
  }
})