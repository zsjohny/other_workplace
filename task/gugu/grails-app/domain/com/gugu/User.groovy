package com.gugu

class User {
    String userName
    String userNiceName
    String userPhone
    String userLocation
    String userIntru
    String userSex
    String userHeadUrl
    String userPhoneCode
    String userIsRegis
    String userPass
    String userKey
    String userChange
    Double locationX
    Double locationY

    static mapping = {
        version false
        id column: "userId"
        userNiceName length: 128
        userPhone length: 11
        userName length: 218
        userLocation length: 64
        userIntru length: 512
        userHeadUr length: 512
        userPhoneCode length: 16
        userIsRegis length: 8
        userPass length: 128
        userKey length: 512
        userChange length: 8
    }


    static constraints = {
        userName(nullable: true)
        userNiceName(nullable: true)
        userLocation(nullable: true)
        userIntru(nullable: true)
        userPhone(nullable: true)
        userSex(nullable: true)
        userHeadUrl(nullable: true)
        userPhoneCode(nullable: true)
        userIsRegis(nullable: true)
        userPass(nullable: true)
        userKey(nullable: true)
        userChange(nullable: true)
        locationX(nullable: true)
        locationY(nullable: true)
    }

}