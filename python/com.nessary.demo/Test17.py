print("#--------------------------直接调用inner")


def outer(func):
    def inner():
        print("认证成功！")
        result = func()
        print("日志添加成功")
        return result

    return inner


@outer
def f1():
    print("业务部门1数据接口......")


print("--------------------------直接调用inner多参数")


def outer(func):
    def inner(*args, **kwargs):
        print("认证成功！")
        result = func(*args, **kwargs)
        print("日志添加成功")
        return result

    return inner


@outer
def f1(name, age):
    print("%s 正在连接业务部门1数据接口......" % name)


# 调用方法
f1("jack", 18)

print("#--------------------------直接调用")


def outer(func):
    print("认证成功！")
    result = func()
    print("日志添加成功")
    return result


@outer
def f1():
    print("业务部门1数据接口......")


print("#--------------------------直接多装饰器")


def outer1(func):
    def inner(*args, **kwargs):
        print("认证成功！")
        result = func(*args, **kwargs)
        print("日志添加成功")
        return result

    return inner


def outer2(func):
    def inner(*args, **kwargs):
        print("一条欢迎信息。。。")
        result = func(*args, **kwargs)
        print("一条欢送信息。。。")
        return result

    return inner


@outer1
@outer2
def f1(name, age):
    print("%s 正在连接业务部门1数据接口......" % name)


# 调用方法
f1("jack", 18)


print("#________________________________装饰器自定义的参数")

# 认证函数
def  auth(request,kargs):
    print("认证成功！")
# 日志函数
def log(request,kargs):
    print("日志添加成功")
# 装饰器函数。接收两个参数，这两个参数应该是某个函数的名字。
def Filter(auth_func,log_func):
    # 第一层封装，f1函数实际上被传递给了main_fuc这个参数
    def outer(main_func):
        # 第二层封装，auth和log函数的参数值被传递到了这里
        def wrapper(request,kargs):
            # 下面代码的判断逻辑不重要，重要的是参数的引用和返回值
            before_result = auth(request,kargs)
            if(before_result != None):
                return before_result;

            main_result = main_func(request,kargs)
            if(main_result != None):
                return main_result;

            after_result = log(request,kargs)
            if(after_result != None):
                return after_result;

        return wrapper
    return outer
# 注意了，这里的装饰器函数有参数哦，它的意思是先执行filter函数
# 然后将filter函数的返回值作为装饰器函数的名字返回到这里，所以，
# 其实这里，Filter(auth,log) = outer , @Filter(auth,log) =  @outer
@Filter(auth,log)
def f1(name,age):

    print("%s 正在连接业务部门1数据接口......"%name)

# 调用方法
f1("jack",18)

