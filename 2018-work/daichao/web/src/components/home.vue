<template>
  <div class="homePage" :style="{'-webkit-overflow-scrolling': scrollMode}">
    <div class="inform">
      <div id="informSwiper" class="swiper-container">
        <div class="swiper-wrapper">
          <div class="swiper-slide" v-for="(item , index) in informList" :key="index">
            <p>{{item.content}}</p>
          </div>
        </div>
      </div>
    </div>
    <mt-loadmore 
      @bottom-status-change="handleBottomChange"
      :bottom-method="loadBottom"
      :bottom-all-loaded="allLoaded" 
      :auto-fill="autoFill"
      ref="loadmore" >
      <div class="banner">
        <div id="banner" class="swiper-container" >
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="(item , index) in bannerData" :key="index">
              <a :href="item.pointUrl">
                <img :src="item.picUrl" :alt="item.titleDesc">
              </a>
            </div>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="con_item" v-for="item in pageList" :key="item.id">
          <div class="hot">
            <img src="~@/assets/hot.png" />
          </div>
          <div class="item_main">
            <h3 class="im_title"> {{ item.appName }} </h3>
            <p class="line"></p>
            <div class="explain">
              <div class="explain_left">
                <p>最高借款金额</p>
                <p>借款期限</p>
                <p>放款时间</p>
              </div>
              <div class="explain_middle">
                <p>(芝麻分越高，额度越高)</p>
                <p>(可以加期限哦)</p>
                <p>(无需门槛，最宽松放款)</p>
              </div>
              <div class="explain_right">
                <p>{{ item.creditMinLimit }}-{{ item.creditMaxLimit }}元</p>
                <p>{{ item.creditMinTerm }}-{{ item.creditMaxTerm }}天</p>
                <p>{{ item.sucessPayTime }}分钟</p>
              </div>
            </div>
            <p class="line"></p>
            <div class="grade">
              <div class="grade_tit">
                <span>评分</span> 
                <span class="grade_num"> {{ item.sucessRate*1000/100 }} </span>
              </div>
              <Star :grade="item.sucessRate*1000/100" />
            </div>
            <div @click="changeRouter(item.id , item)" class="borrow_btn" >立即借款</div>
          </div>
        </div>
      </div>
      <div class="tip" v-show="allLoaded">没有更多数据了</div>
      <div slot="bottom" class="mint-loadmore-bottom">
        <span v-show="bottomStatus !== 'loading'" :class="{ 'is-rotate': bottomStatus === 'drop' }">↑</span>
        <span v-show="bottomStatus === 'loading'">
          <mt-spinner v-show="bottomStatus == 'loading'" color="#26a2ff"></mt-spinner>
        </span>
      </div>
      <div style="height: 1.2rem"></div>
      </mt-loadmore>
  </div>
</template>

<script>
import Vue from "vue";
import Star from "@/components/star.vue";
import { Loadmore, Spinner } from "mint-ui";
import Swiper from "swiper";
import "swiper/dist/css/swiper.min.css";
Vue.component(Loadmore.name, Loadmore);
Vue.component(Spinner.name, Spinner);

export default {
  name: "App",
  components: {
    Star
  },
  data() {
    return {
      bannerData: [],
      pageList: [],
      informList: [],
      page: "1",
      pageSize: "10",
      allLoaded: false, //数据是否加载完毕
      autoFill: false, //若为真，loadmore 会自动检测并撑满其容器
      bottomStatus: "",
      scrollMode: "auto"
    };
  },
  created() {
    this.getData();
  },
  methods: {
    getData: function() {
      this.$get('/v1/home/base-info',{
        page: this.page,
        pageSize: this.pageSize
      }).then(res => {
        this.bannerData = res.data.data.banners;
        this.pageList = res.data.data.creditProducts;
        this.informList = res.data.data.broadcast;
        this.$nextTick(function() {
          this.scrollMode = "touch";
          this.initSwiper();
        });
      }).catch(e => console.log(e))
    },

    loadBottom: function() {
      this.handleBottomChange("loading");
      this.page++;
      this.$get('/v1/home/base-info',{
        page: this.page,
        pageSizeL: this.pageSize
      }).then(res => {
        if(res.data.data.creditProducts.length !== 0) {
          for (var i = 0; i < res.data.data.creditProducts.length; i++) {
            this.pageList.push(res.data.data.creditProducts[i]);
          }
          this.handleBottomChange("loadingEnd");
        } else {
          this.allLoaded = true;
          this.handleBottomChange("loadingEnd");
        }
        this.$refs.loadmore.onBottomLoaded();
      }).catch(e => console.log(e))
    },

    handleBottomChange: function(status) {
      this.bottomStatus = status;
    },

    initSwiper: function() {
      new Swiper("#informSwiper", {
        observer: true,
        observeParents: true,
        direction: "vertical", //垂直方向
        autoplay: {
          delay: 2500,
          disableOnInteraction: false
        },
        loop: true
      });
      new Swiper('#banner' , {
        observer:true,
        observeParents:true,
        slidesPerView: "auto",
        centeredSlides: true,
        spaceBetween: 10,
        autoplay: {
          delay: 3000,
          disableOnInteraction: false,
        },
        initialSlide: 1,
        loop: true,
      })
    },

    changeRouter: function(itemID , loanList) {
      this.$post('/v1/credit-shop/uv',{
        uid: '',
        productId: itemID
      }).then(res => {
        this.$router.push({
          name: 'details',
          params: {id: itemID},
          query: {list: JSON.stringify(loanList)}
        })
      }).catch(e => console.log(e))
      
    }
  }
};
</script>

