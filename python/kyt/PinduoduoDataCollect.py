import time

from selenium import webdriver

if __name__ == '__main__':
    driver = webdriver.PhantomJS()

    driver.set_window_size(800, 600)

    url = "https://uland.taobao.com/quan/detail?sellerId=2074593329&activityId=894957b4721846f88e5d76c69c3fc920"

    driver.get(url)
    cookie = {'domain': 'uland.taobao.com', 'httponly': False,
              'name': 'cookie1', 'path': '/', 'secure': False,
              'value': 'AVS1SZn0W1IKVLM14ii50Z1GpWXmWN618LIpRCE%2B%2BJA%3D'}
    time.sleep(2)
    # driver.add_cookie(cookie)
    driver.find_element_by_id("getCouponBtn").click()

    html = driver.page_source
    with open("login.html", "w+", encoding="utf-8") as f:
        f.write(html)
