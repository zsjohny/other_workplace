//搜索商品列表
var util = require('../../../utils/util.js')
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
/**
 * 上拉加载更多请求函数
 */
var page = 2;
function loadMoreList(that,memberId, keyword) {
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
    page: page,  //当前是第几页
    pageSize: page_size,  //每页显示条数
    storeId: that.data.storeId,
    keyword:keyword,
    shopCouponId: that.data.shopCouponId
  }
  var url = app.facade.couponProdcutApi;
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
      if (res.data.data.productList.length > 0) {
        var list = that.data.productData;
        var productList = res.data.data.productList;
        //console.log(res);
        for (var i = 0; i < productList.length; i++) {
          list.push(productList[i]);
        }
        that.setData({
          productData: list,
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
    }
  });
}
/**
 * 初始请求数据
 */
var initData = function (that, memberId, keyword) {
  var store_Id = wx.getStorageSync('storeId');
  dialog.loading();
  var param = {
    data:{
      memberId: that.data.memberId, //会员id
      page: 1,   //当前是第几页
      pageSize: 10,    //每页显示条数
      storeId: store_Id,
      keyword: keyword,
      shopCouponId: that.data.shopCouponId
    }
  }
  app.facade.getCouponProdcutList(param).then(res => {
    var data = res.data,
        productList = data.productList;
    //有商品    
    if (productList.length > 0){
      that.setData({
        productData: productList,
        moreOver: false,
        isHasProduct:false
      })
    }else{
      that.setData({
        isHasProduct: true
      })
    }  
  })
}
//登录获取用户信息
var loginGetData = function (that, memberId, keyword) {
  page = 2;
  //登录成功后
  app.loginAuthorizeFun(0, function (id) {
    that.setData({
      memberId: id
    })
    initData(that, id, keyword);
  })
}
Page({
  data: {
    storeId: '',   //门店id,
    memberId: '',   //会员id,
    shopCouponId:'',   //优惠券id
    productData: [],   //商品列表
    isHasProduct:false, //是否有商品
    keyword:'',     //搜索关键词
    hasMore: false,//加载更多
    moreOver: false,
    moreComplete: true, //加载完成
    goTopShowState: false, //置顶按钮显示状态
    searchBtn:true //搜索按钮搜索
  },
  onLoad: function (options) {
    var that = this,
       shopCouponId = options.shopCouponId;
       that.setData({
          shopCouponId: shopCouponId
       })
  },
  onShow: function () { 
    var that = this,
        memberId = wx.getStorageSync('id'),
        storeId = wx.getStorageSync("storeId"),
        keyword = that.data.keyword;
    that.setData({
      storeId: storeId,
      memberId: memberId
    })
    //打开商品详情时，返回不重载数据
    if (!app.globalData.isRefleshProductList) {
      loginGetData(that, memberId, keyword);
    }
  },
  onHide: function () {
    //刷新重置
    app.globalData.isRefleshProductList = false
  },
  onUnload: function () {
    app.globalData.isRefleshProductList = false
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this,
      memberId = that.data.memberId,
      keyword = that.data.keyword;
    loginGetData(that, memberId, keyword);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this,
        memberId = that.data.memberId,
        moreComplete = that.data.moreComplete,
        keyword = that.data.keyword;
    console.log(moreComplete);
    if (moreComplete) {
      loadMoreList(that, memberId, keyword)
    }
  },
  //跳转至商品详情页面
  gotoDetail: function (event) {
    var id = event.currentTarget.dataset.id;
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
  //点击按钮搜索
  searchFunc:function(e){ 
    var that = this,
        keyword = e.detail.value,
        memberId = that.data.memberId;
    if (!keyword){
        dialog.toastError("请输入搜索关键词！");
        return
    }
    //设置关键词，下拉加载更多时需要用到
    that.setData({
      keyword:keyword
    })
    loginGetData(that, memberId, keyword)
  },
  //搜索
  searchAll:function(){
    var that =this,
        keyword = that.data.keyword,
        memberId = that.data.memberId;
    if(!keyword) {
      dialog.toastError("请输入搜索关键词！");
      return
    }
    console.log("关键词", keyword, memberId);
    loginGetData(that, memberId, keyword);
  },
  //清除关键词
  clearKeyword:function(){
    var that = this,
        memberId = that.data.memberId;
    console.log(that.data.keyword);
    that.setData({
       keyword:''
    })
    initData(that, memberId, that.data.keyword);
  },
  //输入框获取值函数
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
  }
})
