<template>
  <div class="app-container">
    <el-form ref="auditForm" :model="auditForm" :inline="true" label-width="80px">
      <el-form-item label="用户昵称"><el-input v-model="auditForm.wxName"/></el-form-item>
      <el-form-item label="姓名"><el-input v-model="auditForm.name"/></el-form-item>
      <el-form-item label="手机号"><el-input v-model="auditForm.phone"/></el-form-item>
      <el-form-item label="所在地区">
        <!-- <area-cascader v-model="auditForm.region" :level="1" :data="pcaa" placeholders="请选择" type="text"/> -->
        <area-select :level="2" v-model="auditForm.region" :data="pcaa" :placeholders="['选择省', '选择市', '选择区']" type="text" @change='getRegion'/>
      </el-form-item>
      <el-form-item label="身份证号"><el-input v-model="auditForm.idCardNo"/></el-form-item>
      <el-form-item label="提交时间">
        <el-date-picker
          v-model="auditForm.time"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="query">搜索</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form-item>
      <el-table :data="auditTableData" border style="width: 100%" highlight-current-row @current-change="">
        <el-table-column prop="wxName" label="用户昵称" align="center"/>
        <el-table-column prop="name" label="姓名" align="center"/>
        <el-table-column prop="phone" label="手机号" align="center"/>
        <el-table-column prop="idCardNo" label="身份证号" align="center"/>
        <el-table-column label="地址" align="center">
          <template slot-scope="scope">
            <span>{{scope.row.province}}{{scope.row.city}}{{scope.row.county}}{{scope.row.addressDetail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1" size="small" type="success">市级代理</el-tag>
            <el-tag v-if="scope.row.type === 0" size="small" type="success">客户</el-tag>
            <el-tag v-if="scope.row.type === 2" size="small" type="success">县代理</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTimeReadable" label="提交时间" align="center"/>
        <el-table-column label="审核状态" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.auditStatus === 0" type="success">通过</el-tag>
            <el-tag v-if="scope.row.auditStatus === 1">处理中</el-tag>
            <el-tag v-if="scope.row.auditStatus === 2" type="fail">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-row>
              <el-button type="text" v-if="scope.row.auditStatus == 1" @click="handleUpdate(scope.row)">审核</el-button>
              <el-button type="text" @click='deleteAgent(scope.row)'>删除</el-button>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      :total="totalCount"
      class="pagination"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"/>
    <!-- 审核弹窗 -->
    <el-dialog
      title="代理商审核"
      :visible.sync="centerDialogVisible"
      width="30%"
      center>
      <!--审核-->
      <div class="examine-box" v-if="examineStatus === 1">
        <dl class="base-info">
          <dt>基本信息</dt>
          <dd>
            <img src="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15390565468631539056546863.png" alt="">
            <p>{{auditInfo.wxName}}</p>
            <p>{{auditInfo.createTimeReadable}}</p>
          </dd>
        </dl>
        <dl class="apply-info">
          <dt>申请信息</dt>
          <dd>
            <p>姓名：{{auditInfo.name}}<span>手机号：{{auditInfo.phone}}</span></p>
            <p>身份证号：{{auditInfo.idCardNo}}</p>
            <p>地址：{{auditInfo.province}}{{auditInfo.city}}{{auditInfo.county}}{{auditInfo.addressDetail}} </p>
          </dd>
        </dl>
      </div>
      <span slot="footer" class="dialog-footer" v-if="examineStatus === 1">
        <el-button type="primary" @click="examineSubmit">审核通过</el-button>
        <el-button @click="examineRefuse()">审核拒绝</el-button>
      </span>
      <!-- 审核成功 -->
      <div class="examine-success" v-if="examineStatus === 2">
        <span class="icon"></span>
        <h3 class="title">操作成功</h3>
        <p class="text">用户已通过审核</p>
        <p class="text">请前往代理商管理进行查看</p>
      </div>
      <span slot="footer" class="dialog-footer" v-if="examineStatus === 2">
        <el-button type="primary" @click="centerDialogVisible = false">继续审核</el-button>
        <el-button @click="gotoAgent()">前往查看</el-button>
      </span>
      <!-- 拒绝 -->
      <div class="examine-refuse" v-if="examineStatus === 0">
        <el-form :model="examineForm" :rules="examineRules" ref="examineForm" label-width="10px">
          <div class="label-title">拒绝理由<span>（必填项）</span></div>
          <el-form-item label="" prop="reason">
            <el-input type="textarea" v-model="examineForm.reason" maxlength="100" placeholder="请输入拒绝理由，最多输入100字"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer" v-if="examineStatus === 0">
        <el-button type="primary" @click="examineRefuseSubmit()">确定</el-button>
        <el-button @click="centerDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import { pca, pcaa } from 'area-data'
import {auditList, audit, deleteAgent, doOrderReward} from '@/api/audit'
export default {
  name: 'Index',
  data() {
    return {
      examineStatus:1,            //0:拒绝； 1:待审核; 2:审核成功
      auditForm: {
        wxName: '',
        name: '',
        phone: '',
        region: [],
        idCardNo: '',
        time: ''
      },
      examineForm:{
        reason:''
      },
      auditTableData: [
        // { nickName: 'shdas', name: 'sss', phone: 12222, idNumber: 212121, address: '三个傻瓜都要算的', type: '市级代理', time: '2018.09.20' }
      ],
      examineRules:{
        reason:[{ required: true, message: '请填写拒绝理由', trigger: 'blur' }]
      },
      pcaa: pcaa,
      currentPage: 0,
      pageSize: 10,
      totalCount: null,
      centerDialogVisible:false,
      auditInfo: ''
    }
  },
  created() {
    this.init()
  },
  methods: {
    init(){
      auditList({
        pageSize: this.pageSize,
        pageNumber: this.currentPage,
        wxName: this.auditForm.wxName,
        userName: this.auditForm.name,
        phone: this.auditForm.phone,
        province: this.auditForm.region[0],
        city: this.auditForm.region[1],
        county: this.auditForm.region[2],
        idCardNo: this.auditForm.idCardNo,
        createTimeBefore: this.auditForm.time[0],
        createTimeAfter: this.auditForm.time[1]
      }, res=>{
        if(res.code === 200) {
          console.log(res.data)
          this.auditTableData = res.data.list
          this.totalCount = res.data.total
          this.pageSize = res.data.pageSize
        }else{
          this.$message({
            type: 'error',
            message: res.data
          });
        }
      })
    },
    query() {
      this.pageSize = 10
      this.init()
    },
    reset() {
      this.pageSize = 10
      this.auditForm.wxName = ''
      this.auditForm.name = ''
      this.auditForm.region = []
      this.auditForm.idCardNo = ''
      this.auditForm.time = []
      this.auditForm.phone = ''
      this.init()
    },
    getRegion(val){
      if(val[1] ==  "市辖区"){
        val[1] = val[0]
      }
      console.log(val)
      this.auditForm.region = val
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 0
      this.init()
    },
    handleCurrentChange(val) {
      console.log(val,'./././././')
      this.currentPage = val
      this.init()
    },
    //显示审核弹窗
    handleUpdate(row) {
      this.examineStatus = 1;
      this.centerDialogVisible = true
      this.auditInfo = row
    },
    //同意提交
    examineSubmit(){
      audit({
        auditId: this.auditInfo.id,
        msg: this.auditInfo.reason,
        isPass: 1
      }, res=>{
        if(res.code === 200) {
           this.examineStatus = 2;
           this.init()
        }else{
          this.$message({
            type: 'error',
            message: res.data
          });
        }
      })

    },
    //拒绝
    examineRefuse(){
      this.examineStatus = 0;
    },
    //拒绝提交
    examineRefuseSubmit(){
       audit({
        auditId: this.auditInfo.id,
        msg: this.examineForm.reason,
        isPass: 0
      }, res=>{
        if(res.code === 200) {
          this.examineStatus = 1;
          this.centerDialogVisible = false
          this.init()
        }else{

        }
      })

    },
    // 删除代理
    deleteAgent(row) {
      this.$confirm('确认删除此条记录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteAgent({
            auditId: row.id
          }, res=>{
            if(res.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.init()
            }else{
              this.$message({
                type: 'error',
                message: res.data
              });
            }

          })
        }).catch(() => {

        });

    },
    gotoAgent() {
      this.$router.push({
        path: '/agent-system/index'
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./index.scss";
</style>
