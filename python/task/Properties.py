#!/usr/bin/python3
# coding=utf8
class Properties(object):

    def __init__(self, file_name):
        self.__file_name = file_name
        self.__properties = {}

    def __getDict(self, str_name, dict_name, value):

        if str_name.find('.') > 0:
            k = str_name.split('.')[0]
            str_name.setdefault(k, {})
            return self.__getDict(str_name[len(k) + 1:], dict_name[k], value)
        else:
            dict_name[str_name] = value
            return

    def getProperties(self):
        try:
            pro_file = open(self.__file_name, 'r',encoding="utf-8")
            for line in pro_file.readlines():
                line = line.strip().replace('\n', '')
                if line.find("#") != -1:
                    line = line[0:line.find('#')]
                if line.find('=') > 0:
                    strs = line.split('=')
                    strs[1] = line[len(strs[0]) + 1:]
                    self.__getDict(strs[0].strip(), self.__properties, strs[1].strip())
        except Exception as e:
            print(e)
        finally:
            pro_file.close()
        return self.__properties
