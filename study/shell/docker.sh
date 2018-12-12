#!/bin/bash 






#参数列表
apply_list="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2"
exportpath="/tmp/docker_deploy"
auth="nessary:Pj879227577"

for apply in $apply_list 
do 
branch_name=$apply

if [[  -d $exportpath ]];then
`sudo rm -rf  $exportpath/*`
echo "delete success"

fi

#下载依赖包

`git clone -b master https://$auth@gitee.com/nessary/etongjin.git $exportpath/master`

`sudo mv $exportpath/master/*  $exportpath/ && rm -rf $exportpath/master`

#除去其他依赖
`echo -e  "rootProject.name = 'finace_miscroservice' \n include 'miscroservice_commons' \n include '$branch_name'" >$exportpath/settings.gradle`


`git clone -b  miscroservice_commons https://$auth@gitee.com/nessary/etongjin.git $exportpath/miscroservice_commons`


`git clone -b $branch_name https://$auth@gitee.com/nessary/etongjin.git $exportpath/$branch_name`

#更改dockerfile的环境
`mv $exportpath/$branch_name/Dockerfile-dev $exportpath/$branch_name/Dockerfile`

#执行打包
`sudo /opt/gradle-3.3/bin/gradle  -b $exportpath/build.gradle build -x test`

		
	#执行docker-compose
	#设置环境变量
server_list="101.37.151.54"

for address in $server_list 
     do 
			 
	 #设置docker环境变量
	 
	 #构建作业
	`export DOCKER_HOST="tcp://$address:3839" && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml build`

	 #停止作业
	`export DOCKER_HOST="tcp://$address:3839" && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml down`
	 #启动作业
	`export DOCKER_HOST="tcp://$address:3839" && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml up -d`
			
	done 

done 


#---------------------------------------------------------应用服务分割线


#参数列表
apply_list="miscroservice_config   miscroservice_authorize   miscroservice_task_scheduling miscroservice_user  miscroservice_borrow  miscroservice_activity miscroservice_getway"
exportpath="/tmp/docker_deploy"
auth="nessary:Pj879227577"

for apply in $apply_list 
do 
branch_name=$apply

if [[  -d $exportpath ]];then
`sudo rm -rf  $exportpath/*`
echo "delete success"

fi

#下载依赖包

`git clone -b master https://$auth@gitee.com/nessary/etongjin.git $exportpath/master`

`sudo mv $exportpath/master/*  $exportpath/ && rm -rf $exportpath/master`

#除去其他依赖
`echo -e  "rootProject.name = 'finace_miscroservice' \n include 'miscroservice_commons' \n include '$branch_name'" >$exportpath/settings.gradle`


`git clone -b  miscroservice_commons https://$auth@gitee.com/nessary/etongjin.git $exportpath/miscroservice_commons`


`git clone -b $branch_name https://$auth@gitee.com/nessary/etongjin.git $exportpath/$branch_name`

#更改dockerfile的环境
`mv $exportpath/$branch_name/Dockerfile-dev $exportpath/$branch_name/Dockerfile`

#执行打包
`sudo /opt/gradle-3.3/bin/gradle  -b $exportpath/build.gradle build -x test`

		
	#执行docker-compose
	#设置环境变量
server_list="101.37.151.54 120.27.222.192"

for address in $server_list 
     do 
			 
	 #设置docker环境变量
	 
	 #构建作业
	`export DOCKER_HOST="tcp://$address:3839" && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml build`


	 #停止作业
	`export DOCKER_HOST="tcp://$address:3839" && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml down`
	 #启动作业
	`export DOCKER_HOST="tcp://$address:3839" &&export innerAddress=$address&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml up -d`

			
	done 

done 
