import json
from urllib import request


def collect():
    header = {"User-Agent":
                  "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36"}

    url = ""

    req = request.Request(
        url=url, headers=header)
    res = request.urlopen(req)
    html = res.read().decode("utf-8")
    jsons = json.loads(html)
    print(jsons)
