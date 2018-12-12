import threading

from Test15 import threads

print("不带参数即()的装饰器=======================")


def filter(mainMethod):
    def innerInter(*args, **kwargs):
        print("pre doing.....")
        result = mainMethod(*args, **kwargs)
        print("end doing...")
        return result

    return innerInter


@filter
def join(name, age):
    print("姓名%s,的年龄是%s正在参加活动......." % (name, age))


join("tom", "24")

print("带参数即()的装饰器=========================")


def auth(name):

    print("%s认证成功"%name)


def log(name):


    print("%s日志记录成功" % name)


def filter(auth, log):
    def innerClass(mainMethod):

        def doTask(*args, **kwargs):
            auth(args[0])
            result = mainMethod(*args, **kwargs)
            log(args[0])
            return result

        return doTask

    return innerClass

@threads(100)
@filter(auth,log)
def join(name, age):
    print("姓名%s,的年龄是%s正在参加活动......." % (name, age))



join("jetty", "26")