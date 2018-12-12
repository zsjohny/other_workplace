# coding=utf-8
import logging
import os
import tempfile
from urllib import request as res

import pymysql
from bottle import route, run, get, error, static_file, redirect

logging.basicConfig(level=logging.DEBUG, format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S',
                    filename='test.log',

                    filemode='w')

# 获得console输入
console = logging.StreamHandler()
# 自定格式
console.setFormatter(logging.Formatter("%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s"))
# 追加到root
logging.getLogger("").addHandler(console)


def openDb():
    conn = pymysql.connect(host='47.91.157.122', port=3310, user='root', passwd='879227577',
                           db='test', charset='utf8',
                           cursorclass=pymysql.cursors.DictCursor)

    return conn


def close(conn):
    conn.close()


def commit(conn):
    conn.commit()


def init():
    conn = openDb()
    conn.cursor().execute('''
        CREATE TABLE IF NOT EXISTS `user` (
      `id` int(11) NOT NULL AUTO_INCREMENT ,
      `auth` varchar(512) NOT NULL ,
      `used` bit(1) DEFAULT NULL ,
      `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ,
      `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
      ''')
    commit(conn)
    close(conn)

    global savePath
    savePath = tempfile.gettempdir() + "/download"

    if not os.path.exists(savePath):
        os.mkdir(savePath)
    downloadResources = {
        0: "https://nj02all01.baidupcs.com/file/347176466a5f5e958a3d075d7569b70f?bkt=p3-000037d48f4f368cff3155aa31280d7cb887&fid=613120074-250528-233196936032514&time=1498365090&sign=FDTAXGERLBHS-DCb740ccc5511e5e8fedcff06b081203-p2K%2FEsJh4jsCQQRtoA8bBtJQskA%3D&to=69&size=172366763&sta_dx=172366763&sta_cs=0&sta_ft=exe&sta_ct=0&sta_mt=0&fm2=MH,Guangzhou,Netizen-anywhere,,zhejiang,ct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=000037d48f4f368cff3155aa31280d7cb887&sl=76480590&expires=8h&rt=sh&r=940532269&mlogid=4070848058938463837&vuk=282335&vbdid=2227392999&fin=javaIO1.exe&fn=javaIO1.exe&rtype=1&iv=0&dp-logid=4070848058938463837&dp-callid=0.1.1&hps=1&csl=80&csign=ZMLyV6T0L9zkkwFfMOo%2F4sxc4LA%3D&by=themis",
        1: "https://nj02all01.baidupcs.com/file/8b6316fb5193b4b291e91a95e00d5c3d?bkt=p3-0000765e7a55277529be31570d6500e7601a&fid=613120074-250528-1118803972083949&time=1498364982&sign=FDTAXGERLBHS-DCb740ccc5511e5e8fedcff06b081203-uO4QP7fTcw2cAOFnp3SWGugUgGI%3D&to=69&size=184244287&sta_dx=184244287&sta_cs=0&sta_ft=exe&sta_ct=0&sta_mt=0&fm2=MH,Guangzhou,Netizen-anywhere,,zhejiang,ct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=0000765e7a55277529be31570d6500e7601a&sl=76480590&expires=8h&rt=sh&r=267582082&mlogid=4070818958837319657&vuk=282335&vbdid=2227392999&fin=javaIO2.exe&fn=javaIO2.exe&rtype=1&iv=0&dp-logid=4070818958837319657&dp-callid=0.1.1&hps=1&csl=80&csign=ZMLyV6T0L9zkkwFfMOo%2F4sxc4LA%3D&by=themis"

    }
    reName = ["1.exe", "2.exe"]
    for k, v in downloadResources.items():
        if not os.path.isfile(savePath + "/" + reName[k]):
            res.urlretrieve(v, savePath + "/" + reName[k])


def record(func):
    def annoymous(auth, name):
        logging.info("args %s start arrived download %s" % (auth, name))
        result = func(auth, name)
        logging.info("args %s end arrived download %s" % (auth, name))
        return result

    return annoymous


def getResources(auth):
    resources = {"a1": "1", "a2": "2", "a3": "3"
        , "b1": "4", "b2": "5", "b3": "6"}
    try:
        res = resources[auth]
    except:
        res = "error"
    return res


# 静态文件访问
@route('/index')
def index():
    return "you have not auth"


@error(404)
def error(code):
    logging.error("发生错误 %s" % code)
    return "you have not auth "


@get("/download/<auth>/<name>")
@record
def download(auth, name):
    auth = getResources(auth)
    if "error" in auth:
        redirect("/404")
    else:
        conn = openDb()
        cur = conn.cursor()
        cur.execute(" select count(*) from `user` WHERE auth ='%s'" % auth)
        commit(conn)
        if 0 == cur.fetchone()["count(*)"]:
            cur.execute("INSERT INTO `user` ( `auth`, `used`) VALUES ( '%s', b'1')" % auth)
            commit(conn)
            close(conn)
            return static_file(name + ".exe", root=savePath, download=True)
        else:
            close(conn)
            return "你已经下过资源"


if __name__ == '__main__':
    init()
    run(port=9206, host="0.0.0.0", reloader=True)
