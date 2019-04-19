// 我的优惠券
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var app = getApp()
var codeTimer;  //定时器
/**
 * 上拉加载更多通用请求函数
 */
var page = 2;
var page_size = 10;
var getMoreList = function (that, validityType) {
    var store_Id = that.data.storeId;
    var member_id = that.data.memberId;
    var sendData = {};
    //当已经显示全部时，下拉不显示loading
    if (that.data.moreOver) {
        that.setData({
            hasMore: false,
            moreComplete: true
        })
    } else {
        //console.log(1);
        that.setData({
            hasMore: true,
            moreComplete: false
        })
    }
    sendData = {
        current: page,        //当前是第几页
        size: page_size,     //每页显示条数
        memberId: member_id,
        storeId: store_Id,
        isValidityEnd: validityType
    }
    //console.log(sendData);
    var coupon_url = app.facade.myCouponListApi,
        sign = util.MD5(util.paramConcat(sendData));
    wx.request({
        url: coupon_url,
        data: sendData,
        header: {
            'version': constant.version,
            'sign':sign
        },
        success: function (res) {
            //console.log(res);
            //判断是否有数据，有则取数据
            if (res.data.data && res.data.data.memberCouponList.records.length > 0) {
                var list = that.data.couponData;
                var couponList = res.data.data.memberCouponList.records;
                //console.log(couponList);
                for (var i = 0; i < couponList.length; i++) {
                    list.push(couponList[i]);
                }
                that.setData({
                    couponData: list,
                    moreComplete: true
                });
                page++;
                //没有数据了
            } else {
                that.setData({
                    hasMore: false,
                    moreOver: true,
                    moreComplete: true
                });
            }
        },
        complete: function () {
            //隐藏loadding
            setTimeout(function () {
                that.setData({
                    hasMore: false
                });
            }, 1000)
        }
    });
}
/**
 * 初始化获取优惠券列表数据
 * @param validityType   0：已过期， 1：可用的， 2：已使用
 */
var initCouponData = function (that, validityType){
    page = 2;
    var store_Id = that.data.storeId,
        member_Id = that.data.memberId;
    var param = {
        data:{
            current: 1,         //当前是第几页
            size: 10,           //每页显示条数
            isValidityEnd: validityType,
            memberId: member_Id,
            storeId: store_Id
        },
        loading:true
    }
    app.facade.getMyCouponList(param).then(res => {
        var data = res.data,
            couponList = data.memberCouponList.records;
        //有数据    
        if (couponList.length > 0){
          that.setData({
            isCoupon:false,
            couponData: couponList,
            moreOver: false
          })
        }else{
          that.setData({
            isCoupon: true
          })
        }
    })
}
/**
 * 获取我的优惠券类型的数量
 */
