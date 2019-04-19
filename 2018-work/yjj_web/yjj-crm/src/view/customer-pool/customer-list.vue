<template>
	<div class="ivu-card-body ivu-card ivu-card-bordered">
		 <div class="ui-info-box" v-if="infoBoxState">
		 	<span class="message">已选中{{selectNumber}}项</span>
	      	<Button type="primary" class="btn-margin" @click="showReceiveModal">领取</Button>
	      	<Button type="primary" class="btn-margin" @click="showDistributeModal">分配</Button>
	      	<div class="close-bar" @click="closeInfoBox"><Icon type="ios-close" size="40" /></div>
	     </div>
		 <div class="search-con ft-align-right">
		 	<Button type="success" icon="ios-add" @click="createCustomerModal">创建客户</Button>
	      	<Button type="primary" class="btn-margin" @click="showImportCustomerModal">导入客户</Button>
	     </div>
		<Row>
			<Col span="24">
		   		<Table
		   		highlight-row
		   		ref="selection"
		   		border
		   		:loading="loading"
		   		:columns="columnsData"
		   		:data="tableData"
		   		@on-select-all="handleRowAllSelect"
		   		@on-selection-change="handleRowChange"></Table>
			</Col>
			<Col span="24">
				<div class="page-box">
					 <Page :total="pageTotal" @on-change="changePage"></Page>
				</div>
			</Col>
			<Modal draggable v-model="receiveModalState" title="客户领取" @on-ok="receiveCustomer">
				<p>你要领取以下客户：{{selectNumber}}个</p>
				<div class="show-customer">
					<span v-for="item in showSelectName">{{item}}，</span>
					<span v-if="selectCustomerName.length > 4">……</span>
				</div>
		    </Modal>
		    <Modal draggable v-model="distributeModalState" title="客户领取" @on-ok="distributeCustomer">
				<p>你要领取以下客户：{{selectNumber}}个</p>
				<div class="show-customer">
					<span v-for="item in showSelectName">{{item}}，</span>
					<span v-if="selectCustomerName.length > 4">……</span>
				</div>
				<div class="model-form">
					<Form :label-width="80">
						<FormItem label="分配到人员" prop="distributeToManage">
				        	<Input v-model="distributeToManage"></Input>
				    	</FormItem>
					</Form>
				</div>
		    </Modal>
		    <Modal draggable v-model="importModalState" title="客户导入">
				<div class="modal-box">
					<p>请按照导入格式，导入数据。<br/>格式要求：xls或xlsx格式</p>
					<Upload
						ref="upload"
						action="https://local.nessary.top:8081/crm/pool/excel/add/"
            			:data="userData"
						:format="['xls', 'xlsx']"
						:on-success="uploadSuccess">
				        <Button type="info" class="add-btn btn-margin" iocn="ios-cloud-upload-outline">添加数据</Button>
				    </Upload>
				</div>
				<div class="ivu-upload-list show-file"></div>
		    </Modal>
		    <Modal width="700" v-model="createCustomerState" title="创建客户" :loading="createModalLoading" @on-ok="createCustomerSubmit('customerForm')">
		        <Form ref="customerForm" :model="formValidate" :rules="ruleValidate" :label-width="100">
				    <FormItem label="企业名称" prop="companyName">
				      <Input v-model="formValidate.companyName" placeholder="请输入企业名称"></Input>
				    </FormItem>
				    <FormItem label="法人姓名" prop="legalName">
				        <Input v-model="formValidate.legalName" placeholder="请输入法人姓名"></Input>
				    </FormItem>
				    <FormItem label="营业执照" prop="licence">
				        <Input v-model="formValidate.licence" placeholder="请输入营业执照"></Input>
				    </FormItem>    
				    <FormItem label="公司网址" prop="website">
				        <Input v-model="formValidate.website" placeholder="请输入公司网址"></Input>
				    </FormItem>
				    <FormItem label="职位" prop="job">
				        <Input v-model="formValidate.job" placeholder="请输入职位"></Input>
				    </FormItem>
				    <FormItem label="姓名" prop="userName">
				        <Input v-model="formValidate.userName" placeholder="请输入姓名"></Input>
				    </FormItem>
				    <FormItem label="客户来源" prop="source">
				       <Select v-model="formValidate.source">
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
				        <Input v-model="formValidate.telPhone" placeholder="请输入客户电话或手机号"></Input>
				    </FormItem>
				    <FormItem label="客户类型" prop="type">
				       <Select v-model="formValidate.type">
				       	  <Option value="0类型(未联系)">0类型(未联系)</Option>
				          <Option value="1类型(已签约)">1类型(已签约)</Option>
				          <Option value="2类型(已确认合作未签约)">2类型(已确认合作未签约)</Option>
				          <Option value="3类型(高愿意客户)">3类型(高愿意客户)</Option>
				          <Option value="4类型(非KP)">4类型(非KP)</Option>
				          <Option value="5类型(毁单)">5类型(毁单)</Option>
				       </Select>
				    </FormItem>
				     <FormItem label="省份" prop="address">
				        <div class="address-box"><al-selector data-type="name" v-model="formValidate.address" level="2" /></div>
				    </FormItem>
				    <FormItem label="所属行业" prop="industry">
				        <Select v-model="formValidate.industry">
				          <Option  v-for="item in industryData" :value="item">{{item}}</Option>
				        </Select>
				    </FormItem>
				    <FormItem label="主营业务" prop="business">
				        <Input v-model="formValidate.business" placeholder="请输入主营业务"></Input>
				    </FormItem>
				    <FormItem label="新增归属人" prop="belonger">
				        <Input v-model="formValidate.belonger" placeholder="请输入主营业务"></Input>
				    </FormItem>
				    <FormItem label="最新跟进记录" prop="followRecord">
				        <Input v-model="formValidate.followRecord" placeholder="请输入主营业务"></Input>
				    </FormItem>
			 		<!-- <FormItem>
			            <Button type="primary" @click="createCustomerSubmit('customerForm')">Submit</Button>
			        </FormItem> -->
				  </Form>  
		    </Modal>
		</Row>
	</div>
