#!/bin/bash 
#先下载代码
exportpath="/var/lib/jenkins/workspace/etongjin_activity_dev"
branch_name="miscroservice_activity"
auth="nessary:Pj879227577"

if [[  -d $exportpath ]];then
  `sudo rm -rf  $exportpath/*`

fi

#下载依赖包

`git clone -b master https://$auth@gitee.com/nessary/etongjin.git $exportpath/master`

`sudo mv $exportpath/master/*  $exportpath/ && rm -rf $exportpath/master`

#除去其他依赖
`echo -e  "rootProject.name = 'finace_miscroservice' \n include 'miscroservice_commons' \n include '$branch_name'" >$exportpath/settings.gradle`


`git clone -b  miscroservice_commons https://$auth@gitee.com/nessary/etongjin.git $exportpath/miscroservice_commons`


`git clone -b $branch_name https://$auth@gitee.com/nessary/etongjin.git $exportpath/$branch_name`

#然后gradle 打包
`sudo /opt/gradle-2.10/bin/gradle  -b $exportpath/build.gradle build -x test`


server_list="101.37.151.54 120.27.222.192"
for server in $server_list 
do 

#发布到各自所在服务
`sudo scp -r $exportpath/$branch_name/build/libs/m*.jar root@$server:/tmp/$branch_name.jar` 

#关闭服务
port=`sudo ssh root@$server ps -ef|grep $branch_name|grep -v grep|awk '{print $2}'|head -n 1`

if [[ ${#port} -gt 1 ]];then
  `sudo ssh root@$server kill  -9 $port`
fi   


pid=`sudo ssh root@$server jps -l|grep $branch_name|awk '{print $1}'`

if [[ $pid -ne ''  ]];then
 `sudo ssh root@$server kill  -9 $pid`

fi

#启动服务
`sudo ssh root@$server "nohup /stock/java/bin/java -jar  -Xms216m -Xmx512m  /tmp/$branch_name.jar  --spring.profiles.active=test >/stock/log/$branch_name.log 2>&1 &"`


done 


