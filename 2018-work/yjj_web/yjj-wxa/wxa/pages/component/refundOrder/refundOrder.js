// 退款/售后列表
var dialog = require("../../../utils/dialog");
var app = getApp()
/**
 * 上拉加载更多通用请求函数
 */
var page = 2;
var page_size = 10;
var getMoreList = function (that) {
  //当已经显示全部时，下拉不显示loading
  if (that.data.moreOver) {
    that.setData({
      hasMore: false,
      moreComplete: true
    })
  } else {
    that.setData({
      hasMore: true,
      moreComplete: false
    })
  }
  var param = {
    data: {
      pageSize: 10,          //条数 
      pageNumber: page,        //页码
      storeId: that.data.storeId
    }
  }
  app.orderApi.getRefundList(param).then(res => {
    var dataArray = that.data.listData,
      data = res.data;
    //有数据
    if (data.length > 0) {
      that.setData({
        listData: dataArray.concat(data),
        moreComplete: true
      })
    } else {
      //没有数据了
      that.setData({
        hasMore: false,
        moreOver: true,
        moreComplete: false
      });
      console.log("moreOver", that.data.moreOver);
    }
    page++;
  })
}
/**
 * 初始化列表数据
 */
var initData = function (that, memberId) {
  page = 2;
  var sendData = {
    pageSize: 10,          //条数 
    pageNumber: 1,        //页码
    storeId: that.data.storeId
  }
  var param = {};
  param.data = sendData;
  param.loading = true;
  //console.log(sendData);
  //售后列表
  app.orderApi.getRefundList(param).then(res => {
    var data = res.data;
    console.log("售后列表数据", data);
    that.setData({
      listData: data.list
    })
  })
}

Page({
  data: {
    storeId: '',             //门店id
    memberId: '',            //会员id
    listData: [],          //售后商品列表数据, 
    hasMore: false,       //底部加载更多loading控制
    moreComplete: true,   //加载完成
    moreOver: false,      //没有更多数据
    deleteIndex:'',       //删除的订单index
    deleteSaleId: ''      //删除的售后单号
  },
  onLoad: function () {
    var that = this;
    //获得确定框组件
    this.confirmPopup = this.selectComponent("#deletePopup");
  },
  onShow: function () {
    var that = this;
    var memberId = wx.getStorageSync('id');
    var storeId = wx.getStorageSync('storeId');
    that.setData({
      storeId: storeId,
      memberId: memberId
    })
    initData(that, memberId);
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    that.setData({
      moreComplete: true,
      moreOver: false
    })
    initData(that, that.data.memberId);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)  
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    if (this.data.moreComplete) {
      getMoreList(that);
    }
  },
  //确实删除函数
  confirmDeleteFunc: function () {
    var that = this,
        listData = that.data.listData,
        deleteIndex = that.data.deleteIndex,
        deleteSaleId = that.data.deleteSaleId;
    console.log("vdeleteIndex", deleteIndex, deleteSaleId);
    var param = {
      data: {
        afterSaleId: deleteSaleId
      }
    }
    app.orderApi.deleteRefundOrder(param).then(res => {
      this.confirmPopup.hidePopup();
      dialog.toast("删除成功");
      listData.splice(deleteIndex, 1);
      console.log("删除成功后新的listData", listData);
      that.setData({
        listData: listData
      })
    })
  },
  //关闭删除提示框
  showxbox: function (e) {
    var index = e.currentTarget.dataset.index,
        saleid = e.currentTarget.dataset.saleid;
    this.setData({
      deleteIndex: index,
      deleteSaleId: saleid
    })    
    this.confirmPopup.showPopup();
  },
  //关闭删除提示框
  closexbox: function () {
    this.confirmPopup.hidePopup();
  },
  //在次申请函数
  applyFunc: function (e) {
    var skuId = e.currentTarget.dataset.skuid,
        orderId = e.currentTarget.dataset.orderid;
    wx.navigateTo({
      url: '/pages/component/refundApply/refundApply?skuId=' + skuId + "&orderId=" + orderId
    })
  },
  //查看详情函数
  gotoRefundDetil: function (e) {
    var saleId = e.currentTarget.dataset.saleid;
    wx.navigateTo({
      url: '/pages/component/refundDetail/refundDetail?saleId=' + saleId
    })
  }
})