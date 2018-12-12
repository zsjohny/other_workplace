<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道详查</title>
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
<input type="hidden" id="tunnelInfoEditFindUser" value="${tunnelNumber}"/>
<input type="hidden" id="page" value="${page}"/>
<form method="post" class="definewidth m20">

    <input type="hidden" name="tunnelInfoId" value="${tunnelInfo.id}"/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <th colspan="2"></th>
            <th style="vertical-align: middle;text-align: center">进口端</th>
            <th style="vertical-align: middle;text-align: center">出口端</th>
        </tr>


        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">隧道编号</td>
            <td colspan="2" name="tunnelNumber">${tunnelInfo.tunnelNumber}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">隧道名称</td>
            <td colspan="2" name="tunnelName">${tunnelInfo.tunnelName}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">所在项目编号</td>
            <td colspan="2" name="tunnelName">${tunnelInfo.projectNumber}</td>

        </tr>


        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">所在线位</td>
            <td colspan="2" name="wireLocation">${tunnelInfo.wireLocation}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">设计阶段</td>
            <td colspan="2" name="wireLocation">
                <c:if test="${tunnelInfo.designPeriod ==0}"> 工可</c:if>
                <c:if test="${tunnelInfo.designPeriod ==1}">初步设计</c:if>
                <c:if test="${tunnelInfo.designPeriod ==2}">施工图设计</c:if>
            </td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">结构型式</td>
            <td colspan="2">
                <c:if test="${tunnelInfo.structuralStyle==0}">分离式 </c:if>
                <c:if test="${tunnelInfo.structuralStyle==1}">连体式</c:if>
                <c:if test="${tunnelInfo.structuralStyle==2}">单洞</c:if>
            </td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">隧道走向</td>
            <td colspan="2" name="tunnelDriection">${tunnelInfo.tunnelDriection}</td>
        </tr>

        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">起讫里程</td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">右线</td>
            <td name="startCourse_rightWire_importPort">${tunnelInfo.startCourse_rightWire_importPort}</td>
            <td name="startCourse_rightWire_exportPort">${tunnelInfo.startCourse_rightWire_exportPort}</td>
        </tr>

        <tr>

            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">左线</td>
            <td name="startCourse_leftWire_importPort">${tunnelInfo.startCourse_leftWire_importPort}</td>
            <td name="startCourse_leftWire_exportPort">${tunnelInfo.startCourse_leftWire_exportPort}</td>
        </tr>


        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">路面标高<br/>(m)
            </td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">右线</td>
            <td name="roadHigh_rightWire_importPort">${tunnelInfo.roadHigh_rightWire_importPort}</td>
            <td name="roadHigh_rightWire_exportPort">${tunnelInfo.roadHigh_rightWire_exportPort}</td>
        </tr>


        <tr>

            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">左线</td>
            <td name="roadHigh_leftWire_importPort">${tunnelInfo.roadHigh_leftWire_importPort}</td>
            <td name="roadHigh_leftWire_exportPort">${tunnelInfo.roadHigh_leftWire_exportPort}</td>
        </tr>

        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">洞门型式</td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">右线</td>
            <td name="portalType_rightWire_importPort">${tunnelInfo.portalType_rightWire_importPort}</td>
            <td name="portalType_rightWire_exportPort">${tunnelInfo.portalType_rightWire_exportPort}</td>
        </tr>


        <tr>

            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">左线</td>
            <td name="portalType_leftWire_importPort">${tunnelInfo.portalType_leftWire_importPort}</td>
            <td name="portalType_leftWire_exportPort">${tunnelInfo.portalType_leftWire_exportPort}</td>
        </tr>

        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">线间距<br/>(m)
            </td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">洞口</td>
            <td name="interWire_tunnelHead_importPort">${tunnelInfo.interWire_tunnelHead_importPort}</td>
            <td name="interWire_tunnelHead_exportPort">${tunnelInfo.interWire_tunnelHead_exportPort}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">洞身</td>
            <td contenteditable="true" name="interWire_tunnelBody" colspan="2">${tunnelInfo.interWire_tunnelBody}</td>
        </tr>

        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">平面线形</td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">右线</td>
            <td colspan="2" name="parallelLine_rightWire">${tunnelInfo.parallelLine_rightWire}</td>

        </tr>


        <tr>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">左线</td>
            <td colspan="2" name="parallelLine_leftWire">${tunnelInfo.parallelLine_leftWire}</td>

        </tr>

        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">坡度坡长</td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">右线</td>
            <td colspan="2" name="slopeLine_rightWire">${tunnelInfo.slopeLine_rightWire}</td>
        </tr>


        <tr>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">左线</td>
            <td colspan="2" name="slopeLine_leftWire">${tunnelInfo.slopeLine_leftWire}</td>
        </tr>

        <tr>
            <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">隧道埋深<br/>(m)
            </td>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">右线</td>
            <td colspan="2" name="tunnelLine_rightWire">${tunnelInfo.tunnelLine_rightWire}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">左线</td>
            <td colspan="2" name="tunnelLine_leftWire">${tunnelInfo.tunnelLine_leftWire}</td>
        </tr>
        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">施工组织建议</td>
            <td colspan="2" name="constructionTeamAdvice"
            >${tunnelInfo.constructionTeamAdvice}</td>
        </tr>


        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">气候条件</td>
            <td colspan="2" name="weatherCondition">${tunnelInfo.weatherCondition}</td>
        </tr>


        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">地形地貌</td>
            <td colspan="2" name="topographicFeatures">${tunnelInfo.topographicFeatures}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">地层岩性概况</td>
            <td colspan="2" name="generalSituationFormation">${tunnelInfo.generalSituationFormation}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">不良地质概况</td>
            <td colspan="2" name="badGeologicalSurvey">${tunnelInfo.badGeologicalSurvey}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">水文地质概况</td>
            <td colspan="2" name="hydrogeologicalSurvey">${tunnelInfo.hydrogeologicalSurvey}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">地震烈度及动参数
            </td>
            <td colspan="2" name="dynamicParameters">${tunnelInfo.dynamicParameters}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">浅埋偏压情况</td>
            <td colspan="2" name="shallowBuried">${tunnelInfo.shallowBuried}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" class="tableleft" style="vertical-align: middle;text-align: center">
                既有或规划建（构）筑物
            </td>
            <td colspan="2" name="plannedConstruction">${tunnelInfo.plannedConstruction}</td>
        </tr>

        <tr>
            <td colspan="2" class="tableleft" style="vertical-align: middle;text-align: center"></td>
            <td colspan="2">
                <button type="button" class="btn btn-success" name="backid" id="tunnelInfoDetailBackid">返回列表</button>
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
        $('#tunnelInfoDetailBackid').click(function () {
            location.href = "/tunnel/tunnelInfo/findTunnelInfo.do?page=" + $("#page").val();

        });

    });
</script>