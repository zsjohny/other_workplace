<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 洞口失稳风险源调查修改</title>
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
            <td width="10%" class="tableleft">风险源是否存在</td>
            <td>
                <c:if test="${baseDomain.riskSourceExists==true}">
                    <input type="radio" name="riskSourceExists" value="true" onclick="javascript:$('#riskPro').show()"
                           checked/>存在
                    <input type="radio" name="riskSourceExists" value="false"
                           onclick="javascript:$('#riskPro').hide()"/>不存在
                </c:if>
                <c:if test="${baseDomain.riskSourceExists==false}">
                    <input type="radio" name="riskSourceExists" value="true" onclick="javascript:$('#riskPro').show()"/>存在
                    <input type="radio" name="riskSourceExists" value="false" checked
                           onclick="javascript:$('#riskPro').hide()"/>不存在
                </c:if>
            </td>
        </tr>


        <tr id="riskPro"
            <c:if test="${baseDomain.riskSourceExists==false}">style="display: none" </c:if> >
            <td width="10%" class="tableleft">风险发生概率</td>
            <td>
                <select name="riskHappendProbalility">
                    <c:if test="${baseDomain.riskHappendProbalility==1}">
                        <option selected value="1">几乎不可能</option>
                        <option value="2">很少发生</option>
                        <option value="3">偶然发生</option>
                        <option value="4">可能发生</option>
                        <option value="5">频繁发生</option>
                    </c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==2}">
                        <option value="1">几乎不可能</option>
                        <option selected value="2">很少发生</option>
                        <option value="3">偶然发生</option>
                        <option value="4">可能发生</option>
                        <option value="5">频繁发生</option>
                    </c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==3}">
                        <option value="1">几乎不可能</option>
                        <option value="2">很少发生</option>
                        <option selected value="3">偶然发生</option>
                        <option value="4">可能发生</option>
                        <option value="5">频繁发生</option>
                    </c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==4}">
                        <option value="1">几乎不可能</option>
                        <option value="2">很少发生</option>
                        <option value="3">偶然发生</option>
                        <option selected value="4">可能发生</option>
                        <option value="5">频繁发生</option>
                    </c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==5}">
                        <option value="1">几乎不可能</option>
                        <option value="2">很少发生</option>
                        <option value="3">偶然发生</option>
                        <option value="4">可能发生</option>
                        <option selected value="5">频繁发生</option>
                    </c:if>
                </select>
            </td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">风险源描述</td>
            <td><input type="text" name="riskDecribe" value="${baseDomain.riskDecribe}"/></td>
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
            <td width="10%" class="tableleft">植被发育</td>
            <td><input type="text" name="vegetationDeveop" value="${baseDomain.vegetationDeveop}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水系</td>
            <td><input type="text" name="shallowWater" value="${baseDomain.shallowWater}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地层岩性</td>
            <td><input type="text" name="generalSituation" value="${baseDomain.generalSituation}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地下水</td>
            <td><input type="text" name="undergroundWater" value="${baseDomain.undergroundWater}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地质构造</td>
            <td><input type="text" name="geologicalStructure" value="${baseDomain.geologicalStructure}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">不良地质</td>
            <td><input type="text" name="badGeological" value="${baseDomain.badGeological}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">顺层偏压</td>
            <td><input type="text" name="beddingBias" value="${baseDomain.beddingBias}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地形偏压</td>
            <td><input type="text" name="trrainBias" value="${baseDomain.trrainBias}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">周边环境</td>
            <td><input type="text" name="arroundEnviroment" value="${baseDomain.arroundEnviroment}"/></td>
        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="tunnelHeadRiskExamineEditSave">保存
                </button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="tunnelHeadRiskExamineEditBackid">返回列表
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

        $('#tunnelHeadRiskExamineEditSave').click(function () {
            var id = $("input[name='id']").val();
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var riskSourceExists = $("input[name='riskSourceExists']:checked").val();
            var riskHappendProbalility = $("select[name='riskHappendProbalility'] option:selected").val();
            var riskDecribe = $("input[name='riskDecribe']").val();


            var importOrExport = $("input[name='importOrExport']:checked").val();
            var topographicFeatures = $("input[name='topographicFeatures']").val();
            var vegetationDeveop = $("input[name='vegetationDeveop']").val();
            var shallowWater = $("input[name='shallowWater']").val();
            var generalSituation = $("input[name='generalSituation']").val();
            var undergroundWater = $("input[name='undergroundWater']").val();
            var geologicalStructure = $("input[name='geologicalStructure']").val();
            var badGeological = $("input[name='badGeological']").val();
            var beddingBias = $("input[name='beddingBias']").val();
            var trrainBias = $("input[name='trrainBias']").val();
            var arroundEnviroment = $("input[name='arroundEnviroment']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=tunnelHeadRiskExamine&id=" + id + "&tunnelNumber=" + tunnelNumber + "&riskSourceExists=" + riskSourceExists
                    + "&riskHappendProbalility=" + riskHappendProbalility + "&riskDecribe=" + riskDecribe + "&importOrExport=" + importOrExport + "&topographicFeatures=" + topographicFeatures + "&vegetationDeveop=" + vegetationDeveop + "&shallowWater=" + shallowWater + "&generalSituation=" + generalSituation + "&undergroundWater=" + undergroundWater + "&geologicalStructure=" + geologicalStructure + "&badGeological=" + badGeological + "&beddingBias=" + beddingBias + "&trrainBias=" + trrainBias + "&arroundEnviroment=" + arroundEnviroment, function (data) {

                if (data == true) {
                    $("#tunnelHeadRiskExamineEditBackid").click();
                } else {
                    $("input[name='topographicFeatures']").val("");

                    $("input[name='topographicFeatures']").attr("placeholder", "修改失败,错误原因联系技术!!");

                }
            })

        });


        $("#tunnelHeadRiskExamineEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelHeadRiskExamine&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

        });

    });
</script>
