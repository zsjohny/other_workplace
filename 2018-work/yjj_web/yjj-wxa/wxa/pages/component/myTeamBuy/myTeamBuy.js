// 我的团购
var dialog = require("../../../utils/dialog")
var constant = require('../../../constant');
var util = require('../../../utils/util.js');
var app = getApp()
/**
 * 上拉加载更多请求函数
 */
var page = 1;
var page_size = 10;
var getMoreList = function (that, teamBuyType) {
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
  var url = '';
  if (teamBuyType == "1"){
    url = app.facade.teamBuyNotStartApi;
  }else{
    url = app.facade.teamBuySuccessApi;
  }
  console.log(url);
  var offset = parseInt(page) * parseInt(page_size);
  var sendData = {
    offset: offset,        //偏移量
    limit: page_size,     //每页显示条数
    storeId: that.data.storeId,
    memberId: that.data.memberId,
    inOut: teamBuyType
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
        var activityArray = that.data.activityData;
        var data = res.data;
        console.log("data", data);
        //有数据
        if (data.data.rows.length > 0) {
          //即将成团
          if (teamBuyType) {
            console.log("activityArray", activityArray);
            that.setData({
              activityData: activityArray.concat(data.data.rows),
              moreComplete: true
            })
          } else {
            //团购成功
            that.setData({
              activityData: activityArray.concat(data.data.rows),
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
/**
 * 倒计时格式化函数
 */
var teamTimer = null;
function timeDownFormat(that, msec) {
  var msec = msec,
    timeFormatObj = null;
  //console.log("msec", msec);  
  if (parseInt(msec) > 0) {
    timeFormatObj = util.msecFormat(msec);
    console.log("倒计时", util.msecFormat(msec));  
  } else {
    clearTimeout(teamTimer);
    //刷新数据
    //loginGetData(that);
  }
  return timeFormatObj.hours + "小时" + timeFormatObj.minute + "分钟" + timeFormatObj.second + "秒"
}
/**
 * 倒计时函数
 */
function downTimeFunc(that, d, dataList) {
  d++;
  var l = dataList.length,
      dataList = dataList;
  for (var i = 0; i < l; i++) {
    //待开始
    var msec = '';
    if (dataList[i].orderStatus == 0){
      msec = dataList[i].countDown - d * 1000;
      dataList[i].countDown = msec;
    }
    //console.log("activityTimeDown(that, msec)", activityTimeDown(that, msec));
    dataList[i].timeFormat = timeDownFormat(that, msec)
  }
  that.setData({
    activityData: dataList
  })
  console.log("处理后列表数据", dataList);
}
/**
 * 初始化列表数据
 * @param  teamBuyType - 1：即将成团，0：团购成功
 */
var initActivityData = function (that, teamBuyType) {
  page = 2;
  dialog.loading();
  var storeId = that.data.storeId;
  var memberId = that.data.memberId;
  var sendData = {
    offset: 0,        //偏移量 = page *limit
    limit: 10,       //每页显示条数
    memberId: memberId,
    storeId: storeId
  };
  //即将成团
  var url = '';
  if (teamBuyType == 1) {
    url = app.facade.teamBuyNotStartApi;
  } else {
    url = app.facade.teamBuySuccessApi;
  }
  //console.log(sendData);
  var sign = util.MD5(util.paramConcat(sendData));
  console.log("列表提交的数据：", sendData);
  wx.request({
    url: url,   //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("返回列表的数据：", res);
      if (res.data.successful) {
        var data = res.data.data,
            extData = res.data.extData;
        that.setData({
          noticeText:extData.tip
        })    
        //即将成团
        var url = '';
        if (teamBuyType == 1) {
          that.setData({
            activityData: data.rows
          })
        } else {
          that.setData({
            activityData: data.rows,
            phoneNumber: res.data.extData.phone
          })
        }
        var dataList = data.rows,
          i = '',
          l = dataList.length,
          d = 0;
        //用来判断是否运行倒计时
        var runArray = [];
        for (var k = 0; k < dataList.length; k++) {
          if (dataList[k].orderStatus == 0) {
            runArray.push(dataList[k].orderStatus)
          }
        }
        console.log("runArray", runArray);
        //倒计时函数
        var timeDown = function () {
          downTimeFunc(that, d, dataList);
          teamTimer = setTimeout(timeDown, 1000)
        }
        if (runArray.length > 0) {
          timeDown();
        }
        console.log("处理后列表的数据1：", that.data.activityData);
      } else {
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
 * 调用微信支付函数
 */
var paymentFunc = function (that, member_id, store_id, order_id) {
  var param = {
    data: {
      memberId: member_id, 
      storeId: store_id,
      orderId: order_id
    }
  }
  app.facade.wxaPay(param).then(res => {
    console.log("付款返回数据", res.data);
    const payData = res.data;
    wx.requestPayment({
      'appId': payData.appId,
      'timeStamp': payData.timeStamp,
      'nonceStr': payData.nonceStr,
      'package': payData.package,
      'signType': payData.signType,
      'paySign': payData.paySign,
      'success': function (res) {
        clearInterval(teamTimer);
        //console.log('success',res);
        //支付成功后刷新数据
        initActivityData(that, 1);
      },
      'fail': function (res) {
        clearInterval(teamTimer);
        //console.log('fail',res);
        //支付失败后刷新数据
        initActivityData(that, 1);
      },
      'complete': function (res) {
        // console.log('complete',res); 
      }
    });
  })
}

/**
 * 定时获取提货状态
 */
var timingGetOrderState = function (that, order_id) {
  var param = {
    data: {
      memberId: that.data.memberId, //会员id
      storeId: that.data.storeId,
      orderStatus: 4,
      orderId: order_id
    }
  }
  app.facade.getOrderState(param).then(res => {
    var data = res.data;
    console.log(data);
    if (data.data.flag) {
      //更新订单数据
      clearInterval(getTimer);
      clearInterval(codeTimer);
      that.setData({
        boxShow: false
      })
    }
  })
}
var getTimer = null;  //提货定时器
var codeTimer = null; //二维码失效定时器
Page({
  data: {
    storeId: '',             //门店id
    memberId: '',           //会员id
    activityData: [],       //列表数据
    tabState1: true,
    tabState2: false,     //控制tab选择的状态
    teamBuyType: 1,         //控制列表类型,1-默认是即将成团,0-团购成功
    hasMore: false,       //列表控制底部加载更多loading
    confirmReceipt: false,  //确认收货弹窗显示
    moreComplete: true,    //列表数据是否加载完成，控制上拉是否还需加在数据
    moreOver: false,       //列表控制更多
    payBtnState: true,     //付款按钮是否变灰不可点
    boxShow: false,      // 二维码弹窗
    codeImgUrl: '',      //提货二维码
    codePrompt: true,      //二维码失效前提示文字
    invalidPrompt: false, //二维码失效后提示文字
    codeImgState: true,   //二维码显示
    refleshBtn: false,    //点击刷新按钮
    codeOrderId:'',     //查看当前二维码的订单id
    takeOrderId: '',    //确认收货的订单id
    phoneNumber:'',     //门店手机号
    noticeText: ""      //提示文字
  },
  onLoad: function () {
    var that = this;
    var storeId = wx.getStorageSync('storeId'),
        memberId = wx.getStorageSync('id');
    that.setData({
      storeId: storeId,
      memberId: memberId
    })
  },
  onShow: function () {
    var that = this;
    that.setData({
      tabState1: true,
      tabState2: false,
      moreComplete: true,
      hasMore: false,
      moreOver: false,
      teamBuyType: 1
    })
    initActivityData(that, 1);
  },
  onHide: function () {
    //timer = null;
    clearTimeout(teamTimer);
    clearInterval(getTimer);
    clearInterval(codeTimer);
  },
  onUnload: function () {
    clearTimeout(teamTimer);
    clearInterval(getTimer);
    clearInterval(codeTimer);
  },
  //tab切换
  tabFun: function (e) {
    var that = this;
    var tab = e.currentTarget.dataset.tab;
    clearTimeout(teamTimer);
    that.setData({
      moreComplete: true,
      hasMore: false,
      moreOver: false,
    })
    //即将成团
    if (tab == "1") {
      that.setData({
        tabState1: true,
        tabState2: false,
        teamBuyType: 1
      })
      initActivityData(that, 1)
    } else if (tab == "2") {
      that.setData({
        tabState1: false,
        tabState2: true,
        teamBuyType: 0
      })
      initActivityData(that, 0)
    }
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    that.setData({
      moreComplete: true,
      tabState1: true,
      tabState2: false,
      moreOver: false,
      teamBuyType: 1
    })
    clearTimeout(teamTimer);
    initActivityData(that, 1);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this,
      teamBuyType = that.data.teamBuyType,
      moreComplete = that.data.moreComplete;
    if (moreComplete) {
      //clearTimeout(teamTimer);
      getMoreList(that, teamBuyType);
    }
  },
  //跳转到商品详情页
  gotoProductDetail: function (e) {
    var product_id = e.currentTarget.dataset.productid;
    wx.navigateTo({
      url: '../detail/detail?productId=' + product_id
    })
  },
  //查看物流详情
  openExpressDetail: function (e) {
    var that = this;
    var orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '../express/express?orderId=' + orderId
    })
  },
  //显示确认收货弹窗
  showReceiptPopup: function (e) {
    var orderId = e.currentTarget.dataset.id;
    this.setData({
      confirmReceipt: true,
      takeOrderId: orderId
    })
  },
  //关闭确认收货弹窗
  closeReceiptXbox: function () {
    this.setData({
      confirmReceipt: false
    })
  },
  //确认收货
  checkReceipt: function (e) {
    var that = this;
    var member_id = that.data.memberId,
        store_id = that.data.storeId,
        order_id = that.data.takeOrderId;
    var param = {
      data:{
        memberId: member_id,
        storeId: store_id,
        shopMemberOrderId: order_id
      }
    }
    app.facade.confirmReceipt(param).then(res => {
      that.setData({
        confirmReceipt: false,
        moreComplete: true,
        tabState1: true,
        tabState2: false,
        moreOver: false
      })
      setTimeout(function () {
        //重新初始化列表数据
        initActivityData(that, 1);
      }, 2000)
    })
  },
  //付款
  confirmPay: function (e) {
    var that = this;
    that.setData({
      payBtnState: false
    })
    setTimeout(function () {
      that.setData({
        payBtnState: true
      })
    }, 3000)
    var member_id = that.data.memberId,
        store_id = that.data.storeId,
        order_id = e.currentTarget.dataset.id;
    //支付函数
    paymentFunc(that, member_id, store_id, order_id);
  },
  //定义页面可转发的函数-团购邀请好友
  onShareAppMessage: function (res) {
    var shareProductId = res.target.dataset.id,
        shareTitle = res.target.dataset.title,
        activityId = res.target.dataset.activityid
        shareImageUrl = res.target.dataset.img;
    var param = "?productId=" + shareProductId + '&shareFlag=1' + "&activityId=" + activityId + "&targetType=1";     
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
      return {
        title: shareTitle,
        path: '/pages/component/detail/detail' + param,
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
  },
  //拨打电话
  makePhone: function (e) {
    console.log(0);
    util.makePhone(e);
  },  
  //三分钟二维码失效
  codeImgInvalid: function () {
    var that = this;
    codeTimer = setTimeout(function () {
      //显示失效提示文字
      that.setData({
        invalidPrompt: true,
        refleshBtn: true,
        codeImgState: false,
        codePrompt: false
      })
    }, 183000)
  },
  //点击刷新提货二维码
  refleshCode: function (e) {
    var that = this,
        store_id = that.data.storeId,
        member_id = that.data.memberId,
        order_id = that.data.codeOrderId,
        rand = Math.random();
    var codeImgUrl = app.facade.getcodeImgApi + "?memberId=" + member_id + "&storeId=" + store_id + "&type=dingdan" + "&id=" + order_id + "&width=400&height=400&rand=" + rand;
    //刷新重新加载二维码
    that.setData({
      codeImgUrl: codeImgUrl
    })
    //隐藏提示文字
    that.setData({
      invalidPrompt: false,
      codeImgState: true,
      codeImgUrl: codeImgUrl,
      codePrompt: true,
      refleshBtn: false
    })
    that.codeImgInvalid();
  },
  //显示提货二维码弹窗
  showCodeImgBox: function (e) {
    var that = this,
        store_id = that.data.storeId,
        member_id = that.data.memberId,
        order_id = e.target.dataset.id,
        rand = Math.random();
    var codeImgUrl = app.facade.getcodeImgApi + "?memberId=" + member_id + "&storeId=" + store_id + "&type=dingdan" + "&id=" + order_id + "&width=400&height=400&rand=" + rand;
    //console.log(codeImgUrl);
    that.setData({
      boxShow: true,
      codePrompt: true,
      codeImgState: true,
      invalidPrompt: false,
      refleshBtn: false,
      codeImgUrl: codeImgUrl,
      codeOrderId: order_id
    })
    //二维码失效
    that.codeImgInvalid();
    //定时获取提货状态
    var timingGetState = function () {
      timingGetOrderState(that, order_id);
      getTimer = setTimeout(timingGetState, 1000);
    }
    timingGetState()
  },
  //关闭二维码弹窗
  closeCodeImgBox: function () {
    this.setData({
      boxShow: false
    })
    clearInterval(codeTimer);
    clearInterval(getTimer);
  }
})