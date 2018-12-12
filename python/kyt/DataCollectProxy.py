import os
import re
import time
from urllib import request

from bs4 import BeautifulSoup
from selenium import webdriver

# record_word = {"围巾",
record_word = {
    "手套", "鞋", "库存服饰", "袜子", "服饰加工", "腰带", "披肩",
    "领带", "服装、服饰代理加盟", "丝巾", "帽子", "成品鞋加工", "领花领结", "鞋材、鞋件加工", "二手服装加工设备"
                                                               "服装机械设备", "服装代理", "制服、工作服", "女装", "服装加工",
    "礼服、婚纱", "男装", "中老年服装", "服装辅料", "民族服装",
    "儿童服装", "库存服装", "运动服装", "孕妇装", "婴儿服装"}


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
            word = uls[index].get_text()
            _group = re.compile("(?<=href=\")(.*)\"").search(href)
            if _group is not None:
                url = http_prefix + _group.group(1)
                try:
                    page = 1
                    while 1:

                        req = request.Request(url=url, headers=header)
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
                                    print(new_res)
                                    save(word, new_res)
                                    EXIST_DATA.append(new_res)

                            except Exception:
                                pass
                        page += 1
                        url = re.sub("(-.*)?.html", "-{}.html".format(page), url)

                except Exception:
                    pass


def get_patent_url():
    driver = webdriver.PhantomJS(executable_path=r"./phantomjs.exe")
    url = "http://china.herostart.com/"
    driver.get(url)
    time.sleep(1)

    uls = driver.find_elements_by_tag_name("ul")
    for arr in uls:
        for url in arr.find_elements_by_tag_name("a"):
            if (url.text in record_word):
                get_child_url(url.get_attribute("href"), url.text)


EXIST_DATA = []


def get_child_url(url, word):
    # page = 1
    page = 4
    driver = None
    url = "http://china.herostart.com/k/cad6ccd7-%s.html" % (page)

    try:
        while 1:
            driver = webdriver.PhantomJS(executable_path=r"./phantomjs.exe")
            driver.get(url)
            print(url)
            time.sleep(1)
            h3s = driver.find_elements_by_tag_name("h3")
            for arr in h3s:
                get_detail_url(arr, word)
            page += 1
            url = re.sub("(-.*)?.html", "-{}.html".format(page), url)
    except Exception as error:
        print(error)
    finally:
        if driver is not None:
            driver.close()


def get_detail_url(arr, word):
    driver = None
    try:
        url = arr.find_elements_by_tag_name("a")[0].get_attribute("href")
        driver = webdriver.PhantomJS(executable_path=r"./phantomjs.exe")
        driver.get(url)
        time.sleep(1)
        table = driver.find_elements_by_tag_name("table")[0].text
        company = re.compile("所属公司：(.*) ").search(table).group(1)
        telgroup = re.compile("联系电话： (.*)(\\n.*)").search(table)
        tel = telgroup.group(1)
        if re.compile("\\d+").match(telgroup.group(2).strip()):
            tel += "---" + telgroup.group(2)
        # {'电话: 03198296678---13930994363  ___姓名: 王贺  __公司名称:   河北日伊丰达羊绒世家有限公司  __地址:    浦江街59号'}
        address = re.compile("发货地址： (.*)").search(table).group(1)
        name_group = re.compile("联系人： (.*)").search(table)
        name = "无"
        if name_group is not None:
            name = name_group.group(1)
        new_res = {
            "电话: {}  ___姓名: {}  __公司名称:  {}  __地址:    {}".format(re.sub("\n", "", tel), name, company, address)}
        if new_res not in EXIST_DATA:
            print(new_res)
            save(word, new_res)
            EXIST_DATA.append(new_res)
    except Exception:
        print(url)
    finally:
        if driver is not None:
            driver.close()


def save(word, new_res):
    path = os.path.expanduser('~') + "\\Desktop\\数据录入"
    if not os.path.exists(path):
        os.makedirs(path)

    with  open(path + "\{}产品的店所有清单.txt".format(word), mode="a+",
               encoding='utf-8', ) as f:
        f.write(str(new_res))
        f.write("\n")
        f.write("____________________________________________\n")


if __name__ == "__main__":
    # get_patent_url()
    # get_child_url("", "手套")
    get_new_url()
