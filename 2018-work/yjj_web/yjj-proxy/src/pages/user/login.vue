<template>
  <div class="content">
    <div class="yjjLogoWrap"><img src="~@/assets/image/user/yjjLogo.png" alt="" class="yjjLogo"></div>
    <div class="welcomeWrap">
      <img src="~@/assets/image/user/Welcome.png" alt="" class="welcome">
      <p>俞 姐 姐 代 理 商 平 台</p>
    </div>
    <ul class="loginWrap">
      <li class="van-hairline--bottom">
        <p class="regTitle">手机号:</p>
        <input class="input" v-model="phone" type="tel" maxlength="11" @keyup="handleInput" placeholder="请输入正确11位手机号">
      </li>
      <li class="van-hairline--bottom">
        <p class="regTitle">验证码:</p>
        <div class="phoneWrap">
          <input class="input" v-model="sms" type="tel" maxlength="4" @keyup="handleInput" placeholder="请输入验证码">
          <button class="codeBtn" @click="getCode">{{codeText}}</button>
        </div>

      </li>
    </ul>
    <p class="info">手机号码即为俞姐姐APP登录账户</p>
    <button class="loginBtn" @click="login">登录</button>
  </div>
</template>

<script>
import {sendAuthCode, login, isLogin, loginByWeiXin} from '@/api/common'
export default {
  name: 'login',
  data () {
    return {
      codeText: '获取验证码',
      timeS: 60,
      disabled: false,
      phone: '',
      sms: '',
      code: '',
      routeFrom: '',
      refereeUserId: ''
    }
  },
  created () {
    if(this.$route.query.uid || this.$route.query.token) {
      this.uid = this.$route.query.uid
      this.token = this.$route.query.token
    }
    this.code = this.$getUrlPara('code')
    if(this.$route.query.routeFrom) {
      this.routeFrom = this.$route.query.routeFrom
    }
    if(this.$route.query.refereeUserId) {
      this.refereeUserId = this.$route.query.refereeUserId
    }
    this.initLoginStatus()
  },
  methods: {
    getCode () {
      if (this.phone.length === 11 && !isNaN(this.phone) && (/^1(3|4|5|7|8)\d{9}$/i.test(this.phone))) {
        if (this.disabled === false) {
          sendAuthCode({
            phone: this.phone,
            type: 1
          }, res => {
            if (res.code === 200) {
              this.countDown()
              this.disabled = true
            } else {
              this.$toast(res.data)
              this.disabled = false
            }
          })
        }
      } else {
        this.$toast('请填写正确的手机号')
      }
    },
    login () {
      if (this.phone.length === 11 && !isNaN(this.phone) && (/^1(3|4|5|7|8)\d{9}$/i.test(this.phone))) {
        if (this.sms !== '') {
          login({
            'phone': this.phone,
            'authCode': this.sms,
            'refereeUserId': this.refereeUserId
          }, res => {
            if (res.code === 200) {
              var data = {
                openId: res.data.openId,
                openToken: res.data.openToken
              }
              window.localStorage.setItem('data', JSON.stringify(data))
              // window.localStorage.setItem('userRole', res.data.proxyCustomerType)
              console.log(window.localStorage.getItem('data'), '登陆之后获取缓存')
              if(this.routeFrom) {
                this.$router.push({
                  path: './' + this.routeFrom
                })
              }else if(this.refereeUserId){ // 扫码落地
                this.$router.push({
                  path: './qrCode'
                })
              }else{
                this.$router.push({
                  path: './goodsList'
                })
              }
            }else if(res.code === 531){
              this.$dialog.alert({
                message: '由于违规您已被禁用，如有疑问请联系400-118-0099'
              })
            } else {
              this.$toast(res.data)
            }
          })
        } else {
          this.$toast('请填写验证码')
        }
      } else {
        this.$toast('请填写正确的手机号')
      }
    },
    countDown () {
      // this.disabled = true;
      let count = setInterval(() => {
        this.timeS--
        if (this.timeS <= 0) {
          this.disabled = false
          this.codeText = '获取验证码'
          clearInterval(count)
          this.timeS = 60
        } else {
          this.codeText = this.timeS + ' s'
        }
      }, 1000)
    },
    handleInput() {
      this.phone = this.phone.replace(/[^\d]/g,'')
      this.sms = this.sms.replace(/[^\d]/g,'')
    },
    initLoginStatus() {
      isLogin({}, res => {
        if(res.code === 200) {
          var data = {
            userIcon: res.data.wxUserIcon,
            nickName: res.data.wxName
          }
          window.localStorage.setItem('userIcon', JSON.stringify(data))
          if (res.data.isLogin === false) {
            this.wxLogin()
          } else { // 已登录

          }
        }
      })
    },
    wxLogin() {
      loginByWeiXin({
        code: this.code
      }, res=>{
        if(res.code === 200) {
          var data = {
            openId: res.data.openId,
            openToken: res.data.openToken
          }
          window.localStorage.setItem('data', JSON.stringify(data))
        }else if(res.code === 531){
          this.$dialog.alert({
            message: '由于违规您已被禁用，如有疑问请联系400-118-0099'
          })
        }else{
          this.$toast(res.data())
        }
      })
    },
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    .wh(7.5rem,12.06rem);
    background-image: url('~@/assets/image/user/loginBg.png');
    background-position: center;
    background-size: cover;
    .yjjLogoWrap{
      width: 7.5rem;
      position: relative;
      top: 1.2rem;
      .yjjLogo{
        .wh(1.37rem,1.26rem);
        margin: 0 auto;
      }
    }
    .welcomeWrap{
      position: relative;
      top: 1.6rem;
      text-align: center;
      font-size: .28rem;
      color: #fff;
      .welcome{
        .wh(3.27rem,.58rem);
        margin: 0 auto 0.2rem;
      }
    }
    .loginWrap{
      width: 6.54rem;
      position: relative;
      left: .48rem;
      top: 2rem;
      li{
        margin-bottom: .42rem;
      }
      .regTitle{
        font-size: .28rem;
        color: #fff;
        margin-bottom: .3rem;
      }
      .input{
        color: #fff;
        font-size: .26rem;
      }
      .phoneWrap{
        display: flex;
        align-items: baseline;
        justify-content: space-between;
        .codeBtn{
          .wh(1.7rem,.6rem);
          background: #fff;
          color: #222;
          font-size: .22rem;
          line-height: .6rem;
          text-align: center;
          border-radius: .04rem;
        }
      }
      input{
        margin-bottom: .32rem;
        width: 4rem;
      }
      input::-webkit-input-placeholder {
        color: rgba(229,229,229,.8);
        font-size: .22rem;
      }
    }
    .info{
      position: relative;
      top: 3.6rem;
      color: #fff;
      font-size: .24rem;
      text-align: center;
    }
    .loginBtn{
      .wh(6.54rem,.88rem);
      background: #fff;
      font-size: .3rem;
      color: #757575;
      line-height: .88rem;
      border-radius: .08rem;
      position: relative;
      left: .48rem;
      top: 3.8rem;
    }
  }
</style>
