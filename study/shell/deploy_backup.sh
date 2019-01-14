#! /bin/bash
#@auth nessary

HISTORY_FILE="./history"
MAX_BACKUP_BRANCH="3"

if [[ ! -f $HISTORY_FILE ]];then
 
 `touch $HISTORY_FILE`
fi


branch_count=`git branch  |wc -l`

if [[ $branch_count -gt $MAX_BACKUP_BRANCH ]];then
    last_branch_version=`git branch|grep -v remote|head -n 1`
	`git branch -D $last_branch_version >/dev/null 2>&1`
    `git push origin --delete $last_branch_version >/dev/null 2>&1`
    echo "delete old version $last_branch_version"      
 
fi


if [[ -n "$1"  ]];then
 `git checkout $1 >/dev/null 2>&1`
 
else

 new_version=`date +%Y%m%d%s`
 `git checkout -b $new_version > /dev/null 2>&1`
 
 if [[  $? -ne 0 ]];then 
  
 `git checkout  $new_version > /dev/null 2>&1`
 fi
 `git reset --hard origin/master > /dev/null 2>&1 && git push -f  origin $new_version >/dev/null 2>&1 `

   if [[ $? -eq 0 ]];then 
       result=`cat $HISTORY_FILE|grep $new_version`
	   if [[ ${#result} -eq 0  ]];then
	   `echo $new_version >> $HISTORY_FILE`
	   fi
       echo "$new_version backup success!!"
   fi 
  `git checkout master >/dev/null 2>&1 ` 
   
fi	

 
