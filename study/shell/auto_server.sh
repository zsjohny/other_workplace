#!/bin/bash
git_commit_log_path="/tmp/miscroserver_git_commit_log.log"
git_commit_new_log_path="/tmp/miscroserver_git_commit_new_log.log"
git_url="git@yjj.nessary.top:root/yjj_java_server.git"
git_path="/opt/git"
branch_project_name="yjj_java_server"
server_save_path="/opt/auto/server-1.jar"
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


miscroservice_arr="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2 miscroservice_config  miscroservice_data_analysis miscroservice_authorize  miscroservice_task_scheduling  miscroservice_getway"



if [[ $differ_info == *"miscroservice_commons"*  &&  -s $git_commit_log_path  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
miscroservice_arr="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2 miscroservice_config  miscroservice_data_analysis miscroservice_authorize  miscroservice_task_scheduling miscroservice_getway"
fi    


for address in $addressArr
do 

		for service in $miscroservice_arr

		do 

			if [[ $differ_info == *$service* ]]; then

			echo  $service




				exportpath=`cd $git_path/$branch_project_name&&pwd`
				
				branch_name=$service
				
				`echo -e "rootProject.name = 'finace_miscroservice' \n include '$branch_name' \n include 'miscroservice_commons'" >$exportpath/settings.gradle`
                 
				result=`cd $exportpath && gradle clean build -x test`
				echo "$result"
				
				echo  $service
				
				`\cp $exportpath/$branch_name/Dockerfile-dev $exportpath/$branch_name/Dockerfile`
				
				 #构建作业
				`cd $exportpath/$branch_name && export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export innerAddress=$address && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml build`

				 #停止作业
				`cd $exportpath/$branch_name && export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export innerAddress=$address && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml down`
				 #启动作业
				`cd $exportpath/$branch_name && export DOCKER_HOST="tcp://$address:3839" && export serviceName=$branch_name &&export innerAddress=$address && docker-compose -p $branch_name -f $exportpath/$branch_name/docker-compose.yml up -d`
						



				 echo 'success'



			fi 





		done


done






`mv $git_commit_new_log_path $git_commit_log_path`




