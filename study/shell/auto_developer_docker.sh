#!/bin/bash

git_commit_log_path="/tmp/git_commit_log.log"

git_commit_new_log_path="/tmp/git_commit_new_log.log"
git_path="/var/opt/gitlab/git-data/repositories/root/yjj_java.git"
server_save_path="/opt/server-1.jar"





`cd $git_path && git log --name-only >$git_commit_new_log_path`

if [ ! -f $git_commit_log_path ]; then
 
`touch $git_commit_log_path`
`mvn clean install -Dmaven.test.skip=true`

fi


differ_info=`diff $git_commit_log_path $git_commit_new_log_path`


miscroservice_arr="jiuy-admin-api jiuy-store-api jfinal-weixin jiuy-wxa-api jiuy-supplier-admin jiuy-operator-admin jiuy-ground-api jiuy-wxaproxy-admin"




if [[ ($differ_info == *"jiuy-biz-common"*  &&  -s $git_commit_log_path) || ($differ_info == *"jiuy-biz-base"*  &&  -s $git_commit_log_path)|| 
($differ_info == *"jiuy-core"*  &&  -s $git_commit_log_path) || ($differ_info == *"jiuy-biz-core"*  &&  -s $git_commit_log_path) ||
($differ_info == *"jiuy-rebuild-service"*  &&  -s $git_commit_log_path) || ($differ_info == *"jiuy-rebuild-service-impl"*  &&  -s $git_commit_log_path)  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
miscroservice_arr="jiuy-admin-api jiuy-store-api jfinal-weixin jiuy-wxa-api jiuy-supplier-admin jiuy-operator-admin jiuy-ground-api jiuy-wxaproxy-admin"
`mvn clean install -Dmaven.test.skip=true`
fi    



for service in $miscroservice_arr

do 
     
	if [[ $differ_info == *$service* ]]; then
     
	 
	echo  $service
	`cd ./$service&&mvn clean install -Dmaven.test.skip=true&&cp $server_save_path  ./target/ &&cd ./target/ &&java -jar server-1.jar local`
     echo "$service deploy success"  
	fi 





done





`mv $git_commit_new_log_path $git_commit_log_path`




