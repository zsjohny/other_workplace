from enum import Enum, unique


# 枚举

# 唯一性的校验
@unique
class MyCacheEnums(Enum):
    MONEY = 1
    TUESDAY = 2
    TURIADAY = 2


print(MyCacheEnums.TURIADAY)
print(MyCacheEnums.TURIADAY.value)
