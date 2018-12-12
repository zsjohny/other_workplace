<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>隧道查询</title>
    <base target="_blank"/>

    <style>
        .baseFloat {
            float: left;
        }

        .onclickQueryProject {

        }

        .onclickQueryNumber {

        }

        .onclickQueryInfo {
        }

    </style>
    <!--必要样式-->
    <link href="/tunnel/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/tunnel/css/city-picker.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.min.js"></script>
    <script>

        var initTunnelInfoTotal;

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
            "otherRiskExamine": "14->其他风险源调查",
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
        function addTunnelInfoQuery() {

            var pageName = $("#deafultTunnelProject").val();
            if (pageName == '') {

                $("#exmainTunnel").html("<li class='list-group-item '> 没有选择隧道信息</li>");
                return;
            }


            var params = $("#tunnelClassType").val().trim() + ":"
                    + $("#tunnelNumber").val().trim() + ":" + $("#deafultTunnelProject").val().trim();

            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=" + $("#deafultTunnelProject").val().trim() + "&tunnelNumber=" + $("#tunnelNumber").val().trim() + "&params=" + params;


        }


        //查询所有内容
        function tunnelInfoTotalQuery() {
            var pageName = $("#tunnelNumber").val();
            if (pageName == '' || initTunnelInfoTotal == '' || initTunnelInfoTotal == undefined) {
                $("#queryNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                $("#exmainTunnel").html("<li class='list-group-item '> 隧道信息</li>");
                return;
            }
            var others = $("#tunnelClassType").val().trim() + ":"
                    + $("#tunnelNumber").val().trim() + ":" + $("#deafultTunnelProject").val().trim();
            var total = pageName + "|" + others + "|" + initTunnelInfoTotal;
            window.location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/index&params=" + total;
        }


        //默认选择触发事件
        $(function () {


            //添加项目触发事件
            $(".onclickQueryProject").click(function () {

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
                                    content += "<a  calss='onclickQueryNumber'><li class='list-group-item '> " + arr[i] + "</li></a> "

                                }
                                $("#queryNumber").html(content);

                                $("#tunnelClassType").val(projectNumber);
                            } else {
                                $("#queryNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                                $("#exmainTunnel").html("<li class='list-group-item '> 隧道信息</li>");
                                $("#initTotal").html("");
                            }
                            //重置提交的内容
                            $("#tunnelNumber").val("");
                            $("#deafultTunnelProject").val("");
                            //重置表格内容
                            $("#initTotal").html("");
                        })
            })

            //添加隧道编号触发事件
            $("#queryNumber").on('click', 'a', function () {
                $("#exmainTunnel").html("<li class='list-group-item'>隧道信息</li>");
                var tunnelNumber = $(this).text().trim();

                //去除上面所有的class
                $(this).parent().find("a>li").each(function () {
                    $(this).removeClass("active");
                });
                //添加新的class
                $(this).find("li").addClass("active");


                $("#tunnelNumber").val(tunnelNumber)


                /**
                 * 查找相应的隧道信息
                 */
                //查询获取其有多少表格的内容
                $.post("/tunnel/tunnelInfoTotal/findTunnelInfoTotalByJson.do",
                        "tunnelNumber=" + $("#tunnelNumber").val().trim() + "&tableName=tunnelImportPort", function (data) {
                            if (data.flag) {
                                var content = "";
                                var arr = data.list;

                                //排序
                                var tempArr = new Array();
                                var p = 0;
                                $.each(tunnelData, function (key, value) {

                                    $.each(arr, function (chilKey, chilValue) {

                                        if (key == chilValue) {
                                            tempArr[p++] = key;
                                        }
                                    })
                                })

                                //重新复制
                                arr = tempArr;
                                if (arr.length == 0) {
                                    $("#exmainTunnel").html("<li class='list-group-item'>没有隧道信息</li>");
                                    return;
                                }
                                //赋值查询全部条件
                                initTunnelInfoTotal = arr;
                                for (var i = 0; i < arr.length; i++) {
                                    var key = arr[i];

                                    var value = tunnelData[key];

                                    content += "<a class='onclickQueryInfo'><li class='list-group-item '> " + value + "</li></a>"


                                }
                                $("#exmainTunnel").html(content);
                            } else {
                                $("#exmainTunnel").html("<li class='list-group-item'>没有隧道信息</li>");
                                //重置提交的内容
                                $("#deafultTunnelProject").val("");
                                //重置查询全部条件
                                initTunnelInfoTotal = "";
                            }
                            //重置表格内容
                            $("#initTotal").html("");
                        })


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

                        //本条加载表格显示
                        /**
                         * 加载分条数
                         */
                        var params = $("#tunnelClassType").val().trim() + ":"
                                + $("#tunnelNumber").val().trim() + ":" + $("#deafultTunnelProject").val().trim();
                        var initTotal = "<iframe width='660px' height='380px' src='/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=true&className=" + key + "&tunnelNumber=" + $("#tunnelNumber").val().trim() + "&params=" + params + "'></iframe>"

                        $("#initTotal").html(initTotal);

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
                                    $("#queryNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                                    return;
                                }

                                for (var i = 0; i < arr.length; i++) {

                                    if (arr[i] == $("#tunnelNumber").val().trim()) {
                                        content += "<a  calss='onclickQueryNumber'><li class='list-group-item active '> " + arr[i] + "</li></a>";

                                        //继续添加相应的隧道信息
                                        /**
                                         * 查找相应的隧道信息
                                         */
                                        //查询获取其有多少表格的内容
                                        $.post("/tunnel/tunnelInfoTotal/findTunnelInfoTotalByJson.do",
                                                "tunnelNumber=" + $("#tunnelNumber").val().trim() + "&tableName=tunnelImportPort", function (datas) {
                                                    if (datas.flag) {
                                                        var contentQueryTunnel = "";
                                                        var arrs = datas.list;

                                                        if (arrs.length == 0) {
                                                            $("#exmainTunnel").html("<li class='list-group-item'>没有隧道信息</li>");
                                                            return;
                                                        }


                                                        for (var i = 0; i < arrs.length; i++) {
                                                            //赋值查询全部条件
                                                            initTunnelInfoTotal = arrs;
                                                            $.each(tunnelData, function (keyQuery, valueQuery) {
                                                                if (arrs[i] == keyQuery) {
                                                                    if (keyQuery == $("#deafultTunnelProject").val().trim()) {

                                                                        contentQueryTunnel += "<a class='onclickQueryInfo'><li class='list-group-item active'> " + valueQuery + "</li></a>"

                                                                        //本条加载表格显示
                                                                        /**
                                                                         * 加载分条数
                                                                         */
                                                                        var paramsInit = $("#tunnelClassType").val().trim() + ":"
                                                                                + $("#tunnelNumber").val().trim() + ":" + $("#deafultTunnelProject").val().trim();
                                                                        var initTotal = "<iframe width='660px' height='380px' src='/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=true&className=" + keyQuery + "&tunnelNumber=" + $("#tunnelNumber").val().trim() + "&params=" + paramsInit + "'></iframe>"

                                                                        $("#initTotal").html(initTotal);

                                                                    } else {

                                                                        contentQueryTunnel += "<a class='onclickQueryInfo'><li class='list-group-item '> " + valueQuery + "</li></a>"
                                                                    }
                                                                }
                                                            })
                                                        }
                                                        $("#exmainTunnel").html(contentQueryTunnel);
                                                    } else {
                                                        $("#exmainTunnel").html("<li class='list-group-item'>没有隧道信息</li>");
                                                        //重置提交的内容
                                                        $("#deafultTunnelProject").val("");
                                                    }

                                                })


                                    } else {
                                        content += "<a  calss='onclickQueryNumber'> <li class='list-group-item '> " + arr[i] + "</li></a>";
                                    }

                                }

                                $("#queryNumber").html(content);

                            } else {
                                $("#queryNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                            }

                        })

            }

        })


    </script>


</head>

<body>


<!-- Content -->
<div class="container">

    <h2 class="page-header">请依次选择:<span style="color: red">项目编号-隧道编号-隧道信息<h5 style="color: #3a4bfb">(隧道信息-->为某项目编号下收录过的隧道信息相应填写的表格)</h5></span>
    </h2>
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
                                        <a class="onclickQueryProject">
                                            <li class="list-group-item active">
                                                    ${list.projectNumber}
                                            </li>
                                        </a>
                                    </c:if>
                                    <c:if test="${list.projectNumber!=tunnelProject}">
                                        <a class="onclickQueryProject">
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
                            <ul class="list-group baseFloat" id="queryNumber">
                                <c:if test="${!empty tunnelNumber}">
                                    <a class="onclickQueryProject">
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
                                    <a class="onclickQueryInfo">
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
                <button class="btn btn-danger" onclick="addTunnelInfoQuery()" type="button">详情</button>
                <button class="btn btn-info" onclick="tunnelInfoTotalQuery()" type="button">全部</button>
            </div>
            <div id="initTotal">
            </div>
        </div>
    </nav>
</div>
<br/>
<br/>
<br/>
<br/>
<br/>
<script id="exmainTunnelNumberSrci">

</script>

</body>
</html>

