var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")

var show = false;
var item = {};


var editUrl = constant.devUrl + "/shop/shopMemberDeliveryAddress/updateDeliveryAddress.json"; 
var editInitPage = function(that){
    var sendData = {};
    var memberId = wx.getStorageSync('id'),
      storeId = wx.getStorageSync('storeId'),
      sku_group = wx.getStorageSync('skuGroup');
      that.setData({
        skugroup: sku_group,
        storeId: storeId,
        memberId: memberId
      })
    sendData={
      deliveryAddressId: that.data.deliveryAddressId,
      storeId: storeId,
      memberId: memberId
    }
    var sign = util.MD5(util.paramConcat(sendData));
    wx.request({
      url: editUrl,
      data:sendData,
      header: {
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log(res);
        if(res.data.jsonResponse.successful){
          var area = res.data.jsonResponse.data.location;
          area = area.split('-');
          console.log(area);
          that.setData({
            region: area,
            editAddress:res.data.jsonResponse.data,
            name: res.data.jsonResponse.data.linkmanName,
            Phone: res.data.jsonResponse.data.phoneNumber,
            detailAddress: res.data.jsonResponse.data.address,
          })
        }
      }
    })

}  

Page({

  /**
   * 页面的初始数据
   */
  data: {
    deliveryAddressId:'',
    name: '',
    Phone: '',
    detailAddress: '',
    provice: '',
    city: '',
    county: '',
    region: [],
    customItem: '',
    skugroup:'',
    storeId: '',  
    memberId:'',
    confirmReceipt:false,  //弹窗
    submitClickFlag: 1,   //修改提交按钮标识
    btnDisabled: true,    //保存提交按钮
    carIdGroupString: '', //购物车订单的参数,返回确认订单页面初始化时需要
    cartOrderFlag: ''    //购物车订单的标识，返回确认订单页面初始化时需要区别不同的接口
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var deliveryAddress_Id = options.deliveryAddressId;  //获取url的参数
    that.setData({
      deliveryAddressId: deliveryAddress_Id,
      cartOrderFlag: options.cartOrderFlag,
      carIdGroupString: options.carIdGroupString
    });
    console.log(that.data.deliveryAddressId)
    console.log(that.data.sku_group);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function (e) {
    var that = this;
    //请求数据
    // model.updateAreaData(that, 0, e);
    editInitPage(that);
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
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
  
  },
  //用户信息
  userNameInput: function (e) {
    this.setData({
      name: e.detail.value
    })
  },
  phoneWdInput: function (e) {
    this.setData({
      Phone: e.detail.value
    })
  },
  addressInput: function (e) {
    this.setData({
      detailAddress: e.detail.value
    })
  },
  saveTap:function(e){
    var that = this;
    var areas = '';
    areas = this.data.region[0] + '-' + this.data.region[1] + '-' + this.data.region[2];
    console.log("aaaa", that.data.region);
    if (that.data.submitClickFlag == 0) { return }
    
    var submitData = {},
      name = this.data.name,
      Phone = this.data.Phone,
      detailAddress = this.data.detailAddress,
      reg_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/;
    if (!name) {
      wx.showToast({
        title: '输入姓名为空',
        icon: 'success',
        duration: 1500
      })
      return
    } else if (!Phone) {
      wx.showToast({
        title: '输入手机号码为空',
        icon: 'success',
        duration: 1500
      })
      return
    } else if (!reg_phone.test(Phone)) {
      wx.showToast({
        title: '手机号码有误',
        icon: 'success',
        duration: 1500
      })
      return
    } else if (that.data.region.length == 0) {
      wx.showToast({
        title: '输入地址为空',
        icon: 'success',
        duration: 1500
      })
      return
    } else if (!detailAddress) {
      wx.showToast({
        title: '输入详细为空',
        icon: 'success',
        duration: 1500
      })
      return
    }


    submitData = {
      linkmanName: this.data.name,
      phoneNumber: this.data.Phone,
      location: areas,
      address: this.data.detailAddress,
      deliveryAddressId: that.data.deliveryAddressId,
      memberId: that.data.memberId,
      storeId: that.data.storeId
    };
    var sign = util.MD5(util.paramConcat(submitData));
    var submitUrl = constant.devUrl + "/shop/shopMemberDeliveryAddress/updateDeliveryAddressSave.json";
    //禁用提交订单按钮
    that.setData({
      btnDisabled: false
    });
    wx.request({
      url: submitUrl,
      data: submitData,
      header: {
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log('提交订单数据', res.data);
        if (res.data.jsonResponse.successful) {
          console.log('11')
          that.setData({
            submitClickFlag: 0
          })
          
          var sku_group = wx.getStorageSync('skuGroup');
          console.log(sku_group);
          var cartOrderFlag = that.data.cartOrderFlag,
              carIdGroupString = that.data.carIdGroupString;
          //购物车订单的标识，返回确认订单页面初始化时需要区别不同的接口
          /*if (cartOrderFlag){
            wx.redirectTo({
              url: '../confirmOrder/confirmOrder?skuGroup=' + sku_group + '&otherPage=' + 1 + '&cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString
            })
          }else{
            wx.redirectTo({
              url: '../confirmOrder/confirmOrder?skuGroup=' + sku_group + '&otherPage=' + 1
            })
          }*/
          //返回到订单页面
          wx.navigateBack({
            delta: 2
          });
          // wx.navigateBack({
          //   delta: 1
          // })
        } else {

        }

      }
    })
  },
  delTap:function(e){
    var that = this;
    // var areas = '';
    // areas = this.data.province + '-' + this.data.city + '-' + this.data.county;
    var submitData = {};

    submitData = {
      deliveryAddressId: that.data.deliveryAddressId,
      memberId: that.data.memberId,
      storeId: that.data.storeId
    };
    var sign = util.MD5(util.paramConcat(submitData));
    var submitUrl = constant.devUrl + "/shop/shopMemberDeliveryAddress/deleteDeliveryAddress.json";
    wx.request({
      url: submitUrl,
      data: submitData,
      header: {
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log('提交订单数据', res.data);
        if (res.data.jsonResponse.successful) {
          var sku_group = wx.getStorageSync('skuGroup');
          console.log(sku_group);
          // wx.removeStorageSync("orderType");
          wx.removeStorageSync("deliveryAddressId");
          var cartOrderFlag = that.data.cartOrderFlag,
              carIdGroupString = that.data.carIdGroupString;
          /*if (cartOrderFlag){
            wx.redirectTo({
              url: '../confirmOrder/confirmOrder?skuGroup=' + sku_group + '&otherPage=' + 1 + '&del=' + 1 + '&cartOrderFlag=' + cartOrderFlag + '&carIdGroupString=' + carIdGroupString
            })
          }else{
            wx.redirectTo({
              url: '../confirmOrder/confirmOrder?skuGroup=' + sku_group + '&otherPage=' + 1 + '&del=' + 1
            })
          }*/
          //返回到订单页面
          wx.navigateBack({
            delta:2
          });
          
        } else {

        }

      }
    })
    
  },
  //打开弹窗
  openAddressXbox: function (e) {
    var order_id = e.currentTarget.dataset.id;
    console.log("11");
    this.setData({
      confirmReceipt: true,
      orderId: order_id
    })
  },
  //关闭确认收货弹窗
  closeReceiptXbox: function (e) {
    this.setData({
      confirmReceipt: false,
    })
  },
  closexbox: function (e) {
    this.setData({
      confirmReceipt: false,
    })
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
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})