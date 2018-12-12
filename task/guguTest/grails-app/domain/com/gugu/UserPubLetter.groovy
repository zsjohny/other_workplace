package com.gugu

class UserPubLetter {
    Long userId
    String userNiceName
    String userSubject
    String userContent
    String userPicUrl
    Long userPubLetterId
    String isDeleted
    Date createTime
    Double locationX
    Double locationY
    static mapping = {
        version false
        userNiceName length: 128
        userSubject length: 512
        userContent length: 1280
        userPicUrl length: 1280
        isDeleted length: 8

    }
    static constraints = {
	    userContent(nullable: true)
        userPicUrl(nullable: true)
		userSubject(nullable: true)
        userPubLetterId(nullable: true)
    }
}
