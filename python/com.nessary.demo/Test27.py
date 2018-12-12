# coding=utf-8
import queue
from multiprocessing.managers import BaseManager

# 进程或者服务间相互传递

queues = queue.Queue()


def call():
    return queues


if __name__ == '__main__':
    BaseManager.register("task", callable=call)
    manager = BaseManager(address=("localhost", 8080), authkey=b"abc")
    manager.start()
    task = manager.task()
    task.put("hello")
    while 1:
        print(task.get())
