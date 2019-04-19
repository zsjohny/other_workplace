// 确认订单
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")

// orderObject = {};

//登录
var loginGetUserData = function (that, product_attr, coupon_amount, orderone, goodsAddresstwo, other_Page) {
  var sessionId = wx.getStorageSync("sessionId");
  var memberId = wx.getStorageSync('id');
  if (sessionId) {
    wx.checkSession({
      success: function () {
        //session 未过期，并且在本生命周期一直有效
        var id = wx.getStorageSync('id'); //获取会员id
        var store_id = wx.getStorageSync('storeId');
        //console.log("是否登录id",id);
        that.setData({
          memberId: id
        })
        initOrderData(that, id, store_id, product_attr, coupon_amount, orderone, goodsAddresstwo, other_Page);
      },
      fail: function () {
        //登录态过期
        app.loginUnauthorizedFun(function () {
          var id = wx.getStorageSync('id'); //获取会员id
          var store_id = wx.getStorageSync('storeId');
          that.setData({
            memberId: id
          })
        })
        initOrderData(that, id, store_id, product_attr, coupon_amount, orderone, goodsAddresstwo, other_Page);
      }
    })
  } else {
    console.log(3);
    //登录态过期
    app.loginUnauthorizedFun(function () {
      var id = wx.getStorageSync('id'); //获取会员id
      var store_id = wx.getStorageSync('storeId');
      that.setData({
        memberId: id
      })
      initOrderData(that, id, store_id, product_attr);
    })
  }
}
//小数点位数处理
var toDecimal2 = function (x) {
  var f = parseFloat(x);
  if (isNaN(f)) {
    return false;
  }
  var f = Math.round(x * 100) / 100;
  var s = f.toString();
  var rs = s.indexOf('.');
  if (rs < 0) {
    rs = s.length;
    s += '.';
  }
  while (s.length <= rs + 2) {
    s += '0';
  }
  return s;
}
//初始化数据
var initOrderData = function (that, member_id, store_id, product_attr, coupon_amount, orderone, goodsAddresstwo, other_Page) {
  var cartOrderFlag = that.data.cartOrderFlag;
  console.log('cartOrderFlag', cartOrderFlag);
  var sendData = {};
  if (other_Page) {
    if (cartOrderFlag){
      sendData = {
        memberId: member_id, //会员id
        storeId: store_id,
        carIds: that.data.carIdGroupString,
        orderType: other_Page,
        shopMemberDeliveryAddressId: goodsAddresstwo
      }
    }else{
      sendData = {
        memberId: member_id, //会员id
        storeId: store_id,
        productIdSkuIdCount: product_attr,
        orderType: other_Page,
        shopMemberDeliveryAddressId: goodsAddresstwo
      }
    }
  } else {
    if (cartOrderFlag){
      sendData = {
        memberId: member_id, //会员id
        storeId: store_id,
        carIds: that.data.carIdGroupString,
        shopMemberDeliveryAddressId: goodsAddresstwo
      }
    }else{
      sendData = {
        memberId: member_id, //会员id
        storeId: store_id,
        productIdSkuIdCount: product_attr,
        shopMemberDeliveryAddressId: goodsAddresstwo
      }
    }
  }
  var targetType = that.data.targetType,
      activityId = that.data.activityId;
  //如果是活动商品
  if (targetType && activityId){
    sendData.targetType = targetType;
    sendData.activityId = activityId
  }
  console.log(sendData);
  var sign = util.MD5(util.paramConcat(sendData));
  dialog.loading();
  wx.request({
    url: that.data.apiUrl, //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': wx.getStorageSync("sessionId"),
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("确认订单", res);
      var data = res.data.data;
      console.log(data.productInfos.allProductPrice);
      if (res.data.successful) {
        var paymentMoney = '',
            couponType = that.data.couponType,
            couponAmount = '';   //优惠金额
        //打折券    
        if (couponType == 2) {
          var disAmount = toDecimal2(parseFloat(coupon_amount/10) * parseFloat(data.productInfos.allProductPrice));
          paymentMoney = disAmount;
          couponAmount = toDecimal2(data.productInfos.allProductPrice - disAmount);
        } else {
          //红包、满减
          paymentMoney = toDecimal2(data.productInfos.allProductPrice - coupon_amount);
          couponAmount = coupon_amount
        }  
        that.setData({
          productInfos: data.productInfos,
          shopCouponCount: data.shopMemberCouponCount,
          paymentAmount: paymentMoney,
          couponAmount: couponAmount,
          buyType: data.productInfos.buyType,
          activityId: data.productInfos.activityId,
          productIds: data.productInfos.productSKUList[0],
          productList: data.productInfos.productSKUList  //商品清单列表数据
        })

        console.log(orderone)
        console.log(typeof (orderone))

          if (orderone != "" || typeof (orderone) == "number") {
            if (orderone == 0) {
                console.log("orderone == 0")
                that.setData({
                  businessName: data.addressInfo.dbusinessName,
                  address: data.addressInfo.daddress,
                  orderType: data.addressInfo.dorderType,
                })
            } else if (orderone == 1) {   
              console.log("orderone == 1")
                that.setData({
                  businessName: data.addressInfo.businessName,
                  address: data.addressInfo.address,
                  orderType: data.addressInfo.orderType,
                  linkmanName: data.addressInfo.linkmanName,
                  phoneNumber: data.addressInfo.phoneNumber,
                  deliveryAddressId: data.addressInfo.deliveryAddressId,
                  hasAddressInfo: data.addressInfo.hasAddressInfo 
                })
            }
          } else {
            console.log("orderone == 2")
            if (data.orderType == 0) {
              that.setData({
                businessName: data.storeBusiness.businessName,
                address: data.storeBusiness.businessAddress,
                orderType: data.orderType
              })
            } else {
              console.log("orderone == 3")
              that.setData({
                businessName: data.addressInfo.businessName,
                address: data.addressInfo.address,
                orderType: data.addressInfo.orderType,
                linkmanName: data.addressInfo.linkmanName,
                phoneNumber: data.addressInfo.phoneNumber,
                deliveryAddressId: data.addressInfo.deliveryAddressId,
                hasAddressInfo: data.addressInfo.hasAddressInfo 
              })
            }
          }
      } else {
        dialog.toast(res.data.error)
      }
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 800)
    }
  })
}
//提交支付函数
var paymentFunc = function (that, member_id, store_id, order_id) {
  var sendData = {
    memberId: member_id, //会员id
    storeId: store_id,
    orderId: order_id
  }
  console.log(sendData);
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
      //启用提交订单按钮
      // that.setData({
      //   btnDisabled: true
      // });
      console.log('支付返回数据', res.data);
      
      if(res.data.successful){
        const payData = res.data.data;
        wx.requestPayment({
          'appId': payData.appId,
          'timeStamp': payData.timeStamp,
          'nonceStr': payData.nonceStr,
          'package': payData.package,
          'signType': payData.signType,
          'paySign': payData.paySign,
          'success': function (res) {
            console.log('success', res);
            console.log(sendData);
            wx.redirectTo({
              url: '../orderDetail/orderDetail?orderId=' + sendData.orderId
            })
          },
          'fail': function (res) {
            console.log('fail', res);
            console.log(sendData);
            wx.redirectTo({
              url: '../orderDetail/orderDetail?orderId=' + sendData.orderId
            })
          },
          'complete': function (res) {
            console.log(res); console.log('complete');
          }
        });
      }else{
        if (res.data.code == -5001) {
          var showData = {
            isShow: true,
            title: '',    //活动提示框提示的标题
            content: res.data.error,    //活动提示框提示的内容
            iconType: '0',    //活动提示框icon显示的类型
            btnType: '0',   //活动提示框按钮显示的类型
            cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
            confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
          }
          that.setData({
            activityPromptData: showData
          })           
        } else if (res.data.code == -5002) {
          var showData = {
            isShow: true,
            title: '',    //活动提示框提示的标题
            content: res.data.error,    //活动提示框提示的内容
            iconType: '0',    //活动提示框icon显示的类型
            btnType: '0',   //活动提示框按钮显示的类型
            cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
            confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
          }
          that.setData({
            activityPromptData: showData
          })           
        } else if (res.data.code == -5003) {
          var showData = {
            isShow: true,
            title: '',    //活动提示框提示的标题
            content: res.data.error,    //活动提示框提示的内容
            iconType: '0',    //活动提示框icon显示的类型
            btnType: '0',   //活动提示框按钮显示的类型
            cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
            confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
          }
          that.setData({
            activityPromptData: showData
          })           
        } else if (res.data.code == -5004) {
          var order_id = res.data.data.orderId;
          console.log("order_id" + order_id)
          var showData = {
            isShow: true,
            title: '',    //活动提示框提示的标题
            content: res.data.error,    //活动提示框提示的内容
            iconType: 1,    //活动提示框icon显示的类型
            btnType: 1,   //活动提示框按钮显示的类型
            cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
            confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
          }
          that.setData({
            activityPromptData: showData,
            orderId: order_id,
          })
        } else if (res.data.code == -5005) {
          var showData = {
            isShow: true,
            title: '',    //活动提示框提示的标题
            content: res.data.error,    //活动提示框提示的内容
            iconType: '0',    //活动提示框icon显示的类型
            btnType: 2,   //活动提示框按钮显示的类型
            cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
            confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
          }
          that.setData({
            activityPromptData: showData
          })           
        }
      }
      
    }
  })
}

