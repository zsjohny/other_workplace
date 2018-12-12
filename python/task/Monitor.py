#!/usr/bin/python3
# coding=utf8
import sys
import time
from threading import Thread

from Cpu import Cpu
from Disk import Disk
from Log import Log
from Properties import Properties
from ytj import YTJService

properties = Properties("info.properties").getProperties()


# 报警邮件和来存吧的health
class Task(Thread):
    def __init__(self):
        super(Task, self).__init__()

    def run(self):
        notify_email = properties["notify_email"]
        cpu = Cpu(properties["cpu_monitor_rate"], properties["cpu_over_max_count"], notify_email)

        disk = Disk(properties["all_disk_max_capacity"], properties["single_file_max_capacity"],
                    properties["all_disk_warn_capacity"], properties["direct_remove_file_suffix"],
                    properties["mem_warn_capacity"], log, notify_email)
        ytj = YTJService(log)

        second = int(properties["check_interval"])

        while 1:
            try:
                cpu.monitor()
                log.info("cpu health")
            except Exception as e:
                log.error("cpu检测脚本出错%s", e.with_traceback(sys.exc_info()[2]))
            try:
                disk.cpu_system_info()
                log.info("disk health")
            except Exception as e:
                log.error("磁盘检测脚本出错%s", e.with_traceback(sys.exc_info()[2]))

            try:
                ytj.check()
                log.info("ytj health")
            except Exception as e:
                log.error("磁盘ytj脚本出错%s", e.with_traceback(sys.exc_info()[2]))

            time.sleep(second)


class Monitor:
    def __init__(self):
        pass

    def task(self):
        global log
        global num
        global log_name
        log_name = "monitor.log"

        num = 1
        for n in properties["log_max_size"].split("*"):
            num *= int(n)
        log = Log(log_name, num)
        Task().start()


if __name__ == "__main__":
    Monitor().task()
