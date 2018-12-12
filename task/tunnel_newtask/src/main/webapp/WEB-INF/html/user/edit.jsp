<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色修改</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/tunnel/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/tunnel/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="/tunnel/css/style.css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.js"></script>
    <script type="text/javascript" src="/tunnel/js/ckform.js"></script>
    <script type="text/javascript" src="/tunnel/js/common.js"></script>
    <script src="/tunnel/js/md5.js"></script>

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }

        .sidebar-nav {
            padding: 9px 0;
        }

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }


    </style>
    <script>
        //恢复提示
        function resetTips() {
            $("input[name='name']").attr("placeholder", "");
            $("input[name='realName']").attr("placeholder", "");
        }

    </script>
</head>
<body>
<input type="hidden" id="userEditFindUser" value="${name}"/>
<form method="post" class="definewidth m20">

    <input type="hidden" name="id" value="${user.id}"/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">登录名</td>
            <c:if test="${user.authorLevel==0}">

                <td><input type="text" name="name" readonly onfocus="resetTips()" value="${user.name}"/></td>
            </c:if>
            <c:if test="${user.authorLevel!=0}">
                <td><input type="text" name="name" onfocus="resetTips()" value="${user.name}"/></td>

            </c:if>

        </tr>
        <tr>
            <td class="tableleft">密码</td>
            <input type="hidden" name="passKey" value="${user.passKey}"/>
            <input type="hidden" name="originalPass" value="${user.password}"/>
            <td><input type="text" name="password" placeholder="密码不填写则是原始密码" onfocus="resetTips()" value=""/></td>
        </tr>
        <tr>
            <td class="tableleft">真实姓名</td>
            <td><input type="text" name="realName" onfocus="resetTips()" value="${user.realName}"/></td>
        </tr>

        <c:if test="${user.authorLevel==0}">
            <input type="radio" name="status" style="display: none" value="true" checked/>
            <input type="radio" name="authorLevel" style="display: none" value="0" checked/>
        </c:if>
        <c:if test="${user.authorLevel!=0}">
            <tr>
                <td class="tableleft">状态</td>
                <td>
                    <c:if test="${user.status==true}">
                        <input type="radio" name="status" value="true" checked/> 启用
                        <input type="radio" name="status" value="false"/> 禁用
                    </c:if>
                    <c:if test="${user.status==false}">
                        <input type="radio" name="status" value="true"/> 启用
                        <input type="radio" name="status" value="false" checked/> 禁用
                    </c:if>

                </td>
            </tr>
            <tr>
                <td class="tableleft">角色</td>
                <td>
                    <c:if test="${user.authorLevel==1}">
                        <input type="radio" name="authorLevel" value="2"/> 普通用户
                        <input type="radio" name="authorLevel" value="1" checked/> 高级用户
                    </c:if>
                    <c:if test="${user.authorLevel==2}">
                        <input type="radio" name="authorLevel" value="2" checked/> 普通用户
                        <input type="radio" name="authorLevel" value="1"/> 高级用户
                    </c:if>
                </td>
            </tr>
        </c:if>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" id="userModifySave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="userModifyBackid">返回列表</button>
            </td>
        </tr>
    </table>
</form>
<br/>
<br/>
<br/>
<br/>
</body>
</html>
<script>
    $(function () {
        $('#userModifyBackid').click(function () {
            location.href = "/tunnel/login/findUser.do?page=0"

        });

        $('#userModifySave').click(function () {

            var name = $("input[name='name']").val();
            var id = $("input[name='id']").val();
            var password = $("input[name='password']").val();
            var passKey = $("input[name='passKey']").val();
            var realName = $("input[name='realName']").val();
            var originalPass = $("input[name='originalPass']").val();
            var status = $("input[name='status']:checked").val();
            var authorLevel = $("input[name='authorLevel']:checked").val();

            //检验
            if (name == "") {
                $("input[name='name']").val("");
                $("input[name='name']").attr("placeholder", "登录名不得为空!!");
                return;
            }


            if (realName == "") {
                $("input[name='realName']").val("");
                $("input[name='realName']").attr("placeholder", "真实姓名不得为空!!");
                return;
            }


            if (name.length < 3) {
                $("input[name='name']").val("");
                $("input[name='name']").attr("placeholder", "登录名不得少于3位!!");
                return;
            }

            if (realName.length < 2) {
                $("input[name='realName']").val("");
                $("input[name='realName']").attr("placeholder", "真实姓名不得少于2位!!");
                return;
            }


            if (password != '' && password.length > 2) {

                password = md5($("input[name='password']").val());
            }


            $.post("/tunnel/login/modifyUser.do",
                    "name=" + name + "&password=" + password + "&realName=" +
                    realName + "&status=" + status + "&authorLevel=" + authorLevel + "&id=" + id + "&passKey=" + passKey + "&originalPass=" + originalPass, function (data) {

                        data = parseInt(data);

                        if (data == 0) {
                            var name = "";
                            if (${!empty name}) {
                                name = $("#userEditFindUser").val();
                            }

                            location.href = "/tunnel/login/findUser.do?page=0&name=" + name;
                        } else if (data == 1) {
                            $("input[name='name']").val("");

                            $("input[name='name']").attr("placeholder", "修改失败,错误原因联系技术!!");

                        } else if (data == 2) {
                            var name = $("input[name='name']").val();

                            $("input[name='name']").val("");

                            $("input[name='name']").attr("placeholder", "用户名:" + name + ",已经存在!!");
                        }
                    })

        });


    });
</script>