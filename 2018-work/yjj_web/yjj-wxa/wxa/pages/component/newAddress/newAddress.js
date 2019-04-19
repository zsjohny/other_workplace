
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var show = false;
var item = {};
Page({
  data: {
    name: '',
    Phone: '',
    detailAddress: '',
    provices: '',
    citys: '',
    countys: '',
    region: [],
    customItem: '',
    submitClickFlag: 1,   //修改提交按钮标识
    btnDisabled: true,    //保存提交按钮
    carIdGroupString: '', //购物车订单的参数,返回确认订单页面初始化时需要
    cartOrderFlag: '',   //购物车订单的标识，返回确认订单页面初始化时需要区别不同的接口
    noAddressFlag: ''     //当没有地址时，返回确认订单页面层级标识  
  },
  onLoad: function (options) {
    this.setData({
      cartOrderFlag: options.cartOrderFlag,
      carIdGroupString: options.carIdGroupString,
      noAddressFlag: options.noAddressFlag
    })
  },
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

  // 保存的按钮
  saveSubmit: function () {
    console.log(this.data.region)
    var areas = '',
      that = this;
    if (that.data.submitClickFlag == 0) { return }

    var memberId = wx.getStorageSync('id'),
      storeId = wx.getStorageSync('storeId'),
      sku_group = wx.getStorageSync('skuGroup');
    areas = this.data.region[0] + '-' + this.data.region[1] + '-' + this.data.region[2];
    console.log(this.data.name);
    var submitData = {},
      name = this.data.name,
      Phone = this.data.Phone,
      detailAddress = this.data.detailAddress,
      reg_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/;
    if (!name) {
      wx.showToast({
        title: '输入姓名为空,请填写',
        icon: 'success',
        duration: 1500
      })
      return
    } else if (!Phone) {
      wx.showToast({
        title: '输入手机号码为空,请填写',
        icon: 'success',
        duration: 1500
      })
      return
    } else if (!reg_phone.test(Phone)) {
      wx.showToast({
        title: '手机号码有误，请输入有效手机号',
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
      memberId: memberId,
      storeId: storeId
    };
    var sign = util.MD5(util.paramConcat(submitData));
    console.log('保存地址', submitData)
    //禁用提交订单按钮
    that.setData({
      btnDisabled: false
    });
    var submitUrl = constant.devUrl + "/shop/shopMemberDeliveryAddress/addDeliveryAddress.json";
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
          console.log(sku_group);
          var deliveryAddressId = res.data.jsonResponse.data.deliveryAddressId
          that.setData({
            submitClickFlag: 0
          })
          var cartOrderFlag = that.data.cartOrderFlag,
              carIdGroupString = that.data.carIdGroupString;
          //返回到订单页面
          var noAddressFlag = that.data.noAddressFlag;
          //确认订单页面直接跳-新增地址页面是，返回一级
          if (noAddressFlag){
            wx.navigateBack({
              delta:  1
            });
          }else{
            //确认订单页面-地址列表-新增编辑地址页面，返回2级
            wx.navigateBack({
              delta: 2
            });
          }
        }
      }
    })
  },
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  }
})