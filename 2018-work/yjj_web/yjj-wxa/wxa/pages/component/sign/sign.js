var app = getApp()
var util = require('../../../utils/util.js')
// var constant = require('../../constant')
var dialog = require("../../../utils/dialog")
var id = wx.getStorageSync('id');
console.log(id, '获取本地id')
const date = new Date();
const day = date.getDate();

Page({
    data: {
        objectId: '',
        days:[],
        signUp:[],
        cur_year: 0,
        cur_month: 0,
        cur_day: 0,
        count: 0,
        weeks_ch: [],
        getPrize: 0,
        signedDays: 0,
        signCoin: 0,
        isSign: false, // 是否签到
        dayAward: [],
        conditionArray: [],
        showGiftWrap: false,
        giftCoin: 0,
        getGiftDate: [] // 可领取奖励日期
    },
    onLoad: function () {
        //获取当前年月
        const date = new Date();
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const weeks = ['日', '一', '二', '三', '四', '五', '六'];
        this.calculateEmptyGrids(year, month);
        this.calculateDays(year, month);
        this.setData({
            cur_year: year,
            cur_month: month,
            weeks_ch: weeks,
            cur_day: day
        })
        //获取当前用户当前任务的签到状态
        this.getSignUp();
    },
    // 签到信息
    getSignUp: function () {
        var param = {
            userId: id
        }
        app.distributionApi.signInit(param).then(res => {
            this.setData({
                getPrize: res.data.getPrize,
                signedDays: res.data.monthNumber,
                signCoin: res.data.signCoin,
                signUp: res.data.days, //已签到天数
                dayAward: res.data.dayAward, // 可领取奖励日期
                // conditionArray: res.data.conditionArray // 连续签到天数条件
            })
            res.data.days.forEach((element) => {
                if(this.data.cur_day == element) {
                    this.setData({
                        isSign: true
                    })
                }
            })
            this.onJudgeSign()
        })
    },
    // 点击签到
    goSign: function () {
        var that = this
        var param = {
            userId: id
        }
        app.distributionApi.sign(param).then(res => {
            if(res.code == 200) {
                wx.showToast({
                    title: '签到成功',
                    icon:'success',
                    duration: 2000,
                })
                setTimeout(()=>{
                    that.getSignUp()
                },2000)
            }else{
                dialog.toastError(res.msg)
            }

        })
    },
    calculateEmptyGrids: function (year, month) {
        var that = this;
        //计算每个月时要清零
        that.setData({
            days:[]
        });
        const firstDayOfWeek = this.getFirstDayOfWeek(year, month);
        if (firstDayOfWeek > 0) {
            for (let i = 0; i < firstDayOfWeek; i++) {
                var obj  = {
                    date:null,
                    isSign:false
                }
                that.data.days.push(obj);
            }
            this.setData({
                days:that.data.days
            });
            //清空
        } else {
            this.setData({
                days: []
            });
        }
    },
    // 绘制当月天数占的格子，并把它放到days数组中
    calculateDays:function(year, month) {
        var that = this;
        const thisMonthDays = this.getThisMonthDays(year, month);
        for (let i = 1; i <= thisMonthDays; i++) {
            var obj = {
                date: i,
                isSign: false
            }
            that.data.days.push(obj);
        }
        this.setData({
            days:that.data.days
        });
    },
    getFirstDayOfWeek:function(year, month) {
        return new Date(Date.UTC(year, month - 1, 1)).getDay();
    },
    getThisMonthDays:function(year, month){
        return new Date(year, month, 0).getDate()
    },
    onJudgeSign:function(){ //判断是否签到
        var that = this;
        var signs = that.data.signUp;
        var daysArr = that.data.days;
        console.log(signs, daysArr, '判断是否签到')
        for (var i=0; i < signs.length;i++){
            for (var j = 0; j < daysArr.length;j++){
                if (daysArr[j].date == signs[i]){
                    daysArr[j].isSign = true;
                }
            }
        }
        that.setData({days:daysArr});
    },
    getGift(e) { // 点击获取签到礼物
        var param = {
            data: {
                userId: id,
                num: e.currentTarget.dataset.select
            }
        }
        app.distributionApi.signGift(param).then(res => {
            if(res.code == 200) {
                this.setData({
                    showGiftWrap: true,
                    giftCoin: res.data
                })
            }else{
                dialog.toastError(res.msg)
            }

        })
    },
    closeGiftWrap() {
        this.setData({
            showGiftWrap: false
        })
        this.getSignUp()
    }
})
