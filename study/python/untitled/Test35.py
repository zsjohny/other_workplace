# coding=utf-8
import asyncio


# 利用list进行工作

class Async():
    def __init__(self, loop):
        self.loop = loop
        self.tasks = []

    def register(self, agrs):
        def innerClass(callback):
            print("start register" + agrs)
            # self.tasks.append(callback())
            '''这里使用的是list加上run_until_complete'''
            asyncio.ensure_future(callback())
            '''这里不会直接触发而是配合loop.run_forever()进行使用'''
            print("end register" + agrs)
            return self.tasks

        return innerClass

    def run(self):
        self.loop.run_forever()

    # self.loop.run_until_complete(asyncio.wait(self.tasks))

    def close(self):
        self.loop.close()


asy = Async(asyncio.get_event_loop())


@asy.register("hello1")
async def tast1():
    print("start task1....")
    await asyncio.sleep(1)
    print("end task1....")


@asy.register("hello2")
async def tast2():
    print("start task2....")
    await asyncio.sleep(1)
    print("end task2....")


if __name__ == '__main__':
    asy.run()
