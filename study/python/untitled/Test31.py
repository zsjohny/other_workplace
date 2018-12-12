# 元组的操作

from collections import namedtuple
if __name__=="__main__":
    Node = namedtuple("Node", ["name", "age"])

    node = Node._make(["tom", 23])

    print(node.age)
