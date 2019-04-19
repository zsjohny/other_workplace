<template>
  <div class="content">
    <div>
      <img src="~@/assets/image/user/title.png" alt="" class="title">
    </div>
    <div class="regWrap">
      <ul>
        <li class="van-hairline--bottom">
          <p class="regTitle">姓名:</p>
          <input v-model="name" type="text" placeholder="请输入真实姓名">
        </li>
        <li class="van-hairline--bottom">
          <p class="regTitle">手机号:</p>
          <div class="phoneWrap">
            <input v-model="phone" :readonly="readonly" type="tel" maxlength="11" @keyup="handleInput" placeholder="请输入正确11位手机号">
            <button @click="getCode" class="codeBtn">{{codeText}}</button>
          </div>
        </li>
        <li class="van-hairline--bottom">
          <p class="regTitle">验证码:</p>
          <input v-model="sms" type="tel" @keyup="handleInput" maxlength="4" placeholder="请输入验证码">
        </li>
        <li class="van-hairline--bottom">
          <p class="regTitle">所在地区:</p>
          <div class="areaWrap" @click="chooseArea">
            <input v-model="region" type="text" placeholder="请选择所在地区" onfocus="this.blur()">
            <img src="~@/assets/image/user/downArrow.png" alt="" class="downArrow">
          </div>
        </li>
        <li class="van-hairline--bottom">
          <p class="regTitle">详细地址:</p>
          <input v-model="address" class="address" type="textarea"  placeholder="请补充详细地址">
        </li>
        <li class="van-hairline--bottom">
          <p class="regTitle">身份证号:</p>
          <input v-model="idNumber" type="text" maxlength="18" @keyup="handleInput" placeholder="请输入真实身份证号">
        </li>
        <li class="van-hairline--bottom">
          <p class="regTitle">类型:</p>
          <div class="typeWrap" @click="chooseType">
            <input type="text" v-model="agentType" onfocus="this.blur()">
            <img src="~@/assets/image/user/downArrow.png" alt="" class="downArrow">
          </div>
        </li>
      </ul>
    </div>
    <div class="infoWrap">
      <ul>
        <li>1.您的个人身份信息平台不会向任何人透露</li>
        <li>2.请仔细阅读代理协议，以保证您的权益</li>
        <li>3.手机号即为俞姐姐公众号登录账号</li>
        <li>4.手机号即为俞姐姐APP登录账号</li>
      </ul>
    </div>
    <p class="agentProtocol">提交即表示同意 <span><router-link class="protocol" to="./agentProtocol">《代理商协议》</router-link></span></p>
    <button v-if="!auditStatus" class="submit" @click="submit">提交</button>
    <button v-else class="submit">审核中，无法再次申请</button>
    <van-popup v-model="showArea" position="bottom">
      <van-area :area-list="areaList" @cancel="cancelArea" @confirm="confirmArea" v-if="showArea"/>
    </van-popup>
    <van-popup v-model="showType" position="bottom">
      <van-picker show-toolbar :columns="types" @cancel="cancelType" @confirm="confirmType" @change="onChange" />
    </van-popup>
    <van-dialog
      v-model="showRefuseDialog"
      confirm-button-text="再次申请"
      :before-close="beforeClose"
    >
      <div class="refuseWrap">
        <p class="refuseTitle">您的申请已被拒绝</p>
        <p class="refuse">拒绝理由</p>
        <p class="refuseInfo">{{refuseInfo}}
        </p>
      </div>

    </van-dialog>
  </div>
</template>

