<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 交通事故风险源调查修改</title>
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
            <td width="10%" class="tableleft">平面线形</td>
            <td><input type="text" name="parallelLine" value="${baseDomain.parallelLine}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">纵面线形</td>
            <td><input type="text" name="verticalAlignment" value="${baseDomain.verticalAlignment}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft"> 交通量(Pcu/h)</td>
            <td><input type="text" name="trafficRadio" value="${baseDomain.trafficRadio}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">货车比例</td>
            <td><input type="text" name="truckScaleRadio" value="${baseDomain.truckScaleRadio}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">运营通风方案</td>
            <td><input type="text" name="operationalVentilationScheme"
                       value="${baseDomain.operationalVentilationScheme}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">防灾救援方案</td>
            <td><input type="text" name="fileRecue" value="${baseDomain.fileRecue}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">监控方案</td>
            <td><input type="text" name="monitorPlan" value="${baseDomain.monitorPlan}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">照明方案</td>
            <td><input type="text" name="lightPlan" value="${baseDomain.lightPlan}"/></td>
        </tr>


        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="trafficAccidentRiskExamineEditSave">保存
                </button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="trafficAccidentRiskExamineEditBackid">
                    返回列表
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

        $('#trafficAccidentRiskExamineEditSave').click(function () {
            var id = $("input[name='id']").val();
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var riskSourceExists = $("input[name='riskSourceExists']:checked").val();
            var riskHappendProbalility = $("select[name='riskHappendProbalility'] option:selected").val();
            var riskDecribe = $("input[name='riskDecribe']").val();


            var parallelLine = $("input[name='parallelLine']").val();
            var verticalAlignment = $("input[name='verticalAlignment']").val();
            var trafficRadio = $("input[name='trafficRadio']").val();
            var truckScaleRadio = $("input[name='truckScaleRadio']").val();
            var operationalVentilationScheme = $("input[name='operationalVentilationScheme']").val();
            var fileRecue = $("input[name='fileRecue']").val();
            var monitorPlan = $("input[name='monitorPlan']").val();
            var lightPlan = $("input[name='lightPlan']").val();
            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=trafficAccidentRiskExamine&id=" + id + "&tunnelNumber=" + tunnelNumber + "&riskSourceExists=" + riskSourceExists
                    + "&riskHappendProbalility=" + riskHappendProbalility + "&riskDecribe=" + riskDecribe + "&parallelLine=" + parallelLine + "&verticalAlignment=" + verticalAlignment + "&trafficRadio=" + trafficRadio + "&truckScaleRadio=" + truckScaleRadio + "&operationalVentilationScheme=" + operationalVentilationScheme + "&fileRecue=" + fileRecue + "&monitorPlan=" + monitorPlan + "&lightPlan=" + lightPlan, function (data) {

                if (data == true) {
                    $("#trafficAccidentRiskExamineEditBackid").click();
                } else {
                    $("input[name='startCourse']").val("");

                    $("input[name='startCourse']").attr("placeholder", "修改失败,错误原因联系技术!!");

                }
            })

        });


        $("#trafficAccidentRiskExamineEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=trafficAccidentRiskExamine&tunnelNumber=" + params.split(":")[1] + "&page=" + page +"&pageName="+$("#pageName").val().trim()+ "&params=" + params;

        });

    });
</script>
