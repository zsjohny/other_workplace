// 权益
var util = require("../../../utils/util")
var app = getApp()

/**
 * 比例百分比格式化计算
 */
var ratioFormatter = function(that){
  var shopowner = that.data.shopowner,
      distributor = that.data.distributor,
      partner = that.data.partner;
  var shopownerNew = {
    "commissionCurrencyRatio": util.toDecimal(shopowner.commissionCurrencyRatio * 100), 
    "commissionCashRatio": util.toDecimal(100 - shopowner.commissionCurrencyRatio * 100), 
    "fans1CommissionEarningsRatio": util.toDecimal(shopowner.fans1CommissionEarningsRatio * 100),
    "fans2CommissionEarningsRatio": util.toDecimal(shopowner.fans2CommissionEarningsRatio * 100), 
    "selfCommissionEarningsRatio": util.toDecimal(shopowner.selfCommissionEarningsRatio * 100)
  },
  distributorNew = {
    "commissionCurrencyRatio": util.toDecimal(distributor.commissionCurrencyRatio * 100),
    "commissionCashRatio": util.toDecimal(100 - distributor.commissionCurrencyRatio * 100), 
    "fans1CommissionEarningsRatio": util.toDecimal(distributor.fans1CommissionEarningsRatio * 100),
    "fans2CommissionEarningsRatio": util.toDecimal(distributor.fans2CommissionEarningsRatio * 100),
    "managerEarningsRatio": util.toDecimal(distributor.managerEarningsRatio * 100),
    "selfCommissionEarningsRatio": util.toDecimal(distributor.selfCommissionEarningsRatio * 100)
  },
  partnerNew = {
    "commissionCurrencyRatio": util.toDecimal(partner.commissionCurrencyRatio * 100),
    "commissionCashRatio": util.toDecimal(100 - partner.commissionCurrencyRatio * 100), 
    "fans1CommissionEarningsRatio": util.toDecimal(partner.fans1CommissionEarningsRatio * 100),
    "fans2CommissionEarningsRatio": util.toDecimal(partner.fans2CommissionEarningsRatio * 100),
    "managerEarningsRatio": util.toDecimal(partner.managerEarningsRatio * 100),
    "selfCommissionEarningsRatio": util.toDecimal(partner.selfCommissionEarningsRatio * 100)
  };
  that.setData({
    shopowner: shopownerNew,
    distributor: distributorNew,
    partner: partnerNew
  })
  console.log("that.data.shopowner",that.data.shopowner);
}
/**
 * 获取权益信息
 */
var getRightsData = function (that) {
  var param = {
    loading:true
  }
  app.distributionApi.getPromoteCondition(param).then(res => {
    var data = res.data,
        grade = data.grade,
        condition = data.condition,
        done = data.done,
      earningsStrategy = data.earningsStrategy;

    that.setData({
      grade: grade,
      isTrue: data.isTrue,
      status: data.status,
      condition: condition,
      shopowner: earningsStrategy[1],
      distributor: earningsStrategy[2],
      partner: earningsStrategy[3]
    })
    if (data.explain){
      that.setData({
        explain: data.explain
      })
    }
    if (condition.classA) {
      that.setData({
        followerTotal: condition.classA,   //设置粉丝满足数
        haveFollower: done.classA         //已有一级粉丝数
      })
    }
    //如果角色是分粉丝，设置已购买的件数
    if (grade == 0) {
      that.setData({
        countTotal: condition.buyCount,
        haveBuyCount: done.buyCount,
      })
    }
    //如果角色是分销商，设置分销商满足数
    if (grade == 2){
      that.setData({
        followerTotal: condition.distributor,  
        haveFollower: done.distributor      
      })
    }
    //如果角色是分销商和店长时，设置月销售额满足数、已销售额
    if(grade == 1 || grade == 2){
      that.setData({
        salesTotal: condition.countMoney,
        haveSalesVolume: done.countMoney
      })
    }
    //如果角色是店长时，设置一级粉丝+二级粉丝邀请满足总数、已邀请数
    if (grade == 1) {
      that.setData({
        inviterTotal: condition.classAB,
        haveInviter: done.classAB
      })
    }
    calculate(that, data);
    ratioFormatter(that);
  })
}
/**
 * 计算进度条函数
 */
