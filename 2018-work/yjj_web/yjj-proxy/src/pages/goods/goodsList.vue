<template>
    <div class="goodContent">
      <div>
        <img src="~@/assets/image/goods/banner.png" alt="" class="banner">
        <div class="titleWrap">
          <span>俞姐姐智能门店销售系统</span>
        </div>
      </div>
      <van-list
        v-model="loading"
        :finished="finished"
        :immediate-check="false"
        @load="onLoad"
      >
        <van-cell class="vanWarp" v-for="(item, index) in goodsList" :key="index">
          <div class="goodsList" >
            <img :src="item.mainImages" alt="" class="goodsImg" @click="goDetail(item)">
            <div class="infoWrap">
              <p class="title" @click="goDetail(item)">{{item.goodsName}}</p>
              <p class="price" @click="goDetail(item)">￥<span>{{item.goodsPrice}}</span>/年</p>
              <div class="bottomInfo">
                <span class="saled">已售: {{item.salesVolume+item.allVolume}}件</span> <span class="buyNow" @click="buyNow(item)">立即购买</span>
              </div>
            </div>
          </div>
        </van-cell>
      </van-list>

      <tabbar :active="active"></tabbar>
    </div>
</template>

<script>
import {isLogin, loginByWeiXin, loginOut} from '@/api/common'
import Tabbar from '@/components/Tabbar/tabbar'
import {getGoodsList} from '@/api/goods'
export default {
  name: 'goodsList',
  data () {
    return {
      goodsList: [],
      active: 0,
      isLogin: 0, //未登录
      uid: null,
      token: null,
      loading: false,
      finished: false,
      pageNum: 1,
    }
  },
  components: {
    Tabbar
  },
  beforeCreate () {
    // document.querySelector('body').setAttribute('style', 'background: #f6f6f6;')
  },
  beforeDestroy () {
    // document.querySelector('body').setAttribute('style', 'background: #fff;')
  },
  beforeRouteLeave(to,from,next){
    console.log('离开页面之前', to, from)
    if(to.path === '/mine'){
      if(this.isLogin == 0) {
        this.$router.push({
          path: './login',
          query: {
            routeFrom: 'goodsList',
            uid : this.uid,
            token : this.token
          }
        })
      }else{
        next()
      }
    }else{
      next()
    }
  },
  created () {
    console.log(this.$getUrlPara(), 'this.$getUrlPara()')
    this.code = this.$getUrlPara('code')
    if (this.$route.query.code) {
      this.code = this.$route.query.code
    }
    window.sessionStorage.setItem('code' , JSON.stringify(this.code))
    this.init()
    this.initLoginStatus()
  },
  methods: {
    init() {
      getGoodsList({
        pageNum: this.pageNum,
        pageSize: 10
      }, res=>{
        this.loading = false
        if(res.code === 200) {
          if(this.pageNum == 1) {
            this.goodsList = res.data.list
          }else{
            res.data.list.forEach((ele) => {
              this.goodsList.push(ele)
            })
          }
          if(res.data.list.length === 0) {
            this.finished = true
          }
        }else{
          this.$toast(res.data)
        }
      })
    },
    initLoginStatus() {
      isLogin({}, res => {
        if(res.code === 200) {
          var data = {
            userIcon: res.data.wxUserIcon,
            nickName: res.data.wxName
          }
          window.localStorage.setItem('userIcon', JSON.stringify(data))
          if (res.data.isLogin === false) {
            this.wxLogin()
          } else { // 已登录
            this.isLogin = 1
          }
        }

      })
    },
    goDetail (item) {
      this.$router.push({
        path: './goodsDetail',
        query: {
          id: item.id,
          isLogin: this.isLogin,
          uid : this.uid,
          token : this.token
        }
      })
    },
    buyNow (item) {
      if(this.isLogin == 0) {
        this.$router.push({
          path: './login',
          query: {
            routeFrom: 'goodsList',
            uid : this.uid,
            token : this.token
          }
        })
      }else{
        this.$router.push({
          path: './payOrder',
          query: {
            id: item.id
          }
        })
      }
    },
    wxLogin() {
      loginByWeiXin({
        code: this.code
        // code: '123456qwer'
      }, res=>{
        console.log(res)
        if(res.code === 200) {
          this.uid = res.data.openId
          this.token = res.data.openToken
          var data = {
            openId: res.data.openId,
            openToken: res.data.openToken
          }
          window.localStorage.setItem('data', JSON.stringify(data))
          if(res.data.isSuccess) { // 微信登录
            this.isLogin = 1
          }else{
            this.isLogin = 0
          }
        }else if(res.code === 531){
          this.$dialog.alert({
            message: '由于违规您已被禁用，如有疑问请联系400-118-0099'
          })
        }
      })
    },
    onLoad() {
      setTimeout(() => {
        this.pageNum += 1
        this.init()
      })
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .goodContent{
    padding-bottom: 1rem;
    .banner{
      .wh(7.5rem,2.2rem);
    }
    .titleWrap{
      .wh(3.47rem,.74rem);
      background-image: url('~@/assets/image/goods/leftTitle.png');
      background-position: center;
      background-size: cover;
      position: absolute;
      top: .73rem;
      left: 0;
      line-height: .74rem;
      text-align: center;
      color: #fff;
      font-size: .26rem;
    }
    .goodsList{
      width: 7.02rem;
      background: #fff;
      position: relative;
      top: .2rem;
      left: .24rem;
      margin-bottom: .2rem;
      .flex;
      .goodsImg{
        .wh(1.7rem,1.7rem);
        padding: .3rem .2rem .3rem .24rem;
      }
      .infoWrap{
        padding-top: .24rem;
        .title{
          font-size: .28rem;
          color: #222;
          margin-bottom: .2rem;
        }
        .price{
          font-size: .24rem;
          color: #ff2742;
          margin-bottom: .15rem;
          span{
            font-size: .32rem;
          }
        }
        .bottomInfo{
          .flex;
          align-items: center;
          justify-content: space-between;
          width: 4.65rem;
          .saled{
            font-size: .24rem;
            color: #757575;
          }
          .buyNow{
            .wh(2rem,.65rem);
            background: #222;
            color: #fff;
            font-size: .24rem;
            border-radius: 1rem;
            text-align: center;
            line-height: .65rem;
          }
        }
      }
    }
  }
</style>
