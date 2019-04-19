<template>
  <div class="content">
    <van-cell-group>
      <van-cell title="姓名" :value=name />
      <van-cell v-if="type > 0" title="身份证号" :value=idNumber />
      <van-cell v-if="type == 0" title="手机号" :value=phone />
    </van-cell-group>
    <div class="bottom" v-if="type > 0">
      <van-cell-group>
        <van-cell title="地址管理" is-link to="./addressList" />
        <van-cell title="客服电话" value="0571-374834764" />
        <van-cell title="关于我们" is-link  to="./aboutUs"/>
      </van-cell-group>
    </div>
    <div class="logout" @click="logOut">退出登录</div>
  </div>
</template>

<script>
import {loginOut} from '@/api/common'
import {getPersonalInfo} from '@/api/user'
export default {
  name: 'personalInfo',
  data () {
    return {
      idNumber: '',
      name: '',
      type: '',
      phone: ''
    }
  },
  created () {
    this.init()
  },
  methods: {
    init(){
      getPersonalInfo({},res=>{
        if(res.code === 200){
          if(res.data.idCardNo) {
            this.idNumber = res.data.idCardNo
          }
          if(res.data.phone) {
            this.phone = res.data.phone
          }
          this.name = res.data.name
          this.type = res.data.type
        }
      })
    },
    logOut () {
      loginOut({}, res => {
        if (res.code === 200) {
          this.$router.push({
            path: './login'
          })
        }
      })
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    .bottom{
      margin-top: .2rem;
    }
    .logout{
      .wh(7.02rem,1rem);
      background-color: #222;
      color: #fff;
      text-align: center;
      line-height: 1rem;
      border-radius: .08rem;
      position: absolute;
      left: .24rem;
      bottom: 1rem;
    }
  }
</style>
