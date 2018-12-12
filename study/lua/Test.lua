---
--- Created by Ness.
--- DateTime: 2017/6/18 23:51
---

--单行注释
--print("hello single line annotation")

-- ===============================================

--[[
多行注释
--]]

--print("hello Im multiLine annotation")

-- ===============================================
--[[
数据类型

boolean
string
table

print(type(false))
print(type("string"))
print(type({}))
--[[print([[Im multiLine String

 -----

]]--[[)]]

--]]
-- ===============================================

--[[

 链接符
print(1 +2)
print("2" +"4")
--print("3"+"error")--error
print("3".."error")
print(#"string")--长度


tables = {}
tables["key"] = "value"
tables["keys1"] = "2"



--for i, v in ipairs(tables)
--ipairs只会输出正整数的key
for k, v in pairs(tables)
--pairs会把所有的k--v输出
do
    print(i..":"..v)
end


--]]

-- ===============================================

--[[
 函数

function myFirstFunc(name)
    print("hello".." : "..name)
end


myFirstFunc("tom")


function sumCalc(number)
    if number <=0 then
        return 1

    else

        return number*sumCalc(number-1)

    end


end


print(sumCalc(5))

--]]

-- ===============================================

--[[
变量
先计算等式后边在计算等式左边

x, y = 1, 10
x, y = y, x

print(x..":"..y)



function returnCacl(a, b)
    return b, a

end


x, y = returnCacl(1, 2)
print(x..":"..y)


--]]

-- ===============================================



--[[


循环
a = 10

while a>2
do
    a = a-1
end
print(a)


if a ==1 then
    print("------1-------")
elseif a==2 then
    print("------2-------")
else
    print("------other-------")
end





function variable(...)
    local var = { ... }
    for i, v in ipairs(var) do
        print(v)
    end
end


variable(1.1, 2.2, 5, 5)



if 3 ~=2 then
    print("3 not equal 2")
end


--]]

-- ===============================================

--[[
字符串的用法

--大写
print(string.upper("I`m  taking "))

--小写
print(string.lower("I`m lowing"))

--查找
print(string.find("I`m talking  talk", "talk"))

--格式化
print(string.format("I`m age %d  ,name is %s", 4, "tom"))

--字符串长度
print(string.len("I`m  "))

--正则 %表示转译字符

--[[.(点): 与任何字符配对
%a: 与任何字母配对
%c: 与任何控制符配对(例如\n)
%d: 与任何数字配对
%l: 与任何小写字母配对
%p: 与任何标点(punctuation)配对
%s: 与空白字符配对
%u: 与任何大写字母配对
%w: 与任何字母/数字配对
%x: 与任何十六进制数配对
%z: 与任何代表0的字符配对]]

--[[print(string.match("sss", '%d'))

pairs: 迭代 table，可以遍历表中所有的 key 可以返回 nil
ipairs: 迭代数组，不能返回 nil,如果遇到 nil 则退出]]

--]]
-- ===============================================


--[[
表格
fruits = { "banana", "apples" , "orange" }

for i, v in pairs(fruits) do
    print(v)
end

--排序后

table.sort(fruits)

for i, v in pairs(fruits) do
    print(v)
end



--]]

-- ===============================================
--[[

 模块
 require("model")
model.model1()

--私有
require("model")
model.model2()

 --]]
-- ===============================================

--[[

元素

--setmetatable 用{}

my = setmetatable({ key1 = "value1" }, {

    __index = {
        key2 = "value2"

    }
})

print(my.key2)



--setmetatable 用的是函数

mytable = setmetatable({ key1 = "value1" }, {
    __index = function(mytable, key)
        if key == "key2" then
            return "metatablevalue"
        else
            return nil
        end
    end
})

print(mytable.key1, mytable.key2)


--temp

temptable = {

}

my = setmetatable(
{ keys1 = "value1" }, {
    __index = {
        keys2 = "value2"

    },
    --存在的时候就在这里找index
    __newindex = temptable
    --新增的时候在newindex
}

)


my.keys3 = "values3"

print(my.keys3)
--在tmp的列表中
print(temptable.keys3)
print(my.keys2)


--]]

-- ===============================================


--[[

 协程

c0 = coroutine.create(
function(n)
    print(n)
end
)

coroutine.resume(c0, 4)

c1 = coroutine.create(

function()

    for k = 0, 10, 1 do
        print(k)
        if k==3 then
            print(coroutine.running())
            print(coroutine.status(c1))
        end
        coroutine.yield()
    end

end

)

coroutine.resume(c1)
coroutine.resume(c1)
coroutine.resume(c1)
coroutine.resume(c1)
coroutine.resume(c1)


--]]


-- ===============================================

--[[
文件读取
file = io.open("test.txt", "r+")

--设为默认文件,下面所有的所有操作都对改操作进行
io.input(file)

print(io.read())
print(io.read())

--设置默认输出文件
io.output(file)
io.write("\n-----注释")

--关闭输入流
io.close(file)



--]]

-- ===============================================

--[[
错误处理
function name(n)

    assert(type(n)=='number', "not number")

end


name("2")
--catch

function testError(i)
    print(i)
end

if pcall(testError(1) ) then
    print("have incorrect")
else
    print("have error")
end




--]]










