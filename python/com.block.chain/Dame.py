import time
import os
import logging
import daemon


def child_process():
    logging.info("child process's pid: %d" % os.getpid())
    while (1):
        logging.info("child's still alive.")
        time.sleep(1)


def main():
    # DaemonContext 实现了__enter__() 和__exit__()，因此我们可以一句话搞定整个 daemon context
    with daemon.DaemonContext():
        # daemon 目前不支持 log，所以这部分工作只能我们手动初始化
        logging.basicConfig(filename='/var/log/mylog.log', level=logging.INFO)
        child_process()


if __name__ == '__main__':
    main()