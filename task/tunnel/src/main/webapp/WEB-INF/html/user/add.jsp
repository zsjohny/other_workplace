<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色添加</title>
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
            $("input[name='password']").attr("placeholder", "");
            $("input[name='realName']").attr("placeholder", "");
        }

    </script>
</head>
<body>

<input type="hidden" id="userAddFindUser" value="${name}"/>
<form class="definewidth m20">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">登录名</td>
            <td><input type="text" name="name" onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td class="tableleft">密码</td>
            <td><input type="password" name="password" onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td class="tableleft">真实姓名</td>
            <td><input type="text" name="realName" onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td class="tableleft">状态</td>
            <td>
                <input type="radio" name="status" value="true" checked/> 启用
                <input type="radio" name="status" value="false"/> 禁用
            </td>
        </tr>
        <tr>
            <td class="tableleft">角色</td>
            <td>
                <input type="radio" name="authorLevel" value="2" checked/> 普通用户
                <input type="radio" name="authorLevel" value="1"/> 高级用户
            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="userAddSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="userAddBackid">返回列表</button>
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

        $('#userAddSave').click(function () {

            var name = $("input[name='name']").val();
            var password = $("input[name='password']").val();
            var realName = $("input[name='realName']").val();
            var status = $("input[name='status']:checked").val();
            var authorLevel = $("input[name='authorLevel']:checked").val();

            //检验
            if (name == "") {
                $("input[name='name']").val("");
                $("input[name='name']").attr("placeholder", "登录名不得为空!!");
                return;
            }

            if (password == "") {
                $("input[name='password']").val("");
                $("input[name='password']").attr("placeholder", "密码不得为空!!");
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

            if (password.length < 3) {
                $("input[name='password']").val("");
                $("input[name='password']").attr("placeholder", "密码不得少于3位!!");
                return;
            }

            if (realName.length < 2) {
                $("input[name='realName']").val("");
                $("input[name='realName']").attr("placeholder", "真实姓名不得少于2位!!");
                return;
            }


            password = md5($("input[name='password']").val());


            $.post("/tunnel/login/modifyUser.do",
                    "name=" + name + "&password=" + password + "&realName=" +
                    realName + "&status=" + status + "&authorLevel=" + authorLevel, function (data) {

                        data = parseInt(data);

                        if (data == 0) {
                            var name = "";
                            if (${!empty name}) {
                                name = $("#userAddFindUser").val();
                            }

                            location.href = "/tunnel/login/findUser.do?page=0&name=" + name;

                        } else if (data == 1) {
                            $("input[name='name']").val("");

                            $("input[name='name']").attr("placeholder", "新增失败,错误原因联系技术!!");

                        } else if (data == 2) {
                            var name = $("input[name='name']").val();

                            $("input[name='name']").val("");

                            $("input[name='name']").attr("placeholder", "用户名:" + name + ",已经存在!!");
                        }
                    })

        });


        $("#userAddBackid").click(function () {


            location.href = "/tunnel/login/findUser.do?page=1"

        });

    });
</script>