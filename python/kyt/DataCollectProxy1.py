# -*- coding: utf-8 -*-
import os
import re
from urllib import request

from bs4 import BeautifulSoup


def get_new_url():
    header = {"User-Agent":
                  "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36"}
    http_prefix = "http://china.herostart.com/"
    req = request.Request(url=http_prefix, headers=header)
    res = request.urlopen(req)
    html = res.read().decode("gbk")
    soup = BeautifulSoup(html)

    uls = soup.find_all("ul")
    indexs = [17, 27]

    for index in indexs:
        for href in str(uls[index]).split("li"):
            _group = re.compile("(?<=href=\")(.*)\"").search(href)
            if _group is not None:
                url = http_prefix + _group.group(1)
                word = re.compile("[\u4e00-\u9fa5]+").search(href).group()
                if word == "围巾":
                    continue
                try:
                    page = 1
                    while 1:

                        req = request.Request(url=url, headers=header)
                        print(url)
                        res = request.urlopen(req)
                        html = res.read().decode("gbk")
                        soup = BeautifulSoup(html)
                        h3s = soup.find_all("h3")
                        if len(h3s) == 0:
                            break
                        for arr in h3s:
                            try:
                                detail_url = re.compile("(?<=href=\")(http:.*)\"").search(str(arr)).group(1)
                                req = request.Request(url=detail_url, headers=header)
                                res = request.urlopen(req)
                                html = res.read().decode("gbk")
                                soup = BeautifulSoup(html)

                                table = soup.find_all("table")[2].get_text()
                                company = re.compile("(.*)更多产品").search(table).group(1)
                                tel = re.compile("联系电话：\n(.*)").search(table).group(1)
                                address = re.compile("发货地址：\n(.*)").search(table).group(1)
                                name_group = re.compile("联系人：\n(.*)").search(table)
                                name = "无"
                                if name_group is not None:
                                    name = name_group.group(1)
                                new_res = {
                                    "电话: {}  ___姓名: {}  __公司名称:  {}  __地址:    {}".format(tel, name, company, address)}
                                if new_res not in EXIST_DATA:
                                    save(word, new_res)
                                    EXIST_DATA.append(new_res)

                            except Exception:
                                pass
                        page += 1
                        url = re.sub("(-.*)?.html", "-{}.html".format(page), url)

                except Exception:
                    pass


EXIST_DATA = []


def save(word, new_res):
    path = "./data_record"
    if not os.path.exists(path):
        os.makedirs(path)
    file = path + "/{}产品的店所有清单.txt".format(word)
    with open(file, mode="w",
              encoding='utf-8') as f:
        f.write(str(new_res))
        f.write("\n")
        f.write("____________________________________________\n")


if __name__ == "__main__":
    get_new_url()
