//商品详情
var app = getApp(),
    dialog = require("../../../utils/dialog"),
    constant = require('../../../constant'),
    util = require("../../../utils/util"),
    share = require("../../../common/shareDown");
/**
 * @des  收藏通用请求函数
 * @param  memberId-会员id
 * @param  productId-商品id
 * @param  productType-0商品
 * @param  url-接口地址
 * @param  collectState-是否收藏的状态
 **/
var addUrl = constant.devUrl + "/miniapp/member/favorite/update.json"; 
var collectRequest = function (that,memberId, productId, productType, collectState){
  console.log(collectState,memberId, productId);
  var store_Id = wx.getStorageSync('storeId');
  var sendData = {};
  //是否有会员id
  if (memberId && productId && productType) { 
    sendData={
        memberId: memberId,
        productId: productId,
        type: productType,
        status: collectState,
        storeId: store_Id
    }
  }else if (!memberId && productId && productType){
    sendData = {
      productId: productId,
      type: productType,
      status: collectState,
      storeId: store_Id
    }
  }
  var sign = util.MD5(util.paramConcat(sendData));
  console.log(sendData);
  wx.request({
    url: addUrl, //接口地址
    data: sendData,
    header: {
      'wxa-sessionid': wx.getStorageSync("sessionId"),
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      console.log(res.data);
      var status = res.data.data.status;
      if (res.data.successful && status ==0) {
        dialog.toast("收藏成功")
      }else {
        dialog.toast("取消收藏")
      }
      that.setData({
        collectState: status
      })
    },
    fail:function(errer){
      dialog.toast(errer)
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide()
      }, 1500)
    }
  })
}
//登录获取用户信息
var loginGetUserInitData = function (that, fromValue, sourceUser) {
  var sessionId = wx.getStorageSync("sessionId");
  //console.log(sessionId, wx.getStorageSync('id'), app.globalData.userMemberId);
  //登录成功后
  app.loginAuthorizeFun(fromValue, function (id) {
    //获取会员id
    var memberId = id;
    var sessionId = wx.getStorageSync("sessionId");
    var storeId = wx.getStorageSync('storeId');
    var nickName = wx.getStorageSync('nickName');
    if (memberId) {
      that.setData({
        memberId: memberId,
        storeId: storeId
      })
      //获取门店商家类型信息（个人版、企业版）
      util.getStoreWxaType(memberId, storeId, function (wxaType) {
        that.setData({
          bussinessType: wxaType
        })
      });
      initData(that, memberId, storeId, sessionId);
      util.getPhoneNumber(that, storeId, memberId)
      //确定分享关系
      console.log("sourceUserId", sourceUser);
      if (sourceUser) {
        let shareType = 2,
          activityProductState = that.data.activityProductState;
        //没有昵称时，默认为游客
        if (!nickName){
          nickName = "游客";
        }  
        //分享活动商品
        if (activityProductState != 0) {
          shareType = 1;
        }
        var param = {
          data: {
            wxId: memberId,
            sourceUser: sourceUser,
            shareType: shareType,
            wxNickname: nickName
          }
        }
        app.distributionApi.shareFriend(param).then(res => {
          console.log("确定关系成功");
        })
      }
    }
  })
}
 //初始化详情数据
