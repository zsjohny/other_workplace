import time as time

from selenium import webdriver

# brower = webdriver.PhantomJS(executable_path=r"C:\Program Files\phantomjs\bin\phantomjs.exe")
# brower = webdriver.Firefox(executable_path="C:\Program Files (x86)\drivers\geckodriver.exe")
# brower = webdriver.Chrome("C:\Program Files (x86)\drivers\chromedriver.exe")
brower = webdriver.Chrome("C:\Program Files (x86)\Google\Chrome\Application\chrome.exe")

# url = "http://www.alimama.com/index.htm"
url = "http://www.alimama.com/index.htm"

brower.get(url)

time.sleep(4)
print("hello")

html = brower.find_element_by_xpath("/html/body/div[1]/div/ul[1]/li[3]/div").tag_name

print("\033[32m")
print("-________")
print(html)
print("\033[0m")
print()




