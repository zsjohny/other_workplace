<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道进出口调查详情</title>
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
            <td>${baseDomain.tunnelNumber}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">进口or出口</td>
            <td>
                <c:if test="${baseDomain.importOrExport==true}">
                    进口
                </c:if>
                <c:if test="${baseDomain.importOrExport==false}">
                    出口
                </c:if>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地形地貌</td>
            <td>${baseDomain.topographicFeatures}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">植被条件</td>
            <td>${baseDomain.vegetationCover}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">洞口地质概况</td>
            <td>${baseDomain.tunnelHeadQuality}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议刷坡坡率</td>
            <td>${baseDomain.cuttingSlope}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水影响</td>
            <td>${baseDomain.surfaceWaterInfluence}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议洞门型式</td>
            <td>
                <c:if test="${baseDomain.portalType==0}">
                    端墙式
                </c:if>
                <c:if test="${baseDomain.portalType==1}">
                    削竹式
                </c:if>
                <c:if test="${baseDomain.portalType==2}">
                    环框式
                </c:if>
                <c:if test="${baseDomain.portalType==3}">
                    翼墙式
                </c:if>
                <c:if test="${baseDomain.portalType==4}">
                    柱式
                </c:if>
                <c:if test="${baseDomain.portalType==5}">
                    台阶式
                </c:if>
                <c:if test="${baseDomain.portalType==6}">
                    遮光棚式
                </c:if>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td>${baseDomain.plannedConstruction}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">洞口衔接条件</td>
            <td>${baseDomain.tunnelHeadCondition}</td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">施工场地</td>
            <td>${baseDomain.constructionPlant}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">施工便道</td>
            <td>${baseDomain.constructionRoad}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">环保水保</td>
            <td>${baseDomain.waterEnviron}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td>${baseDomain.otherNotice}</td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
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



        $("#tunnelImportPortEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelImportPort&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

        });

    });
</script>