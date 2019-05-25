import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'Home',
      components: {
        content: resolve => require(['@/components/home'],resolve),
        footer: resolve => require(['@/components/footer'],resolve)
      }
    },
    {
      path: '/details/:id',
      name: 'details',
      components: {
        content: resolve => require(['@/components/details'],resolve),
        footer: resolve => require(['@/components/footer'],resolve)
      } 
    },
    {
      path: '/login',
      name: 'login',
      components: {
        content: resolve => require(['@/components/login'],resolve)
      } 
    }
  ]
})
