var constant = require('../constant');
var md5File = require('md5');
var md5 = md5File.md5;
function formatTime(date) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}
function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}
/**
 * 删除数组方法
 */
function removeByValue(arr, val) {
  for (var i = 0; i < arr.length; i++) {
    if (arr[i] == val) {
      arr.splice(i, 1);
      break;
    }
  }
}
/**
 * 小数点位数处理
 */
function toDecimal2(x) {
  var f = parseFloat(x);
  if (isNaN(f)) {
    return false;
  }
  var f = Math.round(x * 100) / 100;
  var s = f.toString();
  var rs = s.indexOf('.');
  if (rs < 0) {
    rs = s.length;
    s += '.';
  }
  while (s.length <= rs + 2) {
    s += '0';
  }
  return s;
}
/**
 *  上传文件函数
 * 
 * @param  url-接口地址
 * @param  filePath-文件路径值
 * @param  keyName-key名字
 * @param  formData-上传的参数
 * @param  success-上传成功回调函数
 * @param  fail-上传失败回调函数
 **/
function uploadFileFun(uploadUrl, filePath, keyName, formData, success, fail) {
  //console.log(uploadUrl,formData);
  var sendData = formData;
  sendData.filePath = filePath;
  sendData.name = keyName;
  var sign = md5(paramConcat(sendData));
  wx.uploadFile({
    url: uploadUrl,
    filePath: filePath,
    name: keyName,
    header: {
      'content-type': 'multipart/form-data',
      'version': constant.version,
      'sign':sign
    }, // 设置请求的 header
    formData: formData, // HTTP 请求中其他额外的 form data
    success: function (res) {
      console.log(res,0);
      if (res.statusCode == 200 && !res.data.result_code) {
        typeof success == "function" && success(res.data);
      } else {
        typeof fail == "function" && fail(res);
      }
    },
    fail: function (res) {
      console.log(res,-1);
      typeof fail == "function" && fail(res);
    }
  })
}
/**
 * 首页和想要跳转到详情页面,添加足迹记录
 * 
 * @param  productid-商品id
 **/
function gotoDetialRecord(productid){
    var sessionId = wx.getStorageSync('sessionId');
    var memberId = wx.getStorageSync('id');
    var storeId = wx.getStorageSync('storeId');
    //添加足迹
    if (sessionId) {
      var sendData = {};
      //是否有会员id
      if (memberId) {
        sendData = {
          memberId: memberId, //会员id
          productId: productid,   //商品id
          type: 0,    //类型：0商品
          storeId: storeId
        }
      } else {
        sendData = {
          productId: productid,   //商品id
          type: 0,    //类型：0商品
          storeId: storeId
        }
      }
      var sign = md5(paramConcat(sendData));
      //console.log("添加足迹提交的数据：",sendData,sign);
      wx.request({
        url: constant.devUrl + '/miniapp/member/visit/addupdate.json', //接口地址
        data: sendData,
        header: {
          'wxa-sessionid': sessionId,
          'version': constant.version,
          'sign':sign
        },
        success: function (res) {
          //console.log("添加足迹返回的数据：",res);
          wx.navigateTo({
            url: '../component/detail/detail?productId=' + productid
          })
        }
      })
    } else {
      wx.navigateTo({
        url: '../component/detail/detail?productId=' + productid
      })
    }
}
/**
 * 返回顶部
 */
function gotoTop() {
  if (wx.pageScrollTo) {
    wx.pageScrollTo({
      scrollTop: 0
    })
  } else {
    wx.showModal({
      title: '提示',
      content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。'
    })
  }
}
/**
 * 页面滑动是否显示置顶按钮
 */
function onPageScroll(that){
  var scrollHeight;
  if (wx.createSelectorQuery()) {
    wx.createSelectorQuery().select('.body-contain').boundingClientRect(function (rect) {
      scrollHeight = rect.top
      if (scrollHeight < -150) {
        that.setData({
          goTopShowState: true
        })
      } else {
        that.setData({
          goTopShowState: false
        })
      }
    }).exec()
  }
}
/**
 * 定时获取未读消息条数
 */
function timingGetMsgNumber(that,storeId,memberId){
  var sendData = {
    storeId: storeId,
    memberId: memberId
  }
  var sign = md5(paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: constant.devUrl + '/miniapp/message/getMemberNoReadCount.json', //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      //console.log(res.data,res.data.data.memberNoReadMessageCount);
      if (res.data.successful){
        that.setData({
          msgNumber: res.data.data.memberNoReadMessageCount
        })
      }
    }
  })
}
/**
 * 设置会员未读消息(进入小程序就调用)
 */
function setReadMsgNumber(that, storeId, memberId) {
  var sendData = {
    storeId: storeId,
    memberId: memberId
  }
  var sign = md5(paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: constant.devUrl + '/miniapp/message/setMemberNoReadIsRead.json', //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      //console.log(res.data)
      if (res.data.successful) {
        that.setData({
          msgNumber: 0
        })
      }
    }
  })
}
/**
 * 获取商家门店信息-判断是否个人或者企业
 */
