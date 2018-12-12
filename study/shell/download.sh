#!/bin/bash 
#用来批量处理下载git分支代码 输入的参数 是 姓名 和密码
echo -e "\033[32m 开始进行拉取代码分支\033[0m"
echo "请输入码云用户名,输入完毕回车"
read username
echo "请输入码云密码,输入完毕回车"
read userpasswd

echo "请输入项目导出目录,输入完毕回车(直接回车默认当前目录)"
read exportpath

if [[ $exportpath != '' ]];then

	if [[ ! -d $exportpath ]];then 
	  echo "请检查输入的导出目录[$exportpath]是否存在"
	  exit 0
	else
    exportpath=$exportpath/	
	fi   
  
fi   



file_name="/tmp/nessary.txt"
dir_name="/tmp/etongjin"
auth=$username:$userpasswd
if [[ ! -f $file_name ]];then
 
  `touch $file_name`
fi

if [[  -d $dir_name ]];then
 
  `rm -rf  $dir_name`
fi  



if [[  -d $exportpath ]];then
 
  `rm -rf  $exportpath`
fi  
  #先去克隆出仓库  然后寻找到当前项目下所有的分支 保存起来
`cd /tmp/ &&git clone https://$auth@gitee.com/nessary/etongjin.git  $di_name&&cd $dir_name&&git branch -a >$file_name`

`sed -i '1,2 d' $file_name`

#分别将保存下来的分支分别下载到指定目录
while read line 
do 
  branch_name=`echo $line |awk -F / '{print$3}'`
  
 `git clone -b $branch_name https://$auth@gitee.com/nessary/etongjin.git $exportpath$branch_name`
  
  #把主文件不需要建立文件夹
  if [[ $branch_name == 'master' ]];then
  
      `mv ./master/*  ./ && rm -rf ./master`
  fi	  
  
  
  
done <  $file_name

echo "所有代码拉取成功"

