import Vue from 'vue'
import Router from 'vue-router'
import wxRouters from './wxRouters'

Vue.use(Router)

const routes = [
  ...wxRouters
]

const router = new Router({
  // base: '/proxy/',
  // mode: 'history',
  routes,
  // strict: process.env.NODE_ENV !== 'production'
})

const whiteList = ['/goodsList', '/goodsDetail', '/login', '/register']
router.beforeEach((to, from, next) => {
  console.log(to, from, 'to from next')
  if (to.meta.title) { // 路由发生变化修改页面title
    document.title = to.meta.title
  }
  if(window.localStorage.getItem('data')) {
    var info = window.localStorage.getItem('data')
  }
  if(info !== null) {
    next()
  }else{ // 验签
    if(whiteList.indexOf(to.path) > -1){
      next()
    }else{
      next('./login')
    }
  }
})

export default router
