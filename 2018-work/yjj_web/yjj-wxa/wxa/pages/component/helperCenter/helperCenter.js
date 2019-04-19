
var app = getApp()
var constant = require('../../../constant')
var dialog = require("../../../utils/dialog")
Page({
  data: {
    hotQuestions: [], // 热门问题
    typeQuestions: [], // 分类问题
    tabState: 1,
    inputShow: false, // 输入显示取消按钮
    keywordList: [],
    getSearchData: false,
    pageNumber: 1, // 分类问题分页
    searchPageNum: 1, // 搜索分页
    noQuestion: false,
    scrollLeft: 0, // tab标题的滚动条位置
    scrollX: true
  },
  onLoad: function () {
    var searchArr = wx.getStorageSync('QaSearchInfo') || [],
      newSearchArr = Array.from(new Set(searchArr))
    this.setData({
      keywordList: newSearchArr.reverse()
    })
  },
  onShow: function () {
    this.initQuestion()
    this.initQaType()
    dialog.loading();
    // setTimeout(function () {
    //   dialog.hide()
    // }, 1000)
  },
  // 获取热门问题
  initQuestion:function() {
    var param = {
      data: {}
    }
    app.publicApi.getHotQuestion(param).then(res => {
      console.log(res)
      if(res.code === 200) {
        this.setData({
          hotQuestions: res.data
        })
        dialog.hide()
      }else{

      }
    })
  },
  // 问题分类
  initQaType: function() {
    var param = {
      data: {}
    }
    app.publicApi.getQaType(param).then(res => {
      console.log(res)
      if (res.code === 200) {
        this.setData({
          typeList: res.data
        })
        this.initQuestionList(res.data[0].id)
      } else {

      }
    })
  },
  // 分类问题列表
  initQuestionList: function(tab) {
    var param = {
      data: {
        type: tab,
        pageNumber: this.data.pageNumber
      }
    }
    app.publicApi.getQuestionList(param).then(res => {
      console.log(res)
      if (res.code === 200) {
        this.setData({
          loadingMore: false
        })
        dialog.hide();
        if(res.data.length > 0) {
          if (this.data.pageNumber == 1) {
            this.setData({
              typeQuestions: res.data
            })
          }else{
            if(res.data.length > 0) {
              var typeQuestions = this.data.typeQuestions,
                  newArr = [...typeQuestions, ...res.data];
              this.setData({
                typeQuestions: newArr
              })
            }
          }
          console.log("更多后typeQuestions", this.data.typeQuestions);
        }else{
          if (this.data.pageNumber > 1) {
            // this.setData({
            //   noQuestion: true
            // })
          }
        }
      } else {

      }
    })
  },
  questionSearch: function (question) {
    var param = {
      data: {
        question: question,
        pageNumber: this.data.searchPageNum
      }
    }
    app.publicApi.questionSearch(param).then(res => {
      console.log(res)
      if (res.code === 200) {
        this.setData({
          loadingMore: false
        })
        if(res.data.length > 0) {
          if (this.data.searchPageNum == 1){
            this.setData({
              getSearchData: true,
              searchList: res.data
            })
          }else{
            if (res.data.length > 0) {
              var searchList = this.data.searchList,
                newArr = [...searchList, ...res.data];
              this.setData({
                getSearchData: true,
                searchList: newArr
              })
            }
          }
          
        }else{
          wx.showToast({
            title: '暂无相关问题',
            icon: 'none',
            duration: 1000,
          })
        }
      } else {

      }
    })
  },
  tabFun: function(e) {
    dialog.loading();
    var tab = e.currentTarget.dataset.tab,
      index = e.currentTarget.dataset.index;
    this.setData({
      tabState: index + 1,
      typeQuestions: {},
      pageNumber: 1
    })
    this.initQuestionList(tab)
  },
  searchSeekInput: function(e) {
    console.log(e.detail.value)
    this.questionSearch(e.detail.value)
  },
  searchBlur: function(e){
    console.log(e.detail.value, 'e.detail.value');
    var searchArr = wx.getStorageSync('QaSearchInfo') || [],
        currentValue = e.detail.value;
    if (currentValue) {
      searchArr.push(currentValue)
    }
    wx.setStorageSync('QaSearchInfo', searchArr)
  },
  searchFocus: function(e) {
    this.setData({
      inputShow: true
    })
    var searchArr = wx.getStorageSync('QaSearchInfo') || [],
        newSearchArr = Array.from(new Set(searchArr))
    this.setData({
      keywordList: newSearchArr.reverse()
    })
    console.log(newSearchArr, 'searchArrsearchArr', this.data.inputShow)
  },
  // 取消
  cancel: function() {
    this.setData({
      inputShow: false,
      keyword: ''
    })
  },
  // 清楚搜索历史
  clearSearchList: function() {
      var that = this
      wx.clearStorage('QaSearchInfo')
      setTimeout(function () {
        var searchArr = wx.getStorageSync('QaSearchInfo') || []
        that.setData({
          keywordList: searchArr.reverse()
        })
        if (searchArr.length == 0) {
          wx.showToast({
            title: '清除成功',
            icon: 'success',
            duration: 1000,
          })
        }
      }, 1000)

  },
  // 跳转详情
  goDetail: function(e) {
    wx.navigateTo({
      url: "/pages/component/helperDetail/helperDetail?id=" + e.currentTarget.dataset.select
    })
  },
  // 分类问题加载
  onReachBottom: function() {
    this.setData({
      loadingMore: true
    })
    if (!this.data.noQuestion) {
      this.data.pageNumber++;
      console.log(this.data.pageNumber,'pageNumber')
      this.initQuestionList()
    }
    if (this.data.getSearchData) {
      this.data.searchPageNum++;
      this.questionSearch(this.data.keyWordSearch)
    }
    console.log("onBottom")
  },
  // 历史记录点击搜索
  keywordBtn: function(e) {
    this.setData({
      keyWordSearch: e.currentTarget.dataset.value
    })
   this.questionSearch(e.currentTarget.dataset.value)
  },
  touchstart: function (e) {
    console.log(e,'././././././.')
  },
  touchend: function(e) {
    console.log(e,',,,,,,,,,,,,,,')
  }
})
