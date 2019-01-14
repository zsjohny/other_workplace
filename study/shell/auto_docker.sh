#!/bin/bash

git_commit_log_path="/tmp/git_commit_log.log"

git_commit_new_log_path="/tmp/git_commit_new_log.log"
git_url="git@yjj.nessary.top:root/yjj_java.git"
git_path="/opt/git"
branch_project_name="yjj_java"
server_save_path="/opt/auto/server-1.jar"




if [[  -d $git_path/$branch_project_name ]]; then 
 result=`cd $git_path/$branch_project_name &&git pull`
 echo $result
else
 `cd $git_path&&rm -rf $branch_project_name&&git clone $git_url >/dev/null 2>&1` 
fi 

`mv  $git_path/$branch_project_name/pom_dev.xml $git_path/$branch_project_name/pom.xml >/dev/null 2>&1`


`cd $git_path/$branch_project_name && git log --name-only >$git_commit_new_log_path`

if [ ! -f $git_commit_log_path ]; then
 
`touch $git_commit_log_path`
`cd $git_path/$branch_project_name && mvn clean install -Dmaven.test.skip=true`

fi


differ_info=`diff $git_commit_log_path $git_commit_new_log_path`


#miscroservice_arr="jiuy-admin-api jiuy-store-api jfinal-weixin jiuy-wxa-api jiuy-supplier-admin jiuy-operator-admin jiuy-ground-api jiuy-wxaproxy-admin"
miscroservice_arr="jiuy-store-api"



if [[ ($differ_info == *"jiuy-biz-common"*  &&  -s $git_commit_log_path) || ($differ_info == *"jiuy-biz-base"*  &&  -s $git_commit_log_path)|| 
($differ_info == *"jiuy-core"*  &&  -s $git_commit_log_path) || ($differ_info == *"jiuy-biz-core"*  &&  -s $git_commit_log_path) ||
($differ_info == *"jiuy-rebuild-service"*  &&  -s $git_commit_log_path) || ($differ_info == *"jiuy-rebuild-service-impl"*  &&  -s $git_commit_log_path)  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
#miscroservice_arr="jiuy-admin-api jiuy-store-api jfinal-weixin jiuy-wxa-api jiuy-supplier-admin jiuy-operator-admin jiuy-ground-api jiuy-wxaproxy-admin"
miscroservice_arr="jiuy-store-api"
`cd $git_path/$branch_project_name && mvn clean install  -Dmaven.test.skip=true >/dev/null 2>&1`
fi    



if [[ -n "$1"  ]];then
SINGLE_SERVICE_NAME=jiuy-$1-api
miscroservice_arr=$SINGLE_SERVICE_NAME
differ_info=$SINGLE_SERVICE_NAME

fi 


for service in $miscroservice_arr

do 
     
	if [[ $differ_info == *$service* ]]; then
     
	 
      exportpath=`cd $git_path/$branch_project_name&&pwd`
	
	  echo  $service
	 
	  if [[ -n "$2"  ]];then

      `cd $exportpath/$service&&mvn clean install -Dmaven.test.skip=true&&\cp $server_save_path  ./target/ &&cd ./target/ &&java -jar server-1.jar online $2` 
     
      echo $exec_cmd
      else
      `cd $exportpath/$service&&mvn clean install -Dmaven.test.skip=true&&\cp $server_save_path  ./target/ &&cd ./target/ &&java -jar server-1.jar online` 
      fi 
	  echo "$service deploy success"  
	fi 





done





`mv $git_commit_new_log_path $git_commit_log_path`




