import {getRequest} from '../utils/request'
import { GET } from "../utils/request"

const listUser = (reqParams, callback) => {
  GET('/operate/publicAccountUser/listUser', reqParams).then(res => {
    callback(res)
  })
}

export {
  listUser, // 用户
}
