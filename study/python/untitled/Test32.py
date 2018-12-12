import asyncio


# 携程

class Asyncs:
    def __init__(self, loop=None):
        self.loop = loop

    def run(self):
        async def entry():
            print("ready....")
            await asyncio.sleep(2)
            print("doSomething...")

        tasks = [entry(), entry()]

        self.loop.run_until_complete(asyncio.wait(tasks))



        #     def entrys(self, callback):
        #         def inner(*agrs):
        #             print("before....")
        #             result = callback(*agrs)
        #             print("after....")
        #             return result
        #
        #         # ()去掉了
        #         return inner
        #
        #     def close(self):
        #         self.loop.close()
        #
        #
        # a = Asyncs()
        #
        #
        # @a.entrys
        # def test(test):
        #     print("hello")
        #   # 输出结果
        #     '''
        #     before....
        #     after....
        #     hello

        #     '''


        # def entrys(self, auth):
        #         def inner(callback):
        #             def mainClss(*agrs):
        #                 print("before...."+auth)
        #                 result = callback(*agrs)
        #                 print("after....")
        #                 return result
        #
        #             return mainClss
        #
        #         return inner
        #
        #     def close(self):
        #         self.loop.close()
        #
        #
        # a = Asyncs()
        #
        #
        # @a.entrys("1")
        # def test(test):
        #     print("hello")
        #     # 输出结果
        #     '''
        #     before....1
        #     hello
        #     after....
        #     '''

    def close(self):
        self.loop.close()


loop = asyncio.get_event_loop()

asy = Asyncs(loop)

asy.run()
asy.close()
