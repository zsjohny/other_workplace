// 颜值分明细
var dialog = require("../../../utils/dialog")
var constant = require('../../../constant');
var util = require('../../../utils/util.js');
var app = getApp()
//上拉加载更多通用请求函数
var page = 1;
var page_size = 10;

var getMoreList = function (that, payType) {
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
  console.log(that.data.hasMore);
  var param = {};
  param.loading = true;
  var url = app.facade.coinListApi;
  console.log(url);
  var offset = parseInt(page) * parseInt(page_size);
  var sendData = {
    offset: offset,        //偏移量
    limit: page_size,     //每页显示条数
    storeId: that.data.storeId,
    memberId: that.data.memberId,
    inOut: payType
  };
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
        var coinArray = that.data.coinData;
        var data = res.data;
        console.log("data", data);
        //有数据
        if (data.data.rows.length > 0) {
          //收入
          if (payType) {
            console.log("coinArray", coinArray);
            that.setData({
              coinData: coinArray.concat(data.data.rows),
              moreComplete: true
            })
          } else {
            //支出
            that.setData({
              coinData: coinArray.concat(data.data.rows),
              moreComplete: true
            })
          }
        } else {
          //没有数据了
          that.setData({
            hasMore: false,
            moreOver: true,
            moreComplete: false
          });
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
  })
}
//初始化列表数据  payType - 1：收入，0：支出
var initCoinData = function (that, payType){
  page = 1;
  var storeId = that.data.storeId;
  var memberId = that.data.memberId;
  var sendData = {
    offset: 0,        //偏移量
    limit: 10,     //每页显示条数
    memberId: memberId, 
    storeId: storeId,
    inOut: payType
  }
  var param = {};
  param.data = sendData;
  param.loading = true;
  //console.log(sendData);
  //请求接口
  app.facade.getCoinList(param).then(res =>{
    var data = res.data;
    console.log("返回数据",data.rows);
    //收入
    if (payType){
      that.setData({
        coinData: data.rows
      })
    }else{
      //支出
      //console.log(data.rows);
      that.setData({
        coinData: data.rows
      })
    }
  })
  //获取颜值分账户数据
  app.common.getCoinAccountData("", function (data) {
    if (data.aliveCoins > 0) {
      var aliveCoinsMoney = parseInt(data.aliveCoins) / 100;
      that.setData({
        aliveCoins: data.aliveCoins,
        aliveCoinsMoney: aliveCoinsMoney
      })
    }
    if (data.allIn > 0) {
      that.setData({
        allInCoins: data.allIn
      })
    }
    if (data.allOut > 0) {
      that.setData({
        allOutCoins: data.allOut
      })
    }
  })
  //获取最近
}
Page({
  data: {
    storeId: '',             //门店id
    memberId: '',           //会员id
    coinData:[],            //列表数据
    aliveCoins: 0,          //可用颜值分数
    allInCoins: 0,          //入账颜值分数
    allOutCoins: 0,          //出账颜值分数
    incomeTabState:true,
    payTabState: false,    //控制tab选择的状态
    isTabMore: 1,         //控制列表上拉加载,1-默认是收入,0-支出
    hasMore: false,       //列表控制底部加载更多loading
    moreComplete:true,    //列表数据是否加载完成，控制上拉是否还需加在数据
    moreOver: false       //列表控制更多
  },
  onLoad: function () {
    var that = this;
    var storeId = wx.getStorageSync('storeId'),     
        memberId = wx.getStorageSync('id'); 
    that.setData({
      storeId:storeId,
      memberId: memberId
    })

  },
  onShow: function () {
    var that = this;
    that.setData({
      incomeTabState: true,
      payTabState: false,
      moreComplete: true,
      hasMore: false,
      moreOver: false,
      isTabMore: 1
    })
    initCoinData(that,1)
  },
  //tab切换
  tabFun: function (e) {
    var that = this;
    var tab = e.currentTarget.dataset.tab;
    //收入
    if (tab == "income") {
      that.setData({
        incomeTabState: true,
        payTabState: false,
        moreComplete: true,
        hasMore: false,       
        moreOver: false,      
        isTabMore: 1
      })
      initCoinData(that, 1)
    } else if (tab == "pay") {
      that.setData({
        incomeTabState: false,
        payTabState: true,
        moreComplete: true,
        hasMore: false,
        moreOver: false,  
        isTabMore: 0
      })
      initCoinData(that, 0)
    }
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    that.setData({
      moreComplete: true,
      incomeTabState: true,
      payTabState: false,
      moreOver: false,
      isTabMore:1
    })
    initCoinData(that, 1);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this,
        isTabMore = that.data.isTabMore,
        moreComplete = that.data.moreComplete;
    if (moreComplete){
      getMoreList(that, isTabMore);
    } 
  },
  //跳转到结算规则
  gotoSettlementRule:function(){
    var url = "/pages/component/settlementRule/settlementRule";
    console.log(app.common.judgeNavigateTo(url));
  }
})