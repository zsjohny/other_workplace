// 选择优惠券
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
//上拉加载更多通用请求函数
var page = 2;
var page_size = 10;
var coupon_url = constant.devUrl + "/miniapp/coupon/getAvailableMemberCouponList.json";
var getMoreList = function (that, totalExpressAndMoney) {
  var store_Id = wx.getStorageSync('storeId');
  var member_id = wx.getStorageSync('id');
  var sendData = {};
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
    sendData = {
      current: page,        //当前是第几页
      size: page_size,     //每页显示条数
      memberId: member_id,
      storeId: store_Id,
      totalExpressAndMoney: totalExpressAndMoney,
      productId: that.data.productIds
    }
    var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: coupon_url,
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      var data = res.data.data;
      //console.log(res);
      //判断是否有数据，有则取数据  
      if (res.data.data && data.records.length > 0) {
        var list = that.data.couponData;
        var historyList = that.data.couponHistoryData;
        var couponList = data.records;
        //console.log(couponList);
        for (var i = 0; i < couponList.length; i++) {
          list.push(couponList[i]);
        }
        that.setData({
          couponData: list,
          moreComplete: true
        });
       
        page++;
        //没有数据了
      } else {
        that.setData({
          hasMore: false,
          moreOver: true,
          moreComplete: true
        });
      }
    },
    complete: function () {
      //隐藏loadding
      setTimeout(function () {
        that.setData({
          hasMore: false,
        });
      }, 1000)
    }
  });
}
//初始化获取优惠券列表数据
var initUsableData = function (that, totalExpressAndMoney, productIds) {
  page = 2;
  var store_Id = wx.getStorageSync('storeId');
  var member_Id = wx.getStorageSync('id');
  var sendData = {
    current: 1,        //当前是第几页
    size: 10,     //每页显示条数
    memberId: member_Id,
    storeId: store_Id,
    totalExpressAndMoney: totalExpressAndMoney,
    productId: productIds
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  dialog.loading();
  wx.request({
    url: coupon_url, //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      //console.log("可用优惠券：", res.data.data);
      var data = res.data.data;
      var couponList = data.records;
      var seccussFlag = res.data.successful;
      if (seccussFlag && data) {
          that.setData({
            couponData: couponList,
            moreOver: false
          })
      } else {
        dialog.toast(res.data.error)
      }
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 500)
    }
  })
}
Page({
  data: {
    storeId: '',            //门店id
    couponData: [],          //可用优惠券列表数据
    isTabMore: 1,     //控制列表上拉加载
    hasMore: false,  //可用底部加载更多loading控制
    moreComplete: true, //可用加载完成
    moreOver: false,   //可用更多
    chooseState:false,  //选中优惠券
    totalExpressAndMoney:''   //优惠券金额
  },
  onLoad: function (options) {
    var that = this;
    var couponMoney = options.totalExpressAndMoney,
        productIds = options.productIds;
    that.setData({
      totalExpressAndMoney: couponMoney,
      productIds: productIds
    })
    //initUsableData(that, that.data.totalExpressAndMoney);
  },
  onShow: function () {
    var that = this,
        productIds = that.data.productIds,
        totalExpressAndMoney = that.data.totalExpressAndMoney;
    initUsableData(that, that.data.totalExpressAndMoney, productIds);
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    var isTabMore = that.data.isTabMore;
    var moreComplete = that.data.moreComplete;
    if (moreComplete) {
      getMoreList(that, that.data.totalExpressAndMoney);
    }
  },
  //不使用优惠券返回确认订单页面
  backFun: function () {
    wx.removeStorageSync("couponId");
    wx.removeStorageSync("couponName");
    wx.removeStorageSync("couponAmount");
    wx.removeStorageSync("couponType");
    wx.navigateBack({
      delta: 1
    })
  },
  //选择优惠券
  chooseCoupon:function(e){
    var couponName = e.currentTarget.dataset.name,
        couponId = e.currentTarget.dataset.id,
        couponType = e.currentTarget.dataset.type,
        discount = e.currentTarget.dataset.discount,
        money = e.currentTarget.dataset.money,
        couponAmount = '';
    //折扣券    
    if (couponType == 2){
      couponAmount = discount
    }else{
      //红包、满减券
      couponAmount = money
    }    
    console.log(couponName);
    wx.setStorageSync("couponName", couponName);
    wx.setStorageSync("couponId", couponId);
    wx.setStorageSync("couponType", couponType);
    wx.setStorageSync("couponAmount", couponAmount);
    this.setData({
      chooseState:true
    })
    //返回确认订单页面
    wx.navigateBack({
      delta: 1
    })
  }
})