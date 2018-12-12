#!/usr/bin/python2.7
# coding:utf-8
from urllib import request

from bs4 import BeautifulSoup


def openUrl(url=None):
    url = url if url else "http://cuiqingcai.com/1319.html"

    header = {"User-Agent":
                  "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36"}

    req = request.Request(url=url, headers=header)
    res = request.urlopen(req)
    html = res.read().decode("utf-8")
    soup = BeautifulSoup(html)

    #获取属性
    # print(soup.title)
    #只获取文本内容
    # print(soup.title.string)
    #只获取属性
    # print(soup.head.attrs)
    #
    # for link in soup.find_all("a"):
    #     print(link.get("href"))
    # print(soup.get_text())

    #遍历所有子节点
    # print(soup.a.contents)

    # print(soup.find_all("a",limit=2))

    # print(soup.select("title"))
    #通过类名查找
    html="<a class='sister' href='http://example.com/elsie' id='link1'><!-- Elsie --></a>"

    soup=BeautifulSoup(html)
    print(soup.select("#link1"))
    #组合查找
    print(soup.select('p #link1'))
    #属性查找
    print(soup.select('a[class="sister"]'))
if __name__ == '__main__':
    openUrl()
