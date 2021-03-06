//商城所有商品
var util = require('../../utils/util.js')
var constant = require('../../constant')
var dialog = require("../../utils/dialog")
var app = getApp()
var url = constant.devUrl + '/miniapp/product/allProductList.json';
/**
 * 上拉加载更多请求函数
 */
var pageNum = 2;
function loadMoreList(that, url, memberId){
  var store_Id = wx.getStorageSync('storeId');
  var page_size = 10;
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
  var sendData = {
      memberId: memberId,     //会员id
      current: pageNum,  //当前是第几页
      size: page_size,  //每页显示条数
      storeId: store_Id
    }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: url,
    data: sendData,
    header: {
      'wxa-sessionid': wx.getStorageSync("sessionId"),
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      //判断是否有数据，有则取数据  
      if (res.data.data.records.length > 0) {
        var list = that.data.productData;
        var productList = res.data.data.records;
        //console.log(res);
        for (var i = 0; i < productList.length; i++) {
          list.push(productList[i]);
        }
        that.setData({
          productData: list,
          moreComplete: true
        });
        pageNum++;
        //没有数据了
      } else {
        that.setData({
          hasMore: false,
          moreOver: true,
          moreComplete: true
        });
      }
    }
  });
}

/**
 * 初始请求数据
 */
var initData = function (that, memberId, sessionId){
   var store_Id = wx.getStorageSync('storeId');
   dialog.loading();
   var sendData = {
       memberId: memberId, //会员id
       current: 1,   //当前是第几页
       size: 10,    //每页显示条数
       storeId: store_Id
    }
   var sign = util.MD5(util.paramConcat(sendData));
   console.log("所有商品列表提交的数据：",sendData);
    wx.request({
      url: url, //接口地址
      data: sendData,
      header: {
        'wxa-sessionid': sessionId,
        'version': constant.version,
        'sign':sign
      },
      success: function (res) {
        console.log("所有商品返回列表的数据：",res);
        var productList = res.data.data.records;
        //console.log(productList);
        if (res.data.successful) {
          //console.log(res.data.data.isNoProduct);
          that.setData({
            productData: productList,
            isNoProduct: res.data.data.isMore
            // isNoProductText: res.data.data.text
          })
          if (res.data.data.isMore > 0) {
            that.setData({
              moreOver: false,
            })
          }
        }else {
          dialog.toast(res.data.error)
        }
      },
      complete: function () {
        setTimeout(function () {
          dialog.hide();
        }, 1000)
      }
    })
 }

 /**
 * 登录获取用户信息
 */
var loginGetUserData = function (that) {
  pageNum = 2;
  app.loginAuthorizeFun(0, function (id) {
    var sessionId = wx.getStorageSync("sessionId");
    if (id) {
      that.setData({
        memberId: id
      })
    }
    initData(that, id, sessionId)
  })
}
var timer = null;
Page({
  data: {
    scrollTop: 0,
    productData: [],   //商品列表
    scrollHeight: 0,   //商品滚动列表高度
    hasMore: false,//加载更多
    moreOver: false,
    moreComplete: true, //加载完成
    memberId:'',   //会员id,
    isNoProduct: '',   //是否有商品
    isNoProductText: '', //提示文字
    goTopShowState: false, //置顶按钮显示状态
    msgNumber: '',    //消息条数
    readFlag: false   //是否清除未读消息
  },
  onLoad: function () {
    var that = this;
  },
  onShow: function () { //监听页面显示
    var that = this;
    //打开商品详情时，返回不重载数据
    if (!app.globalData.isRefleshProductList) {
      //获取会员id,初始化数据
      loginGetUserData(that);
    }
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    //设置会员未读消息为已读
    var readFlag = that.data.readFlag;
    if (readFlag) {
      util.setReadMsgNumber(that, store_id, member_id);
      that.setData({
        readFlag: false
      })
    }
    var getMsgNumber = function () {
      util.timingGetMsgNumber(that, store_id, member_id);
      timer = setTimeout(getMsgNumber, 3000);
    }
    getMsgNumber();
    //获取电话热线
    util.getPhoneNumber(that, store_id, member_id)
  },
  onHide: function () {
    clearTimeout(timer);
    //刷新重置
    app.globalData.isRefleshProductList = false
  },
  onUnload: function () {
    clearTimeout(timer);
    app.globalData.isRefleshProductList = false
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    loginGetUserData(that);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    var memberId = this.data.memberId;
    var moreComplete = that.data.moreComplete;
    if (moreComplete) {
      loadMoreList(that, url, memberId)
    }
  },
  //跳转至商品详情页面
  gotoDetail: function (event) {
    var id = parseInt(event.currentTarget.dataset.id);
    //console.log(id);
    wx.navigateTo({
      url: '../component/detail/detail?productId=' + id
    })
  },
  //返回顶部
  goTop: function (e) {
    util.gotoTop()
  },
  //页面滑动显示置顶按钮
  onPageScroll: function () {
    var that = this;
    util.onPageScroll(that)
  },
  //点击设置未读消息为已读
  clearMsgNumber: function () {
    var that = this;
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    util.setReadMsgNumber(that, store_id, member_id);
    that.setData({
      msgNumber: 0,
      readFlag: true
    })
  }
})
