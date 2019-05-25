<template>
  <div class="detailsPage">
    <Header headerTitle='借款详情' txtColor='#67DECF' />
    <div class="content" v-for="(item , index) in pageList" :key="index">
      <div class="name">
        <div class="left">
          <img :src="item.appIconUrl" alt="">
        </div>
        <div class="right">
          <div class="top">
            <h3>{{item.appName}}</h3>
            <div class="lilv">{{item.dayInterest}}%</div>
          </div>
          <div class="bottom">
            <p class="time">{{item.sucessPayTime}}分钟</p>
            <p>日利率</p>
          </div>
        </div>
      </div>
      <div class="detail">
        <div class="detailChild">
          <p>借款范围</p>
          <p>{{item.creditMinLimit}}-{{item.creditMaxLimit}}¥</p>
        </div>
        <div class="detailChild">
          <p>借款期限</p>
          <p>{{item.creditMinTerm}}-{{item.creditMaxTerm}}日</p>
        </div>
        <div class="detailChild right">
          <p>最快放款</p>
          <p>{{item.sucessPayTime}}分钟</p>
        </div>
      </div>
      <div class="select">
        <div class="left" :class="{active: !isShow}" @click="isShow = !isShow" >借款详情</div>
        <div class="right" :class="{active: isShow}" @click="isShow = !isShow" >借款评论</div>
        <p class="bottomLine" :class="{toRight: isShow}" ></p>
      </div>
      <div class="borrowDetail" v-show="!isShow">
        <div class="borrowDetailtop">
          <div class="left">
            <div class="classtit">
              <div class="tit">金额</div>
              <div class="frame">
                <input onInput="javascript:this.value=this.value.replace(/[^\d]/g,'')" type="number" v-model="money" placeholder="0" >
                <span class="unit">元</span>
              </div>
            </div>
            <div class="monery">应还款：<span>{{sum}}</span>元</div>
          </div>
          <div class="middleLine"></div>
          <div class="right">
            <div class="classtit">
              <div class="tit">时间</div>
              <div class="frame">
                <input oninput="javascript:this.value=this.value.replace(/[^\d]/g,'')" type="number" v-model="day" placeholder="0">
                <span class="unit">天</span>
              </div>
            </div>
            <div class="monery"></div>
          </div>
        </div>
        <ul class="borrowDetailbottom">
          <li>
            <h4>申请条件</h4>
            <div class="borrowAnswer" v-html="item.itemAttach.applyCondition">
              <!-- {{item.itemAttach.applyCondition}} -->
            </div>
          </li>
          <li>
            <h4>所需材料</h4>
            <div class="borrowAnswer" v-html="item.itemAttach.requestMaterial">
              <!-- {{item.itemAttach.requestMaterial}} -->
            </div>
          </li>
          <li>
            <h4>平台说明</h4>
            <div class="borrowAnswer" v-html="item.itemAttach.platformDesc">
              <!-- {{item.itemAttach.platformDesc}} -->
            </div>
          </li>
        </ul>
      </div>
      <ul class="commentDetail" v-show="isShow">
        <li>
          <div class="left">
            平均放款速度
          </div>
          <div class="right">
            评分<span>{{item.itemComment.avgCollection}}</span>
          </div>
          <div class="star">
            <Star :grade="item.itemComment.avgCollection" />
          </div>
        </li>
        <li>
          <div class="left">
            平均难易程度
          </div>
          <div class="right">
            评分<span>{{item.itemComment.avgDifficulty}}</span>
          </div>
          <div class="star">
            <Star :grade="item.itemComment.avgDifficulty" />
          </div>
        </li>
        <li>
          <div class="left">
            平均催收
          </div>
          <div class="right">
            评分<span>{{item.itemComment.avgPayRate}}</span>
          </div>
          <div class="star">
            <Star :grade="item.itemComment.avgPayRate" />
          </div>
        </li>
      </ul>
      <div class="button" @click="goOut(item.appTenantUrl)">立即申请</div>
    </div>
  </div>
