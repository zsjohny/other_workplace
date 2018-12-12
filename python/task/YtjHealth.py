#!/usr/bin/python3
# coding=utf8
import subprocess
import time
from urllib import request

from Log import Log


def kill_by_process(pid):
    subprocess.getoutput("kill -9 %s" % pid)


class Health:

    def __init__(self, log=Log):
        self.__check_all_url_cmd = {
            # "http://localhost:6898/healths": "miscroservice_register_discovery_centers_v1",
            # "http://localhost:6899/healths": "miscroservice_register_discovery_centers_v2",
            "http://localhost:8081/config/healths": "miscroservice_config",
            "http://localhost:8081/healths": "miscroservice_getway",
            "http://localhost:8081/activity/healths": "miscroservice_activity",
            "http://localhost:8081/borrow/healths": "miscroservice_borrow",
            "http://localhost:8081/user/healths": "miscroservice_user",
            "http://localhost:8081/task_scheduling/healths": "miscroservice_task_scheduling"
        }
        self.__optimize_params = {"miscroservice_activity": "-Xms216m -Xmx512m",
                                  "miscroservice_borrow": "-Xms512m -Xmx1024m",
                                  "miscroservice_config": "-Xms126m -Xmx256m ",
                                  "miscroservice_getway": "-Xms1024m -Xmx2048m",
                                  "miscroservice_register_discovery_centers_v1": "-Xms20m -Xmx126m",
                                  "miscroservice_register_discovery_centers_v2": "-Xms20m -Xmx126m",
                                  "miscroservice_user": "-Xms512m -Xmx1024m",
                                  "miscroservice_task_scheduling": "-Xms1024m -Xmx2048m"}
        self.__profile_params = {"miscroservice_activity": "--spring.profiles.active=dev",
                                 "miscroservice_borrow": "--spring.profiles.active=dev",
                                 "miscroservice_config": "--spring.profiles.active=center",
                                 "miscroservice_getway": "--spring.profiles.active=center",
                                 "miscroservice_register_discovery_centers_v1": "",
                                 "miscroservice_register_discovery_centers_v2": "",
                                 "miscroservice_user": "--spring.profiles.active=dev",
                                 "miscroservice_task_scheduling": "--spring.profiles.active=center"}
        self.__command = "nohup /stock/java/bin/java -jar  {0}  /tmp/{1}.jar  {2} >/dev/null 2>&1 &"
        self.__log = log

    @staticmethod
    def __kill(name):
        all_java_pid = subprocess.getoutput("jps -l")
        for _single in all_java_pid.split("\n"):
            if name in _single:
                subprocess.getoutput("kill -9 %s" % str(_single).split(" ")[0])

    def check(self):
        for url, name in self.__check_all_url_cmd.items():
            if self.__check(url, name, 1):
                continue

    # @return true表示通过 false表示不通过
    def __check(self, urls, name, count):
        try:
            if count > 3:
                self.__log.info("当前应用%s超过最大重试次数" % name)
                return True
            else:
                req = request.Request(url=urls)
                res = request.urlopen(req)
                html = res.read().decode("utf-8")
                if html != '':
                    self.__log.info("服务%s健康" % name)
                    return True
        except Exception as reason:
            self.__kill(name)
            self.__log.info("杀死进程%s" % name)
            time.sleep(2)
            subprocess.getoutput(
                str.format(self.__command, self.__optimize_params[name], name, self.__profile_params[name]))
            self.__log.info("重新恢复应用%s成功" % name)
            time.sleep(40)
            count += 1
            self.__check(urls, name, count)

    @staticmethod
    def __check_alive(name):
        result = subprocess.getoutput("ps -ef|grep %s|grep -v grep|awk '{print $2}'|head -n 1" % name)
        if result.isdigit():
            return result
        else:
            return 0

    def check_service(self):
        __restart_command = {"zookeeper": "/stock/zookeeper/bin/zkServer.sh start",
                             "redis": "/stock/redis/src/redis-server  /stock/redis/redis.conf",
                             "nginx": "/stock/nginx/sbin/nginx"}

        # __restart_command = {"zookeeper": "/stock/zookeeper/bin/zkServer.sh start",
        #                      "rabbitmq": "nohup /stock/rabbitmq/sbin/rabbitmq-server —detached >/dev/null 2>&1  &",
        #                      "nginx": "/stock/nginx/sbin/nginx"}

        for k, v in __restart_command.items():

            if self.__check_alive(k) is 0:
                self.__log.info("恢复服务%s成功" % k)
                subprocess.getoutput(v)
            else:
                self.__log.info("检测服务%s健康" % k)
