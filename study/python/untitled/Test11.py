def consumer():
    r = ''
    while True:
        n = yield r
        if not n:
            return
        print("consumer con %s" % n)
        r = "200 ok"


def producer(c):
    next(c)
    n = 0
    while n < 5:
        n += 1
        print("producer pro %s" % n)
        r = c.send(n)
        print("consumer response %s " % r)

if __name__=='__main__':
    c=consumer()
    producer(c)
