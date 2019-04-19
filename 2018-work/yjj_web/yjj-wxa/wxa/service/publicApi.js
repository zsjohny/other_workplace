/**
 * 此模块管理所有新的小程序接口(不分类的接口)
 * @date  2018-11-29
 */
var requestUtil = require('./request.js'),
    constant = require('../constant'),
    httpRequest = requestUtil.httpRequest,
    newRequest = requestUtil.newRequest,
    apiDomain = constant.apiUrl;

//不同模块的api路径-测试、正式线
//var orderPath = apiDomain + '/wxOrder';

//不同模块的api路径-开发线
// var orderPath = 'http://192.168.1.188:8072';

/**
 * QA热门问题
 */
export function getHotQuestion(param) {
  return httpRequest(apiDomain +'/user/qa/shop/hot/question', param);
}
/**
 * QA问题分类列表
 */
export function getQaType(param) {
  return httpRequest(apiDomain + '/user/qa/shop/type/list', param);
}
/**
 * QA问题分类
 */
export function getQuestionList(param) {
  return httpRequest(apiDomain + '/user/qa/shop/type/question', param);
}
/**
 * QA问题搜索
 */
export function questionSearch(param) {
  return httpRequest(apiDomain + '/user/qa/shop/question/search', param);
}
/**
 * QA问题详情
 */
export function questionDetail(param) {
  return httpRequest(apiDomain + '/user/qa/shop/question/detail', param);
}
/**
 * QA问题是否帮助
 */
export function isUserful(param) {
  return httpRequest(apiDomain + '/user/qa/shop/question/useful', param);
}
