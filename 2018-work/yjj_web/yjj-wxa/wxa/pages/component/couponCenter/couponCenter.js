// 领券中心
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
/**
 * 获取优惠券数据列表函数
 */
var numb = {};
var getAllCoupon = function (that) {
    var param = {
        data:{
            memberId: wx.getStorageSync('id'),
            storeId: wx.getStorageSync('storeId')
        },
        loading:true
    }
    app.facade.getCouponCenterData(param).then(res =>{
        var couponList = res.data;
    console.log(res);
    numb.couponList = couponList;
    if (couponList.length > 0) {
        that.setData({
            totalNumber: couponList.length,
            couponData: couponList,
            hasCoupon: true
        });
    } else {
        that.setData({
            hasCoupon: false
        })
    }
})
}
/**
 * 领取所有优惠券的函数
 */
var takeAllCoupon = function(that) {
    var listId = [];
    for (var i in numb.couponList){
        listId += numb.couponList[i].id + ",";
    }
    //console.log(listId);
    var param = {
        data:{
            memberId: that.data.memberId,
            storeId: that.data.storeId,
            shopCouponTemplateIds: listId
        }
    }
    //先判断网络
    app.common.judgeNetwork(function(){
      app.facade.getAllShopCoupon(param).then(res => {
        var successNumber = res.data.getSuccessCount;
        var text = "恭喜你成功获得" + successNumber + "张优惠券";
        dialog.toastError(text);
        that.setData({
          successNumber: successNumber,
          hasCoupon: false
        })
      })  
    })
}
/**
 * 领取单张惠券的函数
 */
var takeSingleCoupon = function (that, couponId) {
    //console.log(couponId);
    var param = {
        data: {
            memberId: that.data.memberId,
            storeId: that.data.storeId,
            tempId: couponId
        }
    }
    //先判断网络
    app.common.judgeNetwork(function(){
      app.facade.getSingleShopCoupon(param).then(res => {
        var successNumber = res.data.getSuccessCount,
          text = "恭喜你成功获得" + successNumber + "张优惠券";
        dialog.toastError(text);
        setTimeout(function () {
          //刷新列表数据
          getAllCoupon(that);
        }, 1500)
      })
    }) 
}
Page({
    data: {
        memberId:'',      //会员id
        storeId:'',      //门店id
        couponData: [],  //优惠券列表
        hasCoupon: true,  //是否有优惠券
        totalNumber:'',          //总共数量
        successNumber:'' //领取成功的数量
    },
    onLoad: function () {
        var that = this;
        that.setData({
            memberId: wx.getStorageSync('id'),
            storeId: wx.getStorageSync('storeId')
        })
        getAllCoupon(that);
    },
    onShow: function () {

    },
    onHide:function(){

    },
    //领取所有优惠券
    getAllCoupon:function(){
        var that = this;
        //检查网络
        app.common.judgeNetwork(function(){
            takeAllCoupon(that);
        })
    },
    //领取单张优惠券
    getSingleCoupon:function(e){
        var that = this,
            id = e.currentTarget.dataset.id;
        //检查网络
        app.common.judgeNetwork(function () {
            takeSingleCoupon(that, id);
        })
    },
    //跳转至我的优惠券
    gotoMyCoupon:function(){
        var url = '../myCoupon/myCoupon';
        wx.redirectTo({
            url: url
        })
    }
})