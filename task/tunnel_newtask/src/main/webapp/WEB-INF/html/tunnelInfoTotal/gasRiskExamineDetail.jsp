<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 瓦斯风险源现场调查详情</title>
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
            <td width="10%" class="tableleft">风险源是否存在</td>
            <td>
                <c:if test="${baseDomain.riskSourceExists==true}">存在</c:if>
                <c:if test="${baseDomain.riskSourceExists==false}">不存在</c:if>
            </td>
        </tr>

        <c:if test="${baseDomain.riskSourceExists==true}">
            <tr>
                <td width="10%" class="tableleft">风险发生概率</td>
                <td>
                    <c:if test="${baseDomain.riskHappendProbalility==1}">几乎不可能</c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==2}">很少发生</c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==3}">偶然发生</c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==4}">可能发生</c:if>
                    <c:if test="${baseDomain.riskHappendProbalility==5}">频繁发生</c:if>
                </td>
            </tr>
        </c:if>

        <tr>
            <td width="10%" class="tableleft">风险源描述</td>
            <td>${baseDomain.riskDecribe}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">起始里程</td>
            <td>${baseDomain.startCourse}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">终止里程</td>
            <td>${baseDomain.endCourse}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地层岩性</td>
            <td>${baseDomain.generalSituation}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地质构造</td>
            <td>${baseDomain.geologicalStructure}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">煤层厚度</td>
            <td>${baseDomain.seamThickness}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">隧道埋深</td>
            <td>${baseDomain.tunnelLine}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">水文地质条件</td>
            <td>${baseDomain.hydrogeologicalCondition}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">绝对瓦斯涌出量</td>
            <td>${baseDomain.soluteGasEmissionRate}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">瓦斯压力</td>
            <td>${baseDomain.gasPressure}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">煤体结构类型</td>
            <td>${baseDomain.coalStructureType}</td>
        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="gasRiskExamineEditBackid">返回列表</button>
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

    $("#gasRiskExamineEditBackid").click(function () {

        var params = $("#params").val();
        var page = $("#page").val();
        location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=gasRiskExamine&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

    });
</script>
