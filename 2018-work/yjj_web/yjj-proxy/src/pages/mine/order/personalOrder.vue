<template>
  <div class="content" v-if="show">
    <van-pull-refresh v-model="isLoading" @refresh="onRefresh">
      <van-list
        v-model="loading"
        :finished="finished"
        :immediate-check="false"
        @load="loadMore"
      >
        <div class="orderWrap" v-for="(item, index) in orderList" :key="index" @click="goDetail(item)" v-if="orderList.length > 0">
          <div class="orderNum">订单编号: {{item.orderNo}}</div>
          <div class="goodsList">
            <img :src="item.goodsImages" alt="" class="goodsImg">
            <div class="infoWrap">
              <p class="title">{{item.goodsName}}</p>
              <p class="price">￥<span>{{item.goodsPrice}}</span>/年</p>
              <p class="bottomInfo">
                <span class="saled">购买年限: {{item.goodsTimeNum}}年</span>
              </p>
            </div>
          </div>
          <div class="priceWrap van-hairline--top">
            <span v-if="type < 3">出处: {{item.selfName}}</span> <span>实付款: ￥{{item.goodsPrice}}</span>
          </div>
          <div v-if="type < 3" class="earn van-hairline--top">所得金额: ￥{{item.rewardMoney}}</div>
        </div>
      </van-list>
    </van-pull-refresh>
    <div v-if="orderList.length == 0" class="emptyStatusWrap">
      <img class="emptyStatus" src="~@/assets/image/common/empty-status.png">
      <p>暂无订单信息</p>
    </div>
  </div>
</template>

<script>
  import {getOrderList} from '@/api/order'
export default {
  name: 'personalOrder',
  data () {
    return {
      isLoading: false,
      orderList: [],
      type: null,
      pageSize: 10,
      pageNum: 1,
      loading: false,
      finished: false,
      show: false
    }
  },
  created () {
    if(this.$route.query.type) {
      this.type = this.$route.query.type
      if(this.type == 1){
        document.title = '客户订单'
      }else if(this.type == 2){
        document.title = '代理订单'
      }
    }
    this.init()
  },
  methods: {
    init() {
      getOrderList({
        type: this.type,
        pageSize: this.pageSize,
        pageNum: this.pageNum
      }, res=>{
        this.show = true
        if(res.code === 200) {
          this.loading = false
          if (this.pageNum === 1) {
            this.orderList = res.data.list
          } else {
            res.data.list.forEach((element) => {
              this.orderList.push(element)
            })
          }
          if (res.data.hasNextPage === false) {
            this.finished = true
          }
        }else{
          this.$toast(res.data)
        }
      })
    },
    loadMore() {
      setTimeout(() => {
        this.pageNum += 1
        this.init()
      }, 500)
    },
    onRefresh () {
      setTimeout(() => {
        // this.$toast('刷新成功')
        this.pageNum = 1
        this.isLoading = false
        this.init()
      }, 500)
    },
    goDetail(item) {
      this.$router.push({
        path: './orderDetail',
        query: {
          orderNo: item.orderNo,
          type: this.type
        }
      })
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  [v-cloak] {
    display: none;
  }
.content{
  .orderWrap{
    width: 7.02rem;
    background-color: #fff;
    /*position: relative;*/
    /*left: .24rem;*/
    /*top: .2rem;*/
    margin-top: .2rem;
    margin-left: .24rem;
    .orderNum{
      height: 1rem;
      background-color: #f0f0f0;
      line-height: 1rem;
      padding-left: .24rem;
      border-top-left-radius: .08rem;
      border-top-right-radius: .08rem;
    }
    .goodsList{
      .flex;
      .goodsImg{
        .wh(1.7rem,1.7rem);
        padding: .3rem .2rem .3rem .24rem;
      }
      .infoWrap{
        padding-top: .3rem;
        .title{
          font-size: .28rem;
          color: #222;
          margin-bottom: .3rem;
        }
        .price{
          font-size: .24rem;
          color: #ff2742;
          margin-bottom: .25rem;
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
    .priceWrap{
      .flex;
      align-items: center;
      justify-content: space-between;
      text-align: center;
      padding: .3rem .24rem 0.3rem 0.24rem;
      border-bottom-left-radius: .08rem;
      border-bottom-right-radius: .08rem;
      font-size: .26rem;
      color: #222;
    }
    .earn{
      text-align: right;
      padding: .3rem .24rem 0.3rem 0.24rem;
      font-size: .26rem;
      color: #ff2742;
    }
  }
  .emptyStatusWrap{
    text-align: center;
    .emptyStatus{
      .wh(3.24rem,2.57rem);
      margin: 4rem auto 0.5rem;
    }
  }
}
</style>
