/**
 * 此模块管理分销相关的小程序接口
 * @date  2018-10-29
 */
var requestUtil = require('./request.js'),
    constant = require('../constant'),
    httpRequest = requestUtil.httpRequest,
    newRequest = requestUtil.newRequest,
    apiDomain = constant.apiUrl;

//不同模块的api路径-test线
var distributionPath = apiDomain + '/distribution',        //分销相关模块
    activityPath = apiDomain + '/activity';                //活动相关模块

//不同模块的api路径-开发线
//  var distributionPath = 'http://192.168.1.187:8074',
//      activityPath = 'http://192.168.1.187:8075';

/**
 * 个人中心-获取我的收益、粉丝、团队、邀请人数据
 */
export function getMyInformation(param) {
  return httpRequest(distributionPath + '/distribution/my/information', param);
}
/**
 * 账户金额-总金额-订单佣金-管理佣金
 */
export function getAccountBill(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/count/bill', param);
}
/**
 * 账户金币-金币总数数据
 */
export function getGoldBill(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/count/gold/bill', param);
}
/**
 * 账户金额、账户金币-收支明细列表
 */
export function getBillDetails(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/bill/details', param);
}
/**
 * 收支详情
 */
export function getAccountDetails(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/order/account/details', param);
}
/**
 * 提现
 */
export function toWithdrawCash(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/cashOut', param);
}
/**
 * 提现-提现成功
 */
export function getWithdrawSuccessData(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/cashOut', param);
}
/**
 * 我的粉丝-粉丝总数
 */
export function getMyFollowerData(param) {
  return httpRequest(distributionPath + '/distribution/count/follower', param);
}
/**
 * 我的粉丝-一级粉丝明细
 */
export function getMyFollowerDetails(param) {
  return httpRequest(distributionPath + '/distribution/follower/details', param);
}
/**
 * 权益页面-晋升条件
 */
export function getPromoteCondition(param) {
  return httpRequest(distributionPath + '/distribution/promote/condition', param);
}
/**
 * 权益页面-权益申请
 */
export function applyRights(param) {
  return httpRequest(distributionPath + '/distribution/distribution/proposer', param);
}
/**
 * 签到-初始状态
 */
export function signInit(param) {
  return httpRequest(activityPath + '/shop/sign/show/days', param);
}
/**
 * 签到-点击签到
 */
export function sign(param) {
  return httpRequest(activityPath + '/shop/sign/do/sign', param);
}
/**
 * 签到-点击获取签到礼物
 */
export function signGift(param) {
  return httpRequest(activityPath + '/shop/sign/periodical/prize', param);
}
/**
 * 团体订单列表
 */
export function teamOrderList(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/team/order', param);
}
/**
 * 团体订单-订单详情
 */
export function teamOrderDetail(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/team/order/detail', param);
}
/**
 * 团体订单-信息
 */
export function teamOrderCount(param) {
  return httpRequest(distributionPath + '/distribution/cashOutIn/team/order/count', param);
}
/**
 * 确认分享关系
 */
export function shareFriend(param) {
  return httpRequest(activityPath + '/activity/share/shareFriend', param);
}
