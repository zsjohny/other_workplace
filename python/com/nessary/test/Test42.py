from urllib import request


def openUrl():
    headers = {
        "Cookie": "JXCSESSIONID=EA6FB0882ED022015763148A4173905D-n1"    }
    req = request.Request(url="http://jxc.1771.com/getMoney.action ", headers=headers)
    req = request.urlopen(req)
    html = req.read().decode("utf-8")
    print("value %s"%html)

if __name__=="__main__":
    openUrl()