</template>

<script>
  import Header from './HeaderBack'
  import Star from '@/components/star.vue'
  import { MessageBox, Toast } from 'mint-ui' 
  export default {
    components: {
      Header,
      Star
    },
    data() {
      return {
        isShow: false,
        pageList: [],
        productId: '',
        money: '',
        day: ''
      }
    },
    watch: {
      money(val) {
        if(val*1 >= this.pageList[0].creditMaxLimit){
          this.money = this.pageList[0].creditMaxLimit
        }
      },
      day(val) {
        if(val*1 >= this.pageList[0].creditMaxTerm){
          this.day = this.pageList[0].creditMaxTerm
        }
      },
      
    },
    computed: {
      sum(){
        return (this.money*this.day*(this.pageList[0].dayInterest)/100 + this.money*1.0).toFixed(2) 
      }
    },
    created() {
      let obj = JSON.parse(this.$route.query.list)
      for( let i in obj.itemAttach ) {
        obj.itemAttach[i] = obj.itemAttach[i].replace(/[\r\n]/g, "<br />");
      }
      this.pageList.push(obj)
      this.productId = this.pageList[0].id
      
    },
    methods: {
      goOut(url){
        if(!sessionStorage.getItem('DCLogin')) {
          Toast({
            message: '请先登录~',
            position: 'bottom',
            duration: 2000
          })
          this.$router.push('/login')
        } else {
          MessageBox({
            title: '温馨提示',
            message: '贷款有风险，借贷需谨慎<br/>若有前期费，支付更谨慎',
            confirmButtonText: '确定',
            closeOnClickModal: false
          }).then(action => {
            this.$post('/v1/credit-shop/uv',{
              uid: sessionStorage.getItem('DCuid'),
              productId: this.productId
            }).then(res => {
              if(res.status === 200){
                window.location.href = url
              }
            }).catch(e => console.log(e))
          })
        }
      }
    }

  }
</script>

