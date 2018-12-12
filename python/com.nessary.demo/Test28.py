from multiprocessing.managers import BaseManager

# 多进程相互传递信息

if __name__ == '__main__':
    BaseManager.register("task")
    conn = BaseManager(address=("localhost", 8080), authkey=b'abc')
    conn.connect()
    task = conn.task()
    while 1:
        task.put(2)
