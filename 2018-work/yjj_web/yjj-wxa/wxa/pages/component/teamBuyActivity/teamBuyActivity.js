//团购列表
var util = require('../../../utils/util.js')
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
/**
 * 上拉加载更多请求函数
 */
var page = 1,
    page_size = 10;
function loadMoreList(that, memberId){
  var store_Id = wx.getStorageSync('storeId');
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
  var offset = parseInt(page) * parseInt(page_size);
  var sendData = {
    memberId: memberId,    //会员id
    offset: offset,        //当前是第几页的偏移量,0开始(页码*每页条数)
    limit: page_size,      //每页显示条数
    storeId: store_Id
  }
  var sign = util.MD5(util.paramConcat(sendData)),
      url = app.facade.teamBuyApi;
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
      if (res.data.data.rows.length > 0) {
        var dataList = that.data.activityData;
        var activityList = res.data.data.rows;
        //console.log(res);
        for (var i = 0; i < activityList.length; i++) {
          dataList.push(activityList[i]);
        }
        that.setData({
          activityData: dataList,
          moreComplete: true
        });
        var dataList = dataList,
          i = '',
          d = 0;
        //用来判断是否运行倒计时
        var runArray = [];
        for (var k = 0; k < dataList.length; k++) {
          if (dataList[k].activityState == 1 || dataList[k].activityState == 2) {
            runArray.push(dataList[k].activityState)
          }
        }
        console.log("runArray", runArray);
        //时间倒计时
        var timeDown = function () {
          downTimeFunc(that, d, dataList)
          //activetyTimer = setTimeout(timeDown, 1000)
        }
        if (runArray.length > 0) {
          timeDown();
        }
        //团购进度条计算
        var activityData = that.data.activityData,
          newActivityData = calculateProgressWidth(activityData);
        that.setData({
          activityData: newActivityData
        })
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
 * 登录获取数据
 */
var loginGetData = function(that){
  page = 1;
  //登录成功后
  app.loginAuthorizeFun(0, function (id) {
    that.setData({
      memberId:id
    })
    initData(that,id)
  })
}
/**
 * 倒计时格式化显示函数
 */
var activetyTimer = null;
function timeDownFormat(that, msec){
  var msec = msec,
      timeFormatObj = null;    
  //console.log("msec", msec);  
  if (parseInt(msec) > 0) {
    timeFormatObj = util.msecFormat(msec);
    //console.log("倒计时", util.msecFormat(msec));  
  }
  //console.log("timeFormatObj", timeFormatObj);
  return timeFormatObj
}
/**
 * 倒计时函数
 */
function downTimeFunc(that, d, data) {
  var dataList = data;
  var l = dataList.length;
  for (var i = 0; i < l; i++) {
    //待开始
    var msec = '';
    if (dataList[i].activityState == 1 || dataList[i].activityState == 2) {
      msec = dataList[i].countDown;
    }
    dataList[i].timeFormat = timeDownFormat(that, msec)
  }
  that.setData({
    activityData: dataList
  })
  //团购进度条计算
  var newActivityData = calculateProgressWidth(dataList);
  that.setData({
    activityData: newActivityData
  })
  console.log("处理后列表数据", dataList);
}
/**
 * 团购进度条计算函数
 */
function calculateProgressWidth(activityData){
  var width = '';
  for (var i = 0; i < activityData.length; i++) {
    //即将开始和进行中的时候计算
    if (activityData[i].activityState == 1 || activityData[i].activityState == 2) {
      //件数参团
      if (activityData[i].conditionType == 2) {
        var meetProductCount = parseInt(activityData[i].meetProductCount),
            orderedProductCount = parseInt(activityData[i].orderedProductCount);
        if (orderedProductCount > meetProductCount) {
          orderedProductCount = meetProductCount;
        }
        width = (orderedProductCount / meetProductCount) * 702;
        activityData[i].activityProgressWidth = width;
      } else {
        //人数成团-3.7.7和之前的版本是人数成团
        var userCount = parseInt(activityData[i].userCount),
            activityMemberCount = parseInt(activityData[i].activityMemberCount);
        if (activityMemberCount > userCount) {
          activityMemberCount = userCount;
        }
        width = (activityMemberCount / userCount) * 702;
        activityData[i].activityProgressWidth = width;
      }
    }
  }
  return activityData
}
/**
 * 初始请求数据
 */
var initData = function (that, memberId){
   var store_Id = wx.getStorageSync('storeId');
   dialog.loading();
  var param = {
    data:{
      memberId: memberId, //会员id
      offset: 0,        //当前是第几页的偏移量,0开始(页码*每页条数)
      limit: 10,       //每页显示条数
      storeId: store_Id
    },
    loading:true
  }
  app.facade.getTeamBuyList(param).then(res =>{
     console.log("返回的数据：", res.data);
    var data = res.data;
    that.setData({
      activityData: data.rows
    })
    //console.log(data.rows);
    var dataList = data.rows,
        i = '',
        d = 0;
    //用来判断是否运行倒计时
    var runArray = [];
    for (var k = 0; k < dataList.length; k++) {
      if (dataList[k].activityState == 1 || dataList[k].activityState == 2) {
        runArray.push(dataList[k].activityState)
      }
    }    
    console.log("runArray", runArray);
    //时间倒计时;
    var timeDown = function(){
      downTimeFunc(that, d, dataList)
      //activetyTimer = setTimeout(timeDown, 1000)
    } 
    if (runArray.length > 0) {
      timeDown();
    }  
    console.log("处理后列表数据",that.data.activityData);
  })
 }
Page({
  data: {
    scrollTop: 0,
    activityData: [],        //活动列表数据
    hasMore: false,        //加载更多
    moreOver: false,
    moreComplete: true,    //加载完成
    memberId:'',           //会员id,
    isNoProductText: '',   //提示文字
    goTopShowState: false, //置顶按钮显示状态
  },
  onLoad: function () {
    var that = this;
  },
  onShow: function () { //监听页面显示
    var that = this;
    //获取会员id,初始化数据
    loginGetData(that);
  },
  onHide: function () {
    //activetyTimer = null;
    
  },
  onUnload: function () {
    
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    
    that.setData({
      activityData: []
    })
    loginGetData(that);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this,
        memberId = this.data.memberId,
        moreComplete = that.data.moreComplete;
    if (moreComplete) {
      loadMoreList(that,memberId)
    }
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
  //团购跳转至商品详情
  gotoActivityDetail: function (e) {
    var param = "",
      productId = e.target.dataset.productid,
      activityId = e.target.dataset.activityid;
    param = "?productId=" + productId + "&activityId=1" + "&targetType=" + targetType;
    wx.navigateTo({
      url: '../component/detail/detail' + param
    })
  },
  //定义页面可转发的函数-团购邀请好友
  onShareAppMessage: function (res) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
      var shareShopProductId = res.target.dataset.productid,
          shareTitle = res.target.dataset.name,
          shareImageUrl = res.target.dataset.img,
          activityId = res.target.dataset.activityid;
      var shareParam = '?productId=' + shareShopProductId + '&shareFlag=1' + "&activityId=" + activityId + "&targetType=1";
      //console.log(shareTitle,shareImageUrl);
      return {
        title: shareTitle,
        path: '/pages/component/detail/detail' + shareParam,
        imageUrl: shareImageUrl,
        success: function (res) {

        },
        fail: function (res) {
          // 转发失败
        }
      }
    }
    if (res.from === 'menu') {
      // 右上角转发菜单
      console.log(res.target)
    }
  }
})
