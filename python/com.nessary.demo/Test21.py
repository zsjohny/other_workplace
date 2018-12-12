import _thread
import logging
import threading
import timeit
from urllib import request

logging.basicConfig(level=logging.INFO, format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S',
                    filename='test.log',
                    filemode='w')


def openUrl():
    req = request.urlopen("https://www.hao123.com/?tn=95750348_s_hao_pg")
    # req = request.urlopen("http://api.qzgvip.com:9201/user/merchandise/loadMerchandiseByQuery.do")
    html=req.read().decode("utf-8")
    print(html)


def calTime():
    try:

        global times
        _interval = timeit.timeit(openUrl, number=1)
        logging.info(
            "threadName%s method calTime %s" % (threading.current_thread().getName(), _interval))
    except Exception as reason:
        raise reason
        exit(-1)


if __name__ == '__main__':
    lens = 1
    for x in range(lens):
        _thread.start_new_thread(calTime, ())

    while 1:
        pass
