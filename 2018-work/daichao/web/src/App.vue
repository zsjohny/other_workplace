<template>
  <div id="app">
    <router-view name="content"></router-view>
    <!-- 广告位 -->
    <router-view name="footer"></router-view>
  </div>
</template>

<script>
  export default {
    
  }
</script>

<style lang="scss">
@import "@/lib/reset.scss";
#app{
  @include rect(100% , 100%);
  overflow: scroll;
  font-size: 0.3rem;
  @include background-color(#F1FDFB);
}
.advertising{
  position: fixed;
  bottom: 0;
  z-index: 998;
  @include rect(100% , 1rem);
  padding: 0.19rem 0.3rem;
  @include background-color(#FFFFFF);
  @include flexbox();
  -webkit-box-shadow:0px 1px 0.06rem 0px rgba(0,0,0,0.21);
  box-shadow:0px 1px 0.06rem 0px rgba(0,0,0,0.21);
  .left{
    @include rect(0.62rem , 0.62rem);
    background: url('~@/assets/logo.png') 0 0 no-repeat;
    background-size: 100%;
  }
  .center{
    @include flex();
    padding-left: 0.36rem;
    @include font-size(0.3rem);
    @include text-color(#404A4C);
    @include line-height(0.62rem);
  }
  .right{
    @include rect(0.36rem , 0.36rem);
    margin-top: 0.1rem;
    background: url('~@/assets/close.png') 0 0 no-repeat;
    background-size: 100%;
  }
}

</style>
