#!/bin/bash
git_commit_log_path="/tmp/miscroserver_application_git_commit_log.log"
git_commit_new_log_path="/tmp/miscroserver_application_git_commit_new_log.log"
all_servers="miscroservice_user miscroservice_crm miscroservice_order miscroservice_store miscroservice_product miscroservice_payment miscroservice_operate miscroservice_publicaccount miscroservice_distribution miscroservice_activity miscroservice_supplier"

git_url="git@yjj.nessary.top:root/yjj_java_top.git"
git_path="/opt/git"
branch_project_name="yjj_java_top"
addressArr="172.16.94.69"


if [[  -d $git_path/$branch_project_name ]]; then 
 result=`cd $git_path/$branch_project_name &&git pull`
 echo $result
else
 `cd $git_path&&rm -rf $branch_project_name&&git clone $git_url` 
fi 



`cd $git_path/$branch_project_name && git log --name-only >$git_commit_new_log_path`

if [ ! -f $git_commit_log_path ]; then
 
`touch $git_commit_log_path`

fi


differ_info=`diff $git_commit_log_path $git_commit_new_log_path`


miscroservice_arr=$all_servers



if [[ $differ_info == *"miscroservice_commons"*  &&  -s $git_commit_log_path  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
miscroservice_arr=$all_servers
fi    


for address in $addressArr
do 

	for service in $miscroservice_arr

	do 

		if [[ $differ_info == *$service* ]]; then

		echo  $service




			project_name="$service"
			exportpath=`cd $git_path/$branch_project_name&&pwd`
			branch_name=$service

			`echo -e "rootProject.name = 'finace_miscroservice' \n include '$project_name' \n include 'miscroservice_commons'" >$exportpath/settings.gradle`

			`cd $exportpath && gradle clean build -x test`
			port=`cat $exportpath/$branch_name/src/main/resources/application.yml |grep 'port:'|head -n 1 |awk '{print $2}'`
			
			echo  $service
			`\cp -f /opt/auto/docker-compose.yml $exportpath/$branch_name/ `
			`\cp  -f /opt/auto/Dockerfile-dev $exportpath/$branch_name/ `
			
			`mv $exportpath/$branch_name/Dockerfile-dev $exportpath/$branch_name/Dockerfile`
			
			 #构建作业
			`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export port=$port&&export innerAddress=$address&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml build`

			 #停止作业
			`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export port=$port&&export innerAddress=$address&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml down`
			 #启动作业
			`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export port=$port&&export innerAddress=$address&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml up -d`
					



			 echo 'success'



		fi 





	done


done


`mv $git_commit_new_log_path $git_commit_log_path`




