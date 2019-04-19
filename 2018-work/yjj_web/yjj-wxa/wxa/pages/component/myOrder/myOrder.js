// 我的订单
var app = getApp()
var getTimer1 = null,
    util = require("../../../utils/util"),
    constant = require('../../../constant'),
    dialog = require("../../../utils/dialog");
/**
 * 下拉加载更多通用函数
 */
var pages = 2;
function getMoreList(that, status){
  //当已经显示全部时，下拉不显示loading
  if (that.data.moreOver) {
    that.setData({
      hasMore: false
    })
  } else {
    that.setData({
      hasMore: true
    })
  }
  var param = {
    data: {
      storeId: that.data.storeId,
      orderStatus: status,
      current: pages,             //当前是第几页
      size: 10                    //每页显示条数
    }
  }
  app.orderApi.getOrderList(param).then(data => {
    console.log("上拉加载后的data", data);
    var dataArray = that.data.orderListData,
        data = data.data.smallPage.records;
    //有数据
    if (data.length > 0) {
      that.setData({
        orderListData: dataArray.concat(data)
      })
    } else {
      //没有数据
      that.setData({
        hasMore: false,
        moreOver: true
      });
    }
    pages++;
    console.log("上拉加载后的orderListData", orderListData);
  })
}
/**
 * 获取订单数据列表通用函数
 */
function getOrderCommon(that, status) {
  pages = 2;
  var param = {
    data:{
      storeId: that.data.storeId,
      orderStatus: status,
      current: 1,    //当前是第几页
      size: 10                     //每页显示条数
    },
    loading:true
  }
  app.orderApi.getOrderList(param).then(data =>{
    var orderData = data.data.smallPage.records;
    console.log('返回数据',orderData)
    if (orderData.length > 0) {
      that.setData({
        orderListData: orderData,
        pullSuccess: true
      })
    }
  })
};
/**
 * 库存不足时，自动取消订单
 */
function cancelOrderAuto(that, member_id, store_id){
  var param = {
    data:{
      memberId: member_id,
      storeId: store_id,
      orderId: that.data.orderId,
      orderStatus: 0,
      resonType: 3,
      reason: '因商品库存不足，系统自动取消订单'
    }
  }
  app.orderApi.cancelOrderAuto(param).then(res =>{
    setTimeout(function () {
      //刷新数据
      var statue = that.data.tabState;
      getOrderCommon(that, statue);
    }, 3000)
  })
}
/**
 * 支付函数
 */
