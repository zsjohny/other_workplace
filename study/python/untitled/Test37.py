# coding=utf-8
import pymysql

sql = '''/*--user=thunder;--password=thunder;--host=127.0.0.1;--enable-remote-backup=0;--execute;--port=3306;*/\ 
inception_magic_start;
use test;
CREATE TABLE `alifeba_user` (
     `ID` int(11) unsigned NOT NULL auto_increment comment "aaa",
     `username` varchar(50) NOT NULL Default "" comment "aaa",
     `realName` varchar(50) NOT NULL Default "" comment "aaa",
     `age` int(11) NOT NULL Default 0 comment "aaa",
     PRIMARY KEY (`ID`)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT="AAAA";
inception_magic_commit;'''
try:
    conn = pymysql.connect(host='192.168.5.130', user='root', passwd='root', db='', port=6669)
    cursor = conn.cursor()
    cursor.execute(sql)
    results = cursor.fetchall()
    column_name_max_size = max(len(i[0]) for i in cursor.description)
    row_num = 0
    for result in results:
        row_num = row_num + 1
        print('*'.ljust(27, '*'), row_num, '.row', '*'.ljust(27, '*'))
        row = map(lambda x, y: (x, y), (i[0] for i in cursor.description), result)
        for each_column in row:
            if each_column[0] != 'errormessage':
                print
                each_column[0].rjust(column_name_max_size), ":", each_column[1]
            else:
                print
                each_column[0].rjust(column_name_max_size), ':', each_column[1].replace('\n', '\n'.ljust(
                    column_name_max_size + 4))
    cursor.close()
    conn.close()
except Exception as e:
    print("Mysql Error  %s" % e)

    inception_magic_commit;
