# coding=utf-8
# import subprocess
import os
# subprocess.call(["nohup", "/stock/rabbitmq/sbin/rabbitmq-server —detached > /dev/null 2>&1 &"])
# subprocess.Popen("nohup /stock/rabbitmq/sbin/rabbitmq-server —detached > /dev/null 2>&1 &")
os.system("nohup /stock/rabbitmq/sbin/rabbitmq-server —detached > /dev/null 2>&1 &")