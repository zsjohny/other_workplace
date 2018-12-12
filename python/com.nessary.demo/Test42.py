from bottle import route, template, run


class my:
    def use(self):
        return ("<span style='color:red'>红色</apan>", 2)


@route("/index")
def index():
    m = my()

    return template("index", name="tom", age=24, m=m)


# <!--引进python的书写-->
# %rebase("python.html")


run(port=8058, reloader=True, host="localhost")
