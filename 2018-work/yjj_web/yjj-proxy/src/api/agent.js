import {postRequest} from '../config/network'
import {getRequest} from '../config/network'

const unbindCounty = (reqParams, callback) => {
  getRequest('/public/proxyCustomer/unbind', reqParams).then(res => {
    callback(res)
  })
}
const myProxyCustomer = (reqParams, callback) => {
  getRequest('/public/proxyCustomer/myProxyCustomer', reqParams).then(res => {
    callback(res)
  })
}

export {
  unbindCounty, // 解绑县代理和客户
  myProxyCustomer // 家族管理
}
