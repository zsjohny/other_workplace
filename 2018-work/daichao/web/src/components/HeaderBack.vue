<template>
  <div class="header">
    <i class="icon_left" @click="goBack"></i><span :style="{color:txtColor}">{{headerTitle}}</span>
  </div>
</template>

<script>
  export default {
    props: ['headerTitle','txtColor'],
    methods:{
      goBack:function(){
        window.history.go(-1)
      }
    }
  }
</script>
<style lang="scss" scoped>
@import '~@/lib/reset.scss';

.header{
  position: fixed;
  top: 0;
  left: 0;
  @include rect(100% , 0.88rem);
  @include background-color(#FFFFFF);
  box-shadow:0.02rem 0.03rem 0.15rem rgba(0,0,0,0.18);
   -webkit-box-shadow:0.02rem 0.03rem 0.15rem rgba(0,0,0,0.18);
  @include text-align();
  @include line-height(0.88rem);
  margin-bottom:0.15rem;
  z-index: 999;
  .icon_left{
    position:fixed;
    left: 0.4rem;
    top: 0.27rem;
    display: inline-block;
    width: 0.2rem;
    height: 0.35rem;
    vertical-align: text-bottom;
    background: url('~@/assets/back.png') no-repeat;
    background-size: auto 100%;

  }
  span{
    @include font-size(0.36rem);
    @include text-color(#262626);
  }
}
</style>
