<template>
  <div class="app-container order-container">
    <div class="order-head">
      <span class="number">订单号：{{detailData.orderNo}}   <span>交易流水号：{{detailData.payOrderNo}}</span></span>
      <span v-if='detailData.source == 1' class="platform">下单平台：俞姐姐公众号</span>
    </div>
    <div class="order-info">
      <table cellspacing="0" cellpadding="0" border="0" class="order-table">
        <thead class="">
          <th>订单信息</th>
          <th>购买年限</th>
          <th>总金额</th>
          <th>实付金额</th>
        </thead>
        <tbody>
          <tr>
            <td>
              <div class="img">
                <img :src="detailData.goodsImages" alt="">
              </div>
              <div class="info">
                <p class="title">{{detailData.goodsName}}</p>
                <p class="price">{{detailData.goodsPrice}}元/年</p>
              </div>
            </td>
            <td>{{detailData.goodsTimeNum}}年</td>
            <td>{{detailData.price}}</td>
            <td>{{detailData.actualPrice}}</td>
          </tr>
          <tr><td colspan="4" v-if='detailData.payWay == 1'>支付方式：微信支付</td></tr>
          <!-- <tr><td colspan="4">订单备注：asdasdasd奥术大师大所多阿斯达四大</td></tr> -->
          <tr><td colspan="4">订单所属</td></tr>
          <tr>
            <td colspan="4">
              <ul class="order-user">
                <li>
                  <img src="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15390565468631539056546863.png">
                  <p>{{detailData.selfName}}/<span>下单</span></p>
                </li>
                <!-- <li>
                  <img src="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15390565468631539056546863.png">
                  <p>婆娑若梦/<span>下单</span></p>
                </li>
                <li>
                  <img src="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15390565468631539056546863.png">
                  <p>婆娑若梦/<span>下单</span></p>
                </li> -->
              </ul>
            </td>
          </tr>
          <tr>
            <td colspan="4" v-if='detailData.status == 1'>订单状态：已付款</td>
            <td colspan="4" v-if='detailData.status == 0'>订单状态：处理中</td>
            <td colspan="4" v-if='detailData.status == 2'>订单状态：支付失败</td>
          </tr>
        </tbody>
      </table>
      <!-- 物流信息 -->
      <dl class="express-info" v-if="modifyExpressState">
        <dt class="title">物流信息<span @click="showModifyModel()">修改</span></dt>
        <dd>
          <p>姓名：{{ruleForm.name}}</p>
          <p>手机号：{{ruleForm.phone}}</p>
          <p>收货地址：{{ruleForm.address}}</p>
        </dd>
      </dl>
      <div class="express-info modify-express" v-else="modifyExpressState">
        <div class="title">物流信息<span @click="modifySubmit('ruleForm')">确定</span></div>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="express-ruleForm">
          <el-form-item label="姓名:" prop="name">
            <el-input v-model="ruleForm.name" maxlength="10"></el-input>
          </el-form-item>
          <el-form-item label="手机号:" prop="phone">
            <el-input v-model="ruleForm.phone" maxlength="11"></el-input>
          </el-form-item>
          <el-form-item label="收货地址:" prop="address">
            <el-input type="textarea" v-model="ruleForm.address" maxlength="100"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import { modifySubmit } from '@/api/order'
import {orderDetail, saveAddress} from '@/api/manage'
export default {
  name: 'Detail',
  data() {
    return {
      modifyExpressState:true,
      detailData:{},
      ruleForm:{
        name:'',
        phone:'',
        address:''
      },
      rules: {
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
          ],
          phone: [
            { required: true, message: '请输入手机号码', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '请输入收货地址', trigger: 'blur' }
          ]
      },
      orderData: {}

    }
  },
  created() {
    if(this.$route.query.data) {
      this.orderData = this.$route.query.data
    }
    this.init()
  },
  methods: {
    init(){
      orderDetail({
        orderId: this.orderData.orderNo
      }, res=>{
        if(res.code === 200) {
          this.detailData = res.data[0]
          this.ruleForm.name = this.detailData.receiverName
          this.ruleForm.phone = this.detailData.receiverPhone
          this.ruleForm.address = this.detailData.receiverAddress
        }
      })
    },
    //显示修改物流模块
    showModifyModel(){
      this.modifyExpressState = false
    },
    //修改提交
    modifySubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          saveAddress({
            id: this.detailData.id,
            receiverName: this.ruleForm.name,
            receiverPhone: this.ruleForm.phone,
            receiverAddress: this.ruleForm.address
          }, res=>{
            if(res.code === 200) {
              this.init()
              this.modifyExpressState = true
            }else{
              this.$toast(res.data)
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
  @import "./detail.scss";
</style>
