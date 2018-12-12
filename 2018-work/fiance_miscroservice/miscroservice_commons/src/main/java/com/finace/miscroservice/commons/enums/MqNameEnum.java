package com.finace.miscroservice.commons.enums;

/**
 * Mq的名称枚举
 */
public enum MqNameEnum {
    EXCHANGER_NAME_SUFFIX("ExchangeName"), QUEUE_NAME_SUFFIX("QueueName"), ROUTING_KEY_NAME_SUFFIX("RoutingKeyName");

    private String value;

    MqNameEnum(String value) {
        this.value = value;
    }

    public String toName() {
        return value;
    }

}
