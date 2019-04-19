<template>
  <div>
    <div class="content" v-if="!showPoster">
      <div class="logoWrap"><img :src="userData.userIcon" alt="" class="logo"></div>
      <div class="posterInfo">
        <p class="name">{{userData.nickName}}</p>
        <img src="~@/assets/image/mine/poster/title.png" alt="" class="title">
        <img src="~@/assets/image/common/logo.png" alt="" class="miniCode">
        <div class="description">
          <p>说明:</p>
          <p>点击“生成海报”按钮，即可生成附有你二维码的邀请海报。可将海报发至朋友圈，微信群等社交平台邀请好友成为你的代理或客户</p>
        </div>
      </div>
      <div class="btnWrap">
        <button class="inviteClient" @click="invite(1)">邀请客户</button>
        <button v-if='type == 1' class="inviteAgent" @click="invite(2)">邀请代理</button>
      </div>
      <div class="loadings" v-if="loading">
        <van-loading type="spinner" color="white" />
      </div>
    </div>
    <img v-show="showImg" src="" alt="" id="userIcon">
    <img v-show="showImg" src="" alt="" id="qrCode">
    <canvas v-show="showcanvas" id="myCanvas" width="750" height="1206" class="canvas"></canvas>
    <img v-show="showCreatePoster" crossOrigin ="anonymous" src="" alt="" id="posterImg">
  </div>
</template>

