import psutil


class Cpu:

    def __init__(self, cpu_high_warn=5, cpu_high_max_count=3, process_name=None):
        self.__cpu_high_warn = cpu_high_warn
        self.__cpu_high_max_count = cpu_high_max_count
        self.__cpu_high_arr = {}
        self.process_name = str(process_name)

    @staticmethod
    def __get_process(p_name):
        process_list = []
        all_pid = psutil.pids()
        for pid in all_pid:
            p = psutil.Process(pid)
            if p_name.lower() in p.name().lower():
                process_list.append(p)
        return process_list

    def kill_process(self):
        process_info = self.__get_process(self.process_name)
        for process in process_info:
            try:
                # 杀死进程
                process.terminate()
            except Exception as e:
                print(e)

    def cpu_monitor(self):
        process_info = self.__get_process(self.process_name)
        for process in process_info:
            # 获取cpu的百分比
            current_cpu = process.cpu_percent(1) / psutil.cpu_count()
            if current_cpu > self.__cpu_high_warn:
                if str(process.pid) not in self.__cpu_high_arr.keys():
                    count = 0
                else:
                    count = int(self.__cpu_high_arr[str(process.pid)])
                count += 1
                if count >= self.__cpu_high_max_count:
                    try:
                        # 杀死进程
                        process.terminate()
                    except Exception as e:
                        print(e)
                else:
                    self.__cpu_high_arr[str(process.pid)] = count
