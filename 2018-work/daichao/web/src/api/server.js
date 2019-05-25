import axios from 'axios'
import { Indicator , Toast  } from 'mint-ui'

axios.defaults.baseURL = 'http://47.96.110.1:8088/credit-diversion/api'

/**
 * axios请求拦截器
 * 
 */
axios.interceptors.request.use(config => {
  Indicator.open({spinnerType: 'fading-circle'});   // 请求开始 出现旋转的loading
  return config
}, error => {
  return Promise.reject(error)
})


/**
 * axios响应拦截器
 * 
 */

axios.interceptors.response.use(response => {
  Indicator.close(); // 请求结束 loading结束
  return response
},err => {
  Indicator.close();
  if(err && err.response){
    Toast({
      message: err.response.data.msg,
      position: 'bottom',
      duration: 2000
    })
  }
  return Promise.reject(err)
})

/**
 * 封装get方法
 * @param url
 * @param data
 */

export function get(url,params={}){
  return axios({
    method: 'get',
    url,
    params,
    responseType: 'json'
  })
}


/**
 * 封装post请求
 * @param url
 * @param params
 */

export function post(url, params={}){
  return axios({
    method: 'post',
    url,
    params,
    responseType: 'json'
  })
}
