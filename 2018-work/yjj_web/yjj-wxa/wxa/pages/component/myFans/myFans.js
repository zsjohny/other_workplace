// 我的粉丝
var dialog = require("../../../utils/dialog"),
    util = require("../../../utils/util"),
    constant = require("../../../constant");
var app = getApp()
/**
 * 上拉加载更多通用请求函数
 */
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
  var sendData = {
      userId: that.data.memberId,
      page: page       //页码
    };
  var url = app.distributionApi.getMyFollowerDetails;
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
        var accountDataArray = that.data.listData;
        var data = res.data.data;
        //有数据
        if (data.length > 0) {
          that.setData({
            listData: accountDataArray.concat(data),
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
/**
 * 初始化数据
 */
var initData = function (that, memberId){
  page = 2;
  var sendData = {
    page: 1        //页码
  }
  var param = {};
  param.data = sendData;
  param.loading = true;
  //粉丝数据
  app.distributionApi.getMyFollowerData().then(res => {
    var data = res.data;
    console.log("数据", data);
    that.setData({
      fansData: data
    })
  })
  //粉丝明细列表
  app.distributionApi.getMyFollowerDetails(param).then(res => {
    var data = res.data;
    console.log("收支列表数据", data);
    that.setData({
      listData: data
    })
  })
}
Page({
  data: {
    storeId: '',            //门店id
    memberId: '',            //会员id
    fansData:'',           //粉丝数据
    listData:[],          //列表数据
    hasMore: false,       //底部加载更多loading控制
    moreComplete: true,   //加载完成
    moreOver: false      //没有更多数据
  },
  onLoad: function () {
    var that = this;
  },
  onShow: function () {
    var that = this,
        memberId = wx.getStorageSync('id');
    that.setData({
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
      getMoreList(that);
    }
  }
})