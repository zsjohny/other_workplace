# encoding=utf8
import json
from urllib import request

import pymysql
from selenium import webdriver


def openUrl(index):
    page = 1
    keyword = ["酒吧", "KTV", "电影院", "健身房"]
    word = keyword[int(index)]

    try:

        search = request.quote(word)
        # base_url = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&da_par=direct&pcevaname=pc4.1&qt=spot&from=webmap&c=179&wd={0}&wd2=&pn={1}&nn={2}0&db=0&sug=0&addr=0&pl_data_type=cater&pl_sub_type=&pl_price_section=0%2C%2B&pl_sort_type=data_type&pl_sort_rule=0&pl_discount2_section=0%2C%2B&pl_groupon_section=0%2C%2B&pl_cater_book_pc_section=0%2C%2B&pl_hotel_book_pc_section=0%2C%2B&pl_ticket_book_flag_section=0%2C%2B&pl_movie_book_section=0%2C%2B&pl_business_type=cater&pl_business_id=&da_src=pcmappg.poi.page&on_gel=1&src=7&gr=3&l=12&rn=50&tn=B_NORMAL_MAP&u_loc=13383898,3510469&ie=utf-8&b=(13385604.03,3492289.39;13392836.03,3543425.39)&t=1496664782500"
        base_url = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&da_par=direct&pcevaname=pc4.1&qt=spot&from=webmap&c=179&wd={0}&wd2=&pn={1}&nn={2}&db=0&sug=0&addr=0&district_name=%E5%85%A8%E9%83%A8&business_name=&pl_data_type=life&pl_sub_type=KTV&pl_price_section=0%2C%2B&pl_sort_type=default&pl_sort_rule=0&pl_discount2_section=0%2C%2B&pl_groupon_section=0%2C%2B&pl_cater_book_pc_section=0%2C%2B&pl_hotel_book_pc_section=0%2C%2B&pl_ticket_book_flag_section=0%2C%2B&pl_movie_book_section=0%2C%2B&pl_business_type=life&pl_business_id=&da_src=pcmappg.poi.page&src=7&l=12.257690563916007&rn=50&tn=B_NORMAL_MAP&u_loc=13380918,3506061&ie=utf-8&b=(13335564.21875,3509094.8125;13417038.78125,3530293.1875)&t=1497339446501"

        url = base_url.format(
            search, page, page)

        conn = openDb()
        while 1:
            req = request.Request(url=url)
            res = request.urlopen(req)
            jsons = json.loads(res.read())

            arr = jsons["content"]

            cur = conn.cursor()
            for line in arr:
                if "tel" not in line:
                    print("%s 过滤.." % line["name"])
                    continue
                point = conver_point(line["x"], line["y"])
                print("%s 记录成功.."%line["name"])
                strs = (line["name"], word, line["tel"], "浙江省", "杭州市",
                        str(line["area_name"]).split("市")[1],
                        line["addr"], float(point[0]), float(point[1]),
                        line["tel"])

                cur.execute(
                    "INSERT INTO `wuai`.`t_maps` (`name`,`scene`,`phone`,`province`, `city`, `district`, `address`, `longitude`, `latitude`) SELECT  '%s','%s','%s','%s','%s', '%s', '%s', '%.6f', '%.6f' FROM dual WHERE  NOT  EXISTS (SELECT id FROM  `wuai`.`t_maps` WHERE phone ='%s' ) " % strs)
                commit(conn)
            page += 1
            url = base_url.format(search, page, page)
    except Exception as e:
        print(e)



def openDb():
    conn = pymysql.connect(host='120.55.67.48', port=3310, user='wuai', passwd='2017',
                           db='wuai', charset='utf8mb4',
                           cursorclass=pymysql.cursors.DictCursor)

    return conn


def close(conn):
    conn.close()


def commit(conn):
    conn.commit()


def conver_point(x, y):
    driver = webdriver.PhantomJS(executable_path=r"./phantomjs.exe")
    driver.get("http://52Woo.com:9203/conver.html?x=%s&y=%s" % (x, y))
    point = driver.find_element_by_tag_name("body").text
    return str(point).split(":")


import execjs
import math


def execConver(conver_x, conver_y):
    mercator = {"x": conver_x, "y": conver_y}

    lonlat = {}

    x = mercator['x'] / 20037508.3427892 * 180
    y = mercator['y'] / 20037508.3427892 * 180

    lonlat['x'] = x
    lonlat['y'] = 180 / math.pi * (2 * math.atan(math.exp(y * math.pi / 180)) - math.pi / 2)

    print(lonlat)


# python执行js

def exec():
    # jscript=execjs.get(execjs.runtime_names.JScript)
    # print(jscript.eval("1 + 2"))

    # result = execjs.compile('''
    #
    # <script type="text/javascript"
    #                 src="http://api.map.baidu.com/api?v=2.0&ak=MclkIs6Tv09tXD4LDPHVTMg5coXEphDz"></script>
    #
    #     <script type="text/javascript">
    #
    #     function test(){
    #
    #      var map = new BMap.Map("allmap");
    #     var b = new BMap.MercatorProjection().pointToLngLat(new BMap.Pixel(13364311.41,351967701));
    #
    #     return b.lng + "," + b.lat
    #
    #     }
    #    </script>
    #   ''').call("test")
    # print(result)

    print(
        execjs.get().name)

    result = execjs.compile('''
    
    <script type="text/javascript"
                    src="http://api.map.baidu.com/api?v=2.0&ak=MclkIs6Tv09tXD4LDPHVTMg5coXEphDz"></script>

        <script type="text/javascript">

        function test(){

         var map = new BMap.Map("allmap");
        var b = new BMap.MercatorProjection().pointToLngLat(new BMap.Pixel(13364311.41,351967701));
        
        return b.lng + "," + b.lat 
        
        }
       </script>
      ''').call("test")
    print(result)


if __name__ == '__main__':
    index = input("请输入需要执行的数字 \n 0-酒吧 1-KTV 2-电影院 3-健身房\n")

    openUrl(index)
