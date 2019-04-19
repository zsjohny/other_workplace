<template>
  <div class="content">
    <div class="logoWrap"><img src="~@/assets/image/mine/poster/yjj-logo.png" alt="" class="logo"></div>
    <div class="posterInfo">
      <img src="~@/assets/image/mine/poster/qrCode-title.png" alt="" class="title">
      <img src="~@/assets/image/mine/poster/yjj-qrCode.png" alt="" class="miniCode">
      <p class="info">扫描上方二维码进入俞姐姐公众号</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'qrCode',
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
    .wh(7.5rem,12.06rem);
    background: url("~@/assets/image/mine/poster/qrCodeBg.png");
    background-size: cover;
    background-position: center;
    .logoWrap{
      .logo{
        .wh(1.67rem,1.67rem);
        position: absolute;
        border-radius: 100px;
        top: 1.2rem;
        left: 2.915rem;
      }
    }
    .posterInfo{
      .wh(6.12rem,8.38rem);
      background: url("~@/assets/image/mine/poster/modal2.png");
      background-position: center;
      background-size: cover;
      position: absolute;
      top: 2rem;
      left: .69rem;
      text-align: center;
      .title{
        .wh(5.29rem,.35rem);
        margin: 2rem auto 0;
      }
      .miniCode{
        .wh(2.6rem,2.6rem);
        margin: 1rem auto 0;
      }
      .info{
        color: #848484;
        font-size: .26rem;
        margin-top: 1rem;
      }
    }
  }
</style>
