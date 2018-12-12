package com.gugu

import grails.transaction.Transactional

@Transactional
class UserService {


    def saveOrUpdateUserInfo = {

        user ->
            User userSave = null
            if (user.getId() != null) {

                userSave = User.find("from User where  id=? ", [user.getId()])

            } else {
                userSave = User.find("from User where  userPhone=? ", [user.getUserPhone()])
            }

            if (userSave) {


                if (!"true".equals(user.getUserChange() ? user.getUserChange() : "false")) {

                    if (!user.getUserKey().equals(userSave.getUserKey())) {
                        return "error"
                    }

                }

                user.getUserName() ? userSave.setUserName(user.getUserName()) : ""
                user.getUserNiceName() ? userSave.setUserNiceName(user.getUserNiceName()) : ""
                user.getUserPhone() ? userSave.setUserPhone(user.getUserPhone()) : ""
                user.getUserLocation() ? userSave.setUserLocation(user.getUserLocation()) : ""
                user.getUserIntru() ? userSave.setUserIntru(user.getUserIntru()) : ""
                user.getUserSex() ? userSave.setUserSex(user.getUserSex()) : ""
                user.getUserHeadUrl() ? userSave.setUserHeadUrl(user.getUserHeadUrl()) : ""
                user.getUserPhoneCode() ? userSave.setUserPhoneCode(user.getUserPhoneCode()) : ""
                user.getUserIsRegis() ? userSave.setUserIsRegis(user.getUserIsRegis()) : ""
                user.getUserPass() ? userSave.setUserPass(user.getUserPass()) : ""
                user.getUserKey() ? userSave.setUserKey(user.getUserKey()) : ""
                user.getLocationX() ? userSave.setLocationX(user.getLocationX()) : ""
                user.getLocationY() ? userSave.setLocationY(user.getLocationX()) : ""

                //更新所有好友列表信息
                UserFriend.executeUpdate("update UserFriend uf set userNiceName =:userNiceName where uf.userFrientId=:userFrientId", [userNiceName: user.getUserNiceName() ? user.getUserNiceName() : "", userFrientId: Integer.parseInt(String.valueOf(userSave.getId()))])


                user = userSave.save()


            } else {
                user = user.save()

            }


            user

    }


    def queruUserInfoByPheonOrId = {

        user, userId ->
            User userSave = null
            if (userId != null) {

                userSave = User.find("from User where  id=? ", [userId])

            } else {
                userSave = User.find("from User where  userPhone=? ", [user.getUserPhone()])
            }

            userSave


    }


    def saveOrUpdateUserFriendById = {
        userFriend ->

            UserFriend us = UserFriend.find("from  UserFriend where  userId =? and userFrientId=?", [userFriend.getUserId(), userFriend.getUserFrientId()])

            if (us == null) {
                us = new UserFriend()
                us.setUserId(userFriend.getUserId())
                us.setUserFrientId(userFriend.getUserFrientId())
                User userFind = User.findById(userFriend.getUserFrientId())
                userFriend.getUserRemark() ? us.setUserRemark(userFriend.getUserRemark()) : us.setUserRemark(userFind.getUserNiceName() == null ? "" : userFind.getUserNiceName())
                us.setUserNiceName(userFind.getUserNiceName() == null ? "" : userFind.getUserNiceName())
                us = us.save()
            } else {

                userFriend.getUserRemark() ? us.setUserRemark(userFriend.getUserRemark()) : ""
                us = us.save()

            }
            us

    }


    def viewUserFriendInfo = {

        userId, pageSize, pageNumber ->

            /*  List<UserFriend> list = UserFriend.findAll("from  UserFriend where  userId =? ", [userId], [max: 2, offset: 2])*/
            List<UserFriend> list = UserFriend.findAll("from  UserFriend where  userId =? ", [userId], [sort: "id", order: "DESC", max: pageNumber, offset: pageSize])

            list

    }
    def searchUserFriendInfo = {

        userId, word, pageSize, pageNumber ->

            /*  List<UserFriend> list = UserFriend.findAll("from  UserFriend where  userId =? ", [userId], [max: 2, offset: 2])*/
            List<UserFriend> list = UserFriend.findAll("from  UserFriend where ( userRemark like :words OR  userNiceName like :words)and userId=:userId", [words: '%' + word + '%', userId: userId], [sort: "id", order: "DESC", max: pageNumber, offset: pageSize])

            list

    }

//    Long userId
//    Long userFriendId
//    String userNiceName
//    String userSubject
//    String userContent
//    String userPicUrl
//    userLetterReplyId
    def sendUserLetter = {
        userLetter ->

            if (userLetter.getIsReader().equals("1") && userLetter.getUserLetterReplyId() != null) {

                userLetter = UserLetter.find("from  UserLetter where  id =? and userFriendId=?  ", [userLetter.getUserLetterReplyId(), userLetter.getUserId()])

                if (userLetter != null) {
                    userLetter.setIsReader("1")
                    userLetter = userLetter.save()
                }

                return userLetter
            }

            userLetter = userLetter.save()

            userLetter
    }


    def viewUserAllLetter = {

        userId, pageSize, pageNumber ->


            List<UserLetter> list = UserLetter.findAllByUserFriendId(userId, [sort: "createTime", order: "desc", max: pageNumber, offset: pageSize])



            list

    }


    def sendUserPubLetter = {


        it.save()


    }


    def viewUserAllPubLetter = {
        pageSize, pageNumber ->
            List<UserPubLetter> list = UserPubLetter.findAllByIsDeleted("0", [sort: "createTime", order: "desc", max: pageNumber, offset: pageSize])


            list
    }


    def supportPubLetter = {
        userSupport ->

            UserSupport us = UserSupport.find("from  UserSupport where  userId =? and userPubLetterId=?", [userSupport.getUserId(), userSupport.getUserPubLetterId()])

            if (us != null) {


                userSupport.getIsSupport() ? us.setIsSupport(userSupport.getIsSupport()) : ""
                us = us.save()
            } else {

                us = userSupport.save()

            }
            us
    }

    def viewUserSendAndAcceptAllLetter = {

        userId, pageSize, pageNumber ->


            List<UserLetter> list = UserLetter.findAllByUserFriendIdOrUserId(userId, userId, [sort: "createTime", order: "desc", max: pageNumber, offset: pageSize])



            list

    }


    def viewUserOwnerAllPubLetter = {
        userId, pageSize, pageNumber ->
            List<UserPubLetter> list = UserPubLetter.findAllByIsDeletedAndUserId("0", userId, [sort: "createTime", order: "desc", max: pageNumber, offset: pageSize])

            println list

            list
    }
    def searchAllUserInfo = {

        word, pageSize, pageNumber ->

            /*  List<UserFriend> list = UserFriend.findAll("from  UserFriend where  userId =? ", [userId], [max: 2, offset: 2])*/
            List<User> list = User.findAll("from  User where userPhone like :words OR  userNiceName like :words", [words: '%' + word + '%'], [sort: "id", order: "DESC", max: pageNumber, offset: pageSize])

            list

    }

    def saveUserBlackList = {
        userBlackList ->
            def result = UserBlackList.find("from UserBlackList where userId=:userId and userBlackId=:userBlackId and isDeleted='0'", [userId: userBlackList.getUserId(), userBlackId: userBlackList.getUserBlackId()])

            if (result != null) {
                false
            } else {
                userBlackList.save()
                true
            }

    }

}