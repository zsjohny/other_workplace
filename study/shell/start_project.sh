#! /bin/bash
#auto priject

echo -e "\033[32m-----开始启动程序------\033[1m"

#svn

`cd /opt/svn && svnserve -d -r /opt/svn --listen-port=8028`


if [ $? -eq 0 ] ;then
 echo "svn start success"
else 
 echo "svn start fail"
 exit 0
fi

#nginx

`/opt/nginx/sbin/nginx`



if [ $? -eq 0 ] ;then
 echo "nginx start success"
else
 echo "nginx start fail"
 exit 0
fi



#nexus 
 `/opt/nexus/nexus-3.1.0-04/bin/nexus start`


if [ $? -ne 0 ] ;then
#if [ $? -eq 0 ] ;then
 echo "nexus start success"
else
 echo "nexus start fail"
 exit 0
fi




#activemq
`/opt/activemq/bin/activemq start`

if [ $? -ne 0 ] ;then
#if [ $? -eq 0 ] ;then
 echo "mq start success"
else
 echo "mq start fail"
 exit 0
fi





#redis
`/opt/redis/src/redis-server /opt/redis/redis.conf`


if [ $? -eq 0 ] ;then
 echo "redis start success"
else
 echo "redis start fail"
 exit 0
fi





#mongo
`rm -rf /opt/mongodb/data/mongod.lock`
`rm -rf /opt/mongodb/data/WiredTiger.lock`

sleep 1s
`cd /opt/mongodb && /opt/mongodb/bin/mongod -f /opt/mongodb/mongodb.conf`

if [ $? -ne 0 ] ;then
 echo "mongo start success"
else
 echo "mongo start fail"
 exit 0
fi


sleep 2s

echo "所有服务已经加载完毕----"


exit 0
