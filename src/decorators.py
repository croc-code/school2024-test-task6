from exceptions import (ArgumentCountError, CollisionError, DateError,
                        HoursError)


def exception_decorator(func):
    def wrapper(*args, **kwargs):
        try:
            return func(*args, **kwargs)
        except HoursError as error:
            print(f"{error}")
        except ArgumentCountError as error:
            print(f"{error}")
        except CollisionError as error:
            print(f"{error}")
        except DateError as error:
            print(f"{error}")
        return None

    return wrapper
