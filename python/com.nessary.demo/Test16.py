import os
import re
import time
from urllib import request

from  openpyxl import Workbook
from selenium import webdriver


def __operateExcel(title, content, fileName="上海金交所历史行情数据.xlsx"):
    if len(content) == 0:
        print("不进行处理")
        return
    if os.path.exists(fileName):
        os.remove(fileName)
    wb = Workbook()
    # 激活 worksheet
    ws = wb.active
    # 可以附加行，从第一列开始附加
    # 去除空白数据并且进行标识
    _orginal = []
    _markCount = []
    for i, v in enumerate(title):
        if str(v).strip() in '':
            _markCount.append(i)
        else:
            _orginal.append(v)

    ws.append(_orginal)

    lens = len(title)

    _tempContent = []
    _indexA = 0
    __temContain = False
    for x in content:

        _indexA += 1
        # 先判断
        if _indexA % lens == 0:
            # 判断是否追加
            if __temContain:
                ws.append(_tempContent)
            _tempContent.clear()
            _indexA = 0

        # 更换合约名称
        if _indexA == 2:

            for k, v in fixedDic.items():
                t = re.search(k, x)
                if t:
                    x = v
                    __temContain = True
                    break
                else:
                    __temContain = False

        _markFlag = False

        for k in _markCount:
            _indexB = k + 1

            if _indexA % _indexB == 0:
                _markFlag = True
                break

        if _markFlag:
            continue

        _tempContent.append(x)

    # 保存文件
    wb.save(fileName)


fixedDic = {"[mM][Aa][Uu][(]?[Tt][+][Dd][)]?": "mAu(T+D)", "[Aa][Uu][(]?[Tt][+][Dd][)]?": "Au(T+D)",
            "[Aa][Gg][(]?[Tt][+][Dd][)]?": "Ag(T+D)"}


def openChrom(list, url=None, startTime=None, endTime=None):

    if not startTime:
        startTime = time.strptime("2008-8-9", "%Y-%m-%d")
    else:
        startTime = time.strptime(str(startTime), "%Y-%m-%d")
    if not endTime:
        endTime = time.localtime(time.time())
    else:
        endTime = time.strptime(str(endTime), "%Y-%m-%d")

    url = url if url else "http://www.sge.com.cn/{0}"
    if not list or len(list) == 0:
        raise "请填写正确的list"

    driver = webdriver.PhantomJS(executable_path=r"C:\Program Files\phantomjs\bin\phantomjs.exe")

    # explainDic = {"合约": "ctype", "开盘价": "open", "最高价": "high", "最低价": "low", "收盘价": "close", "涨跌": "", "涨跌幅": "",
    # explainDic = {"合约": "ctype", "开盘价": "open", "最高价": "high", "最低价": "low", "收盘价": "close", "涨跌": "", "涨跌幅": "",
    explainDic = {"合约": "ctype", "开盘价": "open", "最高价": "high", "最低价": "low", "收盘价": "close", "涨跌": "",
                  "加权平均价": "",
                  "成交量": "volume", "成交金额": "amount", "持仓量": "", "交收量": ""}
                  # "成交量": "volume", "成交金额": "amount", "持仓量": ""}
    # explainArr = ["time", "ctype", "open", "high", "low", "close", "", "", "", "volume", "amount", "", ""]
    # explainArr = ["time", "ctype", "open", "high", "low", "close", "", "", "", "volume", "amount", ""]
    explainArr = ["time", "ctype", "open", "high", "low", "close", "", "",  "volume", "amount", "", ""]

    totalArr = []

    lens = len(explainDic)

    for x in list:
        # 获取资源
        driver.get(url.format(x))
        body = driver.find_element_by_xpath("//table[@border='0']")

        # 获取时间
        # time--------
        # xxx/xx/xx
        times = driver.find_element_by_xpath("//h1[contains(.,'上海黄金交易所')]")

        str(times.text)
        expactTime = time.strptime(str(times.text)[7:-4], "%Y年%m月%d日")
        # 判断时间是否符合需要选中的时间
        if not startTime <= expactTime <= endTime:
            print("时间不符合规则")
            continue

        text = time.strftime("%Y/%m/%d", expactTime)
        # time---------------------


        __incre = 0

        while True:
            __incre += 1

            try:

                _tempTag = body.find_elements_by_tag_name("tr")[__incre]

                _tempArr = str(_tempTag.text).split("\n")

                _index = 0
                _temp = []

                for kv in _tempArr:

                    _index += 1

                    _temp.append(kv.replace(",", ""))

                    # 读取数据
                    if _index == lens:
                        # 重新初始化
                        _index = 0
                        # 插入时间
                        _temp.insert(0, text)
                        # 进行解析处理

                        totalArr.extend(_temp)

                        _temp.clear()




                        # for t in explainSet:
            except Exception as reason:
                break
    else:
        print("into")

        __operateExcel(explainArr, totalArr)


def openPage(url=None, start=1, end=3):
    if not re.search("\d+", str(start)):
        raise "请输入正确的开始页数"
    if not re.search("\d+", str(end)):
        raise "请输入正确的结束页数"

    url = str(url).split("?")[0] + "?p=%s" if url else "http://www.sge.com.cn/sjzx/mrhqsj?p=%s"

    _tempUrl = url.replace("%s", "{0}")

    quotaList = []
    for i in range(start, end):
        url = _tempUrl.format(i)
        res = request.urlopen(url)
        body = res.read().decode("utf-8")
        arr = re.findall("/sjzx/mrhqsj/\d+[?]top=\d+", str(body))

        quotaList.extend(arr)

    return quotaList


if __name__ == '__main__':
    # startPage = input("请输入开始页数 回车是默认页数")
    # endPage = input("请输入结束页数 回车是默认页数")
    # startDate = input("请输入开始日期格式xxxx-x-x 回车是默认日期")
    # endDate = input("请输入结束日期格式xxxx-x-x 回车是默认日期")
    #
    # openChrom(openPage(start=startPage if startPage else 198, end=endPage if endPage else 213),
    #           startTime=startDate if startDate else "2008-8-8", endTime=endDate if endDate else "2009-3-20")


    openChrom(openPage(start=211, end= 213),startTime= "2008-8-8",endTime= "2009-3-20")

    # print(re.search("[Aa][Uu][(]?[Tt][+][Dd][)]?", "Au(t+d)"))

    # openChrom(["/sjzx/mrhqsj/509696?top=789398439266459648"])
    # __operateExcel(['time', 'ctype', 'open', 'high', 'low', 'close', '', '', '', 'volume', 'amount', '', ''],
    #
    #                ['2008/09/19', 'AuT+D', '193.39', '196.48', '184.25', '184.75', '0.50', '0.0027', '190.73', '27970',
    #                 '5334733620.00', '45810', '2178', '2008/09/19', 'Au(T+N1)', '187.98', '188.07', '185.73', '185.77',
    #                 '1.51',
    #                 '0.0082', '187.02', '168', '31420200.00', '2', ' ', '2008/09/19', 'Au(T+N2)', '187.00', '187.00',
    #                 '187.00',
    #                 '187.00', '14.67', '0.0851', '187.00', '600', '112200000.00', '504', ' ', '2008/09/19', 'Ag(T+D)',
    #                 '3044.00',
    #                 '3044.00', '2925.00', '2939.00', '86.00', '0.0301', '3000.00', '54514', '163555140.00', '110180',
    #                 ' '])
