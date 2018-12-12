<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 隧道浅埋段调查详情</title>
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
            <td width="10%" class="tableleft">起始里程</td>
            <td>${baseDomain.startCourse}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">终止里程</td>
            <td>${baseDomain.endCourse}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">隧道埋深(m)</td>
            <td>${baseDomain.tunnelLine}</td>
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
            <td width="10%" class="tableleft">浅埋段不良地质</td>
            <td>${baseDomain.shallowBad}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水影响</td>
            <td>${baseDomain.surfaceWaterInfluence}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td>${baseDomain.plannedConstruction}</td>
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
            <td width="10%" class="tableleft">建议施工方式</td>
            <td>${baseDomain.constractWay}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td>${baseDomain.otherNotice}</td>
        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
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




        $("#tunnelShallowCoverEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelShallowCover&tunnelNumber=" + params.split(":")[1] + "&page=" + page+"&pageName="+$("#pageName").val().trim() + "&params=" + params;

        });

    });
</script>
