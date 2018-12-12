# -*- coding: utf-8 -*-
import re
from urllib import request, parse;

from bs4 import BeautifulSoup


def collect():
    page = 1
    keywordArr = "女装 服装 时尚品牌 秋冬款 服装设计 组货店 布料 服装加工".split(" ")
    resDataText = []
    area = "江苏%s"
    for key in keywordArr:
        page = 1

        while 1:
            try:

                url = "http://www.qixin.com/search?key={}&page={}"

                user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)'
                dr = re.compile(r'<[^>]+>', re.S)

                phone_pattern = re.compile("([\\w+,])?(13\d|14[5|7]|15\d|166|17[3|6|7]|18\d)\d{8}([\\w+,]])?")
                header = {'User-Agent': user_agent,
                          'Content-Type': 'application/json',
                          "cookie": "cookieShowLoginTip=1; sid=s%3AwgY601ucxh0r8qVfRVYOQ4IJriVNyGj7.ePcA79yf9XLNK96d1mxNdax97Fh6PrfQmscXDNSQBQI; channel=baidu; Hm_lvt_52d64b8d3f6d42a2e416d59635df3f71=1533447826,1534782276,1534817224; responseTimeline=79; _zg=%7B%22uuid%22%3A%20%22165089ce8f4572-047c6c80c03723-541f3715-155498-165089ce8f5153%22%2C%22sid%22%3A%201534815537.622%2C%22updated%22%3A%201534817231.731%2C%22info%22%3A%201534782276156%2C%22cuid%22%3A%20%227570edfd-bec8-4161-a2cd-3c09f02f42c3%22%7D; Hm_lpvt_52d64b8d3f6d42a2e416d59635df3f71=1534817232"}

                req = request.Request(url.format(parse.quote(area % key), page), headers=header)
                response = request.urlopen(req)

                html = response.read().decode("utf-8")

                soup = BeautifulSoup(html)
                data = soup.find_all("span")

                name = ""
                tel = ""
                addr = ""
                last_addr = ""

                if (len(data) <= 4):
                    print("过滤")
                    break
                for strs in data:
                    line = str(strs)

                    if addr != '' and addr != last_addr and tel != '' and name != '':
                        if re.search(phone_pattern, tel):
                            new_res = {"电话: {}  __店名:  {}  __地址:    {}".format(tel, name, addr)}
                            resDataText.append(new_res)
                            addr = last_addr

                    if "历史名称：" in line:
                        name = dr.sub("", line[line.find("历史名称：") + 5:])
                    elif "电话：" in line:

                        tel = dr.sub("", line[line.find("电话：") + 3:])
                    elif "地址：" in line:

                        addr = dr.sub("", line[line.find("地址：") + 3:line.rfind("查看地图")])

            except Exception as  a:
                print(a)
                print("当前词汇%s,当前页数%s" % (key, page))
                with  open("./杭州本地女性产品的店所有清单.txt", mode="w+", encoding='utf-8') as f:
                    for tmp in resDataText:
                        f.write(str(tmp))
                        f.write("\n")
                        f.write("____________________________________________\n")
                return
            page += 1

    with  open("./杭州本地女性产品的店所有清单.txt", mode="w+", encoding='utf-8') as f:
        for tmp in resDataText:
            f.write(str(tmp))
            f.write("\n")
            f.write("____________________________________________\n")


if __name__ == '__main__':
    collect()
