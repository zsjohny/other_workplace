#! /bin/bash
echo "Content-Type:text/html;charset=utf-8"
echo ""  
 
 
 base_path=/stock/nginx/logs
 tmp_file=/tmp/access_tmp.log
 if [[ ! -d $tmp_file ]] ;then
   `touch $tmp_file`
 fi 
 `echo "" >$tmp_file`
 
 pv_file=/tmp/access_tmp_pv
 if [[ ! -d $pv_file ]] ;then
   `touch $tmp_file`
 fi 
 uv_file=/tmp/access_tmp_uv
  if [[ ! -d $uv_file ]] ;then
   `touch $tmp_file`
 fi 
 
 #保证能够在服务器有权限执行
 `chomd -R 777 /tmp/acc*`
 
 for file in `ls $base_path` 
	 do
	   if [[  $file =~ "_" ]] ;then
	   
		awk '{print $6}' $base_path/$file | wc -l >$pv_file
		awk '{print $10}' $base_path/$file | sort -r |uniq -c |wc -l >$uv_file
		
		 pv=`cat $pv_file`
		 uv=`cat $uv_file`
	     
		echo "$file的 pv是:$pv次; uv:$uv个人数 <br/><p>" >>$tmp_file
	
	   fi
	   

	 done   
	 
   eval cat $tmp_file				 