function paymentFunc(that,member_id, store_id, order_id) {
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
            console.log('success', res, order_id);
            wx.redirectTo({
              url: '../orderDetail/orderDetail?orderId=' + sendData.orderId
            })
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
        if (code == -704){
          dialog.toastError(res.data.error);
          cancelOrderAuto(that, member_id, store_id)
        }
        else if (code == -5001 || code == -5002 || code == -5003) {
          that.setData({
            activityPromptData: showData
          })
        }else if (code == -5004) {
          var order_id = res.data.data.orderId;
          console.log("order_id" + order_id);
          showData.iconType = 1;
          showData.btnType = 1;
          that.setData({
            activityPromptData: showData,
            orderId: order_id,
          })
        } else if (code == -5005) {
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
function timingGetOrderState(that, store_id, order_id,status) {
  var param = {
    data:{
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
      getOrderCommon(that, that.data.tabState)
    }
  })
}
Page({
  data: {
    storeId: '',            //门店id
    memberId: '',
    allData:[],              //全部
    orderListData: [],          //全部订单列表数据
    pendingPaymentTime:[],    //订单倒计时
    oblData: [],  //待付款的列表数据
    pickData: [],  //待提货的列表数据
    sendData:[],//待发货的列表数据
    putData:[],  //待收货的列表数据
    isTabMore: 0,     //控制列表上拉加载
    tabState: -1, // 订单状态初始
    dealState:true,  //交易状态
    isShowCodeModel:false, //是否显示提货二维码弹窗的状态
    boxOpcityBg:false,
    successState:false,
    invalidPrompt: false, //二维码失效后的提示文字
    codeShowState: true,   //二维码失效前的显示的提示文字和二维码
    refleshBtn: false,    //点击刷新按钮
    paytrue:true,   //付款状态
    payfalse:false,
    pickStatus:false,  //提货状态
    pickOpcityBg:false,
    state:"" ,   //订单的状态
    confirmReceipt:false , //确认收货弹窗显示
    payStatus:false,
    statue:'',
    codeOrderId: '',        //显示当前二维码的订单id
    btnType:'',
    productId:'',           //分享商品id
    productName: '',
    image:'',
    btnDisabled: true,    //保存提交按钮
    // 提示框显示控制
    activityPromptData:{},  //活动弹窗显示的数据
    isShow: false,
    moreOver: false,   // 下拉加载无订单
    loadingMore: false, // 下拉加载样式
    pullSuccess: false, // 下拉成功
    deleteOrderId:'',    //需要删除订单的id
    cancelOrderId: ''    //需要取消订单的id
  },
  onLoad: function (e) {
    var that = this,
        memberId = wx.getStorageSync('id'),
        storeId = wx.getStorageSync('storeId');
    if (e.status) {
      var a = e.status;
      that.setData({
        tabState: a,
        memberId: memberId,
        storeId: storeId
      })
    }
  },
  onShow: function (e) {
    var that =this,
        status = that.data.tabState;
    getOrderCommon(that, status);
    that.codePickInvalid();
    //隐藏右上角分享按钮
    wx.hideShareMenu()
  },
  onReady: function () {
    //获得confirmPopup组件
    this.confirmPopup = this.selectComponent("#receiptPopup");
    this.deletePopup = this.selectComponent("#deletePopup");
    this.cancelPopup = this.selectComponent("#cancelPopup");
  },
  //隐藏提示框
  hidePrompt() {
    this.setData({
      isShow: !this.data.isShow
    })
  },
  //展示提示框
  showPrompt() {
    this.setData({
      isShow: !this.data.isShow
    })
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    getOrderCommon(that, that.data.tabState)
    setTimeout(function () {
      if(that.data.pullSuccess) {
          wx.stopPullDownRefresh()
      }
    }, 300)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    console.log("that.data.tabState", that.data.tabState);
    if(!that.data.moreOver){
      getMoreList(that, that.data.tabState)
    }
  },
  //订单tab切换
  tabFun: function (e) {
    var that = this,
        tab = e.currentTarget.dataset.tab;
    console.log(tab,'切换tab');
    that.setData({
        hasMore: false,
        moreOver: false,
        tabState: tab,
        orderListData: []
    })
    getOrderCommon(that, tab)
  },
  //跳到订单详情
  gotoOrderDetail: function (e) {
    var id = e.currentTarget.dataset.id,
        banner = e.currentTarget.dataset.banner,
        that = this;
    //保存跳转时状态
    that.setData({
        state: e.currentTarget.dataset.state
    })
    wx.navigateTo({
      url: '../orderDetail/orderDetail?orderId=' + id + '&banner=' + banner
    })
  },
  //点击提货显示提货二维码弹窗
  showCodePopup: function (e) {
    var that = this,
        order_id = e.currentTarget.dataset.id,
        store_id = that.data.storeId,
        member_id = that.data.memberId,
        state = e.currentTarget.dataset.orderStatus;
    that.setData({
      tabState: state,
      isShowCodeModel: true,
      boxOpcityBg: true
    })
    var param = {
      data:{
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
        text1: data.data.text1,
        text2: data.data.text2,
        text3: data.data.text3,
        text4: data.data.text4,
        text5: data.data.text5,
        text6: data.data.text6,
        imageUrl: imgUrl,
        codeOrderId: data.data.orderId,
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
        order_id = e.currentTarget.dataset.id;
    console.log(order_id);
    var rand = Math.random();
    var codeImgUrl = constant.devUrl + "/mobile/memberOrder/qrcode/src.do?memberId=" + member_id + "&storeId=" + store_id + "&type=dingdan" + "&id=" + order_id + "&width=400&height=400";
    //刷新重新加载二维码
    that.setData({
      imageUrl: codeImgUrl
    })
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
    getOrderCommon(that, that.data.tabState)
  },
  //支付
  payFunc:function(e) {
    var that = this;
    console.log("支付",e);
    var order_id = e.currentTarget.dataset.id;
    that.setData({
      orderId: order_id
    })
    var store_id = that.data.storeId,
        member_id = that.data.memberId;
    paymentFunc(that,member_id, store_id, order_id);
  },
  //定义页面可转发的函数-团购邀请好友
  onShareAppMessage: function (res) {
    console.log(this.data.productName, this.data.productId)
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
    console.log(that.data.tabState)
    getOrderCommon(that, that.data.tabState)
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
         orderId = e.currentTarget.dataset.id;
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
   openReceiptXbox:function(e){
     var order_id = e.currentTarget.dataset.id;
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
       data:{
         storeId: store_id,
         shopMemberOrderId: order_id
       },
       loading:true
     }
     app.orderApi.orderComfirmReceipt(param).then(data => {
       that.closeReceiptXbox();
       dialog.toastError("订单交易完成！");
       that.setData({
         confirmReceipt: false
       })
       //重新初始化数据
       setTimeout(function () {
         getOrderCommon(that, tabState)
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
   showDeletePopup:function(e){
     this.deletePopup.showPopup();
     var order_id = e.currentTarget.dataset.id;
     this.setData({
       deleteOrderId: order_id
     })
   },
   //关闭删除订单弹窗
  closeDeleteXbox:function(){
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
      data:{
        orderId: deleteOrderId
      }
    }
    app.orderApi.deleteOrder(param).then(data => {
      dialog.toast("订单删除成功！");
      that.closeDeleteXbox();
      //重新初始化数据
      setTimeout(function () {
        that.setData({
          orderListData: []
        })
        getOrderCommon(that, that.data.tabState)
      }, 2500)
    })
  },
  //显示取消订单弹窗
  showCancelPopup: function (e) {
    this.cancelPopup.showPopup();
    var order_id = e.currentTarget.dataset.id;
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
        storeId: that.data.memberId,
        orderId: that.data.cancelOrderId,
        orderStatus: 0
      }
    }
    app.orderApi.cancelOrder(param).then(res => {
      that.closeCancelXbox();
      //刷新数据
      setTimeout(function () { 
        var status = that.data.tabState;
        getOrderCommon(that, status);
      }, 1000)
    })
  },
  //申请售后
  refundApply: function (e) {
    var item = e.currentTarget.dataset.item;
    if (item.itemList.length > 1){ // 多个商品
      wx.navigateTo({
        url: '../refundList/refundList?orderId=' + item.orderId
      })
    }else{ // 单个商品
      wx.navigateTo({
        url: '../refundApply/refundApply?orderId=' + item.orderId + '&skuId=' + item.itemList[0].skuId
      })
    }
  }
})
