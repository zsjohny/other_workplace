var dialog = {}
//loading
dialog.loading = function(title = "加载中"){
    wx.showToast({title:title,icon:'loading',mask:true,duration:3000})
}
//隐藏提示框
dialog.hide = function(){
    wx.hideToast();
}
//成功提示框
dialog.toast = function(title="提示您"){
    wx.showToast({title:title,icon:'success'})
}
//普通文本错误提示框-可以显示7个以上汉字
dialog.toastError = function (title) {
  wx.showToast({
    title: title,
    icon: 'none',
    duration: 2000
  })
}
//普通文本提示框-可以显示7个以上汉字
dialog.toastText = function (title) {
  wx.showToast({
    title: title,
    icon: 'none',
    duration: 3000
  })
}
module.exports = dialog