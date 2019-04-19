<template>
      <van-tabbar v-model="num" @change="changeTab">
        <van-tabbar-item to="./goodsList">
          <span>商城</span>
          <img
            class="icon"
            slot="icon"
            slot-scope="props"
            :src="props.active ? icon.mallSelect : icon.mall"
          >
        </van-tabbar-item>
        <van-tabbar-item to="./mine">
          <span>我的</span>
          <img
            class="icon"
            slot="icon"
            slot-scope="props"
            :src="props.active ? icon.mineSelect : icon.mine"
          >
        </van-tabbar-item>
      </van-tabbar>
</template>

<script>
export default {
  name: 'tabbar',
  data () {
    return {
      icon: {
        mall: require('@/assets/image/common/mall.png'),
        mallSelect: require('@/assets/image/common/mallSelect.png'),
        mine: require('@/assets/image/common/mine.png'),
        mineSelect: require('@/assets/image/common/mineSelect.png')
      },
      num: this.active
    }
  },
  props: ["active"],
  // props: {
  //   active: {
  //     type: Number,
  //     default: 0
  //   }
  // },
  created () {

  },
  methods: {
    changeTab (active) {
      console.log(active)
      // this.active = active
    }
  }
}
</script>

<style scoped lang="less">
.icon{
  width: .44rem;
  height: .44rem;
}
</style>
