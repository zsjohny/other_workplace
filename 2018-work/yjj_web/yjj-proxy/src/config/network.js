import axios from 'axios'
import config from '../../config'
import requestUrl from './requestUrl'

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
// axios.defaults.baseURL = process.env.NODE_ENV === 'production' ? config.build.baseUrl : config.dev.baseUrl

axios.interceptors.request.use(
  config => {
    var urlArr = [
      '/yOpen/publicAccountUser/sendAuthCode',
    ]
    var data = JSON.parse(window.localStorage.getItem('data'))
    console.log(data, 'token, uid')
    if (!(urlArr.indexOf(config.url) > -1)) {
      if (data !== null) {
        // config.params['openToken'] = data.openToken
        // config.params['openId'] = data.openId
        config.params = Object.assign(config.params, data)
      } else {

      }
    }
    console.log('请求', config)
    // 请求成功200
    return config
  },
  error => {
    return Promise.resolve(error)
  }
)

axios.interceptors.response.use(
  response => {
    console.log('返回', response)
    // if (response.headers.token) {
    //   var data = {
    //     uid: response.headers.uid,
    //     token: response.headers.token
    //   }
    //   window.localStorage.setItem('data', JSON.stringify(data))
    // }
    return response.data
  },
  error => {
    return Promise.resolve(error)
  }
)
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: url,
    data: params,
    // transformRequest: [ (data) => {
    //   let ret = ''
    //   for (let it in data) {
    //     ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
    //   }
    //   return ret
    // }],
    headers: {
      // 'X-Requested-With': 'XMLHttpRequest'
      // 'Content-Type': 'application/x-www-form-urlencoded'
      // 'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export const getRequest = (url, params) => {
  return axios({
    method: 'get',
    url: requestUrl(url),
    params: params
  })
}
