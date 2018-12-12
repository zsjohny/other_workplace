#!/bin/bash
git_commit_log_path="/tmp/miscroserver_application_git_commit_log.log"
git_commit_new_log_path="/tmp/miscroserver_application_git_commit_new_log.log"
all_servers="miscroservice_user miscroservice_crm miscroservice_order miscroservice_store miscroservice_product miscroservice_payment miscroservice_operate miscroservice_publicaccount miscroservice_distribution miscroservice_activity miscroservice_supplier"
git_path="/var/opt/gitlab/git-data/repositories/root/yjj_java_top.git"


`cd $git_path && git log --name-only >$git_commit_new_log_path`

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


for service in $miscroservice_arr

do 

	if [[ $differ_info == *$service* ]]; then

	echo  $service




		project_name="$service"
		exportpath=`pwd`
		branch_name=$service

		`echo -e "rootProject.name = 'finace_miscroservice' \n include '$project_name' \n include 'miscroservice_commons'" >./settings.gradle`

		`gradle clean build -x test`
		port=`cat $exportpath/$branch_name/src/main/resources/application.yml |grep 'port:'|head -n 1 |awk '{print $2}'`
		
     	echo  $service
		`cp /opt/docker-compose.yml $exportpath/$branch_name/ `
		`cp /opt/Dockerfile-test $exportpath/$branch_name/ `
		
		`mv $exportpath/$branch_name/Dockerfile-test $exportpath/$branch_name/Dockerfile`
		
		 #构建作业
		`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export port=$port&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml build`

		 #停止作业
		`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export port=$port&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml down`
		 #启动作业
		`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export port=$port&& docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml up -d`
				



		 echo 'success'



	fi 





done





`mv $git_commit_new_log_path $git_commit_log_path`




