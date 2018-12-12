--
-- Created by IntelliJ IDEA.
-- User: nessary
-- Date: 2017/12/18
-- Time: 20:09
-- To change this template use File | Settings | File Templates.
--


--初始化脚本
--@limterKey 限定的redis的key
--@limterCount 限定的个数
--@uniqueId 唯一的标识
--@return
local function init(limterKey, limterCount, uniqueId)


    --先检测是否已经设置了唯一标识
    local uniqueIdVal = tostring(redis.pcall("get", "redis:operater:limter"))

    --如果已经设置 进行判断 是否是本机
    if (uniqueIdVal ~= "false") then

        --之前存的唯一符号的标识
        if (uniqueIdVal ~= uniqueId) then
            return "forbid init time in 60s ,please after 60s have try ??"
        else
            --已经设置过无需再次设置
            return "has been setting init "
        end
    end

    --限定的自增的redis的key标识
    local limterIncreaseRedisKey = string.format("redis:increase:%s:limter", limterKey)
    --检查自增值是否归零
    local currentCount = tonumber(redis.pcall("get", limterIncreaseRedisKey))
    --判断是否没有设置或者是超过了当前的最大值
    if (currentCount == nil or currentCount >= limterCount) then
        --重新设置
        redis.pcall("set", limterIncreaseRedisKey, 0)
    end

    --设定操作的唯一标识是自己 保证多台机器启动 只有一台能够作业 通过默认时间保证不被某一台机器强制霸占
    redis.pcall("setex", "redis:operater:limter", 60, uniqueId)

    return "success"
end


-- 获取令牌
--@limterKey 限定的redis的key
--@limterCount 限定的个数
--@currentTime 当前的时间戳
--@return -1 失败 0 成功 具体数字是需要等待的令牌个数
local function acquire(limterKey, limterCount, currentTime)
    --限定的自增的redis的key标识
    local limterIncreaseRedisKey = string.format("redis:increase:%s:limter", limterKey)

    --限定的时间间隔
    local limterMissec = 1000



    --第一次记录的时间的key
    local firstRecordTimeRedisKey = limterIncreaseRedisKey .. ":first_record_time"


    --获取自增记录
    local currentCount = tonumber(redis.pcall("incr", limterIncreaseRedisKey))

    if (currentCount == 1) then
        --记录第一次时间的值
        redis.call("set", firstRecordTimeRedisKey, currentTime)
    end
    --获取第一次记录时间
    local firstRecordTime = tonumber(redis.call("get", firstRecordTimeRedisKey))

    --两者时间差
    local time = currentTime - firstRecordTime

    --判断是否在限定的时间内
    if (time < limterMissec) then

        if (currentCount > limterCount) then
            --返回需要等待的时间
            return time
        end


    else

        --重新设置自增数量
        redis.pcall("set", limterIncreaseRedisKey, 0)
    end



    return "success"
end


-- agrv1 是启动的参数
local method = ARGV[1]


if (method == "init") then
    --传入参数类型 keys 第一个是key值 第二个是限制的大小  第三个是电脑唯一标识
    return init(KEYS[1], tonumber(KEYS[2]), KEYS[3])
elseif (method == "acquire") then
    --传入参数类型 keys 第一个是key值 第二个是限制的大小 第三个是时间戳
    return acquire(KEYS[1], tonumber(KEYS[2]), tonumber(KEYS[3]))
end
