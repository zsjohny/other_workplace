<template>
  <div class="content" v-if='show'>
    <div v-if="proxyCustomerType != 0">
      <div class="personWrap">
        <div class="personInfo" @click="goPersonInfo">
          <img src="~@/assets/image/mine/personalInfo.png" alt="" class="">
          <p class="title">个人资料</p>
        </div>
        <div>
          <img :src="userData.userIcon" alt="" class="userImg">
        </div>
        <div class="personInfo" @click="goPoster">
          <img src="~@/assets/image/mine/createPoster.png" alt="" class="createPoster">
          <p class="title">生成海报</p>
        </div>
        <div class="personInfo" v-if="data.status == 2">
          <img src="~@/assets/image/mine/audit-refuse.png" alt="" class="createPoster">
          <p class="title">审核拒绝</p>
        </div>
        <div class="personInfo" v-if="data.status == 1">
          <img src="~@/assets/image/mine/auditing.png" alt="" class="createPoster">
          <p class="title">审核中</p>
        </div>
      </div>
      <p class="name">{{userData.nickName}}</p>
      <div class="orderWrap">
        <p class="orderCenter van-hairline--bottom">订单中心</p>
        <div class="titleWrap" v-if="proxyCustomerType == 1">
          <div class="personalWrap" @click="goOrder(3)">
            <img src="~@/assets/image/mine/personalOrder.png" alt="" class="personal">
            <span>个人订单</span>
          </div>
          <div class="agentWrap" @click="goOrder(2)">
            <img src="~@/assets/image/mine/agentOrder.png" alt="" class="agent">
            <span>代理订单</span>
          </div>
          <div class="clientWrap" @click="goOrder(1)">
            <img src="~@/assets/image/mine/clientOrder.png" alt="" class="client">
            <span>客户订单</span>
          </div>
        </div>
        <div class="titleWrap1">
          <div class="personalWrap" @click="goOrder(3)">
            <img src="~@/assets/image/mine/personalOrder.png" alt="" class="personal">
            <span>个人订单</span>
          </div>
          <div class="clientWrap" @click="goOrder(1)">
            <img src="~@/assets/image/mine/clientOrder.png" alt="" class="client">
            <span>客户订单</span>
          </div>
        </div>
      </div>
      <div class="profitWrap">
        <div class="profitNum van-hairline--bottom">
          <span>收益金额</span>
          <div class="profitRight" @click="goProfit">
            <span>查看详情</span>
            <img src="~@/assets/image/common/blue-rightArrow.png" alt="" class="rightArrow">
          </div>
        </div>
        <div class="profit">
          <div class="priceWrap">
            <p class="price">{{data.todayReward}}</p>
            <p class="info">今日收入</p>
          </div>
          <div class="priceWrap">
            <p class="price">{{data.waitReward}}</p>
            <p class="info">待发收入</p>
          </div>
        </div>
      </div>
      <div class="agentMWrap">
        <div class="agentNum van-hairline--bottom">
          <span>家族管理</span>
          <div class="agentRight" @click="goAgent">
            <span>查看详情</span>
            <img src="~@/assets/image/common/red-rightArrow.png" alt="" class="rightArrow">
          </div>
        </div>
        <div class="profit" v-if="proxyCustomerType == 1">
          <div class="priceWrap">
            <p class="price">{{data.agentNum}}</p>
            <p class="info">新增代理</p>
          </div>
          <div class="priceWrap">
            <p class="price">{{data.customerNum}}</p>
            <p class="info">新增客户</p>
          </div>
        </div>
        <div class="profit1" v-if="proxyCustomerType == 2">
          <div class="priceWrap">
            <p class="price">{{data.customerNum}}</p>
            <p class="info">新增客户</p>
          </div>
        </div>
      </div>
    </div>
    <div v-if="proxyCustomerType == 0">
      <div class="personWrap">
        <div class="personInfo" @click="goPersonInfo">
          <img src="~@/assets/image/mine/personalInfo.png" alt="" class="">
          <p class="title">个人资料</p>
        </div>
        <div>
          <img :src="userData.userIcon" alt="" class="userImg">
        </div>
        <div class="personInfo" v-if="data.status == 2">
          <img src="~@/assets/image/mine/audit-refuse.png" alt="" class="createPoster">
          <p class="title">审核拒绝</p>
        </div>
        <div class="personInfo" v-if="data.status == 1">
          <img src="~@/assets/image/mine/auditing.png" alt="" class="createPoster">
          <p class="title">审核中</p>
        </div>
      </div>
      <p class="name">{{userData.nickName}}</p>
      <div>
        <van-cell-group>
          <van-cell title="我的订单" is-link to="" @click="goOrder(3)"/>
          <van-cell title="地址管理" is-link to="./addressList" />
        </van-cell-group>
      </div>
      <div class="bottom">
        <van-cell-group>
          <van-cell title="客服电话" value="0571-374834764" />
          <van-cell title="关于我们" is-link  to="./aboutUs"/>
        </van-cell-group>
      </div>
    </div>

    <tabbar :active="active"></tabbar>
  </div>
