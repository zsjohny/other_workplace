// 退款/售后列表
var dialog = require("../../../utils/dialog"),
    util = require("../../../utils/util"),
    constant = require("../../../constant");
var app = getApp()
/**
 * 上拉加载更多通用请求函数
 */
var page = 2;
var page_size = 10;
var getMoreList = function (that) {
  //当已经显示全部时，下拉不显示loading
  console.log("moreOver", that.data.moreOver);
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
  var sendData = {
    page: page
  };
  var param = {
    data: sendData
  }
  app.distributionApi.getBillDetails(param).then(res => {
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
var initData = function (that, orderId) {
  page = 2;
  var param = {
    data:{
      orderId: orderId,
    },
    loading:true
  };
  //console.log(sendData);
  //售后列表
  app.orderApi.getMultipleOrder(param).then(res => {
    var data = res.data;
    console.log("售后列表数据", data);
    that.setData({
      listData: data.itemList
    })
  })
}

Page({
  data: {
    storeId: '',             //门店id
    memberId: '',            //会员id
    listData:'', 
    hasMore: false,       //底部加载更多loading控制
    moreComplete: true,   //加载完成
    moreOver: false,      //没有更多数据
  },
  onLoad: function (option) {
    var that = this,
        orderId = option.orderId;
    that.setData({
      orderId: orderId
    })    
  },
  onShow: function () {
    var that = this,
        memberId = wx.getStorageSync('id'),
        storeId = wx.getStorageSync('storeId');
    that.setData({
      storeId: storeId,
      memberId: memberId
    })
    initData(that, that.data.orderId);
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    that.setData({
      moreComplete: true,
      moreOver: false
    })
    //initData(that, that.data.memberId);
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
  //在次申请函数
  applyFunc: function (e) {
    var skuId = e.currentTarget.dataset.skuid,
        orderId = this.data.orderId;
    wx.navigateTo({
      url: '/pages/component/refundApply/refundApply?skuId=' + skuId + '&orderId=' + orderId
    })
  }
})