//订单详情
var app = getApp()
/**
 * 获取初始化数据
 */
var getInitData = function (that, memberId) {
  var param = {
    data:{
      orderNo: that.data.orderNo
    },
    loading: true
  };
  app.distributionApi.teamOrderDetail(param).then(res => {
    var data = res.data;
    that.setData({
      detailData: data.order,
      record: data.record,
      orderItemSku: data.orderItemSku
    })
  })
}
Page({
  data: {
    memberId: ''    //会员id
  },
  onLoad: function (options) {
    var that = this,
      memberId = wx.getStorageSync('id');
    that.setData({
      memberId: memberId,
      orderNo: options.orderNo
        // orderNo: '15338060744311077'
    })
  },
  onShow: function () {
    var that = this;
    getInitData(that, that.data.memberId);
  }
})
