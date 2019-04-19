//搜索商品列表
var util = require('../../../utils/util.js')
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
var url = constant.devUrl + '/miniapp/product/searchProductList.json';
/**
 * 上拉加载更多请求函数
 */
var pageNum = 2;
function loadMoreList(that, url, memberId, keyword) {
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
    storeId: store_Id,
    keyword:keyword
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: url,
    data: sendData,
    header: {
      'wxa-sessionid': wx.getStorageSync("sessionId"),
      'version': constant.version,
      'sign': sign
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
var initData = function (that, memberId, sessionId, keyword) {
  var store_Id = wx.getStorageSync('storeId');
  dialog.loading();
  var sendData = {};
  //是否有会员id
  if (memberId) {
    sendData = {
      memberId: memberId, //会员id
      current: 1,   //当前是第几页
      size: 10,    //每页显示条数
      storeId: store_Id,
      keyword: keyword
    }
  } else {
    sendData = {
      current: 1,   //当前是第几页
      size: 10,    //每页显示条数
      storeId: store_Id
    }
  }
  var sign = util.MD5(util.paramConcat(sendData));
  console.log("搜索列表提交的数据：", sendData);
  wx.request({
    url: url, //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': sessionId,
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("搜索返回列表的数据：", res);
      var productList = res.data.data.records;
      console.log(productList);
      if (res.data.successful) {
        //console.log(res.data.data.isMore);
        that.setData({
          contendshow: false,
          productData: productList,
          isNoProduct: res.data.data.isMore
        })
        if (res.data.data.isMore > 0) {
          that.setData({
            moreOver: false,
          })
        }
      } else {
        dialog.toast(res.data.error)
      }
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 1000)
    },
    fail:function(){

    }
  })
}
//登录获取用户信息
var loginGetUserData = function (that, keyword) {
  pageNum = 2;
  app.loginAuthorizeFun(0, function (id) {
    var sessionId = wx.getStorageSync("sessionId");
    if (id) {
      that.setData({
        memberId: id
      })
    }
    initData(that, id, sessionId, keyword)
  })
}
/**
 * 初始化关键字数据
 */
