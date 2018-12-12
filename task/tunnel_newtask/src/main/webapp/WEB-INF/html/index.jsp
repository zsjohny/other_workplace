<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- saved from url=(0078)http://www.17sucai.com/preview/2/2016-07-26/jQuery-VerificationCode/index.html -->
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>隧道后台登录</title>
    <meta name="author" content="DeathGhost">
    <link rel="stylesheet" type="text/css" href="/tunnel/css/indexStyle.css" tppabs="css/style.css">
    <style>
        body {
            height: 100%;
            background: #16a085;
            overflow: hidden;
        }

        canvas {
            z-index: -1;
            position: absolute;
        }

        input:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
            border: 1px solid #CCC !important;
            height: 27px !important;
            line-height: 27px !important;
            border-radius: 0 4px 4px 0;
        }
    </style>
    <script src="/tunnel/js/jquery.js"></script>
    <script src="/tunnel/js/md5.js"></script>
    <script src="/tunne/js/verificationNumbers.js" tppabs="js/verificationNumbers.js"></script>
    <script src="/tunnel/js/Particleground.js" tppabs="js/Particleground.js"></script>
    <script>

        var forbid = false;

        document.onkeydown = function (e) {
            e = e || window.event;
            if (e.keyCode == 13) {
                $("#submit_btn").click();
                return false;
            }
        }

        $(document).ready(function () {
            //粒子背景特效
            $('body').particleground({
                dotColor: '#5cbdaa',
                lineColor: '#5cbdaa'
            });

            $("#submit_btn").click(function () {

                if (forbid) {
                    return;
                }

                //参数检验
                if ($("input[name='name']").val() == '') {
                    $("input[name='name']").attr("placeholder", "账号不能为空!!");
                    return;
                }

                //参数检验
                if ($("input[name='password']").val() == '') {
                    $("input[name='password']").attr("placeholder", "密码不能为空!!");
                    return;
                }

                //参数检验
                if ($("input[name='name']").val().length < 3) {
                    $("input[name='name']").val("");
                    $("input[name='name']").attr("placeholder", "账号不得少于3位!!");
                    return;
                }

                //参数检验
                if ($("input[name='password']").val().length < 3) {
                    $("input[name='password']").val("");
                    $("input[name='password']").attr("placeholder", "密码不得少于3位!!");

                    return;
                }

                var name = $("input[name='name']").val();
                var password = md5($("input[name='password']").val());

                $("#submit_btn").val("正在登陆...");
                $("#submit_btn").attr("class", "submit_btn btn-danger");

                forbid = true;
                $.post("/tunnel/login/load.do", "name=" + name + "&password=" + password, function (data) {
                    if (data.flag == false) {
                        $("input[name='password']").val("");

                        if (data.msg.trim() != '') {
                            $("input[name='password']").attr("placeholder", data.msg);
                        } else {
                            $("input[name='password']").attr("placeholder", "用户名或密码错误!!");
                        }
                        $("#submit_btn").val("立即登陆");
                        $("#submit_btn").attr("class", "submit_btn");
                        forbid = false;
                    } else {
                        var sessionId = data.jessionId;
                        location.href = "/tunnel/login/access.do?sessionId=" + sessionId;
                    }
                })


            });
        });

        //恢复提示
        function resetTips() {
            $("input[name='name']").attr("placeholder", "账号");
            $("input[name='password']").attr("placeholder", "密码");
        }

    </script>
</head>
<body>
<canvas class="pg-canvas" width="1590" height="790"></canvas>
<dl class="admin_login">
    <dt>
        <strong>隧道管理后台</strong>
        <em>Tunnel Management System</em>
    </dt>
    <dd class="user_icon">
        <input type="text" name="name" onfocus="resetTips()" placeholder="账号" class="login_txtbx">
    </dd>
    <dd class="pwd_icon">
        <input type="password" name="password" onfocus="resetTips()" placeholder="密码" class="login_txtbx">
    </dd>
    <dd>
        <input type="button" value="立即登陆" id="submit_btn" class="submit_btn">
    </dd>
    <dd>
        <p>&copy; 2015-2016 隧道 版权所有</p>
        <p>杭-8998988-1</p>
    </dd>
</dl>


</body>
</html>