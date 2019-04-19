// import request from '@/utils/request'

//获取订单列表数据
export const getOrderList = (params) => {
  return request({
    url: '/',
    method: 'get',
    data: params
  })
}

//修改物流信息
export const modifySubmit = (params) => {
  return request({
    url: '/',
    method: 'get',
    data: params
  })
}
