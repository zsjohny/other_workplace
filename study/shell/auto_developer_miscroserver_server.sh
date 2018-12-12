#!/bin/bash
git_commit_log_path="/tmp/miscroserver_git_commit_log.log"
git_commit_new_log_path="/tmp/miscroserver_git_commit_new_log.log"
git_path="/var/opt/gitlab/git-data/repositories/root/yjj_java_server.git"


`cd $git_path && git log --name-only >$git_commit_new_log_path`

if [ ! -f $git_commit_log_path ]; then
 
`touch $git_commit_log_path`

fi


differ_info=`diff $git_commit_log_path $git_commit_new_log_path`


miscroservice_arr="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2 miscroservice_config  miscroservice_data_analysis miscroservice_authorize  miscroservice_task_scheduling  miscroservice_getway"



if [[ $differ_info == *"miscroservice_commons"*  &&  -s $git_commit_log_path  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
miscroservice_arr="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2 miscroservice_config  miscroservice_data_analysis miscroservice_authorize  miscroservice_task_scheduling miscroservice_getway"
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
		
     	echo  $service
		
		`mv $exportpath/$branch_name/Dockerfile-test $exportpath/$branch_name/Dockerfile`
		
		 #构建作业
		`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml build`

		 #停止作业
		`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml down`
		 #启动作业
		`export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml up -d`
				



		 echo 'success'



	fi 





done





`mv $git_commit_new_log_path $git_commit_log_path`




