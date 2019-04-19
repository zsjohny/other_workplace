import {getRequest} from '../config/network'

const getOrderList = (reqParams, callback) => {
  getRequest('/public/proxy/pay/getOrderDetailByUserId', reqParams).then(res => {
    callback(res)
  })
}
const getOrderDetail = (reqParams, callback) => {
  getRequest('/public/proxy/pay/getAfterOrderDetail', reqParams).then(res => {
    callback(res)
  })
}

export {
  getOrderList, // 订单列表
  getOrderDetail
}
