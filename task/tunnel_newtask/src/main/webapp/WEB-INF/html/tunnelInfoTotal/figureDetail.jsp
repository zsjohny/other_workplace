<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>附图详情</title>
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

</head>
<body>

<input type="hidden" id="params" value="${params}"/>
<input type="hidden" id="pageName" value="${pageName}"/>
<input type="hidden" id="page" value="${page}"/>
<input type="hidden" name="id" value="${baseDomain.id}"/>
<form class="definewidth m20" id="editFiles">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td>${baseDomain.tunnelNumber}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄位置</td>
          <%--  <td>
                <c:if test="${baseDomain.cameraSite==true}">
                    进口
                </c:if>
                <c:if test="${baseDomain.cameraSite==false}">
                    出口
                </c:if>

            </td>--%>
            <td>${baseDomain.cameraSite}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">里程桩号</td>
            <td>${baseDomain.mileagePile}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄方向</td>
            <td>${baseDomain.shootingDirection}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄时间</td>
            <td>${baseDomain.shootingTime}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">照片图像</td>
            <td id="displayPhotos">
                <input type="hidden" name="photosPicture" value="${baseDomain.photosPicture}"/>
            </td>

        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="figureEditBackid">返回列表</button>
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

        //加载图片属性
        var paths = $("input[name='photosPicture']").val()

        if (paths != '') {
            //加载图片
            var pathArr = paths.toString().split(":");
            var content = "";

            for (var i = 0; i < pathArr.length; i++) {
                if (pathArr[i] != '' && pathArr[i].trim() != '') {
                    content += "<img  class='img-responsive center-block'  id='" + pathArr[i].split(".")[0] + "'  src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=figure&name=" + pathArr[i] + "'/>";
                }
            }
            $("#displayPhotos").append(content);

        }

        $("#figureEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = "/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=figure&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

        });

    });


</script>
