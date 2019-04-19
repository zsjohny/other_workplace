// 合伙人协议
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
Page({
  data: {
    websiteUrl: constant.devUrl + '/wxa/partnerAgreement.html'
  },
  onLoad: function () {
    
  },
  onShow: function () {
    dialog.loading();
    setTimeout(function () {
      dialog.hide()
    }, 1000)
  }
})