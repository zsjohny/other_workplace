package com.jiuyuan.constant;

/**
 * 会员套餐类型
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/8/14 20:47
 * @Copyright 玖远网络
 */
public enum MemberPackageType{


    /**
     * 会员版
     */
    MEMBER (2, "member", "memberPackageType"),
    /**
     * 至尊版
     */
    SUPREMACY (3, "supremacy", "memberPackageType")
    ;

    /**
     * 套餐类型 2.会员套餐,3.至尊套餐
     */
    private int type;
    /**
     * 套餐对应字典表的code
     */
    private String dictCode;
    /**
     * 套餐对应字典表的group_code
     */
    private String dictGroupCode;
    /**
     * 申请会员使用年限
     */
    public static final int USER_YEAR = 1;

    MemberPackageType(int type, String dictCode, String dictGroupCode) {
        this.type = type;
        this.dictCode = dictCode;
        this.dictGroupCode = dictGroupCode;
    }


    /**
     * 通过类型获取会员套餐
     *
     * @param packageType packageType
     * @return com.jiuyuan.constant.MemberPackageType
     * @author Charlie
     * @date 2018/8/15 13:45
     */
    public static MemberPackageType getByType(Integer packageType) {
        if (packageType == null) {
            return null;
        }
        for (MemberPackageType type : MemberPackageType.values ()) {
            if (type.type == packageType) {
                return type;
            }
        }
        return null;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictGroupCode() {
        return dictGroupCode;
    }

    public void setDictGroupCode(String dictGroupCode) {
        this.dictGroupCode = dictGroupCode;
    }
}
