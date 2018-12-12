#!/usr/bin/python2.7
# coding:utf-8
import json
import logging
import re
import sched
import threading
import time
import uuid
from urllib import request

import jieba
import pymysql

running = False

logging.basicConfig(level=logging.INFO, format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S',
                    filename='test.log',
                    filemode='w')

key = "xfw3gg91tz"

step = 3


class MyThread(threading.Thread):
    def __init__(self):
        super(MyThread, self).__init__()

    def run(self):
        self.__task(False)

    def exec(self):
        global running
        if not running:
            running = True
        self.__task(True)

    def __openUrl(self, execute=False):
        if not execute:
            return
        else:

            header = {"User-Agent":
                          "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36"}

            conn = openDb()
            urlA = "http://api.dataoke.com/index.php?r=Port/index&type=total&appkey=%s&v=2&page={0}" % key
            # 检测
            try:
                req = request.Request(
                    url=urlA.format(1), headers=header)
                res = request.urlopen(req)
                html = res.read().decode("utf-8")
                jsons = json.loads(html)
                jsons["result"]
            except Exception as reason:
                logging.error("出错了%s" % reason)
                close(conn)
                raise reason

            deleteDb(conn)
            commit(conn)
            close(conn)

            def __inner():
                pageA = 0
                pageB = 0
                pageC = 0
                threads = []

                urlB = "http://api.dataoke.com/index.php?r=Port/index&type=paoliang&appkey=%s&v=2&page={0}" % key
                urlC = "http://api.dataoke.com/index.php?r=Port/index&type=top100&appkey=%s&v=2&page={0}" % key

                threads.append(
                    threading.Thread(target=opens,
                                     args=(pageB, header, openDb(), urlB, SECOND, step, EVERY_TIME_COUNT_COUPON)))
                # threads.append(
                #     threading.Thread(target=opens, args=(pageC, header, openDb(), urlC, THIRD, step, TOP_100_COUPON)))

                for x in threads:
                    x.start()

        threading.Thread(target=__inner).start()

    def __task(self, execute=False):
        self.__openUrl(execute)

    def stop(self):
        global running
        if running:
            running = False
            logging.info("sleep.....")
            time.sleep(60)
            logging.info("wake up.....")


def opens(page, header, conn, url, ids, interval, types):
    while running:
        try:
            page += 1
            req = request.Request(
                url=url.format(
                    page), headers=header)
            res = request.urlopen(req)
            html = res.read().decode("utf-8")
            jsons = json.loads(html)
            ids = saveDb(conn, jsons["result"], ids, interval, types)
        except Exception as reason:
            logging.info(str(reason) + "\n")


def openDb():
    conn = pymysql.connect(host='121.43.59.216', port=3310, user='gjs', passwd='gjs',
                           # conn = pymysql.connect(host='47.91.157.122', port=3310, user='root', passwd='879227577',
                           db='family', charset='utf8mb4',
                           cursorclass=pymysql.cursors.DictCursor)

    return conn


def close(conn):
    conn.close()


def commit(conn):
    conn.commit()


def deleteDb(conn):
    conn.cursor().execute("delete from merchandise where deleted=FALSE and haveCoupon =TRUE  and couponType=5")
    logging.info("数据删除成功")


dicts = {"baseActiveUrl": "https://item.taobao.com/item.htm?id={0}"}

category = "纸巾-居家,厨房用品-居家,家庭清洁-居家,收纳-居家,家纺-居家,家饰-居家,卫浴-居家,宠物-居家,其他-居家,保健品-美食,粮油副食-美食,休闲零食-美食,坚果蜜饯-美食,奶制品-美食,冲饮-美食,糖果-美食,酒-美食,其他-美食,笔-文体,本子-文体,办公-文体,图书-文体,运动装备-文体,健身器械-文体,户外-文体,汽车用品-文体,其他-文体,手机壳-数码,手机膜-数码,耳机-数码,数据线-数码,电脑配件-数码,智能设备-数码,平板配件-数码,生活电器-数码,其他-数码,童装-母婴,童鞋-母婴,玩具-母婴,孕妇用品-母婴,辅食营养-母婴,喂养-母婴,母婴洗护-母婴,婴童出行-母婴,其他-母婴,面膜-美妆,口红-美妆,洁面-美妆,彩妆-美妆,美妆工具-美妆,护肤-美妆,男士护理-美妆,套装-美妆,其他-美妆,男装-服饰,女装-服饰,内衣-服饰,打底裤-服饰,男袜-服饰,女袜-服饰,裙子-服饰,睡衣-服饰,其他-服饰,男鞋-鞋包,女鞋-鞋包,单肩包-鞋包,腰带-鞋包,钱包-鞋包,行李箱-鞋包,拖鞋-鞋包,帽子-鞋包,其他-鞋包"

cateArr = category.split(",")

_coupUrl = "https://uland.taobao.com/coupon/edetail?pid=mm_120668789_20844901_70604448&itemId={0}&activityId={1}"


