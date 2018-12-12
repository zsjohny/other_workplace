<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>全局搜索</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/tunnel/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/tunnel/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="/tunnel/css/style.css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.js"></script>
    <script type="text/javascript" src="/tunnel/js/ckform.js"></script>
    <script type="text/javascript" src="/tunnel/js/common.js"></script>
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


        var downloadName = "";
        var numberType;

        $(function () {

            var params = $("#params").val();
            if (params == '') {
                return
            }

            var tunnelNumber = params.split("|")[0];
            var others = params.split("|")[1];
            var tunnelInfo = params.split("|")[2];
            var arr;
            if (tunnelInfo.indexOf(",") == -1) {
                arr = new Array(tunnelInfo);
            } else {
                arr = tunnelInfo.split(",");
            }
            numberType = tunnelNumber;


            //重新排序
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

            //重新赋值
            arr = tempArr;

            /**
             * 加载总条数
             */
            var content = "";
            for (var i = 0; i < arr.length; i++) {

                content += "<iframe width='700px' height='400px' src='/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=true&className=" + arr[i] + "&tunnelNumber=" + tunnelNumber + "&params=" + others + "'></iframe>"
                downloadName += arr[i] + ","

            }


            $("#total").append(content);


        })

        //下载
        function downloadAll() {
            location.href = "/tunnel/login/downloadExcel.do?downloadType=2&className=total&tunnelNumber=" + numberType + ":" + downloadName + "&excelName=隧道编号" + numberType + "全部";
        }


    </script>
</head>
<body>
<c:if test="${!empty pageName && pageName==false}">
    <button type="button" class="btn btn-info" name="backid" id="totalIndexBackid">返回列表</button>
    <button type="button" class="btn btn-danger" type="button">
        <a href="javascript:downloadAll()">总下载</a>
    </button>
</c:if>
<input type="hidden" value="${params}" id="params"/>
<div id="total">

</div>
<script>

    $("#totalIndexBackid").click(function () {

        console.log()

        var params = $("#params").val().trim().split("|")[1];

        location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=query&params=" + params;

    });


</script>
<br/>
<br/>
<br/>
<br/>
</body>
</html>
