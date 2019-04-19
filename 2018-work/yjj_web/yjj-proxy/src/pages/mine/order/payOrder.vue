<template>
  <div class="content">
    <div class="addressWrap" @click="goAddress" v-if="addressInfo !== '' ">
      <img src="~@/assets/image/common/address.png" alt="" class="address">
      <div class="middleWrap">
        <p class="info"><span>{{addressInfo.receiverName}}</span> <span>{{addressInfo.receiverPhone}}</span></p>
        <p>{{address}}{{addressInfo.receiverAddress}}</p>
      </div>
      <img src="~@/assets/image/common/rightArrow.png" alt="" class="rightArrow">
    </div>
    <div class="addressWrap" @click="goAddress" v-if="addressInfo == ''">
      <p class="noAddress">您还没有设置收货地址，赶紧去设置吧！</p>
      <img src="~@/assets/image/common/rightArrow.png" alt="" class="rightArrow">
    </div>
    <img src="~@/assets/image/goods/colorLine.png" alt="" class="colorLine">
    <div class="orderWrap" @click="goDetail()">
      <div class="goodsList">
        <img :src="goodsList.mainImages" alt="" class="goodsImg">
        <div class="infoWrap">
          <p class="title">{{goodsList.goodsName}}</p>
          <p class="price">￥<span>{{goodsList.goodsPrice}}</span>/年</p>
        </div>
      </div>
      <div class="priceWrap van-hairline--top">
        <span>购买年限:</span> <span class="year"> {{goodsList.timeNum}}年</span>
      </div>
      <div class="earnWrap van-hairline--top">
        <span>合计:</span> <span class="price">￥{{goodsList.goodsPrice}}</span>
      </div>
    </div>
    <div class="payBtn" @click="goPay">
      <img src="~@/assets/image/goods/wx.png" alt="" class="wx">
      <span>微信快捷支付</span>
    </div>
    <div class="loadings" v-if="showLoading">
      <van-loading type="spinner" color="white" />
    </div>
  </div>
</template>

<script>
  import {getBeforeOrderDetail, goPay, cancelOrder} from '@/api/goods'
export default {
  name: 'payOrder',
  data () {
    return {
      orderList: [
        {url: require('@/assets/image/goods/vip.png'), title: '俞姐姐智能门店系统至尊版', price: '29800.00'}
      ],
      goodsId: '',
      addressInfo: '',
      goodsList: '',
      address: '',
      payInfo: '',
      showLoading: false,
      payDisabled: false
    }
  },
  created () {
    if(this.$route.query.id) {
      this.goodsId = this.$route.query.id
    }
    this.init()
  },
  methods: {
    init() {
      getBeforeOrderDetail({
        goodsId: this.goodsId
      }, res=> {
        if(res.code === 200) {
          this.addressInfo = res.data.address
          this.goodsList = res.data.goods
          this.address = this.addressInfo.province + this.addressInfo.city + this.addressInfo.county
        }
      })
    },
    goDetail () {

    },
    goPay() {
      if(this.payDisabled === false) {
        this.payDisabled = true
        if(this.addressInfo.receiverAddress){
          this.showLoading = true
          goPay({
            goodsId: this.goodsList.id,
            addressId: this.addressInfo.id
          }, res=>{
            if(res.code === 200) {
              this.payDisabled = false
              this.showLoading = false
              this.payInfo = res.data
              this.wxPay()
            }else{
              this.payDisabled = false
              this.showLoading = false
              this.$toast(res.data)
            }
          })
        }else{
          this.$toast('请添加地址')
          this.payDisabled = false
        }
      }
    },
    cancelPay() { // 取消支付
      cancelOrder({
        orderId: this.payInfo.orderId
      }, res=>{
        if(res.code === 200) {
          this.$toast('支付取消')
        }
      })
    },
    onBridgeReady(){
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
          "appId":this.payInfo.appId,     //公众号名称，由商户传入
          "paySign":this.payInfo.paySign,         //微信签名
          "timeStamp":this.payInfo.timeStamp, //时间戳，自1970年以来的秒数
          "nonceStr":this.payInfo.nonceStr , //随机串
          "package":this.payInfo.package,  //预支付交易会话标识
          "signType":this.payInfo.signType     //微信签名方式
        },
        (res) => {
          if(res.err_msg == "get_brand_wcpay_request:ok" ) {
            this.$router.replace({
              path: './orderDetail',
              query: {
                orderNo: this.payInfo.orderId,
                type: 3
              }
            })
          }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
            this.cancelPay()
          }else if(res.err_msg == "get_brand_wcpay_request:fail" ){
            this.$toast('支付失败')
          } //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
        }
      );
    },
    wxPay() {
      if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
          document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady, false);
        }else if (document.attachEvent){
          document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady);
          document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady);
        }
      }else{
        this.onBridgeReady();
      }
    },
    goAddress() {
      this.$router.push({
        path: './addressList',
        query: {
          routeFrom: 'payOrder',
          id: this.goodsId
        }
      })
    },
    goAddressDetail() {
      this.$router.push({
        path: './add-audit-address',
        query: {
          routeFrom: 'payOrder',
          id: this.goodsId
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
      .noAddress{
        color: #222;
        font-size: .28rem;
        margin-left: .24rem;
      }
    }
    .colorLine{
      .wh(7.5rem,.11rem);
    }
    .orderWrap{
      width: 7.5rem;
      background-color: #fff;
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
            margin-bottom: .85rem;
          }
          .price{
            font-size: .24rem;
            color: #ff2742;
            span{
              font-size: .32rem;
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
        font-size: .28rem;
        color: #222;
        .year{
          color: #757575;
        }
      }
      .earnWrap{
        .priceWrap();
        padding-bottom: .3rem;
        .price{
          color: #ff2742;
        }
      }
    }
    .payBtn{
      .wh(7.02rem,.88rem);
      color: #fff;
      font-size: .3rem;
      border-radius: .08rem;
      background-color: #222;
      position: absolute;
      bottom: .6rem;
      left: .24rem;
      .flex;
      align-items: center;
      justify-content: center;
      .wx{
        .wh(.42rem,.34rem);
        margin-right: .2rem;
      }
    }
    .loadings{
      .wh(2rem,2rem);
      background-color: #222;
      border-radius: .1rem;
      position: fixed;
      top: 50%;
      left: 50%;
      -webkit-transform: translate(-50%,-50%);
      transform: translate(-50%,-50%);
      .flex;
      align-items: center;
      justify-content: center;
    }
  }
</style>
