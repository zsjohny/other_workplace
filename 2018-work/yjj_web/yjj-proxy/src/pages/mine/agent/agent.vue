<template>
  <div class="content">
    <van-tabs v-model="active" sticky @click="change">
      <van-tab title="代理管理" v-if="proxyCustomerType == 1">
        <div class="agentWrap">
          <div>
            <p>今日新增</p>
            <p class="num">{{todayCreateCount}}</p>
          </div>
          <div>
            <p>总代理</p>
            <p class="num">{{total}}</p>
          </div>
        </div>
        <div class="searchWrap">
          <img src="~@/assets/image/mine/profit/search.png" alt="" class="search">
          <input type="text" v-model="phoneOrName" placeholder="请输入用户昵称/姓名/手机号" @blur="blur()">
        </div>
        <van-list
          v-model="loading"
          :finished="finished"
          :immediate-check="false"
          @load="onLoadCityAgent"
        >
          <div class="userWrap" v-for="(item, index) in agents" :key="index">
            <img v-if="item.wxUserIcon" :src="item.wxUserIcon" alt="" class="userImg">
            <img v-else src="~@/assets/image/common/logo.png" alt="" class="userImg">
            <div class="userInfoWrap van-hairline--bottom">
              <p class="userInfoT"><span>{{item.wxName}}</span><span class="time">{{item.createTimeReadable}}</span></p>
              <div class="userInfoB">
                <p><span>{{item.userName}}/</span><span>{{item.phone}}</span></p>
                <span class="untie" @click="unbind(item)">解绑</span>
              </div>
            </div>
          </div>
        </van-list>
        <div v-if="agents.length == 0" class="noOrder">
          <p>你还没有代理哦, 快去发展代理吧</p>
          <p class="goPoster" @click="goPoster">发展代理</p>
        </div>
      </van-tab>
      <van-tab title="客户管理">
        <div class="agentWrap">
          <div>
            <p>今日新增</p>
            <p class="num">{{todayCreateCount}}</p>
          </div>
          <div>
            <p>总客户</p>
            <p class="num">{{total}}</p>
          </div>
        </div>
        <div class="searchWrap">
          <img src="~@/assets/image/mine/profit/search.png" alt="" class="search">
          <input type="text" placeholder="请输入用户昵称/姓名/手机号" v-model="phoneOrName" @blur="blur">
        </div>
        <van-list
          v-model="loading"
          :finished="finished"
          :immediate-check="false"
          @load="onLoadCityAgent"
        >
          <div class="userWrap" v-for="(item, index) in agents" :key="index">
            <img v-if="item.wxUserIcon" :src="item.wxUserIcon" alt="" class="userImg">
            <img v-else src="~@/assets/image/common/logo.png" alt="" class="userImg">
            <div class="userInfoWrap van-hairline--bottom">
              <p class="userInfoT"><span>{{item.wxName}}</span></p>
              <p class="userInfoB"><span>{{item.createTimeReadable}}</span></p>
            </div>
            <div class="untieBtn" @click="unbind(item)">解绑</div>
          </div>
        </van-list>
        <div v-if="agents.length == 0" class="noOrder">
          <p>你还没有客户哦, 快去发展客户吧</p>
          <p class="goPoster" @click="goPoster">发展客户</p>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
import {myProxyCustomer, unbindCounty} from '@/api/agent'
export default {
  name: 'agent',
  data () {
    return {
      active: 0,
      agents: [
        // {url: require('@/assets/image/mine/poster/yjj-logo.png')},
        // {url: require('@/assets/image/mine/poster/yjj-logo.png')},
        // {url: require('@/assets/image/mine/poster/yjj-logo.png')}
      ],
      phoneOrName: '',
      type: 2,
      pageSize: 10,
      pageNumber: 1,
      loading: false,
      finished: false,
      check: false,
      todayCreateCount: '', // 代理新增
      total: '', // 代理总数
      proxyCustomerType: null
    }
  },
  created () {
    if(this.$route.query.proxyCustomerType) {
      this.proxyCustomerType = this.$route.query.proxyCustomerType
      if(this.proxyCustomerType == 2) {
        this.type = 1
      }
    }
    this.init()
  },
  methods: {
    init () {
      myProxyCustomer({
        phoneOrName: this.phoneOrName,
        type: this.type,
        pageSize: this.pageSize,
        pageNumber: this.pageNumber
      }, res => {
        if (res.code === 200) {
          this.loading = false
          this.todayCreateCount = res.data.todayCreateCount
          this.total = res.data.userList.total
          if (this.pageNumber === 1) {
            this.agents = res.data.userList.list
          } else {
            res.data.userList.list.forEach((element) => {
              this.agents.push(element)
            })
          }
          if (res.data.userList.hasNextPage === false) {
            this.finished = true
          }
        }
      })
    },
    change (index, title) {
      this.type = 2 - index
      this.pageNumber = 1
      this.loading = false
      this.finished = true
      console.log(index, title, this.type)
      this.init()
    },
    onLoadCityAgent () {
      // 异步更新数据
      console.log('././././')
      setTimeout(() => {
        this.pageNumber += 1
        this.init()
      }, 500)
    },
    blur () {
      this.pageNumber = 1
      this.init()
    },
    unbind (item) {
      this.$dialog.confirm({
        title: '确定解绑吗？'
      }).then(()=>{
        unbindCounty({
          refereeId: item.refereeId
        }, res => {
          if (res.code === 200) {
            this.pageNumber = 1
            this.init()
          }
        })
      })
      console.log(item)
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
    .agentWrap{
      .wh(7.02rem,3.22rem);
      background: url("~@/assets/image/mine/profit/total.png");
      background-position: center;
      background-size: cover;
      position: relative;
      top: .3rem;
      left: .24rem;
      .flex;
      align-items: center;
      justify-content: space-around;
      color: #fff;
      font-size: .28rem;
      .num{
        font-size: .32rem;
        margin-top: .3rem;
        font-weight: bold;
      }
    }
    .searchWrap{
      .wh(6.55rem,.9rem);
      background-color: #fff;
      border-radius: .14rem;
      position: relative;
      top: -.45rem;
      left: .475rem;
      .flex;
      align-items: center;
      .search{
        .wh(.4rem,.4rem);
        padding: 0 .24rem;
      }
      input{
        width: 5rem;
      }
    }
    .userWrap{
      .flex;
      align-items: center;
      background-color: #fff;
      padding: .4rem .24rem 0 .24rem;
      .userImg{
        .wh(1rem,1rem);
        margin-right: .24rem;
        padding-bottom: .4rem;
      }
      .userInfoWrap{
        width: 5.7rem;
        padding-bottom: .4rem;
        .userInfoT{
          color: #222;
          font-size: .28rem;
          margin-bottom: .2rem;
          .flex;
          justify-content: space-between;
          .time{
            color: #757575;
            font-size: .26rem;
          }
        }
        .userInfoB{
          color: #757575;
          font-size: .26rem;
          .flex;
          justify-content: space-between;
          .untie{
            color: #ff2742;
          }
        }
      }
      .untieBtn{
        .wh(1.66rem,.66rem);
        text-align: center;
        background-color: #ff2742;
        font-size: .26rem;
        color: #fff;
        line-height: .66rem;
        border-top-left-radius: .33rem;
        border-bottom-left-radius: .33rem;
        position: absolute;
        right: 0;
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
