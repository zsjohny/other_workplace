`docker run -dit -v /opt/nginx:/etc/nginx/conf.d -v /home/fpt:/home/fpt -v /home/download:/home/download   -p 80:80 -p 81:81 --name nginx nginx`



docker run --env LDAP_ORGANISATION="nessary" --env LDAP_DOMAIN="dc=domain,dc=com"  --env LDAP_ADMIN_PASSWORD="879227577"  -p 1389:389 --name ldap --detach osixia/openldap

docker exec ldap ldapsearch -x -H ldap://0.0.0.0:1389 -b dc=domain,dc=com -D "cn=admin,dc=example,dc=org" -w admin


export name=jiuy-admin-api


scp  root@47.96.153.80:/mnt/jiuy/$name/webroot/WEB-INF/classes/*properties root@47.96.153.80:/mnt/jiuy/$name/webroot/WEB-INF/classes/*txt root@47.96.153.80:/mnt/jiuy/$name/webroot/WEB-INF/classes/*xml  /home/nessa/Desktop/$name/WEB-INF/classes/

scp -r   /home/nessa/Desktop/$name/* root@47.96.153.80:/mnt/tomcat/webapps/ROOT
 
 /mnt/tomcat/bin/catalina.sh run
 
 
 rm  -rf /mnt/jiuy/$name/webroot_bark &&cp -r /mnt/jiuy/$name/webroot /mnt/jiuy/$name/webroot_bark&&rm  -rf /mnt/jiuy/$name/webroot/* &&rm -rf /mnt/jiuy/$name/tomcat/logs/*&& mv /mnt/tomcat/webapps/ROOT/* /mnt/jiuy/$name/webroot/ && /mnt/jiuy/$name/tomcat/bin/shutdown.sh &&/mnt/jiuy/$name/tomcat/bin/startup.sh&&tail -n 100 -f  /mnt/jiuy/$name/tomcat/logs/catalina.out
 


 
 分布式事务
 springboot protobuf
 ab.exe -n 5000 -c 500 "http://yjj_dev.nessary.top:8081/miniapp/homepage/homeProductList.json?type=1&current=1&size=10&storeId=12130&memberId=105713"
 abs.exe -n 5000 -c 500 "https://online.yujiejie.com/jstore/home/dailyNew?current=2&size=10&firstQueryTime=1550144955017"
 