//文章详情
var app = getApp()
var util = require("../../../utils/util")
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
var url = constant.devUrl + "/miniapp/homepage/getArticleDetail.json";
//初始化数据
function initDate(that, store_id,member_id, article_id){
  var sendData = {
    storeId: store_id,
    memberId: member_id,
    articleId: article_id
  }
  var sign = util.MD5(util.paramConcat(sendData));
  //console.log(sendData);
  dialog.loading();
  wx.request({
    url: url, //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      console.log("文章详情：", res);
      var data = res.data.data;
      if (res.data.successful) {
        var content = data.context;
        //console.log(content);
        //1.4.0以上版本
        //var contentObj = JSON.parse(content.replace(/\n/g, "\\n").replace(/\r/g, "\\r"));
        // for (var i = 0, l = contentObj.length; i < l; i++){
        //   var contentItem = contentObj[i].content,
        //       linkIndex = contentItem.indexOf("Σ"),
        //       linkText = contentItem.slice(0,linkIndex),
        //       linkUrl = contentItem.slice(linkIndex + 1),
        //     contentItem = "<navigator url='" + linkUrl +"'>" + linkText + "</navigator>";
        //   contentObj[i].content = contentItem;
        // } 
        var contentStr = content.replace(/\n/g, "\\n").replace(/\r/g, "\\r");
        var contentObj = JSON.parse(contentStr);
        that.setData({
          articleContent: contentObj,
          articleTitle: data.title
        })
      } 
    },
    complete: function () {
      setTimeout(function () {
        dialog.hide();
      }, 500)
    }
  })
}
//登录获取用户信息
var loginGetUserData = function (that, article_id) {
  var fromValue = 0,
      store_id = wx.getStorageSync('storeId');
  app.loginAuthorizeFun(fromValue, function (id) {
    initDate(that, store_id, id, article_id)
  })
}
Page({
  data: {
    articleId:'',        //文章id
    articleTitle:'',     //文章标题
    articleContent:''    //文章内容
  },
  onLoad: function (options){
    var that = this;
    var articleId = options.articleId;
    console.log("文章id", articleId);
    that.setData({   //保存商品id
      articleId: articleId
    })
  },
  onShow:function(){
    var that = this;
    loginGetUserData(that, that.data.articleId);
  }
})