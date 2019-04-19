<template>
  <div class="content">
    <ul class="inputWrap">
      <li class="van-hairline--bottom">
        <span>收件人:</span>
        <input type="text" class="input" placeholder="请输入收件人" v-model="name">
      </li>
      <li class="van-hairline--bottom">
        <span>联系电话:</span>
        <input type="tel" class="input" maxlength="11" @keyup="handleInput" placeholder="请输入电话号码" v-model="phone">
      </li>
      <li class="region van-hairline--bottom" @click="setAddress">
        <div>
          <p>所在地区:</p>
          <input type="text" placeholder="请输入所在地区" v-model="region" class="input" onfocus="this.blur()">
        </div>
        <img src="~@/assets/image/user/downArrow.png" alt="" class="downArrow">
      </li>
      <li class="van-hairline--bottom">
        <p>详细地址:</p>
        <van-field
          v-model="address"
          class="input"
          type="textarea"
          placeholder="请输入详细地址"
          rows="1"
          autosize
        />
        <!--<input type="text" placeholder="请输入详细地址" v-model="address" class="input">-->
      </li>
      <li class="switch van-hairline--bottom">
        <span>默认地址:</span>
        <van-switch v-model="isDefault" />
      </li>
    </ul>
    <div class="btnWrap">
      <button class="conserve" @click="saveAddress" :disabled="dis">保存</button>
      <button class="delete" v-if="showDelete" @click="deleteAddress" :disabled="dis">删除</button>
    </div>
    <van-popup v-model="showArea" position="bottom">
      <van-area :area-list="areaList" @cancel="cancelArea" @confirm="confirmArea" v-if="showArea"/>
    </van-popup>
  </div>
</template>

<script>
import areaList from '@/config/area'
import {selectAddress, saveAddress} from '@/api/user'
export default {
  name: 'add-audit-address',
  data () {
    return {
      checked: true,
      showArea: false,
      areaList: areaList,
      data: null,
      showDelete: false,
      info: '',
      region: '',
      province: '',
      county: '',
      city: '',
      name: '',
      phone: '',
      address: '',
      isDefault: true,
      routerFrom: '',
      dis: false
    }
  },
  created () {
    if(this.$route.query.data) {
      this.data = this.$route.query.data
      if(this.data !== 0) {
        this.showDelete = !this.showDelete
        this.init()
      }
      console.log(this.data,'this.$router.query.data')
    }
    if(this.$route.query.routerFrom) {
      this.routerFrom = this.$route.query.routerFrom
    }
  },
  methods: {
    init(){
      selectAddress({
        id: this.data.id
      }, res=>{
        if(res.code === 200) {
          this.info = res.data
          this.county = this.info.county
          this.city = this.info.city
          this.province = this.info.province
          this.region = this.province + '/' + this.city + '/' + this.county
          this.name = this.info.receiverName
          this.phone = this.info.receiverPhone
          this.address = this.info.receiverAddress
          this.isDefault = this.info.isDefault === 1 ? true : false
        }else{
          this.$toast(res.data)
        }
      })
    },
    handleInput() {
      this.phone = this.phone.replace(/[^\d]/g,'')
    },
    setAddress () {
      this.showArea = !this.showArea
      // this.$refs.regionInput.focus(()=>{
      //   document.activeElement.blur()
      // })
    },
    cancelArea () {
      this.showArea = !this.showArea
    },
    confirmArea (data) {
      this.showArea = !this.showArea
      console.log(data)
      this.province = data[0].name
      this.city = data[1].name
      this.county = data[2].name
      this.region = this.province + '/' + this.city + '/' + this.county
    },
    saveAddress() { // 保存删除
      if(this.dis === false) {
        if(this.address !== '' && this.phone !== '' && this.name !== '' && this.province !== '') {
          if(this.showDelete) {
            this.dis = true
            // 修改
            saveAddress({
              id: this.data.id,
              isDefault: this.isDefault === true ? 1 : 0,
              province: this.province,
              city: this.city,
              county: this.county,
              receiverName: this.name,
              receiverPhone: this.phone,
              receiverAddress: this.address,
              // delStatus:
            }, res=>{
              if(res.code === 200) {
                this.$toast('保存成功')
                if(this.routerFrom === 'payOrder') {
                  console.log('来自支付订单')
                  setTimeout(()=>{
                    this.$router.replace({
                      path: './payOrder'
                    })
                  },3000)
                }else{
                  setTimeout(()=>{
                    this.$router.replace({
                      path: './addressList',
                      query: {
                        routerFrom: 'add_audit'
                      }
                    })
                  },3000)
                }
              }else{
                this.$toast(res.data)
              }
              this.dis = false
            })
          }else{
            this.dis = true
            // 添加
            saveAddress({
              isDefault: this.isDefault === true ? 1 : 0,
              province: this.province,
              city: this.city,
              county: this.county,
              receiverName: this.name,
              receiverPhone: this.phone,
              receiverAddress: this.address,
              // delStatus:
            }, res=>{
              if(res.code === 200) {
                this.$toast('保存成功')
                if(this.routerFrom === 'payOrder') {
                  console.log('来自支付订单')
                  setTimeout(()=>{
                    this.$router.replace({
                      path: './payOrder'
                    })
                  },3000)
                }else{
                  setTimeout(()=>{
                    this.$router.replace({
                      path: './addressList',
                      query: {
                        routerFrom: 'add_audit'
                      }
                    })
                  },3000)
                }
              }
              this.dis = false
            })
          }
        }else{
          this.$toast('请填写完整信息')
        }
      }

    },
    deleteAddress() {
      this.$dialog.confirm({
        title: '您确定要删除地址吗？'
      }).then(()=>{
        saveAddress({
          id: this.data.id,
          isDefault: this.isDefault === true ? 1 : 0,
          province: this.province,
          city: this.city,
          county: this.county,
          receiverName: this.name,
          receiverPhone: this.phone,
          receiverAddress: this.address,
          delStatus: 1
        }, res=>{
          if(res.code === 200) {
            // this.$toast('删除成功')
            // setTimeout(()=>{
              this.$router.replace({
                path: './addressList',
                query: {
                  routerFrom: 'add_audit'
                }
              })
            // },3000)
          }
        })
      }).catch(()=>{

      })

    }
  }
}
</script>

<style scoped lang="less">
  @import '~@/assets/less/base';
  .content{
    background-color: #fff;
    .inputWrap{
      padding: .3rem .24rem;

      li{
        padding: .2rem 0;
        .input{
          width: 6rem;
        }
      }
      .region{
        .flex;
        align-items: center;
        justify-content: space-between;
        .downArrow{
          .wh(.3rem,.16rem);
        }
      }
      .switch{
        .flex;
        align-items: center;
        span{
          margin-right: .4rem;
        }
      }
    }
    .btnWrap{
      position: absolute;
      bottom: 1rem;
      left: .24rem;
      .conserve{
        .wh(7.02rem,1rem);
        background-color: #222;
        color: #f6f6f6;
        text-align: center;
        line-height: 1rem;
        border-radius: .08rem;
      }
      .delete{
        .conserve();
        background-color: #999;
        margin-top: .2rem;
      }
    }
  }
</style>
