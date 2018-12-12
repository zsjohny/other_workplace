<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 隧道浅埋段调查添加</title>
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
<form class="definewidth m20">
    <table class="table table-bordered table-hover definewidth m10">

        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" readonly name="tunnelNumber"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">起始里程</td>
            <td><input type="text" name="startCourse" placeholder="请填写数字!!"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">终止里程</td>
            <td><input type="text" name="endCourse" placeholder="请填写数字!!"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">隧道埋深(m)</td>
            <td><input type="text" name="tunnelLine" <%--placeholder="请填写数字!!"--%>/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地形地貌</td>
            <td><input type="text" name="topographicFeatures"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">植被条件</td>
            <td><input type="text" name="vegetationCover"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">浅埋段不良地质</td>
            <td><input type="text" name="shallowBad"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水影响</td>
            <td><input type="text" name="surfaceWaterInfluence"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td><input type="text" name="plannedConstruction"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">施工场地</td>
            <td><input type="text" name="constructionPlant"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">施工便道</td>
            <td><input type="text" name="constructionRoad"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">环保水保</td>
            <td><input type="text" name="waterEnviron"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议施工方式</td>
            <td><input type="text" name="constractWay"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td><input type="text" name="otherNotice"/></td>
        </tr>


        <tr>

            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="tunnelShallowCoverAddSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="tunnelShallowCoverAddBackid">返回列表
                </button>
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

        $('#tunnelShallowCoverAddSave').click(function () {
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var startCourse = $("input[name='startCourse']").val();
            var endCourse = $("input[name='endCourse']").val();
            var tunnelLine = $("input[name='tunnelLine']").val();
            var topographicFeatures = $("input[name='topographicFeatures']").val();
            var vegetationCover = $("input[name='vegetationCover']").val();
            var shallowBad = $("input[name='shallowBad']").val();
            var surfaceWaterInfluence = $("input[name='surfaceWaterInfluence']").val();
            var plannedConstruction = $("input[name='plannedConstruction']").val();
            var constructionPlant = $("input[name='constructionPlant']").val();
            var constructionRoad = $("input[name='constructionRoad']").val();
            var waterEnviron = $("input[name='waterEnviron']").val();
            var constractWay = $("input[name='constractWay']").val();
            var otherNotice = $("input[name='otherNotice']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=tunnelShallowCover&tunnelNumber=" + tunnelNumber + "&startCourse=" + startCourse + "&endCourse=" + endCourse + "&tunnelLine=" + tunnelLine + "&topographicFeatures=" + topographicFeatures + "&vegetationCover=" + vegetationCover + "&shallowBad=" + shallowBad + "&surfaceWaterInfluence=" + surfaceWaterInfluence + "&plannedConstruction=" + plannedConstruction + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad + "&waterEnviron=" + waterEnviron + "&constractWay=" + constractWay + "&otherNotice=" + otherNotice, function (data) {

                if (data == true) {
                    $("#tunnelShallowCoverAddBackid").click();
                } else {
                    $("input[name='startCourse']").val("");

                    $("input[name='startCourse']").attr("placeholder", "新增失败,错误原因联系技术!!");

                }
            })

        });





        $("#tunnelShallowCoverAddBackid").click(function () {
            var params = $("#params").val(); 
     //判断是否是页面选择进来的还是从原始页面进来的
            if (params.split(":")[2] == '1') {

                location.href = '/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=false&className=tunnelShallowCover&tunnelNumber=' + params.split(":")[1].trim() + '&params=' + params.split(":")[0].trim() + ":" + params.split(":")[1].trim() + ':1'
                return;
            }

            location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=examine&params=" + params;

        });

    })
    ;
</script>
