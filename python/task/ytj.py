#!/usr/bin/python3
# coding=utf8
from Log import Log
from YtjHealth import Health


class YTJService:

    def __init__(self, log=Log):
        self.__log = log

    def check(self):
        health = Health(self.__log)
        try:
            health.check()
        except Exception as e:
            msg = "检测java应用出错"
            print(msg) if self.__log is None else self.__log.warn(msg)
        try:
            health.check_service()
        except Exception as e:
            msg = "检测服务出错"
            print(msg) if self.__log is None else self.__log.warn(msg)
