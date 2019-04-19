<template>
  <div class="content">
    <van-swipe :width="325">
      <van-swipe-item>
        <div class="total">
          <img src="~@/assets/image/mine/profit/total.png" alt="" class="totalImg">
          <div class="totalWrap">
            <p class="totalTitle">总收入</p>
            <p class="totalPrice">￥{{earnInfo.allReward}}<span></span></p>
            <div class="detailWrap van-hairline--top">
              <div>
                <p>已发收入</p>
                <p class="money">￥<span>{{earnInfo.arealdyReward}}</span></p>
              </div>
              <div>
                <p>未发收入</p>
                <p class="money">￥<span>{{earnInfo.noReward}}</span></p>
              </div>
            </div>
          </div>
        </div>
      </van-swipe-item>
      <van-swipe-item>
        <div class="today">
          <img src="~@/assets/image/mine/profit/today.png" alt="" class="todayImg">
          <div class="todayWrap">
            <p>今日收入</p>
            <p class="money">￥<span>{{earnInfo.toDayReward}}</span></p>
          </div>
        </div>
      </van-swipe-item>
    </van-swipe>
    <div class="calenderWrap">
      <div class="calenderLeft">
        <i></i>
        <span>收益流水</span>
      </div>
      <div class="calender" @click="selectDate">
        <img src="~@/assets/image/mine/profit/timeWrap.png" alt="" class="timeWrap">
        <img src="~@/assets/image/mine/profit/timeImg.png" alt="" class="timeImg">
        <span>{{startTime}} - {{endTime}}</span>
      </div>
    </div>
    <van-list
      v-model="loading"
      :finished="finished"
      :immediate-check="false"
      @load="loadMore"
    >
      <div class="orderListWrap" v-if="orderList.length > 0">
        <div class="orderList">
          <p class="title">今日</p>
          <div class="orderWrap van-hairline--surround" v-for="(item, index) in orderList" :key="index">
            <p class="orderNum">订单编号:
              <span class="num">{{item.orderNo}}</span>
              <span class="issue" v-if="item.isGrant == 0">待发放</span>
              <span class="issued" v-if="item.isGrant == 1">已发放</span>
            </p>
            <p>下单: {{item.selfName}}/上级代理: {{item.oneLevelName}}</p>
            <p>订单金额: ￥{{item.orderMoney}} /所得金额: ￥{{item.rewardMoney}}</p>
            <p>下单时间: {{$formatDate(item.createTime)}}</p>
          </div>
        </div>
      </div>
      <div v-if="orderList == 0" class="noOrder">
        <p>你还没有收益哦, 快去发展客户吧</p>
        <p class="goPoster" @click="goPoster">发展客户</p>
      </div>
    </van-list>
    <van-popup v-model="showDate" position="bottom">
      <van-datetime-picker
        v-model="currentDate"
        type="date"
        title="起始时间"
        :max-date="maxDate"
        @confirm="confirmStart"
      />
    </van-popup>
    <van-popup v-model="showDateEnd" position="bottom">
      <van-datetime-picker
        v-model="currentDate"
        type="date"
        title="结束时间"
        :min-date="minDate"
        :max-date="maxDate"
        @confirm="confirmEnd"
      />
    </van-popup>
  </div>
</template>

<script>
  import {getProfit, getProfitMoney} from '@/api/profit'
