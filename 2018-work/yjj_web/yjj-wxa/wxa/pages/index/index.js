//index.js
var util = require('../../utils/util.js')
var constant = require('../../constant')
var dialog = require("../../utils/dialog")
var share = require("../../common/shareDown")
var app = getApp()
/**
 * 商品上拉加载更多通用请求函数
 */
var page = 2;
var page_size = 10;
var url = constant.devUrl + "/miniapp/homepage/homeProductList.json";
var getMoreList = function (that, typeId, member_id) {
  var store_Id = wx.getStorageSync('storeId');
  //当已经显示全部时，下拉不显示loading
  if (typeId == 0) {  //店长推荐
    if (that.data.moreOver1) {
      that.setData({
        hasMore1: false,
        moreComplete1: true
      })
    } else {
      that.setData({
        hasMore1: true,
        moreComplete1: false
      })
    }
  } else {
    if (that.data.moreOver2) {
      that.setData({
        hasMore2: false,
        moreComplete2: true
      })
    } else {
      that.setData({
        hasMore2: true,
        moreComplete2: false
      })
    }
  }
  var sendData = {
    "type": typeId,   //分类id
    current: page,        //当前是第几页
    size: page_size,     //每页显示条数
    storeId: store_Id,
    memberId: member_id
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: url,
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      //console.log(res, res.data.data.records);
      //判断是否有数据，有则取数据  
      if (res.data.data.records.length > 0) {
        var productList = res.data.data.records;
        if (typeId == 0) {  //店长推荐
          var list = that.data.shopProductData;
          for (var i = 0; i < productList.length; i++) {
            list.push(productList[i]);
          }
          that.setData({
            shopProductData: list,
            moreComplete1: true
          })
        } else {   //热销推荐
          var list = that.data.hotProductData;
          for (var i = 0; i < productList.length; i++) {
            list.push(productList[i]);
          }
          that.setData({
            hotProductData: list,
            moreComplete2: true
          })
        }
        page++;
        //没有数据了
      } else {
        //店长推荐
        if (typeId == 0) {
          that.setData({
            hasMore1: false,
            moreOver1: true,
            moreOver2: false,
            moreComplete1: true
          });
        } else {  //热销推荐
          that.setData({
            hasMore2: false,
            moreOver2: true,
            moreOver1: false,
            moreComplete2: true
          });
        }
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
 * 获取商品数据列表通用函数
 */
var getProductCommon = function (that, typeId, member_id, tabIndex) {
  that.setData({
    categoryOver: false
  })
  page = 2;
  var store_Id = wx.getStorageSync('storeId');
  var sendData = {
    "type": typeId, //分类id
    current: 1,        //当前是第几页
    size: 10,     //每页显示条数
    storeId: store_Id,
    memberId: member_id
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData, "sign", util.paramConcat(sendData),sign);
  dialog.loading();
  wx.request({
    url: url, //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("首页推荐：", res);
      var data = res.data.data,
        productList = '',
        seccussFlag = res.data.successful;
      //是否是500，没有records的情况 （判断服务器500的情况）
      if (res.statusCode == 500) {
        //如果店长推荐有500或者没有records字段时，设置是否有商品的状态
        if (typeId == 0) {
          that.setData({
            isShowProductMode1: 0
          })
        } else {
          that.setData({
            isShowProductMode2: 0
          })
        }
      } else {
        if (data.records) {
          productList = data.records;
        }
      }
      //有商品时   
      if (seccussFlag && productList.length > 0) {
        if (typeId == 0) {  //店长推荐
          //如果点击热销推荐无商品时，店长推荐默认选中
          if (that.data.isShowProductMode2 == 0) {
            that.setData({
              isShowProductMode1: 1,
              tabState1: true
            })
          }
          that.setData({
            shopProductData: productList,
            isShowProductMode1: 1,
            tabState1: true,
            tabState2: false,
            shopNoProduct: data.isMore
          })
          //是否有更多商品
          if (data.isMore > 0) {
            that.setData({
              moreOver1: false
            })
          }
          //console.log("店长推荐有商品", productList);
          //console.log("店长推荐页面有数据的状态tabState1、isShowProductMode1", that.data.tabState1, that.data.isShowProductMode1);
        } else { //热销推荐
          //如果店长推荐无商品时，热销推荐默认选中
          if (that.data.isShowProductMode1 == 0) {
            that.setData({
              tabState2: true
            })
          } else {
            that.setData({
              tabState2: false,
            })
            //当热销推荐的按钮被点击时，热销得选中
            if (tabIndex == 1) {
              that.setData({
                tabState2: true,
              })
            }
          }
          that.setData({
            hotProductData: productList,
            isShowProductMode2: 1,
            hotNoProduct: data.isMore
          })
          //console.log("热销有商品", productList);
          //console.log("热销推荐页面有数据的状态tabState2、isShowProductMode2", that.data.tabState2, that.data.isShowProductMode2);
          //是否有更多商品
          if (data.isMore > 0) {
            that.setData({
              moreOver2: false,
            })
          }
        }
      } else {  //无商品时
        if (typeId == 0) {
          that.setData({
            isShowProductMode1: 0,
            tabState1: false
          })
          //console.log("111",that.data.isShowProductMode1);
        } else {
          that.setData({
            isShowProductMode2: 0,
            tabState2: false
          })
        }
        //dialog.toast(res.data.error)
        //console.log("页面切换时回首页无数据的状态", that.data.tabState1, that.data.isShowProductMode1, that.data.tabState2, that.data.isShowProductMode2);
      }
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 500)
    }
  })
}
/**
 * 倒计时时间格式化输出通用函数
 * @param  minute - 秒数
 **/
function timeFormat(minute) {
  var totalSeconds = minute;    // 总秒数
  var h = Math.floor(totalSeconds / 60 / 60),  // 小时
    m = Math.floor(totalSeconds / 60 % 60),       // 分钟
    s = Math.floor(totalSeconds % 60);            // 秒
  var timeObj = {
    hours: h,
    minute: m,
    second: s
  }
  //console.log(timeObj);
  return timeObj;
}
var activityGroupTimer = null;  //团购活动倒计时
var activitySecondTimer = null;  //秒杀活动倒计时
var activityGroupTimeout = false; //团购启动及关闭定时器  
var activitySecondTimeout = false; //秒杀启动及关闭定时器  
/**
 *  倒计时通用函数
 *  @param type  1-秒杀，2-团购
 **/
function activityTimeDown(milliseconds, that, member_id, activityType) {
  //秒杀
  if (activityType == 1) {
    var totalTimes = milliseconds / 1000;
    //console.log(totalTimes);
    if (totalTimes <= 0) {
      //更新活动数据
      getActivityData(that, member_id);
      clearInterval(activitySecondTimer);
      //activitySecondTimeout = true;
    } else {
      activitySecondTimer = setInterval(function () {
        //console.log("是否进来");
        totalTimes--;
        if (totalTimes <= 0) {
          //更新活数据
          clearInterval(activitySecondTimer);
          getActivityData(that, member_id);
        } else {
          let newData = {
            activitySecondTime: timeFormat(totalTimes)
          }
          that.setItem(newData);
        }
      }, 1000);
    }
  } else {  //团购
    var totalTimes = milliseconds / 1000;
    //console.log(totalTimes);
    if (totalTimes <= 0) {
      //更新活动数据
      getActivityData(that, member_id);
      clearInterval(activityGroupTimer);
      //activityGroupTimeout = true;
    } else {
      activityGroupTimer = setInterval(function () {
        //console.log("是否进来");
        totalTimes--;
        if (totalTimes <= 0) {
          //更新活数据
          clearInterval(activityGroupTimer);
          getActivityData(that, member_id);
        } else {
          let newData = {
            activityGroupTime: timeFormat(totalTimes)
          }
          that.setItem(newData);
        }
      }, 1000);
    }
  }
}
/**
 * 获取团购秒杀活动数据
 */
function getActivityData(that, member_id) {
  var store_id = wx.getStorageSync('storeId');
  var sendData = {
    storeId: store_id,
    memberId: member_id
  }
  console.log(sendData);
  var sign = util.MD5(util.paramConcat(sendData));
  //dialog.loading();
  wx.request({
    url: constant.devUrl + '/miniapp/homepage/homeActivity.json', //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("团购秒杀数据", res);
      var data = res.data.data;
      if (res.data.successful) {
        var activityGruopImgs = data.teamBuyActivity.shopProductShowcaseImgs;
        var activitySecondImgs = data.secondBuyActivity.shopProductShowcaseImgs;
        //console.log("团购秒杀图片", activityGruopImgs, activitySecondImgs);
        //团购的橱窗图片
        if (activityGruopImgs) {
          let newData = {
            activityGruopImgs: JSON.parse(activityGruopImgs)
          }
          that.setItem(newData);
          //console.log("团购图片", JSON.parse(activityGruopImgs), that.data.activityGruopImgs[0]);
        }
        //秒杀的橱窗图片
        if (activitySecondImgs) {
          let newData = {
            activitySecondImgs: JSON.parse(activitySecondImgs)
          }
          that.setItem(newData);
          //console.log("秒杀图片", JSON.parse(activitySecondImgs));
        }
        let activityData = {
          activityGruopData: data.teamBuyActivity,     //团购数据
          activitySecondData: data.secondBuyActivity,  //秒杀数据   
          activityGruopStatus: data.teamBuyActivity.activityStatus,     //团购的状态
          activitySecondStatus: data.secondBuyActivity.activityStatus     //秒杀的状态
        }
        that.setItem(activityData);
        //团购进度条计算
        if (data.teamBuyActivity.activityPrice) {
          //件数参团
          var width = '';
          if (data.teamBuyActivity.conditionType == 2) {
            var meetProductCount = parseInt(data.teamBuyActivity.meetProductCount),
              orderedProductCount = parseInt(data.teamBuyActivity.orderedProductCount);
           //differMeetProductCount = meetProductCount - orderedProductCount;
            if (orderedProductCount > meetProductCount) {
              orderedProductCount = meetProductCount;
            }
            width = (orderedProductCount / meetProductCount) * 702;
          } else {
            //人数成团-3.7.7和之前的版本是人数成团
            var userCount = parseInt(data.teamBuyActivity.userCount),
                activityMemberCount = parseInt(data.teamBuyActivity.activityMemberCount);
            if (activityMemberCount > userCount) {
              activityMemberCount = userCount;
            }
            width = (activityMemberCount / userCount) * 702;
          }
          console.log("计算进度条", width);
          let widthData = {
            activityProgressWidth: width,  //拼团获取进度条宽度
          }
          that.setItem(widthData);
        }
        var activityGroupTimes = '';  //团购时间
        var activitySecondTimes = '';  //秒杀时间
        //团购倒计时处理
        if (data.teamBuyActivity.activityPrice) {
          clearInterval(activityGroupTimer);
          //团购待开始
          if (data.teamBuyActivity.activityStatus == 1) {
            activityGroupTimes = data.teamBuyActivity.surplusStartTime
          }
          //团购进行中
          if (data.teamBuyActivity.activityStatus == 2) {
            activityGroupTimes = data.teamBuyActivity.surplusEndTime
          }
          //倒计时函数
          if (activityGroupTimes){
            activityTimeDown(activityGroupTimes, that, member_id, 2)
          }
        }
        //秒杀倒计时处理
        if (data.secondBuyActivity.activityPrice) {
          clearInterval(activitySecondTimer);
          //秒杀待开始
          if (data.secondBuyActivity.activityStatus == 1) {
            activitySecondTimes = data.secondBuyActivity.surplusStartTime
          }
          //秒杀进行中
          if (data.secondBuyActivity.activityStatus == 2) {
            activitySecondTimes = data.secondBuyActivity.surplusEndTime
          }
          if (activitySecondTimes){
            activityTimeDown(activitySecondTimes, that, member_id, 1)
          }
        }
      }
    }
  })
}
/**
 * 获取优惠券提醒函数  
 */
