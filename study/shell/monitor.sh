#这里根据每个系统不同筛选的不一样
#cat /etc/shells 查看当前系统支持的 排在第一位是默认支持的
#@auth nessary
#monitor service 

#申明字典
declare -A service

#特殊的命令
command="python3 MonitorShell.py"


#linux可以将空格的字符串当成list 通过for循环遍历他
service=(["redis"]="/stock/redis/src/redis-server /stock/redis/redis.conf"  
["zbox"]="/opt/zbox/zbox start"  ["svn"]="svnserve -d -r /stock/svn --listen-port=8068" ["jenkins"]="service jenkins start"  
["nginx"]="/stock/nginxs/nginx/sbin/nginx" ["rabbitmq"]=$command )

#这里检测是否是通过定时任务进行检测 如果是则尝试自动重启，不是则略过
while true
do
   #遍历所有的key--即相应的服务for line in ${!service[*]}
   for line in ${!service[*]}
   do
       #获得是否还有的服务
	res=`ps -ef|grep $line|grep -v grep|awk '{print $2}' `
	#进行检测
	if [[ ${#res} -gt 1 ]] ;then
 	   echo "service $line is running"
	else
  	    echo -e  "\033[32mservice $line is not running \033[0m"
  	    echo -e "\033[32mnow restart service $line\033[0m"
		`${service[$line]}`
           
	fi 

  done
  #暂停
  sleep 20

done 



