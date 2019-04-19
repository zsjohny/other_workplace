<template>
  <div class="content" v-cloak>
    <div class="goodsWrap">
      <img :src="data.mainImages" alt="" class="extremeGoods">
      <!--<img src="~@/assets/image/goods/extremeGoods.png" alt="" class="extremeGoods">-->
      <span class="saled">已售: {{data.salesVolume+ data.allVolume}}件</span>
    </div>
    <div class="detailWrap">
      <div class="detail">
        <div class="top">
          <span class="line"></span>
          <span>商品详情</span>
        </div>
        <img :src="item" alt="" v-for="(item, index) in goodsImages">
      </div>
    </div>
    <div class="footer">
      <div class="footerL van-hairline--top" @click="goGoods">
        <img src="~@/assets/image/common/mall.png" alt="" class="mall">
        <div>商城</div>
      </div>
      <div class="footerR" @click="goBuy">立即购买</div>
    </div>
  </div>
</template>

<script>
import {isLogin} from '@/api/common'
import {getGoodsDetail} from '@/api/goods'
export default {
  name: 'goodsDetail',
  data () {
    return {
      id: '',
      data: {},
      uid: '',
      token: '',
      isLogin: '',
      goodsImages: []
    }
  },
  beforeCreate () {
    // document.querySelector('body').setAttribute('style', 'background: #f6f6f6;')
  },
  beforeDestroy () {
    // document.querySelector('body').setAttribute('style', 'background: #fff;')
  },
  created () {
    if(this.$route.query.id) {
      this.id = this.$route.query.id
    }
    if(this.$route.query.isLogin){
      this.isLogin = this.$route.query.isLogin
    }
    this.code = this.$getUrlPara('code')
    if(this.$route.query.uid) {
      this.uid = this.$route.query.uid
    }
    if(this.$route.query.token){
      this.token = this.$route.query.token
    }

    console.log(this.$route.query.isLogin,'..................///////////')
    this.init()
  },
  methods: {
    init(){
      getGoodsDetail({
        id: this.id
      },res=>{
        if(res.code === 200) {
          this.data = res.data
          this.goodsImages = JSON.parse(this.data.goodsImages)

        }else{
          this.$toast(res.msg)
        }
      })
    },
    goGoods () {
      this.$router.push({
        path: './goodsList'
      })
    },
    goBuy () {
      if(this.isLogin == 0) { // 未登录
        this.$router.push({
          path: './login',
          query: {
            routeFrom: 'goodsDetail',
            uid : this.uid,
            token : this.token
          }
        })
      }else{
        this.$router.push({
          path: './payOrder',
          query: {
            id: this.data.id
          }
        })
      }
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    .goodsWrap{
      background: #fff;
      position: relative;
      .extremeGoods{
        .wh(7.02rem,3.78rem);
        padding: .2rem .24rem 0.6rem .24rem;
      }
      .saled{
        position: absolute;
        right: 0.24rem;
        bottom: 0.1rem;
      }
    }
    .detailWrap{
      margin-top: .2rem;
      background: #fff;
      padding-bottom: 1rem;
      .detail{
        width: 7.02rem;
        margin-left: .24rem;
        font-size: .32rem;
        padding-top: .2rem;
        .top{
          display: flex;
          align-items: center;
          margin-bottom: .2rem;
          .line{
            .wh(.06rem, .32rem);
            background-color: #222;
            border-radius: .05rem;
            margin-right: .15rem;
          }
        }

      }
    }
    .footer{
      .flex;
      align-items: center;
      position: fixed;
      bottom: 0;
      .wh(7.5rem,1rem);
      .footerL{
        .wh(3rem,1rem);
        background: #fff;
        .flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        color: #222;
        font-size: .2rem;
        .mall{
          .wh(.44rem,.44rem);
          margin-bottom: .05rem;
        }
      }
      .footerR{
        .wh(4.5rem,1rem);
        background-color: #222;
        color: #fff;
        text-align: center;
        font-size: .3rem;
        line-height: 1rem;
      }
    }
  }
  [v-cloak] {
    display: none;
  }
</style>
