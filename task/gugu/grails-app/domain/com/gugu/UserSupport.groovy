package com.gugu

class UserSupport {

    Long userId
    Long userPubLetterId
    Date createTime
    String isSupport

    static mapping = {
        version false
        isSupport length: 8
    }
    static constraints = {

    }
}