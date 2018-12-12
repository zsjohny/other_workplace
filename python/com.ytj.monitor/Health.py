import subprocess
from urllib import request

class Health:

    def __init__(self):
        self.__check_all_url_cmd = {"http://127.0.0.1:8081/activity/healths":
                                        "miscroservice_activity", "http://127.0.0.1:8081/activity/healths":
                                        "miscroservice_borrow", "http://127.0.0.1:8081/config/healths":
                                        "miscroservice_config", "http://127.0.0.1:8081/getway/healths":
                                        "miscroservice_getway",
                                    "http://127.0.0.1:8081/user/healths": "miscroservice_user",
                                    "http://127.0.0.1:8081/task_scheduling/healths": "miscroservice_task_scheduling",
                                    "http://127.0.0.1:6898/healths": "miscroservice_register_discovery_centers_v1",
                                    "http://127.0.0.1:6899/healths": "miscroservice_register_discovery_centers_v2"}
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
        self.__commond = "nohup /stock/java/bin/java -jar  {0}  /tmp/{1}.jar  {2} >/dev/null 2>&1 &"
        pass

    def kill(self, name):
        all_java_pid = subprocess.getoutput("jps -l")
        for _single in all_java_pid.split("\n"):
            if name in _single:
                subprocess.getoutput("kill -9 %s" % str(_single).split(" ")[0])

    def check(self):
        for url, name in self.__check_all_url_cmd.items():
            try:
                req = request.Request(url=url)
                res = request.urlopen(req)
                html = res.read().decode("utf-8")
                if html != '':
                    print("health")
                    continue
            except Exception as reason:
                self.kill(name)
                time.sleep(2)
                SSH().connec(
                    str.format(self.__commond, self.__optimize_params[name], name, self.__profile_params[name]))

    def check_alive(self, name):
        result = subprocess.getoutput("ps -ef|grep %s|grep -v grep|awk '{print $2}'|head -n 1" % name)
        if result.isdigit():
            return result
        else:
            return 0

    def kill_by_process(self, pid):
        subprocess.getoutput("kill -9 %s" % pid)

    def check_service(self):
        __restart_command = {"zookeeper": "/stock/zookeeper/bin/zkServer.sh start",
                             "rabbitmq": "nohup /stock/rabbitmq/sbin/rabbitmq-server —detached >/dev/null 2>&1  &",
                             "redis": "/stock/redis/src/redis-server  /stock/redis/redis.conf",
                             "nginx": "/stock/nginx/sbin/nginx"}

        for k, v in __restart_command.items():

            if self.check_alive(k) is 0:
                print("恢复%s" % k)
                subprocess.getoutput(v)
            else:
                print("服务%s健康" % k)

