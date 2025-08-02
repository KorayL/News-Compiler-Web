import pkgutil
import importlib
import inspect

from src.Site import Site

# contains all modules to be imported when "from sites import *" is called
__all__ = []

for finder, modname, ispkg in pkgutil.walk_packages(__path__, prefix=__name__ + "."):

    # Modules that start with an underscore are considered private and not imported
    if modname.split(".")[-1].startswith("_"):
        continue

    try:
        # Attempt to import the module
        module = importlib.import_module(modname)
    except ImportError as e:
        # If the module cannot be imported, print an error message and continue
        print(f"Error importing module {modname}: {e}")
        continue

    # Check if the module has classes that are subclasses of Site
    for name, obj in inspect.getmembers(module, inspect.isclass):
        if issubclass(obj, Site) and obj is not Site and obj.__module__ == modname:
            globals()[name] = obj
            __all__.append(name)