export default {
  name: 'profit',
  data () {
    return {
      showDate: false,
      showDateEnd: false,
      currentDate: new Date(),
      maxDate: new Date(),
      minDate: new Date(),
      orderList: '',
      earnInfo: '',
      startTime: '',
      endTime: '',
      pageNum: 1,
      pageSize: 10,
      loading: false,
      finished: false,
    }
  },
  beforeCreate(){
    document.querySelector('body').setAttribute('style', 'background: #fff;')
  },
  created () {
    this.startTime = this.formatDate(new Date())
    this.endTime = this.formatDate(new Date())
    this.init()
    this.earn()
  },
  beforeDestroy () {
    document.querySelector('body').setAttribute('style', 'background: #f6f6f6;')
  },
  methods: {
    init() {
      getProfit({
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        startTime: this.startTime,
        endTime: this.endTime
      },res=>{
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
          this.$toast(res.msg)
        }
      })
    },
    loadMore() {
      setTimeout(() => {
        this.pageNum += 1
        this.init()
      }, 500)
    },
    earn() {
      getProfitMoney({}, res=>{
        if(res.code === 200) {
          this.earnInfo = res.data
        }else{
          this.$toast(res.msg)
        }
      })
    },
    selectDate() {
      this.showDate = !this.showDate
      this.minDate = new Date(2008,1,1)
    },
    confirmStart(value) {
      this.minDate = value
      this.startTime = this.formatDate(value)
      this.showDate = !this.showDate
      this.showDateEnd = !this.showDateEnd
    },
    confirmEnd(value) {
      this.endTime = this.formatDate(value)
      this.init()
      this.showDateEnd = !this.showDateEnd
    },
    formatDate(date) {
      var y = date.getFullYear();
      var m = date.getMonth() + 1;
      m = m < 10 ? ('0' + m) : m;
      var d = date.getDate();
      d = d < 10 ? ('0' + d) : d;
      // var h = date.getHours();
      // var minute = date.getMinutes();
      // minute = minute < 10 ? ('0' + minute) : minute;
      // var second= date.getSeconds();
      // second = minute < 10 ? ('0' + second) : second;
      return y + '-' + m + '-' + d;
    },
    goPoster() {
      this.$router.push({
        path: './createPoster'
      })
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';

  .content{
    .total{
      background-color: #fff;
      padding: .3rem .15rem;
      .totalImg{
        .wh(6.5rem,3.6rem);
      }
      .totalWrap{
        position: absolute;
        top: .7rem;
        left: .15rem;
        padding: 0 .5rem;
        color: #fff;
        font-size: .28rem;
        .totalPrice{
          margin-bottom: .1rem;
          margin-top: .1rem;
          font-size: .48rem;
          span{
            font-size: .74rem;
          }
        }
        .detailWrap{
          .flex;
          align-items: center;
          justify-content: space-around;
          .wh(5.5rem,1.2rem);
          text-align: center;
          font-size: .24rem;
          .money{
            font-size: .26rem;
            margin-top: .2rem;
            span{
              font-size: .34rem;
            }
          }
        }
      }
      //.wh(6.2rem,2.8rem);
      /*background: url("~@/assets/image/mine/profit/total.png");*/
      /*background-position: center;*/
      /*background-size: contain;*/
    }
    .today{
      background-color: #fff;
      padding: .3rem .15rem;
      .todayImg{
        .wh(6.5rem,3.6rem);
      }
      .todayWrap{
        .wh(5.5rem,3.6rem);
        position: absolute;
        top: 0.3rem;
        left: .3rem;
        color: #fff;
        font-size: .28rem;
        padding-left: .3rem;
        .flex;
        flex-direction: column;
        justify-content: center;
        .money{
          font-size: .48rem;
          span{
            font-size: .74rem;
          }
        }
      }
    }
    .calenderWrap{
      .flex;
      align-items: center;
      padding: .3rem 0 .2rem .3rem;
      justify-content: space-between;
      background-color: #fff;
      .calenderLeft{
        .flex;
        align-items: center;
        i{
          .wh(.07rem,.3rem);
          background-color: #222;
          border-radius: .04rem;
          margin-right: .24rem;
        }
      }
      .calender{
        position: relative;
        .flex;
        align-items: center;
        .timeWrap{
          .wh(3.77rem,.75rem);
        }
        .timeImg{
          .wh(.55rem,.55rem);
          position: absolute;
          left: .1rem;
          top: .1rem;
        }
        span{
          position: absolute;
          left: 0.8rem;
          top: .25rem;
          color: #fff;
          font-size: .24rem;
        }
      }
    }
    .orderListWrap{
      margin-top: .2rem;
      background-color: #fff;
      .orderList{
        padding: .3rem;
        .title{
          color: #222;
          font-size: .3rem;
        }
        .orderWrap{
          width: 6.9rem;
          margin-top: .24rem;
          color: #757575;
          font-size: .24rem;
          .orderNum{
            .flex;
            align-items: center;
            justify-content: space-between;
            padding: .24rem;
            background-color: #f6f6f6;
            .num{
              color: #222;
            }
            .issue{
              color: #f3714d;
            }
            .issued{
              color: #31abf5;
            }
          }
          p{
            padding: .24rem;
          }
        }
      }
    }
    .noOrder{
      text-align: center;
      margin-top: 2.5rem;
      .goPoster{
        .wh(3rem,.8rem);
        color: #fff;
        font-size: .3rem;
        border-radius: .08rem;
        background-color: #222;
        margin: .5rem auto;
        line-height: .8rem;
      }
    }
  }
</style>
