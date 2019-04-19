<template>
  <Form ref="customerForm" :model="form" :rules="rules" @keydown.enter.native="handleSubmit" :label-width="80">
    <FormItem label="企业名称" prop="companyName">
      <Input v-model="form.companyName" placeholder="请输入企业名称"></Input>
    </FormItem>
    <FormItem label="法人姓名" prop="legalName">
        <Input v-model="form.legalName" placeholder="请输入法人姓名"></Input>
    </FormItem>
    <FormItem label="营业执照" prop="licence">
        <Input v-model="form.licence" placeholder="请输入营业执照"></Input>
    </FormItem>    
    <FormItem label="公司网址" prop="website">
        <Input v-model="form.website" placeholder="请输入公司网址"></Input>
    </FormItem>
    <FormItem label="职位" prop="job">
        <Input v-model="form.job" placeholder="请输入职位"></Input>
    </FormItem>
    <FormItem label="姓名" prop="userName">
        <Input v-model="form.userName" placeholder="请输入姓名"></Input>
    </FormItem>
    <FormItem label="客户来源" prop="source">
       <Select v-model="form.source">
          <Option value="客服热线">客服热线</Option>
          <Option value="市场招商">市场招商</Option>
          <Option value="员工介绍">员工介绍</Option>
          <Option value="网络搜索">网络搜索</Option>
          <Option value="官网报名">官网报名</Option>
          <Option value="老客户介绍">老客户介绍</Option>
          <Option value="招商会咨询">招商会咨询</Option>
       </Select>
    </FormItem>
    <FormItem label="客户电话" prop="telPhone">
        <Input v-model="form.telPhone" placeholder="请输入客户电话或手机号"></Input>
    </FormItem>
    <FormItem label="客户类型" prop="type">
       <Select v-model="form.type">
          <Option value="意向客户">意向客户</Option>
          <Option value="合作客户">合作客户</Option>
          <Option value="普通用户">普通用户</Option>
       </Select>
    </FormItem>
     <FormItem label="省份" prop="address">
        <div class="address-box"><al-selector data-type="name" v-model="form.address" level="2" /></div>
    </FormItem>
    <FormItem label="所属行业" prop="industry">
        <Select v-model="form.industry">
          <Option value="服装">服装</Option>
          <Option value="美甲">美甲</Option>
          <Option value="美容">美容</Option>
        </Select>
    </FormItem>
    <FormItem label="主营业务" prop="business">
        <Input v-model="form.business" placeholder="请输入主营业务"></Input>
    </FormItem>
    <!-- <FormItem>
      <Button @click="handleSubmit" type="primary" long>提交</Button>
    </FormItem> -->
  </Form>     
</template>
<script>
export default {
  name: 'customerForm',
  props: {
    userNameRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入姓名'}
        ]
      }
    },
    sourceRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入客服来源', trigger: 'blur'}
        ]
      }
    },
    telPhoneRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入客户电话', trigger: 'blur', rules:'/^((0\d{2,3}-\d{7,8})|(1[358476]\d{9}))$/'}
        ]
      }
    },
    typeRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入客户类型', trigger: 'blur'}
        ]
      }
    },
    addressRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入省份地址', trigger: 'blur'}
        ]
      }
    },
    industryRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入所属行业', trigger: 'blur'}
        ]
      }
    },
    businessRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '请输入主营业务', trigger: 'blur'}
        ]
      }
    }
  },
  data () {
    return {
      form: {
        companyName:'',
        legalName:'',
        licence:'',
        website:'',
        job:'',
        userName: '',
        source:'',
        telPhone:'',
        type:'',
        industry:'',
        address: [],
        business:''
      }
    }
  },
  computed: {
    rules () {
      return {
        userName: this.userNameRules,
        telPhone: this.telPhoneRules,
        source: this.sourceRules,
        type:this.typeRules,
        address: this.addressRules,
        industry: this.industryRules,
        business: this.businessRules
      }
    }
  },
  methods: {
    handleSubmit () {
      this.$refs.customerForm.validate((valid) => {
        if (valid) {
          this.$emit('on-ok', {
            userName: this.form.userName,
            password: this.form.password
          })
        }
      })
    }
  }
}
</script>
