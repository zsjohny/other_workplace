import time

import pymysql
from selenium import webdriver


def get_content(link):
    url = "http://www.gujiabs.com/mall/fulidetail.html?id=" + link
    driver = webdriver.PhantomJS(executable_path=r"C:\Program Files\phantomjs\bin\phantomjs.exe")
    driver.get(url)
    time.sleep(3)

    data = driver.page_source
    str = "<div class=\"ql-editor\" contenteditable=\"false\">"

    start = data.find(str)
    end = data.find("</div>", start)
    return data[start + len(str):end]


conn = pymysql.connect(host='121.43.59.216', port=3310, user='gjs', passwd='gjs',
                       db='family', charset='utf8mb4',
                       cursorclass=pymysql.cursors.DictCursor)

with  conn.cursor() as  cur:
    cur.execute('SELECT uuid FROM `welfareInfo`')
    result = cur.fetchall()
    for each in result:
        uuid = each["uuid"]
        content = get_content(uuid)
        if 'html' in content:
            continue
        else:
            cur.execute("update welfareInfo set articleContent =' %s' WHERE  uuid='%s'" % (content, uuid))
            print("%s  福利 生成成功" % uuid)
            conn.commit()
