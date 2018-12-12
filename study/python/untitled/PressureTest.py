import re
import socket
import struct
import threading

import Market_pb2

newLitst = []
import json

def _tcp_connect():
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        port = 18830
        host = "192.168.1.84"
        host = "tdpqa.goldplusgold.com"
        host = "localhost"
        client.connect((host, port))

        while True:
            try:
                # iden = Market_pb2.Identification()
                # iden.category = Market_pb2.QUOTEMINPAGE
                # body = Market_pb2.Response()
                # # proto repete 的处理 CopyFrom()  MergeFrom()
                # body.identification.CopyFrom(iden)
                # # struct.pack("b", body)
                # client.sendall(body)

                #heart
                heartbeat=Market_pb2.Heartbeat()
                heartbeat.info="TD1111"
                iden = Market_pb2.Identification()
                iden.category = Market_pb2.HEARTBEAT
                body = Market_pb2.Response()
                body.heartbeat.CopyFrom(heartbeat)
                body.identification.CopyFrom(iden)
                print(body)
                client.send(str(body).encode("utf-8"))

                print("线程%s长连接正在接受数据" % (threading.current_thread().getName()))

                result = client.recv(1024)
                print(str(result, encoding="utf-8"))

            except Exception as reason:
                print("服务器链接出错 出错原因 %s" % (reason))
                client.close()
                _tcp_connect()

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
