<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>项目修改</title>
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
            $("input[name='projectNumber']").attr("placeholder", "");
            $("input[name='projectName']").attr("placeholder", "");
            $("input[name='examinePersion']").attr("placeholder", "");
            $("input[name='contactInfo']").attr("placeholder", "");
            $("input[name='examineDate']").attr("placeholder", "");
        }
    </script>
</head>
<body>
<input type="hidden" id="page" value="${page}"/>
<input type="hidden" id="projectEditFindUser" value="${projectNumber}"/>
<form method="post" class="definewidth m20">

    <input type="hidden" name="projectId" value="${project.id}"/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">项目编号</td>
            <td><input type="text" name="projectNumber" value="${project.projectNumber}" onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">项目名称</td>
            <td><input type="text" name="projectName" value="${project.projectName}" onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">调查人员</td>
            <td><input type="text" name="examinePersion" value="${project.examinePersion}" onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">联系方式</td>
            <td><input type="text" name="contactInfo" value="${project.contactInfo}" maxlength="11"
                       onfocus="resetTips()"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">调查日期</td>
            <td><input type="date" name="examineDate" value="${project.examineDate}" onfocus="resetTips()"/>

            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" id="userModifySave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="projectModifyBackid">返回列表</button>
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
        $('#projectModifyBackid').click(function () {
            location.href = "/tunnel/project/findProject.do?page=" + $("#page").val();

        });

        $('#userModifySave').click(function () {

            var id = $("input[name='projectId']").val();
            var projectNumber = $("input[name='projectNumber']").val();
            var projectName = $("input[name='projectName']").val();
            var examinePersion = $("input[name='examinePersion']").val();
            var contactInfo = $("input[name='contactInfo']").val();
            var examineDate = $("input[name='examineDate']").val();


            //检验
            if (projectNumber == "") {
                $("input[name='projectNumber']").val("");
                $("input[name='projectNumber']").attr("placeholder", "项目编号不得为空!!");
                return;
            }

            if (projectName == "") {
                $("input[name='projectName']").val("");
                $("input[name='projectName']").attr("placeholder", "项目名称不得为空!!");
                return;
            }

            if (examinePersion == "") {
                $("input[name='examinePersion']").val("");
                $("input[name='examinePersion']").attr("placeholder", "调查人员不得为空!!");
                return;
            }

            if (contactInfo == "") {
                $("input[name='contactInfo']").val("");
                $("input[name='contactInfo']").attr("placeholder", "联系方式不得为空!!");
                return;
            }


            if (projectNumber.length < 3) {
                $("input[name='projectNumber']").val("");
                $("input[name='projectNumber']").attr("placeholder", "项目编号不得少于3位!!");
                return;
            }

            if (projectName.length < 3) {
                $("input[name='projectName']").val("");
                $("input[name='projectName']").attr("placeholder", "项目名称不得少于3位!!");
                return;
            }

            if (examinePersion.length < 2) {
                $("input[name='examinePersion']").val("");
                $("input[name='examinePersion']").attr("placeholder", "调查人员不得少于2位!!");
                return;
            }

            if (contactInfo.length < 11) {
                $("input[name='contactInfo']").val("");
                $("input[name='contactInfo']").attr("placeholder", "联系方式不得少于11位!!");
                return;
            }

            if (!(/^[0-9]+$/.test(contactInfo))) {
                $("input[name='contactInfo']").val("");
                $("input[name='contactInfo']").attr("placeholder", "联系方式必须为纯数字!!");
                return;
            }

            $.post("/tunnel/project/modifyProject.do",
                    "id=" + id + "&projectNumber=" + projectNumber + "&projectName=" + projectName + "&examinePersion=" +
                    examinePersion + "&contactInfo=" + contactInfo + "&examineDate=" + examineDate, function (data) {
                        data = parseInt(data);

                        if (data == 0) {
                            projectNumber = "";
                            if (${!empty projectNumber}) {
                                projectNumber = $("#projectEditFindUser").val();
                            }

                            location.href = "/tunnel/project/findProject.do?page=1&projectNumber=" + projectNumber;

                        } else if (data == 1) {
                            $("input[name='projectNumber']").val("");

                            $("input[name='projectNumber']").attr("placeholder", "修改失败,错误原因联系技术!!");

                        } else if (data == 2) {
                            var projectNumber = $("input[name='projectNumber']").val();

                            $("input[name='projectNumber']").val("");

                            $("input[name='projectNumber']").attr("placeholder", "项目编号:" + projectNumber + ",已经存在!!");
                        }
                    })

        });


    });
</script>