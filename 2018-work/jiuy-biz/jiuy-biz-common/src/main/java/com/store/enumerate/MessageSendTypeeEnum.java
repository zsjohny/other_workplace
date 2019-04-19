package com.store.enumerate;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 消息发送类型枚举
 * 
 * 发送类型：0客服单发会员、1会员单发客服、2客服群发会员、3系统替代客服单发会员
 * @author zhaoxinglin
 */
public enum MessageSendTypeeEnum implements IntEnum {
	unknown(-1),
	serverToMember(0),
    memberToServer(1),
    serverToMemberGroup(2),
	systemReplaceServerToMember(3);

    private MessageSendTypeeEnum(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }
    
    public boolean is(int intValue) {
        return getIntValue() == intValue;
    }
    
    public boolean is(MessageSendTypeeEnum MessageSendType) {
        return getIntValue() == MessageSendType.getIntValue();
    }

	public static MessageSendTypeeEnum getEnum(int type) {
		if (type == 0){
			return serverToMember;
		}else if (type == 1){
			return memberToServer;
		}else if (type == 2){
			return serverToMemberGroup;
		}else if (type == 3){
			return systemReplaceServerToMember;
		}else{
			return unknown;
		}
	}

}
