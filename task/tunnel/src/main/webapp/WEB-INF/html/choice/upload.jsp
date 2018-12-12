<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>隧道上传</title>
    <base target="_blank"/>


    <!--必要样式-->
    <link href="/tunnel/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/tunnel/css/city-picker.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.min.js"></script>

</head>

<body>
<div>
    <form class="definewidth m20" id="files">
        <button type="button" class="btn btn-info" type="button">
            <a href="/tunnel/login/downloadExcel.do">模板下载</a>
        </button>
        <br/>
        <span id="tips" style="color: red">Excel上传时,一般需要20几秒请耐心等待...</span>
        <input type="file" name="file" onchange="upload()"/>

    </form>
</div>
</body>
<script>

    function upload() {

        var formData = new FormData($("#files")[0]);
        $.ajax({
            url: '/tunnel/login/uploadExcel.do',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data == true) {
                    $("#tips").html("上传成功!!")
                } else {

                    $("#tips").html("上传失败!!")
                }
            },
            error: function () {

                $("#tips").html("Excel解析失败,请联系管理员,查看日志分析!")
            }
        });
    }

</script>
</html>