<style lang="scss" scoped>
@import '~@/lib/reset.scss';
.detailsPage{
  width: 100%;
  height: 100%;
  padding-top:0.88rem;
  overflow:auto;
  color: #404A4C;
  .content{
    padding: 0 0.1rem 1rem;
    margin-top: 0.1rem;
    position: relative;
    .name{
      @include rect(100% , 1.3rem);
      @include border-radius(0.1rem);
      box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      -webkit-box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      background-color: #FFFFFF;
      padding:0.3rem;
      @include flexbox();
      margin-bottom:0.1rem;
      .left{
        @include rect(0.7rem , 0.7rem);
        img{
          display: block;
          @include rect(100% ,100%);
        }
      }
      .right{
        @include flex();
        width: 100%;
        padding-left:0.3rem;
        .top, .bottom{
          @include rect(100% , 0.3rem);
          @include flexbox();
          @include justify-content(space-between);
          @include line-height(0.3rem);
          h3{
            font-size: 0.3rem;
            font-weight: normal;
          }
          .lilv{
            font-size: 0.2rem;
            color: #FC5C5B;
          }
          .time{
            font-size: 0.2rem;
            color: #858697;
          }
          p{
            font-size: 0.26rem;
          }
        }
        .bottom{
          margin-top:0.12rem;
        }
      }
    }

    .detail{
      @include rect(100% , 1.3rem);
      @include flexbox();
      padding: 0.15rem 0;
      background-color: #FFFFFF;
      @include border-radius(0.1rem);
      box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      -webkit-box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      margin-bottom: 0.1rem;
      .detailChild{
        @include flex();
        @include rect(auto , 100%);
        @include text-align();
        border-right: 1px solid #EBEAEA;
        p{
          font-size: 0.26rem;
          &:nth-child(2){
            margin-top: 0.2rem;
          }
        }
        &.right{
          border: 0;
        }
      }
    }

    .select{
      @include rect(100% ,0.8rem);
      @include flexbox();
      background-color: #FFFFFF;
      @include border-radius(0.1rem);
      box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      -webkit-box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      padding: 0.1rem 0;
      margin-bottom: 0.19rem;
      position: relative;
      >div{
        @include flex();
        @include rect(auto , 100%);
        @include line-height(0.6rem);
        @include font-size(0.3rem);
        @include text-align(); 
      }
      .left{
        border-right: 1px solid #EBEAEA;
      }
      .bottomLine{
        position: absolute;
        @include rect(1rem ,0.05rem);
        @include background-color(#67DECF);
        border-radius: 0.05rem 0.05rem 0 0;
        bottom: 0;
        transition: all 0.2s;
        -webkit-transition: all 0.2s;
        left: 1.33rem;
      }
      .active{
        color: #67DECF;
      }
      .toRight{
        left: 4.97rem;
      }
    }

    .borrowDetail{
      @include rect(100% ,auto);
      background-color: #FFFFFF;
      @include border-radius(0.05rem);
      box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      -webkit-box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      .borrowDetailtop{
        @include rect(100% , 1.17rem);
        padding: 0 0.29rem;
        @include flexbox();
        .left,.right{
          padding-top: 0.23rem;
          .classtit{
            @include rect(100% , 0.35rem);
            @include flexbox();
            @include justify-content(space-between);
            .tit{
              min-width: 0.6rem;
              @include font-size(0.3rem);
              @include line-height(0.35rem)
            }
            .frame{
              @include rect(1.5rem , 0.35rem);
              @include text-align();
              @include font-size(0.2rem);
              border: 1px solid #E7E7E7;
              border-radius: 0.05rem;
              position: relative;
              input{
                @include rect(1.4rem , 0.35rem);
                border: 0;
                text-align: center;
              }
              .unit{
                position: absolute;
                right: 0.06rem;
              }
            }
          }
          .monery{
            @include font-size(0.2rem);
            @include text-color(#858697);
            margin-top: 0.14rem;
          }
        }
        .middleLine{
          @include rect(0.03rem , 0.3rem);
          @include background-color(#67DECF);
          margin:0.26rem 1rem 0;
        }
      }
      .borrowDetailbottom{
        li{
          @include rect(100% , auto);
          border-top: 1px solid #EBEAEA;
          padding: 0.14rem 0 0.14rem 0.49rem;
          background-color: #FFFFFF;
          h4{
            font-size: 0.3rem;
            border-left: 0.03rem solid #67DECF;
            font-weight: normal;
            line-height: 0.3rem;
            margin-bottom: 0.12rem;
            text-indent: 0.18rem;
          }
          .borrowAnswer{
            font-size: 0.2rem;
            padding-left: 0.18rem;
            color: #858697;
          }
        }
      }
    }
    
    .commentDetail{
      @include rect(100% , 2.6rem);
      padding: 0.3rem 1.97rem 0.36rem 0.45rem;
      background: #FFFFFF;
      @include border-radius(0.05rem);
      box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      -webkit-box-shadow: 0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      li{
        @include rect(100% , 0.3rem);
        @include line-height(0.3rem);
        .left{
          float: left;
        }
        .right{
          min-width: 0.8rem;
          float: right;
          font-size: 0.24rem;
          color: #FC5C5B;
          padding-top: 0.04rem;
          margin-left: 0.1rem;
        }
        .star{
          height: 100%;
          padding-top: 0.04rem;
        }
        &:nth-child(2){
          margin: 0.52rem 0;
        }
      }
    }

    .button{
      margin: 0.3rem auto 0;
      @include rect(3.5rem , 0.8rem);
      font-size: 0.36rem;
      color: #FFFFFF;
      background: #67DECF;
      border-radius: 0.1rem;
      box-shadow:0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      -webkit-box-shadow:0.04rem 0.03rem 0.04rem rgba(137,137,137,0.32);
      text-align: center;
      line-height: 0.8rem;
    }
  }

}
</style>
