#!/bin/bash

git_commit_log_path="/tmp/git_commit_log.log"
git_commit_new_log_path="/tmp/git_commit_new_log.log"
git_path="/var/opt/gitlab/git-data/repositories/ytj_java/ytj.git"


`cd $git_path && git log --name-only >$git_commit_new_log_path`

if [ ! -f $git_commit_log_path ]; then
 
`touch $git_commit_log_path`

fi


differ_info=`diff $git_commit_log_path $git_commit_new_log_path`

miscroservice_arr="miscroservice_register_discovery_centers_v1 miscroservice_register_discovery_centers_v2 miscroservice_config   miscroservice_authorize   miscroservice_official_website  miscroservice_task_scheduling  miscroservice_user  miscroservice_borrow  miscroservice_activity miscroservice_getway"



if [[ $differ_info == *"miscroservice_commons"*  &&  -s $git_commit_log_path  ]];then 
 `echo "" >$git_commit_log_path`
echo "init all project" 
differ_info=`diff $git_commit_log_path $git_commit_new_log_path`
miscroservice_arr="miscroservice_official_website  miscroservice_task_scheduling  miscroservice_user  miscroservice_borrow  miscroservice_activity"
fi    




for service in $miscroservice_arr

do 

if [[ $differ_info == *$service* ]]; then

echo  $service


	gradle_server="47.97.174.183"

	exportpath=`pwd`
	project_name="$service"

	
	`echo -e "rootProject.name = 'finace_miscroservice' \n include '$project_name' \n include 'miscroservice_commons'" >./settings.gradle`

	
	
    #本机不打包 远程打包
	`sudo rsync  -r $exportpath/$project_name/* root@$gradle_server:/tmp/$project_name/`
	`sudo rsync  -r $exportpath/settings.gradle $exportpath/build.gradle root@$gradle_server:/tmp/`
	`sudo rsync  -r $exportpath/miscroservice_commons/* root@$gradle_server:/tmp/miscroservice_commons/`
	
	
	`sudo ssh root@$gradle_server "export GRADLE_USER_HOME='/opt/.gradle'&&export JAVA_HOME='/stock/java'&&/opt/gradle/bin/gradle -p /tmp clean build -x test"`


	echo "now  start job"


	if [[ $service == "miscroservice_getway" ]];then 

       #拷贝回来
      `sudo scp root@$gradle_server:/tmp/$project_name/build/libs/m*.jar  /tmp/$service.jar`	
	  
	  `cp ./miscroservice_getway/Dockerfile /tmp/ &&docker stop java&&docker rm java&&docker build -t java /tmp/&&docker run -dit --name java -v /tmp:/tmp -p 1020:1020 -p 8081:8081 java`
	  echo 'success'

	else 

		server_list="47.97.174.183"
		for server in $server_list 
		do 

		port=`sudo ssh root@$server ps -ef|grep $project_name|grep -v grep|awk '{print $2}'|head -n 1`

		if [[ ${#port} -gt 1 ]];then
		 `sudo ssh root@$server kill  -9 $port`
		fi   


		pid=`sudo ssh root@$server /stock/java/bin/jps -l|grep $project_name|awk '{print $1}'`

		if [[ $pid -ne ''  ]];then
		`sudo ssh root@$server kill  -9 $pid`

		fi

		`sudo ssh root@$server "nohup /stock/java/bin/java -jar  -Xms126m -Xmx216m  /tmp/$project_name/build/libs/*.jar >/stock/log/$project_name.log 2>&1 &"`


		done 

    fi




fi 





done



`mv $git_commit_new_log_path $git_commit_log_path`



