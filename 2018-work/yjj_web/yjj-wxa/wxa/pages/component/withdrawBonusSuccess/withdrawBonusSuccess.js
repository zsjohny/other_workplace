//立即提现
var app = getApp()
/**
 * 获取初始化数据
 */
var getInitData = function (that, memberId) {
  var sendData = {
    data: {
      userId: memberId //会员id
    },
    loading: true
  };
  app.distributionApi.getWithdrawSuccessData(sendData).then(res => {
    var data = res.data;
    that.setData({
      withdrawData: data
    })
  })
}
Page({
  data: {
    memberId: '',                 //会员id
    withdrawData:'',             //页面初始化数据
  },
  onLoad: function (options) {
    var that = this,
        money = options.money,
        createTime = options.createTime,
        userName = options.userName;
    that.setData({
      money: money,
      createTime: createTime,
      userName: userName
    })    
  },
  onShow: function () {
    var that = this,
        memberId = wx.getStorageSync('id');
    getInitData(that, memberId);    
  },
  //返回个人中心
  gotoMypage:function(){
    var url = '../../my/my';
    wx.switchTab({
      url: url
    })
  }
})