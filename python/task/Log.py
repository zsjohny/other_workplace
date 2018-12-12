#! /usr/bin/python
# -*- coding: utf-8 -*-


import logging
import os


def check(func):
    def __check_file_size(msg, *args):
        file_size = os.path.getsize(file_name)
        if file_size > default_file_size:
            reset()
        result = func(msg, *args)
        return result

    return __check_file_size


def reset():
    with open(file_name, "w+") as f:
        f.truncate()


class Log:
    def __init__(self, log_name, file_size=None):
        # log初始化
        logging.basicConfig(level=logging.INFO,
                            format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                            datefmt='%a, %d %b %Y %H:%M:%S',
                            filemode='w', stream=open(log_name, 'w', encoding='utf-8'))
        self.__log = logging
        global default_file_size
        default_file_size = 1024 if file_size is None else file_size
        global file_name
        file_name = log_name

    @check
    def info(self, msg, *args):
        self.__log.info(msg if args is None else msg % args)

    @check
    def warn(self, msg, *args):
        self.__log.warning(msg if args is None else msg % args)

    @check
    def error(self, msg, *args):
        self.__log.error(msg if args is None else msg % args)


if __name__ == "__main__":
    log = Log("monitor.log")
    log.warn("disk used {0}% should deal with")
