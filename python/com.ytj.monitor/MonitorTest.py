import json
import os

import paramiko

##读取当前路径
base_dir = os.getcwd()
##读取在远程主机执行的脚本
# cmd_filepath = base_dir + r"touch"
cmd_filepath = r"E:/python/com.ytj.monitor/touch1"
cmd_file = open(cmd_filepath, "r")
cmd = cmd_file.read()

##连接远程主机
client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect("192.168.89.129", 22, 'root', '879227577')
##执行命令
stdin, stdout, stderr = client.exec_command(cmd)
##读取信息
for line in stdout:
    print(line)
    # data = json.loads(line)
    # print(type(data))
    # print(data)
##关闭连接
client.close()