function getStoreWxaType(member_id, store_id,callback) {
  var sendData = {
    storeId: store_id,
    memberId: member_id
  }
  var sign = md5(paramConcat(sendData));
  //console.log("门店",sendData);
  wx.request({
    url: constant.devUrl + '/miniapp/shop/getShopInfo.json', //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign':sign
    },
    success: function (res) {
      console.log(res.data)
      if (res.data.successful) {
        var isWxaType = res.data.data.wxaType;
        wx.setStorageSync('wxaType', isWxaType);
        callback(isWxaType);
      }
    }
  })
}
/**
 * 获取电话咨询热线
 */
function getPhoneNumber(that, storeId, memberId) {
  var sendData = {
    storeId: storeId,
    memberId: memberId
  }
  var sign = md5(paramConcat(sendData));
  //console.log(sendData);
  wx.request({
    url: constant.devUrl + '/miniapp/homepage/hasHotOnline.json', //接口地址
    data: sendData,
    header: {
      'version': constant.version,
      'sign': sign
    },
    success: function (res) {
      console.log("电话热线:" ,res.data);
      if (res.data.successful) {
        that.setData({
          hotPhone: res.data.data
          // hasHotonline: res.data.data.hasHotonline,
          // hotOnline: res.data.data.hotOnline
        })
      }
    }
  })
}
/**
 * 拨打电话号码
 */
function makePhone(e){
  var phone_Number = e.currentTarget.dataset.phone;
  //console.log(phone_Number);
  if (wx.makePhoneCall) {
    wx.makePhoneCall({
      phoneNumber: phone_Number
    })
  } else {
    // 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
    wx.showModal({
      title: '提示',
      content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。'
    })
  }
}
/**
 * 对象按照属性名的字母顺序进行排列
 */
function objKeySort(obj) {//排序的函数
  var newkey = Object.keys(obj).sort();
  //先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
  var newObj = {};//创建一个新的对象，用于存放排好序的键值对
  for (var i = 0; i < newkey.length; i++) {//遍历newkey数组
    newObj[newkey[i]] = obj[newkey[i]];//向新创建的对象中按照排好的顺序依次增加键值对
  }
  return newObj;//返回排好序的新对象
}
/**
 * 接口入参拼接成字符串通用函数
 */
function paramConcat(obj) {
  var newObj = objKeySort(obj);
  //遍历newkey对象
  var paramStr = "";
  var secret = "yjj2018";
  for (var Key in newObj) {
    paramStr += Key + newObj[Key];
  }
  return secret + paramStr + secret;
}
/**
 * 倒计时时间格式化输出通用函数
 * @param  msec - 豪秒数
 **/
function msecFormat(msec) {
  var totalSeconds = parseInt(msec) / 1000;       // 总毫秒数
  
  var h = Math.floor(totalSeconds / 60 / 60),        // 小时
      m = Math.floor(totalSeconds / 60 % 60),       // 分钟
      s = Math.floor(totalSeconds % 60);            // 秒
  // if (h <= 9) h = '0' + h;
  // if (m <= 9) m = '0' + m;
  // if (s <= 9) s = '0' + s; 
  var timeObj = {
    hours: h,
    minute: m,
    second: s
  }
  //console.log(timeObj);
  return timeObj;
}
/**
 * 时间格式化通用函数
 * @param  msec - 豪秒数
 **/
//  
function secondFormat(msec) {
  var second = Math.floor(msec / 1000), // 总秒数
      hr = Math.floor(second / 3600),          
      min = Math.floor(second / 60 % 60),       
      sec = Math.floor(second % 60);           
  return hr + "小时" + min + "分钟" + sec + "秒";
}
/**
 * 判断是否是对象
 */
function isObject(obj) {
  return (typeof obj == 'object') && obj.constructor == Object;
} 
//输出接口
module.exports = {
  formatTime: formatTime,
  removeByValue: removeByValue,
  uploadFileFun: uploadFileFun,  //上传文件
  gotoDetialRecord: gotoDetialRecord, //跳转详情添加足迹记录
  gotoTop: gotoTop,   //置顶
  onPageScroll: onPageScroll,  //是否显示置顶函数
  timingGetMsgNumber: timingGetMsgNumber,  //定时获取未读消息条数
  setReadMsgNumber: setReadMsgNumber,  //设置已读消息
  getStoreWxaType: getStoreWxaType,   // 获取商家信息
  makePhone: makePhone,           //拨打电话
  getPhoneNumber: getPhoneNumber,           //获取电话
  MD5:md5,          //MD5加密方法
  paramConcat: paramConcat,   //参数拼接方法
  toDecimal: toDecimal2,     //小数点处理
  msecFormat: msecFormat,
  isObject: isObject,
  secondFormat: secondFormat  
}