/**
 * 统一的请求方法
 */
var Promise = require('../utils/es6-promise.min.js').Promise,
    constant = require('../constant'),
    util = require('../utils/util.js'),
    dialog = require('../utils/dialog.js'),
    page = 2,
    page_size = 10;
/**
 *  url 地址 不用带域名
 *  param json 格式的参数
 *  {
 *    done: 可选 对应wx.request中的 complete 不管成功失败都会调用,
 *    data: 可选 发送到后台的参数
 *    loading: 可选 是否需要loading，默认不显示: true-显示，false-不显示
 *    header:额外的头信息
 *    defaultPage: 是否有分页 true-有
 *    successful: 是否定义返回数据successful判断 默认true-是
 *  }
 *
 */
export function myRequest(url,param){
  // 如果只有uri则拼接地址
  if (url.indexOf("https://") == -1) {
    url = constant.devUrl + url;
  }
  console.log("入参", param);
  param = param || {};
  param.header = param.header || {};
  param.data = param.data || {};
  if (param.successful == false || param.successful === undefined) {
    param.successful = false
  } else {
    param.successful = true
  }
  var done = param.done || null;
  var isSHowLoading = param.loading;
  //是否显示loading
  if (isSHowLoading) {
    dialog.loading();
  }
  if (done) {
    delete param.done;
  }
  // 每次请求都需要添加的数据
  var store_id = wx.getStorageSync('storeId');
  var member_id = wx.getStorageSync('id');
  param.data.storeId = store_id;
  if (member_id) {
    param.data.memberId = member_id;
  }
  // 是否有默认的分页
  if (param.defaultPage) {
    param.data.current = page;       //当前是第几页
    param.data.size = page_size;    //每页显示条数
    delete param.defaultPage;
  }
  //console.log("请求入参",param);
  var sign = util.MD5(util.paramConcat(param.data));
  return new Promise(function (resolve, reject) {
    wx.request({
      url: url, //接口地址
      data: param.data,
      method: "POST",
      header: {
        'version': constant.version,
        'sign': sign,
        'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
        ...param.header
      },
      success: function (res) {
        if (param.successful){
          if (res.data.successful) {
            var data = res.data;
            //console.log("请求成功", data);
            done && done(data);
            resolve(data);
          } else {
            dialog.toastError(res.data.error)
          }
        }else{
          var data = res.data;
          done && done(data);
          resolve(data);
        }
      },
      fail: function (res) {
        console.log("请求失败", res);
        done && done(res);
        reject(res);
      },
      complete: function () {
        if (isSHowLoading) {
          setTimeout(function () {
            dialog.hide();
          }, 500)
        }
      }
    });
  });
}
/**
 *  url 地址 -因为新接口的域名和结构不同，所以重复写一个请求方法
 *  param json 格式的参数
 *  {
 *    done: 可选 对应wx.request中的 complete 不管成功失败都会调用,
 *    data: 可选 发送到后台的参数
 *    loading: 可选 是否需要loading，默认不显示: true-显示，false-不显示
 *    header:额外的头信息
 *    isSign: 是否参数需要延签加密-以为部分新接口的需要验签不能放在header，默认不需要-false
 *    successful: 是否定义返回数据successful判断 默认true-是
 *  }
 */
export function httpRequest(url, param) {
  console.log("入参", param);
  param = param || {};
  param.header = param.header || {};
  param.data = param.data || {};
  if (param.successful == false || param.successful === undefined) {
    param.successful = false
  } else {
    param.successful = true
  }
  var done = param.done || null;
  var isSHowLoading = param.loading;
  //是否显示loading
  if (isSHowLoading) {
    dialog.loading();
  }
  if (done) {
    delete param.done;
  }
  // 每次请求都需要添加的数据
  var member_id = wx.getStorageSync('id');
  if (member_id) {
    param.data.userId = member_id;
  }
  //console.log("请求入参",param);
  var sign = util.MD5(util.paramConcat(param.data));
  //是否参数需要延签
  if (param.isSign) {
    param.data.wxSign = sign;
  }
  return new Promise(function (resolve, reject) {
    wx.request({
      url: url, //接口地址
      data: param.data,
      method: "POST",
      header: {
        'version': constant.version,
        'sign': sign,
        'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
        ...param.header
      },
      success: function (res) {
        if (param.successful) {
          if (res.data.code == "200") {
            var data = res.data;
            //console.log("请求成功", data);
            done && done(data);
            resolve(data);
          } else {
            dialog.toastError(res.data.error)
          }
        }else{
          var data = res.data;
          done && done(data);
          resolve(data);
        }
      },
      fail: function (res) {
        console.log("请求失败", res);
        done && done(res);
        reject(res);
      },
      complete: function () {
        if (isSHowLoading) {
          setTimeout(function () {
            dialog.hide();
          }, 500)
        }
      }
    });
  });
}