/**
 * 库存不足时，动态改变所有商品数据显示"库存不足"状态
 */
function changeStockState(that, noStockData) {
  var productSKUList = that.data.productList;
  console.log(productSKUList);
  for (var i = 0, l = productSKUList.length; i < l; i++) {
    for (let j = 0, h = noStockData.length; j < h; j++) {
      if (productSKUList[i].productId == noStockData[j].productId) {
        productSKUList[i].isStock = true
      } else {
        productSKUList[i].isStock = false
      }
    }
  }
  that.setData({
    productList: productSKUList
  })
}

Page({
  data: {
    memberId: wx.getStorageSync("id"),    //会员id
    storeId: wx.getStorageSync("storeId"),
    orderType: '',             //送货类型：0到店取货；1送货上门
    deliveryAddressId: '',
    businessName: '',
    address: '',
    remark: '', //备注
    otherPage: '',//判断从其他页面的跳转
    couponId: '',  //选择的优惠券id
    couponName: '',  //选择的优惠券名字
    couponAmount: 0,   //选中的可用优惠券金额
    paymentAmount: '',   //需支付的金额
    skuGroup: '',   //sku组合,
    productInfos: '', //订单sku数据,
    shopCouponCount: '', //优惠券数量
    storeBusiness: '',   //门店数据
    btnDisabled: true,    //订单提交按钮
    submitClickFlag: 1,   //订单提交按钮标识
    del: 0,
    payStatus:2,           //提交订单的状态
    buyType:'',
    activityId:'',
    orderId:'',    //订单id
    btnType:'',
    productIds:{},
    // 提示框显示控制
    isShow: false,
    apiUrl: '',             //接口地址,
    cartOrderFlag:'',    //购物车订单的标识
    carIdGroupString: '', //购物车订单的参数
    productList: []  //商品清单列表数据
  },
  onReady:function(){
   
  },
  onLoad: function (option) {
    var that = this;
    var sku_group = option.skuGroup,
        other_Page = option.otherPage,
        deliveryAddressId = option.deliveryAddressId,
        del = option.del,
        targetType = option.targetType,        //是否活动和普通商品
        activityId = option.activityId;       //活动id
    if (targetType && activityId){
      that.setData({
        targetType: targetType,
        activityId: activityId
      })
    }
    if (sku_group){
      that.setData({
        skuGroup: sku_group
      })
    }
    if (other_Page) {
      that.setData({
        otherPage: other_Page
      })
    }
    if (deliveryAddressId) {
      that.setData({
        deliveryAddressId: deliveryAddressId
      })
    }
    if (del) {
      that.setData({
        del: del
      })
    }
    //购物车跳转过来时
    var cartOrderFlag = option.cartOrderFlag;
    var carIdGroupString = option.carIdGroupString;
    var apiUrl = '';
    //购物车订单接口
    if (cartOrderFlag) {
      apiUrl = constant.devUrl + '/miniapp/car/confirmOrderFromCar.json';
    } else {
      apiUrl = constant.devUrl + '/mobile/memberOrder/confirmOrderV362.json';
    }
    if (cartOrderFlag && carIdGroupString) {
      that.setData({
        cartOrderFlag: cartOrderFlag,        //购物车订单的标识
        carIdGroupString: carIdGroupString   //购物车订单的参数
      })
    }
    that.setData({
      apiUrl: apiUrl                     //接口地址
    })
    console.log(del);
  },
  onShow: function () {
    var that = this;
    var orderone = wx.getStorageSync("orderType"),
        deliveryAddressId = wx.getStorageSync("deliveryAddressId");
    console.log(orderone, deliveryAddressId);
    //console.log(getCurrentPages());
    var product_attr = that.data.skuGroup,
        member_id = that.data.memberId,
        store_id = that.data.storeId,
        other_Page = that.data.otherPage;
    //优惠券
    var coupon_id = wx.getStorageSync("couponId"),
        coupon_name = wx.getStorageSync("couponName"),
        coupon_amount = wx.getStorageSync("couponAmount"),
        couponType = wx.getStorageSync("couponType");
    if (coupon_amount == '') {
      coupon_amount = 0;
    }     
    console.log(that.data.orderType);
    //console.log(that.data.productInfos.allProductPrice);
   
    if (that.data.del==1){
      loginGetUserData(that, product_attr, coupon_amount, orderone, deliveryAddressId, other_Page);
    }else{
      if (other_Page) {
        //登录初始化数据
        loginGetUserData(that, product_attr, coupon_amount, orderone, deliveryAddressId, other_Page);
      } else {
        //登录初始化数据
        loginGetUserData(that, product_attr, coupon_amount, orderone, deliveryAddressId);
      }
    }
    that.setData({
      couponType: couponType,
      couponId: coupon_id,
      couponName: coupon_name,
      orderType: orderone,
      deliveryAddressId: deliveryAddressId,
    })
    //隐藏右上角分享按钮
    wx.hideShareMenu()  
  },
  onHide: function () {
    
  },
  onUnload: function () {
    wx.removeStorageSync("couponId");
    wx.removeStorageSync("couponName");
    wx.removeStorageSync("couponAmount");
    wx.removeStorageSync("couponType");
    wx.removeStorageSync("orderType");
    wx.removeStorageSync("deliveryAddressId");
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
  //  备注
  bindKeyInput: function (e) {
    console.log(e.detail.value);
    this.setData({
      remark: e.detail.value
    })
  },
  //选择优惠券
  chooseCoupon: function () {
    var that = this,
        productList = that.data.productList,
        productIdArray = [];
    for (let i = 0, l = productList.length; i < l; i++) {
      productIdArray.push(productList[i].productId);
    }
    var productIds = productIdArray.join();
    wx.navigateTo({
      url: '../chooseCoupon/chooseCoupon?totalExpressAndMoney=' + this.data.productInfos.allProductPrice + "&productIds=" + productIds
    })
  },
  //选择收货方式 url='../goodsWay/goodsWay?orderType={{orderType}}' open-type='redirect' delta='2'
  goodsWayType: function (e) {
    console.log(e.currentTarget.dataset.type)
    var orderType = e.currentTarget.dataset.type;
    var that = this;
    console.log(getCurrentPages());
    //如果是购物车的订单
    var cartOrderFlag = this.data.cartOrderFlag,
        carIdGroupString = this.data.carIdGroupString;
    if (cartOrderFlag) {
      wx.navigateTo({
        url: '../goodsWay/goodsWay?orderType=' + orderType + '&deliveryAddressId=' + that.data.deliveryAddressId + '&cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString
      });
    }else{
      wx.navigateTo({
        url: '../goodsWay/goodsWay?orderType=' + orderType + '&deliveryAddressId=' + that.data.deliveryAddressId
      });
    }
  },
  //点击编辑收货地址
  editAddressBtn:function(e){
    //如果是购物车的订单
    var cartOrderFlag = this.data.cartOrderFlag,
        carIdGroupString = this.data.carIdGroupString;
    var noAddressFlag = '';  //当没有地址时，跳转到新增地址页面，返回确认订单页面层级标识    
    if (cartOrderFlag){
      wx.navigateTo({
        url: '../newAddress/newAddress?cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString + '&noAddressFlag=1'
      });
    }else{
      wx.navigateTo({
        url: '../newAddress/newAddress?noAddressFlag=1'
      });
    }
  },
  //提交订单
  confirmPay: function (e) {
    var that = this;
    dialog.loading();
    if (that.data.submitClickFlag == 0) { return }
    //禁用提交订单按钮
    that.setData({
      btnDisabled: false
    });
    var form_id = e.detail.formId;
    console.log(form_id);
    var member_id = wx.getStorageSync('id'),   //会员id
      store_id = wx.getStorageSync('storeId'),   //门店id
      coupon_id = that.data.couponId, //优惠券id
      product_attr = that.data.skuGroup;   //sku
    // orderType = that.data.orderType;
    console.log(e.currentTarget.dataset.type)
    console.log(that.data.oneType);
    //判断地址是否为空，为空不能提交
    var address = that.data.address;
    if (!address){
      dialog.toastError("请先填写收货地址");
      that.setData({
        btnDisabled: true
      });
      return 
    }
    var submitData = {};
    //购物车的提交订单
    var cartOrderFlag = that.data.cartOrderFlag;
    //购物车的提交订单时，获取sku,组合成productIdSkuIdCount的值
    var skuArray = [];
    var productList = that.data.productList;
    for (let i = 0, l = productList.length; i < l; i++){
      var skuGroup = productList[i].productId + "_" + productList[i].skuId + "_" + productList[i].count;
      skuArray.push(skuGroup);
    }
    var skuArrayString = skuArray.join();
    //到店取货
    if (that.data.orderType == 0) {
      if (cartOrderFlag) {
        submitData = {
          memberId: member_id,
          storeId: store_id,
          couponId: coupon_id,
          orderType: that.data.orderType,
          productIdSkuIdCount: skuArrayString,
          carIds: that.data.carIdGroupString,
          remark: that.data.remark,
          formId: form_id,
          buyWay: that.data.buyType,
          activityId: that.data.activityId
        }
      }else{
        submitData = {
          memberId: member_id,
          storeId: store_id,
          couponId: coupon_id,
          orderType: that.data.orderType,
          productIdSkuIdCount: product_attr,
          remark: that.data.remark,
          formId: form_id,
          buyWay: that.data.buyType,
          activityId: that.data.activityId
        }
      }
    }
    //送货上门
    else if (that.data.orderType == 1) {
      if (cartOrderFlag) {
        submitData = {
          memberId: member_id,
          storeId: store_id,
          couponId: coupon_id,
          orderType: that.data.orderType,
          productIdSkuIdCount: skuArrayString,
          carIds: that.data.carIdGroupString,
          remark: that.data.remark,
          formId: form_id,
          deliveryAddressId: that.data.deliveryAddressId,
          buyWay: that.data.buyType,
          activityId: that.data.activityId
        }
      }else{
        submitData = {
          memberId: member_id,
          storeId: store_id,
          couponId: coupon_id,
          orderType: that.data.orderType,
          productIdSkuIdCount: product_attr,
          remark: that.data.remark,
          formId: form_id,
          deliveryAddressId: that.data.deliveryAddressId,
          buyWay: that.data.buyType,
          activityId: that.data.activityId
        }
      }
    }
    that.setData({
      submitClickFlag: 0
    })
    var sign = util.MD5(util.paramConcat(submitData));
    console.log('提交订单入参数据', submitData);
    var submitUrl = constant.devUrl + "/mobile/memberOrder/addShopMemberOrder.json";
    wx.request({
      url: submitUrl,
      data: submitData,
      header: {
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log('提交订单数据', res.data);
        console.log(that.prompt0);
        if (res.data.successful) {
          that.setData({
            submitClickFlag: 0
          })
          //console.log(res.data.data.orderId);
          var order_id = res.data.data.orderId;
          var paymentAmount = that.data.paymentAmount;
          console.log(paymentAmount);
          console.log(res.data);
          if (paymentAmount <= 0) {
            wx.redirectTo({
              url: '../orderDetail/orderDetail?orderId=' + order_id
            })
          } else {
            dialog.hide()
            //支付
            paymentFunc(that, member_id, store_id, order_id);
          }
        } else {
          if (res.data.code == -704){
            dialog.toastError(res.data.error);
            setTimeout(function(){
              changeStockState(that, res.data.data)
            },3000)
          }
          if (res.data.code == -5001) {
            var showData = {
              isShow: true,
              title: '',    //活动提示框提示的标题
              content: res.data.error,    //活动提示框提示的内容
              iconType: '0',    //活动提示框icon显示的类型
              btnType: '0',   //活动提示框按钮显示的类型
              cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
              confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
            }
            that.setData({
              activityPromptData: showData
            })           
          } else if (res.data.code == -5002){    
            var showData = {
              isShow: true,
              title: '',    //活动提示框提示的标题
              content: res.data.error,    //活动提示框提示的内容
              iconType: '0',    //活动提示框icon显示的类型
              btnType: '0',   //活动提示框按钮显示的类型
              cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
              confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
            }
            that.setData({
              activityPromptData: showData
            })
          } else if (res.data.code == -5003) {            
            var showData = {
              isShow: true,
              title: '',    //活动提示框提示的标题
              content: res.data.error,    //活动提示框提示的内容
              iconType: '0',    //活动提示框icon显示的类型
              btnType: '0',   //活动提示框按钮显示的类型
              cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
              confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
            }
            that.setData({
              activityPromptData: showData
            })
          } else if (res.data.code == -5004) {
             //恭喜您，参团成功！
            var order_id = res.data.data.orderId;
            console.log("order_id" + order_id)
            var showData = {
              isShow: true,
              title: '',    //活动提示框提示的标题
              content: res.data.error,    //活动提示框提示的内容
              iconType: 1,    //活动提示框icon显示的类型
              btnType: 1,   //活动提示框按钮显示的类型
              cancelEvent: '_closePayPrompt',   //提示框关闭取消的回调函数名字
              confirmEvent: '_confirmPay',   //提示框确定支付的回调函数名字
            }
            that.setData({
              activityPromptData: showData,
              orderId: order_id,
            })
          }
        }

      }
    })
  },
  edit:function(e){
    var id = e.currentTarget.dataset.id;
    var cartOrderFlag = this.data.cartOrderFlag,
        carIdGroupString = this.data.carIdGroupString;
    //购物车订单的标识，返回确认订单页面初始化时需要区别不同的接口
    if (cartOrderFlag){
      wx.navigateTo({
        url: '../goodsWay/goodsWay?orderType=' + 1 + '&deliveryAddressId=' + id + '&cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString
      });
    }else{
      wx.navigateTo({
        url: '../goodsWay/goodsWay?orderType=' + 1 + '&deliveryAddressId=' + id
      });
    }
  },
  //定义页面可转发的函数-团购邀请好友
  onShareAppMessage: function (res) {
    var that = this,
        activityId = that.data.productInfos.activityId,
        productId = this.data.productIds.productId,
        shareTitle = this.data.productIds.name,
        shareImg = this.data.productIds.image;
    wx.hideShareMenu();
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: shareTitle,
      path: '/pages/component/detail/detail?productId=' + productId + '&shareFlag=1' + "&activityId=" + activityId + "&targetType=1",
      imageUrl: productId,
      success: function (res) {

      },
      fail: function (res) {
        // 转发失败
      }
    }
  },
  _closePayPrompt:function() {
    //触发取消回调    
    var that = this;
    var btnType = that.data.btnType;
    if (btnType == 0){      
      wx.navigateBack({
        delta: 1
      })
    }else{
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
  _confirmPay: function ()  {
    //触发成功回调 _confirmPay
    var that = this;
     var order_id = that.data.orderId;
     var member_id = wx.getStorageSync('id'),   //会员id
       store_id = wx.getStorageSync('storeId');   //门店id
     paymentFunc(that, member_id, store_id, order_id);
     that.setData({
       activityPromptData: {
         isShow: false
       }
     })
  },
  
})