var initData = function (that, memberId, store_id, sessionId){
   dialog.loading();
   var sendData = {};
   //如果是活动商品
  var activityId = that.data.activityId,
      targetType = that.data.targetType;
  if (activityId && targetType){
    sendData = {
      memberId: memberId,
      productId: that.data.productId,
      storeId: store_id,
      activityId: activityId,
      targetType: targetType
    }
   }else{
    sendData = {
      memberId: memberId,
      productId: that.data.productId,
      storeId: store_id
    }
   } 
   var sign = util.MD5(util.paramConcat(sendData));
   console.log(sendData);
   wx.request({
     url: constant.devUrl + '/miniapp/product/productitem.json', //接口地址
     data: sendData,
     header: {
       'wxa-sessionid': sessionId,
       'version': constant.version,
       'sign':sign
     },
     success: function (res) {
         console.log("详情：", res);
         var detailData = res.data.data,
             DetailImages = detailData.product.detailImages,
             SizeTableImage = detailData.product.sizeTableImage;
         var shopOwnDetail = detailData.product.shopOwnDetail;
         //全屏查看自定义描述区图片的数组处理   
         //console.log(shopOwnDetail); 
         var ownImagesArray = [];
         if (shopOwnDetail){
           shopOwnDetail = JSON.parse(shopOwnDetail.replace(/\n/g, "\\n").replace(/\r/g, "\\r"));
           for (var i = 0, l = shopOwnDetail.length; i < l; i++) {
             if (shopOwnDetail[i].type == 0) {
               ownImagesArray.push(shopOwnDetail[i].content);
             }
           }
         }
       if (res.data.successful) {
         that.setData({
           name:res.data.data.name
         })
         //活动数据处理
         var activityTimes = ''; 
         var teamBuyData = detailData.teamBuyActivity;   //团购的数据
         var secondBuyData = detailData.secondBuyActivity; //秒杀的数据
         var activityData ='';  //获取到活动的数据
         var summaryImages = detailData.product.summaryImages, //橱窗图
             videoDisplayImage = detailData.product.videoDisplayImage,  //视频的橱窗截图
             totalSummaryImages = '';     //橱窗总图片  
         //有视频的橱窗截图
         if (videoDisplayImage){
           totalSummaryImages = summaryImages.concat(videoDisplayImage);
         }else{
           totalSummaryImages = summaryImages
         }    
         that.setData({
           videoDisplayUrl: detailData.product.videoDisplayUrl,    //橱窗视频
           videoDisplayImage: videoDisplayImage,                      
           totalSummaryImages: totalSummaryImages,  //橱窗总图片
           activityProductState: detailData.intoActivity   //当前商品是否参加活动的状态
         })
         console.log(totalSummaryImages.length);
         //详情里面的图片是否为null的判断
         if (DetailImages != null) {
           that.setData({
             DetailImages: DetailImages
           })
         }
         if (SizeTableImage != null) {
           that.setData({
             SizeTableImage: SizeTableImage
           })
         }
         if (ownImagesArray != null) {
           that.setData({
             ownImagesArray: ownImagesArray
           })
         }
         that.setData({
           summaryImages: summaryImages,
           dataDetail: detailData,
           collectState: detailData.isFavorite,
           shopOwnDetail: shopOwnDetail,
           videoUrl: detailData.videoUrl,
           isShowTrybtn:true    //显示预约试穿按钮
         })
         //团购
         if (teamBuyData && detailData.intoActivity == 1){
           activityData = teamBuyData;
           //console.log("团购数据", activityData);
           that.setData({
             activityData: activityData, //活动的数据
             activityState: activityData.activityStatus  //拼团秒杀活动是否开始的状态
           })
           //未开始时
           if (activityData.activityStatus == '1') {
             activityTimes = activityData.surplusStartTime
           }
           //进行中
           else if (activityData.activityStatus == '2') {
             activityTimes = activityData.surplusEndTime
           }
           if (activityTimes > 0) {
             //timingFun();
             activityTimeDown(activityTimes, that, memberId, store_id, sessionId);
           }
         } 
         //秒杀
         else if (secondBuyData && detailData.intoActivity == 2){ 
           activityData = secondBuyData;
           //console.log("秒杀数据", activityData);
           that.setData({
             activityData: activityData, //活动的数据
             activityState: activityData.activityStatus  //拼团秒杀活动是否开始的状态
           })
           //未开始时
           if (activityData.activityStatus == '1') {
             activityTimes = activityData.surplusStartTime
           }
           //进行中
           else if (activityData.activityStatus == '2') {
             activityTimes = activityData.surplusEndTime;
             //如果是进行中时，点击马上抢购才需打开sku弹窗
             if (that.data.isOpenSkuBox) {
               that.setData({
                 buyBoxState: true
               })
             }
           }
           if (activityTimes > 0) {
             //timingFun();
             activityTimeDown(activityTimes, that, memberId, store_id, sessionId);
           }
         }
         //console.log("activityTimes", activityTimes);
         if (detailData.skuInfos){
           that.setData({
             skuInfos: detailData.skuInfos
           })
           //有颜色
           if (detailData.skuInfos.colorList) {
             //改变原colorList的数据
             var colorList = detailData.skuInfos.colorList;
             for (let k = 0, l = colorList.length; k < l; k++) {
               colorList[k].selected = false;
               colorList[k].isStock = true
             }
             that.setData({
               colorList: colorList
             })
           }
           //有尺寸
           if (detailData.skuInfos.sizeList) {
             //改变原sizeList的数据
             var sizeList = detailData.skuInfos.sizeList;
             for (let k = 0, l = sizeList.length; k < l; k++) {
               sizeList[k].selected = false;
               sizeList[k].isStock = true
             }
             that.setData({
               sizeList: sizeList
             })
           }
           //有skuList时
           if (detailData.skuInfos.skuList) {
             that.setData({
               skuList: detailData.skuInfos.skuList
             })
           }
         }
       } else {
         dialog.toast(res.data.error)
       }
     },
     complete: function () {
       setTimeout(function () {
         dialog.hide()
       }, 1000)
     }
   })
 }
