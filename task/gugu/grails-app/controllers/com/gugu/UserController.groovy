package com.gugu

import com.sun.org.apache.xml.internal.security.utils.Base64
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import java.security.MessageDigest
import java.text.SimpleDateFormat

import com.gugu.SendsUtils;

class UserController {

    /*   code=200代表操作成功
       code=201代表没有数据
       code=202代表参数写的不对
       code=203代表没有进行验证码的获取
       code=204代表验证码不正确
       cod=205代表userKey不正确
       */


    def userService

    //头像上传
    def uploadUserData = {

        MultipartFile headFile = request.getFile("file")



        if (params.userPhone == null || headFile == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }

        String fileName = System.currentTimeMillis() + ".jpg"

        User user = new User(["userPhone": params.userPhone, "userHeadUrl": fileName, "userKey": params.userKey])
        def msg = userService.saveOrUpdateUserInfo(user)

        if ("error".equals(msg)) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }


        File file = new File("D:\\pic\\default\\" + params.userPhone)


        if (!file.exists()) {
            file.mkdirs()
        } else {

            File[] files = file.listFiles()
            for (File fi : files) {
                System.gc();

                fi.delete()
            }

        }


        headFile.transferTo(new File(file.getPath(), fileName))





        render(contentType: "application/json") {
            code 200
        }
        return
    }

    //信息保存
    def saveUserInfo = {


        User user = params

        if (user == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }



        def msg = userService.saveOrUpdateUserInfo(user)

        if ("error".equals(msg)) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }



        if (msg == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {
            render(contentType: "application/json") {
                code 200
            }

        }


    }
    //头像预览
    def disHeadUrl = {

        User user = params


        if (params.userPhone == null && params.id == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }




        def userQuery = userService.queruUserInfoByPheonOrId(user, null)

        if (userQuery == null) {
            render(contentType: "application/json") {
                code 201
            }
            return
        } else {

            String headUrl = userQuery.getUserHeadUrl() ? userQuery.getUserHeadUrl() : "985595"

            File file = new File("D:\\pic\\default\\" + userQuery.getUserPhone(), headUrl)

            if (file.exists()) {


                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))

                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())

                byte[] b = new byte[1024]

                int len = 0
                while ((len = bis.read(b)) != -1) {

                    bos.write(b, 0, len)
                }

                bos.close()


            } else {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("D:\\pic\\default", "default.jpg")))

                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())

                byte[] b = new byte[1024]

                int len = 0
                while ((len = bis.read(b)) != -1) {

                    bos.write(b, 0, len)
                }

                bos.close()

            }


        }


    }

    //获取验证码
    def gainCode = {


        if (params.userPhone == null) {
            render(contentType: "application/json") {
                code 202
            }
            return

        }

        String num = "";
        for (int i = 0; i < 6; i++) {
            num += String.valueOf(((int) (Math.random() * 10)));
        }

        Boolean flag = SendsUtils.SendMsg(params.userPhone, num);
//        Boolean flag = true
        if (flag) {
            //msg access
            User user = new User(["userPhone": params.userPhone, "userPhoneCode": num + "", "userChange": "true"])


            def msg = userService.saveOrUpdateUserInfo(user)

            if (msg == null) {

                render(contentType: "application/json") {
                    code 201

                }

            } else {
                render(contentType: "application/json") {
                    code 200
                    /*number num*/
                }

            }
        } else {

            render(contentType: "application/json") {
                code 201

            }
        }

    }

    //注册
    def regsitUser = {

        if (params.userPhone == null || params.userPhoneCode == null || params.userPass == null) {
            render(contentType: "application/json") {
                code 202
            }
            return

        }

        User user = params

        def userQuery = userService.queruUserInfoByPheonOrId(user, null)




        if (userQuery == null) {
            render(contentType: "application/json") {
                code 201
            }
            return
        } else {

            if (userQuery.getUserPhoneCode() == null) {
                render(contentType: "application/json") {
                    code 201
                }

                render(contentType: "application/json") {
                    code 203
                }
                return
            } else if (userQuery.getUserPhoneCode().equals(user.getUserPhoneCode())) {

                user = new User(["userPhone": params.userPhone, "userPhoneCode": "xiaoluo", "userIsRegis": "true", "userPass": params.userPass, "userChange": "true", "locationX": params.locationX, "locationY": params.locationY])


                user = userService.saveOrUpdateUserInfo(user)

                if (user == null) {

                } else {


                    String str = user.getUserPhone() + user.getUserPass() + System.currentTimeMillis() + ""

                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(str.getBytes());
                    BigInteger hash = new BigInteger(1, md5.digest());
                    String hashFromContent = hash.toString(16);


                    String key = Base64.encode(hashFromContent.getBytes());

                    user = new User(["userPhone": params.userPhone, "userKey": key, "userChange": "true"])

                    userService.saveOrUpdateUserInfo(user)


                    user = User.find("from User where  userPhone=? ", [params.userPhone])

                    render(contentType: "application/json") {
                        code 200
                        userKey key
                        userId user.getId()

                    }


                }


            } else {
                render(contentType: "application/json") {
                    code 204
                }

            }
        }


    }

    //登录
    def loadUser = {

        User user = params

        if (user == null) {
            render(contentType: "application/json") {
                code 202
            }
            return

        }



        def userQuery = userService.queruUserInfoByPheonOrId(user, null)




        if (userQuery == null) {
            render(contentType: "application/json") {
                code 201
            }
            return
        } else {

            if (user.getUserPass().equals(userQuery.getUserPass())) {


                String str = user.getUserPhone() + user.getUserPass() + System.currentTimeMillis() + ""

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(str.getBytes());
                BigInteger hash = new BigInteger(1, md5.digest());
                String hashFromContent = hash.toString(16);


                String key = Base64.encode(hashFromContent.getBytes());

                user = new User(["userPhone": params.userPhone, "userKey": key, "userChange": "true", "locationX": params.locationX, "locationY": params.locationY])

                userService.saveOrUpdateUserInfo(user)

                render(contentType: "application/json") {
                    code 200
                    userKey key
                    userId userQuery.getId()
                    userNiceName userQuery.getUserNiceName() ? userQuery.getUserNiceName() : ""

                }

            } else {
                render(contentType: "application/json") {
                    code 206
                }

            }
        }


    }

    //输入旧密码更改
    def modifyUserPass = {

        User user = params

        if (user == null || params.userNewPass == null) {
            render(contentType: "application/json") {
                code 202
            }
            return

        }



        def userQuery = userService.queruUserInfoByPheonOrId(user, null)




        if (userQuery == null) {
            render(contentType: "application/json") {
                code 201
            }
            return
        } else {

            if (user.getUserPass().equals(userQuery.getUserPass())) {


                String str = user.getUserPhone() + user.getUserPass() + System.currentTimeMillis() + ""

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(str.getBytes());
                BigInteger hash = new BigInteger(1, md5.digest());
                String hashFromContent = hash.toString(16);


                String key = Base64.encode(hashFromContent.getBytes());

                user = new User(["userPhone": params.userPhone, "userKey": key, "userChange": "true", "userPass": params.userNewPass])

                userService.saveOrUpdateUserInfo(user)

                render(contentType: "application/json") {
                    code 200
                    userKey key


                }

            } else {
                render(contentType: "application/json") {
                    code 206
                }

            }
        }


    }

    //设置界面查看
    def loadUserAccount = {

        User user = params

        if (params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        def userQuery = userService.queruUserInfoByPheonOrId(user, null)




        if (userQuery == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            if (!user.getUserKey().equals(userQuery.getUserKey())) {
                render(contentType: "application/json") {
                    code 205
                }
                return
            }



            render(contentType: "application/json") {
                code 200
                data {
                    userId userQuery.getId() ? userQuery.getId() : ""
                    userNiceName userQuery.getUserNiceName() ? userQuery.getUserNiceName() : ""
                    userPhone userQuery.getUserPhone()
                    userLocation userQuery.getUserLocation() ? userQuery.getUserLocation() : ""
                    userIntru userQuery.getUserIntru() ? userQuery.getUserIntru() : ""
                    userSex userQuery.getUserSex() ? userQuery.getUserSex() : ""
                    userHeadUrl "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + userQuery.getUserPhone() + "&userHeadUrl=" + userQuery.getUserHeadUrl()


                }
            }

        }


    }

    //查看个人资料
    def viewUserData = {


        if (params.userFriendId == null || params.userId == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }

        //-----黑名单
        def result = UserBlackList.find("from UserBlackList where userId=:userId and userBlackId=:userBlackId and isDeleted='0'", [userId: Long.valueOf(params.userFriendId), userBlackId: Long.valueOf(params.userId)])
        if (result) {
            render(contentType: "application/json") {
                code 206
            }
            return
        }



        Long usId = Long.parseLong(params.userFriendId)

        User userQuery = userService.queruUserInfoByPheonOrId(null, usId)

        String userNiceNames = userQuery.getUserNiceName() ? userQuery.getUserNiceName() : ""

        UserFriend us = UserFriend.find("from  UserFriend where  userId =? and userFrientId=?", [Integer.valueOf(params.userId), Integer.valueOf(params.userFriendId)])

        String isFriends = "false"
        if (us != null) {
            isFriends = "true"

            userNiceNames = us.getUserRemark() ? us.getUserRemark() : ""

        }

        if (userQuery == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data {
                    userFrientId userQuery.getId() ? userQuery.getId() : ""
                    userNiceName userNiceNames
                    userLocation userQuery.getUserLocation() ? userQuery.getUserLocation() : ""
                    userHeadUrl "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + userQuery.getUserPhone() + "&userHeadUrl=" + userQuery.getUserHeadUrl()
                    isFriend isFriends
                }

            }

        }


    }

    //添加好友和修改好友备注
    def saveOrUpdateUserFriendById = {

        UserFriend userFriend = params

        if (userFriend == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }

        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        usId = Long.parseLong(params.userFrientId)

        user = userService.queruUserInfoByPheonOrId(null, usId)

        if (user == null) {

            render(contentType: "application/json") {
                code 204
            }

        }


        def users = userService.saveOrUpdateUserFriendById(userFriend)

        if (users == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200

            }

        }


    }
    //查看所有的好友信息
    def viewUserFriendInfo = {
        if (params.userId == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }



        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        Integer pageSize = 0
        if (params.pageSize != null && Integer.parseInt(params.pageSize) >= 1) {
            pageSize = (Integer.parseInt(params.pageSize) - 1) * 20
        }



        List<UserFriend> list = userService.viewUserFriendInfo(Integer.parseInt(params.userId), pageSize, 20)



        List<Map<String, String>> array = new ArrayList<>()

        list.each {

            Map<String, String> map = new HashMap<>()

            map.put("userId", it.getUserFrientId())
            map.put("userRemark", it.getUserRemark() ? it.getUserRemark() : "")


            user = userService.queruUserInfoByPheonOrId(null, Long.parseLong(it.getUserFrientId() + ""))
            String headUrl = ""
            if (user != null) {
                headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + user.getUserPhone() + "&userHeadUrl=" + user.getUserHeadUrl()
            } else {
                headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
            }

            map.put("userHeadUrl", headUrl)

            array.add(map)


        }

        if (list == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data array
            }

        }


    }

    //搜索的好友信息
    def searchUserFriendInfo = {
        if (params.word == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        Integer pageSize = 0
        if (params.pageSize != null && Integer.parseInt(params.pageSize) >= 1) {
            pageSize = (Integer.parseInt(params.pageSize) - 1) * 20
        }



        List<UserFriend> list = userService.searchUserFriendInfo(Integer.parseInt(params.userId), params.word, pageSize, 20)



        List<Map<String, String>> array = new ArrayList<>()

        list.each {

            Map<String, String> map = new HashMap<>()








            map.put("userId", it.getUserFrientId())
            map.put("userRemark", it.getUserRemark() ? it.getUserRemark() : "")


            user = userService.queruUserInfoByPheonOrId(null, Long.parseLong(it.getUserFrientId() + ""))
            String headUrl = ""
            if (user != null) {
                headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + user.getUserPhone() + "&userHeadUrl=" + user.getUserHeadUrl()
            } else {
                headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
            }

            map.put("userHeadUrl", headUrl)

            array.add(map)


        }

        if (list == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data array
            }

        }


    }

    //写信
