package com.yujj.entity.product;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/26 11:27
 * @Copyright 玖远网络
 */
public class MemberPackageVo{

    private Integer memberType;
    private int timeUnit;
    private Integer value;

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MemberPackageVo{" +
                "memberType=" + memberType +
                ", timeUnit=" + timeUnit +
                ", value=" + value +
                '}';
    }
}