var activityTimer = null;  //活动倒计时
var activityTimeout = false; //启动及关闭定时器  
//活动时间倒计
//var i = 0; 
var activityTimeDown = function (activityTimes, that, memberId, store_id, sessionId) {
  var totalSeconds = activityTimes / 1000;
  //console.log(totalSeconds);
  var h = 0, m = 0, s = 0;
  if (totalSeconds <= 0) {
    //更新商品数据
    initData(that, memberId, store_id, sessionId);
    clearInterval(activityTimer);
  } else {
    activityTimer = setInterval(function () {
      //console.log("是否进来");
      totalSeconds--;
      if (totalSeconds <= 0) {
        clearInterval(activityTimer);
        //更新商品数据
        initData(that, memberId, store_id, sessionId);
      } else {
        h = Math.floor(totalSeconds / 60 / 60);
        m = Math.floor(totalSeconds / 60 % 60);
        s = Math.floor(totalSeconds % 60);
        //小于0时设置为0
        if (h < 0) {
          h = 0;
          m = 0;
          s = 0
        }
        that.setData({
          activityHours: h,
          activityMinute: m,
          activityseconds: s
        })
      }
    }, 1000);
  }
}
Page({
  data: {
    tabState1: true,  //tab的状态，默认简介显示
    tabState2: false,
    collectState:'-1',  //收藏状态
    shareState: false,  //分享提示
    dataDetail:[],  //简介数据
    remarkData: [],  //详情数据
    memberId:'',   //会员id
    storeId: '',   //storeid
    productId:'', //商品id,
    summaryImages:[],   //橱窗图片
    totalSummaryImages: [],   //总的橱窗图片，包括视频的截图
    shopOwnDetail:[],  //自定义描述区
    ownImagesArray:[],  //自定义描述区图片数组
    DetailImages:[],   //商品展示
    SizeTableImage:[], //商品尺寸
    canIUse: wx.canIUse('button.open-type.share'),  //分享兼容处理
    goTopShowState: false, //返回顶部显示状态
    boxOpcityBg: false,   //蒙版显示状态
    compatible:false,  //iphone6s兼容样式,
    buyBoxState:false,   //购买弹窗
    colorList: [],         //颜色sku列表
    sizeList: [],     //尺寸sku列表
    skuList: [],      //sku列表
    colorSkuIndex: '',   //颜色sku选中
    sizeSkuIndex: '',   //尺寸sku选中
    colorSkuName: '', //选择颜色的名字
    sizeSkuName: '', //选择尺寸的名字
    buyNumber:1,      //购买数量
    colorArr:'',    //选择的颜色
    sizeArr:'',     //选择的尺寸
    stockState: false,  //控制购买数量按钮的状态
    stockNumber:1,  //库存数量
    bussinessType:'', //商家类型
    isShowVideo:true,  //是否详情里的显示视频
    submitClickFlag:1, //预约按钮提交请求次数控制
    slideShowVideo:false,  //橱窗图视频点击显示控制
    videoDisplayUrl:'',    //橱窗图的视频
    videoDisplayImage: '',  //视频的橱窗图片
    activityData:'',   //活动的数据
    activityGroupBuyData: '',   //团购活动数据
    activitySecondBuyData: '',  //秒杀活动数据
    activityProductState: 0,   //当前商品是否参加活动 默认为0不参加， 0未参与活动、1表示参与团购活动、2表示参与秒杀活动
    activityTimes: '',   //拼团秒杀活动倒计剩余时间
    activityHours:'',   //拼团秒杀活动倒计时-小时
    activityMinute: '',   //拼团秒杀活动倒计时-分钟
    activitySecond: '',   //拼团秒杀活动倒计时-秒
    activityState: '',   //拼团秒杀活动未开始的状态
    isOpenSkuBox: false,      //秒杀马上抢购时，是否需打开sku弹窗 true-打开
    shareFlag: '',         //分享商品详情页面跳转过来的标识
    tryFlag: 0,       //sku弹窗显示不同按钮的标识 0-立即购买、1-预约、2-加入购物车、
    isShowSharePopup: false,      //是否显示分享朋友和朋友圈弹窗
    sourceUserId: '',            //邀请者memberId
    isShowSettingPopup:false,   //显示授权设置弹窗,
    shareSuccess: false  //分享成功后的状态 true-成功后不刷新页面（成功后提示文字用）
  },
  onReady: function () {
    //获得popup弹窗组件
    this.authPopup = this.selectComponent("#settingPopup");
    console.log(this.authPopup);
  },
  onLoad: function (options){
    var that = this;
    var product_id = options.productId,  //获取url的参数
        isOpenSkuBox = options.isOpenSkuBox,   //秒杀马上抢购时，需打开sku弹窗
        shareFlag = options.shareFlag,       //分享商品详情页面跳转过来的标识为1
        sourceUserId = options.sourceUserId,   //邀请者memberId
        targetType = options.targetType,        //是否活动和普通商品
        activityId = options.activityId;       //活动id
    console.log("分享的标识",shareFlag);    
    //options 中的 scene 需要使用 decodeURIComponent 才能获取到生成二维码时传入的 scene
    console.log("scene",options.scene);
    // 方便二维码测试打开调试
    // wx.setEnableDebug({
    //   enableDebug: true
    // })
    //活动商品
    if (targetType && activityId) {
      that.setData({  
        targetType: targetType,
        activityId: activityId
      })
    }
     //邀请者memberId
    if (sourceUserId){
      that.setData({ 
        sourceUserId: sourceUserId
      })
    }
    //二维码分享识别判断
    if (options.scene){
      product_id = options.scene;
      shareFlag = 1;
      console.log("拿到scene参数成功", product_id, shareFlag);
    }
    if (shareFlag){
      that.setData({
        shareFlag: shareFlag
      }) 
    }
    that.setData({ 
      productId: product_id
    })
    console.log("二维码参数设置data成功", that.data.productId, that.data.shareFlag);
    //秒杀马上抢购时，需打开sku弹窗，设置isOpenSkuBox为打开状态
    if (isOpenSkuBox) {
      that.setData({ 
        isOpenSkuBox: true
      })
    }
  },
  onShow:function(){
    var that = this;
    //隐藏权限设置
    that.setData({
      isShowSettingPopup:false
    })
    if (!that.data.shareFlag){
      console.log("不刷新");
      app.globalData.isShowIndex = true; //返回首页是否刷新首页数据  false-刷新、true-不刷新
      app.globalData.isRefleshProductList = true;
    }else{
      console.log("刷新");
      //分享直接进去到商品详情时，点击菜单上的‘回到首页’需要刷新首页数据
      app.globalData.isShowIndex = false; 
      //是否显示更多商品（返回首页浮动条）
      that.setData({
        isShowBackHome: true
      })
    }
    var systemWidth = wx.getSystemInfoSync();
    //console.log(systemWidth);
    //兼容iphone6s的样式
    if (systemWidth.screenWidth <= 375 && systemWidth.screenHeight <= 667){
      that.setData({
        compatible: true
      })
    }
    clearInterval(activityTimer);
    //确认用户来源-0：是自主注册， 1：是邀请注册
    var sourceUser = that.data.sourceUserId,
        fromValue = '';
    console.log("sourceUser", sourceUser);
    if (sourceUser){
      fromValue = 1;
    }else{
      fromValue = 0
    }
    //分享成功后,返回不刷新页面
    if (!that.data.shareSuccess){
      //初始化数据
      loginGetUserInitData(that, fromValue, sourceUser);
    }
  },
  onHide:function(){
    var that = this;
    //clearInterval(activityTimer);
    //clearConsult(that);
    //当从确定订单页面返回时，不自动显示sku弹窗
    that.setData({
      isOpenSkuBox:false
    })
  },
  onUnload:function(){
    clearInterval(activityTimer);
    wx.removeStorageSync("skuGroup");
  },
  onReady:function(){
    //视频控组上下文
    this.videoContext = wx.createVideoContext('product-video')
  },
  //收藏函数
  collectFun:function(e){
    var that = this;
    var state = e.currentTarget.dataset.state,
        memberId = wx.getStorageSync('id'),
        productId = parseInt(that.data.productId);
    //console.log(memberId, productId);
    if (memberId){
      if (state == 0) {
        //取消收藏
        collectRequest(that, memberId, productId,'0','-1');
      } else {
        //添加收藏
        collectRequest(that, memberId, productId, '0', '0');
      }
    }else{
      var sourceUser = that.data.sourceUserId,
        fromValue = '';
      if (sourceUser) {
        fromValue = 1;
      } else {
        fromValue = 0
      }
      loginGetUserInitData(that, fromValue, sourceUser);
    }
  },
  //显示转发按钮
  onShareAppMessage: function (res) {
    var that = this,
        memberId = that.data.memberId;
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    var shareParam = '';
    //如果是活动商品
    var activityId = that.data.activityId,
        targetType = that.data.targetType;
    if (activityId && targetType) {
      shareParam = '?productId=' + this.data.productId + '&shareFlag=1' + "&sourceUserId=" + memberId + "&activityId=" + activityId + "&targetType=" + targetType;
    } else {
      shareParam = '?productId=' + this.data.productId + '&shareFlag=1' + "&sourceUserId=" + memberId;
    } 
    var shareTitle = that.data.dataDetail.product.name,
        shareImgUrl = that.data.summaryImages[0];
    console.log("shareParam", shareParam);
    return {
      title: shareTitle,
      imageUrl: shareImgUrl,
      path: '/pages/component/detail/detail' + shareParam,
      success: function (res) {
        // 转发成功
        that.setData({
          shareState: false,
          shareSuccess: true,
          isShowSharePopup:false
        })
        var shareType = 2,
            targetId = that.data.productId,
            activityProductState = that.data.activityProductState,
            activityType = '';
        var param = {
          type: shareType,
          targetId: targetId
        }
        //分享函数
        app.common.shareFunc(param, function (data) {
          console.log("data.isOutOfMax", data.isOutOfMax);
          //当isOutOfMax为true时，已经超过最大分享收益次数,不给颜值分和佣金、金币
          dialog.toastError("分享成功！");
          var coinNumber = '',
              text = '',
              typeSwitch = data.typeSwitch; 
          //现金    
          if (typeSwitch == 1){
              coinNumber = data.earningsCash;
              text ="元"
          }else{
             coinNumber = data.earningsGoldCoin;
              text = "金币"
          }    
          if (!coinNumber) {
            coinNumber = 0;
          }
          if (!data.isOutOfMax) {
            let promptText = "恭喜您分享成功，获得" + coinNumber + text;
            if (coinNumber > 0){
              dialog.toastError(promptText);
            }
            //活动商品需刷新页面
            if (activityProductState != 0) {
              var sessionId = wx.getStorageSync("sessionId"),
                storeId = wx.getStorageSync('storeId'),
                memberId = that.data.memberId;
              setTimeout(function () {
                initData(that, memberId, storeId, sessionId);
              }, 2000)
            }
          }
        });  
      },
      fail: function (res) {
        // 转发失败
        that.setData({
          shareState: false
        })
      }
    }
  },
  //显示分享和下载popup
  showSharePopup:function(){
    var that = this;
    //判断网络
    app.common.judgeNetwork(function(){
      that.setData({
        isShowSharePopup: true,
        isShowVideo:false
      })
    })
  },
  //关闭分享和下载popup
  hideSharePopup:function(){
    var that = this;
    that.setData({
      isShowSharePopup: false,
      isShowVideo:true
    })
  },
  //显示分享提示
  shareFun: function () {
    var that = this;
    that.setData({
      shareState: true
    })
    setTimeout(function(){
      that.setData({
        shareState: false
      })
    },3000)
  },
  //隐藏分享提示
  closeShareFun: function () {
    this.setData({
      shareState: false
    })
  },
  //返回顶部
  goTop: function () {
    util.gotoTop()
  },
  //页面滚动到某个位置时
  onPageScroll: function () {
    var that = this;
    var scrollHeight;
    if (wx.createSelectorQuery()) {
      wx.createSelectorQuery().select('.tab-box').boundingClientRect(function (rect) {
        scrollHeight = rect.top;
        //console.log(scrollHeight);
        if (scrollHeight < -440) {
          that.setData({
            tabState1: false,
            tabState2: true
          })
        } else if (scrollHeight < -150){
          that.setData({
            goTopShowState: true
          })
        } else {
          that.setData({
            tabState1: true,
            tabState2: false,
            goTopShowState: false
          })
        }
      }).exec()
    }
  },
  //橱窗图全屏查看
  fullScreenView:function(e){
    //分享成功后的状态
    this.setData({
      shareSuccess: false
    })
    clearInterval(activityTimer);
    var that = this;
    var url = e.currentTarget.dataset.url;
    wx.previewImage({
      current: url, // 当前显示图片的http链接
      urls: that.data.summaryImages
    })
  },
  //详情全屏查看
  fullScreenDetail: function (e) {
    //分享成功后的状态
    this.setData({
      shareSuccess: false
    })
    clearInterval(activityTimer);
    var that = this;
    var url = e.currentTarget.dataset.url;
    var ownImagesArray = that.data.ownImagesArray;
    var DetailImagesArray = that.data.DetailImages;
    var sizeImageArray = that.data.SizeTableImage;
    var wxaqrcodeUrl = that.data.dataDetail.product.wxaqrcodeUrl
    //给旋转90度的图片做处理
    var newOwnImagesArray = [];
    var param = '?x-oss-process=image/auto-orient,1';
    for (var i = 0, l = ownImagesArray.length; i < l; i++) {
      newOwnImagesArray.push(ownImagesArray[i] + param);
    }
    var urlArray = [];
    //console.log(newOwnImagesArray, DetailImagesArray, sizeImageArray);
    urlArray = newOwnImagesArray.concat(sizeImageArray.concat(DetailImagesArray.concat(wxaqrcodeUrl)));
    wx.previewImage({
      current: url, // 当前显示图片的http链接
      urls: urlArray
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
  //显示立即购买弹窗
  gotoBuyBox:function(e){
    var tryFlag = e.currentTarget.dataset.tryflag;
     //活动未开始时,不显示sku弹窗
    if (this.data.activityState == 1){
      dialog.toastError("别急哦，活动还未开始，请继续关注");
      return;
    }
    //活动已结束
    if (this.data.activityState == 3) {
      dialog.toastError("活动已结束，请继续关注其他活动");
      return;
    }
    //点击预约试穿时,sku显示预约试穿的按钮
    if (tryFlag == '0'){
      console.log('购买或者马上抢', tryFlag);
      this.setData({
        tryFlag: 0
      })
      console.log('that.data.tryFlag为', this.data.tryFlag);
    }
    this.setData({
      buyBoxState:true,
      isShowVideo:false
    })
  },
  //关闭立即购买弹窗
  closeXbox:function(){
    var that = this;
    //console.log("原先数据", that.data.originalColorList, that.data.originalSizeList);
    that.setData({
      buyBoxState: false,
      buyNumber: 1,
      isShowVideo: true,
      colorSkuIndex: '',      //清空选中的sku
      sizeSkuIndex: ''
    })
    //初始化颜色和尺码为页面加载到的数据
    var sizeList = that.data.sizeList,
        colorList = that.data.colorList;
    for (let i = 0, l = sizeList.length; i < l; i++) {
      sizeList[i].selected = false;
      sizeList[i].isStock = true
    }
    for (let k = 0, l = colorList.length; k < l; k++) {
      colorList[k].selected = false;
      colorList[k].isStock = true
    }
    that.setData({
      colorList: colorList,
      sizeList: sizeList
    })
  },
  /**
   * 是否有库存显示函数
   * @param flag  1-颜色；2-尺寸 
   */
  skuCombineStock: function (skuId,flag){
    var that = this,
        colorList = that.data.colorList,
        sizeList = that.data.sizeList,
        skuList = that.data.skuList;
    //颜色
    if (flag == 1){
      var sizeCountArray = [];
      for (var i = 0, l = skuList.length; i < l; i++) {
        if (skuId == skuList[i].colorId) {
          //无库存时，保存无库存的尺寸
          if (skuList[i].count == 0){
            sizeCountArray.push(skuList[i].sizeId);
          }
        }else{
          for (let k = 0, l = sizeList.length; k < l; k++) {
            sizeList[k].isStock = true;
          }
        }
      }
      //改变原sizeList的数据
      for (let k = 0, l = sizeList.length; k < l; k++) {
        for (let j = 0, h = sizeCountArray.length; j < h; j++) {
          if (sizeList[k].sizeId == sizeCountArray[j]) {
            sizeList[k].isStock = false;
          }
        }
      }
      //console.log("无库存的sizeCountArray和最新的sizeList", sizeCountArray, sizeList);
      //重新设置数据渲染
      that.setData({
        sizeList: sizeList
      })
    }else{
      //尺寸
      var colorCountArray = [];
      for (var i = 0, l = skuList.length; i < l; i++) {
        if (skuId == skuList[i].sizeId) {
          //无库存时，保存无库存的颜色
          if (skuList[i].count == 0) {
            colorCountArray.push(skuList[i].colorId);
          }
        } else {
          for (let k = 0, l = colorList.length; k < l; k++) {
            colorList[k].isStock = true;
          }
        }
      }
      //改变原colorList的数据
      for (let k = 0, l = colorList.length; k < l; k++) {
        for (let j = 0, h = colorCountArray.length; j < h; j++) {
          if (colorList[k].colorId == colorCountArray[j]) {
            colorList[k].isStock = false
          }
        }
      }
      //console.log("无库存的colorCountArray和最新的colorList", colorCountArray, colorList);
      //重新设置数据渲染
      that.setData({
        colorList: colorList
      })
    }   
  },
  //sku选择
  chooseSku:function(e){
    var that = this;
    var skuId = e.target.dataset.id;
    var name = e.target.dataset.name;
    console.log("当前选中skuId",skuId);
    var clickIndex = e.currentTarget.dataset.click,
        colorList = that.data.colorList,
        sizeList = that.data.sizeList,
        skuList = that.data.skuList;
    that.setData({
      buyNumber: 1
    })     
    //颜色
    if (clickIndex == '1'){
      //先清空原先选中的值    
      that.setData({
        colorSkuIndex: '',
        colorSkuName: ''
      })
      for (var i = 0, len = colorList.length; i < len; i++) {
        if (colorList[i].colorId == skuId) {
          //colorList[i].selected = !colorList[i].selected; 
          //选中,设置选中的颜色值
          if (colorList[i].selected){
            colorList[i].selected = false;
            var skuId = '';
            that.skuCombineStock(skuId, 1)
          }else{
            that.setData({
              colorSkuIndex: skuId,
              colorSkuName: name
            })
            colorList[i].selected = true;
            that.skuCombineStock(skuId, 1)
          }
          //console.log("colorSkuIndex", that.data.colorSkuIndex, colorList[i].selected);
        }else{
          colorList[i].selected = false
        }
      }
      that.setData({
        colorList: colorList
      })
      console.log("colorList", colorList);
    } 
    //尺寸
    else if (clickIndex == '2'){
      //先清空原先选中的值    
      that.setData({
        sizeSkuIndex: '',
        sizeSkuName: ''
      })
      for (var i = 0, len = sizeList.length; i < len; i++) {
        if (sizeList[i].sizeId == skuId) {
          //选中,设置选中的尺寸值
          if (sizeList[i].selected) {
            sizeList[i].selected = false;
            var skuId = '';
            that.skuCombineStock(skuId, 2)
          } else {
            that.setData({
              sizeSkuIndex: skuId,
              sizeSkuName: name,
            })
            sizeList[i].selected = true;
            that.skuCombineStock(skuId, 2)
          }
        }else{
          sizeList[i].selected = false
        }
      }
      that.setData({
        sizeList: sizeList
      })
      console.log("sizeList", sizeList);
    }
  },
  //购买数量减
  reduceFun:function(){
    var that = this;
    var buyNumber = that.data.buyNumber;
    if (buyNumber <= 1){ 
      that.setData({
        buyNumber:1
      })
      dialog.toast("数量不能小于1");
    }else{
      
      buyNumber --;
      that.setData({
        buyNumber: buyNumber
      })
      if (parseInt(buyNumber) <= parseInt(that.data.stockNumber)) {
        that.setData({
          stockState: false
        })
      }
    }
  },
  //购买数量加
  addFun: function () {
    var that = this;
    var buyNumber = that.data.buyNumber,
        surplusProductCount = that.data.activityData.surplusProductCount,
        stockNumber = that.data.stockNumber,
        colorId = this.data.colorSkuIndex,
        sizeId = this.data.sizeSkuIndex;
    buyNumber++;
    that.setData({
      buyNumber: buyNumber
    })     
    //活动超过库存量时
    if (that.data.activityState == 1 || that.data.activityState == 2) {
      if (buyNumber > surplusProductCount){
        dialog.toastError("选择购买数量超过了最大库存量");
        return;
      }
    }else{
      if (colorId && sizeId){
        var solorSize = colorId + sizeId,
            skuList = that.data.skuList,
            skuListNum = '';
        for (var i = 0, l = skuList.length; i < l; i++) {
          skuListNum = skuList[i].colorId + skuList[i].sizeId;
          if (solorSize == skuListNum) {
            that.setData({
              stockNumber: skuList[i].count
            })
          }
        }
        console.log("buyNumber\stockNumber", buyNumber,that.data.stockNumber);
        //普通商品超过库存时
        if (parseInt(buyNumber) >= parseInt(that.data.stockNumber)) {
          that.setData({
            stockState:true
          })
          dialog.toastError("选择购买数量超过了最大库存量");
          return;
        }
      }
    } 
  },
  //回到首页
  backHomeFunc: function (e) {
    var that =this;
    if (!that.data.shareFlag){
      app.globalData.isShowIndex = true;
    }
    wx.switchTab({
      url: '../../index/index'
    })
  },
  //sku没有选择校验
  checkSkuChoose:function(){
    var colorId = this.data.colorSkuIndex,
        sizeId = this.data.sizeSkuIndex,
        colorList = this.data.colorList,
        sizeList = this.data.sizeList,
        flag = 1;
    console.log("colorId,sizeId,colorList,sizeList", colorId, sizeId, colorList, sizeList);    
    //颜色和尺寸为空校验(因老版本有商品没有sku，也可以购买，所以不做校验)
    if (colorList.length > 0 && sizeList.length > 0) {
      if (!colorId && !sizeId) {
        dialog.toastError("请选择颜色和尺码");
        flag = 0;
        return
      }
      if (!colorId) {
        dialog.toastError("请选择颜色");
        flag = 0;
        return
      }
      if (!sizeId) {
        dialog.toastError("请选择尺码");
        flag = 0;
        return
      }
    }
    return flag;
  },
  //提交跳转到确认订单
  confirmOrder:function(){
    var that = this;
    //活动未开始时,不显示sku弹窗
    if (this.data.activityState == 1) {
      dialog.toastError("别急哦，活动还未开始，请继续关注");
      return;
    }
    var buyNumber = this.data.buyNumber,
        colorId = this.data.colorSkuIndex,
        sizeId = this.data.sizeSkuIndex,
        solorSize = colorId + sizeId,
        colorList = that.data.colorList,
        sizeList = that.data.sizeList;
    console.log(colorId, sizeId, solorSize);
    if (!that.checkSkuChoose()){
      return
    }
    var skuList = this.data.skuList;
    var skuListNum = '';
    var skuId = '';
    if (skuList){
      for (var i = 0, l = skuList.length; i < l; i++) {
        skuListNum = skuList[i].colorId + skuList[i].sizeId;
        if (solorSize == skuListNum) {
          skuId = skuList[i].skuId
          that.setData({
            stockNumber: skuList[i].count
          })
        }
      }
    }
    if (skuId == ''){
      skuId =0;
    }
    var productId = this.data.productId;
    var sku_group = productId + "_" + skuId + "_" + buyNumber;
    //如果是活动商品
    var activityId = that.data.activityId,
        targetType = that.data.targetType,
        param = '';
    if (activityId && targetType) {
      param = "&activityId=" + activityId + "&targetType=" + targetType;
      sku_group += param;
    }
    console.log(sku_group);
    wx.setStorageSync('skuGroup', sku_group)
    wx.navigateTo({
      url: '../confirmOrder/confirmOrder?skuGroup='+sku_group
    })
    that.setData({
      buyBoxState:false,
      buyNumber: 1
    })
  },
  //播放结束自动退出全屏
  endPlay:function(){
    this.videoContext.exitFullScreen();
  },
  //退出全屏控制
  quitControl:function(e){
    var fullScreen = e.detail.fullScreen;
    console.log(fullScreen);
    if (fullScreen == false){
      this.videoContext.pause();
    }
    //分享成功后的状态
    this.setData({
      shareSuccess: false
    })
  },
  //橱窗里的视频弹窗显示
  showVideoFunc: function () {
    var that = this;
    that.setData({
      slideShowVideo: true
    })
  },
  //关闭橱窗里的视频弹窗显示
  closeVideo: function () {
    var that = this;
    that.setData({
      slideShowVideo: false
    })
  },
  //显示设置权限弹窗
  showSettingPopup:function(){
    var that = this;
    console.log(0);
    //this.popup.showPopup();
    that.setData({
      isShowSettingPopup:true,
      isShowVideo:false
    })
  },
  //隐藏设置权限弹窗
  hideSettingPopup: function () {
    var that = this;
    //this.popup.hidePopup();
    that.setData({
      isShowSettingPopup: false,
      isShowVideo:true
    })
  },
  //获取合成分享图片
  getDownShareImg:function(){
    var that = this;
    //如果是活动商品
    var activityId = that.data.activityId,
        targetType = that.data.targetType,
        storeId = wx.getStorageSync('storeId'),
        paramData = {};
    if (activityId && targetType){
      paramData = {
        memberId: that.data.memberId,
        storeId: storeId,
        shopProductId: that.data.productId,
        activityId: activityId,
        targetType: targetType
      }
    }else{
      paramData = {
        memberId: that.data.memberId,
        storeId: storeId,
        shopProductId: that.data.productId
      }
    }        
    var param = {
       data: paramData
    }
    //判断是否有二维码
    var wxaqrcodeUrl = that.data.dataDetail.product.wxaqrcodeUrl;
    if (!wxaqrcodeUrl){
       dialog.toastError("该功能体验版不支持！");
       return;
    }
    dialog.loading();
    //console.log(sendData);
    //判断网络
    app.common.judgeNetwork(function(){
      //有网络获取分享图片
      app.facade.getShareImage(param).then(function (data) {
        console.log("成功获取分享图", data);
        console.log(data.data.wxaProductShareImage);
        //获取权限状态后,调用下载图片函数
        share.getUserSetting(function (isAuth){
          console.log("返回的isAuth", isAuth);
          share.downShareImg(that, data.data.wxaProductShareImage, isAuth);
        })
      })
    },function(){
      //无网络时，隐藏分享弹窗
      that.setData({
        isShowSharePopup:false
      })
    })
  },
  //权限设置回调函数
  bindopensetting: function (e) {
    var that = this;
    console.log("设置权限后回调", e.detail);
    //this.popup.showPopup();
    that.setData({
      isShowSettingPopup: false
    })
  },
  //跳转到购物车
  gotoShoppingCart:function(){
    wx.switchTab({
      url: '/pages/shoppingCart/shoppingCart'
    })
  },
  //显示加入购物车弹窗函数
  showCartXbox:function(e){
    var tryFlag = e.currentTarget.dataset.tryflag;
    //点击加入购物车时,sku显示加入购物车的按钮
    if (tryFlag == '2') {
      this.setData({
        tryFlag: 2,
        buyBoxState:true,
        isShowVideo: false
      })
    } 
  },
  //加入购物车函数
  joinCartFunc:function(){
    var that = this;
    var store_Id = wx.getStorageSync('storeId');
    //获取skuid和数量
    var buyNumber = this.data.buyNumber,
       colorId = this.data.colorSkuIndex,
       sizeId = this.data.sizeSkuIndex,
       solorSize = colorId + sizeId;
    if (!that.checkSkuChoose()) {
      return
    }  
    var skuList = this.data.skuList,
        skuListNum = '',
        skuId = '';
    if (skuList) {
      for (var i = 0, l = skuList.length; i < l; i++) {
        skuListNum = skuList[i].colorId + skuList[i].sizeId;
        if (solorSize == skuListNum) {
          skuId = skuList[i].skuId
        }
      }
    }
    if (skuId == '') {
      skuId = 0;
    }
    var param = {
      data:{
        memberId: this.data.memberId,
        storeId: store_Id,
        productId: that.data.productId,
        skuId: skuId,
        skuNumber: buyNumber
      }
    }
    console.log("加入购物车入参", param);
    app.facade.joinShopCar(param).then(res => {
      dialog.toastError("成功添加到购物车！");
      that.setData({
        buyBoxState: false,
        isShowVideo:true
      })
    });
  }
})