</template>

<script>
import { getCustomerData, createCustomer, manageCustomer } from '@/api/customer'
import { industryData } from '@/assets/js/industryData'
import Router from 'vue-router'
import customerForm from '_c/customer-form'
import { removeByValue } from '@/libs/tools'
import { getUserId } from '@/libs/util'
export default {
  name: 'customer-pool-list',
  components: {
    customerForm
  },
  data () {
  	const checkTelPhone = (rule, value, callback) => {
  		var re_phone = /^((0\d{2,3}-\d{7,8})|(1[358476]\d{9}))$/;
  		if (value === '') {
            callback(new Error('请输入客户电话或手机号'));
        }else if(!re_phone.test(value)){
			callback(new Error('请输入正确格式的电话'));
        }
  	}
	return {
		industryData:industryData,          //行业数据
		infoBoxState:false,
		selectNumber:0,
		selectAllState:false,
		selectCustomerId:[],
		selectCustomerName:[],
		createModalLoading:true,    //创建表单的按钮
		showSelectName:[],                 //显示最多4个姓名
		loading:false,
		receiveModalState:false,          // 控制领取弹窗的状态
		distributeModalState: false,          // 控制分配弹窗的状态
		createCustomerState:false,      // 控制创建客户弹窗的状态
		importModalState:false,         //控制导入客户状态
		distributeToManage:'',          //分配到人员
		pageTotal:1,               //总页数
		userId:'',					//用户id
		fileName:'',                //导入文件名
    	userData: {userId: '', addStatus:0},
		columnsData: [
			{
				type: 'selection',
				width: 60,
				align: 'center'
			},
			{
				title: '客户ID',
				key: 'id'
			},
			{
				title: '姓名',
				key: 'name'
			},
			{
				title: '客户电话',
				key: 'phone'
			},
			{
				title: '省份',
				key: 'province'
			},
			{
				title: '城市',
				key: 'city'
			},
			{
				title: '区县',
				key: 'district'
			},
			{
				title: '所属行业',
				key: 'profession'
			},
			{
				title: '主营业务',
				key: 'mainBusiness'
			},
			{
				title: '创建时间',
				key: 'time'
			},
			{
				title: '所属用户姓名',
				key: 'belonger'
			},
			{
				title: '最新跟进记录',
				key: 'lastestRecord'
			},
			{
				title: '状态',
				key: 'type'
			}
		],
		tableData: [],
		 formValidate: {
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
	        business:'',
	        belonger:'',
	        followRecord:'',
        },
        ruleValidate: {
            userName: [
                { required: true, message: '请输入姓名'}
            ],
            source: [
                { required: true, message: '请输入客服来源', trigger: 'blur' }
            ],
            telPhone: [
                {required: true, message: '请输入电话号码', trigger: 'blur', pattern:/^((0\d{2,3}-\d{7,8})|(1[358476]\d{9}))$/}
            ],
            type: [
                { required: true, message: '请输入客户类型', trigger: 'blur'}
            ],
            address: [
                {required: true, type: 'array', message: '请输入地址',  trigger: 'blur'}
            ],
            industry: [
                { required: true, message: '请输入所属行业', trigger: 'blur'}
            ],
            business: [
                { required: true, message: '请输入主营业务', trigger: 'blur'}
            ],
            belonger: [
                { required: true, message: '请输入新增归属人', trigger: 'blur'}
            ],
            followRecord: [
                { required: true, message: '请输入最新跟进记录', trigger: 'blur'}
            ]
        }
	}
  },
  created () {
    // 组件创建完后获取数据，
    this.fetchData(1);
    this.userId = getUserId();
    this.userData.userId = getUserId();
    console.log("用户id",getUserId());
  },
  computed: {
    
  },
  methods: {
  	//loadding模拟测试
    fetchData (pageNumber) {
      	var that = this;
      	that.loading = true;
      	var params = {
      		status:0,
      		pageSize:10,
      		pageNumber:pageNumber
      	};
		getCustomerData(params).then(res => {
			that.loading = false;
			let data = res.data;
			if(data.code ==200){
				this.tableData = data.data.list;
				this.pageTotal = data.data.total
			}
		})
    },
    //显示领取客户弹窗
    showReceiveModal(){
    	this.receiveModalState = true
    },
    //确认领取
    receiveCustomer(){
    	var params = {
    		ids:this.selectCustomerId.join(),
    		status:0,
    		userId:this.userData.userId
    	};
    	manageCustomer(params).then(res =>{
    		let data = res.data;
    		if(data.code == 200){
    			this.$Message.success('领取成功');
    			this.receiveModalState = false;
    			this.fetchData(1)
    		}else{
    			this.$Message.error(data.data)
    		}
    	})
    },
    //显示分配客户弹窗
  	showDistributeModal(){
  		this.distributeModalState = true
  	},
    //确认分配
    distributeCustomer(){
    	if(!this.distributeToManage){
    		this.$Message.error("请输入分配人员")
    		return
    	}
		var params = {
    		ids:this.selectCustomerId.join(),
    		allotName:this.distributeToManage,
    		status:2,
    		userId:this.userData.userId
    	};
    	manageCustomer(params).then(res =>{
    		let data = res.data;
    		if(data.code == 200){
    			this.$Message.success('分配成功');
    			this.distributeModalState = false;
    			this.fetchData(1)
    		}else{
    			this.$Message.error(data.data)
    		}
    	})
    },
     //显示导入客户弹窗
    showImportCustomerModal(){
    	this.importModalState = true
    },
    //上传成功
    uploadSuccess(response, file, fileList){
      console.log(response, file, fileList, 'response, file, fileList')
      if(response.code === 200){
        this.$Notice.success({
          title: '文件上传成功',
          desc: '文件 ' + file.name + '上传成功。',
          duration: 2,
          onClose: ()=>{
            this.importModalState = false
          }
        });
        this.fetchData(1);
      }else{
        this.$Notice.warning({
          title: '文件上传失败',
          desc: response.data.data,
          duration: 2,
          onClose: ()=>{
            this.importModalState = false
          }
        });
      }
    },
    //关闭按钮条
    closeInfoBox(){
    	this.infoBoxState = false
    },
    //全选
     handleRowAllSelect (selection) {
     	this.infoBoxState = true;
     },
    //选择改变
    handleRowChange(selection){
    	//console.log("aa",selection);
    	this.selectNumber = selection.length;
    	//没有选中状态时
		if(selection.length == 0){
    		this.infoBoxState = false
    	}else{
    		this.infoBoxState = true
    	}
    	var idArray = [],
    	    nameArray = [];
    	for(let i = 0,l = selection.length; i < l;i++){
    		idArray.push(selection[i].id);
    		nameArray.push(selection[i].name)
    	}
    	//显示最多4个名字
        if(nameArray.length > 4){
        	this.showSelectName = nameArray.slice(0,4);
        }else{
        	this.showSelectName = nameArray;
        }
        console.log("showSelectName",this.showSelectName);
		
    	this.selectCustomerId = idArray;
	    this.selectCustomerName = nameArray;
    	this.selectNumber = selection.length;

    	console.log("当前选中的值", this.selectCustomerId, this.selectCustomerName)
    },
     //获取分页数据
    getPageData(currentPage){
    	let data = [];
    	data = this.fetchData (currentPage)
		return data;  	
    },
    changePage(currentPage){
    	this.tableData = this.getPageData(currentPage)
    },
    //创建客户
    createCustomerModal(){
    	this.createCustomerState = true;
    },
    //创建客户
    createCustomerSubmit(name){
    	console.log("进入");
    	var params = {
      		businessName:this.formValidate.companyName,
      		artificialPersonName:this.formValidate.legalName,
      		businessLicence:this.formValidate.licence,
      		businessUrl:this.formValidate.website,
      		position:this.formValidate.job,
      		customerSource:this.formValidate.source,
      		name:this.formValidate.userName,
      		phone:this.formValidate.telPhone,
      		customerGrade:this.formValidate.type,
      		province:this.formValidate.address[0],
      		city:this.formValidate.address[1],
      		district:this.formValidate.address[2],
      		profession:this.formValidate.industry,
      		mainBusiness:this.formValidate.business,
      		belonger:this.formValidate.belonger,
	        lastestRecord:this.formValidate.followRecord,
      		addStatus:0,
      		userId:this.userId
      };
      this.$refs[name].validate((valid) => {
      	console.log(this.createModalLoading);
      	console.log("所有的数据",this.formValidate,params);
        if (valid) {
        	console.log("数据",params);
        	createCustomer(params).then(res =>{
        		var data = res.data;
        		console.log(data);
        		if(data.code == 200){
        			this.createCustomerState = false;
		            this.createModalLoading = false;
		            this.$Message.success('Success!');
		            this.$refs.customerForm.resetFields();  //重置
		            this.fetchData(1);
        		}else{
        			alert(data.data);
        			this.createModalLoading = false;
        			setTimeout(() => {
			            this.createModalLoading = true;
			        }, 1000);
        		}
        	})
        }else{
			this.createModalLoading = false;
			console.log("不通过");
			setTimeout(() => {
	            this.createModalLoading = true;
	        }, 1000);
        }
      })
    }
  }
}
</script>

<style>
	.ui-form{
		width: 100%;
	}
	.show-customer{
		height: 30px;
		color: #fd4374;
		font-size: 14px;
		margin: 10px 0;
	}
	.model-form{
		margin-top: 15px;
	}
</style>
