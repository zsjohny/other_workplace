from cx_Freeze import setup, Executable

# Dependencies are automatically detected, but it might need
# fine tuning.
buildOptions = dict(packages = [], excludes = [])

import sys
base = 'Win32GUI' if sys.platform=='win32' else None

executables = [
    Executable('C:\\Users\\Ness\\PycharmProjects\\untitled', base=base, targetName = 'Test5.py')
]

setup(name='1.0',
      version = '1.0',
      description = 'sd',
      options = dict(build_exe = buildOptions),
      executables = executables)
