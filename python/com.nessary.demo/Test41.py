# coding=utf-8
import json
import threading
from urllib import request

# 每个用户的局部变量
keywords = threading.local
keywords = "JVM"


def open_url_csdn():
    haders = {
        "Cookie":
            '''uuid_tt_dd=-8161604059533917427_20170706; bdshare_firstime=1499826536436; UM_distinctid=15d5d8a5d6e2a6-014abea86e3a77-474a0721-15f900-15d5d8a5d6f194; __message_sys_msg_id=0; __message_gu_msg_id=0; __message_cnel_msg_id=0; __message_district_code=000000; __message_in_school=0; UserName=qq_30040941; UserInfo=Zi5GRGgLErA9ZQU98OEPC4JWXKRI3VezqTeMIDKHf5E%2BRbPFmK%2FeLRVd1gKLKOgQQsnurIWN85Fdu571xY1YFyim63siTXDa9iKRLAhhCXf0th3rCsrb6XLYB%2Fe628cY6C%2BFJiT9Nz2t7bYcreAoUg%3D%3D; UserNick=qq_30040941; AU=3FE; UN=qq_30040941; UE="nessary@foxmail.com"; BT=1503752156534; access-token=08584428-edcb-4f7e-b058-7f9e688bec59; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1503545231,1503645022,1503734759,1503743820; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1503752191; dc_tos=ovamm7; dc_session_id=1503734761283_0.2613062326498812'''
        ,
        "User-Agent": "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"

    }
    res = request.Request(
        url="http://my.csdn.net/my/favorite/get_favorite_list?pageno=1&pagesize=10000&username=qq_30040941",
        headers=haders)

    rep = request.urlopen(res)
    lists = json.loads(rep.read())["list"]

    with open("record_csdn.txt", "w+", encoding="utf-8") as f:

        for index, value in dict(lists).items():

            if (keywords in value["title"]):
                f.write("文章标题 %s  ,链接==>:%s" % (value["title"], value["url"]))
                f.write("\n")
                f.flush()


def open_url_jujin():
    headers = {
        "X-Juejin-Client": "1503026520738",
        "X-Juejin-Src": "web",
        "X-Juejin-Token": "eyJhY2Nlc3NfdG9rZW4iOiI1T2R6ZDNqb3c0NWpuZ3Q2IiwicmVmcmVzaF90b2tlbiI6ImlHdTkzVzJoeFZ4TnpVMzIiLCJ0b2tlbl90eXBlIjoibWFjIiwiZXhwaXJlX2luIjoyNTkyMDAwfQ==",
        "X-Juejin-Uid": "5839bd01ac502e006e9eb2a1",
        "User-Agent": "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"

    }

    page = 1
    with open("record_jujin.txt", "w+", encoding="utf-8") as f:

        while (1):

            res = request.Request(

                url="http://user-like-wrapper-ms.juejin.im/v1/user/5839bd01ac502e006e9eb2a1/like/entry?page=%s&pageSize=20" % page,
                headers=headers)

            rep = request.urlopen(res)

            lists = json.loads(rep.read())["d"]["entryList"]

            page += 1

            if page * 20 > 700:
                break

            try:
                for line in lists:

                    if (keywords in line["title"]):
                        f.write("文章标题 %s  ,链接==>:%s" % (line["title"], line["originalUrl"]))
                        f.write("\n")
                        f.flush()
            except:
                continue


if __name__ == '__main__':
    open_url_csdn()

    open_url_jujin()
