#!/bin/bash
source /etc/profile
PROFILE="dev"
addressArr="47.96.70.20"
`mkdir /tmp/$PROFILE >/dev/null 2>&1`
git_commit_log_path="/tmp/$PROFILE/miscroserver_server_git_commit_log.log"
git_commit_new_log_path="/tmp/$PROFILE/miscroserver_server_git_commit_new_log.log"
git_url="git@gitlab.com:nessary/xiaoshi_java_server.git"
git_path="/opt/git"
branch_project_name="xiaoshi_java_server"
miscroservice_arr="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2 miscroservice_config  miscroservice_data_analysis miscroservice_authorize  miscroservice_task_scheduling miscroservice_getway"



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


if [[ $differ_info == *"miscroservice_commons"*  &&  -s $git_commit_log_path  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
fi    


#只跟新新的
if  [[ -n "$1"   ]];then
 miscroservice_arr="miscroservice_"$1
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
                    `cd $exportpath && gradle clean build -x test`
				
				echo  $service
				
				`\cp $exportpath/$branch_name/Dockerfile-$PROFILE $exportpath/$branch_name/Dockerfile`
				
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




