import os

# contains all modules to be imported when "from sites import *" is called
__all__ = []
for file_name in os.listdir(os.path.realpath(__file__).removesuffix("__init__.py")):
    if "__" not in file_name:
        file_name = file_name[:-3]
        __all__.append(file_name)
