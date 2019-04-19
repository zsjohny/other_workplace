// 我的颜值分
var dialog = require("../../../utils/dialog")
var util = require("../../../utils/util")
var constant = require("../../../constant")
var app = getApp()
//上拉加载更多通用请求函数
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
  var param = {};
  console.log(that.data.hasMore);
  var store_Id = wx.getStorageSync('storeId'),
      sendData = {
        current: page,        //当前是第几页
        size: page_size,     //每页显示条数
        storeId: that.data.storeId,
        memberId: that.data.memberId
      };
  var url = app.facade.coinProductApi;
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
        var paroductArray = that.data.productData;
        var data = res.data.data;
        //有数据
        if (data.records.length > 0) {
          that.setData({
            productData: paroductArray.concat(data.records),
            moreComplete: true
          })
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
  });
  // app.common.getMoreData(url, param, function(data){
  // },function(){})
}
//初始化数据
var initCoinData = function (that, member_id){
  page = 2;
  var param = {
    data: {
      memberId: member_id,
      current: 1,        //当前是第几页
      size: 10    //每页显示条数
    },
    loading:true
  }
  //获取商品列表
  app.facade.getCoinProduct(param).then(res => {
    console.log("商品数据", res);
    var data = res.data;
    if (data.records.length > 0){
      that.setData({
        productData: data.records
      })
    }
  });
  //获取颜值分账户数据
  app.common.getCoinAccountData("",function(data){
    if (data.aliveCoins >0){
      var aliveCoinsMoney = parseInt(data.aliveCoins) /100;
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
  //获取最近的一个分享
  app.facade.getLastShareFriend().then(res => {
    var data = res.data;
    that.setData({
      getLastShareFriendData: data.successTime
    })
  });
}
Page({
  data: {
    storeId: '',            //门店id
    memberId:'',            //会员id
    coinData:[],            //数据
    aliveCoins:0,          //可用颜值分数
    allInCoins: 0,          //入账颜值分数
    allOutCoins: 0,          //出账颜值分数
    aliveCoinsMoney:0,      //颜值分等值人民币
    productData: [],          //商品列表数据
    hasMore: false,        //底部加载更多loading控制
    moreComplete: true,    //加载完成-控制是否可以上拉加载
    moreOver: false,        //没有数据
    goTopShowState: false,  //返回顶部显示状态
    shareSuccess: false     //分享成功后的状态 true-成功后不刷新页面（成功后提示文字用）
  },
  onLoad: function () {
    var that = this;
  },
  onShow: function () {
    var that = this;
    // wx.showShareMenu({
    //   withShareTicket: true
    // })
    var memberId = wx.getStorageSync('id');
    var storeId = wx.getStorageSync('storeId');
    that.setData({
      storeId: storeId,
      memberId: memberId
    })
    var isRefleshMyCoinPage = app.globalData.isRefleshProductList;
    //不刷新
    if (isRefleshMyCoinPage){
      that.setData({
        shareSuccess:true
      })
    }else{
      that.setData({
        shareSuccess: false
      })
    }
    //分享成功后,返回不刷新页面
    if (!that.data.shareSuccess) {
      initCoinData(that, memberId);
    }
  },
  onHide:function(){
    //刷新重置
    app.globalData.isRefleshProductList = false
  },
  onUnload: function () {
    app.globalData.isRefleshProductList = false
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    that.setData({
      moreComplete:true,
      moreOver:false
    })
    initCoinData(that, that.data.memberId);
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
  //返回顶部
  goTop: function (e) {
    util.gotoTop()
  },
  //页面滑动显示置顶按钮
  onPageScroll: function () {
    var that = this;
    util.onPageScroll(that)
  },
  //跳转到邀请规则
  gotoShareRule:function(){
    var url = "/pages/component/shareRule/shareRule";
    app.common.judgeNavigateTo(url)
  },
  //跳转到颜值分明细
  gotoMyCoinDetail:function(){
    var url = "/pages/component/myCoinDetail/myCoinDetail";
    app.common.judgeNavigateTo(url);
  },
  //跳转到我的邀请
  gotoMyShare: function () {
    var url = "/pages/component/myShare/myShare";
    app.common.judgeNavigateTo(url);
  }, 
  //跳转立即提现页面
  gotoWithdrawCash: function () {
    var url = "/pages/component/withdrawCash/withdrawCash";
    app.common.judgeNavigateTo(url);
  },
  //跳转商品详情
  gotoProductDetail:function(e){
    var productId = e.target.dataset.id,
       url = "/pages/component/detail/detail?productId=" + productId;
    app.common.judgeNavigateTo(url);
  },
  //定义页面可转发的函数
  onShareAppMessage: function (res) {
    var that = this;
    // 来自页面内转发按钮
    if (res.from === 'button') {
      //console.log(res.target);
      var shareProductId = res.target.dataset.id,
          shareTitle = res.target.dataset.title,
          shareImageUrl = res.target.dataset.img,
          productPrice = res.target.dataset.price,
          activityProductState = res.target.dataset.activitytype,
          storeId = wx.getStorageSync('storeId'),
          memberId = wx.getStorageSync('id');
      //console.log(shareProductId, shareTitle, shareImageUrl);
      var shareParam = '';
      //分享活动商品,无佣金和颜值分
      // if (activityProductState != 0) {
      //   shareParam = '?productId=' + shareProductId + '&shareFlag=1';
      // } else {
      //   shareParam = '?productId=' + shareProductId + '&shareFlag=1' + '&sourceUserId=' + memberId;
      // }
      shareParam = '?productId=' + shareProductId + '&shareFlag=1' + '&sourceUserId=' + memberId;
      var shareType = 2,
        activityType = '',
        param = {};
      console.log("shareParam", shareParam);
      return {
        title: shareTitle,
        path: '/pages/component/detail/detail' + shareParam,
        imageUrl: shareImageUrl,
        success: function (res) {
          console.log("转发成功", res);
          that.setData({
            shareSuccess:true
          })
          app.globalData.isRefleshProductList = true; //和打开商品详情保持同步
          //分享活动商品
          // if (activityProductState != 0) {
          //   //shareType = 1;
          //   if (activityProductState == 1) {
          //     activityType = 2
          //   } else if (activityProductState == 2) {
          //     activityType = 1
          //   }
          //   param = {
          //     storeId: that.data.storeId,
          //     memberId: memberId,
          //     type: shareType,
          //     targetId: shareProductId,
          //     activityType: activityType
          //   }
          // } else {
            
          // }
          //普通商品
          param = {
            storeId: that.data.storeId,
            memberId: memberId,
            type: shareType,
            targetId: shareProductId
          }
          //分享函数
          var coinNumber = Math.round((parseFloat(productPrice) / 10).toFixed(1));
          if (coinNumber > 10) {
            coinNumber = 10;
          } else if (isNaN(coinNumber)) {
            coinNumber = 0;
          }
          console.log("coinNumber", coinNumber);
          app.common.shareFunc(param, function (data) {
            console.log("data.isOutOfMax", data.isOutOfMax);
            //当isOutOfMax为true时，已经超过最大分享收益次数,不给颜值分和佣金
            if (!data.isOutOfMax) {
              let text = "恭喜您分享成功，获得" + coinNumber + "颜值分";
              if (coinNumber > 0) {
                dialog.toastError(text);
              }
            }
          });
        },
        fail: function (res) {
          // 转发失败
          dialog.toastError("分享失败");
        }
      }
    }
    // 右上角转发菜单
    if (res.from === 'menu') {
      console.log(res);
      return {
        success: function (res) {
          console.log("转发成功",res);
        },
        fail: function (res) {
          // 转发失败
        }
      }
    }
  }
})