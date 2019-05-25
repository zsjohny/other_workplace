export default {
  install(Vue){
    Vue.prototype.doregEXP = function (val , regEXP) {
      return regEXP.test(val)
    }
    Vue.prototype.regEXP = {
      phone_reg: /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/,
      password_reg: /^[A-Za-z0-9]+$/,
      only_number: /^\d+$/,
      only_word: /^[a-zA-Z]+$/,
      isName: /[\u4E00-\u9FA5]/,
      isSecondId: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
      isFirstId: /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/
    }
  }
}