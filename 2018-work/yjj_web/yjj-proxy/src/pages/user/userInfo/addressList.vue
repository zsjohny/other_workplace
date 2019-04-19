<template>
  <div class="content">
    <div class="addressList" v-for="(item,index) in addressList" :key="index">
      <div class="addressWrap van-hairline--bottom">
        <div class="leftWrap">
          <p class="leftTWrap"><span>{{item.receiverName}}</span><span class="phone">{{item.receiverPhone}}</span></p>
          <p class="leftBWrap">{{item.province}}{{item.city}}{{item.county}}{{item.receiverAddress}}</p>
        </div>
        <img src="~@/assets/image/common/rightArrow.png" alt="" class="rightArrow">
      </div>
      <div class="setWrap">
        <div class="setLeft" @click="setDefault(item)">
          <img src="~@/assets/image/user/userInfo/circled.png" alt="" class="circle" v-if="item.isDefault == 1">
          <img src="~@/assets/image/user/userInfo/circle.png" alt="" class="circle" v-if="item.isDefault == 0">
          <span>设为默认地址</span>
        </div>
        <div class="setRight" @click="addAddress(item)">
          <img src="~@/assets/image/user/userInfo/refact.png" alt="" class="refact">
          <span>编辑</span>
        </div>
      </div>
    </div>
    <div class="addBtn" @click="addAddress(0)">添加地址</div>
  </div>
</template>

<script>
  import {getAddressList , editAddressDefault} from '@/api/user'
export default {
  name: 'addressList',
  data () {
    return {
      addressList: [],
      routerFrom: '',
      id: ''
    }
  },
  created () {
    if(this.$route.query.routerFrom) {
      this.routerFrom = this.$route.query.routerFrom
    }
    if(this.$route.query.id) {
      this.id = this.$route.query.id
    }
    this.init()
  },
  // beforeRouteLeave(to,from,next){
    // console.log('离开页面之前', to, from)
    // if(this.routerFrom === 'add_audit'){
    //   // next('./login')
    //   // console.log('../././././././.')
    // }else{
    //   next()
    // }
  // },
  methods: {
    init(){
      getAddressList({}, res=>{
        if(res.code === 200) {
          this.addressList = res.data
        }
      })
    },
    addAddress(val){
      this.$router.push({
        path: './add-audit-address',
        query: {
          data: val,
        }
      })
    },
    setDefault(item) {
      this.addressList.forEach((element) => {
        if(element.id == item.id) {
          if(item.isDefault == 0) {
            element.isDefault = 1
            editAddressDefault({
              id: item.id,
              isDefault: 1,
            }, res=>{
              if(res.code === 200) {
                this.$toast('设置成功')
                if(this.routeFrom === 'payOrder') {
                  this.$router.push({
                    path: '/payOrder',
                    query: {
                      id: this.goodsId
                    }
                  })
                }
              }
            })
          }
        }else{
          element.isDefault = 0
        }
      })

    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    padding-bottom: .3rem;
    .addressList{
      background-color: #fff;
      padding: .3rem .24rem;
      margin-bottom: .2rem;
      .addressWrap{
        width: 7.02rem;
        .flex;
        align-items: center;
        justify-content: space-between;
        padding-bottom: .25rem;
        .leftWrap{
          width: 6.2rem;
          .leftTWrap{
            color: #222;
            font-size: .3rem;
            .flex;
            justify-content: space-between;
            .phone{
              color: #333;
              font-size: .26rem;
            }
          }
          .leftBWrap{
            color: #848484;
            font-size: .24rem;
            margin-top: .24rem;
          }
        }
        .rightArrow{
          .wh(.44rem,.44rem);
        }
      }
      .setWrap{
        .flex;
        align-items: center;
        justify-content: space-between;
        padding-top: .25rem;
        .setLeft{
          .flex;
          align-items: center;
          .circle{
            .wh(.44rem,.44rem);
            margin-right: .2rem;
          }
        }
        .setRight{
          .flex;
          align-items: center;
          .refact{
            .wh(.36rem,.35rem);
            margin-right: .2rem;
          }
        }
      }
    }
    .addBtn{
      .wh(7.02rem,.88rem);
      line-height: .88rem;
      background-color: #222;
      border-radius: .08rem;
      text-align: center;
      color: #fff;
      font-size: .3rem;
      margin: .3rem auto;
      /*position: fixed;*/
      /*bottom: .6rem;*/
      /*left: .24rem;*/
    }
  }
</style>
