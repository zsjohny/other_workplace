// 编辑用户昵称
var util = require("../../../utils/util")
var constant = require('../../../constant')
var storeId = wx.getStorageSync('storeId');
var dialog = require("../../../utils/dialog")
Page({
  data: {
    nickName: '',
    successState:false,  //提示框状态
    memberId: ''      //用户id
  },
  onLoad: function () {
  
  },
  onShow: function () {
    var that = this;
    var nickName = '';
    if (wx.getStorageSync('userNickname')){
      nickName = wx.getStorageSync('userNickname');
    }else{
      nickName = "游客";
    }
    //获取用户信息
    that.setData({
      nickName: nickName,
      headUrl: wx.getStorageSync('headUrl'),
      memberId: wx.getStorageSync('id')
    })
  },
  bindKeyInput: function (e) {
    this.setData({
      nickName: e.detail.value
    })
  },
  //提交函数
  saveFunc:function(){
    var that = this;
    var uploadUrl = constant.devUrl + "/miniapp/member/updatefieldvalue.json";
    var nickNameValue = that.data.nickName.replace(/\s/g, ''),
        memberId = that.data.memberId;
    //console.log(nickNameValue);
    var app_id = wx.getStorageSync('appId');
    //是否有会员id
    var sendData = {};
    sendData = {
      memberId: memberId,
      key: 'userNickname',
      value: nickNameValue,
      storeId: storeId,
      oldPath:''
    }
    var sign = util.MD5(util.paramConcat(sendData));
    console.log(sendData);
    if (nickNameValue){
        wx.request({
          url: uploadUrl, //接口地址
          data: sendData,
          header: {
            'wxa-sessionid': wx.getStorageSync("sessionId"),
            'version': constant.version,
            'sign':sign
          },
          success: function (res) {
            console.log(res);
            //成功后显示提示框
            that.setData({
              successState: true
            })
            //提交成功后，提示框显示3秒后消失
            setTimeout(function () {
              that.setData({
                successState: false
              })
            }, 3000)
          },
          fail: function (res) {
            dialog.toast(res.data.errMsg)
          }
        })
    }else{
       dialog.toast("昵称不能为空")
    }
  }
})