//    Long userId
//    Long userFriendId
//    String userNiceName
//    String userSubject
//    String userContent
//    String userPicUrl
//    userLetterReplyId

    //写信或者是回信
    def sendUserLetter = {

        UserLetter userLetter = params
        Long usId = Long.parseLong(params.userId)

        if (userLetter == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }



        User user = userService.queruUserInfoByPheonOrId(null, usId)

        //判断是批量发送还是分开发送
        if ("true".equals(params.isMore)) {
            /*   try {*/




            String.valueOf(params.userFriendIds).split(",").each {

                if (it != null && it ==~ /\d+/) {

                    UserLetter userLe = new UserLetter()
                    userLe.setUserId(usId)
                    userLe.setUserSubject(userLetter.getUserSubject())
                    userLe.setUserContent(userLetter.getUserContent())
                    userLe.setUserPicUrl(userLetter.getUserPicUrl() ? userLetter.getUserPicUrl() : "")
                    User use = userService.queruUserInfoByPheonOrId(null, Long.parseLong(it))
                    userLe.setUserFriendId(Integer.parseInt(it))
                    userLe.setUserNiceName(use.getUserNiceName() ? use.getUserNiceName() : "")
                    userLe.setCreateTime(new Date())
                    userLe.setLocationX(userLetter.getLocationX())
                    userLe.setLocationY(userLetter.getLocationY())
                    userLe.getIsReader() ? "" : userLe.setIsReader("0")

                    userLe.save()


                }
            }
            render(contentType: "application/json") {
                code 200
            }
            /*  } catch (Exception e) {
                  render(contentType: "application/json") {
                      code 201
                  }*/
            return
        }

        if (!"true".equals(params.isReader)) {
            //-----黑名单
            def result = UserBlackList.find("from UserBlackList where userId=:userId and userBlackId=:userBlackId and isDeleted='0'", [userId: Long.valueOf(params.userFriendId), userBlackId: Long.valueOf(params.userId)])
            if (result) {
                render(contentType: "application/json") {
                    code 206
                }
                return
            }
        }


        userLetter.setCreateTime(new Date())
        userLetter.getIsReader() ? userLetter.setIsReader("1") : userLetter.setIsReader("0")



        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        def reslut = userService.sendUserLetter(userLetter)

        if (reslut == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
            }

        }


    }

