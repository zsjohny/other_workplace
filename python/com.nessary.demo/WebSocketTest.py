
import time
import re
from websocket import create_connection

import threading

Lock = threading.RLock()


def _on_message():
    '接受参数'
    while True:
        try:
            Lock.acquire()

            # print("\033[32m")
            msg = {"category": "ALLINSTQUOTE", "instType": "AU_TD", "index": 0}
            ws.send(str(msg))

            print("当前线程名称{0}\nwebsocket正在接受{1}".format(threading.current_thread().name, ws.next()))
            # print("\033[1m")
            time.sleep(0.5)
            Lock.release()
        except Exception as reason:
            print("当前出错原因 %s" % reason)
    else:
        print("连接超时")


newLitst = []


if __name__ == '__main__':
    global max
    max = "ready"
    while not re.match("\\d+", max):
        max = input("请输入需要开启的线程数大小:")

    max = int(max)
    # max = cpu_count()

    print("总共%s 个线程 执行socket连接" % max)

    global ws
    ws = create_connection("ws://192.168.1.84:18831/market")
    print("### opemn ###")
    time.sleep(2)
    msg = {"category": "ALLINSTQUOTE", "instType": "AU_TD", "index": 0}
    ws.send(str(msg))

    threadList_ = []

    for i in range(max):
        threadList_.append(threading.Thread(target=_on_message))

    for i in threadList_:
        i.start()
    for i in threadList_:
        i.join()
