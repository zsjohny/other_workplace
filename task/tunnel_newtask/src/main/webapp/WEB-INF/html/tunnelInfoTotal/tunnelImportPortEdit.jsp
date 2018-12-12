<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道进出口调查修改</title>
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
            <td><input type="text" name="tunnelNumber" readonly value="${baseDomain.tunnelNumber}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">进口or出口</td>
            <td>
                <c:if test="${baseDomain.importOrExport==true}">
                    <input type="radio" name="importOrExport" value="true" checked/> 进口
                    <input type="radio" name="importOrExport" value="false"/> 出口
                </c:if>
                <c:if test="${baseDomain.importOrExport==false}">
                    <input type="radio" name="importOrExport" value="true"/> 进口
                    <input type="radio" name="importOrExport" value="false" checked/> 出口
                </c:if>
            </td>
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
            <td width="10%" class="tableleft">洞口地质概况</td>
            <td><input type="text" name="tunnelHeadQuality" value="${baseDomain.tunnelHeadQuality}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议刷坡坡率</td>
            <td><input type="text" name="cuttingSlope" value="${baseDomain.cuttingSlope}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水影响</td>
            <td><input type="text" name="surfaceWaterInfluence" value="${baseDomain.surfaceWaterInfluence}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议洞门型式</td>
            <td>
                <select name="portalType">
                    <c:if test="${baseDomain.portalType==0}">
                        <option selected value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>

                    <c:if test="${baseDomain.portalType==1}">
                        <option value="0">端墙式</option>
                        <option selected value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.portalType==2}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option selected value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.portalType==3}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option selected value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.portalType==4}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option selected value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.portalType==5}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option selected value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.portalType==6}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option selected value="6">遮光棚式</option>
                    </c:if>
                </select>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td><input type="text" name="plannedConstruction" value="${baseDomain.plannedConstruction}"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">洞口衔接条件</td>
            <td><input type="text" name="tunnelHeadCondition" value="${baseDomain.tunnelHeadCondition}"/></td>
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
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td><input type="text" name="otherNotice" value="${baseDomain.otherNotice}"/></td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="tunnelImportPortEditSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="tunnelImportPortEditBackid">返回列表</button>
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

        $('#tunnelImportPortEditSave').click(function () {

            var id = $("input[name='id']").val();
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var importOrExport = $("input[name='importOrExport']:checked").val();
            var topographicFeatures = $("input[name='topographicFeatures']").val();
            var vegetationCover = $("input[name='vegetationCover']").val();
            var tunnelHeadQuality = $("input[name='tunnelHeadQuality']").val();
            var cuttingSlope = $("input[name='cuttingSlope']").val();
            var surfaceWaterInfluence = $("input[name='surfaceWaterInfluence']").val();
            var portalType = $("select[name='portalType'] option:selected").val();
            var plannedConstruction = $("input[name='plannedConstruction']").val();
            var tunnelHeadCondition = $("input[name='tunnelHeadCondition']").val();
            var constructionPlant = $("input[name='constructionPlant']").val();
            var constructionRoad = $("input[name='constructionRoad']").val();
            var waterEnviron = $("input[name='waterEnviron']").val();
            var otherNotice = $("input[name='otherNotice']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do",
                    "className=tunnelImportPort&id=" + id + "&tunnelNumber=" + tunnelNumber + "&importOrExport=" + importOrExport + "&topographicFeatures=" +
                    topographicFeatures + "&vegetationCover=" + vegetationCover + "&tunnelHeadQuality=" + tunnelHeadQuality + "&cuttingSlope=" + cuttingSlope + "&surfaceWaterInfluence=" + surfaceWaterInfluence + "&portalType=" + portalType
                    + "&plannedConstruction=" + plannedConstruction + "&tunnelHeadCondition=" + tunnelHeadCondition + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad
                    + "&waterEnviron=" + waterEnviron + "&otherNotice=" + otherNotice, function (data) {

                        if (data == true) {
                            $("#tunnelImportPortEditBackid").click();
                        } else {
                            $("input[name='topographicFeatures']").val("");

                            $("input[name='topographicFeatures']").attr("placeholder", "修改失败,错误原因联系技术!!");

                        }
                    })

        });


        $("#tunnelImportPortEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelImportPort&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

        });

    });
</script>