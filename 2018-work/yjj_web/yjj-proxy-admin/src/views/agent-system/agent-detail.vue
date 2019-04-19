<template>
    <el-container>
      <el-header>
        <el-row :gutter="20">
          <div class="detail-head">
            <img class="user-head" src="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15390565468631539056546863.png" alt="">
            <dl class="user-info">
              <dt>{{info.name}}<span class="identity-icon" v-if='info.type == 1'>市</span><span v-if='info.type == 2' class="identity-icon">县</span></dt>
              <dd>
                <p><span>手机号: {{info.phone}}</span> <span>身份证号: {{info.idCardNo}}</span></p>
                <p>地址: {{info.province}}{{info.city}}{{info.county}}{{info.addressDetail}}</p>
              </dd>
            </dl>
            <div class="box-card box-card1">
              <div class="header">
                <p class="number">{{rewardData.allReward}}</p>
                <p class="text">总收益</p>
              </div>
              <div class="item">
                <span>已发: {{rewardData.arealdyReward}}</span>
                <span>未发: {{rewardData.noReward}}</span>
              </div>
            </div>
            <div class="box-card box-card2">
              <div class="header">
                <p class="number">{{rewardData.noReward}}</p>
                <p class="text">未发金额</p>
              </div>
              <div class="item">
                  <span @click="doAllReward">发放金额</span>
                  <span @click="getLists">发放记录</span>
              </div>
            </div>
          </div>
          <!--  detail-head end -->
        </el-row>
      </el-header>
      <el-main class="main">
        <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
          <el-tab-pane label="我的客户" name="1">
            <!--<el-row :gutter="10">-->
              <!--<el-col :span="4">-->
                <!--<el-card :body-style="{ padding: '0px' }">-->
                  <!--<div class="card-mod">-->
                    <!--<span>123154812</span>-->
                    <!--<div class="text">-->
                     <!--今日新增代理-->
                    <!--</div>-->
                  <!--</div>-->
                <!--</el-card>-->
              <!--</el-col>-->
              <!--<el-col :span="4">-->
                <!--<el-card :body-style="{ padding: '0px' }">-->
                  <!--<div class="card-mod card-mod1">-->
                    <!--<span>418515151</span>-->
                    <!--<div class="text">-->
                      <!--代理总数-->
                    <!--</div>-->
                  <!--</div>-->
                <!--</el-card>-->
              <!--</el-col>-->
            <!--</el-row>-->
            <!--<el-form ref="agentForm" :model="agentForm" class="queryWrap">-->
              <!--<el-col :span="10">-->
                <!--<el-form-item label="活动名称" label-width="80px"><el-input v-model="agentForm.name"/></el-form-item>-->
              <!--</el-col>-->
              <!--<el-col :span="4">-->
                <!--<el-form-item label-width="30px"><el-button type="primary">搜索</el-button></el-form-item>-->
              <!--</el-col>-->
            <!--</el-form>-->
            <el-table :data="tableData" stripe style="width: 100%">
              <el-table-column prop="wxName" label="昵称" align="center"/>
              <el-table-column prop="phone" label="手机号" align="center"/>
              <el-table-column prop="createTimeReadable" label="注册时间" align="center"/>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click='unBindAgent(scope.row)'>解绑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="我的代理" name="2" v-if='agentType == 1'>
            <el-table :data="tableData" stripe style="width: 100%">
              <el-table-column prop="userName" label="姓名" align="center"/>
              <el-table-column prop="phone" label="手机号" align="center"/>
              <el-table-column prop="idCardNo" label="身份证号" align="center"/>
              <el-table-column label="地址" align="center">
                <template slot-scope='scope'>
                  <span>{{scope.row.province}}{{scope.row.city}}{{scope.row.county}}{{scope.row.addressDetail}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="createTimeReadable" label="注册时间" align="center"/>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click='unBindAgent(scope.row)'>解绑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="客户订单" name="3">
            <el-table :data="orderTableData" stripe style="width: 100%">
              <el-table-column prop="orderNo" label="订单编号" align="center"/>
              <el-table-column prop="goodsName" label="商品名称" align="center"/>
              <el-table-column prop="goodsPrice" label="金额" align="center"/>
              <el-table-column label="购买年限" align="center">
                 <template slot-scope='scope'>
                  <span>{{scope.row.goodsTimeNum}} 年</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" align="center">
                <template slot-scope='scope'>
                  <el-tag v-if='scope.row.status === 0'>处理中</el-tag>
                  <el-tag v-if='scope.row.status === 1'>已支付</el-tag>
                  <el-tag v-if='scope.row.status === 2'>支付失败</el-tag>
                  <!--<el-tag v-if='scope.row.status === 0'>处理中</el-tag>-->
                </template>
              </el-table-column>
              <el-table-column label="支付方式" align="center">
                <template slot-scope='scope'>
                  <span v-if='scope.row.source == 1'>微信支付</span>
                </template>
              </el-table-column>
              <el-table-column v-if='agentType == 1' prop="oneLevelName" label="所属县代理" align="center"/>
              <el-table-column prop="selfName" label="下单客户" align="center"/>
              <el-table-column prop="idCardNo" label="下单平台" align="center">
                <template slot-scope='scope'>
                  <span v-if='scope.row.source == 1'>俞姐姐公众号</span>
                </template>
              </el-table-column>
              <el-table-column label="下单时间" align="center">
                <template slot-scope="scope">
                  <span>{{$formatDate(scope.row.createTime)}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click='goOrderDetail(scope.row)'>查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="代理订单" name="4" v-if='agentType == 1'>
            <el-table :data="orderTableData" stripe style="width: 100%">
              <el-table-column prop="orderNo" label="订单编号" align="center"/>
              <el-table-column prop="goodsName" label="商品名称" align="center"/>
              <el-table-column prop="goodsPrice" label="金额" align="center"/>
              <el-table-column label="购买年限" align="center">
                 <template slot-scope='scope'>
                  <span>{{scope.row.goodsTimeNum}} 年</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" align="center">
                <template slot-scope='scope'>
                  <el-tag v-if='scope.row.status === 0'>处理中</el-tag>
                  <el-tag v-if='scope.row.status === 1'>已支付</el-tag>
                  <el-tag v-if='scope.row.status === 0'>支付失败</el-tag>
                  <el-tag v-if='scope.row.status === 0'>处理中</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="支付方式" align="center">
                  <template slot-scope='scope'>
                  <span v-if='scope.row.source == 1'>微信支付</span>
                </template>
              </el-table-column>
              <el-table-column prop="selfName" label="下单客户" align="center"/>
              <el-table-column label="下单平台" align="center">
                 <template slot-scope='scope'>
                  <span v-if='scope.row.source == 1'>俞姐姐公众号</span>
                </template>
              </el-table-column>
              <el-table-column label="下单时间" align="center">
                <template slot-scope="scope">
                  <span>{{$formatDate(scope.row.createTime)}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click='goOrderDetail(scope.row)'>查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="个人订单" name="5">
            <el-table :data="orderTableData" stripe style="width: 100%">
              <el-table-column prop="orderNo" label="订单编号" align="center"/>
              <el-table-column prop="goodsName" label="商品名称" align="center"/>
              <el-table-column prop="goodsPrice" label="金额" align="center"/>
              <el-table-column label="购买年限" align="center">
                 <template slot-scope='scope'>
                  <span>{{scope.row.goodsTimeNum}} 年</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" align="center">
                <template slot-scope='scope'>
                  <el-tag v-if='scope.row.status === 0'>处理中</el-tag>
                  <el-tag v-if='scope.row.status === 1'>已支付</el-tag>
                  <el-tag v-if='scope.row.status === 2'>支付失败</el-tag>
                  <!-- <el-tag v-if='scope.row.status === 0'>处理中</el-tag> -->
                </template>
              </el-table-column>
              <el-table-column label="支付方式" align="center">
                  <template slot-scope='scope'>
                  <span v-if='scope.row.source == 1'>微信支付</span>
                </template>
              </el-table-column>
              <el-table-column label="下单平台" align="center">
                 <template slot-scope='scope'>
                  <span v-if='scope.row.source == 1'>俞姐姐公众号</span>
                </template>
              </el-table-column>
              <el-table-column label="下单时间" align="center">
                <template slot-scope="scope">
                  <span>{{$formatDate(scope.row.createTime)}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click='goOrderDetail(scope.row)'>查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="个人收入" name="6">
            <el-table :data="personalRewardData" stripe style="width: 100%">
              <el-table-column prop="payOrderNo" label="交易流水号" align="center"/>
              <el-table-column prop="orderNo" label="订单编号" align="center"/>
              <el-table-column prop="goodsName" label="商品名称" align="center"/>
              <el-table-column prop="orderMoney" label="金额" align="center"/>
              <el-table-column prop="rewardMoney" label="收益金额" align="center"/>
              <el-table-column prop="selfName" label="下单客户" align="center"/>
              <el-table-column label="下单时间" align="center">
                <template slot-scope="scope">
                  <span>{{$formatDate(scope.row.createTime)}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button v-if='scope.row.isGrant == 1'>收益金额已发放</el-button>
                  <el-button @click="doReward(scope.row)" v-if='scope.row.isGrant == 0'>发放收益金额</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-main>
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
     <el-pagination
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      :total="totalCount"
      class="pagination"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"/>
    </el-container>
</template>
<script>
  import {CustomerDetail, order, rewardInfo, personalReward, rewardList} from '@/api/manage'
  import {unbind , doOrderReward, rewardAll } from '@/api/audit'
export default {
  name: 'AgentDetail',
  data() {
    return {
      activeName: '1',
      agentForm: {
        name: ''
      },
      agentTableData: [
        { name: 'shdg', phone: 1222222, idNumber: 2132323232323, address: '看上方法改好了', loginTime: '2018/09/09' }
      ],
      tableData: [],
      orderTableData: [],
      personalRewardData: [],
      type: 1,
      pageSize: 10,
      currentPage: 0,
      totalCount: null,
      item: {},
      info: {},
      rewardData: {},
      agentType: 1, // 1 市代理 2 县代理
      rewardVisible: false,
      rewardList: []
    }
  },
  created() {
    if(this.$route.query.data) {
      console.log(this.$route.query.data, 'this.$route.query.data')
      this.item = JSON.parse(this.$route.query.data)
      this.agentType = this.item.type
    }
    this.init()
    this.getReward()
  },
  methods: {
    init(){
      CustomerDetail({
        publicAccountUserId: this.item.userId,
        phoneOrName: this.agentForm.name,
        type: this.type,
        pageSize: this.pageSize,
        pageNumber: this.currentPage
      }, res=>{
        if(res.code === 200) {
          this.totalCount = res.data.userList.total
          this.pageSize = res.data.userList.pageSize
          this.info = res.data.proxyCustomer
          this.tableData = res.data.userList.list
        }else{
           this.$message({
            type: 'error',
            message: res.data
          });
        }
      })
    },
    initOrder(){
      order({
        pageSize: this.pageSize,
        pageNum: this.currentPage,
        type: this.type,
        userId: this.item.userId
      }, res=>{
        if(res.code === 200) {
          this.orderTableData = res.data.list
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
    getReward(){
      rewardInfo({
        userId: this.item.userId
      }, res=>{
        if(res.code === 200) {
          this.rewardData = res.data
        }else{
          this.$message({
            type: 'error',
            message: res.data
          });
        }
      })
    },
    getPersonalReward(){
      personalReward({
        pageSize: this.pageSize,
        pageNum: this.currentPage,
        userId: this.item.userId
      }, res=>{
        if(res.code === 200) {
          this.personalRewardData = res.data.list
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
    handleClick(tab, event) {
      console.log(tab.index, event)
      this.pageSize = 10
      if(this.agentType == 1) { //市代理
        if(tab.index < 2) {
          this.type = Number(tab.index) + 1
          this.init()
        }else if(tab.index > 4){
          this.getPersonalReward()
        }else{
          this.type = Number(tab.index) - 1
          this.initOrder()
        }
      }else{ // 县代理
        if(tab.index == 0) {
          this.type = 1
          this.init()
        }else if(tab.index == 3) {
          this.getPersonalReward()
        }else if(tab.index == 1){
          this.type = 1
          this.initOrder()
        }else{
          this.type = 3
          this.initOrder()
        }
      }

    },
    goOrderDetail(row) {
      this.$router.push({
        path: '/order/detail',
        query: {
          data: row
        }
      })
    },
     handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 0
      this.init()
      this.getPersonalReward()
      this.initOrder()
    },
    handleCurrentChange(val) {
      console.log(val)
      this.currentPage = val
      this.init()
      this.getPersonalReward()
      this.initOrder()
    },
    unBindAgent(row) {
      unbind({
        refereeId: row.refereeId
      }, res=>{
        if(res.code === 200) {
          this.$message({
            type: 'success',
            message: '解绑成功'
          });
          this.init()
        }else{
           this.$message({
            type: 'error',
            message: res.data
          });
        }
      })
    },
    doAllReward() {  // 发放总金额
      if(this.rewardData.noReward > 0) {
        this.$confirm(`是否发放${this.rewardData.noReward}元收益金额`, '发放收益金额', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          rewardAll({
            userId: this.info.userId
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
    },
    doReward(val){
      if(val.rewardMoney > 0) {
        this.$confirm(`是否发放${val.rewardMoney}元收益金额`, '发放收益金额', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          doOrderReward({
            id: val.id
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

    },
    getLists() { // 发放记录
      this.rewardVisible = !this.rewardVisible
      rewardList({
        pageNum: 0,
        pageSize: 10,
        userId: this.item.userId,
        isGrants: 1
      }, res=>{
        if(res.code === 200) {
          this.rewardList = res.data.list
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "src/styles/detail.scss";
  .main{
    margin-top: 100px;
    .queryWrap{
      margin-top: 30px;
    }
  }
  .pagination{
    margin-left: 20px;
  }
</style>
