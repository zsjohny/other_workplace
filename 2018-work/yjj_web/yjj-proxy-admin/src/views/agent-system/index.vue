<template>
  <div class="app-container">
    <el-form ref="agentForm" :model="agentForm" :inline="true" label-width="80px">
      <el-form-item label="姓名">
        <el-input v-model="agentForm.name"/>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="agentForm.phone"/>
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker
          v-model="agentForm.time"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <br>
      <el-form-item label="所在地区">
        <!--<area-select :level="2" v-model="agentForm.region" :data="pcaa" :placeholders="['选择省', '选择市', '选择区']" type="text"/>-->
        <area-select :level="2" v-model="agentForm.region" :data="pcaa" :placeholders="['选择省', '选择市', '选择区']" type="text" @change='getRegion'/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="query">搜索</el-button>
      </el-form-item>
      <el-tabs v-model="activeName" type="border-card" @tab-click="clickTab">
        <el-tab-pane label="市级代理" name="1">
          <el-table :data="tableData" border style="width: 100%" highlight-current-row>
            <el-table-column prop="id" label="ID" align="center"/>
            <el-table-column prop="name" label="姓名" align="center"/>
            <el-table-column prop="phone" label="手机号" align="center"/>
            <el-table-column label="所在地区" align="center">
              <template slot-scope='scope'>
                <span>{{scope.row.province}}{{scope.row.city}}{{scope.row.county}}</span>
              </template>
            </el-table-column>
            <el-table-column label="注册时间" align="center">
              <template slot-scope="scope">
                <span>{{$formatDate(scope.row.createTime)}}</span>
              </template>
            </el-table-column>
            <el-table-column label="客户数" align="center">
              <el-table-column prop="customerNum" label="本人客户" align="center"/>
              <el-table-column prop="agentCustomerNum" label="代理客户" align="center"/>
            </el-table-column>
            <el-table-column label="成单数" align="center">
              <el-table-column prop="customerOrderNum" label="本人成单" align="center"/>
              <el-table-column prop="agentCustomerOrderNum" label="代理成单" align="center"/>
            </el-table-column>
            <el-table-column label="销售额" align="center">
              <el-table-column prop="mySale" label="本人销售额/获得提成" align="center">
                <template slot-scope='scope'>
                  <span>{{scope.row.customerMoney}} / {{scope.row.customerMoneyReward}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="agentSale" label="代理销售额/获得提成" align="center">
                <template slot-scope='scope'>
                  <span>{{scope.row.agentCustomerMoney}} / {{scope.row.agentCustomerMoneyReward}}</span>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="收益金额" align="center">
              <el-table-column prop="alreadyMoney" label="已发金额" align="center"/>
              <el-table-column prop="noSendMoney" label="未发金额" align="center"/>
            </el-table-column>
            <el-table-column label="状态" align="center">
              <template slot-scope="scope">
                <el-tag v-if='scope.row.delStatus === 0'>启用</el-tag>
                <el-tag v-if='scope.row.delStatus === 1'>已禁用</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-row>
                  <el-button type="text" @click="goDetail(scope.row)">查看</el-button>
                  <el-button type="text" @click='doReward(scope.row)'>发放金额</el-button>
                  <el-popover v-model="scope.row.visible" placement="bottom" trigger="click" width="100">
                    <div><el-button type="text" @click="getLists(scope.row)">发放记录</el-button></div>
                    <div><el-button type="text" v-if='scope.row.delStatus === 0' @click="startOrBan(scope.row, 1)">禁用</el-button></div>
                    <el-button type="text" v-if='scope.row.delStatus === 1' @click="startOrBan(scope.row, 0)">启用</el-button>
                    <el-button slot="reference" type="text" @click="getMore(scope.row)">更多</el-button>
                  </el-popover>
                </el-row>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="县级代理" name="2">
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="id" label="ID" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="手机号"/>
            <el-table-column label="所在地区">
               <template slot-scope='scope'>
                <span>{{scope.row.province}}{{scope.row.city}}{{scope.row.county}}</span>
              </template>
            </el-table-column>
            <el-table-column label="注册时间">
              <template slot-scope="scope">
                <span>{{$formatDate(scope.row.createTime)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="customerNum" label="客户数"/>
            <el-table-column prop="customerOrderNum" label="成单数"/>
            <el-table-column label="销售额/获得提成">
              <template slot-scope='scope'>
                  <span>{{scope.row.customerMoney}} / {{scope.row.customerMoneyReward}}</span>
                </template>
            </el-table-column>
            <el-table-column label="收益金额">
              <el-table-column prop="alreadyMoney" label="已发金额"/>
              <el-table-column prop="noSendMoney" label="未发金额"/>
            </el-table-column>
            <el-table-column label="状态">
              <template slot-scope="scope">
                <el-tag v-if='scope.row.delStatus === 0'>启用</el-tag>
                <el-tag v-if='scope.row.delStatus === 1'>已禁用</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-row>
                  <el-button type="text" @click="goDetail(scope.row)">查看</el-button>
                  <el-button type="text" @click='doReward(scope.row)'>发放金额</el-button>
                  <el-popover v-model="scope.row.visible" placement="bottom" trigger="click" width="100">
                    <div><el-button type="text" @click="getLists(scope.row)">发放记录</el-button></div>
                    <div><el-button type="text" v-if='scope.row.delStatus === 0' @click="startOrBan(scope.row, 1)">禁用</el-button></div>
                    <el-button type="text" v-if='scope.row.delStatus === 1' @click="startOrBan(scope.row, 0)">启用</el-button>
                    <el-button slot="reference" type="text" @click="getMore(scope.row)">更多</el-button>
                  </el-popover>
                </el-row>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
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
    <el-dialog title="发放记录" :visible.sync="rewardVisible">
      <div class="record-box">
        <div class="title">发放记录</div>
        <ul class="list">
          <li class="item" v-for="(item, index) in rewardList" :key="index">
            <img src="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15390565468631539056546863.png">
            <div class="info">
              <p class="name">{{item.selfName}}</p>
              <p class="time">{{$formatDate(item.createTime)}}</p>
              <p class="sum">发放金额：{{item.rewardMoney}}元</p>
            </div>
          </li>
        </ul>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { pca, pcaa } from 'area-data'
import {customerList, rewardList} from '@/api/manage'
import {doOrderReward, stopCustomer, rewardAll} from '@/api/audit'
export default {
  name: 'Index',
  data() {
    return {
      agentForm: {
        name: '',
        phone: '',
        startTime: '',
        endTime: '',
        region: [],
        time: ''
      },
      pcaa: pcaa,
      tableData: [],
      cityTableData: [
        { id: 1, name: 'hhh', phone: 123455, address: '杭州', regTime: '2018/09/20', myCustomer: 150, agentCustomer: 100, myLists: 100, agentLists: 100, mySale: 100, agentSale: 100, grant: 100, unGrant: 100 },
        { id: 1, name: 'hhh', phone: 123455, address: '杭州', regTime: '2018/09/20', myCustomer: 150, agentCustomer: 100, myLists: 100, agentLists: 100, mySale: 100, agentSale: 100, grant: 100, unGrant: 100 }
      ],
      countyTableData: [
        { id: 1, name: 'hhh', phone: 123455, address: '杭州', regTime: '2018/09/20', clients: 100, lists: 100, grant: 100, unGrant: 100 }
      ],
      activeName: '1', // tab默认选中
      showGetMore: false,
      currentPage: 1,
      pageSize: 10,
      totalCount: null,
      modalState:false,
      type: 1,
      rewardVisible: false,
      rewardList: []
    }
  },
  created() {
    this.init()
  },
  methods: {
    init(){
      customerList({
        name: this.agentForm.name,
        phone: this.agentForm.phone,
        type: this.type, // 1 市代理 2 县代理
        province: this.agentForm.region[0],
        city: this.agentForm.region[1],
        county: this.agentForm.region[2],
        startTime: this.agentForm.time[0],
        endTime: this.agentForm.time[1],
        pageSize: this.pageSize,
        pageNum: this.currentPage,
      }, res=>{
        if(res.code === 200) {
          this.tableData = res.data.list
          this.tableData.forEach((element) => {
            element.visible = false
          })
          console.log(this.tableData, 'this.tableData')
          this.totalCount = res.data.total
          this.pageSize = res.data.pageSize
        }
      })
    },
    query() { // 搜索
      this.pageSize = 10
      this.init()
    },
    clickTab(tab, event) {
      console.log(tab, event)
      this.type = Number(tab.index) + 1
      this.pageSize = 10
      this.init()
    },
    getMore(row) {
      // console.log(row)
      // this.showGetMore = true
    },
    getLists(row) {
      this.rewardVisible = !this.rewardVisible
      rewardList({
        pageNum: 0,
        pageSize: 10,
        userId: row.userId,
        isGrants: 1
      }, res=>{
        if(res.code === 200) {
          this.rewardList = res.data.list
        }
      })
    },
    // handleCurrentChange(val) { // 点击表格行
    //   console.log(val)
    // },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 0
      this.init()
    },
    handleCurrentChange(val) {
      console.log(val)
      this.currentPage = val
      this.init()
    },
    getRegion(val){
      if(val[1] ==  "市辖区"){
        val[1] = val[0]
      }
      console.log(val,'省市区')
      this.agentForm.region = val
    },
    goDetail(row) { // 代理商详情
      this.$router.push({
        path: '/agent-system/agent-detail',
        query:{
          data: JSON.stringify(row)
        }
      })
    },
    startOrBan(row, val) { // 启用禁用
      this.$confirm(`是否${val == 0 ? '启用' : '禁用'}此用户`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        stopCustomer({
          userId: row.userId,
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
    },
    doReward(row){
      if(row.noSendMoney > 0) {
        this.$confirm(`是否发放${row.noSendMoney}元收益金额`, '发放收益金额', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          rewardAll({
            userId: row.userId
          }, res=>{
            if(res.code === 200) {
              this.$message({
                type: 'success',
                message: '发放成功!'
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
      }else{
        this.$message({
          type: 'warning',
          message: '无收益金额发放'
        });
      }


    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "./index.scss";
</style>