var initKeyData = function (that, memberId, sessionId) {
  var store_Id = wx.getStorageSync('storeId');
  dialog.loading();
  var sendData = {};
  sendData = {
    storeId: store_Id,
    memberId: memberId
  }
  var sign = util.MD5(util.paramConcat(sendData));
  console.log("搜索列表提交的数据：", sendData);
  wx.request({
    url: constant.devUrl + '/miniapp/product/getSearchKeywordList.json', //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': sessionId,
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("搜索返回列表的数据：", res);
      var keywordList = res.data.data;
      if (res.data.successful) {
        if (res.data.data.length == 0) {
          that.setData({
            isShow: false
          });
        } else {
          that.setData({
            keywordList: keywordList,
            isShow: true
          });
        }

      } else {
        dialog.toast(res.data.error)
        that.setData({
          isShow: false
        })
      }
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 1000)
      console.log("dd")
    },
     fail: function () {
      console.log("33")
    }
  })
}
var timer;
Page({
  data: {
    scrollTop: 0,
    productData: [],   //商品列表
    scrollHeight: 0,   //商品滚动列表高度
    hasMore: false,//加载更多
    moreOver: false,
    moreComplete: true, //加载完成
    memberId: '',   //会员id,
    isNoProduct: '',   //是否有商品
    isNoProductText: '', //提示文字
    goTopShowState: false, //置顶按钮显示状态
    msgNumber: '',    //消息条数
    readFlag: false,   //是否清除未读消息
    keyword: '',     //搜索关键词
    clearShow:false,   //是否显示清除按钮
    contendshow: true, //内容显示
    searchBtn:true, //搜索按钮搜索
    isShow:false  //搜索关键词是否显示

  },
  onLoad: function (options) {
    var that = this;
    var index = options.index;
    var member_id = wx.getStorageSync('id');
    var sessionId = wx.getStorageSync("sessionId");
    if(index == 1){
      that.setData({
        contendshow:true
      })
      initKeyData(that, member_id, sessionId)
    }else{
      that.setData({
        contendshow: false
      })
    }
    //如果是从搜索页面跳转时
    that.setData({
      keyword:options.keyword
    })
  },
  onShow: function () { //监听页面显示
    var that = this;
   
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
    //如果是从搜索页面跳转时，根据关键词或者数据
    var keyword = that.data.keyword;
    if (keyword){
      //打开商品详情时，返回不重载数据
      if (!app.globalData.isRefleshProductList) {
        loginGetUserData(that, keyword);
      }
    }
  },
  onHide: function () {
    clearInterval(timer);
    //刷新重置
    app.globalData.isRefleshProductList = false
  },
  onUnload:function(){
    clearInterval(timer);
    app.globalData.isRefleshProductList = false
  },
  // //下拉刷新
  // onPullDownRefresh: function () {
  //   var that = this;
  //   var keyword = that.data.keyword;
  //   loginGetUserData(that, keyword);
  //   setTimeout(function () {
  //     wx.stopPullDownRefresh()
  //   }, 500)
  // },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    var memberId = this.data.memberId;
    var moreComplete = that.data.moreComplete;
    var keyword = that.data.keyword;
    console.log(moreComplete);
    if (moreComplete) {
      loadMoreList(that, url, memberId, keyword)
    }
  },
  //跳转至商品详情页面
  gotoDetail: function (event) {
    var id = parseInt(event.currentTarget.dataset.id);
    //console.log(id);
    wx.navigateTo({
      url: '../detail/detail?productId=' + id
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
  },
 
  //点击按钮搜索
  searchFunc:function(e){ 
    var that = this;
    var keyword = e.detail.value;
    if (!keyword){
        dialog.toastError("请输入搜索关键词！");
        return
    }
    //设置关键词，下拉加载更多时需要用到
    that.setData({
      keyword:keyword
    })
    loginGetUserData(that, keyword)
  },
  //搜索
  searchAll:function(){
    var that =this;
    var keyword = that.data.keyword;
    if (!keyword) {
      dialog.toastError("请输入搜索关键词！");
      return
    }
    loginGetUserData(that, keyword);
  },
  //清除关键词
  clearKeyword:function(){
    var that = this;
    console.log(that.data.keyword);
    that.setData({
       keyword:''
    })
  },
  searchSeekInput:function(e){
    var that = this;
    console.log(e.detail.value);
    var inputValue = e.detail.value;
    if (inputValue != ""){
        that.setData({
          searchBtn : false,
          keyword: inputValue,     
          clearShow: true
        })
    }else{
      that.setData({
        searchBtn: true,
        keyword: inputValue,
        clearShow: false
      })
    }
  
  },
  //每一个关键词
  keywordBtn: function (e) {
    var that = this;
    var keyword = e.currentTarget.dataset.value;
    that.setData({
      keyword: keyword,
      searchBtn:false
    })
    that.searchAll();
  },
  //获取焦点时显示清除按钮
   showClearBtn:function(){
     var that = this;
     if (that.data.keyword !=""){
       this.setData({
         clearShow: true,
         searchBtn: false,
       })
     }
  },
  //失去焦点时显示清除按钮
  hideClearBtn: function () {
    var that = this;
    if (that.data.keyword == "") {
      this.setData({
        clearShow: false,
        searchBtn: true,
      })
    }
  },
  //清除关键词
  searchClear: function () {
    var that = this;
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    var sessionId = wx.getStorageSync("sessionId");
    var sendData = {
      storeId: store_id,
      memberId: member_id
    }
    var sign = util.MD5(util.paramConcat(sendData));
    console.log("清除数据：", sendData);
    wx.request({
      url: constant.devUrl + "/miniapp/product/clearSearchKeyword.json", //接口地址
      data: sendData,
      header: {
        'wxa-sessionid': sessionId,
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log("清除返回的数据：", res);

        if (res.data.successful) {

        } else {
          dialog.toast(res.data.error)
        }
      },
      complete: function () {
        initKeyData(that, member_id, sessionId)
        setTimeout(function () {
          dialog.hide();
        }, 1000)
      }, error: function () {

      }
    })
  },
  //取消
  backPrevPage: function () {
    wx.switchTab({
      url: '../../index/index',
    })
  },
})