<script>
import {posterQrcode} from '@/api/poster'
import {getPersonalInfo} from '@/api/user'
export default {
  name: 'createPoster',
  data () {
    return {
      showPoster: false,
      showCreatePoster: true,
      showcanvas: false,
      showImg: false,
      loading: false,
      qrCode: '',
      data: {},
      type: null,
      userData: {},
      posterBg: require('@/assets/image/mine/poster/posterBg.jpg'),
      posterModal: require('@/assets/image/mine/poster/modal1.png'),
      userIcon: "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIqLVWicHKwuBjkIrWqxibjVwrm5x5IVxxibbPD1fjicu8x8K1amYU6zkBrQ4f1keiavd9ibXHStQIYZjww/132",
      qrCodes: "https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/inviteLogin383166t1541396445689.jpg",
    }
  },
  created () {
    this.init()
    this.userData = JSON.parse(window.localStorage.getItem('userIcon'))

  },
  mounted(){
    // this.loading = true
    // this.draw()
  },
  methods: {
    init() {
      getPersonalInfo({

      }, res=>{
        if(res.code === 200) {
          this.data = res.data
          this.type = res.data.type
        }
      })
    },
    invite (val) {
      this.loading = true
      // this.showcanvas = true
      posterQrcode({
        type: val
      }, res => {
        if (res.code === 200) {
          // this.showCreatePoster = true
          this.qrCode = res.data
          this.showPoster = true
          this.draw()
        } else {
          this.$toast(res.data)
          this.loading = false
          this.showcanvas = false
        }
      })
    },
    hide () {
      this.showPoster = !this.showPoster
    },
    draw() {
      var c = document.getElementById('myCanvas')
      var ctx = c.getContext('2d')
      var img = new Image()
      img.setAttribute("crossOrigin",'Anonymous')
      img.src = this.posterBg
      img.onload = function() {
        ctx.drawImage(img,0,0,750,1206);
      }
      setTimeout(()=>{
        ctx.beginPath()
        ctx.textAlign = 'center'
        ctx.font = '30px PingFangSC-Medium'
        ctx.fillStyle = '#222'
        ctx.fillText(this.userData.nickName,375,260)
        // ctx.fillText('Little Imp',375,260)
        ctx.closePath()
      },200)
      setTimeout(()=>{
        var userIcon = document.getElementById('userIcon')
        userIcon.setAttribute("crossOrigin",'Anonymous')
        userIcon.src = this.userData.userIcon+"?timeStamp="+ new Date()
        // userIcon.src = this.userIcon
        userIcon.onload = function(){
          ctx.save()
          ctx.arc(375,100,67,0,2*Math.PI,true)
          ctx.clip()
          ctx.drawImage(userIcon,308,30,135,135)
          ctx.restore()
        }

      },500)
      setTimeout(()=>{
        var qrCode = document.getElementById('qrCode')
        qrCode.setAttribute("crossOrigin",'Anonymous')
        qrCode.src = this.qrCode+"?timeStamp="+ new Date()
        // qrCode.src = this.qrCodes
        qrCode.onload = function(){
          console.log(qrCode,'qrCode..........加载完成')
          ctx.drawImage(qrCode,235,480,280,280)
        }
      },700)
      setTimeout(()=>{
       this.createPosterImg(c)
        this.showcanvas = false
      },1000)
    },
    createPosterImg(canvas) {
      document.getElementById("posterImg").src =  canvas.toDataURL("image/jpg");
      this.loading = false
      this.showcanvas = false
    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    .wh(7.5rem,13.72rem);
    background: url("~@/assets/image/mine/poster/createPosBg.png");
    background-position: center;
    background-size: cover;
    .createPosBg{}
    .logoWrap{
      .logo{
        .wh(1.34rem,1.34rem);
        position: absolute;
        border-radius: 100px;
        top: .3rem;
        left: 3.08rem;
      }
    }
    .posterInfo{
      .wh(6rem,9.74rem);
      background: url("~@/assets/image/mine/poster/modal.png");
      background-position: center;
      background-size: cover;
      position: absolute;
      top: 1rem;
      left: .75rem;
      text-align: center;
      .name{
        font-size: .3rem;
        color: #222;
        margin-top: 1.2rem;
      }
      .title{
        .wh(4.47rem,.87rem);
        margin: .7rem auto 0;
      }
      .miniCode{
        .wh(1.8rem,1.8rem);
        margin: .9rem auto 0;
      }
      .info{
        color: #848484;
        font-size: .26rem;
        margin-top: .4rem;
      }
      .description{
        margin-top: 1rem;
        padding: 0 .65rem;
        p:first-child{
          text-align: left;
          margin-bottom: .1rem;
        }
        color: #222;
        font-size: .24rem;
      }
    }
    .btnWrap{
      position: absolute;
      top: 11rem;
      left: .75rem;
      .inviteClient{
        .wh(6rem,1rem);
        background-color: #222;
        color: #f6f6f6;
        font-size: .3rem;
        text-align: center;
        line-height: 1rem;
        border-radius: .08rem;
      }
      .inviteAgent{
        .inviteClient();
        background-color: #999;
        margin-top: .2rem;
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
  .getPoster{
    .wh(7.5rem,12.06rem);
    background: url("~@/assets/image/mine/poster/posterBg.png");
    background-position: center;
    background-size: cover;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 9999;
    .logoWrap{
      .logo{
        .wh(1.34rem,1.34rem);
        position: absolute;
        border-radius: 100px;
        top: .3rem;
        left: 3.08rem;
      }
    }
    .posterInfo{
      .wh(6rem,9.74rem);
      background: url("~@/assets/image/mine/poster/modal1.png");
      background-position: center;
      background-size: cover;
      position: absolute;
      top: 1rem;
      left: .75rem;
      text-align: center;
      .name{
        font-size: .3rem;
        color: #222;
        margin-top: 1.2rem;
      }
      .title{
        .wh(4.47rem,.87rem);
        margin: .7rem auto 0;
      }
      .miniCode{
        .wh(2.6rem,2.6rem);
        margin: 1.6rem auto 0;
      }
      .info{
        color: #848484;
        font-size: .26rem;
        margin-top: 1rem;
      }
    }
  }
  .canvas{
    position: absolute;
    top: 0px;
    width:375px;
    height:603px;
  }
  #posterImg{

  }
</style>
