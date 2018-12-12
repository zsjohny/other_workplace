# -*- coding: utf-8 -*-
import json
import os
import random
import re
from urllib import request, parse;

from Properties import Properties

pro = Properties("province.properties")

keysArr = ["1e8c026584273cd7dca6e56744400289", "9a7cdc939c22449b6c76bedebd44d149", "2f52b814f431a00001d4af0f59b183c0",
           "ddce78d564e4beecfcd4be5341b81cec"]


def collect(provity, words, path):
    phone_patten = re.compile('([\\w+,])?(13\d|14[5|7]|15\d|166|17[3|6|7]|18\d)\d{8}([\\w+,]])?')
    keywordArr = words.split(" ")

    area = pro.getProperties(provity).split(" ")
    for city in area:
        resDataText = []
        for keyword in keywordArr:

            page = 1

            url = "http://restapi.amap.com/v3/place/text?key={}&keywords={}&types=&city={}&children=1&offset=20&page={}&extensions=all"

            user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)'

            header = {'User-Agent': user_agent}
            while 1:
                try:
                    req = request.Request(
                        url.format(keysArr[random.randint(0, len(keysArr)-1)], parse.quote(keyword), parse.quote(city),
                                   page),
                        headers=header)
                    response = request.urlopen(req)
                    data = response.read().decode("utf-8")
                    resData = json.loads(data, encoding="utf-8")
                    if len(resData["pois"]) == 0:
                        break
                    for res in resData["pois"]:
                        tel = res["tel"]
                        if str(tel) == "[]" or not re.search(phone_patten, tel):
                            continue
                        new_res = {"电话: {}  __店名:  {}  __地址:    {}".format(tel, res["name"], res["address"])}
                        resDataText.append(new_res)
                except Exception as e:
                    print(e)
                    break

                page += 1

        with  open(path + "\{}{}本地产品的店所有清单.txt".format(provity, city), mode="w+",
                   encoding='utf-8') as f:
            for tmp in resDataText:
                f.write(str(tmp))
                f.write("\n")
                f.write("____________________________________________\n")


if __name__ == '__main__':
    print("每天只能录入 1万条数据")
    path = os.path.expanduser('~') + "\\Desktop\\数据录入"
    if not os.path.exists(path):
        os.makedirs(path)
    provinces = input("请输入您需要录入的城市,回车则是全国省市,多个用空格分开")
    words = input("请输入您需要录入的词汇,多个利用空格分开")
    if words.strip() == '':
        exit(-1)
    if provinces.strip() == '':
        provinces = pro.keys()
    else:
        provinces = provinces.split(" ")

    for province in provinces:
        collect(province, words, path)