</template>

<script>
import Tabbar from '@/components/Tabbar/tabbar'
import {getPersonalInfo} from '@/api/user'
import {isLogin} from '@/api/common'
export default {
  name: 'mine',
  data () {
    return {
      proxyCustomerType: null,
      data: '',
      active: 1,
      code: null,
      show: false,
      userData: ''
    }
  },
  components: {
    Tabbar
  },
  created () {
    this.init()
    this.userData = JSON.parse(window.localStorage.getItem('userIcon'))
  },
  beforeRouteEnter(to, from, next) {
    console.log(window.sessionStorage.getItem('code'), 'window.sessionStorage')
    if(window.sessionStorage.getItem('code')){
      var code = JSON.parse(window.sessionStorage.getItem('code'))
    }
    isLogin({}, res=>{
      if(res.data.isLogin === false) {
        next({
          path: './login',
          query: {
            code: code
          }
        })
      }else{
        next()
      }
    })
  },
  methods: {
    init(){
      getPersonalInfo({

      }, res=>{
        if(res.code === 200) {
          this.show = true
          this.proxyCustomerType = res.data.type
          this.data = res.data
        }
      })
    },
    goOrder(val) {
      this.$router.push({
        path: './personalOrder',
        query: {
          type: val
        }
      })
    },
    goAgent() {
      this.$router.push({
        path: './agent',
        query: {
          proxyCustomerType: this.proxyCustomerType
        }
      })
    },
    goProfit() {
      this.$router.push({
        path: './profit'
      })
    },
    goPoster() {
      this.$router.push({
        path: './createPoster'
      })
    },
    goPersonInfo() {
      this.$router.push({
        path: './personalInfo'
      })
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    padding-bottom: 1.8rem;
    .personWrap{
      .wh(7.5rem,3.3rem);
      background-image: url("~@/assets/image/mine/bg.png");
      background-size: cover;
      background-position: center;
      .flex;
      align-items: center;
      justify-content: space-around;
      .personInfo{
        color: #fff;
        font-size: .24rem;
        text-align: center;
        img{
          .wh(.57rem,.57rem);
          margin: 0.3rem auto 0.1rem;
        }
        .title{
          /*margin-top: .2rem;*/
        }
      }
      .userImg{
        .wh(1.34rem,1.34rem);
        border-radius: .7rem;
        border: .04rem solid #f2f2f2;
      }
    }
    .name{
      width: 7.5rem;
      position: absolute;
      top: 2.6rem;
      color: #fff;
      font-size: .32rem;
      text-align: center;
    }
    .orderWrap{
      .wh(7.02rem,2.53rem);
      background: #fff;
      border-radius: .08rem;
      position: relative;
      top: .2rem;
      left: .24rem;
      .orderCenter{
        width: 6.54rem;
        color: #222;
        font-size: .3rem;
        margin-left: .24rem;
        padding: .24rem 0;
      }
      .titleWrap{
        width: 6.25rem;
        height: 1.8rem;
        margin-left: .4rem;
        .flex;
        align-items: center;
        justify-content: space-between;
        color: #757575;
        font-size: .26rem;
        .personalWrap{
          .personal{
            .wh(.48rem,.48rem);
            margin: 0 auto .1rem;
          }
        }
        .agentWrap{
          .agent{
            .wh(.47rem,.49rem);
            margin: 0 auto .1rem;
          }
        }
        .clientWrap{
          .client{
            .wh(.75rem,.48rem);
            margin: 0 auto .1rem;
          }
        }
      }
      .titleWrap1{
        width: 6.25rem;
        height: 1.8rem;
        margin-left: .4rem;
        .flex;
        align-items: center;
        justify-content: space-around;
        color: #757575;
        font-size: .26rem;
        .personalWrap{
          .personal{
            .wh(.48rem,.48rem);
            margin: 0 auto .1rem;
          }
        }
        .clientWrap{
          .client{
            .wh(.75rem,.48rem);
            margin: 0 auto .1rem;
          }
        }
      }
    }
    .profitWrap{
      .wh(7.02rem,2.53rem);
      background: #fff;
      border-radius: .08rem;
      position: relative;
      top: .4rem;
      left: .24rem;
      color: #5184f7;
      .profitNum{
        width: 6.54rem;
        font-size: .3rem;
        margin-left: .24rem;
        padding: .24rem 0;
        .flex;
        align-items: center;
        justify-content: space-between;
        .profitRight{
          .flex;
          align-items: center;
          font-size: .24rem;
          .rightArrow{
            .wh(.16rem,.28rem);
            margin-left: .1rem;
          }
        }
      }
      .profit{
        .wh(5.05rem,1.8rem);
        margin-left: 1rem;
        .flex;
        align-items: center;
        justify-content: space-between;
        font-size: .36rem;
        .priceWrap{
          text-align: center;
        }
        .price{
          font-weight: bold;
          margin-bottom: .1rem;
        }
        .info{
          color: #222;
          font-size: .26rem;
        }
      }
    }
    .agentMWrap{
      .wh(7.02rem,2.53rem);
      background: #fff;
      border-radius: .08rem;
      position: relative;
      top: .6rem;
      left: .24rem;
      color: #f3714d;
      .agentNum{
        width: 6.54rem;
        font-size: .3rem;
        margin-left: .24rem;
        padding: .24rem 0;
        .flex;
        align-items: center;
        justify-content: space-between;
        .agentRight{
          .flex;
          align-items: center;
          font-size: .24rem;
          .rightArrow{
            .wh(.16rem,.28rem);
            margin-left: .1rem;
          }
        }
      }
      .profit{
        .wh(5.05rem,1.8rem);
        margin-left: 1rem;
        .flex;
        align-items: center;
        justify-content: space-between;
        font-size: .36rem;
        .priceWrap{
          text-align: center;
        }
        .price{
          font-weight: bold;
          margin-bottom: .1rem;
        }
        .info{
          color: #222;
          font-size: .26rem;
        }
      }
      .profit1{
        .wh(5.05rem,1.8rem);
        margin-left: 1rem;
        .flex;
        align-items: center;
        justify-content: center;
        font-size: .36rem;
        .priceWrap{
          text-align: center;
        }
        .price{
          font-weight: bold;
          margin-bottom: .1rem;
        }
        .info{
          color: #222;
          font-size: .26rem;
        }
      }
    }
    .bottom{
      margin-top: .2rem;
    }
    .clientsWrap{
      .wh(7.5rem,3.3rem);
      background-image: url("~@/assets/image/mine/bg.png");
      background-size: cover;
      background-position: center;
      .flex;
      align-items: center;
      justify-content: space-between;
      .userWrap{
        .flex;
        align-items: center;
        margin-left: .24rem;
        color: #fff;
        font-size: .32rem;
        .userImg{
          .wh(1.34rem,1.34rem);
          border-radius: .7rem;
          border: .04rem solid #f2f2f2;
          margin-right: .3rem;
        }
      }
      .personalWrap{
        .wh(2.13rem,.83rem);
        background-image: url("~@/assets/image/mine/clientInfo.png");
        background-size: cover;
        background-position: center;
        .flex;
        align-items: center;
        color: #fff;
        .personalInfo{
          .wh(.57rem,.57rem);
          margin-right: .2rem;
        }
      }
    }
  }
</style>
