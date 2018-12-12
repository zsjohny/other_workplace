package com.gugu

class UserFriend {

    Integer userId
    String userRemark
    Integer userFrientId
    String userNiceName

    static mapping = {
        version false
        userNiceName length: 128
    }




}