<template>
  <div class="login">
    <div class="logo">登录</div>
    <div class="form">
      <div class="formItem">
        <div class="formLabel">
          <span>手机号码</span>
          <span class="errorTip">{{ phoneNumErrorTip }}</span>
        </div>
        <div class="inputBox">
          <input 
            oninput="javascript:this.value=this.value.replace(/[^\d]/g,'')" 
            class="formInput" 
            v-model="phoneNum" 
            type="tel" 
            maxlength="11" 
            placeholder="请输入手机号码" >
        </div>
      </div>
      <div class="formItem">
        <div class="formLabel">
          <span>验证码</span>
        </div>
        <div class="inputBox">
          <input 
            oninput="javascript:this.value=this.value.replace(/[^\d]/g,'')" 
            class="formInput" 
            v-model="vCode" 
            type="tel" 
            maxlength="6" 
            placeholder="请输入验证码" >
          <button 
            @click="getCode" 
            class="formBtn" 
            :class="{ lightBtn: phoneNumStatus }" 
            :disabled="!vCodeBtnStatus" >
            {{vCodeTxt}}
          </button>
        </div>
      </div>
      <div class="loginBtn">
        <Button @click="verigyLogin" type="primary" :disabled="!phoneNumStatus" size="large">立即登录</Button>
      </div>
      <!-- <div class="bottom">
        <div class="checkRadio" :class="{checkRadioDisable : !checkedStatus }" @click="checkedStatus = !checkedStatus" ></div>
        <div class="protocols">
          <div>注册即表示同意</div>
          <p>
            <router-link to="/implicitAgreement">《用户注册协议》</router-link>
            <router-link to="/privacyAgreement">《用户隐私协议》</router-link>
          </p>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
  import { Button , Toast } from 'mint-ui';
  export default {
    components: {
      Button,
    },
    data() {
      return {
        phoneNum:'',
        phoneNumErrorTip: '',
        phoneNumStatus: false,
        vCode: '',
        vCodeTxt:'获取验证码',
        vCodeBtnStatus: false,
        // checkedStatus: true
      }
    },
    watch: {
      phoneNum: function (val) {
        if(val.length == 11 ) {
          if( this.doregEXP(val , this.regEXP.phone_reg) ) {
            this.phoneNumStatus = true
            this.vCodeBtnStatus = true
          } else {
            this.phoneNumErrorTip = '请输入正确的手机号'
            this.phoneNumStatus = false
            this.vCodeBtnStatus = true
          }
        } else {
          this.phoneNumErrorTip = ''
          this.phoneNumStatus = false
          this.vCodeBtnStatus = true
        }
      },
    },
    methods: {
      getCode: function() {
        const _this = this
        this.vCodeBtnStatus = false
        this.$post('/v1/sms/' + this.phoneNum,{
          optType: '1'
        }).then(res => {
          if(res.status === 200){
            let curSeconds = 60
            let sendvCode = window.setInterval(function() {
              _this.vCodeTxt = curSeconds + '秒后重发'
              if(curSeconds <= 0) {
                window.clearInterval(sendvCode)
                _this.vCodeTxt = '重新获取'
                _this.vCodeBtnStatus = true
              }
              curSeconds--
            }, 1000)
            Toast({
              message: '短信验证码发送成功~',
              position: 'bottom',
              duration: 2000
            })
          }
        }).catch(e => console.log(e))
      },
      verigyLogin: function() {
        if(this.vCode === ''){
          Toast({
            message: '请输入验证码',
            position: 'bottom',
            duration: 2000
          })
        } else {
          this.$post('/v1/login/sms-quick', {
            mobile: this.phoneNum,
            smscode: this.vCode
          }).then(res => {
            if(res.status === 200){
              Toast({
                message: '登录成功',
                position: 'bottom',
                duration: 2000
              })
              sessionStorage.setItem('DCLogin', '1')
              sessionStorage.setItem('DCuid', res.data.data.uid)
              this.$router.go(-1)
            }
          }).catch(e => console.log(e))
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
@import '~@/lib/reset.scss';
::-webkit-input-placeholder{
  color: #858697;
}
input:-webkit-autofill {
  -webkit-box-shadow: 0 0 0px 1000px white inset !important;
}
.login{
  @include rect(100% , 100%);
  background-color: #FFFFFF;
  .logo{
    @include rect(100% , 4.9rem);
    @include text-align();
    @include font-size(0.8rem);
    @include text-color(#67DECF);
    padding-top: 1.58rem;
    font-weight: 300;
  }
  .form{
    @include rect(100% , auto);
    padding: 0 0.5rem;
    .formItem{
      @include rect(100% ,0.86rem);
      margin-bottom: 0.2rem;
      border-bottom: 1px solid #858697;
      .formLabel{
        @include rect(100% , 0.27rem);
        span{
          @include font-size(0.24rem);
          @include text-color(#858697);
          @include line-height(0.27rem);
        }
        .errorTip{
          @include font-size(0.16rem);
          @include text-color(#FC5C5B);
          padding-left: 0.15rem;
        }
      }
      .inputBox{
        @include rect(100% , 0.48rem);
        margin-top:0.08rem;
        @include flexbox();
        .formInput{
          @include flex();
          width: auto;
          border:0;
          @include font-size(0.3rem);
          @include text-color(#858697);
          font-weight: 400;
        }
        .formBtn{
          border:0;
          background: rgba(255,255,255,0);
          @include text-color(#858697);
          @include font-size(0.3rem);
        }
        .lightBtn{
          @include text-color(#67DECF);
        }
      }
    }
    .loginBtn{
      @include rect(2.5rem , 0.7rem);
      @include font-size(0.36rem);
      @include text-color(#FFFFFF);
      margin: 0.6rem auto 0.4rem;
      .mint-button{
        height: 0.7rem;
        @include gradient( linear, right, #85F5BA , #67DECF);
        border-radius: 0.35rem;
      }
    }
    // .bottom{
    //   @include flexbox();
    //   @include rect(100% , 0.18rem);
    //   @include justify-content(center);
    //   @include align-items(center);
    //   .checkRadio{
    //     @include rect(0.18rem , 100%);
    //     background: url('~@/assets/agree.png') 0 0 no-repeat;
    //     background-size: 100%;
    //   }
    //   .checkRadioDisable{
    //     background-image: url('~@/assets/disagree.png');
    //   }
    //   .protocols{
    //     margin-left:0.15rem;
    //     @include font-size(0.18rem);
    //     @include line-height(0.44rem);
    //     div{
    //       float: left;
    //     }
    //     p{
    //       float: left;
    //       a{
    //         @include text-color(#68DFCE);
    //       }
    //     }
        
    //   }
    // }
  }
}
</style>
