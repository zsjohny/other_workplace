package com.jiuy.timer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作定时任务类型枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/30 16:17
 * @Copyright 玖远网络
 */

public enum QrtzType {

    ADD("1","添加"),
    UPDATE("2","修改"),
    PAUSED("3","暂停"),
    RESUME("4","重新启动"),
    DEL("5","删除"),
    EXECUTE("6","执行"),
    EXECUTE_PERSON("7","手动执行");

    private String name;

    private String code;

    QrtzType(String code,String name){
        this.name = name;
        this.code = code;
    }

    /**
     * 转换成一个静态的map
     */
    private static Map<String,QrtzType> enumMap = new HashMap<>();

    static{
        QrtzType[] qts = QrtzType.values();
        for (QrtzType qt : qts) {
            enumMap.put(qt.getCode(),qt);
        }
    }

    /**
     * 通过code获取操作枚举
     * @param code
     * @author Aison
     * @date 2018/5/30 16:23
     */
    public static QrtzType getByCode(String code){

        return enumMap.get(code);
    }

    /**
     * 通过code获取操作枚举
     * @param code
     * @author Aison
     * @date 2018/5/30 16:23
     */
    public static String getByCodeInteger(Integer code){
        String codeStr = code == null ? "" : code.toString();
        QrtzType qrtzType = enumMap.get(codeStr);
        return qrtzType == null ? "" : qrtzType.getName();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
