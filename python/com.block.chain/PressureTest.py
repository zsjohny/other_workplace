import re
import socket
import  time
import threading


newLitst = []

def _tcp_connect():
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        port = 1020
        host = "www.etongjin.net"
        client.connect((host, port))

        jsons = {"uid": ""}
        client.send(str("%s_h_%s" % (int(time.time() * 1000), jsons)).encode("utf-8"))

        result = client.recv(1024)
        print(str(result, encoding="utf-8"))
        client.close()

    except Exception as reason:
        print(reason)


if __name__ == '__main__':


    global max
    max = "ready"
    while not re.match("\\d+", max):
        max = input("请输入需要开启的线程数大小:")

    max = int(max)
    # max = cpu_count()

    print("总共%s 个 线程 执行长连接" % max)

    threadList = []
    for i in range(max):
        threadList.append(threading.Thread(target=_tcp_connect))

    for i in threadList:
        i.start()
    for i in threadList:
        i.join()
