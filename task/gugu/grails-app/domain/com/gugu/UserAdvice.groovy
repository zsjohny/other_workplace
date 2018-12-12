package com.gugu

class UserAdvice {

    String realIp
    Long userId
    Long letterId
    Long letterPubId
    String reason

    static mapping = {
        version false
        realIp length: 1024
        reason length: 2016
    }

    static constraints = {
        reason(nullable: true)
        realIp(nullable: true)
        userId(nullable: true)
        letterId(nullable: true)
        letterPubId(nullable: true)
    }


}
