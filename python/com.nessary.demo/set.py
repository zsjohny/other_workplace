import sys
from cx_Freeze import setup, Executable

setup(
    name = "test",
    version = "3.6.1",
    description = "A Dijkstra's Algorithm help tool.",
    executables = [Executable("Test5.py", base = "Win32GUI")])