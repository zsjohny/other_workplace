//编辑个人信息
var app = getApp()
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var store_id = wx.getStorageSync('storeId');
//获取用户信息
var getUserData = function (that, memberId, sessionId) {
  var sendData = {
    storeId: store_id,
    memberId: memberId //会员id
  };
  var sign = util.MD5(util.paramConcat(sendData));
  wx.request({
    url: constant.devUrl + '/miniapp/member/myInfo.json', //接口地址
    data: {
      storeId: store_id,
      memberId: memberId //会员id
    },
    header: {
      'wxa-sessionid': sessionId,
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      var memberInfo = res.data.data.memberInfo;
      //console.log(memberInfo, memberInfo.userIcon)
      if (res.data.successful && memberInfo) {
        that.setData({
          nickName: memberInfo.userNickname,
          headUrl: memberInfo.userIcon,
          memberId: memberInfo.id,
        })
        //方便修改昵称页面读取昵称
        if (memberInfo.userNickname !=""){
           wx.setStorageSync("userNickname", memberInfo.userNickname);
        }
      } else {
        dialog.toast(res.data.error)
      }
    }
  })
}
//登录获取用户信息
var loginGetUserData = function (that) {
  var sessionId = wx.getStorageSync("sessionId");
  if (sessionId){
    wx.checkSession({
      success: function () {
        //session 未过期，并且在本生命周期一直有效
        var id = wx.getStorageSync('id'); //获取会员id
        var sessionId = wx.getStorageSync("sessionId");
        var store_id = wx.getStorageSync('storeId');
        getUserData(that, id, sessionId, store_id)
      },
      fail: function () {
        //登录态过期
        app.loginUnauthorizedFun(function () {
          var id = wx.getStorageSync('id'); //获取会员id
          var sessionId = wx.getStorageSync("sessionId");
          var store_id = wx.getStorageSync('storeId');
          getUserData(that, id, sessionId, store_id)
        })
      }
    })
  }else{
    //登录态过期
    app.loginUnauthorizedFun(function () {
      var id = wx.getStorageSync('id'); //获取会员id
      var sessionId = wx.getStorageSync("sessionId");
      var store_id = wx.getStorageSync('storeId');
      getUserData(that, id, sessionId, store_id)
    })
  }
}
Page({
  data: {
    nickName:'',    //昵称
    headUrl:'',     //头像
    memberId: '',      //用户id
    not_member:'游客'
  },
  onLoad:function(){

  },
  onShow:function(){
    var that = this;
    //获取用户信息
    loginGetUserData(that);
  },
  //选择本地图片函数
  changeHeadFun: function () {
    //console.log("选择图片函数");
    let _this = this;
    wx.showActionSheet({
      itemList: ['相册', '拍照'],
      itemColor: "#333",
      success: function (res) {
        if (!res.cancel) {
          if (res.tapIndex == 0) {
            _this.chooseHeadImage('album')
          } else if (res.tapIndex == 1) {
            _this.chooseHeadImage('camera')
          }
        }
      }
    })
  },
  //更改头像函数
  chooseHeadImage: function (type){
    var that = this;
    var oldHeadUrl = that.data.headUrl;
    console.log(oldHeadUrl);
    if (wx.chooseImage) {
      wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: [type], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
          var tempFilePaths = res.tempFilePaths;
          //console.log(res.tempFiles[0].size);
          //var fileSize = res.tempFiles[0].size;
          //提交成功后显示图片
          that.setData({
            headUrl: tempFilePaths[0] //头像路径值
          })
          //上传图片
          var memberId = that.data.memberId,
            headUrl = that.data.headUrl;
          //console.log(memberId);
          var uploadUrl = constant.devUrl + "/miniapp/member/updatefieldvalue.json"; //util.devUrl
          var formData = {
            memberId: memberId,
            key: 'userIcon',
            value: headUrl,
            storeId: store_id,
            oldPath: oldHeadUrl
          }
          console.log(formData);
          util.uploadFileFun(uploadUrl, headUrl, 'userIcon', formData, function (res) {
            //修改成功后设置新头像的值
            //console.log(res);
            dialog.toast("提交成功");
            //提交成功后显示图片
            that.setData({
              headUrl: tempFilePaths[0] //头像路径值
            })
          }, function (res) {
            //console.log(res.errMsg);
            dialog.toast("图片超过大小了")
          })
        }
      })
    } else {
      // 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
      wx.showModal({
        title: '提示',
        content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。'
      })
    }

  }
})