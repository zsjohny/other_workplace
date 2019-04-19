import {getRequest} from '../utils/request'
import { GET } from "../utils/request"

const auditList = (reqParams, callback) => {
  GET('/operate/proxy/customer/auditList', reqParams).then(res => {
    callback(res)
  })
}

const audit = (reqParams, callback) => {
  GET('/operate/proxy/customer/audit', reqParams).then(res => {
    callback(res)
  })
}
const deleteAgent = (reqParams, callback) => {
  GET('/operate/proxy/customer/delete', reqParams).then(res => {
    callback(res)
  })
}
const unbind = (reqParams, callback) => {
  GET('/operate/proxy/customer/unbind', reqParams).then(res => {
    callback(res)
  })
}
const doOrderReward = (reqParams, callback) => {
  GET('/operate/proxy/order/doOrderReward', reqParams).then(res => {
    callback(res)
  })
}
const stopCustomer = (reqParams, callback) => {
  GET('/operate/proxy/customer/stopCustomer', reqParams).then(res => {
    callback(res)
  })
}
const rewardAll = (reqParams, callback) => {
  GET('/operate/proxy/order/doOrderRewardByUser', reqParams).then(res => {
    callback(res)
  })
}
export {
  auditList, // 审核列表
  audit, // 审核
  deleteAgent, // 删除
  unbind, // 解绑
  doOrderReward, // 发放收益
  stopCustomer, // 启用禁用代理
  rewardAll // 发放总收益
}
