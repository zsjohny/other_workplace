import axios from '@/libs/api.request'

//获取公海池列表数据
export const getCustomerPoolData = (params) => {
  return axios.request({
    url: '/pool/find/all',
	params: params,
    method: 'get'
  })
}

//客户管理列表数据
export const getCustomerData = (params) => {
  return axios.request({
    url: '/pool/find/all',
	params: params,
    method: 'get'
  })
}

//创建公海和常用客户
export const createCustomer = (params) => {
  return axios.request({
    url: '/pool/customer/add',
	params: params,
    method: 'get'
  })
}

// excel添加客户
export const addImportCustomer = (params) => {
  return axios.request({
    url: '/pool/excel/add',
	 params: {
      params
    },
    method: 'get'
  })
}

//处理公海池用户 领取-退回-分配
export const manageCustomer = (params) => {
  return axios.request({
    url: '/pool/customer/manage',
	params: params,
    method: 'get'
  })
}

