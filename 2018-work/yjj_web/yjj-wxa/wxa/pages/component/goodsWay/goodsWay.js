
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var addUrl = constant.devUrl + "/mobile/memberOrder/chooseDeliveryType.json";

var tapData = 2;
var initPage = function (that, orderType, deliveryAddressId) {
  var store_Id = wx.getStorageSync('storeId');
  var member_id = wx.getStorageSync('id');
  var sendData = {};
  sendData = {
    memberId: member_id,
    storeId: store_Id,
    orderType: orderType,
    storeId: store_Id,
    memberid: member_id
  };
  var sign = util.MD5(util.paramConcat(sendData));
  wx.request({
    url: addUrl,
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log(res);
      wx.setStorageSync('orderType', orderType);
      //判断是否有数据，有则取数据

      if (res.data.successful) {
        if (orderType == 0) {    //到店提货
          that.setData({
            tabState1: true,
            tabState2: false,
            goodsList1: res.data.data,
            orderType: orderType
          })
          res.data.data[0].orderType = 0;
        } else if (orderType == 1) {  //送货上门
        //deliveryAddressId
          // var data = res.data.data;
      
          that.setData({
            tabState1: false,
            tabState2: true,
            goodsList2: res.data.data,
            goodsListType: res.data.data[0],
            deliveryAddressIds: deliveryAddressId,
            orderType: orderType
          })
          if (res.data.data.length > 0) {
            if (tapData == 2) {
              res.data.data[0].orderType = 1;
              wx.setStorageSync('goodsAddress2', res.data.data[0]);
            }
          }




        }
      }

    },
    complete: function () {
      //隐藏loadding

    },
    fail: function (res) {
      console.log(res.statusCode)
      wx.showToast({
        title: '失败 ',
        icon: 'fail',
        duration: 2000
      })
    }
  });

}


Page({

  /**
   * 页面的初始数据
   */
  data: {
    storeId: '',
    memberid: '',
    tabState1: true,   //tab切换状态
    tabState2: false,
    orderType: '',     //订单id
    goodsListType: {},
    deliveryAddressId:'',
    carIdGroupString: '', //购物车订单的参数,返回确认订单页面初始化时需要
    cartOrderFlag: ''   //购物车订单的标识，返回确认订单页面初始化时需要区别不同的接口
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const that = this;
    that.setData({
      orderType: options.orderType,
      deliveryAddressId: options.deliveryAddressId,
      cartOrderFlag: options.cartOrderFlag,
      carIdGroupString: options.carIdGroupString
    })
    console.log(options.carIdGroupString);
    console.log(that.data.orderType);

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this;


  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this;
    var deliveryAddressId = that.data.deliveryAddressId;
    initPage(that, that.data.orderType, deliveryAddressId);

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    tapData = 2

  },
  //状态和详细tab切换
  tabFun: function (e) {
    var that = this;
    var tab = e.currentTarget.dataset.tab;
    var member_id = that.data.memberId,   //会员id
      store_id = that.data.storeId,   //门店id
      order_id = that.data.orderId;
    console.log(tab - 1);
    if (tab == 1) {
      that.setData({
        tabState1: true,
        tabState2: false,
        // isTabMore: 1
      })
      initPage(that, 0);
      wx.setStorageSync('goodsListType', that.data.goodsListType);
      console.log(that.data.goodsListType);
    } else if (tab == 2) {
      that.setData({
        tabState1: false,
        tabState2: true,
        // isTabMore: 0
      })
      initPage(that, 1);
      wx.setStorageSync('goodsListType', that.data.goodsListType);

    }
  },
  radioChange: function (e) {
    var that =this;
    tapData += 1;
    var goodsAddress = e.detail.value,
      goodsAddressObject = {};
    goodsAddress = goodsAddress.split("-");
    goodsAddressObject.address = goodsAddress[3];
    goodsAddressObject.phoneNumber = goodsAddress[2];
    goodsAddressObject.linkmanName = goodsAddress[1];
    goodsAddressObject.deliveryAddressId = goodsAddress[0]
    goodsAddressObject.orderType = goodsAddress[4];
    console.log('radio发生change事件，携带value值为：', goodsAddressObject)
    that.setData({
      deliveryAddressId: goodsAddressObject.deliveryAddressId
    })
    wx.setStorageSync('deliveryAddressId', goodsAddressObject.deliveryAddressId);
  },
  editAddress: function (e) {
    var deliveryAddressId = e.currentTarget.dataset.id;
    console.log(deliveryAddressId);
    var cartOrderFlag = this.data.cartOrderFlag,
        carIdGroupString = this.data.carIdGroupString;
    if (cartOrderFlag){
      wx.navigateTo({
        url: '../editAddress/editAddress?deliveryAddressId=' + deliveryAddressId + '&cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString
      })
    }else{
      wx.navigateTo({
        url: '../editAddress/editAddress?deliveryAddressId=' + deliveryAddressId
      })
    }
  },
  newAddress: function (e) {
    var cartOrderFlag = this.data.cartOrderFlag,
        carIdGroupString = this.data.carIdGroupString;
    //购物车订单的标识，返回确认订单页面初始化时需要区别不同的接口
    if (cartOrderFlag){
      wx.navigateTo({
        url: '../newAddress/newAddress?cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString
      })
    }else{
      wx.navigateTo({
        url: '../newAddress/newAddress'
      })
    }
  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})