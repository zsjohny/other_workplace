// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VConsole from 'vconsole'
new VConsole()

import { Toast, Area, Popup, Picker, Tabbar, TabbarItem,
  PullRefresh, List, Swipe, SwipeItem, Cell, CellGroup,
  Field, Switch, Tab, Tabs, Loading, Dialog, DatetimePicker } from 'vant'
Vue.use(Toast)
Vue.use(Area)
Vue.use(Popup)
Vue.use(Picker)
Vue.use(Tabbar).use(TabbarItem)
Vue.use(PullRefresh)
Vue.use(List)
Vue.use(Swipe).use(SwipeItem)
Vue.use(Cell).use(CellGroup)
Vue.use(Field)
Vue.use(Switch)
Vue.use(Tab).use(Tabs)
Vue.use(Loading)
Vue.use(Dialog)
Vue.use(DatetimePicker)
Vue.prototype.$toast = Toast
Vue.prototype.$dialog = Dialog

Vue.config.productionTip = false
Vue.prototype.$http = axios

import {formatDate , getUrlPara } from '@/config/utils'
Vue.prototype.$formatDate = formatDate
Vue.prototype.$getUrlPara = getUrlPara
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
