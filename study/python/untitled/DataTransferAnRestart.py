#!/usr/bin/python2.7
# coding:utf-8
import json
import logging
import os
import re
import sched
import threading
import time
import uuid
import paramiko
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

selectFlag = 4


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
                    url=urlA.format("1"), headers=header)
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
                nonlocal urlA
                pageA = 0
                pageB = 0
                pageC = 0
                threads = []

                urlB = "http://api.dataoke.com/index.php?r=Port/index&type=paoliang&appkey=%s&v=2&page={0}" % key
                urlC = "http://api.dataoke.com/index.php?r=Port/index&type=top100&appkey=%s&v=2&page={0}" % key
                if selectFlag == 1:
                    threads.append(
                        threading.Thread(target=opens,
                                         args=(pageA, header, openDb(), urlA, FIRST, step, NORMAL_COUPON)))
                elif selectFlag == 2:
                    threads.append(
                        threading.Thread(target=opens,
                                         args=(pageB, header, openDb(), urlB, SECOND, step, EVERY_TIME_COUNT_COUPON)))
                elif selectFlag == 3:
                    threads.append(
                        threading.Thread(target=opens,
                                         args=(pageC, header, openDb(), urlC, THIRD, step, TOP_100_COUPON)))
                else:
                    threads.append(
                        threading.Thread(target=opens,
                                         args=(pageA, header, openDb(), urlA, FIRST, step, NORMAL_COUPON)))
                    threads.append(
                        threading.Thread(target=opens,
                                         args=(pageB, header, openDb(), urlB, SECOND, step, EVERY_TIME_COUNT_COUPON)))
                    threads.append(
                        threading.Thread(target=opens,
                                         args=(pageC, header, openDb(), urlC, THIRD, step, TOP_100_COUPON)))

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
            # top100只有一页
            if types == TOP_100_COUPON and page > 1:
                logging.info("top100更新完成")
                print("top100已更新完成")
                return
            req = request.Request(
                url=url.format(
                    page), headers=header)
            res = request.urlopen(req)
            html = res.read().decode("utf-8")
            jsons = json.loads(html)
            ids = saveDb(conn, jsons["result"], int(ids), int(interval), types)

        except Exception as reasons:
            logging.info("error:%s\n" % reasons)
            logging.info("json:%s\n" % html)

            time.sleep(20)
            print(reasons)


def openDb():
    conn = pymysql.connect(host='121.43.59.216', port=3310, user='gjs', passwd='gjbs2016',
                           # conn = pymysql.connect(host='47.91.157.122', port=3310, user='pansheng', passwd='879227577',
                           db='family', charset='utf8mb4',
                           cursorclass=pymysql.cursors.DictCursor)

    return conn


def close(conn):
    conn.close()


def commit(conn):
    conn.commit()


def deleteDb(conn):
    if selectFlag == 1:
        removeSql = "AND couponType = 7"
    elif selectFlag == 2:
        removeSql = "AND couponType = 6"
    elif selectFlag == 3:
        removeSql = "AND couponType = 5"
    else:
        removeSql = ""
    cur = conn.cursor()
    cur.execute("delete from merchandise where deleted=FALSE and haveCoupon =TRUE  %s" % removeSql)
    if len(removeSql) > 1:
        cur.execute("select MAX(id)+1 from merchandise where deleted=FALSE and haveCoupon =TRUE ")
        global FIRST
        global SECOND
        global THIRD
        maxId = cur.fetchone()["MAX(id)+1"]
        if maxId:
            FIRST = SECOND = THIRD = maxId

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

    try:

        ids = index

        cur = conn.cursor()

        for x in jsons:
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

            if logo.startswith("http%://s"):
                logo = "http:" + str(logo.split("http%://s")[1])



            str = (ids, dicts["baseActiveUrl"].format(_tempArr["GoodsID"]), _tempArr["Quan_price"],
                   _tempCouponRadio * float(_tempArr["Quan_price"]), _tempCouponRadio, "", _tempArr["Quan_surplus"],
                   float(_tempArr["Price"]) / float(_tempArr["Org_Price"]),
                   _coupUrl.format(_tempArr["GoodsID"], _tempArr["Quan_id"]), _time, 0, _tempArr["Price"], 1,
                   _tempArr["D_title"], logo, "1",
                   _tempArr["Title"], _tempArr["Org_Price"], _arr[0], _tempArr["Quan_receive"],
                   "tmail" if 0 == _tempArr["IsTmall"] else "tbao", 2 if 1 == _tempArr["IsTmall"] else 1,
                   _tempArr["GoodsID"], _arr[1], _time,
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
            logging.info("插入Id%s成功" % _tempArr["GoodsID"])

    except  Exception as reason:
        logging.error("reason:%s\n" % reason)
        print(reason)

    return ids


DEFAULT_TASK_PROPITY = 0


def openScheduler():
    return sched.scheduler(time.time, time.sleep)


# -------------------------------execute
_schedules = openScheduler()

task = MyThread()


# -------------------------------execute


# clear
def clear():
    os.system("curl - XDELETE  http://tools.hengmo.net:8060/merchandises")


clearCount = 0


# 闭包解决
def start(sta=2, inter=60 * 60 * 6):
    # def start(sta=2, inter=40):
    time.sleep(sta)
    task.run()
    __schedule(inter)

    _schedules.run()
    global clearCount
    # 清理搜索引擎
    clearCount += 1
    if clearCount == 2:
        clear()
        clearCount = 0


def __schedule(inter):
    # 第一个参数是延迟加载的时间 第二个参数是加载的执行的级别  第三个是需要执行的参数 第四个是重复的列表
    _schedules.enter(inter, DEFAULT_TASK_PROPITY, __schedule, (inter,))

    task.stop()

    task.exec()



# -------------------------------restart---------------------------------------------------------------------------------
# -------------------------------restart---------------------------------------------------------------------------------

def restartServer(ip, cmd):
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    client.connect(ip, 22, "root", "Gjbs2016")
    stdin, stdout, stderr = client.exec_command(
        "rm -rf  /{0}/jetty && cp -r /remark/jetty /{0}/jetty&&/{0}/jetty/bin/jetty.sh restart".format(cmd))
    cmd_result = stdout.read(), stderr.read()
    for line in cmd_result:
        logging.info("---restart---%s" % line)

    client.close()


def restart():
    print("正在执行服务系统恢复...请稍候,\n执行过程中 请勿关闭程序")
    client = {"120.27.249.102": "opt", "121.43.59.216": "stock"}
    threads = []
    for k, v in client.items():
        threads.append(threading.Thread(target=restartServer, args=(k, v)))
    for x in threads:
        x.start()
    for x in threads:
        x.join()
    print("系统恢复成功.....")


def select():
    result = input("请选择需要执行的命令:1---恢复系统 ,2-----重新导入优惠劵商品,(回车默认是1)\n")
    if result == "2":
        global selectFlag
        sel = input("请选择你需要数据:\n1--普通优惠劵数据\n2--实时跑量优惠劵数据\n3--top100优惠劵数据\n默认是全部更新")
        if sel:
            selectFlag = int(sel)

        logging.info("select flag=%s 正在更新..." % selectFlag)
        print("正在更新....")
        start()

    elif result == "3":
        print("3")
    else:
        restart()


if __name__ == '__main__':
    select()

