<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>隧道调查</title>
    <base target="_blank"/>

    <style>
        .baseFloat {
            float: left;
        }

        .onclickExmaineProject {

        }

        .onclickExmaineNumber {

        }

        .onclickExmaineInfo {
        }

    </style>
    <!--必要样式-->
    <link href="/tunnel/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/tunnel/css/city-picker.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.min.js"></script>
    <script>

        var tunnelData = {
            "tunnelImportPort": "01->隧道进出口调查",
            "tunnelShallowCover": "02->隧道浅埋段调查",
            "assistTunnel": "03->辅助通道调查",
            "tunnelGrabageExamine": "04->隧道弃渣场调查",
            "tunnelHeadRiskExamine": "05->洞口失稳风险源调查",
            "overBreakRiskExmaine": "06->塌方风险源调查",
            "surgeMudRiskExamine": "07->突水涌泥风险源调查",
            "shapeRiskExamine": "08->大变形风险源调查",
            "rockOutburstRiskExamine": "09->岩爆风险源调查",
            "gasRiskExamine": "10->瓦斯风险源现场调查",
            "fireRiskExmaine": "11->火灾风险源调查",
            "trafficAccidentRiskExamine": "12->交通事故风险源调查",
            "envirRiskExamine": "13->环境风险源调查",
            "otherRiskExamine": "14->其它风险源调查",
            "figure": "15->附图"
        }


        //重置
        function examineReset() {
            $("ul a").find("li").each(function () {
                $(this).removeClass("active");
            });

            //重置提交的内容
            $("#tunnelNumber").val("");
            $("#deafultTunnelProject").val("");
        }


        //填写相应表格内容
        function addTunnelInfoExmaine() {

            var pageName = $("#deafultTunnelProject").val();
            if (pageName == '') {

                $("#exmainTunnel").html("<li class='list-group-item '> 没有选择隧道信息</li>");
                return;
            }


            var params = $("#tunnelClassType").val().trim() + ":"
                    + $("#tunnelNumber").val().trim() + ":" + $("#deafultTunnelProject").val().trim();

            location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/" + pageName + "Add" + "&params=" + params;
        }


        //默认选择触发事件
        $(function () {


            //添加项目触发事件
            $(".onclickExmaineProject").click(function () {

                var projectNumber = $(this).find("li").html().trim();
                //去除上面所有的class
                $(this).parent().find("a>li").each(function () {
                    $(this).removeClass("active");
                });
                //添加新的class
                $(this).find("li").addClass("active");
                /**
                 * 查找相应的隧道编号
                 */
                $.post("/tunnel/tunnelInfo/findTunnelInfoByJson.do",
                        "projectNumber=" + projectNumber, function (data) {

                            if (data.flag) {

                                var content = "";
                                var arr = data.list;
                                for (var i = 0; i < arr.length; i++) {
                                    content += "<a  calss='onclickExmaineNumber'><li class='list-group-item '> " + arr[i] + "</li></a> "

                                }
                                $("#exmaineNumber").html(content);

                                $("#tunnelClassType").val(projectNumber);
                            } else {
                                $("#exmaineNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                                $("#exmainTunnel").html("<li class='list-group-item '> 隧道信息</li>");
                            }
                            //重置提交的内容
                            $("#tunnelNumber").val("");
                            $("#deafultTunnelProject").val("");
                        })
            })

            //添加隧道编号触发事件
            $("#exmaineNumber").on('click', 'a', function () {

                var tunnelNumber = $(this).text().trim();

                //去除上面所有的class
                $(this).parent().find("a>li").each(function () {
                    $(this).removeClass("active");
                });
                //添加新的class
                $(this).find("li").addClass("active");


                $("#tunnelNumber").val(tunnelNumber)


                if (!($("#exmainTunnel").text().trim() == '隧道信息' || $("#exmainTunnel").text().trim() == '没有选择隧道信息')) {
                    return;
                }

                /**
                 * 查找相应的隧道信息
                 */
                var content = "";

                $.each(tunnelData, function (key, value) {
                    content += "<a class='onclickExmaineInfo'><li class='list-group-item '> " + value + "</li></a>"
                })
                $("#exmainTunnel").html(content);


            })

            //添加隧道信息触发事件
            $("#exmainTunnel").on('click', 'a', function () {


                var tunnelInfo = $(this).text().trim();

                //去除上面所有的class
                $(this).parent().find("a>li").each(function () {
                    $(this).removeClass("active");
                });
                //添加新的class
                $(this).find("li").addClass("active");


                $.each(tunnelData, function (key, value) {

                    if (value == tunnelInfo) {
                        $("#deafultTunnelProject").val(key);
                    }

                })


            })


            //默认触发
            var deafultTunnelProject = $("#tunnelClassType").val();

            if (deafultTunnelProject != '') {
                deafultTunnelProject = deafultTunnelProject.trim();
                $.post("/tunnel/tunnelInfo/findTunnelInfoByJson.do",
                        "projectNumber=" + deafultTunnelProject, function (data) {
                            if (data.flag) {

                                var content = "";
                                var arr = data.list;

                                if (arr.length == 0) {
                                    $("#exmaineNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                                    return;
                                }

                                for (var i = 0; i < arr.length; i++) {

                                    if (arr[i] == $("#tunnelNumber").val().trim()) {
                                        content += "<a  calss='onclickExmaineNumber'><li class='list-group-item active '> " + arr[i] + "</li></a>";

                                        //继续添加相应的隧道信息
                                        /**
                                         * 查找相应的隧道信息
                                         */
                                        var tunnelInfoContent = "";

                                        $.each(tunnelData, function (key, value) {
                                            if (key == $("#deafultTunnelProject").val().trim()) {
                                                tunnelInfoContent += "<a class='onclickExmaineInfo'><li class='list-group-item active'> " + value + "</li></a>"
                                            } else {
                                                tunnelInfoContent += "<a class='onclickExmaineInfo'><li class='list-group-item '> " + value + "</li></a>"
                                            }

                                        })
                                        $("#exmainTunnel").html(tunnelInfoContent);


                                    } else {
                                        content += "<a  calss='onclickExmaineNumber'> <li class='list-group-item '> " + arr[i] + "</li></a>";
                                    }

                                }

                                $("#exmaineNumber").html(content);

                            } else {
                                $("#exmaineNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                            }

                        })

            }

        })


    </script>


</head>

<body>

<!-- Content -->
<div class="container">

    <h2 class="page-header">请依次选择:<span style="color: red">项目编号-隧道编号-隧道信息</span></h2>

    <input type="hidden" id="tunnelClassType" value="${tunnelProject}"/>
    <input type="hidden" id="tunnelNumber" value="${tunnelNumber}"/>
    <input type="hidden" id="deafultTunnelProject" value="${tunnelInfo}"/>

    <nav class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand divider">请选择</a>
            </div>
            <div>
                <li class="dropdown">
                    <ul class=" nav navbar-nav">
                        <ul class="list-group baseFloat" id="exminePoject">
                            <c:if test="${!empty list}">
                                <c:forEach items="${list}" var="list" varStatus="status">
                                    <c:if test="${list.projectNumber==tunnelProject}">
                                        <a class="onclickExmaineProject">
                                            <li class="list-group-item active">
                                                    ${list.projectNumber}
                                            </li>
                                        </a>
                                    </c:if>
                                    <c:if test="${list.projectNumber!=tunnelProject}">
                                        <a class="onclickExmaineProject">
                                            <li class="list-group-item">
                                                    ${list.projectNumber}
                                            </li>
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty list}">
                                <li class="list-group-item">没有项目编号!!</li>
                            </c:if>
                        </ul>
                        </li>
                        <li class="dropdown">
                            <ul class="list-group baseFloat" id="exmaineNumber">
                                <c:if test="${!empty tunnelNumber}">
                                    <a class="onclickExmaineProject">
                                        <li class="list-group-item active"> ${tunnelNumber}</li>
                                    </a>
                                </c:if>
                                <c:if test="${empty tunnelNumber}">
                                    <li class="list-group-item"> 隧道编号</li>
                                </c:if>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <ul class="list-group baseFloat" id="exmainTunnel">
                                <c:if test="${!empty tunnelInfo}">
                                    <a class="onclickExmaineInfo">
                                        <li class="list-group-item active"> ${tunnelInfo}</li>
                                    </a>
                                </c:if>
                                <c:if test="${empty tunnelInfo}">
                                    <li class="list-group-item"> 隧道信息</li>
                                </c:if>
                            </ul>
                        </li>
                    </ul>
            </div>
            <div class="form-group">
                <button class="btn btn-warning" onclick="examineReset()" type="button">重置</button>
                <button class="btn btn-danger" onclick="addTunnelInfoExmaine()" type="button">添加</button>
            </div>
        </div>
    </nav>


</div>

<script id="exmainTunnelNumberSrci">

</script>

</body>
</html>