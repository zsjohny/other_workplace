// 订单详情
var dialog = require("../../../utils/dialog"),
    util = require("../../../utils/util"),
    constant = require("../../../constant");
var app = getApp()
var getTimer1 = null;
/**
 * 初始化数据
 */
function initData(that){
  var param = {
    data: {
      id: that.data.orderId,
      storeId: that.data.storeId,
    }
  }
  app.orderApi.orderDetail(param).then(res => {
    console.log(res)
    if (res.code === 200) {
      that.setData({
        detailInfo: res.data,
        orderStatus: res.data.orderStatus,
        productList: res.data.itemList
      })
    }
  })
}
/**
 * 支付函数
 */
function paymentFunc(that, member_id, store_id, order_id) {
  if (that.data.payStatus) {
    return;
  }
  var sendData = {
    memberId: member_id, //会员id
    storeId: store_id,
    orderId: order_id
  }
  //console.log("order_id", order_id);
  var sign = util.MD5(util.paramConcat(sendData));
  var payUrl = constant.devUrl + "/miniapp/pay/wxaPay.json";
  wx.request({
    url: payUrl,
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log('支付返回数据', res.data);
      if (res.data.successful) {
        const payData = res.data.data;
        that.setData({
          orderId: 0
        })
        wx.requestPayment({
          'appId': payData.appId,
          'timeStamp': payData.timeStamp,
          'nonceStr': payData.nonceStr,
          'package': payData.package,
          'signType': payData.signType,
          'paySign': payData.paySign,
          'success': function (res) {
            initData(that)
          }
        });
      } else {
        var showData = {
          isShow: true,
          title: '',    //活动提示框提示的标题
          content: res.data.error,    //活动提示框提示的内容
          iconType: '0',    //活动提示框icon显示的类型
          btnType: '0',   //活动提示框按钮显示的类型
          cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
          confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
        }
        var code = res.data.code;
        if (code == -5001 || code == -5002) {
          that.setData({
            activityPromptData: showData
          })
        }else if (code == -5005) {
          showData.btnType = 2;
          that.setData({
            activityPromptData: showData
          })
        }
      }
    }
  })
}
/**
 * 定时获取提货状态
 */