<style lang="scss">
@import "~@/lib/reset.scss";
.homePage {
  @include rect(100%, 100%);
}
.inform {
  @include rect(100%, 0.3rem);
  @include background-color(#ffd284);
  @include text-color(#ffffff);
  overflow: hidden;
  p {
    @include text-align();
    @include line-height(0.3rem);
    @include font-size(0.2rem);
  }
}
.banner {
  @include rect(100%, 3.31rem);
  margin-bottom: 0.16rem;
  padding-top: 0.2rem;
    .swiper-container {
      width: 100%;
      height: 100%;
      .swiper-wrapper {
        .swiper-slide {
          border-radius: 0.2rem;
          width:6.7rem;
          height:3.0rem;
          -webkit-box-shadow:0 3px 4px rgba(62,62,62,0.25);
          box-shadow:0 3px 4px rgba(62,62,62,0.25);
          img {
            @include rect(100%, 3.0rem);
            border-radius: 0.2rem;
          }
        }
        .swiper-slide-prev,.swiper-slide-next  {
          margin-top: 0.07rem;
          height: 2.84rem !important;
          img {
            height: 2.84rem !important;
          }
        }
      }
    }
}
.content {
  @include rect(100%, auto);
  padding: 0 0.27rem 0 0.3rem;
}
.con_item {
  @include rect(100%, 3.54rem);
  padding: 0.04rem 0.03rem 0 0;
  position: relative;
  margin-bottom: 0.15rem;
  .hot {
    @include rect(0.77rem, 0.86rem);
    position: absolute;
    right: 0;
    top: 0;
    img {
      @include rect(100%, 100%);
    }
  }
  .item_main {
    @include rect(100%, 3.5rem);
    @include background-color(#fff);
    border-radius: 0.1rem;
    box-shadow: 0.04rem 0.07rem 0.14rem rgba(42, 42, 42, 0.07);
    padding: 0.2rem 0.31rem;
    .im_title {
      @include rect(100%, 0.29rem);
      @include font-size(0.3rem);
      line-height: 1;
      @include text-color(#404a4c);
    }
    .line {
      @include rect(5.92rem, 1px);
      margin: 0.18rem auto;
      background: rgba(133, 134, 151, 0.2);
    }
    .explain {
      @include rect(100%, 1.07rem);
      @include flexbox();
      .explain_left {
        @include rect(1.65rem, 100%);
        @include font-size(0.24rem);
        p {
          line-height: 0.33rem;
        }
      }
      .explain_middle {
        height: 100%;
        @include font-size(0.18rem);
        @include text-color(#858697);
        p {
          line-height: 0.33rem;
        }
      }
      .explain_right {
        @include flex();
        @include rect(auto, 100%);
        @include text-align(right);
        @include font-size(0.24rem);
        @include text-color(#fc5c5b);
        p {
          line-height: 0.33rem;
          padding-right: 0.26rem;
          background: url("~@/assets/right.png") center right no-repeat;
          background-size: 0.12rem 0.2rem;
        }
      }
    }
    .grade {
      @include rect(100%, 0.3rem);
      margin-bottom: 0.19rem;
      .grade_tit {
        float: right;
        @include rect(auto, 100%);
        @include font-size(0.24rem);
        @include text-color(#fc5c5b);
        @include line-height(0.26rem);
      }
    }
    .borrow_btn {
      @include rect(3rem, 0.5rem);
      @include background-color(#fc5c5b);
      margin: 0 auto;
      border-radius: 0.1rem;
      box-shadow: 0.04rem 0.04rem 0.04rem rgba(252, 92, 91, 0.34);
      @include font-size(0.3rem);
      @include text-align();
      @include line-height(0.5rem);
      @include text-color(#ffffff);
      letter-spacing: 0.03rem;
    }
  }
}

.mint-loadmore-bottom span {
  display: inline-block;
  -webkit-transition: 0.2s linear;
  transition: 0.2s linear;
  vertical-align: middle;
}

.mint-loadmore-bottom span.is-rotate {
  -webkit-transform: rotate(180deg);
  transform: rotate(180deg);
}

.tip{
  width: 100%;
  text-align: center;
}
</style>
