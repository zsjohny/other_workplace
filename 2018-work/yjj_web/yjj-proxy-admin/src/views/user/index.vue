<template>
  <div class="app-container">
    <el-form ref="userForm" :model="userForm" :inline="true" label-width="80px">
      <el-form-item label="用户昵称"><el-input v-model="userForm.wxName"/></el-form-item>
      <el-form-item label="姓名"><el-input v-model="userForm.userName"/></el-form-item>
      <el-form-item label="手机号"><el-input v-model="userForm.phone"/></el-form-item>
      <el-form-item label="提交时间">
        <el-date-picker
          v-model="userForm.time"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          @change="getTime"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="query">搜索</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="userTableData" border style="width: 100%" highlight-current-row>
      <el-table-column prop="id" label="ID" align="center"/>
      <el-table-column prop="wxName" label="用户昵称" align="center"/>
      <el-table-column prop="userName" label="姓名" align="center"/>
      <el-table-column prop="phone" label="手机号" align="center"/>
      <el-table-column prop="createTimeReadable" label="注册时间" align="center"/>
      <el-table-column prop="platform" label="来源平台" align="center"/>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.delStatus === 0" type="success">启用</el-tag>
          <el-tag v-if="scope.row.delStatus === 1" type="fail">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-row>
            <el-button type="text" @click="startOrBan(scope.row, 0)" v-if='scope.row.delStatus == 1'>启用</el-button>
            <el-button type="text" @click="startOrBan(scope.row, 1)" v-if='scope.row.delStatus == 0'>禁用</el-button>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      :total="totalCount"
      class="pagination"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"/>
  </div>
</template>

<script>
  import {listUser} from '@/api/user'
  import { stopCustomer } from '@/api/audit'
export default {
  name: 'Index',
  data() {
    return {
      userForm: {
        wxName: '',
        userName: '',
        phone: '',
        time: ''
      },
      userTableData: [

      ],
      currentPage: 0,
      pageSize: 10,
      totalCount: null
    }
  },
  created() {
    this.init()
  },
  methods: {
    init(){
      listUser({
        pageNumber: this.currentPage,
        pageSize: this.pageSize,
        wxName: this.userForm.wxName,
        userName: this.userForm.userName,
        phone: this.userForm.phone,
        createTimeBefore: this.userForm.time[0],
        createTimeAfter: this.userForm.time[1]
      }, res=>{
        if(res.code === 200) {
          this.userTableData = res.data.list
          this.totalCount = res.data.total
          this.pageSize = res.data.pageSize
        }
      })
    },
    query() {
      this.pageSize = 10
      this.init()
    },
    reset() {
      this.pageSize = 10
      this.userForm.wxName = ''
      this.userForm.userName= ''
      this.userForm.phone= ''
      this.userForm.time = []
      this.init()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.init()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.init()
    },
    getTime(val) {
      console.log(val[0])
    },
    startOrBan(row, val) {
      this.$confirm(`是否${val == 0 ? '启用' : '禁用'}此用户`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        stopCustomer({
          userId: row.id,
          type: val
        }, res=>{
          if(res.code === 200) {
            this.$message({
              type: 'success',
              message: '操作成功!'
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
    }
  }
}
</script>

<style scoped>

</style>
