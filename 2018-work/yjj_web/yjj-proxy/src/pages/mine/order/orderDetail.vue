<template>
  <div class="content">
    <div class="addressWrap">
      <img src="~@/assets/image/common/address.png" alt="" class="address">
      <div class="middleWrap">
        <p class="info"><span>{{info.receiverName}}</span> <span>{{info.receiverPhone}}</span></p>
        <p>{{info.receiverAddress}}</p>
      </div>
      <img src="~@/assets/image/common/rightArrow.png" alt="" class="rightArrow">
    </div>
    <div class="orderWrap" @click="goDetail(item)">
      <div class="goodsList">
        <img :src="info.goodsImages" alt="" class="goodsImg">
        <div class="infoWrap">
          <p class="title">{{info.goodsName}}</p>
          <p class="price">￥<span>{{info.goodsPrice}}</span>/年</p>
          <p class="bottomInfo">
            <span class="saled">购买年限: {{info.goodsTimeNum}}年</span>
          </p>
        </div>
      </div>
      <div class="earn van-hairline--top" v-if="type == 3">实付款: ￥{{info.actualPrice}}</div>
      <div class="priceWrap van-hairline--top" v-if="type < 3">
        <span>实付款:</span> <span> ￥{{info.actualPrice}}</span>
      </div>
      <div class="earnWrap" v-if="type < 3">
        <span>所得金额:</span> <span>￥{{info.rewardMoney}}</span>
      </div>
    </div>
    <div class="orderInfo">
      <p class="title">订单编号: <span>{{info.orderNo}}</span></p>
      <p class="title">下单时间: <span>{{$formatDate(info.createTime)}}</span></p>
      <p class="title">下单平台: <span>俞姐姐公众号</span></p>
      <p class="title">支付方式: <span>微信在线支付</span></p>
      <p class="title">交易流水号: <span>{{info.payOrderNo}}</span></p>
    </div>
    <div class="orderAscription" v-if="type < 3">
      <p class="title">订单归属:</p>
      <div class="ascriptionWrap">
        <!--<div class="ascription" v-for="(item, index) in ascription" :key="index">-->
          <!--<img src="~@/assets/image/common/logo.png" alt="" class="user">-->
          <!--<p>婆娑若梦/<span class="userType">{{item.user}}</span></p>-->
        <!--</div>-->
        <div class="ascription">
          <img src="~@/assets/image/common/logo.png" alt="" class="user">
          <p>{{info.selfName}}/<span class="userType">下单</span></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {getOrderDetail} from '@/api/order'
export default {
  name: 'orderDetail',
  data () {
    return {
      orderList: [
        {url: require('@/assets/image/goods/vip.png'), title: '俞姐姐智能门店系统至尊版', price: '29800.00'}
      ],
      ascription: [
        {user: '下单'},
        {user: '县代理'},
        {user: '市代理'}
      ],
      orderNo: '',
      info: '',
      type: ''
    }
  },
  created () {
    if(this.$route.query.orderNo) {
      this.orderNo = this.$route.query.orderNo
    }
    if(this.$route.query.type) {
      this.type = this.$route.query.type
    }
    this.init()
  },
  methods: {
    init() {
      getOrderDetail({
        orderId: this.orderNo
      }, res=>{
        if(res.code === 200) {
          this.info = res.data[0]
        }
      })
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    .addressWrap{
      .wh(7.5rem,2rem);
      background-color: #fff;
      .flex;
      align-items: center;
      justify-content: space-between;
      .address{
        .wh(.44rem,.44rem);
        padding-left: .24rem;
      }
      .rightArrow{
        .wh(.44rem,.44rem);
        padding-right: .24rem;
      }
      .middleWrap{
        width: 6.14rem;
        margin-left: .34rem;
        margin-right: .44rem;
        color: #848484;
        font-size: .24rem;
        .info{
          .flex;
          justify-content: space-between;
          color: #222;
          font-size: .3rem;
          margin-bottom: .1rem;
        }
      }
    }
    .orderWrap{
      width: 7.5rem;
      background-color: #fff;
      margin-top: .2rem;
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
        padding: .3rem .24rem 0rem 0.24rem;
        font-size: .24rem;
        color: #222;
      }
      .earnWrap{
        .priceWrap();
        color: #ff2742;
        padding-bottom: .3rem;
      }
      .earn{
        text-align: right;
        padding: .3rem .24rem 0.3rem 0.24rem;
        font-size: .26rem;
        color: #222;
      }
    }
    .orderInfo{
      margin-top: .2rem;
      background-color: #fff;
      padding: .3rem .24rem .3rem .24rem;
      .title{
        color: #222;
        font-size: .28rem;
        margin-bottom: .3rem;
        span{
          color: #757575;
          font-size: .26rem;
          margin-left: .24rem;
        }
      }
    }
    .orderAscription{
      background-color: #fff;
      margin-top: .2rem;
      padding: .3rem .24rem .3rem .24rem;
      .title{
        color: #222;
        font-size: .28rem;
      }
      .ascriptionWrap{
        .flex;
        align-items: center;
        justify-content: space-between;
        padding: .2rem .15rem .2rem .15rem;
        .ascription{
          text-align: center;
          color: #757575;
          font-size: .24rem;
          .user{
            .wh(1.34rem,1.34rem);
            border-radius: .7rem;
            margin: 0 auto .15rem;
          }
          .userType{
            color: #ff2742;
          }
        }
      }
    }
  }
</style>
