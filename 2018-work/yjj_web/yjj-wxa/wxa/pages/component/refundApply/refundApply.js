// 申请售后
var dialog = require("../../../utils/dialog"),
    constant = require("../../../constant");
var app = getApp()
/**
 * 初始化商品数据
 */
function initData(that, orderId, skuId){
  var param = {
    data: {
      orderId: orderId,
      skuId:skuId
    },
    loading: true
  };
  //console.log(sendData);
  //售后列表
  app.orderApi.getRefundProductInfo(param).then(res => {
    var data = res.data;
    console.log("售后列表数据", data);
    that.setData({
      productData: data,
      maxCount:data.count,
      refundMoney: data.refundMoney
    })
  })
}
/**
 * 替换空白字符
 */
function replaceSpace(str){
  return str.replace(/\s/g, '')
}
Page({
  data: {
    storeId: '',             //门店id
    memberId: '',            //会员id
    productData: [],
    maxCount: [],            //最大数量
    type0:false,             //退款
    type1:false,             //退货退款
    chooseType:'',           //选中的服务类型
    buyNumber:'',            //商品数量
    name:'',                 //姓名  
    phone:'',                //手机号  
    reason:'',               //退款原因     
    nameError:false,         //姓名为空错误
    phoneError:false,        //手机为空错误
    textLength:0,            //退款原因文字长度
    saveRefundImg:'',        //需要提交的图片
    refundImgArray: []       //退款凭证图片显示列表 
  },
  onLoad: function (option) {
    var that = this,
      skuId = option.skuId,
      orderId = option.orderId;
    that.setData({
      skuId:skuId,
      orderId: orderId
    })  
  },
  onShow: function () {
    var that = this,
        memberId = wx.getStorageSync('id'),
        storeId = wx.getStorageSync('storeId'),
        orderId = that.data.orderId,
        skuId = that.data.skuId;
    that.setData({
      storeId: storeId,
      memberId: memberId
    })
    initData(that, orderId, skuId);
  },
  //服务类型选择
  chooseRefundType:function(e){
    var typeVal = e.target.dataset.type,
        that = this;
    if (typeVal == '0'){
      that.setData({
        type0:true,
        type1:false,
        chooseType: typeVal
      })
    }else{
      that.setData({
        type0: false,
        type1: true,
        chooseType: typeVal
      })
    }
  },
  //增加数量
  addCount: function (e) {
    var count = e.target.dataset.count,
        productData = this.data.productData,
        refundMoney = this.data.refundMoney,
        maxCount = this.data.maxCount,
        num = parseInt(count);
    num = num + 1;  
    if (num > maxCount) {
      num = 1;
      dialog.toastError("数量不能大于" + maxCount);
      return;
    }
    var averageRefundMoney = app.util.toDecimal(parseFloat(refundMoney) / parseInt(maxCount)),
        doRefundMoney = app.util.toDecimal(averageRefundMoney * parseInt(num));
    productData.count = num;
    productData.refundMoney = doRefundMoney;
    this.setData({
      productData: productData
    }); 
    console.log("productData", productData);
  },
  //减少数量
  reduceCount: function (e) {
    var count = e.target.dataset.count,
        refundMoney = this.data.refundMoney,
        productData = this.data.productData,
        maxCount = this.data.maxCount,
        num = parseInt(count);   
    if (num <= 1) {
      num = 1;
      dialog.toastError("数量不能小于1");
      return false;
    }
    num = num - 1;
    var averageRefundMoney = app.util.toDecimal(parseFloat(refundMoney) / parseInt(maxCount)),
        doRefundMoney = app.util.toDecimal(averageRefundMoney * parseInt(num));
    productData.count = num;
    productData.refundMoney = doRefundMoney;
    this.setData({
      productData: productData
    });
    console.log("productData", productData);
  },
  //设置表单提交的值
  bindKeyInput: function (e) {
    let currentField = e.currentTarget.dataset.field;
    if (currentField == "name") {
      this.setData({
        name: e.detail.value
      });
    }
    else if (currentField == "phone") {
      this.setData({
        phone: e.detail.value
      });
    }
    else if (currentField == "reason") {
      var textVal = e.detail.value,
          currentLength = textVal.length;
      this.setData({
        reason: e.detail.value,
        textLength: currentLength
      });
    } 
  },
  //打开显示操作菜单
  changeImageFunc:function(){
    //console.log("选择图片函数");
    let that = this;
    wx.showActionSheet({
      itemList: ['相册', '拍照'],
      itemColor: "#333",
      success: function (res) {
        if (!res.cancel) {
          if (res.tapIndex == 0) {
            that.chooseImageUpload('album')
          } else if (res.tapIndex == 1) {
            that.chooseImageUpload('camera')
          }
        }
      }
    })
  },
  //选择上传的图片
  chooseImageUpload:function(){
    var that = this,
        refundImgArray = that.data.refundImgArray,
        currentLength = refundImgArray.length;
    if (currentLength >= 4){
      dialog.toast("最多上传4张凭证");
    }else{
      if (wx.chooseImage) {  
        wx.chooseImage({
          count: 4 - currentLength,              // 最多可以选择的图片张数
          sizeType: ['original', 'compressed'],  // original 原图，compressed 压缩图，默认二者都有
          sourceType: ['album', 'camera'],       // album 从相册选图，camera 使用相机，默认二者都有
          success: function (res) {
            var imgPaths = res.tempFilePaths;
            that.uploadImgFile(imgPaths[0]);
          },
          fail: function () {
            // fail
          },
          complete: function () {
            // complete
          }
        })
      }
    }
  },
  //上传图片函数
  uploadImgFile: function (imgPaths){
    var that = this,
        memberId = that.data.memberId,
        storeId = that.data.storeId,
        oldHeadUrl = '',
        refundImgArray = that.data.refundImgArray,
        uploadUrl = app.orderApi.uploadFileApi,
        formData = {
          newFile: imgPaths
        }
    console.log("formData", formData);    
    app.util.uploadFileFun(uploadUrl, imgPaths, 'refundPhoto', formData, function (res) {
      var resData = {};
      if (app.util.isObject(res)) {
        resData = res;
      } else {
        resData = JSON.parse(res);
      }
      console.log("resData", resData.data);
      //提交成功后显示图片
      that.setData({
        refundImgArray: refundImgArray.concat(resData.data)
      });
    }, function (res) {
      dialog.toast("图片超过大小了")
    })
  },
  //删除图片
  deleteImg: function (e) {
    var that = this,
        srcValue = e.currentTarget.dataset.value,
        picArray = that.data.refundImgArray;
    app.util.removeByValue(picArray, srcValue);
    that.setData({
      refundImgArray: picArray
    });
  },
  //图片放大预览
  previewImg:function(e){
    var currentUrl = e.target.dataset.value,
        refundImgArray = this.data.refundImgArray;
    wx.previewImage({
      current: currentUrl, 
      urls: refundImgArray 
    })
  },
  //提交申请
  submitRefund:function(){
    var re_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/,
        that = this,
        orderId = that.data.orderId,
        storeId = that.data.storeId,
        skuId = that.data.skuId,
        refundType = that.data.chooseType,
        name = replaceSpace(that.data.name),
        phone = that.data.phone,
        reason = that.data.reason;
    if (!name){
      dialog.toastError("请输入联系人名");
      that.setData({
        nameError:true
      })
      return 
    } 
    if (!phone) {
      dialog.toastError("请输入正确联系方式");
      that.setData({
        phoneError: true
      })
      return 
    }
    if (!re_phone.test(phone)) {
      dialog.toastError("请输入正确联系方式");
      that.setData({
        phoneError: true
      })
      return
    }
    var refundImg = that.data.refundImgArray.join(','),
        refundMoney = that.data.productData.refundMoney,
        doCount = that.data.productData.count;
    var param = {
      data:{
        storeId: storeId,
        orderId: orderId,
        skuId: skuId,
        name: name,
        phone: phone,
        type: refundType,
        reson: reason,
        refundMoney: refundMoney,
        count: that.data.productData.count,
        photo1:refundImg
      }
    }
    console.log("param", param);
    //售后列表
    app.orderApi.refundApply(param).then(res => {
      var data = res.data;
      console.log("返回数据", data);
      dialog.toast("提交成功");
      setTimeout(function () {
        wx.navigateBack({
          delta: 1
        })
      }, 1000)
    })
  },
  //获取焦点移除红色样式
  removeStyle: function (e) {
    var that = this,
        fieldName = e.currentTarget.dataset.field;
    if (fieldName == "name") {
      that.setData({
        nameError: false
      });
    }
    if (fieldName == "phone") {
      that.setData({
        phoneError: false
      });
    }
  }
})