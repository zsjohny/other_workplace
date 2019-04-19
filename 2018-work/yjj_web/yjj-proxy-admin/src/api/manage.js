import {getRequest} from '../utils/request'
import { GET } from "../utils/request"

const customerList = (reqParams, callback) => {
  GET('/operate/proxy/customer/customerList', reqParams).then(res => {
    callback(res)
  })
}

const CustomerDetail = (reqParams, callback) => {
  GET('/operate/proxy/customer/myProxyCustomer', reqParams).then(res => {
    callback(res)
  })
}
const order = (reqParams, callback) => {
  GET('/operate/proxy/order/getOrderDetailByUserId', reqParams).then(res => {
    callback(res)
  })
}
const rewardInfo = (reqParams, callback) => {
  GET('/operate/proxy/order/rewardInfoPage', reqParams).then(res => {
    callback(res)
  })
}
const personalReward = (reqParams, callback) => {
  GET('/operate/proxy/order/getRewardOrderList', reqParams).then(res => {
    callback(res)
  })
}
const orderDetail = (reqParams, callback) => {
  GET('/operate/proxy/order/getAfterOrderDetail', reqParams).then(res => {
    callback(res)
  })
}
const orderList = (reqParams, callback) => {
  GET('/operate/proxy/order/orderList', reqParams).then(res => {
    callback(res)
  })
}
const saveAddress = (reqParams, callback) => {
  GET('/operate/proxy/order/saveAddress', reqParams).then(res => {
    callback(res)
  })
}
const rewardList = (reqParams, callback) => {
  GET('/operate/proxy/order/getRewardOrderList', reqParams).then(res => {
    callback(res)
  })
}

export {
  customerList, // 代理商列表
  CustomerDetail, // 代理详情
  order, // 代理商订单
  rewardInfo,
  personalReward, // 个人收入
  orderDetail, // 订单详情
  orderList, // 订单列表
  saveAddress, // 修改物流信息
  rewardList, // 收益发放金额
}
