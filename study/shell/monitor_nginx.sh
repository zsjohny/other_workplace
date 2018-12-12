#jar的lvs的自我检查脚本
#! /bin/bash

pid=`ps -ef|grep test|grep -v grep |awk '{print $2}'`

if [  "${#pid}" != 0 ]; then
  echo -e "\033[32m running \033[0m"
else
    `nohup java -jar /stock/tes*.jar >/dev/null 2>&1 &`
    if [ $? = 0 ];then 
      echo "restart ok !!"
    else 
      pid=`ps -ef|grep keepalived|grep -v grep |awk 'NR==1{print $2}'`  
      `kill -9 $pid`
    fi  
      
   
fi
     