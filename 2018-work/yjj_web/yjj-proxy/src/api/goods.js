import {postRequest} from '../config/network'
import {getRequest} from '../config/network'

const getGoodsDetail = (reqParams, callback) => {
  getRequest('/public/proxy/goods/selectById', reqParams).then(res => {
    callback(res)
  })
}
const getGoodsList = (reqParams, callback) => {
  getRequest('/public/proxy/goods/goodsList', reqParams).then(res => {
    callback(res)
  })
}
const getBeforeOrderDetail = (reqParams, callback) => {
  getRequest('/public/proxy/pay/getBeforeOrderDetail', reqParams).then(res => {
    callback(res)
  })
}
const goPay = (reqParams, callback) => {
  getRequest('/public/proxy/pay/payOrder', reqParams).then(res => {
    callback(res)
  })
}
const cancelOrder = (reqParams, callback) => {
  getRequest('/public/proxy/pay/cancelOrder', reqParams).then(res => {
    callback(res)
  })
}

export {
  getGoodsDetail, // 详情
  getGoodsList, // 商品列表
  getBeforeOrderDetail, // 支付订单
  goPay, // 支付
  cancelOrder // 取消支付
}
