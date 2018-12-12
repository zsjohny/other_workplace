<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧辅助通道调查详情</title>
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
<form class="definewidth m20" id="assistFiles">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td>${baseDomain.tunnelNumber}</td>
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
            <td width="10%" class="tableleft">地质概况</td>
            <td>${baseDomain.roadQuality}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">不良地质概况</td>
            <td>${baseDomain.badGeologicalSurvey}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">水文条件概况</td>
            <td>${baseDomain.hydrologyCondition}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议洞门型式</td>
            <td>
                <c:if test="${baseDomain.tunnelHeadStyle==0}">
                    端墙式
                </c:if>
                <c:if test="${baseDomain.tunnelHeadStyle==1}">
                    削竹式
                </c:if>
                <c:if test="${baseDomain.tunnelHeadStyle==2}">
                    环框式
                </c:if>
                <c:if test="${baseDomain.tunnelHeadStyle==3}">
                    翼墙式
                </c:if>
                <c:if test="${baseDomain.tunnelHeadStyle==4}">
                    柱式
                </c:if>
                <c:if test="${baseDomain.tunnelHeadStyle==5}">
                    台阶式
                </c:if>
                <c:if test="${baseDomain.tunnelHeadStyle==6}">
                    遮光棚式
                </c:if>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议刷坡坡率</td>
            <td>${baseDomain.cuttingSlope}</td>
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
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td>${baseDomain.otherNotice}</td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">辅助通道类型</td>
            <td>
                <c:if test="${baseDomain.accessTunnelType==0}">
                    斜井
                </c:if>
                <c:if test="${baseDomain.accessTunnelType==1}">
                    竖井
                </c:if>
                <c:if test="${baseDomain.accessTunnelType==2}">
                    横洞
                </c:if>
                <c:if test="${baseDomain.accessTunnelType==3}">
                    平行导坑
                </c:if>
            </td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">与主洞平面关系</td>
            <td>${baseDomain.mainTunnelRelationWithPlane}</td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">辅助坑道/风井与主洞平面关系示意图</td>
            <td id="displayPhotos">
                <input type="hidden" name="acccessTunnelAndAirshaftRelationWithMainTunnel"
                       value="${baseDomain.acccessTunnelAndAirshaftRelationWithMainTunnel}"/>
            </td>


        </tr>

        <tr>
            <td class="tableleft"></td>
            <td>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="assistTunnelEditBackid">返回列表</button>
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
        var paths = $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val()

        if (paths != '') {
            //加载图片
            var pathArr = paths.toString().split(":");
            var content = "";

            for (var i = 0; i < pathArr.length; i++) {
                if (pathArr[i] != '' && pathArr[i].trim() != '') {
                    content += "<img  class='img-responsive center-block'  id='" + pathArr[i].split(".")[0] + "'  src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=assistTunnel&name=" + pathArr[i] + "'/>";
                }
            }
            $("#displayPhotos").append(content);

        }


        $("#assistTunnelEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=assistTunnel&tunnelNumber=" + params.split(":")[1] + "&page=" + page+"&pageName="+$("#pageName").val().trim() + "&params=" + params;

        });

    });


</script>