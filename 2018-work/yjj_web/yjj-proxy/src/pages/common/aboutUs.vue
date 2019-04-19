<template>
  <div class="content">
    <div class="topWrap">
      <img src="~@/assets/image/common/logo.png" alt="" class="logo">
      <p class="title">专注女装精品供应链</p>
    </div>
    <p class="business">商务合作</p>
    <div class="mailWrap">
      <img src="~@/assets/image/common/mail.png" alt="" class="mail">
      <span class="info">14511636222@163.com</span>
    </div>
    <div class="phoneWrap">
      <img src="~@/assets/image/common/phone.png" alt="" class="phone">
      <span class="info">400-118-0099</span>
    </div>
    <div class="addressWrap">
      <img src="~@/assets/image/common/location.png" alt="" class="location">
      <span class="info">浙江省杭州市滨江区长河街道秋溢路288号</span>
    </div>
    <p class="info">东冠高新科技园1幢1208室</p>
    <p class="version">当前版本V1.0</p>
  </div>
</template>

<script>
export default {
  name: 'aboutUs',
  data () {
    return {

    }
  },
  created () {

  },
  methods: {

  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    text-align: center;
    .topWrap{
      /*position: relative;*/
      /*top: 2rem;*/
      padding-top: 2rem;
      text-align: center;
      .logo{
        .wh(1.61rem,1.61rem);
        margin: 0 auto .2rem;
      }
      .title{
        color: #757575;
        font-size: .32rem;
      }
    }
    .business{
      color: #222;
      font-size: .36rem;
      margin-top: 2.5rem;
    }
    .mailWrap{
      .flex;
      align-items: center;
      justify-content: center;
      margin-top: .6rem;
      .mail{
        .wh(.37rem,.27rem);
        margin-right: .1rem;
      }
    }
    .phoneWrap{
      .flex;
      align-items: center;
      justify-content: center;
      margin-top: .38rem;
      .phone{
        .wh(.44rem,.44rem);
        margin-right: .1rem;
      }
    }
    .addressWrap{
      .flex;
      align-items: center;
      justify-content: center;
      margin-top: .42rem;
      .location{
        .wh(.44rem,.44rem);
        margin-right: .1rem;
      }
    }
    .version{
      margin-top: .8rem;
      color: #bfbfbf;
      font-size: .26rem;
    }
    .info{
      color: #757575;
      font-size: .28rem;
    }
  }
</style>
