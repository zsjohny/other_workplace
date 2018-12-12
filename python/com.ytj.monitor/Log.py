import logging
import os

def check(func):
    def __check_file_size(msg, *args):
        __default_file_size = 1024
        file_size = os.path.getsize(file_name)
        if file_size > __default_file_size:
            reset()
        result = func(msg, *args)
        return result

    return __check_file_size


def reset():
    with open(file_name, "w+") as f:
        f.truncate()


class Log:
    def __init__(self, log_name):
        # log初始化
        logging.basicConfig(level=logging.INFO,
                            format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                            datefmt='%a, %d %b %Y %H:%M:%S',
                            filename=log_name,
                            filemode='w')
        self.__log = logging

        global file_name
        file_name = log_name

    @check
    def info(self, msg, *args):
        self.__log.info(msg % args)

    @check
    def warn(self, msg, *args):
        self.__log.warning(msg % args)

    @check
    def error(self, msg, *args):
        self.__log.error(msg % args)