function getCouponNumber(that){
  var store_Id = that.data.storeId,
      member_Id = that.data.memberId;
  var param = {
    data:{
      memberId: member_Id,
      storeId: store_Id
    }
  }
  app.facade.getMyCouponNumber(param).then(res => {
    var data = res.data;
    console.log(data);
    that.setData({
      noUseCount: data.noUser,    
      useCount: data.alreadyUser,    
      overdueCount: data.outTime
    })
  })
}
Page({
    data: {
        storeId: '',            //门店id
        memberId: '',           //会员id
        couponData:[],          //可用优惠券列表数据
        couponOwnData: {},   //查看优惠券信息数据
        isTabMore: 1,        //控制列表上拉加载
        tabState1:true,    //tab切换状态，未使用
        tabState2:false,   //已使用
        tabState0: false,  //已过期
        noUseCount:'',    //未使用优惠券数量
        useCount: '',    //已使用优惠券数量
        overdueCount: '',    //已过期优惠券数量
        isCoupon: false,     //是否有优惠券数据
        hasMore: false,  //底部加载更多loading控制
        moreComplete: true, //加载完成
        moreOver: false,   //更多
        boxShow:false,   //查看优惠券盒子是否显示
        codeImgUrl:'',   //优惠券二维码
        invalidPrompt:false, //二维码失效后提示文字
        codeImgState:true,   //二维码显示
        refleshBtn:false,    //点击刷新按钮
        couponId:''         //优惠券id
    },
    onLoad: function () {
        var that = this;
    },
    onShow: function () {
        var that = this,
            store_Id = wx.getStorageSync('storeId'),
            member_Id = wx.getStorageSync('id');
        that.setData({
            storeId: store_Id,
            memberId: member_Id
        })
        //初始化状态
        that.setData({
          tabState1: true,
          tabState2: false,
          tabState0: false,
          couponData: [],
          isCoupon: false,
          moreOver: false,
          isTabMore: 1
        })
        getCouponNumber(that);
        initCouponData(that,1)
    },
    //下拉刷新
    onPullDownRefresh: function () {
        var that = this;
      that.setData({
        tabState1: true,
        tabState2: false,
        tabState0: false,
        moreOver: false, 
        isCoupon: false
      })
      getCouponNumber(that);
      initCouponData(that, 1);
        setTimeout(function () {
            wx.stopPullDownRefresh()
        }, 500)
    },
    //上拉加载更多
    onReachBottom: function () {
        var that = this;
        that.setData({
          isCoupon: false
        })
      var isTabMore = that.data.isTabMore;
        if (!that.data.isCoupon){
          var moreComplete = that.data.moreComplete;
          if (moreComplete) {
            getMoreList(that, isTabMore);
          }
        }
    },
    //未使用、已使用、已过期、tab切换(0：已过期， 1：可用的， 2：已使用)
    tabFun: function (e) {
        var that = this,
            tab = e.currentTarget.dataset.tab,
            clearCouponData = that.data.couponData;
        clearCouponData.length = 0;
        //未使用
        if (tab == 1) {
            that.setData({
              tabState1: true,
              tabState2: false,
              tabState0: false,
              couponData: clearCouponData,
              isCoupon: false,
              moreOver: false, 
              isTabMore:1
            })
            initCouponData(that, 1)
        }
        //已使用
        else if (tab == 2) {
            that.setData({
                tabState1: false,
                tabState0: false,
                tabState2: true,
                couponData: clearCouponData,
                isCoupon: false,
                moreOver: false, 
                isTabMore:2
            })
            initCouponData(that, 2)
        }
        //已过期
        else if (tab == 0) {
            that.setData({
                tabState2: false,
                tabState1: false,
                tabState0: true,
                moreOver: false, 
                couponData: clearCouponData,
                isCoupon: false,
                isTabMore:0
            })
            initCouponData(that, 0)
        }
      getCouponNumber(that)
    },
    //三分钟二维码失效
    codeImgInvalid:function() {
        var that = this;
        //三分钟二维码失效
        codeTimer = setTimeout(function () {
            //显示失效提示文字
            that.setData({
                invalidPrompt: true,
                refleshBtn: true,
                codeImgState: false
            })
        }, 183000)
    },
    //查看优惠券详情弹窗
    checkDetail: function (e) {
        var that = this;
        var coupon_id = e.currentTarget.dataset.id,
            statusStr = e.currentTarget.dataset.status;
        console.log(statusStr);   
        //可用才核销
        if (statusStr == 0){
          var store_id = wx.getStorageSync('storeId');
          var member_id = wx.getStorageSync('id');
          this.setData({
              boxShow: true
          })
          var sentData = {
              id: coupon_id,
              memberId: member_id,
              storeId: store_id
          };
          //console.log(sentData);
          var sign = util.MD5(util.paramConcat(sentData));
          var detaiUrl = constant.devUrl + "/miniapp/coupon/getMemberCouponInfo.json";
          wx.request({
              url: detaiUrl,
              data: sentData,
              header: {
                  'version': constant.version,
                  'sign':sign
              },
              success: function (res) {
                  //console.log(res.data);
                  var imgUrl = constant.devUrl + "/miniapp/coupon/qrcode/src.do?storeId=" + store_id + "&memberId=" + member_id + "&memberCouponId=" + coupon_id+"&width=400&height=400";
                  that.setData({
                      couponOwnData: res.data.data.shopMemberCoupon,
                      codeImgUrl:imgUrl,
                      couponId: coupon_id
                  })
                  that.codeImgInvalid();
              }
          })
        }    
    },
    //关闭盒子
    closeBox:function(){
        var that = this;
        //隐藏提示文字
        that.setData({
            invalidPrompt: false,
            codeImgState: true,
            refleshBtn: false,
            boxShow: false,
            codeImgUrl: ''
        })
        initCouponData(that, 1)
        clearTimeout(codeTimer)
    },
    //点击刷新优惠券二维码
    refleshCode: function () {
        var that = this;
        var store_id = wx.getStorageSync('storeId');
        var member_id = wx.getStorageSync('id');
        var coupon_id = that.data.couponId;
        var rand = Math.random();
        var imgUrl = constant.devUrl + "/miniapp/coupon/qrcode/src.do?storeId=" + store_id + "&memberId=" + member_id + "&memberCouponId=" + coupon_id + "&width=400&height=400&rand=" + rand;
        //隐藏提示文字
        that.setData({
            invalidPrompt: false,
            codeImgState: true,
            codeImgUrl: imgUrl,
            refleshBtn: false
        })
        that.codeImgInvalid();
    },
    //跳转到领券中心
    gotoCouponCenter:function(){
        wx.navigateTo({
            url: '../couponCenter/couponCenter'
        })
    },
    //跳转至可用优惠券商品
    gotoCouponProduct:function(e){
      var shopCouponId = e.currentTarget.dataset.id;
        wx.navigateTo({
          url: '../couponProduct/couponProduct?shopCouponId=' + shopCouponId
        })
    }
})