from urllib import parse, request
import json
import socket
import time
class login:
    base_main_url = "xxx"
    base_sub_url = "xxx"
    DEFAULT_LEN = 4

    @property
    def login(self):
        pass

    @login.setter
    def login(self, login):
        if not isinstance(login, tuple) or len(login) != self.DEFAULT_LEN:
            raise "请输入正确的四位元祖参数"

        name = login[0]
        password = login[1]

        url = self.base_main_url + "user/login"
        values = {"platform": "ios", "imei": "879227577", "userName": name,
                  "password": password, "clientId": "879227577"}
        data = parse.urlencode(values).encode("utf-8")
        res = request.Request(url, data)
        req = request.urlopen(res)
        access_token = req.headers["_x-access-token"]

        if not access_token:
            raise "【金有金】用户名和密码不对"

        global token
        token=access_token

        values = {
            "userID": login[2],
            "password": login[3]
        }

        data = parse.urlencode(values)
        url = self.base_sub_url + "login?%s" % data

        res = request.Request(url)

        res.add_header("_x-access-token",token)

        req = request.urlopen(res)

        result = req.read().decode("utf-8")

        if not json.loads(result)["sessionKey"]:

            raise "【TD】用户名和密码不对"





if __name__ == "__main__":


    login = login()

    login.login = ("13100000062", "a123456", "1089117692", "121966")
