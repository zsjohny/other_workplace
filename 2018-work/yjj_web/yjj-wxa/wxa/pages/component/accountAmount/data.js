//金币和现金收支明细筛选的数据
var typeData = [
  {
    "name": "全部",
    "code": ""
  },
  {
    "name": "收入",
    "code": "1"
  },
  {
    "name": "支出",
    "code": "2"
  }
];
var stateData = [
  {
    "name": "全部",
    "code": ""
  },
  {
    "name": "待结算",
    "code": "1"
  },
  {
    "name": "已结算",
    "code": "2"
  }
];
//现金的种类
var kinCashData = [
  {
    "name": "全部",
    "code": ""
  },
  {
    "name": "订单佣金",
    "code": "0"
  },
  {
    "name": "管理奖金",
    "code": "1"
  },
  {
    "name": "提现",
    "code": "2"
  },
  {
    "name": "分享商品",
    "code": "40"
  }
];
//金币的种类
var kindCoinData = [
  {
    "name": "全部",
    "code": ""
  },
  {
    "name": "订单佣金",
    "code": "0"
  },
  {
    "name": "管理奖金",
    "code": "1"
  },
  {
    "name": "签到",
    "code": "3"
  },
  {
    "name": "分享商品",
    "code": "40"
  }
];
module.exports = {
  typeData: typeData,
  stateData: stateData,
  kinCashData: kinCashData,
  kindCoinData: kindCoinData
}