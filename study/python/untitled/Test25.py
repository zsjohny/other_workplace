from bottle import route, run, template, get


@route("/hello/<name>")
def index(name):
    print("1")
    return template("<b>hello {{name}}</b>}", name=name)


@get("/index/<hello>")
def hello(hello):
    return template("<b>{{a}}", a=hello)


@get("/index")
def hello():
    return {"hello": "ks"}


run(port=8080, reloader=True)
