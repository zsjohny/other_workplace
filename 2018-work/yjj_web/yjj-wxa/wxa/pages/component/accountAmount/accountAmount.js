// 账户明细
var dialog = require("../../../utils/dialog"),
    util = require("../../../utils/util"),
    data = require("data");
var app = getApp()
/**
 * 上拉加载更多通用请求函数
 * @param isFilter 1-是筛选的加载更多
 */
var page = 2;
var page_size = 10;
var getMoreList = function (that) {
  //当已经显示全部时，下拉不显示loading
  console.log("moreOver", that.data.moreOver);
  if (that.data.moreOver) {
    that.setData({
      hasMore: false,
      moreComplete: true
    })
  } else {
    that.setData({
      hasMore: true,
      moreComplete: false
    })
  }
  var sendData = {};
  //筛选时
  if (that.data.isFilter){
    var chooseTypeIndex = that.data.chooseTypeIndex,
        chooseStateIndex = that.data.chooseStateIndex,
        chooseKindIndex = that.data.chooseKindIndex;
    sendData = {
      userId: that.data.memberId,
      inOutType: chooseTypeIndex,
      status: chooseStateIndex,
      type: chooseKindIndex,
      choose: 0,
      page: page       
    };
  }else{
    sendData = {
      userId: that.data.memberId,
      choose: 0,
      page: page      
    };
  }  
  var param = {
    data: sendData
  }
  app.distributionApi.getBillDetails(param).then(res => {
    var accountDataArray = that.data.listData,
        data = res.data;
    //有数据
    if (data.length > 0) {
      that.setData({
        listData: accountDataArray.concat(data),
        moreComplete: true
      })
    } else {
      //没有数据了
      that.setData({
        hasMore: false,
        moreOver: true,
        moreComplete: false
      });
      console.log("moreOver", that.data.moreOver);
    }
    page++;
  })
}
/**
 * 初始化列表数据
 */
var initData = function (that, memberId){
  page = 2;
  var sendData = {
    choose:0, 
    page: 1        //页码
  }
  var param = {};
  param.data = sendData;
  param.loading = true;
  //console.log(sendData);
  //佣金数据
  app.distributionApi.getAccountBill().then(res => {
    var data = res.data;
    console.log("数据", data);
    that.setData({
      billData: data,
      grade:data.grade,
      commissionBill: data.commissionBill,
      manageBill: data.manageBill
    })
  })
  //收支列表
  app.distributionApi.getBillDetails(param).then(res => {
    var data = res.data;
    console.log("收支列表数据", data);
    that.setData({
      listData: data
    })
  })
}
Page({
  data: {
    storeId: '',            //门店id
    memberId: '',            //会员id
    billData: '',          //佣金数据
    listData:'',          //列表数据
    hasMore: false,       //底部加载更多loading控制
    moreComplete: true,   //加载完成
    moreOver: false,      //没有更多数据
    filterXboxState:false,  //筛选弹窗显示控制
    typeData: [],     //类型的数据列表
    stateData: [],   //状态的数据列表
    kindData: [],     //种类的数据列表
    chooseTypeIndex:'',      //选中的类型
    chooseStateIndex: '',      //选中的状态
    chooseKindIndex: '',      //选中的种类
    isFilter: 0,               //1-筛选的加载更多
    goTopShowState: false  //返回顶部显示状态
  },
  onLoad: function () {
    var that = this;
  },
  onShow: function () {
    var that = this,
        memberId = wx.getStorageSync('id'),
        storeId = wx.getStorageSync('storeId');
    that.setData({
      storeId: storeId,
      memberId: memberId,
      typeData: data.typeData,     
      stateData: data.stateData,  
      kindData: data.kinCashData
    })
    console.log(that.data.typeData);
    initData(that, memberId);
  },
  //显示说明
  showExplain:function(){
    dialog.toastText("管理奖金每月5号发放，发放后的奖金才可提现")
  },
  //下拉刷新
  onPullDownRefresh: function () {
    var that = this;
    that.setData({
      moreComplete: true,
      moreOver: false
    })
    initData(that, that.data.memberId);
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 500)  
  },
  //上拉加载更多
  onReachBottom: function () {
    var that = this;
    //console.log("上拉加载moreComplete", this.data.moreComplete);
    if (this.data.moreComplete) {
      getMoreList(that);
    }
  },
  //显示筛选弹窗函数
  showFilterXbox: function () {
    let that = this;
    console.log(that);
    that.setData({
      filterXboxState: true
    })
  },
  //关闭筛选弹窗函数
  closeFilterXbox: function () {
    let that = this;
    that.setData({
      filterXboxState: false
    })
  },
  //筛选选择函数
  chooseType:function(e){
    var that = this,
        clickIndex = e.currentTarget.dataset.click,
        chooseTypeIndex = that.data.chooseTypeIndex,
        chooseStateIndex = that.data.chooseStateIndex,
        chooseKindIndex = that.data.chooseKindIndex,
        code = e.target.dataset.code;
    //类型
    if (clickIndex == '1'){
      that.setData({
        chooseTypeIndex: code
      })
    }
    //状态
    if (clickIndex == '2') {
      that.setData({
        chooseStateIndex: code
      })
    } 
    //种类
    if (clickIndex == '3') {
      that.setData({
        chooseKindIndex: code
      })
    }     
  },
  //筛选确定选择函数
  chooseConfirm:function(){
    let that = this;
    page = 2;
    var chooseTypeIndex = that.data.chooseTypeIndex,
        chooseStateIndex = that.data.chooseStateIndex,
        chooseKindIndex = that.data.chooseKindIndex;
    let sendData = {
      userId: that.data.memberId,
      inOutType: chooseTypeIndex,
      status: chooseStateIndex,
      type: chooseKindIndex,
      choose: 0, 
      page: 1        //页码
    };
    console.log("筛选的sendData", sendData);
    var param = {};
    param.data = sendData;
    param.loading = true;
    //收支列表
    app.distributionApi.getBillDetails(param).then(res => {
      var data = res.data;
      console.log("收支列表数据", data);
      that.setData({
        listData: data,
        isFilter:1,
        hasMore: false,       
        moreComplete: true,  
        moreOver: false     
      })
      that.closeFilterXbox()
    })
  },
  //跳转至收入详情，flag-区分金币详情、金额详情
  gotoAmountDetail:function(e){
    var id = e.currentTarget.dataset.id,
        inOutType = e.currentTarget.dataset.incometype,
        url = '../incomeDetail/incomeDetail?id=' + id + "&flag=amount" + "&inOutType=" + inOutType;  
    app.common.judgeNavigateTo(url)
  },  
  //返回顶部
  goTop: function (e) {
    util.gotoTop()
  },
  //页面滑动显示置顶按钮
  onPageScroll: function () {
    var that = this;
    util.onPageScroll(that)
  }
})