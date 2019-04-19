// 我的邀请
var dialog = require("../../../utils/dialog")
var util = require("../../../utils/util")
var constant = require("../../../constant")
var app = getApp()
//上拉加载更多通用请求函数
var page = 1;
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
  var param = {};
  console.log(that.data.hasMore);
  var store_Id = wx.getStorageSync('storeId'),
      offset = parseInt(page) * parseInt(page_size);
  var sendData = {
      offset: offset,        //偏移量
      limit: page_size,     //每页显示条数
      storeId: that.data.storeId,
      memberId: that.data.memberId
    };
  var url = app.facade.shareFriendApi;
  var sign = util.MD5(util.paramConcat(sendData));
  wx.request({
    url: url,
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign,
      'Content-type': 'application/x-www-form-urlencoded;charset=utf-8'
    },
    success: function (res) {
      if (res.data.successful) {
        var shareDataArray = that.data.shareData;
        var data = res.data.data;
        //有数据
        if (data.rows.length > 0) {
          that.setData({
            shareData: shareDataArray.concat(data.rows),
            moreComplete: true
          })
        } else {
          //没有数据了
          that.setData({
            hasMore: false,
            moreOver: true,
            moreComplete: false
          });
          console.log("moreOver",that.data.moreOver);
        }
        page++;
      } else {
        dialog.toastError(res.data.error)
      }
    },
    complete: function (res) {
      that.setData({
        hasMore: false
      });
    }
  });
}
//初始化列表数据
var initData = function (that, memberId){
  page = 1;
  var sendData = {
    memberId:memberId,
    offset: 0,        //偏移量
    limit: 10     //每页显示条数
  }
  var param = {};
  param.data = sendData;
  param.loading = true;
  param.defaultPage = true;
  //console.log(sendData);
  //邀请者列表
  app.facade.getShareFriendList(param).then(res => {
    var data = res.data;
    console.log("邀请者列表数据", data);
    that.setData({
      shareData: data.rows
    })
    //总邀请者数
    if (data.total){
      that.setData({
        shareUserTotal: data.total
      })
    }
  })
}
Page({
  data: {
    storeId: '',            //门店id
    memberId: '',            //会员id
    shareData:[],          //邀请列表数据
    shareUserTotal: 0,    //邀请者总数
    hasMore: false,       //底部加载更多loading控制
    moreComplete: true,   //加载完成
    moreOver: false      //没有更多数据
  },
  onLoad: function () {
    var that = this;
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
    //console.log("上拉加载moreComplete", this.data.moreComplete);
    if (this.data.moreComplete) {
      //console.log("上拉加载实行");
      getMoreList(that);
    }
  }
})