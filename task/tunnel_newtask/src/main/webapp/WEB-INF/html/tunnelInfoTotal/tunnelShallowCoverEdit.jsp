<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 隧道浅埋段调查修改</title>
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
<form class="definewidth m20">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" readonly name="tunnelNumber" value="${baseDomain.tunnelNumber}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">起始里程</td>
            <td><input type="text" name="startCourse" value="${baseDomain.startCourse}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">终止里程</td>
            <td><input type="text" name="endCourse" value="${baseDomain.endCourse}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">隧道埋深(m)</td>
            <td><input type="text" name="tunnelLine" value="${baseDomain.tunnelLine}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地形地貌</td>
            <td><input type="text" name="topographicFeatures" value="${baseDomain.topographicFeatures}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">植被条件</td>
            <td><input type="text" name="vegetationCover" value="${baseDomain.vegetationCover}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">浅埋段不良地质</td>
            <td><input type="text" name="shallowBad" value="${baseDomain.shallowBad}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水影响</td>
            <td><input type="text" name="surfaceWaterInfluence" value="${baseDomain.surfaceWaterInfluence}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td><input type="text" name="plannedConstruction" value="${baseDomain.plannedConstruction}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">施工场地</td>
            <td><input type="text" name="constructionPlant" value="${baseDomain.constructionPlant}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">施工便道</td>
            <td><input type="text" name="constructionRoad" value="${baseDomain.constructionRoad}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">环保水保</td>
            <td><input type="text" name="waterEnviron" value="${baseDomain.waterEnviron}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议施工方式</td>
            <td><input type="text" name="constractWay" value="${baseDomain.constractWay}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td><input type="text" name="otherNotice" value="${baseDomain.otherNotice}"/></td>
        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="tunnelShallowCoverEditSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="tunnelShallowCoverEditBackid">返回列表
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

        $('#tunnelShallowCoverEditSave').click(function () {
            var id = $("input[name='id']").val();
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

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=tunnelShallowCover&id=" + id + "&tunnelNumber=" + tunnelNumber + "&startCourse=" + startCourse + "&endCourse=" + endCourse + "&tunnelLine=" + tunnelLine + "&topographicFeatures=" + topographicFeatures + "&vegetationCover=" + vegetationCover + "&shallowBad=" + shallowBad + "&surfaceWaterInfluence=" + surfaceWaterInfluence + "&plannedConstruction=" + plannedConstruction + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad + "&waterEnviron=" + waterEnviron + "&constractWay=" + constractWay + "&otherNotice=" + otherNotice, function (data) {

                if (data == true) {
                    $("#tunnelShallowCoverEditBackid").click();
                } else {
                    $("input[name='startCourse']").val("");

                    $("input[name='startCourse']").attr("placeholder", "修改失败,错误原因联系技术!!");

                }
            })

        });


        $("#tunnelShallowCoverEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelShallowCover&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

        });

    });
</script>