var getCouponData = function (that, member_id) {
  var param = {
    data: {
      memberId: member_id
    }
  }
  app.facade.waitShopCoupon(param).then(res => {
    //console.log("优惠券：", res);
    var data = res.data;
    //存储优惠券数量和金额
    wx.setStorageSync('couponData', data);
    var count = data.count,
        money = data.money,
        discount = data.discount,
        couponType = data.couponType,
        couponId = data.lastCouponTemplateId,
        localCount = wx.getStorageSync('couponNumber'),
        localMoney = wx.getStorageSync('couponMoneys'),
        localCouponId = wx.getStorageSync('lastCouponTemplateId'),
        localDiscount = wx.getStorageSync('discount');
    //console.log(1,localCount, localMoney);
    that.setData({
      allCoupon: data,
      couponCount: count,
      couponMoney: money
    })
    //优惠券提醒判断-优惠券数量有变更就提醒
    if (localCount != count) {
      //console.log(11, that.data.couponNoticeComplete);
      that.setData({
        couponNoticeComplete: true
      })
      if (that.data.couponNoticeComplete) {
        that.setData({
          noticeShow: true
        })
      }
    }
    //最近的优惠券模板ID变更
    else if (localCouponId != couponId) {
      //console.log(22);
      that.setData({
        couponNoticeComplete: true
      })
      if (that.data.couponNoticeComplete) {
        that.setData({
          noticeShow: true
        })
      }
    }
    else {
      that.setData({
        noticeShow: false,
        couponNoticeComplete: false
      })
    }
  });
}
/**
 * 获取轮播图、文章列表、公告、自定义导航
 */