<script>
import areaList from '@/config/area'
import {sendAuthCode, register, existProxyApply,isLogin, loginByWeiXin} from '@/api/common'
export default {
  name: 'register',
  data () {
    return {
      areaList: areaList,
      showArea: false,
      showType: false,
      types: ['市级代理', '县级代理'],
      codeText: '获取验证码',
      timeS: 60,
      disabled: false,
      name: '',
      phone: '',
      sms: '',
      region: '',
      address: '',
      idNumber: '',
      agentType: '市级代理',
      province: '',
      city: '',
      county: '',
      type: 1,
      refereeUserId: '',
      code: '',
      showRefuseDialog: false, // 拒绝理由
      refuseInfo: null,
      auditStatus: false,
      readonly: false
    }
  },
  created () {
    if(this.$route.query.refereeUserId) {
      this.refereeUserId = this.$route.query.refereeUserId
    }
    this.code = this.$getUrlPara('code')
    if (this.$route.query.code) {
      this.code = this.$route.query.code
    }
    this.init()
    this.initLoginStatus()
  },
  methods: {
    init() {
      existProxyApply({}, res=>{
        console.log(res)
        if(res.code === 200) {
          if(res.data.hasUnAuditProxy === 1) { // 有申请
            if(res.data.auditStatus === 1){ // 审核中
              this.auditStatus = true
              this.$dialog.alert({
                message: '您的代理申请正在审核中，审核通过后您将拥有生成海报功能，预计审核时间48小时'
              })
            }else{ // 拒绝
              this.showRefuseDialog = true
              this.refuseInfo = res.data.auditMsg
              this.name = res.data.audit.name
              this.phone = res.data.audit.phone
              this.sms = res.data.audit.authCode
              this.province = res.data.audit.province
              this.city = res.data.audit.city
              this.county = res.data.audit.county
              this.address = res.data.audit.addressDetail
              this.idNumber = res.data.audit.idCardNo
              this.type = res.data.audit.type
              this.region = this.province + '/' + this.city + '/' + this.county
            }
          }
        }


      })
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
          if(res.data.phone) {
            this.phone = res.data.phone
            this.readonly = true
          }
        }else{
          this.$toast(res.data)
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
          this.$toast(res.data)
        }
      })
    },
    getCode () {
      if (this.phone.length === 11 && !isNaN(this.phone) && (/^1(3|4|5|7|8)\d{9}$/i.test(this.phone))) {
        if (this.disabled === false) {
          sendAuthCode({
            phone: this.phone,
            type: 2
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
    submit () {
      if(this.name != '' && this.phone != '' && this.sms != '' && this.province != '' && this.city != '' && this.county != '' && this.address != '' && this.idNumber != ''){
        register({
          name: this.name,
          phone: this.phone,
          authCode: this.sms,
          province: this.province,
          city: this.city,
          county: this.county,
          addressDetail: this.address,
          idCardNo: this.idNumber,
          type: this.type,
          refereeUserId: this.refereeUserId,
          jsCode: this.code
        }, res => {
          if (res.code === 200) {
            window.localStorage.setItem('userRole', res.data.proxyCustomerType)
            if(this.refereeUserId){ // 扫码落地
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
      }else{
        this.$toast('请将信息填写完整')
      }
    },
    chooseArea () {
      this.showArea = !this.showArea
    },
    cancelArea () {
      this.showArea = !this.showArea
    },
    confirmArea (data) { // 获取所选省市区
      console.log(data)
      this.showArea = !this.showArea
      this.province = data[0].name
      this.city = data[1].name
      this.county = data[2].name
      this.region = this.province + '/' + this.city + '/' + this.county
    },
    chooseType () {
      this.showType = !this.showType
    },
    onChange (picker, value, index) {

    },
    cancelType (value, index) {
      console.log(value, index)
      this.showType = !this.showType
    },
    confirmType (value, index) {
      console.log(value, index)
      this.showType = !this.showType
      this.agentType = value
      this.type = index + 1
    },
    beforeClose(action, done) {
      if (action === 'confirm') {
        setTimeout(done, 500);
      } else {
        done();
      }
    },
    handleInput() {
      this.phone = this.phone.replace(/[^\d]/g,'')
      this.sms = this.sms.replace(/[^\d]/g,'')
      this.idNumber = this.idNumber.replace(/([^0-9Xx])+/g, '')
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    .wh(7.5rem,17.21rem);
    background-image: url('~@/assets/image/user/regbg.png');
    background-position: center;
    background-size: cover;
    .title{
      .wh(6.26rem,.42rem);
      position: relative;
      top: 1rem;
      left: .62rem;
    }
    .regWrap{
      .wh(7.02rem,10.33rem);
      background: rgba(255,255,255,0.5);
      border-radius: .08rem;
      position: relative;
      left: .24rem;
      top: 1.62rem;
      ul{
        width: 6.54rem;
        padding: 0.1rem .24rem 0.2rem .24rem;
        li{
          margin-top: .2rem;
          input{
            width: 4.5rem;
            color: #fff;
            font-size: .26rem;
          }
        }
        .regTitle{
          font-size: .28rem;
          color: #fff;
          margin-bottom: .19rem;
        }
        .address{
          width: 6.5rem;
          padding: .1rem 0;
        }
        .phoneWrap{
          display: flex;
          align-items: baseline;
          justify-content: space-between;
          .codeBtn{
            .wh(1.5rem,.42rem);
            background: #fff;
            color: #222;
            font-size: .22rem;
            line-height: .42rem;
            text-align: center;
            border-radius: .04rem;
          }
        }
        .areaWrap{
          display: flex;
          align-items: baseline;
          justify-content: space-between;
          .downArrow{
            .wh(.3rem,.16rem);
          }
        }
        .typeWrap{
          display: flex;
          align-items: baseline;
          justify-content: space-between;
          .downArrow{
            .wh(.3rem,.16rem);
          }
        }
        input{
          margin-bottom: .2rem;
        }
        input::-webkit-input-placeholder {
            color: rgba(229,229,229,.8);
            font-size: .22rem;
        }
      }
    }
    .infoWrap{
      .wh(7.02rem,2.45rem);
      background: rgba(255,255,255,0.5);
      border-radius: .08rem;
      position: relative;
      left: .24rem;
      top: 1.82rem;
      color: #fff;
      font-size: .24rem;
      ul{
        padding: .15rem .24rem;
        li{
          padding-bottom: .25rem;
        }
      }
    }
    .agentProtocol{
      font-size: .24rem;
      color: #f2f2f2;
      position: relative;
      top: 2.3rem;
      text-align: center;
      .protocol{
        color: #fff;
      }
    }
    .submit{
      .wh(7.02rem,.88rem);
      line-height: .88rem;
      background: #fff;
      font-size: .3rem;
      color: #757575;
      text-align: center;
      position: relative;
      left: .24rem;
      top: 2.5rem;
      border-radius: .08rem;
    }
    .areaList{
      position: absolute;
      left: 0;
      bottom: 0;
    }
    .refuseWrap{
      padding: .3rem;
      text-align: center;
      .refuseTitle{
        margin-bottom: .2rem;
      }
      .refuse{
        text-align: left;
        font-size: .3rem;
      }
      .refuseInfo{
        text-align: left;
        font-size: .25rem;
        margin-top: .1rem;
        background-color: #f6f6f6;
        border-radius: .1rem;
        padding: .1rem;
      }
    }
  }
</style>
