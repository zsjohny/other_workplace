import {getRequest} from '../config/network'

const getPersonalInfo = (reqParams, callback) => {
  getRequest('/public/proxyCustomer/getCustomerInfo', reqParams).then(res => {
    callback(res)
  })
}
const getAddressList = (reqParams, callback) => {
  getRequest('/public/proxy/pay/getAddressList', reqParams).then(res => {
    callback(res)
  })
}
const selectAddress = (reqParams, callback) => {
  getRequest('/public/proxy/pay/selectAddress', reqParams).then(res => {
    callback(res)
  })
}
const saveAddress = (reqParams, callback) => {
  getRequest('/public/proxy/pay/saveAddress', reqParams).then(res => {
    callback(res)
  })
}
const editAddressDefault = (reqParams, callback) => {
  getRequest('/public/proxy/pay/editAddressDefault', reqParams).then(res => {
    callback(res)
  })
}
export {
  getPersonalInfo, // 个人资料
  getAddressList, // 地址列表
  selectAddress, // 详情
  saveAddress, // 保存地址
  editAddressDefault // 设置默认地址
}
