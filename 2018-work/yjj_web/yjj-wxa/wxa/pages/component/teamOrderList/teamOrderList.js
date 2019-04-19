//编辑个人信息
var app = getApp()
var id = wx.getStorageSync('id');

Page({
  data: {
    orderList: [],
    page: 1,
    pullSuccess: false,
    noOrderList: false,
    noMoreOrderList: false,
  },
  onLoad: function () {
      this.init()
      this.getOrderCount()
  },
  onShow: function () {

  },
  init: function(){
      var param = {
          data: {
              page: this.data.page
          }
      }
      app.distributionApi.teamOrderList(param).then(res => {
          if(res.code === 200) {
              this.setData({
                  pullSuccess: true
              })
              if(res.data.length > 0) {
                  if(this.data.page == 1) {
                      this.setData({
                          orderList: res.data
                      })
                  }else{
                      if(res.data.length > 0) {
                          res.data.forEach((ele) => {
                              this.data.orderList.push(ele)
                          })
                      }
                  }
              }else{
                  if(this.data.page > 1){
                      this.setData({
                          noMoreOrderList: true
                      })
                  }
              }
          }
      })
  },
  getOrderCount: function(){
      var param = {
          userId: id
      }
      app.distributionApi.teamOrderCount(param).then(res => {
          console.log(res,'res...')
          if(res.code === 200) {
              this.setData({
                  count: res.data,
                  pullSuccess: true
              })
          }

      })
  },
  onPullDownRefresh: function() { // 下拉刷新
      this.init()
      this.getOrderCount()
      if(this.data.pullSuccess) {
          setTimeout(()=>{
              wx.stopPullDownRefresh()
          },300)
      }

  },
  onReachBottom:function() { // 拉到底部
      if(!this.data.noOrderList) {
          this.data.page++;
          console.log(this.data.page)
          this.init()
      }
  },
  goSearch: function() {
    var url = "/pages/component/teamOrderSearch/teamOrderSearch";
    app.common.judgeNavigateTo(url);
  },
  showOrderInfo: function(){
    wx.showToast({
        title: '订单总数统计团队所有订单，包括自己的订单',
        icon: 'none'
    })
  },
  goDetail: function (e) {
     console.log(e)
      wx.navigateTo({
          url: "/pages/component/teamOrderDetail/teamOrderDetail?orderNo="+ e.currentTarget.dataset.select
      })
  }
})
