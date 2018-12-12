#!/usr/bin/python3
# coding=utf8


class Properties(object):

    def __init__(self, file_name):
        self.__file_name = file_name
        self.__properties = {}
        self.__getProperties()

    def __getDict(self, str_name, dict_name, value):
        dict_name[str_name] = value

    def getProperties(self, key):
        for k, v in self.__properties.items():
            if key in k:
                return v

        return self.__properties[key]

    def keys(self):
        return self.__properties.keys()

    def __getProperties(self):
        try:
            pro_file = open(self.__file_name, 'r', encoding="utf-8")
            for line in pro_file.readlines():
                line = line.strip().replace('\n', '')
                if line.find("#") != -1:
                    line = line[0:line.find('#')]
                if "=" in line:
                    strs = line.split('=')
                else:
                    strs = line.split(':')
                strs[1] = line[len(strs[0]) + 1:]
                self.__getDict(strs[0].strip(), self.__properties, strs[1].strip())
        except Exception as e:
            print(e)
        finally:
            pro_file.close()
        return self.__properties
