package com.gugu

class UserLetter {

    Long userId
    Long userFriendId
    String userNiceName
    String userSubject
    String userContent
    String userPicUrl
    Long userLetterReplyId
    Date createTime
    String isReader
    Double locationX
    Double locationY
    static mapping = {
        version false
        userNiceName length: 128
        userSubject length: 512
        userContent length: 1280
        userPicUrl length: 1280
        isReader length: 8
    }
    static constraints = {
	    userContent(nullable: true)
        userPicUrl(nullable: true)
        userLetterReplyId(nullable: true)
    }
}
