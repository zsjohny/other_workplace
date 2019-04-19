//时间戳转换成时间
export const formatDate = (now, callback) => {
  now=new Date(parseInt(now));
  var year=now.getFullYear();
  var month=now.getMonth()+1;
  var date=now.getDate();
  var hour=now.getHours();
  var minute=now.getMinutes();
  var second=now.getSeconds();
  if(second <= 9){
    second = "0"+second
  }
  if(minute <= 9){
    minute = "0"+minute
  }
  if(hour <= 9){
    hour = "0"+hour
  }
  return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
}

/*
* 获取当前页面的URL 对其带的参数进行处理
*/
export const getUrlPara = (para, callback) => {
  var paraArr=location.search.substring(1).split("&");
  for ( var i=0; i<paraArr.length; i++ ) {
    if ( para==paraArr[i].split('=')[0] ) {
      return paraArr[i].split('=')[1]
    }
  }
  return '';
}
