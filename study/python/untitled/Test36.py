import json
from urllib import request


def openUrl():
    page = 1
    url = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&da_par=direct&pcevaname=pc4.1&qt=spot&from=webmap&c=179&wd=%E9%85%92%E5%90%A7&wd2=&pn={0}&nn={1}0&db=0&sug=0&addr=0&pl_data_type=cater&pl_sub_type=&pl_price_section=0%2C%2B&pl_sort_type=data_type&pl_sort_rule=0&pl_discount2_section=0%2C%2B&pl_groupon_section=0%2C%2B&pl_cater_book_pc_section=0%2C%2B&pl_hotel_book_pc_section=0%2C%2B&pl_ticket_book_flag_section=0%2C%2B&pl_movie_book_section=0%2C%2B&pl_business_type=cater&pl_business_id=&da_src=pcmappg.poi.page&on_gel=1&src=7&gr=3&l=12&rn=50&tn=B_NORMAL_MAP&u_loc=13383898,3510469&ie=utf-8&b=(13385604.03,3492289.39;13392836.03,3543425.39)&t=1496664782500".format(
        page, page)
    while 1:
        req = request.Request(url=url)
        res = request.urlopen(req)
        arr = json.loads(res.read())
        lens = len(arr)

        for line in arr:
            print(line)


if __name__ == '__main__':
    openUrl()
