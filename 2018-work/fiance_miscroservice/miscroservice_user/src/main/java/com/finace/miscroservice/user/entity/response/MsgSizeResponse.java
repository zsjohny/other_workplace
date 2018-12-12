package com.finace.miscroservice.user.entity.response;

import lombok.ToString;

import java.io.Serializable;

/**
 * 消息 数量
 */
@ToString
public class MsgSizeResponse implements Serializable {
    private Integer size;  //消息数量
    private Integer msgCode; //消息类型

    public MsgSizeResponse() {
    }

    public Integer getSize() {
        return this.size;
    }

    public Integer getMsgCode() {
        return this.msgCode;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setMsgCode(Integer msgCode) {
        this.msgCode = msgCode;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MsgSizeResponse)) return false;
        final MsgSizeResponse other = (MsgSizeResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$size = this.getSize();
        final Object other$size = other.getSize();
        if (this$size == null ? other$size != null : !this$size.equals(other$size)) return false;
        final Object this$msgCode = this.getMsgCode();
        final Object other$msgCode = other.getMsgCode();
        if (this$msgCode == null ? other$msgCode != null : !this$msgCode.equals(other$msgCode)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $size = this.getSize();
        result = result * PRIME + ($size == null ? 43 : $size.hashCode());
        final Object $msgCode = this.getMsgCode();
        result = result * PRIME + ($msgCode == null ? 43 : $msgCode.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MsgSizeResponse;
    }
}
