<template>
  <ul class="comment_star">
    <li v-for="(item, index) in starArr" :key="index" >
      <img :src=" [ item === 0 ? starIcon[0] : starIcon[1] ] " />
    </li>
  </ul>
</template>

<script>
export default {
  data() {
    return {
      starArr: [],
      starIcon: [
        require("../assets/star.png"),
        require("../assets/star_show.png")
      ]
    };
  },
  props: ["grade"], //获取父组件传递过来的评分参数

  methods: {
    //处理评分所得对应星级
    starLevel(evaluate) {
      let starGrade = Math.round(evaluate / 2); //最高评分为10，星级只有5级 所以需要除2，再四舍五入获取评分数
      for (let i = 1; i <= 5; i++) {
        i <= starGrade ? this.starArr.push(1) : this.starArr.push(0); //如果i小于当前评分，则满星，否则为不满
      }
    }
  },
  mounted() {
    if (this.grade) {
      this.starLevel(this.grade)
    } 
  },
};
</script>

<style lang="scss" scoped>
@import "~@/lib/reset.scss";

.comment_star {
  @include rect(1.8rem, 100%);
  float: right;
  li {
    @include rect(0.26rem, 0.26rem);
    margin-right: 0.1rem;
    float: left;
    img {
      @include rect(100%, 100%);
      display: block;
    }
  }
}
</style>
