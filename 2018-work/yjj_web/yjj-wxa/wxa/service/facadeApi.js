/**
 * 此模块管理老项目的小程序接口
 *
 */
var requestUtil = require('./request.js'),
    constant = require('../constant'),
    myRequest = requestUtil.myRequest,
    httpRequest = requestUtil.httpRequest,
    apiHost = constant.devUrl,
    apiDomain = constant.apiUrl;
/**
 * 首页获取最新订单消息
 */
export function getOrderMessage(param) {
  return myRequest("/mobile/memberOrder/lastestOrderMessage.json", param);
}
/**
 * 首页获取轮播图、文章列表、公告、自定义导航
 */
export function home(param) {
  return myRequest("/miniapp/homepage/home.json", param);
}
/**
 * 首页获取优惠券提醒
 */
export function waitShopCoupon(param) {
  return myRequest('/miniapp/homepage/waitGetShopCouponTemplateInfo.json', param);
}
/**
 * 首页获取智能模块排序列表
 */
export function getSmartModule(param) {
  return myRequest('/smartModule/findWithInit.json', param);
}
/**
 * 商品详情获取分享图片接口
 */
export function getShareImage(param) {
  return myRequest("/shopProduct/detail/share/getImage.json", param);
}
/**
 * 加入购物车
 */
export function joinShopCar(param) {
  return myRequest('/miniapp/car/shopCarAddGoods.json', param);
}
/**
 * 我的颜值分页面-获取颜值分分享商品列表
 */
export var coinProductApi = apiHost + "/wxa/shareProductList.json";
export function getCoinProduct(param) {
  return myRequest('/wxa/shareProductList.json', param);
}
/**
 * 颜值分明细页面-获取颜值分明细列表数据
 */
export var coinListApi = apiHost + "/wxa/wxaAccountDetail.json";
export function getCoinList(param) {
  return myRequest("/wxa/wxaAccountDetail.json", param);
}
/**
 * 我的邀请-被邀请者列表
 */
export var shareFriendApi = apiHost + "/wxa/shareFriendList.json";
export function getShareFriendList(param) {
  return myRequest('/wxa/shareFriendList.json', param);
}

/**
 * 查询颜值分账户数据
 */
export function getCoinUserData(param) {
  return myRequest("/wxa/wxaAccount.do", param);
}
/**
 * 最近的一个分享数据
 */
export function getLastShareFriend(param) {
  return myRequest("/wxa/lastShareFriend.json", param);
}
/**
 * 获取最小提现颜值分数量
 */
export function getMinCashOutCoins(param) {
  return myRequest("/wxa/minCashOutCoins.json", param);
}
/**
 * 获取颜值分对应人民币的比例
 */
export function getCoins2rmbRadio(param) {
  return myRequest("/wxa/coins2rmbRadio.json", param);
}
/**
 * 获取剩余提现金额
 */
export function getLeftCashOut(param) {
  return myRequest("/wxa/leftCashOut.json", param);
}
/**
 * 查询某个用户的待入账颜值分
 */
export function getWaitInCoins(param) {
  return myRequest("/wxa/waiInCoins.json", param);
}
/**
 * 颜值分提现接口
 */
export function confirmWithdraw(param) {
  return myRequest("/wxa/cashOut.json", param);
}
/**
 * 团购所有活动列表
 */
export var teamBuyApi = apiHost + "/miniapp/homepage/teamActivityList.json";
export function getTeamBuyList(param) {
  return myRequest('/miniapp/homepage/teamActivityList.json', param);
}
/**
 * 秒杀所有活动列表
 */
export var secondBuyApi = apiHost + "/miniapp/homepage/secondActivityList.json";
export function getSecondBuyList(param) {
  return myRequest('/miniapp/homepage/secondActivityList.json', param);
}
/**
 * 我的团购-即将成团、团购成功
 */
export var teamBuyNotStartApi = apiHost + "/mobile/memberOrder/teamBuyActivityUnderwayList.json"; //即将成团
export var teamBuySuccessApi = apiHost + "/mobile/memberOrder/teamBuyActivityOKList.json";  //团购成功
//即将成团
export function getTeamBuyActivityOKList(param) {
  return myRequest('/mobile/memberOrder/teamBuyActivityUnderwayList.json', param);
}
/**
 * 订单确认收货
 */
export function confirmReceipt(param) {
  return myRequest('/mobile/memberOrder/confirmReceipt.json', param);
}
/**
 * 调用微信支付
 */
export function wxaPay(param) {
  return myRequest('/miniapp/pay/wxaPay.json', param);
}
/**
 * 领券中心-获取优惠券列表
 */
export function getCouponCenterData(param) {
  return myRequest('/miniapp/coupon/getWaitGetShopCouponTemplateList.json', param);
}
/**
 * 领券中心-领取所有优惠券
 */
export function getAllShopCoupon(param) {
  return myRequest('/miniapp/coupon/getAllShopCouponTemplate.json', param);
}
/**
 * 领券中心-领取单张优惠券
 */
export function getSingleShopCoupon(param) {
  return myRequest('/miniapp/coupon/getShopCouponTemplate.json', param);
}
/**
 * 我的优惠券-领取优惠券列表
 */
export var myCouponListApi = apiHost + "/miniapp/coupon/list.json"; //优惠券列表
export function getMyCouponList(param) {
  return myRequest('/miniapp/coupon/list.json', param);
}
/**
 * 我的优惠券-领取优惠券数量
 */
export function getMyCouponNumber(param) {
  return myRequest('/miniapp/coupon/listCountNumber.json', param);
}
/**
 * 我的优惠券-领取可用优惠券商品列表
 */
export var couponProdcutApi = apiHost + "/miniapp/coupon/getProductByCouponId.json";
export function getCouponProdcutList(param) {
  return myRequest('/miniapp/coupon/getProductByCouponId.json', param);
}
/**
 * 获取订单提货二维码
 */
export var getcodeImgApi = apiHost + "/mobile/memberOrder/qrcode/src.do";
/**
 * 获取订单的状态-判断订单状态
 */
export var getOrderStateApi= apiHost + "/mobile/memberOrder/checkMemberOrderStatus.json";
export function getOrderState(param) {
  return myRequest('/mobile/memberOrder/checkMemberOrderStatus.json', param);
}
/**
 * 获取门店商家信息
 */
export function getShopInfo(param) {
  return myRequest('/miniapp/shop/getShopInfo.json', param);
}


