//购物车
var util = require('../../utils/util.js')
var dialog = require("../../utils/dialog")
var constant = require('../../constant')
var app = getApp()
var url = constant.devUrl + '/miniapp/car/shopCarList.json';

/**
 * 登录获取用户信息
 */
var loginGetUserData = function (that) {
  var formValue = 0;
  app.loginAuthorizeFun(formValue, function (id) {
    var sessionId = wx.getStorageSync("sessionId");
    that.setData({
      memberId: id
    })
    initData(that, id, sessionId);
  })
}

/**
 * 初始化数据
 */
var initData = function (that, memberId, sessionId) {
  var store_Id = wx.getStorageSync('storeId');
  dialog.loading();
  var sendData = {
    memberId: memberId,
    storeId: store_Id
  }
  var sign = util.MD5(util.paramConcat(sendData));
  console.log("提交的数据",sendData);
  wx.request({
    url: url, //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': sessionId,
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      console.log("返回的数据", res, res.data.data);
      if (res.data.successful) {
        that.setData({
           cartsData: res.data.data,
           selectAllStatus:false
        })
        that.calculateTotalPrice(); //tabbar菜单切换时，需重新计算值，否则会保留之前选中的值
      } else {
        dialog.toastError(res.data.error)
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
 * 库存不足时，动态改变所有数据显示"库存不足"状态
 */
function changeStockState(that, noStockData){
  var cartsData = that.data.cartsData;
  console.log(cartsData, noStockData);
  for (var i = 0, l = cartsData.length; i < l; i++){
    for (let j = 0, h = noStockData.length; j < h; j++) {
      if (cartsData[i].carId == noStockData[j].cardId){
        cartsData[i].isStock = true
      }else{
        cartsData[i].isStock = false
      }
    }
  }
  that.setData({
    cartsData: cartsData
  })
}

/**
 * 校验库存不足
 */
function judgeShopCartStock(that, carIdGroupString, callback){
  var sendData = {
    memberId: that.data.memberId,
    storeId: that.data.storeId,
    carId: carIdGroupString
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log("提交的数据", sendData);
  var stockUrl = constant.devUrl + '/miniapp/car/judgeCardsInventory.json';
  wx.request({
    url: stockUrl, //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("返回的数据", res);
      if (res.data.successful) {
        callback(res.data)
      } else {
        dialog.toastError(res.data.error);
        setTimeout(function(){
          changeStockState(that,res.data.data);
        },3000)
      }
    }
  })
}
Page({
  data: {
    memberId:'',               //会员id
    storeId:'',                //门店
    checkedFalg: false,        //选中状态
    totalPrice: 0,            // 总价格，初始为0
    cartsData: '',            //购物车数据   
    hasList: false,              // 购物车列表是否有数据
    selectedTotalCount: 0,     //选择的购买总件数
    selectAllStatus: false,       // 全选状态，默认全选
    deleteCarid: '',            //确认删除的商品carid
    deleteIndex: ''            //确认删除的商品列表索引
  },
  onLoad: function () {
    var that = this;
    //获得确定框组件
    this.confirmPopup = this.selectComponent("#deletePopup");
  },
  onShow:function(){
    var that = this;
    //获取会员id,初始化数据
    loginGetUserData(that);
    console.log(that.data.cartsData);
    this.calculateTotalPrice();  // 重新获取总价
    var store_id = wx.getStorageSync('storeId'),
        member_id = wx.getStorageSync('id');
    that.setData({
      memberId: member_id,
      storeId: store_id,
    })   
  },
  onHide: function () {
    
  },
  //计算总价
  calculateTotalPrice:function(){
    let cartsData = this.data.cartsData;                  // 获取购物车列表数据
    let total = 0;
    let selectedNumber = 0;   //选中的购买总件数
    for (let i = 0; i < cartsData.length; i++) {             // 循环列表得到每个数据
      if (cartsData[i].selected) {                           // 判断选中才会计算价格
        total += cartsData[i].sku_number * cartsData[i].shopPrice;    // 所有价格加起来
        selectedNumber += cartsData[i].sku_number                 //购买的总件数
      }
    }
    this.setData({                                
      cartsData: cartsData,
      selectedTotalCount: selectedNumber,
      totalPrice: total.toFixed(2)
    });
  },
  //选中函数
  checkedFunc:function(e){
    const index = e.currentTarget.dataset.index;  
    let cartsData = this.data.cartsData;                 // 获取购物车列表
    console.log("cartsData11", cartsData, index, cartsData[index].selected);
    var selected = cartsData[index].selected;         // 获取当前商品的选中状态
    cartsData[index].selected = !selected;              // 改变状态
    this.setData({
      cartsData: cartsData
    });
    this.calculateTotalPrice();           // 重新获取总价
    var Allprice = 0,                     //全选价格
        carts = this.data.cartsData;      //购物车的数据
    for (let i = 0; i < carts.length; i++) {
      Allprice +=  carts[i].sku_number * carts[i].shopPrice;
      //console.log("选择Allprice", Allprice);
    }
    //当商品被全勾选时，全选按钮要选中
    if (Allprice == this.data.totalPrice){
      this.setData({
        selectAllStatus: true
      })
    }else{
      this.setData({
        selectAllStatus: false
      })
    }
    
  },
  //选择全部
  selectAll: function (e){
    let selectAllStatus = this.data.selectAllStatus;    // 是否全选状态
    selectAllStatus = !selectAllStatus;
    let cartsData = this.data.cartsData;
    for (let i = 0; i < cartsData.length; i++) {
      //改变上架的所有商品状态
      if (cartsData[i].sold_out == 1 && cartsData[i].car_suk_status == 1){
        cartsData[i].selected = selectAllStatus;  
      }
    }
    this.setData({
      selectAllStatus: selectAllStatus,
      cartsData: cartsData
    });
    this.calculateTotalPrice();  // 重新获取总价
  },
  // 修改购物车数量
  modifyCount: function (skuNumber, carId, callback){
    var sessionId = wx.getStorageSync("sessionId"),
        memberId = this.data.memberId,
        store_Id = wx.getStorageSync("storeId");
    var sendData = {
      memberId: memberId,
      storeId: store_Id,
      carId: carId,
      skuNumber: skuNumber
    }
    var sign = util.MD5(util.paramConcat(sendData));
    console.log("修改数量提交的数据", sendData);
    let deleteUrl = constant.devUrl + '/miniapp/car/updateCar.json';
    wx.request({
      url: deleteUrl, //接口地址
      data: sendData,
      header: {
        'wxa-sessionid': sessionId,
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log("修改数量返回的数据", res);
        if (res.data.successful) {
          callback();
        } else {
          dialog.toastError(res.data.error)
        }
      }
    })
  },
  // 增加购买数量
  addCount: function (e){
    var index = e.currentTarget.dataset.index,
        carId = e.currentTarget.dataset.carid;
    let cartsData = this.data.cartsData;
    let num = cartsData[index].sku_number;
    //有库存时
    num = num + 1;
    this.modifyCount(num, carId,function(){
      cartsData[index].sku_number = num;
      this.setData({
        cartsData: cartsData
      });
      this.calculateTotalPrice();
    });
  },
  // 减少购买数量
  reduceCount: function (e) {
    var index = e.currentTarget.dataset.index,
        carId = e.currentTarget.dataset.carid;
    let cartsData = this.data.cartsData;
    let num = cartsData[index].sku_number;
    if (num <= 1) {
      return false;
    }
    num = num - 1;
    cartsData[index].sku_number = num;
    this.modifyCount(num, carId);
    this.setData({
      cartsData: cartsData
    });
    this.calculateTotalPrice();
  },
  //跳转至商品详情页面
  gotoDetail: function (event) {
    var productid = parseInt(event.currentTarget.dataset.productid);
    util.gotoDetialRecord(productid);
  },
  //删除商品
  deleteProduct: function (index,carId){
    var that = this;
    let cartsData = this.data.cartsData;
    var sessionId = wx.getStorageSync("sessionId"),
        memberId = this.data.memberId,
        store_Id = wx.getStorageSync("storeId");
    var sendData = {
      memberId: memberId,
      storeId: store_Id,
      carId: carId
    }
    var sign = util.MD5(util.paramConcat(sendData));
    console.log("删除提交的数据", sendData);
    let deleteUrl = constant.devUrl + '/miniapp/car/deleteCarGoods.json';
    wx.request({
      url: deleteUrl, //接口地址
      data: sendData,
      header: {
        'wxa-sessionid': sessionId,
        'version': constant.version,
        'sign': sign
      },
      success: function (res) {
        console.log("删除返回的数据", res);
        if (res.data.successful) {
          cartsData.splice(index, 1);                 // 删除购物车列表里这个商品
          that.setData({
            cartsData: cartsData
          });
          that.confirmPopup.hidePopup();  
          if (cartsData.length == 0) {       // 如果购物车为空
            that.setData({
              hasList: false                  // 修改标识为false，显示购物车为空页面
            });
          } else {                          
            that.calculateTotalPrice();     // 重新计算总价格
          }
        } else {
          dialog.toastError(res.data.error)
        }
      }
    })
  },
  //上架时显示删除提示框
  confirmDelete: function (e) {
    var that = this;
    var index = e.currentTarget.dataset.index,
        carId = e.currentTarget.dataset.carid;
    let cartsData = this.data.cartsData;
    let productName = cartsData[index].productName;
    //弹窗文字过多截取
    var productNameLength = '';
    if (productName.length > 25){
      productNameLength = productName.slice(0, 25) + '...';
    }else{
      productNameLength = productName;
    }
    console.log('carId', carId);
    that.confirmPopup.showPopup();
    that.setData({
      productName: productNameLength,
      deleteCarid: carId,
      deleteIndex: index
    })
    
  },
  //上架时点击提示框确实按钮删除
  confirmDeleteFunc:function(){
    var index = this.data.deleteIndex,
        carId = this.data.deleteCarid;
    this.deleteProduct(index, carId);
  },
  //关闭删除提示框
  closexbox: function () {
    this.confirmPopup.hidePopup();
  },
  //下架时的删除函数
  directDelete:function(e){
    var index = e.currentTarget.dataset.index,
        carId = e.currentTarget.dataset.carid;
    this.deleteProduct(index,carId);
  },
  //提交跳转到确认订单
  confirmOrder: function () {
    var that = this;
    var carIdGroupArray = []; //carId数组
    var carId_group = '';     //单个商品carId组合
    // 循环列表得到每个数据
    let cartsData = this.data.cartsData;
    for (let i = 0; i < cartsData.length; i++) {
      // 判断选中
      if (cartsData[i].selected) {
        if (cartsData[i].carId == '') {
          cartsData[i].carId = 0;
        }
        carIdGroupArray.push(cartsData[i].carId);
      }
    }
    //var carIdGroupString = JSON.stringify(carIdGroupArray);
    var carIdGroupString = carIdGroupArray.join();
    console.log(carIdGroupString);
    if (!carIdGroupString) {
      dialog.toastError("请先选择商品");
      return
    }
    //校验库存不足
    judgeShopCartStock(that, carIdGroupString, function(data){
      console.log("返回的数据",data);
      if (data.data.length == 0){
        //跳转到确认订单页面,cartOrderFlag-在确认订单页面，用于区别调用不同的接口
        if (carIdGroupString) {
          wx.navigateTo({
            url: '/pages/component/confirmOrder/confirmOrder?cartOrderFlag=1&' + 'carIdGroupString=' + carIdGroupString
          })
        }
      }
    })
  }
})
