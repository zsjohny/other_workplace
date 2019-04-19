import {postRequest} from '../config/network'
import {getRequest} from '../config/network'

const sendAuthCode = (reqParams, callback) => {
  getRequest('/public/yOpen/publicAccountUser/sendAuthCode', reqParams).then(res => {
    callback(res)
  })
}
const login = (reqParams, callback) => {
  getRequest('/public/yOpen/publicAccountUser/authentication', reqParams).then(res => {
    callback(res)
  })
}
const register = (reqParams, callback) => {
  getRequest('/public/proxyCustomer/register', reqParams).then(res => {
    callback(res)
  })
}
const loginOut = (reqParams, callback) => {
  getRequest('/public/yOpen/publicAccountUser/logout', reqParams).then(res => {
    callback(res)
  })
}
const isLogin = (reqParams, callback) => {
  getRequest('/public/yOpen/publicAccountUser/isLogin', reqParams).then(res => {
    callback(res)
  })
}
const existProxyApply = (reqParams, callback) => {
  getRequest('/public/proxyCustomer/recentlyUnCheckOrFailedAudit', reqParams).then(res => {
    callback(res)
  })
}
const loginByWeiXin = (reqParams, callback) => {
  getRequest('/public/yOpen/publicAccountUser/loginByWeiXin', reqParams).then(res => {
    callback(res)
  })
}


export {
  sendAuthCode, // 获取验证码
  login, // 登录
  register, // 注册
  loginOut, // 登出
  isLogin, // 是否登录
  existProxyApply, // 是否存在代理
  loginByWeiXin // 微信登录
}
