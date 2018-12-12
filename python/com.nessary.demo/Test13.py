import random
import threading
from urllib import request


def open(url=None):
    req = request.Request(url)
    reps = request.urlopen(req)
    html = reps.read().decode("utf-8")
    print(html)


def openThread():
    threads = []
    # url = "http://api.qzgvip.com:9201/user/lifeHomeInfo/findByType.do"
    url = "http://www.sge.com.cn/sjzx/mrhqsj?p=215"
    len = random.randrange(1,10)
    len = 1
    for i in range(len):
        threads.append(threading.Thread(target=open(url)))

    print("______________________________")
    for i in threads:
        i.start()

    for i in threads:
        i.join()


if __name__ == '__main__':
    openThread()