function getInitData(that, member_id) {
  var param = {
    data: {
      memberId: member_id
    },
    loading: true
  }
  app.facade.home(param).then(res => {
    console.log("获取轮播图、文章列表、公告、自定义导航", res);
    var data = res.data;
    let newData = {
      //noticeData: data.storeNotice,
      storeDisplayImages: data.storeDisplayImages,
      articleList: data.articleList,
      hasHotonline: data.hasHotonline,
      hotOnline: data.hotOnline,
      navigationList: data.navigationList
    }
    that.setItem(newData);
  });
}
/**
 * 获取智能模块排序列表
 */
function getSmartModule(that, member_id) {
  var param = {
    data: {
      memberId: member_id
    }
  }
  console.log("只能门店param", param);
  app.facade.getSmartModule(param).then(res => {
    console.log("获取智能模块排序列表", res);
    var data = res.data;
    that.setData({
      sortData: data
    })
    //如果第一模块是门店图时，而且有图片和开启时,搜索框固定在图片上
    if (data[0].id == 5 && data[0].switcher == "1" && data[0].imgCount == 1){
      that.setData({
        firstSmartPicture: 1
      })
    }
    console.log("firstSmartPicture",that.data.firstSmartPicture);
  });
}
/**
 * 获取最新订单消息的数据
 */
