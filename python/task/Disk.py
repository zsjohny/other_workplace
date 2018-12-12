#!/usr/bin/python3
# coding=utf8
import glob
import os

import psutil

from Email import Email


class Disk:
    def __init__(self, all_file_max_size=60, single_file_max_size=30, disk_high_warn=5, remove_file_suffix=None,
                 mem_high_warn=18, log=None, notify_url=None):
        # mb
        self.__all_file_max_size = float(all_file_max_size)
        self.__single_file_max_size = float(single_file_max_size)
        self.__disk_high_warn = float(disk_high_warn)
        self.__remove_file_suffix = [".log"] if remove_file_suffix is None else str(remove_file_suffix).split(",")
        self.__mem_high_warn = float(mem_high_warn)
        self.__log = log
        self.__email = Email(notify_url)

    def cpu_system_info(self):
        sys_mem = psutil.virtual_memory()
        rate_mem = (sys_mem.total - sys_mem.available) / sys_mem.total * 100
        if rate_mem > self.__mem_high_warn:
            msg = str.format("mem arrived {0}%  should deal with", round(rate_mem))
            print(msg) if self.__log is None else self.__log.warn(msg)
            self.__email.send_msg("内存检测结果", msg)
        # 当前的分区
        space_info = psutil.disk_usage("/")
        if space_info.percent > self.__disk_high_warn:
            self.__disk_count_info("/")
            msg = str.format("disk used {0}% should deal with", space_info.percent)
            print(msg) if self.__log is None else self.__log.warn(msg)
            self.__email.send_msg("磁盘检测结果", msg)

    def __disk_count_info(self, path):
        for _path in glob.glob("%s/*" % path):
            try:
                size = 0
                if os.path.isdir(_path):

                    for root, dirs, files in os.walk(_path, topdown=False):
                        for f in files:
                            size += self.__is_delete_file(_path, os.path.getsize(os.path.join(root, f)))
                    if (size / (1024 * 1024)) > self.__all_file_max_size:
                        self.__disk_count_info(_path)
                elif os.path.isfile(_path):
                    self.__is_delete_file(_path, os.path.getsize(_path))
                else:
                    pass
            except Exception:
                continue

    def __is_delete_file(self, file_name, size):
        if (size / (1024 * 1024)) > self.__single_file_max_size:
            for _suffix in self.__remove_file_suffix:
                if file_name.endswith(_suffix):
                    os.remove(file_name)
                    msg = "delete file %s" % str(file_name)
                    self.__email.send_msg("文件删除提醒", msg)
                    print(msg) if self.__log is None else self.__log.warn(msg)
        return size
