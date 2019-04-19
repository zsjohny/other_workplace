<template>
  <div class="app-container">
    <el-form ref="orderForm" :model="orderForm" :inline="true">
      <el-form-item label="订单编号">
        <el-input v-model="orderForm.orderNo" placeholder="请输入订单编号"/>
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input v-model="orderForm.goodsName"/>
      </el-form-item>
      <el-form-item label="所属人">
        <el-input v-model="orderForm.selfName"/>
      </el-form-item>
      <el-form-item label="所属市代理">
        <el-input v-model="orderForm.twoLevelName"/>
      </el-form-item>
      <el-form-item label="所属县代理">
        <el-input v-model="orderForm.oneLevelName"/>
      </el-form-item>
      <el-form-item label="金额: 从" label-width="80px">
        <el-input v-model="orderForm.minMoney"/>
      </el-form-item>
      <el-form-item label="到" label-width="30px">
        <el-input v-model="orderForm.maxMoney"/>
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker
          v-model="orderForm.time"
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
    </el-form>
    <el-table :data="orderTableData" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" align="center"/>
      <el-table-column prop="goodsName" label="商品名称" align="center"/>
      <el-table-column prop="goodsPrice" label="金额（元）" align="center"/>
      <el-table-column prop="selfName" label="所属人" align="center"/>
      <el-table-column label="所属县代理" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.selfRole == 2">{{scope.row.oneLevelName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属市代理" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.selfRole == 1">{{scope.row.oneLevelName}}</span>
          <span v-else>{{scope.row.twoLevelName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="购买年限" align="center">
        <template slot-scope='scope'>
          <span>{{scope.row.goodsTimeNum}}年</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope='scope'>
          <el-tag v-if='scope.row.status === 0'>处理中</el-tag>
          <el-tag v-if='scope.row.status === 1'>已支付</el-tag>
          <el-tag v-if='scope.row.status === 2'>支付失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center">
        <template slot-scope='scope'>
          <span v-if='scope.row.payWay == 1'>微信支付</span>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center">
        <template slot-scope="scope">
          <span>{{$formatDate(scope.row.createTime)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="下单平台" align="center">
        <template slot-scope='scope'>
          <span v-if='scope.row.source == 1'>俞姐姐公众号</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" @click="goDetail(scope.row)">查看</el-button>
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
  import {orderList} from '@/api/manage'
export default {
  name: 'Index',
  data() {
    return {
      orderForm: {
        orderNo: '',
        goodsName: '',
        selfName: '',
        oneLevelName: '',
        twoLevelName: '',
        minMoney: '',
        maxMoney: '',
        time: []
      },
      orderTableData: [],
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
      orderList({
        pageSize: this.pageSize,
        pageNum: this.currentPage,
        orderNo: this.orderForm.orderNo,
        goodsName: this.orderForm.goodsName,
        selfName: this.orderForm.selfName,
        oneLevelName: this.orderForm.oneLevelName,
        twoLevelName: this.orderForm.twoLevelName,
        minMoney: this.orderForm.minMoney,
        maxMoney: this.orderForm.maxMoney,
        startTime: this.orderForm.time[0],
        endTime: this.orderForm.time[1]
      }, res=>{
        if(res.code === 200) {
          this.orderTableData = res.data.list
          this.totalCount = res.data.total
          this.pageSize = res.data.pageSize
        }
      })
    },
    query() {
      this.pageSize = 10
      this.init()
    },
    reset(){
      this.orderForm.time = []
      this.orderForm.orderNo = ''
      this.orderForm.goodsName = ''
      this.orderForm.selfName = ''
      this.orderForm.oneLevelName = ''
      this.orderForm.twoLevelName = ''
      this.orderForm.minMoney = ''
      this.orderForm.maxMoney = ''
      this.pageSize = 10
      this.init()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 0
      this.init()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.init()
    },
    goDetail(row) {
      this.$router.push({
        path: '/order/detail',
        query: {
          data: row
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">

</style>