function timingGetOrderState(that, store_id, order_id, status) {
  var param = {
    data: {
      storeId: store_id,
      orderStatus: 1,
      orderId: order_id
    }
  }
  app.orderApi.checkOrderStatus(param).then(data => {
    if (data.data.flag) {
      //更新订单数据
    } else {
      that.setData({
        pickStatus: true,
        isShowCodeModel: false,
        pickOpcityBg: true
      })
      clearInterval(getTimer1);
      that.codePickInvalid();
      initData(that)
    }
  })
}
Page({
  data: {
    storeId: '',          //门店id
    memberId: '',         //会员id
    orderId: '',           //订单号
    banner: '',              //是否显示售后单号0显示  1不显示
    isShowCodeModel: false, //是否显示提货二维码弹窗的状态
    boxOpcityBg: false,      //提货二维码弹窗透明层
    invalidPrompt: false, //二维码失效后的提示文字
    codeShowState: true,   //二维码失效前的显示的提示文字和二维码
    refleshBtn: false,    //点击刷新按钮
    imageUrl:'',            //二维码图片
    activityPromptData: {}  //活动弹窗显示的数据
  },
  onReady: function () {
    //获得confirmPopup组件
    this.confirmPopup = this.selectComponent("#receiptPopup");
    this.deletePopup = this.selectComponent("#deletePopup");
    this.cancelPopup = this.selectComponent("#cancelPopup");
  },
  onLoad: function (options) {
    var that = this,
        banner = options.banner,
        member_id = wx.getStorageSync('id'),
        store_id = wx.getStorageSync('storeId');
    that.setData({
      banner: banner,
      orderId: options.orderId,
      storeId: store_id,
      memberId: member_id
    })
  },
  onShow: function () {
    var that = this;
    initData(that);
    //隐藏右上角分享菜单
    wx.hideShareMenu();
  },
  //点击提货显示提货二维码弹窗
  showCodePopup: function (e) {
    var that = this,
      order_id = that.data.orderId,
      store_id = that.data.storeId,
      member_id = that.data.memberId,
      state = that.data.orderStatus;
    that.setData({
      isShowCodeModel: true,
      boxOpcityBg: true,
      codeShowState: true,
      invalidPrompt: false,
      refleshBtn: false
    })
    var param = {
      data: {
        orderId: order_id,
        memberId: member_id,
        storeId: store_id
      }
    }
    //定时获取提货状态
    var timingGetState = function () {
      timingGetOrderState(that, store_id, order_id, state);
      getTimer1 = setTimeout(timingGetState, 1000);
    }
    timingGetState();
    let urlParam = "?storeId=" + store_id + "&memberId=" + member_id + "&type=dingdan&id=" + order_id + "&width=400&height=400";
    app.orderApi.getOrderCodeData(param).then(data => {
      var imgUrl = constant.devUrl + "/mobile/memberOrder/qrcode/src.do" + urlParam;
      that.setData({
        orderOwnData: data.data,
        text1: data.data.text1,
        text2: data.data.text2,
        text3: data.data.text3,
        text4: data.data.text4,
        text5: data.data.text5,
        text6: data.data.text6,
        imageUrl: imgUrl,
        telePhoneNumber: data.data.telePhoneNumber,
      })
      that.codeImgInvalid();
    })
  },
  //三分钟二维码失效
  codeImgInvalid: function () {
    var that = this;
    var codeTimer = setTimeout(function () {
      //显示失效提示文字
      that.setData({
        invalidPrompt: true,
        refleshBtn: true,
        codeShowState: false
      })
    }, 183000)
  },
  //点击刷新优惠券二维码
  refleshCode: function (e) {
    var that = this,
      store_id = that.data.storeId,
      member_id = that.data.memberId,
      order_id = that.data.orderId;
    console.log(order_id);
    var rand = Math.random();
    var codeImgUrl = constant.devUrl + "/mobile/memberOrder/qrcode/src.do?memberId=" + member_id + "&storeId=" + store_id + "&type=dingdan" + "&id=" + order_id + "&width=400&height=400";
    //隐藏提示文字
    that.setData({
      invalidPrompt: false,
      codeShowState: true,
      imageUrl: codeImgUrl,
      refleshBtn: false
    })
    that.codeImgInvalid();
  },
  //关闭提货二维码弹窗
  closeCodeImgBox: function (e) {
    var that = this;
    console.log(that.data.state);
    that.setData({
      boxOpcityBg: false,
      isShowCodeModel: false,
      successState: false
    })
    clearTimeout(getTimer1);
    initData(that);
  },
  //支付
  payFunc: function (e) {
    var that = this;
    console.log("支付", e);
    var order_id = that.data.orderId;
    that.setData({
      orderId: order_id
    })
    var store_id = that.data.storeId,
      member_id = that.data.memberId;
    paymentFunc(that, member_id, store_id, order_id);
  },
  //定义页面可转发的函数-团购邀请好友
  onShareAppMessage: function (res) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: this.data.productName,
      path: '/pages/component/detail/detail?productId=' + this.data.productId + "&readState=1" + '&shareFlag=1',
      imageUrl: this.data.image,
      success: function (res) {
      }
    }
  },
  //关闭提货成功
  hidePickBox: function () {
    var that = this;
    clearTimeout(getTimer1);
    that.setData({
      pickStatus: false,
      pickOpcityBg: false,
    })
    initData(that)
  },
  //三秒钟刷新二维码
  codePickInvalid: function () {
    var that = this;
    var codeTimer = setTimeout(function () {
      that.setData({
        pickStatus: false,
        pickOpcityBg: false
      })
    }, 3000)
  },
  //拨打电话
  makePhone: function (e) {
    util.makePhone(e);
  },
  //查看物流详情
  openExpressDetail: function (e) {
    var that = this,
        orderId = that.data.orderId;
    // 保存跳转时状态
    that.setData({
      tabState: e.currentTarget.dataset.orderStatus
    })
    console.log(e.currentTarget.dataset.orderStatus);
    wx.navigateTo({
      url: '../express/express?orderId=' + orderId
    })
  },
  //显示确认收货弹窗
  openReceiptXbox: function (e) {
    var order_id = this.data.storeId;
    this.confirmPopup.showPopup();
    this.setData({
      orderId: order_id
    })
  },
  //关闭确认收货弹窗
  closeReceiptXbox: function (e) {
    this.confirmPopup.hidePopup();
  },
  //确认收货
  checkReceipt: function (e) {
    var that = this,
        member_id = that.data.memberId,
        store_id = that.data.storeId,
        tabState = that.data.tabState,
        order_id = that.data.orderId;
    var param = {
      data: {
        storeId: store_id,
        shopMemberOrderId: order_id
      },
      loading: true
    }
    app.orderApi.orderComfirmReceipt(param).then(data => {
      that.data.closeReceiptXbox();
      dialog.toastError("订单交易完成！");
      that.setData({
        confirmReceipt: false
      })
      //重新初始化数据
      setTimeout(function () {
        initData(that);
      }, 1500)
    })
  },
  //团购取消函数
  _closePayPrompt: function () {
    var that = this,
      btnType = that.data.btnType;
    if (btnType == 0) {
      wx.navigateBack({
        delta: 1
      })
    } else {
      var order_id = that.data.orderId;
      wx.redirectTo({
        url: '../orderDetail/orderDetail?orderId=' + order_id
      })
    }
    that.setData({
      activityPromptData: {
        isShow: false
      }
    })
  },
  //团购确认函数
  _confirmPay: function () {
    var that = this,
      order_id = that.data.orderId,
      member_id = wx.getStorageSync('id'),   //会员id
      store_id = wx.getStorageSync('storeId');   //门店id
    paymentFunc(that, member_id, store_id, order_id);
    that.setData({
      activityPromptData: {
        isShow: false
      }
    })
  },
  //显示确认删除订单弹窗
  showDeletePopup: function (e) {
    this.deletePopup.showPopup();
    var order_id = this.data.orderId;
    this.setData({
      deleteOrderId: order_id
    })
  },
  //关闭删除订单弹窗
  closeDeleteXbox: function () {
    this.deletePopup.hidePopup();
  },
  //确定单删除订单函数
  deleteOrderFunc: function (e) {
    var that = this,
      member_id = wx.getStorageSync('id'),
      store_id = wx.getStorageSync('storeId'),
      deleteOrderId = that.data.deleteOrderId;
    //console.log(deleteOrderId);
    var param = {
      data: {
        orderId: deleteOrderId
      }
    }
    app.orderApi.deleteOrder(param).then(data => {
      dialog.toast("订单删除成功！");
      that.closeDeleteXbox();
      //重新初始化数据
      setTimeout(function () {
        initData(that);
      }, 1500)
    })
  },
  //显示取消订单弹窗
  showCancelPopup: function (e) {
    this.cancelPopup.showPopup();
    var order_id = this.data.orderId;
    this.setData({
      cancelOrderId: order_id
    })
  },
  //关闭取消订单弹窗
  closeCancelXbox: function () {
    this.cancelPopup.hidePopup();
  },
  //取消订单
  cancelOrderFunc: function (e) {
    var that = this;
    var param = {
      data: {
        memberId: that.data.memberId,
        storeId: that.data.storeId,
        orderId: that.data.cancelOrderId,
        orderStatus: 0
      }
    }
    app.orderApi.cancelOrder(param).then(res => {
      that.closeCancelXbox();
      //刷新数据
      setTimeout(function () {
        initData(that);
      }, 1500)
    })
  },
  //申请售后
  refundApply: function (e) {
    var orderId = this.data.orderId,
        productList = this.data.productList;
    if (productList.length > 1) { // 多个商品
      wx.navigateTo({
        url: '../refundList/refundList?orderId=' + orderId
      })
    } else { // 单个商品
      wx.navigateTo({
        url: '../refundApply/refundApply?orderId=' + orderId + '&skuId=' + productList[0].skuId
      })
    }
  }
})