//查看所有私信
    def viewUserAllLetter = {

        User userfind = params
        if (params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        Integer pageSize = 0
        if (params.pageSize != null && Integer.parseInt(params.pageSize) >= 1) {
            pageSize = (Integer.parseInt(params.pageSize) - 1) * 20
        }



        List<UserLetter> list = userService.viewUserAllLetter(Long.parseLong(params.userId), pageSize, 20)



        List<Map<String, String>> array = new ArrayList<>()

        list.each {


            if (!it.getUserId().equals(Long.parseLong(params.userId))) {


                Map<String, String> map = new HashMap<>()

                User us = userService.queruUserInfoByPheonOrId(null, it.getUserId())

                //限定某个范围内的私信显示
                Double distance = GetDistance(it.getLocationX(), it.getLocationY(), userfind.getLocationX(), userfind.getLocationY())


                if (Math.abs(distance) < 0.01) {

                    //-----黑名单
                    def result = UserBlackList.find("from UserBlackList where userId=:userId and userBlackId=:userBlackId and isDeleted='0'", [userId: Long.valueOf(params.userId), userBlackId: Long.valueOf(it.getUserId())])
                    if (result) {
                        map.put("isBlack", true)
                    } else {
                        map.put("isBlack", false)
                    }

                    map.put("userFriendId", it.getUserId())
                    map.put("userNiceName", us.getUserNiceName() ? us.getUserNiceName() : "")
                    map.put("userSubject", it.getUserSubject() ? it.getUserSubject() : "")
                    map.put("userContent", it.getUserContent() ? it.getUserContent() : "")
                    map.put("userPicUrl", it.getUserPicUrl() ? it.getUserPicUrl() : "")
                    map.put("locationX", it.getLocationX() ? it.getLocationX() : "")
                    map.put("locationY", it.getLocationY() ? it.getLocationY() : "")
                    map.put("userLetterReplyId", it.getId() ? it.getId() : "")
                    map.put("isReader", "0".equals(it.getIsReader()) ? "false" : "true")
                    String headUrl = ""
                    if (us != null) {
                        headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + us.getUserPhone() + "&userHeadUrl=" + us.getUserHeadUrl()
                    } else {
                        headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
                    }

                    map.put("userHeadUrl", headUrl)
                    map.put("userDate", new SimpleDateFormat("MM月dd日").format(it.getCreateTime()))



                    array.add(map)

                }
            }

        }
        Integer count = UserLetter.countByUserFriendIdAndIsReader(usId, "0")

        if (list == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data array
                isReaderCount count

            }

        }


    }