function getNewOrderMessage(that, member_id) {
  var param = {
    data:{
      memberId: member_id
    }
  }
  app.facade.getOrderMessage(param).then(res => {
    console.log("获取最新订单消息数据", res, res.data);
     var newData = {
       noticeData: res.data
     }
     that.setItem(newData);
  });
}
var msgTimer;  //获取未读消息的定时器
Page({
  data: {
    //模块排序的数据
    sortData: [],
    //排序模块的数据
    itemData: {
      storeDisplayImages: [],    //门店滑动头图
      noticeData: '',    //公告信息
      navigationList: [],       //自定义标签导航数据
      articleList: [],    //文章列表
      activityProductName: '',   //拼团分享商品的名字
      activityProductId: '',   //拼团分享商品的id
      activityProgressWidth: '',   //拼团获取进度条宽度
      differMeetProductCount: '',     //团购还差成团件数
      activityGruopData: '',     //团购数据
      activitySecondData: '',     //秒杀数据
      activityGruopStatus: '',     //团购的状态
      activitySecondStatus: '',     //秒杀的状态
      activitySecondTime: {},     //秒杀的倒计时间
      activityGroupTime: {}    //团购的倒计时间
    },
    firstSmartPicture: 0,     //第一个智能模块是否是轮播图，等于1-是轮播图且显示，搜索框浮动在轮播图上，并透明显示
    searchStyleState: false,  //当页面滚动时，控制搜索框的样式，true-为显示不透明
    scrollTop: 0,
    shopProductData: [],   //店长推荐商品列表
    hotProductData: [],   //热销推荐商品列表
    shopNoProduct: 0,   //店长推荐是否有更多商品
    hotNoProduct: 0,   //热销推荐是否有更多商品
    isNoProductText: '', //提示文字
    typeId: '',         //tab切换的Id
    isShowProductMode1: 0,  // 是否显示店长推荐 1-有，0-没有
    isShowProductMode2: 0,  // 是否热销店长推荐 1-有，0-没有
    isStatusCode500: 1,     //同时请求店长、热销商品接口时，判断店长推荐请求是500，方便判断热销商品的显示处理 1-不是500,0-是500
    msgNumber: '',    //消息条数
    scrollHeight: 0,   //商品滚动列表高度
    tabFixedState: false, //导航是否置顶显示状态
    hasMore1: false,     //店长推荐底部加载更多loading控制
    moreComplete1: true, //店长推荐加载完成
    moreOver1: false,
    hasMore2: false,     //热销推荐底部加载更多loading控制
    moreComplete2: true, //热销推荐加载完成
    moreOver2: false,
    isTabMore: 1,     //控制不同tab列表上拉加载
    storeId: '',  //门店id 
    memberId: '',  //memberId 
    sessionId: '',
    goTopShowState: false, //置顶按钮显示状态
    msgShowState: true,   //消息按钮显示状态
    readFlag: false,   //是否清除未读消息
    isReadState: false, //是否清除未读消息
    noticeShow: false,   //优惠券领取提醒
    allCoupon: {},     //优惠券领取提醒的数据
    couponCount: '',    //优惠券数量
    couponMoney: '',    //优惠券金额
    couponNoticeComplete: true,  //首页优惠券仅显示一次
    tabState1: true,      //热销推荐tab
    tabState2: false,      //店长推荐tab
    hasHotonline: '',
    hotOnline: '',  //电话
    isShowSharePopup: false,      //是否显示分享朋友和朋友圈弹窗
    isShowSettingPopup: false,   //显示授权设置弹窗,
    shareSuccess: false,  //分享成功后的状态 true-成功后不刷新页面（成功后提示文字用）
    shareItem:{   
      targetType:'',     //区分普通和活动商品
      shareProductId:'',  //当前分享的商品id
      activityId:'',    //活动id
      price:'',        //商品价格
      shareProductName:'',  //商品标题
      shareImg:'',     //当前分享的商品图片
      isShareCode:''    //商品是否有分享图
    }
  },
  onReady: function () {
    //获得popup弹窗组件
    this.authPopup = this.selectComponent("#settingPopup");
    console.log("组件", this.authPopup);
  },
  onLoad: function () {
    var that = this;
    //获取门店id
    app.getThirdDefineField();
    var storeId = wx.getStorageSync('storeId');
    if (storeId){
      that.setData({
        storeId: storeId
      })
    }
  },
  onShow: function () { 
    console.log('版本号：', constant.version);
    var that = this;
    var storeId = that.data.storeId;
    console.log("门店id",storeId);
    //登录成功后
    app.loginAuthorizeFun(0,function (id) {
      var memberId = wx.getStorageSync('id');
      that.setData({
        memberId: id
      })
      //分享成功后,返回不刷新页面
      if (!that.data.shareSuccess) {
        //首次登录成功后提示优惠券
        getCouponData(that, id);
        console.log("id", id);
        //先获取数据判断是否显示订单信息模块
        getNewOrderMessage(that, id);
        //获取智能模块排序列表
        getSmartModule(that, id);
        //获取团购秒杀数据
        getActivityData(that, id);
      }
      //存储优惠券数据
      var couponData = wx.getStorageSync('couponData');
      //console.log(couponData);
      wx.setStorageSync('couponNumber', couponData.count);
      wx.setStorageSync('couponMoneys', couponData.money);
      wx.setStorageSync('couponType', couponData.couponType);
      wx.setStorageSync('discount', couponData.discount);
      wx.setStorageSync('lastCouponTemplateId', couponData.lastCouponTemplateId);
      //进入小程序就清空未读消息
      that.setData({
        isReadState: app.globalData.isReadState
      })
      if (that.data.isReadState && id) {
        util.setReadMsgNumber(that, storeId, id);
        that.setData({
          readFlag: false,
          isReadState: false
        })
      }
      //电话咨询
      util.getPhoneNumber(that, storeId, id);
    })
    var memberId = wx.getStorageSync('id');
    if (memberId) {
      memberId = wx.getStorageSync('id')
    } else {
      memberId = 0;
    }
    //如果只打开文章或者商品详情时，返回不重载首页数据
    if (app.globalData.isShowIndex) {
      //不重载首页数据
    } else {
      
      //分享成功后,返回不刷新页面
      if (!that.data.shareSuccess) {
        //获取轮播图、文章列表、公告
        getInitData(that, memberId);
        //初始获取商品数据列表
        getProductCommon(that, 0, memberId);
        getProductCommon(that, 1, memberId);
      }
    }
    app.globalData.isShowIndex = false;
    //设置会员未读消息为已读
    var readFlag = that.data.readFlag;
    if (readFlag && memberId) {
      util.setReadMsgNumber(that, storeId, memberId);
      that.setData({
        readFlag: false,
        isReadState: false
      })
    }
    //定时获取未读消息和最新订单消息
    if (memberId){
      var getMsgNumber = function () {
        util.timingGetMsgNumber(that, storeId, memberId);
        getNewOrderMessage(that, memberId); 
        msgTimer = setTimeout(getMsgNumber, 3000);
      }
      getMsgNumber();
    }
  },
  onHide: function () {
    var that = this;
    clearInterval(msgTimer);
    //wx.clearStorageSync('isCheckCoupon');
    wx.removeStorageSync('isCheckCoupon');
    //console.log(that.data.couponNoticeComplete);
    clearInterval(activityGroupTimer);
    clearInterval(activitySecondTimer);
  },
  onUnload:function(){
    clearInterval(msgTimer);
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    var member_id = wx.getStorageSync('id');
    //获取轮播图、文章列表、公告
    getInitData(that, member_id);
    //秒杀团购
    getActivityData(that, member_id)
    //商品列表
    getProductCommon(that, 0, member_id);
    getProductCommon(that, 1, member_id);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    var moreComplete1 = that.data.moreComplete1,
      moreComplete2 = that.data.moreComplete2,
      tabState1 = that.data.tabState1,
      tabState2 = that.data.tabState2;
    var member_id = that.data.memberId;
    if (member_id) {
      member_id = wx.getStorageSync('id')
    } else {
      member_id = 0;
    }
    //console.log(moreComplete);
    if (tabState1 && moreComplete1) {
      getMoreList(that, 0, member_id);
    }
    if (tabState2 && moreComplete2) {
      getMoreList(that, 1, member_id);
    }
  },
  //跳转至商品详情页面
  gotoDetail: function (event) {
    var productid = parseInt(event.currentTarget.dataset.id);
    util.gotoDetialRecord(productid);
  },
  //返回顶部
  goTop: function () {
    util.gotoTop()
  },
  //页面滑动tab置顶
  onPageScroll: function () {
    var that = this;
    var pageScrollHeight;
    var productScrollHeight;
    if (wx.createSelectorQuery()) {
      wx.createSelectorQuery().select('.scroll-main').boundingClientRect(function (rect) {
        pageScrollHeight = rect.top;
        //搜索框的显示控制
        if (pageScrollHeight < -50) {
          that.setData({
            searchStyleState: true
          })
          //console.log("搜索控制", pageScrollHeight, that.data.searchStyleState);
        } else {
          that.setData({
            searchStyleState: false
          })
          //console.log("搜索控制", pageScrollHeight, that.data.searchStyleState);
        }
        //置顶按钮是否显示控制
        if (pageScrollHeight < -100) {
          that.setData({
            goTopShowState: true
          })
        } else {
          that.setData({
            goTopShowState: false
          })
        }
      }).exec();
      wx.createSelectorQuery().select('.product-wrap').boundingClientRect(function (rect) {
        productScrollHeight = rect.top
        if (productScrollHeight < -10) {
          that.setData({
            tabFixedState: true
          })
        } else {
          that.setData({
            tabFixedState: false
          })
        }
      }).exec()
    }
  },
  //点击设置未读消息为已读
  clearMsgNumber: function () {
    var that = this;
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    util.setReadMsgNumber(that, store_id, member_id);
    that.setData({
      msgNumber: 0,
      readFlag: true
    })
  },
  //搜索框跳转函数
  searchFunc: function (e) {
    console.log("11")
    wx.navigateTo({
      url: '../component/searchList/searchList?index=' + 1
    })
  },
  //拨打电话
  makePhone: function (e) {
    var that = this;
    var store_id = wx.getStorageSync('storeId');
    var member_id = wx.getStorageSync('id');
    util.getPhoneNumber(that, store_id, member_id)
    util.makePhone(e)
  },
  //关闭优惠券提醒弹窗
  closeNoticeBox: function () {
    var that = this;
    that.setData({
      noticeShow: false,
      couponNoticeComplete: false
    })
  },
  //跳转至领券中心
  gotoCouponCenter: function () {
    var that = this;
    wx.navigateTo({
      url: '../component/couponCenter/couponCenter'
    })
    that.setData({
      noticeShow: false,
      couponNoticeComplete: false
    })
  },
  //商品tab切换函数
  getTabData: function (e) {
    var tabIndex = e.target.dataset.tab;    //获取被点击的tab 
    var that = this;
    var member_id = wx.getStorageSync('id');
    if (tabIndex == 0) {  //店长推荐
      that.setData({
        tabState1: true,
        tabState2: false,
        moreOver1: false,
        moreOver2: false,
        hasMore2: 0,
        typeId: 0,
        isTabMore: 1
      })
      getProductCommon(that, 0, member_id, tabIndex);
    } else {  //热销推荐
      that.setData({
        tabState1: false,
        tabState2: true,
        moreOver1: false,
        moreOver2: false,
        hasMore1: 0,
        typeId: 1,
        isTabMore: 0
      })
      getProductCommon(that, 1, member_id, tabIndex);
    }
  },
  //秒杀马上抢购按钮函数
  gotoBuy: function (e) {
    var activityData = this.data.itemData,
        shopProductId = activityData.activitySecondData.shopProductId,
        activityId = activityData.activitySecondData.activityId,
        activityStatus = activityData.activitySecondData.activityStatus,
        param = '';
    //待开始不弹出sku选择弹窗    
    if (activityStatus == 1){
      param = "?productId=" + shopProductId + "&activityId=" + activityId + "&targetType=2";
    }else{
      param = "?productId=" + shopProductId + "&activityId=" + activityId + "&targetType=2" + '&isOpenSkuBox=1';
    }    
    wx.navigateTo({
      url: '../component/detail/detail' + param
    })
  },
  // 设置item数据
  setItem: function (data) {
    var oldDataItem = this.data.itemData;
    // 防止覆盖掉新的数据 使用循环的方式
    for (var name in data) {
      oldDataItem[name] = data[name];
    }
    this.setData({
      itemData: oldDataItem
    });
    //console.log("设置的数据",this.data);
  },
  //跳转至文章详情
  gotoArticleDetail:function(e){
    var articleId = e.target.dataset.id,
        url = '../component/articleDetail/articleDetail?articleId=' + articleId;
    wx.navigateTo({
      url: url
    })
  },
  //跳转至团购列表
  gotoTeamBuyActivity: function (e) {
    var articleId = e.target.dataset.id,
      url = '../component/teamBuyActivity/teamBuyActivity';
    wx.navigateTo({
      url: url
    })
  },
  //跳转至秒杀列表
  gotoSecondBuyActivity: function (e) {
    var articleId = e.target.dataset.id,
      url = '../component/secondBuyActivity/secondBuyActivity';
    wx.navigateTo({
      url: url
    })
  },
  //跳转至我的颜值分页面
  gotoMycoin:function(){
    wx.navigateTo({
      url: '../component/myCoin/myCoin'
    })
  },
  /**
   * 定义页面可转发的函数-团购邀请好友
   * @param type: 1-天天拼团的邀请好，2-推荐商品的分享
   * @param targetType: 0-未参与活动的商品、1-表示参与团购活动的商品、2-表示参与秒杀活动的商品
   * @param shareType-用于区别首页拼团邀请和商品列表的分享 2-商品列表、 1-拼团邀请
   */
  onShareAppMessage: function (res) {
    var that = this;
    var shareShopProductId = '',
        shareTitle = '',
        shareImageUrl = '',
        targetType = '',
        memberId = that.data.memberId,
        storeId = that.data.storeId,
        activityId = '';
    //wx.hideShareMenu();
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target);
      var shareType = res.target.dataset.type;
      //分享商品列表
      if (shareType == '2'){
        var shareItem = that.data.shareItem;
        targetType = shareItem.targetType,
        shareShopProductId = shareItem.shareProductId,
        shareTitle = shareItem.shareProductName,
        shareImageUrl = shareItem.shareImg,
        activityId = shareItem.activityId;
      }else{ //天天拼团
        var itemData = that.data.itemData;
        shareShopProductId = itemData.activityGruopData.shopProductId,
        shareTitle = itemData.activityGruopData.shopProductName,
        shareImageUrl = itemData.activityGruopImgs[0],
        activityId = itemData.activityGruopData.activityId;
      }
      let shareParam = '';
      //天天拼团、秒杀商品
      if (activityId && targetType){
        shareParam = '?productId=' + shareShopProductId + '&shareFlag=1' + "&activityId=" + activityId + "&targetType=" + targetType + '&sourceUserId=' + memberId;
      }else{
        //普通商品
        shareParam = '?productId=' + shareShopProductId + '&shareFlag=1' + '&sourceUserId=' + memberId;
      }
      return {
        title: shareTitle,
        path: '/pages/component/detail/detail' + shareParam,
        imageUrl: shareImageUrl,
        success: function (res) {
          console.log(res,"aaaa");
          //商品列表的分享
          if (shareType) {
            that.setData({
              shareSuccess: true,
              isShowSharePopup: false
            })
            var param = {
              type: 2,
              targetId: shareShopProductId
            }
            //分享函数
            app.common.shareFunc(param, function (data) {
             // console.log("data.isOutOfMax", data.isOutOfMax);
              //当isOutOfMax为true时，已经超过最大分享收益次数,不给颜值分和佣金、金币
              dialog.toastError("分享成功！");
              var coinNumber = '',
                text = '',
                typeSwitch = data.typeSwitch;
              //现金    
              if (typeSwitch == 1) {
                coinNumber = data.earningsCash;
                text = "元"
              } else {
                coinNumber = data.earningsGoldCoin;
                text = "金币"
              }
              if (!coinNumber) {
                coinNumber = 0;
              }
              if (!data.isOutOfMax) {
                let promptText = "恭喜您分享成功，获得" + coinNumber + text;
                if (coinNumber > 0) {
                  dialog.toastError(promptText);
                }
              }
            });  
          }
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
  //显示分享和下载popup
  showSharePopup: function (e) {
    var that = this,
      target = e.target;
    share.setShareData(that, target)
  },
  //关闭分享和下载popup
  hideSharePopup: function () {
    var that = this;
    that.setData({
      isShowSharePopup: false
    })
  },
  //显示设置权限弹窗
  showSettingPopup: function () {
    var that = this;
    console.log(0);
    this.authPopup.showPopup();
  },
  //隐藏设置权限弹窗
  hideSettingPopup: function () {
    var that = this;
    this.authPopup.hidePopup();
  },
  //获取合成的分享图片
  getDownShareImg: function () {
    var that = this;
    share.getComposeShareImg(that)
  },
  //跳转至签到页面
  gotoSignPage: function () {
    var url = '../component/sign/sign';
    app.common.judgeNavigateTo(url)
  }
})
