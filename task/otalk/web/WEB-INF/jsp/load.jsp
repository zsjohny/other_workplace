<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>资料认证</title>
    <style type="text/css">
        #center {
            background-image: url(../back.jpg);
            text-align: center;
            height: 1200px;
        }

        .submit {
            border: none;
            font-weight: bold;
            color: #FFF;
            margin: 10px 10px 20px 40px;
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px;
            border-radius: 3px;
            -webkit-box-shadow: #CCC 0px 0px 5px;
            -moz-box-shadow: #CCC 0px 0px 5px;
            box-shadow: #CCC 0px 0px 5px;
            background: #BEBEBE;
            cursor: pointer;
        }

        .submit:hover {
            background: #31b6e7;
        }

        .submit {
            padding: 6px 10px;
        }

        #body {
            position: relative;
        }

        #errorMsg {
            position: absolute;
            float: left;
            top: 322px;
            left: 1030px;
            bottom: 200px;
        }

        a {
            color: #006600;
            text-decoration: none;
        }

        a:hover {
            color: #990000;
        }

        .top {
            margin: 5px auto;
            color: #990000;
            text-align: center;
        }

        .info select {
            border: 1px #993300 solid;
            background: #FFFFFF;
        }

        .info {
            margin: 5px;
            text-align: center;
        }

        .info #show {
            color: #3399FF;
        }

        .bottom {
            text-align: right;
            font-size: 12px;
            color: #CCCCCC;
            width: 1000px;
        }
    </style>

    <script>
        function checkParam() {

            /* 	if (document.getElementById("name").value.trim().length > 0
             && document.getElementById("phone").value.trim().length == 11
             && document.getElementById("author").value.trim().length > 3) {

             return true;
             } */
            if (document.getElementById("phone").value.trim().length == 11) {

                return true;
            }

            /* if (document.getElementById("name").value.trim().length == 0) {
             document.getElementById("nameMsg").innerHTML = "昵称不正确";

             } */

            if (document.getElementById("phone").value.trim().length != 11) {
                document.getElementById("phoneMsg").innerHTML = "手机不正确";

            }
            if (document.getElementById("author").value.trim() != '') {
                if (document.getElementById("author").value.trim().length != 15
                        || document.getElementById("author").value.trim().length <= 3) {
                    document.getElementById("authorMsg").innerHTML = "认证名称不正确";

                }

            }
            return false;

        }

        function clearError() {
            document.getElementById("success").style.display = "none";
            document.getElementById("authorMsg").innerHTML = "";
            document.getElementById("phoneMsg").innerHTML = "";
            document.getElementById("nameMsg").innerHTML = "";

        }
    </script>


</head>
<body>
<div id="center">
    <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>
    <br/> <br/> <br/> <br/> <br/> <span
        style="color: red; font-size: 20px;" id="success"> <c:choose>
    <c:when test="${empty successMsg}">
    </c:when>
    <c:otherwise>
        ${successMsg}
    </c:otherwise>
</c:choose>
		</span> <br/> <br/> <br/>
    <div id="body">
        <form action="author" onsubmit="return checkParam()" method="POST" enctype="multipart/form-data">
            姓&nbsp;&nbsp;名 :&nbsp;&nbsp;<input type="text" name="name" id="name"
                                               onfocus="clearError()" placeholder="输入昵称"/> <br/> <br/> <br/>
            手&nbsp;机号:&nbsp;&nbsp;<input type="text" name="phone" id="phone"
                                         maxlength="11" onfocus="clearError()" placeholder="输入手机号"/> <br/>
            <br/> <br/>认证名称:&nbsp;&nbsp;<input type="text" name="author"
                                               id="author" maxlength="18" onfocus="clearError()"
                                               placeholder="输入认证名称"/> <br/> <br/> <br/>
            <br/>经典节目:&nbsp;&nbsp;&nbsp;<textarea placeholder="多个用空格隔开" style="width: 157px"
                                                  name="classicProgram"></textarea> <br/> <br/> <br/>
            <br/>收听平台:&nbsp;&nbsp;&nbsp;<textarea placeholder="多个用空格隔开" style="width: 157px"
                                                  name="listenProgram"></textarea><br/> <br/> <br/>
            <br/>图片简介:&nbsp;&nbsp;&nbsp;<input multiple="true" name="disPicUrls" style="width: 160px" type="file"/>
            <br/> <br/> <br/>
            <br/>头像上传:&nbsp;&nbsp;&nbsp;<input multiple="true" name="file" style="width: 160px" type="file"/>
            <br/> <br/> <br/>
            &nbsp;&nbsp;&nbsp;<input
                class="submit" type="reset" value="重置"/><input class="submit"
                                                               type="submit" value="认证"/>
        </form>
    </div>
    <br/>
    <br/>
    <div>
        &nbsp;&nbsp;&nbsp;&nbsp;<a href="recommondDis">点击此处进入推荐管理页面</a>
    </div>
    <div>
        <form action="uploadPro" method="post" enctype="multipart/form-data">
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="file" name="file"/> <br/> <input
                class="submit" type="submit" value="上传消息配置"/>
        </form>
    </div>
    <div id="errorMsg">
        <span style="color: red;" id="nameMsg"></span> <br/> <br/> <br/>
        <span style="color: red;" id="phoneMsg"></span> <br/> <br/> <br/>
        <span style="color: red;" id="authorMsg"></span>
    </div>
</div>


</body>
</html>