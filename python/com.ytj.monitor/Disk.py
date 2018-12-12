import psutil
import os
import glob

class Disk:
    def __init__(self, all_file_max_size=60, single_file_max_size=30, disk_high_warn=5, remove_file_suffix=None,
                 mem_high_warn=18):
        # mb
        self.__all_file_max_size = all_file_max_size
        self.__single_file_max_size = single_file_max_size
        self.__disk_high_warn = disk_high_warn
        self.__remove_file_suffix = [".log", ".zip"] if remove_file_suffix is None else remove_file_suffix
        self.__mem_high_warn = mem_high_warn

    def cpu_system_info(self):
        sys_mem = psutil.virtual_memory()
        rate_mem = (sys_mem.total - sys_mem.available) / sys_mem.total * 100
        if rate_mem > self.__mem_high_warn:
            print(str.format("mem arrived {}% should deal with", round(rate_mem)))
        # 当前的分区
        space_info = psutil.disk_usage("/")
        if space_info.percent > self.__disk_high_warn:
            self.__disk_count_info("E:\\tmp")
            print(str.format("disk used {}% should deal with", space_info.percent))

    def __disk_count_info(self, path):
        for _path in glob.glob("%s\*" % path):
            try:
                size = 0
                if os.path.isdir(_path):

                    for root, dirs, files in os.walk(_path, topdown=False):
                        for f in files:
                            size += self.__is_delete_file(_path, os.path.getsize(os.path.join(root, f)))
                    if (size / (1024 * 1024)) > self.__all_file_max_size:
                        self.__disk_count_info(_path)
                elif os.path.isfile(_path):
                    self.__is_delete_file(_path, os.path.getsize(_path))
                else:
                    pass
            except Exception:
                continue

    def __is_delete_file(self, file_name, size):
        if (size / (1024 * 1024)) > self.__single_file_max_size:
            for _suffix in self.__remove_file_suffix:
                if file_name.endswith(_suffix):
                    os.remove(file_name)
                    print("delete file %s" % file_name)
        return size

