import {postRequest} from '../config/network'
import {getRequest} from '../config/network'

const posterQrcode = (reqParams, callback) => {
  getRequest('/public/proxyCustomer/posterQrcode', reqParams).then(res => {
    callback(res)
  })
}

export {
  posterQrcode // 生成海报
}
