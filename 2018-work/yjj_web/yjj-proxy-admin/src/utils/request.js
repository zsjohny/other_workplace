import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'
import requestUrl from "../../../yjj-proxy/src/config/requestUrl";

// 创建axios实例
const service = axios.create({
  // baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 10000 // 请求超时时间
})
service.defaults.baseURL = process.env.NODE_ENV === 'production' ? 'https://local.yujiejie.com/operate' : 'http://192.168.10.105:8088/'
// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      // config.headers['X-Token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
  },
  error => {
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    // if (res.code !== 20000) {
    //   Message({
    //     message: res.message,
    //     type: 'error',
    //     duration: 5 * 1000
    //   })
    //   return Promise.reject('error')
    // } else {
    //   return response.data
    // }
    return response
  },
  error => {
    console.log('err' + error)
    return Promise.reject(error)
  }
)

export const getRequest = (url, params) => {
  return axios({
    method: 'get',
    url: url,
    params: params
  })
}

export const POST = (url, params) => {
  return service.post(url, params).then(res => res.data)
}

export const GET = (url, params) => {
  return service.get(url, { params: params }).then(res => res.data)
}