/*   Long userId
   String userNiceName
   String userSubject
   String userContent
   String userPicUrl
   Long userPubLetterId*/

    //写公开信
    def sendUserPubLetter = {

        UserPubLetter userPubLetter = params

        if (userPubLetter == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        userPubLetter.setCreateTime(new Date())
        userPubLetter.setIsDeleted("0")

        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        def reslut = userService.sendUserPubLetter(userPubLetter)

        if (reslut == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
            }

        }


    }
/*   Long userId
    String userNiceName
    String userSubject
    String userContent
    String userPicUrl
    Long userPubLetterId*/
    //查看所有公开信
    def viewUserAllPubLetter = {
        User userfind = params
        if (params.locationX == null || params.locationY == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }
        Integer pageSize = 0
        if (params.pageSize != null && Integer.parseInt(params.pageSize) >= 1) {
            pageSize = (Integer.parseInt(params.pageSize) - 1) * 20
        }



        List<UserPubLetter> list = userService.viewUserAllPubLetter(pageSize, 20)



        List<Map<String, String>> array = new ArrayList<>()

        list.each {

            Map<String, String> map = new HashMap<>()

            User us = userService.queruUserInfoByPheonOrId(null, it.getUserId())

            //限定某个范围内的私信显示
            Double distance = GetDistance(it.getLocationX(), it.getLocationY(), Double.parseDouble(params.locationX), Double.parseDouble(params.locationY))

            if (Math.abs(distance) < 1) {

                //-----黑名单
                def result = UserBlackList.find("from UserBlackList where userId=:userId and userBlackId=:userBlackId and isDeleted='0'", [userId: Long.valueOf(params.userId), userBlackId: Long.valueOf(it.getUserId())])
                if (result) {
                    map.put("isBlack", true)
                } else {
                    map.put("isBlack", false)
                }

                map.put("userId", it.getUserId())
                UserFriend userFriend = UserFriend.find("from  UserFriend where  userId =? and userFrientId=?", [Integer.valueOf(params.userId), Integer.valueOf(it.getUserId() + "")])

                String userNiceName = us.getUserNiceName() ? us.getUserNiceName() : ""
                if (userFriend != null) {
                    userNiceName = userFriend.getUserRemark() ? userFriend.getUserRemark() : ""
                }

                map.put("userNiceName", userNiceName)
                map.put("userSubject", it.getUserSubject() ? it.getUserSubject() : "")
                map.put("userContent", it.getUserContent() ? it.getUserContent() : "")
                map.put("userPicUrl", it.getUserPicUrl() ? it.getUserPicUrl() : "")
                map.put("userPubLetterId", it.getId() ? it.getId() : "")
                String headUrl = ""
                if (us != null) {
                    headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + us.getUserPhone() + "&userHeadUrl=" + us.getUserHeadUrl()
                } else {
                    headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
                }

                map.put("userHeadUrl", headUrl)
                map.put("userDate", new SimpleDateFormat("MM月dd日").format(it.getCreateTime()))
                Integer count = UserSupport.countByUserPubLetterIdAndIsSupport(it.getId(), "0")
                map.put("supportCount", count)


                def isSupport = UserSupport.find("from  UserSupport where userId=:userId  and userPubLetterId=:userPubLetterId and isSupport='0' ", [userId: Long.parseLong(params.userId), userPubLetterId: (long) it.getId()])

                map.put("isSupport", isSupport ? "true" : "false")

                array.add(map)

            }

        }

        if (list == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data array

            }

        }


    }

    //用户支持或者取消点赞
    def supportPubLetter = {

        UserSupport userSupport = params

        if (userSupport == null || params.userKey == null || params.isDeleted == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        userSupport.setCreateTime(new Date())
        "0".equals(params.isDeleted) ? userSupport.setIsSupport("0") : userSupport.setIsSupport("1")

        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        def us = userService.supportPubLetter(userSupport)

        if (us == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
            }

        }


    }

    //查看用户所有信息
    def viewUserAllInfo = {

        User userfind = params
        if (params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        Integer pageSize = 0
        if (params.pageSize != null && Integer.parseInt(params.pageSize) >= 1) {
            pageSize = (Integer.parseInt(params.pageSize) - 1) * 20
        }

        //私信

        List<UserLetter> list = userService.viewUserSendAndAcceptAllLetter(Long.parseLong(params.userId), pageSize, 20)



        List<Map<String, String>> array = new ArrayList<>()

        list.each {


            Map<String, String> map = new HashMap<>()

            //限定某个范围内的私信显示
            Double distance = GetDistance(it.getLocationX(), it.getLocationY(), userfind.getLocationX(), userfind.getLocationY())
            User us = null

            if (Math.abs(distance) < 0.01) {


                if (it.getUserId().equals(Long.parseLong(params.userId))) {

                    map.put("isOwner", "true")
                    map.put("userFriendId", it.getUserFriendId())
                    us = userService.queruUserInfoByPheonOrId(null, it.getUserFriendId())
                } else {
                    map.put("isOwner", "false")
                    map.put("userFriendId", it.getUserId())
                    us = userService.queruUserInfoByPheonOrId(null, it.getUserId())
                }



                map.put("userNiceName", us.getUserNiceName() ? us.getUserNiceName() : "")
                map.put("userSubject", it.getUserSubject() ? it.getUserSubject() : "")
                map.put("userContent", it.getUserContent() ? it.getUserContent() : "")
                map.put("userPicUrl", it.getUserPicUrl() ? it.getUserPicUrl() : "")
                map.put("locationX", it.getLocationX() ? it.getLocationX() : "")
                map.put("locationY", it.getLocationY() ? it.getLocationY() : "")
                map.put("userLetterReplyId", it.getId() ? it.getId() : "")
                map.put("isReader", "0".equals(it.getIsReader()) ? "false" : "true")
                String headUrl = ""
                if (us != null) {
                    headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + us.getUserPhone() + "&userHeadUrl=" + us.getUserHeadUrl()
                } else {
                    headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
                }

                map.put("userHeadUrl", headUrl)
                map.put("userDate", new SimpleDateFormat("MM月dd日").format(it.getCreateTime()))



                array.add(map)

            }

        }

        //公开信
        List<UserPubLetter> pubList = userService.viewUserOwnerAllPubLetter(Long.parseLong(params.userId), pageSize, 20)





        pubList.each {

            Map<String, String> map = new HashMap<>()

            User us = userService.queruUserInfoByPheonOrId(null, it.getUserId())

            //限定某个范围内的私信显示
            Double distance = GetDistance(it.getLocationX(), it.getLocationY(), Double.parseDouble(params.locationX), Double.parseDouble(params.locationY))

            if (Math.abs(distance) < 0.01) {

                map.put("userId", it.getUserId())
                map.put("userNiceName", us.getUserNiceName() ? us.getUserNiceName() : "")
                map.put("userSubject", it.getUserSubject() ? it.getUserSubject() : "")
                map.put("userContent", it.getUserContent() ? it.getUserContent() : "")
                map.put("userPicUrl", it.getUserPicUrl() ? it.getUserPicUrl() : "")
                map.put("userPubLetterId", it.getId() ? it.getId() : "")
                String headUrl = ""
                if (us != null) {
                    headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + us.getUserPhone() + "&userHeadUrl=" + us.getUserHeadUrl()
                } else {
                    headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
                }

                map.put("userHeadUrl", headUrl)
                map.put("userDate", new SimpleDateFormat("MM月dd日").format(it.getCreateTime()))
                Integer count = UserSupport.countByUserPubLetterIdAndIsSupport(it.getId(), "0")
                map.put("supportCount", count)


                def isSupport = UserSupport.find("from  UserSupport where userId=:userId  and userPubLetterId=:userPubLetterId and isSupport='0' ", [userId: Long.parseLong(params.userId), userPubLetterId: (long) it.getId()])

                map.put("isSupport", isSupport ? "true" : "false")

                array.add(map)

            }

        }




        if (list == null && pubList == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data array


            }

        }


    }

    //搜索的所有用户信息
    def searchAllUserInfo = {
        if (params.word == null || params.userKey == null) {
            render(contentType: "application/json") {
                code 202
            }
            return
        }


        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }

        Integer pageSize = 0
        if (params.pageSize != null && Integer.parseInt(params.pageSize) >= 1) {
            pageSize = (Integer.parseInt(params.pageSize) - 1) * 20
        }



        List<UserFriend> list = userService.searchAllUserInfo(params.word, pageSize, 20)



        List<Map<String, String>> array = new ArrayList<>()

        list.each {

            Map<String, String> map = new HashMap<>()

            map.put("userId", it.getId())
            map.put("userName", it.getUserNiceName() ? it.getUserNiceName() : "")

            String headUrl = ""
            if (user != null) {
                headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + it.getUserPhone() + "&userHeadUrl=" + it.getUserHeadUrl()
            } else {
                headUrl = "http://139.196.40.11:8058/user/disHeadUrl?userPhone=" + "15990073236"
            }

            map.put("userHeadUrl", headUrl)

            array.add(map)


        }

        if (list == null) {

            render(contentType: "application/json") {
                code 201
            }

        } else {


            render(contentType: "application/json") {
                code 200
                data array
            }

        }


    }

    //添加黑名单
    def saveUserBlackList = {
        if (params.userId == null || params.userBlackId == null || params.userKey == null) {
            render(contentType: "application/json") {


                code 202
            }

            return
        }

        Long usId = Long.parseLong(params.userId)

        User user = userService.queruUserInfoByPheonOrId(null, usId)

        if (!params.userKey.toString().equals(user.getUserKey())) {
            render(contentType: "application/json") {
                code 205
            }
            return
        }



        UserBlackList userBlackList = new UserBlackList(["userId"     : Long.valueOf(params.userId),
                                                         "isDeleted"  : "0", "createTime": new Date(),
                                                         "userBlackId": Long.valueOf(params.userBlackId)])


        boolean flag = userService.saveUserBlackList(userBlackList);

        if (flag) {
            render(contentType: "application/json") {


                code 200
            }
        } else {
            render(contentType: "application/json") {


                code 204
            }

        }

    }

    //投诉
    def complainByUser = {
        if (params.reason == null) {
            render(contentType: "application/json") {


                code 202
            }

            return
        }
        UserAdvice userAdvice = null


        String realIp = gainRealIp(request)

        if (realIp == null) {
            realIp = "localhost"
        }

        if (params.userId != null) {

            userAdvice = new UserAdvice(["userId": Long.valueOf(params.userId), "realIp": realIp, "reason": params.reason])


        } else {
            userAdvice = new UserAdvice(["realIp": realIp, "reason": params.reason])

        }


        if (params.letterId != null) {

            userAdvice.setLetterId(Long.valueOf(params.letterId))
        }

        if (params.letterPubId != null) {

            userAdvice.setLetterPubId(Long.valueOf(params.letterPubId))
        }



        userAdvice.save()

        render(contentType: "application/json") {


            code 200
        }


    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

/*1. Lat1 Lung1 表示A点经纬度，Lat2 Lung2 表示B点经纬度；2. a=Lat1 – Lat2 为两点纬度之差  b=Lung1 -Lung2 为两点经度之差；3. 6378.137为地球半径，单位为千米；
 */

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        return s;
        /* s = Math.round(s * 10000)
         return s / 1000;*/
    }

    public static String gainRealIp(HttpServletRequest request) {

        try {
            // 获取真实ip,排除代理ip
            String ipAddress = null;
            ipAddress = request.getHeader("Referer");

            // ipAddress = this.getRequest().getRemoteAddr();
            ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                // 更换niginx代理
                // ipAddress = request.getRemoteAddr();
                ipAddress = request.getHeader("X-Real-IP");
                if (ipAddress.equals("" + "") || ipAddress.endsWith("0:0:0:0:0:0:0:1")) {
                    // 根据网卡取本机配置的IP

                    // linux下也可以获取本地的ip地址
                    Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                    InetAddress ip = null;
                    while (allNetInterfaces.hasMoreElements()) {
                        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            ip = (InetAddress) addresses.nextElement();
                            if (ip != null && ip instanceof Inet4Address) {
                                // 获取真实的Ip地址
                                ipAddress = ip.getHostAddress();

                            }
                        }
                    }

                }

            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割"***.***.***.***".length()=15
            if (ipAddress != null && ipAddress.length() > 15) {

                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }

            return ipAddress;
        } catch (Exception e) {

            // 记录错误返回空字符串
            return "";
        }

    }


}

