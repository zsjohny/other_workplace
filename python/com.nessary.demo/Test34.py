import time
from concurrent.futures import ThreadPoolExecutor

exec = ThreadPoolExecutor(max_workers=2)


#多线程的并发提交

def do(msg):
    time.sleep(2)
    return msg


res = exec.submit(do, ("hello"))

print(res.done())

time.sleep(3)

print(res.done())
print(res.result())
