
/**
 * 保存相册授权弹窗组件
 * @date 2018-08-31
 */
Component({
  options: {
    multipleSlots: true // 在组件定义时的选项中启用多slot支持
  },
  /**
   * 组件的属性列表 
   * 用于组件自定义设置
   */
  properties: {
    // 提示框标题显示
    title: {            // 属性名
      type: String,     // 类型（必填），目前接受的类型包括：String, Number, Boolean, Object, Array, null（表示任意类型）
      value: ''     // 属性初始值（可选），如果未指定则会根据类型选择一个
    },
    // 提示框内容显示
    // isShow :{
    //   type: String ,
    //   value : '',
    //   observer: function (newVal, oldVal, changedPath) {
    //     // 属性被改变时执行的函数（可选），也可以写成在methods段中定义的方法名字符串, 如：'_propertyChange'
    //     // 通常 newVal 就是新设置的数据， oldVal 是旧数据
    //     console.log("newVal  oldVal", newVal,oldVal);
    //     //this.controlPopup()
    //   }
    // }
  },
  /**
   * 私有数据,组件的初始数据
   * 可用于模版渲染
   */
  data: {
    // 提示框显示控制
    isShow:false
  },

  /**
   * 组件的方法列表
   * 更新属性和数据的方法与更新页面数据的方法类似
   */
  methods: {
    /*
     * 公有方法
     */
    //隐藏提示框
    hidePopup(){
      this.setData({
        isShow: false
      })
    },
    //展示提示框
    showPopup(){
      this.setData({
        isShow: true
      })
    },
     /*
     * 内部私有方法建议以下划线开头
     * triggerEvent 用于触发事件
     */
    _cancelEvent(){
      //触发取消回调
      this.triggerEvent("cancelEvent")
    },
    _settingCallback(){
      //回调函数
      this.setData({
        isShow: false
      })
    }
  }
})