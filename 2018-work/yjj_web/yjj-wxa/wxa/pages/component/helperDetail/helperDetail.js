
var app = getApp()
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var WxParse = require('../../../wxParse/wxParse.js');
Page({
  data: {
    id: '',
    qaInfo: '',
    isUserful: true,
    use: 0
  },
  onLoad: function (options) {
    console.log(options,'/////')
    this.setData({
      id: options.id
    })
  },
  onShow: function () {
    this.initDetail()
  },
  initDetail: function() {
    var that = this;
    var param = {
      data: {
        id: this.data.id
      }
    }
    app.publicApi.questionDetail(param).then(res => {
      console.log(res)
      if (res.code === 200) {
        this.setData({
          qaInfo: res.data,
          answer: res.data.answer
        })
        WxParse.wxParse('answer', 'html', res.data.answer, that, 5);
      } else {

      }
    })
  },
  useful: function(e) {
    console.log(e.currentTarget.dataset.select,'wwwwww')
    var param = {
      data: {
        id: this.data.id,
        useful: e.currentTarget.dataset.select
      }
    }
    app.publicApi.isUserful(param).then(res => {
      console.log(res)
      if (res.code === 200) {
        this.setData({
          use: e.currentTarget.dataset.select,
          isUserful: false
        })
      } else {

      }
    })
  }
})
