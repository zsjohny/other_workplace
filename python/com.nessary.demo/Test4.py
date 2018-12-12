import time

import pymysql
from selenium import webdriver


def get_content(link):
    url = "http://www.gujiabs.com/mall/article1.html?id=" + link
    driver = webdriver.PhantomJS(executable_path=r"C:\Program Files\phantomjs\bin\phantomjs.exe")
    driver.get(url)
    time.sleep(5)

    data = driver.page_source
    str = "<div class=\"ql-editor\" contenteditable=\"false\">"

    start = data.find(str)

    end = data.find("</div>", start)
    return data[start + len(str):end]


def connect():
    conn = pymysql.connect(host='121.43.59.216', port=3310, user='gjs', passwd='gjs',
                           db='family', charset='utf8mb4',
                           cursorclass=pymysql.cursors.DictCursor)

    with  conn.cursor() as  cur:
        cur.execute("SELECT * FROM `lifeHomeInfo` where articleContent like '{\"ops%'")
        result = cur.fetchall()
        for each in result:
            uuid = each["uuid"]
            print(uuid)
            content = get_content(uuid)
            print("\033[32m")

            cur.execute("update lifeHomeInfo set articleContent =' %s' WHERE  uuid='%s'" % (content, uuid))
            print("%s  生活家 生成成功" % uuid)
            conn.commit()



if __name__ != "__main__":
    url = "http://www.gujiabs.com/mall/article1.html?id=" + '6532451706a357e07dc5a724a83e41a63cef9eeea'
    driver = webdriver.PhantomJS(executable_path=r"C:\Program Files\phantomjs\bin\phantomjs.exe")
    driver.get(url)
    time.sleep(5)

    data = driver.page_source
    data = driver.page_source
    str = "<div class=\"ql-editor\" contenteditable=\"false\">"

    start = data.find(str)

else:
    connect()


