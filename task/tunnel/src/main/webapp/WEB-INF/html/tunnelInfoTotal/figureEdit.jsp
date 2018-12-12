<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>附图修改</title>
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
<div id="myAlert" class="alert alert-success">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <strong style=" width: auto;display: table;margin-left: auto;margin-right: auto;">删除相应图片时候点击相应图片即可!!</strong>
</div>
<input type="hidden" id="page" value="${page}"/>
<input type="hidden" name="id" value="${baseDomain.id}"/>
<form class="definewidth m20" id="editFiles">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" name="tunnelNumber" readonly value="${baseDomain.tunnelNumber}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄位置</td>
            <td>
                <c:if test="${baseDomain.cameraSite==true}">
                    <input type="radio" name="cameraSite" value="true" checked/> 进口
                    <input type="radio" name="cameraSite" value="false"/> 出口
                </c:if>
                <c:if test="${baseDomain.cameraSite==false}">
                    <input type="radio" name="cameraSite" value="true"/> 进口
                    <input type="radio" name="cameraSite" value="false" checked/> 出口
                </c:if>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">里程桩号</td>
            <td><input type="text" name="mileagePile" value="${baseDomain.mileagePile}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄方向</td>
            <td><input type="text" name="shootingDirection" value="${baseDomain.shootingDirection}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄时间</td>
            <td>
                <input type="date" name="shootingTime" value="${baseDomain.shootingTime}"/>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">照片图像</td>
            <td id="displayPhotos">
                <input type="hidden" name="photosPicture" value="${baseDomain.photosPicture}"/>
                <input type="file" name="files" accept="image/*" multiple onchange="upload()"/>
            </td>

        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="figureEditSave">保存</button>
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
                    content += "<img  class='img-responsive center-block'  id='" + pathArr[i].split(".")[0] + "' onclick='delPhotos(\"" + pathArr[i] + "\")' src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=figure&name=" + pathArr[i] + "'/>";
                }
            }
            $("#displayPhotos").append(content);

        }


        $('#figureEditSave').click(function () {
            var id = $("input[name='id']").val();
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var cameraSite = $("input[name='cameraSite']:checked").val();
            var mileagePile = $("input[name='mileagePile']").val();
            var shootingDirection = $("input[name='shootingDirection']").val();
            var shootingTime = $("input[name='shootingTime']").val();
            var photosPicture = $("input[name='photosPicture']").val();


            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=figure&id=" + id + "&tunnelNumber=" + tunnelNumber + "&cameraSite=" + cameraSite + "&mileagePile=" + mileagePile + "&shootingDirection=" + shootingDirection + "&shootingTime=" + shootingTime + "&photosPicture=" + photosPicture, function (data) {

                if (data == true) {
                    $("#figureEditBackid").click();
                } else {
                    $("input[name='mileagePile']").val("");

                    $("input[name='mileagePile']").attr("placeholder", "修改失败,错误原因联系技术!!");

                }
            })

        });


        $("#figureEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = "/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=figure&tunnelNumber=" + params.split(":")[1] + "&page=" + page+"&pageName="+$("#pageName").val().trim() + "&params=" + params;

        });

    });


    function delPhotos(photoName) {
        if (confirm("确定要删除吗？")) {
            $.post("/tunnel/tunnelInfoTotal/delPhoto.do", "className=figure&name=" + photoName + "&id=" + $("input[name='id']").val(), function (data) {
                if (data == true) {
                    $("#" + photoName.split(".")[0] + "").hide();


                    //加载图片属性
                    var paths = $("input[name='photosPicture']").val()

                    if (paths != '') {
                        //加载图片
                        var pathArr = paths.toString().split(":");
                        var content = "";

                        for (var i = 0; i < pathArr.length; i++) {
                            if (pathArr[i] != '' && pathArr[i].trim() != '') {

                                if (!(pathArr[i].indexOf(photoName) > -1)) {
                                    content += pathArr[i] + ":";
                                }
                            }
                        }
                        //重新赋值
                        $("input[name='photosPicture']").val(content);

                    }


                } else {
                    alert("图片删除失败!!");
                }
            });


        }

    }


    function upload() {
        var formData = new FormData($("#editFiles")[0]);
        $.ajax({
            url: '/tunnel/tunnelInfoTotal/uploadPhotos.do?className=figure',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                var originalPath = $("input[name='photosPicture']").val();
                $("input[name='photosPicture']").val(originalPath + data);
            },
            error: function () {
                alert("上传图片失败，请返回重新加载试试!!");
            }
        });
    }


</script>
