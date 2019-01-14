#!/bin/bash
git_commit_log_path="/tmp/miscroserver_application_git_commit_log.log"
git_commit_new_log_path="/tmp/miscroserver_application_git_commit_new_log.log"
#all_servers="miscroservice_user miscroservice_crm miscroservice_order miscroservice_store miscroservice_product miscroservice_payment miscroservice_operate miscroservice_publicaccount miscroservice_distribution miscroservice_activity miscroservice_supplier"
all_servers="miscroservice_user miscroservice_order miscroservice_store miscroservice_product miscroservice_crm miscroservice_payment miscroservice_operate miscroservice_publicaccount miscroservice_distribution miscroservice_activity miscroservice_supplier"
git_url="git@yjj.nessary.top:root/yjj_java_top.git"
git_path="/opt/git"
branch_project_name="yjj_java_top"
addressArr="172.16.94.69"

BRANCH_NAME="master"
MULTIPLE_DEPLOY_FLAG=false

`ps -ef|grep gradle |awk '{print $2}'|xargs kill -9 >/dev/null 2>&1`

if [[  -d $git_path/$branch_project_name ]]; then 
 result=`cd $git_path/$branch_project_name &&git pull origin $BRANCH_NAME`
 echo $result
else
 `cd $git_path&&rm -rf $branch_project_name&&git clone $git_url >/dev/null 2>&1` 
fi 


#切换老的系统----------------------------------------------------------
HISTORY_FILE="$HOME/history"
MAX_BACKUP_BRANCH="3"



SINGLE_BRANCH_NAME=""


#init
`cd $git_path/$branch_project_name && git checkout $BRANCH_NAME >/dev/null 2>&1`

if [[ ! -f $HISTORY_FILE ]];then
   `touch $HISTORY_FILE`
fi


branch_count=`cd $git_path/$branch_project_name && git branch |grep -v remote |wc -l`

if [[ $branch_count -gt $MAX_BACKUP_BRANCH ]];then

    last_branch_version=`cd $git_path/$branch_project_name && git branch|grep -v remote|head -n 1`
	#去除空格
	last_branch_version=${last_branch_version// /}
	`cd $git_path/$branch_project_name && git branch -D $last_branch_version >/dev/null 2>&1`
    `cd $git_path/$branch_project_name && git push origin --delete $last_branch_version >/dev/null 2>&1`
	`sed -i /$last_branch_version/d $HISTORY_FILE`
     echo "delete old version $last_branch_version"      
 
fi


if [[ -n "$1"  ]];then
      
    #判断是在master还是分支上
	all_branch_name=`cd $git_path/$branch_project_name && git branch|grep -v remote|grep $1`
	
	if test -z $all_branch_name ;then
		  
          SINGLE_BRANCH_NAME=$1
		  #去除服务
		  `sed -i s/miscroservice_$2/\"\"/g $git_commit_log_path`  
		  		  
		  #支持多应用的更新
		  if [[ -n "$2"  ]] && [[ $2 -eq "true" ]] ;then
		  echo "deploy multiple..."
		  addressArr="172.16.94.69  47.99.175.251"
		  MULTIPLE_DEPLOY_FLAG=true

		  fi
		  
	else
	     	  
		 `cd $git_path/$branch_project_name && git checkout $1 >/dev/null 2>&1`
		 #初始化项目
		  if [[ -n "$2"  ]];then
			`sed -i s/miscroservice_$2/\"\"/g $git_commit_log_path`   
			 SINGLE_BRANCH_NAME=$2
		  else
			`echo "all">$git_commit_log_path`
		  fi 
		  BRANCH_NAME=$1
		  
		  #支持多应用的更新
		  if [[ -n "$3"  ]] && [[ $3 -eq "true" ]] ;then
		  echo "deploy multiple..."
		  addressArr="172.16.94.69  47.99.175.251"
		  MULTIPLE_DEPLOY_FLAG=true

		  fi
		  
		  
	fi
	
	  
else

	 new_version=`date +%Y%m%d`
	 
	 `cd $git_path/$branch_project_name && git checkout -b $new_version > /dev/null 2>&1`
	 
	 if [[  $? -ne 0 ]];then 
	  
		  `cd $git_path/$branch_project_name && git checkout  $new_version > /dev/null 2>&1`
	 
	 fi
	 
	 
#	 `cd $git_path/$branch_project_name && git reset --hard origin/master > /dev/null 2>&1 && git push -f  origin $new_version > /dev/null 2>&1`
	 `cd $git_path/$branch_project_name && git merge master > /dev/null 2>&1 && git push -f  origin $new_version > /dev/null 2>&1`
	  
	 
	  if [[ $? -eq 0 ]];then 
		   
		   result=`cat $HISTORY_FILE|grep $new_version`
		   
		   if [[ ${#result} -eq 0  ]];then
		   
		   `echo $new_version >> $HISTORY_FILE`
		   
		   fi
		   
		   echo "$new_version backup success!!"
	  fi 
	  
	  `cd $git_path/$branch_project_name && git checkout $BRANCH_NAME >/dev/null 2>&1` 
	   
fi	
#切换老的系统----------------------------------------------------------




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



#只跟新新的
if  ! test -z $SINGLE_BRANCH_NAME ;then
 miscroservice_arr="miscroservice_"$SINGLE_BRANCH_NAME
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
			if [[ $MULTIPLE_DEPLOY_FLAG = 'true' ]];then
			`sed -i '/^ENTRYPOINT/d' $exportpath/$branch_name/Dockerfile && sed  -i 's/^#//g' $exportpath/$branch_name/Dockerfile`
			fi 
			
			
			
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




