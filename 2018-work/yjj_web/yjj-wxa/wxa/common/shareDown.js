/**
 * 商品列表分享功能共用js
 */
var dialog = require("../utils/dialog"),
    common = require('common.js'),
    requestUtil = require('../service/request.js'),
    myRequest = requestUtil.myRequest;

/**
 * 获取用户当前已经授权的设置状态(下载分享图片保存到手机相册的权限)
 */
function getUserSetting(callback){
  var isAuth = '';
  wx.getSetting({
    success: (res) => {
      var authObj = res.authSetting;
      console.log(authObj);
      isAuth = authObj['scope.writePhotosAlbum'];
      callback(isAuth);
    }
  })
}

/**
 * 保存图片到相册
 */
function saveImageToPhotosAlbumFunc(that, tempFilePath, isAuth) {
  wx.saveImageToPhotosAlbum({
    filePath: tempFilePath,
    success: function (res) {
      console.log('保存相册成功1', res)
      dialog.toastError("小程序图片已生成，可在图库中分享朋友圈！");
      //保存相册成功后，隐藏分享弹窗
      that.setData({
        isShowSharePopup: false
      })
    },
    fail: function (res) {
      //console.log('保存失败1', res, "权限值", isAuth)
      //没有授权,显示设置权限弹窗
      if (!isAuth || typeof (isAuth) == "undefined") {
        that.showSettingPopup();
      } else {
        dialog.toast("保存相册失败");
      }
    }
  })
}

/**
 * 下载分享图片
 */
function downShareImg(that, imgUrl, isAuth) {
  wx.showLoading({
    title: '正在下载中...',
  })
  console.log("isAuth", isAuth);
  wx.downloadFile({
    url: imgUrl,
    success: function (res) {
      console.log('downloadFile:ok：', res);
      var tempFilePath = res.tempFilePath;
      wx.hideLoading();
      //dialog.toast("没有授权");
      if (!isAuth || typeof (isAuth) == "undefined") {
        saveImageToPhotosAlbumFunc(that, tempFilePath, isAuth);
      } else {
        saveImageToPhotosAlbumFunc(that, tempFilePath, isAuth);
      }
    },
    fail: function (res) {
      wx.hideLoading();
      console.log('fail下载失败', res, isAuth)
      //没有授权,显示设置权限弹窗
      if (!isAuth || typeof (isAuth) == "undefined") {
        that.showSettingPopup();
      } else {
        dialog.toast("下载失败");
      }
    }
  })
}

/**
 * 分享转发给好友获取佣金
 * 
 * @param type: 1-天天拼团的邀请好，2-推荐商品的分享
 * @param targetType: 0-未参与活动的商品、1-表示参与团购活动的商品、2-表示参与秒杀活动的商品
 * @param shareObj - 当前分享给好友的按钮
 */
function shareAppGetCoin(that, shareObj){
  var shareItem = that.data.shareItem,
      memberId = that.data.memberId,
      storeId = that.data.storeId;
  //wx.hideShareMenu();
  if (shareObj.from === 'button') {
    // 来自页面内转发按钮
    console.log(shareObj.target);
    //分享商品列表
    var targetType = shareItem.targetType,
        shareShopProductId = shareItem.shareProductId,
        shareTitle = shareItem.shareProductName,
        shareImageUrl = shareItem.shareImg,
        activityId = shareItem.activityId;
    let shareParam = '';
    //天天拼团、秒杀商品
    if (activityId != '0' && targetType != '0') {
      shareParam = '?productId=' + shareShopProductId + '&shareFlag=1' + "&activityId=" + activityId + "&targetType=" + targetType + '&sourceUserId=' + memberId;
    } else {
      //普通商品
      shareParam = '?productId=' + shareShopProductId + '&shareFlag=1' + '&sourceUserId=' + memberId;
    }
    return {
      title: shareTitle,
      path: '/pages/component/detail/detail' + shareParam,
      imageUrl: shareImageUrl,
      success: function (res) {
        //商品列表的分享
        if (shareType) {
          that.setData({
            shareSuccess: true,
            isShowSharePopup: false
          })
          var param = {
            storeId: storeId,
            memberId: memberId,
            type: 2,
            targetId: shareShopProductId
          }
          //分享函数
          var productPrice = that.data.shareItem.price;
          var coinNumber = Math.round((parseFloat(productPrice) / 10).toFixed(1));
          if (coinNumber > 10) {
            coinNumber = 10;
          } else if (isNaN(coinNumber)) {
            coinNumber = 0;
          }
          common.shareFunc(param, function (data) {
            // console.log("data.isOutOfMax", data.isOutOfMax);
            //当isOutOfMax为true时，已经超过最大分享收益次数,不给颜值分和佣金
            if (!data.isOutOfMax) {
              // let text = "恭喜您分享成功，获得" + coinNumber + "颜值分";
              // if (coinNumber > 0) {
              //   dialog.toastError(text);
              // }
            }
          });
        }
      },
      fail: function (res) {
        // 转发失败
      }
    }
  }
  if (shareObj.from === 'menu') {
    // 右上角转发菜单
    console.log(shareObj.target)
  }
}

