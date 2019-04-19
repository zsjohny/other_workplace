/**
 * 此模块管理订单和售后的小程序接口
 * @date  2018-11-24
 */
var requestUtil = require('./request.js'),
    constant = require('../constant'),
    httpRequest = requestUtil.httpRequest,
    myRequest = requestUtil.myRequest,
    newRequest = requestUtil.newRequest,
    apiDomain = constant.apiUrl;

//不同模块的api路径-测试、正式线
//var orderPath = apiDomain + '/wxOrder';        //订单和售后相关模块

//不同模块的api路径-开发线
// var orderPath = 'http://192.168.1.188:8072';

var orderPath = 'http://192.168.1.188:8072',         //新的售后模块
    oldOrderPath = 'http://192.168.1.188:8085',      //老的订单列表接口
    uploadPath = 'http://192.168.1.188:8088';        //上传接口
/**
 * 退款售后列表
 */
export function getRefundList(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/refundList', param);
}
/**
 * 退款售后详情
 */
export function getRefundDetail(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/refundItem', param);
}
/**
 * 退款进度
 */
export function getRefundProgress(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/refundStatus', param);
}
/**
 * 删除退款售后列表的订单
 */
export function deleteRefundOrder(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/deleteOrder', param);
}
/**
 * 多个商品售后列表的订单
 */
export function getMultipleOrder(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/refundButton', param);
}
/**
 * 获取申请售后的商品信息
 */
export function getRefundProductInfo(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/redundApply', param);
}
/**
 * 申请售后提交
 */
export function refundApply(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/submitApply', param);
}
/**
 * 申请售后提交上传图片接口
 */
export var uploadFileApi = orderPath + "/wxOrder/shopOrder/uploadImg";
/**
 * 订单列表
 */
export function getOrderList(param) {
  return myRequest('/mobile/memberOrder/getMemberOrderList.json', param);
}
/**
 * 订单详情
 */
export function orderDetail(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/orderItem', param);
}
/**
 * 订单确认收货
 */
export function orderComfirmReceipt(param) {
  return myRequest('/mobile/memberOrder/confirmReceipt.json', param);
}
/**
 * 订单定时获取提货状态
 */
export function checkOrderStatus(param) {
  return myRequest('/mobile/memberOrder/checkMemberOrderStatus.json', param);
}
/**
 * 订单删除
 */
export function deleteOrder(param) {
  return httpRequest(orderPath + '/wxOrder/shopOrder/deleteOrder', param);
}
/**
 * 订单取消
 */
export function cancelOrder(param) {
  return myRequest('/mobile/memberOrder/storeOrderByOrderId.json', param);
}
/**
 * 自动取消订单
 */
export function cancelOrderAuto(param) {
  return myRequest('/mobile/memberOrder/cancelOrderAuto.json', param);
}
/**
 * 获取订单提货二维码的数据
 */
export function getOrderCodeData(param) {
  return myRequest('/mobile/memberOrder/getDeliveryOrderText.json', param);
}
