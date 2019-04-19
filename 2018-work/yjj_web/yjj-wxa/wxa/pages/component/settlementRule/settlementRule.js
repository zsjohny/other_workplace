// 结算规则
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
Page({
  data: {
    websiteUrl: constant.devUrl + '/wxa/settlementRule.html'
  },
  onLoad: function () {
    var that = this;
  },
  onShow: function () {
    dialog.loading();
    setTimeout(function(){
      dialog.hide()
    },1000)
  }
})