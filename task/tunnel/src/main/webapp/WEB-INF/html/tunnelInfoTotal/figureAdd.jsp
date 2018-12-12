<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>附图添加</title>
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
<script>

    $(function () {
        //添加默认的项目编号
        $("input[name='tunnelNumber']").val($("#params").val().toString().split(":")[1]);

    })

</script>

<body>

<input type="hidden" id="params" value="${params}"/>
<form class="definewidth m20" id="files">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" readonly name="tunnelNumber"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄位置</td>
            <td>
                <input type="radio" name="cameraSite" value="true" checked/> 进口
                <input type="radio" name="cameraSite" value="false"/> 出口
            </td>

        </tr>
        <tr>
            <td width="10%" class="tableleft">里程桩号</td>
            <td><input type="text" name="mileagePile" placeholder="请填写数字!!"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄方向</td>
            <td><input type="text" name="shootingDirection"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">拍摄时间</td>
            <td><input type="date" name="shootingTime"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">照片图像</td>
            <td>
                <input type="hidden" name="photosPicture"/>
                <input type="file" name="files" accept="image/*" multiple onchange="upload()"/>
            </td>
        </tr>

        </tr>
        <tr>

            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="figureAddSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="figureAddBackid">返回列表</button>
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


        //获取当前时间
        var date = new Date();
        var interval = "-";
        var month = date.getMonth() + 1;
        if (month.toString().length == 1) {
            month = "0" + month;
        }
        var day = date.getDate();
        if (day.toString().length == 1) {
            day = "0" + day;
        }


        var currentTime = date.getFullYear() + interval + month + interval + day;

        $("input[type='date']").val(currentTime);


        $('#figureAddSave').click(function () {
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var cameraSite = $("input[name='cameraSite']:checked").val();
            var mileagePile = $("input[name='mileagePile']").val();
            var shootingDirection = $("input[name='shootingDirection']").val();
            var shootingTime = $("input[name='shootingTime']").val();
            var photosPicture = $("input[name='photosPicture']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=figure&tunnelNumber=" + tunnelNumber + "&cameraSite=" + cameraSite + "&mileagePile=" + mileagePile + "&shootingDirection=" + shootingDirection + "&shootingTime=" + shootingTime + "&photosPicture=" + photosPicture, function (data) {

                if (data == true) {
                    $("#figureAddBackid").click();
                } else {
                    $("input[name='mileagePile']").val("");

                    $("input[name='mileagePile']").attr("placeholder", "新增失败,错误原因联系技术!!");

                }
            })

        });





        $("#figureAddBackid").click(function () {
            var params = $("#params").val(); 
     //判断是否是页面选择进来的还是从原始页面进来的
            if (params.split(":")[2] == '1') {

                location.href = '/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=false&className=figure&tunnelNumber=' + params.split(":")[1].trim() + '&params=' + params.split(":")[0].trim() + ":" + params.split(":")[1].trim() + ':1'
                return;
            }

            location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=examine&params=" + params;

        });

    })


    function upload() {
        var formData = new FormData($("#files")[0]);
        $.ajax({
            url: '/tunnel/tunnelInfoTotal/uploadPhotos.do?className=figure',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                $("input[name='photosPicture']").val(data);
            },
            error: function () {
                $("input[name='mileagePile']").attr("placeholder", "上传图片失败，刷新试试!!");
            }
        });
    }


</script>
