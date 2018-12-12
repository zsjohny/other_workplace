package com.goldplusgold.mq.msgs;

/**
 * 用于测试
 * Created by Administrator on 2017/5/17.
 */
public class TestMsg {
    private String str = "Hello There!";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "TestMsg{" +
                "str='" + str + '\'' +
                '}';
    }
}
