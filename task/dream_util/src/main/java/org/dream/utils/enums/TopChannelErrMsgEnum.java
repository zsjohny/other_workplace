package org.dream.utils.enums;

/**
 * 一级渠道进行设置错误提示的新增
 * Created by nessary on 16-9-21.
 */
public enum TopChannelErrMsgEnum {


    NO_PARAMS(0, "没有传入相应的参数,请仔细检查!!"),

    TIME_ERR(1, "时间参数传入有错误!!"),

    EXCHANGE_NO_TIME(2, "当前市场没有设置时间!"),

    WEEK_ERR(3, "勾选的周期不在品种指定的周期内"),

    REAPIR_TASK(4, "与现有定时任务时间过于接近，请重新添加"),

    SUCCESS(5, "");


    private Integer key;


    private String value;

    TopChannelErrMsgEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public static  String getMsg(Integer key) {

        String msg = "";

        if (key == null) {
            return msg;
        }

        for (TopChannelErrMsgEnum err : TopChannelErrMsgEnum.values()) {
            if (key.equals(err.key)) {
                return err.value;
            }
        }

        return msg;
    }

}
