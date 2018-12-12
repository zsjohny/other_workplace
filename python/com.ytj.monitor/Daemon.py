import daemon


class Daemon:

    def task(self):
        pass

    # 后置进程
    def main(self):
        # 后置进程
        with daemon.DaemonContext():
            self.task()
