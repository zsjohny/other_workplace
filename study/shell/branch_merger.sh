#!/bin/bash
`git config --global user.email "nessary@foxmail.com"`
`git config --global user.name "nessary"`
git_code_dev_path="/tmp/git/etongjin"
git_code_test_path="/tmp/git/ytj"

if [ -d $git_code_test_path ];then
  `rm -rf $git_code_test_path`
fi 


`git clone http://pj:879227577@112.17.92.53:9090/ytj_java/ytj.git $git_code_test_path`

if [[ ! -d $git_code_dev_path ]]; then


	git_oringal_path=`git clone https://nessary:Pj879227577@gitee.com/nessary/etongjin.git $git_code_dev_path`
	branch_name=`cd $git_code_dev_path &&git branch -a |grep -v master |awk -F / '{print $NF}'`
	for name in  $branch_name
	do 
	`cd $git_code_dev_path &&git clone -b $name https://nessary:Pj879227577@gitee.com/nessary/etongjin.git ./$name`

	
	done 

fi


branch_name=`cd $git_code_dev_path && git branch -a |grep -v master |awk -F / '{print $NF}'`

	for name in  $branch_name
	do 

	     

	diff_info=`diff -r $git_code_dev_path/$name/src $git_code_test_path/$name/src`
		
		
		if [[ $diff_info != '' ]] ;then
		
		`cp -r $git_code_test_path/$name/src/* $git_code_dev_path/$name/src/`
		`cp -r $git_code_test_path/$name/build.gradle $git_code_dev_path/$name/build.gradle`
		`cd $git_code_dev_path/$name&&git add . && git commit -m 'update "$name"' . &&git push`
		 echo "$name update success"
		
		fi 
		
	done
	



echo "merger success"





server_list="101.37.151.54 120.27.222.192"


for address in $server_list 
do 
	`ssh root@$address "echo '112.17.92.53:47.96.167.92' >/tmp/publish/publish.properties"`		 
      
		
done 