const calculate = function (that, data){
  var haveFollower = that.data.haveFollower,
      haveInviter = that.data.haveInviter,
      haveSalesVolume = that.data.haveSalesVolume,
      haveBuyCount = that.data.haveBuyCount,

      followerTotal = that.data.followerTotal,            
      inviterTotal = that.data.inviterTotal,            
      salesTotal = that.data.salesTotal,  
      countTotal = that.data.countTotal,

      haveFollowerWidth = '',
      haveInviterWidth = '',
      salesVolumeWidth = '',
      countWidth = '',
      viewWidth = 654,             //进度条最大宽度    
      minWidth = 120;             //进度条最小宽度        
  //如果角色是粉丝
  if (data.grade == 0) {
    countWidth = (parseInt(haveBuyCount) / parseInt(countTotal)) * viewWidth;
    if (countWidth > viewWidth){
      countWidth = viewWidth;
    }
    if (countWidth < minWidth) {
      countWidth = minWidth;
    }
    //剩余的进度条宽度
    let surplusWidth = 654 - countWidth; 
    if (surplusWidth < minWidth){
      surplusWidth = minWidth;
    }
    
    that.setData({
      countWidth: countWidth,
      surplusCountWidth:{
        width: surplusWidth, 
        left: countWidth
      },
      surplusCount: countTotal - haveBuyCount
    })
    console.log("111", that.data.surplusCountWidth);
  }
  //如果是有一级粉丝
  if (data.grade != 3){
    haveFollowerWidth = (parseInt(haveFollower) / parseInt(followerTotal)) * viewWidth;
    if (haveFollowerWidth > viewWidth) {
      haveFollowerWidth = viewWidth;
    }
    if (haveFollowerWidth < minWidth) {
      haveFollowerWidth = minWidth;
    }
    //剩余的进度条宽度
    let surplusWidth = 654 - haveFollowerWidth;
    if (surplusWidth < minWidth) {
      surplusWidth = minWidth;
    }
    that.setData({
      surplusFollower: followerTotal - haveFollower,
      followerWidth: haveFollowerWidth,
      surplusFollowerWidth: {
        width: surplusWidth,
        left: haveFollowerWidth
      }
    })  
  }
  //如果是店长，一级粉丝+二级粉丝
  if (data.grade == 1) {
    haveInviterWidth = (parseInt(haveInviter) / parseInt(inviterTotal)) * viewWidth;
    if (haveInviterWidth > viewWidth) {
      haveInviterWidth = viewWidth;
    } 
    if (haveInviterWidth < minWidth) {
      haveInviterWidth = minWidth;
    }
    //剩余的进度条宽度
    let surplusWidth = 654 - haveInviterWidth;
    if (surplusWidth < minWidth) {
      surplusWidth = minWidth;
    }
    that.setData({
      surplusInviter: inviterTotal - haveInviter,
      inviterWidth: haveInviterWidth,
      surplusInviterWidth: {
        width: surplusWidth,
        left: haveInviterWidth
      }
    })  
  }
  //如果是店长和分销商，月销售额
  if (data.grade == 1 || data.grade == 2) {
    salesVolumeWidth = (parseFloat(haveSalesVolume) / parseFloat(salesTotal)) * viewWidth;
    if (salesVolumeWidth > viewWidth) {
      salesVolumeWidth = viewWidth;
    } 
    if (salesVolumeWidth < minWidth) {
      salesVolumeWidth = minWidth;
    }
    //剩余的进度条宽度
    let surplusWidth = 654 - salesVolumeWidth;
    if (surplusWidth < minWidth) {
      surplusWidth = minWidth;
    }
    that.setData({
      salesVolumeWidth: salesVolumeWidth,
      surplusSalesVolume: util.toDecimal(salesTotal - haveSalesVolume),
      surplusSalesVolumeWidth: {
        width: surplusWidth,
        left: salesVolumeWidth
      }
    })
  }
}
Page({
  data: {
    storeId: '',            //门店id
    memberId:'',           //会员id
    followerTotal: '',            //一级粉丝满足总数
    inviterTotal: '',            //（一级粉丝+二级粉丝）满足总数
    salesTotal: '',            //月销销售额满足总数
    countTotal: '',            //满足总件数

    haveFollower: '',            //已有一级粉丝数
    haveInviter: '',            //已有邀请者（一级粉丝+二级粉丝）
    haveSalesVolume: '',       //已达的月销销售额
    haveBuyCount: '',            //已购买的件数

    followerWidth: '',      //已有粉丝的进度条宽度
    inviterWidth: '',      //已有邀请者的进度条宽度
    salesVolumeWidth: '',   //已达销售额的进度条宽度
    countWidth: '',         //已达件数的进度条宽度

    surplusFollower:'',     //剩余粉丝数
    surplusInviter: '',     //剩余邀请者数
    surplusSalesVolume: '', //剩余销售额
    surplusCount: '',       //剩余件数
    popoverData:{},
    surplusCountWidth:{
      width: '111rpx',
      left: '400rpx'
    },
    explain:'',             //拒绝理由
    shopowner: '',          //各级别动态比例的数据
    distributor: '',
    partner: ''    
  },
  onLoad: function () {
    
  },
  onShow: function () {
    var that = this;
    getRightsData(that);
    that.setData({
      popoverData:{
        isShow: false
      }
    })
  },
  //跳转到申请页面
  gotoApplyRightPage:function(e){
    var grade = this.data.grade;
    wx.navigateTo({
      url: '../applyRights/applyRights?grade=' + grade
    })
  },
  //显示说明弹窗
  showRightsXbox:function(){
    console.log("显示"); 
    var grade = this.data.grade;
    var popoverData = {
      isShow: true,  //角色说明弹窗控制
      title: '提示',
      btnType: '2',
      oneBtnText: '我已了解',
      cancelEvent: 'closeXbox'
    }
    //粉丝
    if (grade == 0){
      popoverData.content= [
        '一级粉丝：您直接邀请的用户为您的一级粉丝',
        '二级粉丝：您直接邀请的用户，在邀请的用户为您的二级粉丝'
      ]
    }
    //店长
    if (grade == 1) {
      popoverData.content = [
        '一级粉丝：您直接邀请的用户为您的一级粉丝',
        '二级粉丝：您直接邀请的用户，在邀请的用户为您的二级粉丝',
        '团队消费：本人和本人直接或间接有关联人的消费总和',
        '团队消费：次月将会清零重新统计'
      ]
    }
    //分销商
    if (grade == 2) {
      popoverData.content = [
        '一级粉丝：您直接邀请的用户为您的一级粉丝',
        '团队消费：本人和本人直接或间接有关联人的消费总和',
        '团队消费：次月将会清零重新统计'
      ]
    }
    this.setData({
      popoverData: popoverData
    })
  },
  //关闭说明弹窗
  closeXbox:function(){
    console.log("关闭");
    this.setData({
      popoverData: {
        isShow: false
      }
    })
  },
  //显示拒绝理由弹窗
  showRefuseXbox:function(){
    var explain = this.data.explain;
    var popoverData = {
      isShow: true,  //角色说明弹窗控制
      title: '拒绝理由',
      content:[
        explain
      ],
      btnType: '1',
      cancelBtnText:'取消',
      confirmBtnText: '再次申请',
      cancelEvent: 'closeXbox',
      confirmEvent:'gotoApplyRightPage'
    }
    this.setData({
      popoverData: popoverData
    })
  }
})