/**
 * 获取分享朋友圈的合成图
 */
function getComposeShareImg(that){
  var shareItem = that.data.shareItem,
      memberId = that.data.memberId,
      shareProductId = shareItem.shareProductId,
      activityId = shareItem.activityId,
      storeId = that.data.storeId,
      targetType = shareItem.targetType,
      paramData = {};
  console.log("activityId", activityId, targetType);  
  //如果是秒杀和团购活动商品
  if ((activityId && activityId != '0') && (targetType && targetType != '0')) {
    paramData = {
      memberId: memberId,
      storeId: storeId,
      shopProductId: shareProductId,
      activityId: activityId,
      targetType: targetType
    }
  } else {
    paramData = {
      memberId: memberId,
      storeId: storeId,
      shopProductId: shareProductId
    }
  }
  var param = {
    data: paramData
  }
  console.log("获取合成图入参", param);
  //判断是否有二维码
  var wxaqrcodeUrl = that.data.shareItem.isShareCode;
  if (wxaqrcodeUrl == '0') {
    dialog.toastError("该功能体验版不支持！");
    return;
  }
  dialog.loading();
  //判断网络,有网络获取分享图片
  common.judgeNetwork(function () {
    var url = '/shopProduct/detail/share/getImage.json';
    myRequest(url, param).then(data => {
      console.log("成功获取分享图", data);
      console.log(data.data.wxaProductShareImage);
      //获取权限状态后,调用下载图片函数
      getUserSetting(function (isAuth) {
        console.log("返回的isAuth", isAuth);
        downShareImg(that, data.data.wxaProductShareImage, isAuth)
      })
    });
  }, function () {
    //无网络时，隐藏分享弹窗
    that.setData({
      isShowSharePopup: false
    })
  })
}

/**
 * 设置当前分享商品的数据
 * 
 * @param target - 当前点击分享按钮的元素
 */
function setShareData(that, target){
  var targetType = target.dataset.producttype,
      isShareCode = target.dataset.iscodeimg,
      shareImg = target.dataset.img,
      shareProductName = target.dataset.name,
      activityId = target.dataset.activityid,
      price = target.dataset.price,
      shareProductId = target.dataset.id;
  var shareParam = {
    targetType: targetType,           //区分普通和活动商品
    shareProductId: shareProductId,  //当前分享的商品id
    activityId: activityId,          //活动id
    price: price,                    //商品价格
    shareProductName: shareProductName,  //分享商品标题
    shareImg: shareImg,               //当前分享的商品图片
    isShareCode: isShareCode        //商品是否有分享图
  }
  console.log("当前活动分享数据", shareParam);
  var oldShareItem = that.data.shareItem;
  // 防止覆盖掉新的数据 使用循环的方式
  for (var name in shareParam) {
    oldShareItem[name] = shareParam[name];
  }
  //判断网络
  common.judgeNetwork(function () {
    that.setData({
      isShowSharePopup: true,
      shareItem: oldShareItem
    })
  })
}


//输出接口
module.exports = {
  getUserSetting: getUserSetting,
  saveImageToPhotosAlbumFunc: saveImageToPhotosAlbumFunc,
  downShareImg: downShareImg,
  shareAppGetCoin: shareAppGetCoin,
  setShareData: setShareData,
  getComposeShareImg: getComposeShareImg
}