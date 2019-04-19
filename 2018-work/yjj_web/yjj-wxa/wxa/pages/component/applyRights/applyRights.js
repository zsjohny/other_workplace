// 申请权益
var dialog = require("../../../utils/dialog");
var app = getApp()
/**
 * 提交申请函数
 */
var applySubmit = function (that,submitData) {
  var param = {
    data: submitData,
    loading:true
  }
  app.distributionApi.applyRights(param).then(res => {
    that.setData({
      successState: true
    })
  })
}
Page({
  data: {
    storeId: '',        //门店id
    memberId:'',        //会员id
    grade:'',            //分销商级别
    rightsData:{},       //数据
    successState:false,  //申请是否成功状态
    agreeState: false,   //是否勾选协议状态
    name:'',
    wxNumber: '',
    phone:'',
    identityCard:''
  },
  onLoad: function (options) {
    var that = this,
        storeId = wx.getStorageSync('storeId'),
        memberId = wx.getStorageSync('id');
    that.setData({
      memberId: memberId,
      storeId: storeId,
      grade: options.grade
    })
  },
  onShow: function () {
    
  },
  //设置表单提交的值
  bindKeyInput:function(e){
    //设置表单提交的值
    var currentFiled = e.currentTarget.dataset.field;
    console.log(currentFiled);
    if (currentFiled == "name") {
      this.setData({
        name: e.detail.value
      });
    } else if (currentFiled == "wxNumber"){
      this.setData({
        wxNumber: e.detail.value
      });
    } else if (currentFiled == "phone") {
      this.setData({
        phone: e.detail.value
      });
    } else if (currentFiled == "identityCard") {
      this.setData({
        identityCard: e.detail.value
      });
    }
  },
  //申请提交和校验
  applyRightsSubmit:function(e){
    var that = this,
        memberId = that.data.memberId,
        name = that.data.name,
        wxNumber = that.data.wxNumber,
        phone = that.data.phone,
        identityCard = that.data.identityCard,
        // agreeState = that.data.agreeState,
        reg_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/,
        reg_card = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/; 
    if (!name) {
      dialog.toastError("请输入真实姓名")
      return
    } 
    if (!wxNumber) {
      dialog.toastError("请输入真实微信号")
      return
    } 
    if (!phone) {
      dialog.toastError("请输入真实手机号")
      return
    } 
    if (!reg_phone.test(phone)) {
      dialog.toastError("请输入正确手机号")
      return
    }    
    if (!identityCard) {
      dialog.toastError("请输入真实身份证号")
      return
    } 
    if (!reg_card.test(identityCard)) {
      dialog.toastError("请输入正确身份证号")
      return
    }
    // if (!agreeState) {
    //   dialog.toastError("请勾选协议")
    //   return
    // }
    var submitData = {
      userId: memberId,
      realName: name,
      wxNum: wxNumber,
      phone: phone,
      idCard: identityCard
    }         
    applySubmit(that,submitData);
  },
  //关闭弹窗
  closeXbox:function(){
    this.setData({
      successState:false
    })
  },
  //返回个人中心
  gotoMyPage:function(){
    var url = '../../my/my';
    wx.switchTab({
      url: url
    })
  },
  //同意协议
  agreeFunc:function(){
    var agreeState = this.data.agreeState;
    console.log("agreeState", agreeState);
    if (agreeState){
      this.setData({
        agreeState:false
      })
    }else{
      this.setData({
        agreeState: true
      })
    }
  },
  //跳转至合伙人协议
  gotoAgreementPage: function () {
    var url = '',
        grade = this.data.grade;
    if(grade == 1){
      url = '../distributorAgreement/distributorAgreement' 
    }else{
      url = '../partnerAgreement/partnerAgreement'
    }    
    app.common.judgeNavigateTo(url)
  },
})