def cateGory(title):
    for x in cateArr:
        _temArr = x.split("-")
        _splitWord = jieba.cut(_temArr[0].replace("\n", ""), cut_all=False)
        index = 0
        lens = 0

        for x in _splitWord:
            lens += 1

            if x in title:
                name = _temArr[1].replace("\n", "")
                index += 1
        if index == lens:
            return _temArr[0].replace("\n", ""), name
    else:
        return "其他", "其他"


TOP_100_COUPON = 5

EVERY_TIME_COUNT_COUPON = 6

NORMAL_COUPON = 7

FIRST = 1
SECOND = 2
THIRD = 3

Locks = threading.RLock()


def saveDb(conn, jsons, index, step, types=NORMAL_COUPON):
    if not jsons:
        return
    error_count = 1
    try:

        ids = index

        cur = conn.cursor()

        for x in jsons:
            _tempArr = x

            # 检查是否有重复的 去重
            cur.execute("select count(*) from `merchandise` WHERE  thirdId=%s " % _tempArr["GoodsID"])
            res = int(cur.fetchone()["count(*)"])
            if res == 1:
                continue
            elif res > 1:
                cur.execute("delete from `merchandise` WHERE  thirdId=%s  " % _tempArr["GoodsID"])
            else:
                _tempArr = x

                _tempCouponRadio = _tempArr["Commission_jihua"] if _tempArr["Commission_jihua"] > _tempArr[
                    "Commission_queqiao"] else _tempArr["Commission_queqiao"]
                _tempCouponRadio = float(_tempCouponRadio)
                _tempCouponRadio *= 0.01
                _arr = cateGory(_tempArr["Title"])
                _time = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
                if not isinstance(_arr, tuple):
                    raise "解析错误"
                logo = _tempArr["Pic"]

                if re.search("/http", logo):
                    logo = "http%s" % logo.split("/http")[1]

                elif logo.startswith("/"):
                    logo = "http:%s" % logo

                str = (ids, dicts["baseActiveUrl"].format(_tempArr["GoodsID"]), _tempArr["Quan_price"],
                       _tempCouponRadio * float(_tempArr["Quan_price"]), _tempCouponRadio, "", _tempArr["Quan_surplus"],
                       float(_tempArr["Price"]) / float(_tempArr["Org_Price"]),
                       _coupUrl.format(_tempArr["GoodsID"], _tempArr["Quan_id"]), _time, 0, _tempArr["Price"], 1,
                       _tempArr["D_title"], logo, "1",
                       _tempArr["Title"], _tempArr["Org_Price"], _arr[0], _tempArr["Quan_receive"],
                       "tmail" if 0 == _tempArr["IsTmall"] else "tbao", 2 if 1 == _tempArr["IsTmall"] else 1,
                       _tempArr["SellerID"], _arr[1], _time,
                       uuid.uuid4(), time.mktime(time.strptime(_tempArr["Quan_time"], "%Y-%m-%d %H:%M:%S")) * 1000, "",
                       types)

                cur.execute(
                    "INSERT INTO `family`.`merchandise` ( `id`,`activeUrl`, `backChagres`, `backMoneryRatio`, `backRatio`, `classifyName`, `couponCount`, "
                    "`couponOrderRadio`, `couponUrl`, `createTime`, `deleted`, `discountPrice`, `haveCoupon`, `keyWords`, `logo`, `merchandiseType`, "
                    "`name`, `orginalPrice`, `secondCategoryName`, `selleCount`, `sourceName`, `sourceType`, `thirdId`, `topCategoryName`, `updateTime`, "
                    "`uuid`, `expireTime`, `sift`,`couponType`) VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s',"
                    " '%s', '%s', '%s',  b'%d', '%s', b'%d', '%s', '%s', '%s', "
                    "'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s',"
                    " '%s', '%s', '%s','%d')" % str)
                # # 获取最新行插入主键的id
                # index = conn.insert_id() + 1

                ids += step
                commit(conn)
    except  Exception as reason:
        logging.error(reason + "\n")

        error_count += 1
        if error_count == 3:
            return ids

    return ids


DEFAULT_TASK_PROPITY = 0


def openScheduler():
    return sched.scheduler(time.time, time.sleep)


# -------------------------------execute
_schedules = openScheduler()

task = MyThread()


# -------------------------------execute


# 闭包解决
def start(sta=2, inter=60 * 60 * 3):
    # def start(sta=2, inter=40):
    time.sleep(sta)
    task.run()
    __schedule(inter)

    _schedules.run()


def __schedule(inter):
    # 第一个参数是延迟加载的时间 第二个参数是加载的执行的级别  第三个是需要执行的参数 第四个是重复的列表
    _schedules.enter(inter, DEFAULT_TASK_PROPITY, __schedule, (inter,))

    task.stop()

    task.exec()


if __name__ == '__main__':
    start()
