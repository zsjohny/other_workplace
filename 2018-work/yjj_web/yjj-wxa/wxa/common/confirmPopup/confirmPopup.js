
/**
 * 通用确定框组件
 * @date 2018-11-07
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
    //提示框icon显示
    iconClassName: {    // 属性名
      type: String,    // 类型（必填），目前接受的类型包括：String, Number, Boolean, Object, Array, null（表示任意类型）
      value: ''        // 属性初始值（可选），如果未指定则会根据类型选择一个
    },
    //确定框按钮文字
    confirmText: { 
      type: String,   
      value: '确定'     
    },
    //取消框按钮文字
    cancelText: {     
      type: String,   
      value: '取消'  
    }
  },
  /**
   * 私有数据,组件的初始数据
   * 可用于模版渲染
   */
  data: { 
    isShow: false      // 提示框显示控制
  },
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
    _confirmEvent(){
      ///触发确定回调
      this.triggerEvent("confirmEvent")
    }
  }
})