#!/usr/bin/python3
# coding=utf8
import psutil

from Email import Email


class Cpu:

    def __init__(self, cpu_high_warn=5, cpu_high_max_count=3, notify_email=None, process_name=None):
        self.__cpu_high_warn = float(cpu_high_warn)
        self.__cpu_high_max_count = float(cpu_high_max_count)
        self.__cpu_high_arr = {}
        self.__process_name = process_name
        self.__email = Email(notify_email)

    @staticmethod
    def __get_process(p_name):
        process_list = []
        all_pid = psutil.pids()
        for pid in all_pid:
            p = psutil.Process(pid)
            if p_name is None:
                process_list.append(p)
            elif p_name.lower() in p.name().lower():
                process_list.append(p)
        return process_list

    # 指定名称杀死进程
    def kill_process(self, process_name):
        process_info = self.__get_process(process_name)
        for process in process_info:
            try:
                # 杀死进程
                process.terminate()
                self.__email.send_msg("cpu检测杀死进程", "应用%s占用cpu过高被杀死" % process_name)
            except Exception as e:
                print(e)

    # cpu监控
    def __cpu_monitor(self):
        process_info = self.__get_process(self.__process_name)

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
                        self.__email.send_msg("cpu过高杀死进程", "应用%s经过%d次扫描cpu高达%s被杀死" % (process.name, count, current_cpu))
                    except Exception as e:
                        print(e)
                else:
                    self.__cpu_high_arr[str(process.pid)] = count

    def monitor(self):
        self.__cpu_monitor()
