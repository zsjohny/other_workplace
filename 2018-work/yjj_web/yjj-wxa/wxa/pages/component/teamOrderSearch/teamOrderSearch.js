//订单搜索
var app = getApp()
var dialog = require("../../../utils/dialog")
Page({
  data: {
    orderShow: true,
    historyShow: true,
    keywordList: [],
    orderList: [],
    orderNoKeyword:'', //input搜索关键词
    clickKeyword: '', //点击记录的搜索关键词
    searchBtn: true,  //搜索按钮搜索
    page: 1
  },
  onLoad: function () {
    var searchArr = wx.getStorageSync('searchInfo') || []
    this.setData({
        keywordList: searchArr.reverse()
    })
  },
  onShow: function () {

  },
  //获取搜索列表数据
  initOrderList: function(orderNo){
    var param = {
      data: {
        page: this.data.page,
        orderNo: orderNo
      }
    }
    app.distributionApi.teamOrderList(param).then(res => {
      console.log(res)
      this.setData({
        orderList: res.data,
        orderShow: false
      })
    })
  },
  goDetail: function (e) {
    console.log(e)
    wx.navigateTo({
        url: "/pages/component/teamOrderDetail/teamOrderDetail?orderNo="+ e.currentTarget.dataset.select
    })
  },
  backPrevPage: function(){
    var url = "/pages/component/teamOrderList/teamOrderList";
    app.common.judgeNavigateTo(url);
  },
  //搜索函数
  searchFunc: function() {
    var searchArr = wx.getStorageSync('searchInfo') || [],
        orderNoKeyword = this.data.orderNoKeyword,
        clickKeyword = this.data.clickKeyword,
        searchOrderNo = '';   //要用去搜索的订单号
    //判断input输入的还是点击关键赋值
    if (clickKeyword == ''){
      //判断数组中是否已存在
      let arrIndex = searchArr.indexOf(orderNoKeyword);
      console.log(searchArr.indexOf(orderNoKeyword));
      if (arrIndex != -1) {
        //删除已存在后重新插入至数组
        searchArr.splice(arrIndex, 1);
        searchArr.push(orderNoKeyword);
      } else {
        searchArr.push(orderNoKeyword);
      }
      searchOrderNo = orderNoKeyword;
    }else{
      let arrNumber = searchArr.indexOf(clickKeyword);
      if (arrNumber != -1) {
        searchArr.splice(arrNumber, 1);
        searchArr.push(clickKeyword);
      } else {
        searchArr.push(clickKeyword);
      }
      searchOrderNo = clickKeyword;
    }
    wx.setStorageSync('searchInfo', searchArr);
    this.initOrderList(searchOrderNo);
  },
  //点击关键搜索
  keywordBtnSearch:function(e){
    var keyValue = e.currentTarget.dataset.value,
        that = this;
    that.setData({
      clickKeyword: keyValue
    })
    this.searchFunc();
  },
  //显示搜索按钮
  searchSeekInput: function(e){
    var that = this;
    console.log(e.detail.value);
    var inputValue = e.detail.value;
    if (inputValue != "") {
      that.setData({
        searchBtn: false,
        orderNoKeyword: inputValue,
      })
    } else {
      that.setData({
        searchBtn: true,
        orderNoKeyword: inputValue
      })
    }
  },
  //清空记录
  searchClear: function() {
    var that = this;
    wx.showModal({
      title: '确认清空历史记录吗？',
      success (res) {
        if(res.confirm) {
          wx.removeStorage({
            key: 'searchInfo',
            success (res) {
              dialog.toast('清除成功')
              that.setData({
                  keywordList: []
              })
            }
          })
        }
      }
    })
  }
})
