import {getRequest} from '../config/network'

const getProfit = (reqParams, callback) => {
  getRequest('/public/proxy/pay/getRewardOrderList', reqParams).then(res => {
    callback(res)
  })
}
const getProfitMoney = (reqParams, callback) => {
  getRequest('/public/proxy/pay/rewardInfoPage', reqParams).then(res => {
    callback(res)
  })
}

export {
  getProfit, // 我的收益
  getProfitMoney // 收入
}
