<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/tunnel/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/tunnel/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="/tunnel/css/style.css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.js"></script>
    <script type="text/javascript" src="/tunnel/js/ckform.js"></script>
    <script type="text/javascript" src="/tunnel/js/common.js"></script>


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
<input type="hidden" id="page" value="${page}"/>
<c:if test="${!empty total && total!=1 && total!=0}">
<form class="form-inline definewidth m20" action="/tunnel/tunnelInfo/findTunnelInfo.do" method="post">
    隧道编号：
    <input type="text" name="tunnelNumber" id="findName" class="abc input-default" value="${tunnelNumber}">&nbsp;&nbsp;
    <input type="hidden" name="page" value="${page}"/>
    <button type="submit" class="btn btn-primary">查询</button>
    &nbsp;&nbsp;
    </c:if>
    <c:if test="${authorLevel !=2}">
        <button type="button" class="btn btn-success" id="addTunnelInfonew">新增隧道</button>
    </c:if>
    <%--<c:if test="${!empty total && total !=0}">
        <button type="button" class="btn btn-info" type="button">
            <a href="/tunnel/login/downloadExcel.do?downloadType=0&className=tunnelInfo&excelName=隧道管理">本页下载</a>
        </button>
        <button type="button" class="btn btn-warning" type="button">
            <a href="/tunnel/login/downloadExcel.do?downloadType=1&className=tunnelInfo&tunnelNumber=test:test:test&excelName=隧道管理全部">全部下载</a>
        </button>
    </c:if>--%>
    <c:if test="${total==1}">
        <form method="post" class="definewidth m20">

            <input type="hidden" name="tunnelInfoId" value="${tunnelInfo.id}"/>
            <table class="table table-bordered table-hover definewidth m10">
                <tr>
                    <th colspan="2"></th>
                    <th style="vertical-align: middle;text-align: center">进口端</th>
                    <th style="vertical-align: middle;text-align: center">出口端</th>
                </tr>


                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        隧道编号
                    </td>
                    <td colspan="2" contenteditable="true" name="tunnelNumber"
                        onfocus="resetTips()">${tunnelInfo.tunnelNumber}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        隧道名称
                    </td>
                    <td colspan="2" contenteditable="true" name="tunnelName"
                        onfocus="resetTips()">${tunnelInfo.tunnelName}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        所在项目编号
                    </td>
                    <td colspan="2">

                        <select name="projectNumber" class="form-control">
                            <c:forEach var="list" items="${list}">
                                <c:if test="${tunnelInfo.projectNumber == list.projectNumber}">
                                    <option selected
                                            value="${list.projectNumber}">${list.projectNumber}</option>
                                </c:if>
                                <c:if test="${tunnelInfo.projectNumber != list.projectNumber}">
                                    <option
                                            value="${list.projectNumber}">${list.projectNumber}</option>
                                </c:if>

                            </c:forEach>
                        </select>

                    </td>
                </tr>


                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        所在线位
                    </td>
                    <td colspan="2" contenteditable="true" name="wireLocation"
                        onfocus="resetTips()">${tunnelInfo.wireLocation}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        设计阶段
                    </td>
                    <td colspan="2">
                        <c:if test="${tunnelInfo.designPeriod ==0}">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="0" checked/> 工可
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="1"/> 初步设计
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="2"/> 施工图设计
                                </label>
                            </div>
                        </c:if>
                        <c:if test="${tunnelInfo.designPeriod ==1}">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="0"/> 工可
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="1" checked/> 初步设计
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="2"/> 施工图设计
                                </label>
                            </div>
                        </c:if>
                        <c:if test="${tunnelInfo.designPeriod ==2}">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="0"/> 工可
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="1"/> 初步设计
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="designPeriod" value="2" checked/> 施工图设计
                                </label>
                            </div>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        结构型式
                    </td>
                    <td colspan="2">
                        <c:if test="${tunnelInfo.structuralStyle==0}">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="0" checked/> 分离式
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="1"/> 连体式
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="2"/> 单洞
                                </label>
                            </div>
                        </c:if>
                        <c:if test="${tunnelInfo.structuralStyle==1}">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="0"/> 分离式
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="1" checked/> 连体式
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="2"/> 单洞
                                </label>
                            </div>
                        </c:if>
                        <c:if test="${tunnelInfo.structuralStyle==2}">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="0"/> 分离式
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="1"/> 连体式
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="structuralStyle" value="2" checked/> 单洞
                                </label>
                            </div>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        隧道走向
                    </td>
                    <td colspan="2" contenteditable="true" name="tunnelDriection"
                        onfocus="resetTips()">${tunnelInfo.tunnelDriection}</td>
                </tr>


                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">起讫里程</td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">右线</td>
                    <td contenteditable="true" name="startCourse_rightWire_importPort"
                        onfocus="resetTips()">${tunnelInfo.startCourse_rightWire_importPort}</td>
                    <td contenteditable="true" name="startCourse_rightWire_exportPort"
                        onfocus="resetTips()">${tunnelInfo.startCourse_rightWire_exportPort}</td>
                </tr>


                <tr>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">左线</td>
                    <td contenteditable="true" name="startCourse_leftWire_importPort"
                        onfocus="resetTips()">${tunnelInfo.startCourse_leftWire_importPort}</td>
                    <td contenteditable="true" name="startCourse_leftWire_exportPort"
                        onfocus="resetTips()">${tunnelInfo.startCourse_leftWire_exportPort}</td>
                </tr>


                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">路面标高<br/>(m)
                    </td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">右线</td>
                    <td contenteditable="true" name="roadHigh_rightWire_importPort"
                        onfocus="resetTips()">${tunnelInfo.roadHigh_rightWire_importPort}</td>
                    <td contenteditable="true" name="roadHigh_rightWire_exportPort"
                        onfocus="resetTips()">${tunnelInfo.roadHigh_rightWire_exportPort}</td>
                </tr>


                <tr>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">左线</td>
                    <td contenteditable="true" name="roadHigh_leftWire_importPort"
                        onfocus="resetTips()">${tunnelInfo.roadHigh_leftWire_importPort}</td>
                    <td contenteditable="true" name="roadHigh_leftWire_exportPort"
                        onfocus="resetTips()">${tunnelInfo.roadHigh_leftWire_exportPort}</td>
                </tr>
                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">洞门型式</td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">右线</td>
                    <td contenteditable="true" name="portalType_rightWire_importPort"
                        onfocus="resetTips()">${tunnelInfo.portalType_rightWire_importPort}</td>
                    <td contenteditable="true" name="portalType_rightWire_exportPort"
                        onfocus="resetTips()">${tunnelInfo.portalType_rightWire_exportPort}</td>
                </tr>


                <tr>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">左线</td>
                    <td contenteditable="true" name="portalType_leftWire_importPort"
                        onfocus="resetTips()">${tunnelInfo.portalType_leftWire_importPort}</td>
                    <td contenteditable="true" name="portalType_leftWire_exportPort"
                        onfocus="resetTips()">${tunnelInfo.portalType_leftWire_exportPort}</td>
                </tr>



                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">线间距<br/>(m)
                    </td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">洞口</td>
                    <td contenteditable="true" name="interWire_tunnelHead_importPort"
                        onfocus="resetTips()">${tunnelInfo.interWire_tunnelHead_importPort}</td>
                    <td contenteditable="true" name="interWire_tunnelHead_exportPort"
                        onfocus="resetTips()">${tunnelInfo.interWire_tunnelHead_exportPort}</td>
                </tr>

                <tr>
                    <td width="10%" class="tableleft" style="vertical-align: middle;text-align: center">洞身</td>
                    <td contenteditable="true" name="interWire_tunnelBody" colspan="2">${tunnelInfo.interWire_tunnelBody}</td>
                </tr>


                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">平面线形</td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">右线</td>
                    <td colspan="2" contenteditable="true" name="parallelLine_rightWire"
                        onfocus="resetTips()">${tunnelInfo.parallelLine_rightWire}</td>

                </tr>


                <tr>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">左线</td>
                    <td colspan="2" contenteditable="true" name="parallelLine_leftWire"
                        onfocus="resetTips()">${tunnelInfo.parallelLine_leftWire}</td>

                </tr>

                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">坡度坡长</td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">右线</td>
                    <td colspan="2" contenteditable="true" name="slopeLine_rightWire"
                        onfocus="resetTips()">${tunnelInfo.slopeLine_rightWire}</td>
                </tr>


                <tr>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">左线</td>
                    <td colspan="2" contenteditable="true" name="slopeLine_leftWire"
                        onfocus="resetTips()">${tunnelInfo.slopeLine_leftWire}</td>
                </tr>

                <tr>
                    <td width="10%" rowspan="2" class="tableleft" style="vertical-align: middle;text-align: center">隧道埋深<br/>(m)
                    </td>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">右线</td>
                    <td colspan="2" contenteditable="true" name="tunnelLine_rightWire"
                        onfocus="resetTips()">${tunnelInfo.tunnelLine_rightWire}</td>
                </tr>

                <tr>
                    <td width="10%" style="vertical-align: middle;text-align: center" class="tableleft">左线</td>
                    <td colspan="2" contenteditable="true" name="tunnelLine_leftWire"
                        onfocus="resetTips()">${tunnelInfo.tunnelLine_leftWire}</td>
                </tr>


                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        施工组织建议
                    </td>
                    <td colspan="2" contenteditable="true" name="constructionTeamAdvice"
                        onfocus="resetTips()">${tunnelInfo.constructionTeamAdvice}</td>
                </tr>


                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        气候条件
                    </td>
                    <td colspan="2" contenteditable="true" name="weatherCondition"
                        onfocus="resetTips()">${tunnelInfo.weatherCondition}</td>
                </tr>


                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        地形地貌
                    </td>
                    <td colspan="2" contenteditable="true" name="topographicFeatures"
                        onfocus="resetTips()">${tunnelInfo.topographicFeatures}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        地层岩性概况
                    </td>
                    <td colspan="2" contenteditable="true" name="generalSituationFormation"
                        onfocus="resetTips()">${tunnelInfo.generalSituationFormation}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        不良地质概况
                    </td>
                    <td colspan="2" contenteditable="true" name="badGeologicalSurvey"
                        onfocus="resetTips()">${tunnelInfo.badGeologicalSurvey}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        水文地质概况
                    </td>
                    <td colspan="2" contenteditable="true" name="hydrogeologicalSurvey"
                        onfocus="resetTips()">${tunnelInfo.hydrogeologicalSurvey}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        地震烈度及动参数
                    </td>
                    <td colspan="2" contenteditable="true" name="dynamicParameters"
                        onfocus="resetTips()">${tunnelInfo.dynamicParameters}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        浅埋偏压情况
                    </td>
                    <td colspan="2" contenteditable="true" name="shallowBuried"
                        onfocus="resetTips()">${tunnelInfo.shallowBuried}</td>
                </tr>

                <tr>
                    <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">
                        既有或规划建（构）筑物
                    </td>
                    <td colspan="2" contenteditable="true" name="plannedConstruction"
                        onfocus="resetTips()">${tunnelInfo.plannedConstruction}</td>
                </tr>

                <tr>
                    <td colspan="2" style="vertical-align: middle;text-align: center" class="tableleft"></td>
                    <td colspan="2">
                        <button type="button" class="btn btn-primary" id="userModifySave">修改</button>
                        &nbsp;&nbsp; &nbsp;&nbsp;
                        <button type="button" class="btn btn-danger" type="button"><a
                                href="javascript:del(${tunnelInfo.id})">删除</a></button>

                    </td>


                </tr>
            </table>
        </form>

        <script>

            //恢复提示
            function resetTips() {
                if ($("td[name='tunnelNumber']").text() == "隧道编号不得为空!!" || $("td[name='tunnelNumber']").text() == "隧道编号不得少于3位!!") {
                    $("td[name='tunnelNumber']").text("");
                }
                if ($("td[name='tunnelName']").text() == "隧道名称不得为空!!" || $("td[name='tunnelName']").text() == "隧道名称不得少于3位!!") {
                    $("td[name='tunnelName']").text("");
                }
            }

            $(function () {


                $('#userModifySave').click(function () {

                    var id = $("input[name='tunnelInfoId']").val();
                    var tunnelNumber = $("td[name='tunnelNumber']").text();
                    var tunnelName = $("td[name='tunnelName']").text();
                    var projectNumber = $("select[name='projectNumber'] option:selected").val();


                    //检验

                    if (projectNumber == "" || projectNumber == undefined) {
                        $("select[name='projectNumber']").append("<option selected>请先去项目管理添加项目!!</option>");
                        return;
                    }


                    if (tunnelNumber == "") {
                        $("td[name='tunnelNumber']").text("隧道编号不得为空!!");
                        return;
                    }

                    if (tunnelName == "") {
                        $("td[name='tunnelName']").text("隧道名称不得为空!!");
                        return;
                    }


                    if (tunnelNumber.length < 3) {
                        $("td[name='tunnelNumber']").text("隧道编号不得少于3位!!");
                        return;
                    }

                    if (tunnelName.length < 3) {
                        $("td[name='tunnelName']").text("隧道名称不得少于3位!!");
                        return;
                    }

                    if ($("td[name='tunnelNumber']").text() == "隧道编号不得为空!!" || $("td[name='tunnelNumber']").text() == "隧道编号不得少于3位!!") {
                        return;
                    }
                    if ($("td[name='tunnelName']").text() == "隧道名称不得为空!!" || $("td[name='tunnelName']").text() == "隧道名称不得少于3位!!") {

                        return;
                    }
                    var wireLocation = $("td[name='wireLocation']").text();
                    var designPeriod = $("input[name='designPeriod']:checked").val();
                    var structuralStyle = $("input[name='structuralStyle']:checked").val();
                    var tunnelDriection = $("td[name='tunnelDriection']").text();
                    var startCourse_rightWire_importPort = $("td[name='startCourse_rightWire_importPort']").text();
                    var startCourse_rightWire_exportPort = $("td[name='startCourse_rightWire_exportPort']").text();
                    var startCourse_leftWire_importPort = $("td[name='startCourse_leftWire_importPort']").text();
                    var startCourse_leftWire_exportPort = $("td[name='startCourse_leftWire_exportPort']").text();
                    var roadHigh_rightWire_importPort = $("td[name='roadHigh_rightWire_importPort']").text();
                    var roadHigh_rightWire_exportPort = $("td[name='roadHigh_rightWire_exportPort']").text();
                    var roadHigh_leftWire_importPort = $("td[name='roadHigh_leftWire_importPort']").text();
                    var roadHigh_leftWire_exportPort = $("td[name='roadHigh_leftWire_exportPort']").text();
                    var portalType_rightWire_importPort = $("td[name='portalType_rightWire_importPort']").text();
                    var portalType_rightWire_exportPort = $("td[name='portalType_rightWire_exportPort']").text();
                    var portalType_leftWire_importPort = $("td[name='portalType_leftWire_importPort']").text();
                    var portalType_leftWire_exportPort = $("td[name='portalType_leftWire_exportPort']").text();
                    var interWire_tunnelHead_importPort = $("td[name='interWire_tunnelHead_importPort']").text();
                    var interWire_tunnelHead_exportPort = $("td[name='interWire_tunnelHead_exportPort']").text();
                    var interWire_tunnelBody = $("td[name='interWire_tunnelBody']").text();
                    var parallelLine_rightWire = $("td[name='parallelLine_rightWire']").text();
                    var parallelLine_leftWire = $("td[name='parallelLine_leftWire']").text();
                    var slopeLine_rightWire = $("td[name='slopeLine_rightWire']").text();
                    var slopeLine_leftWire = $("td[name='slopeLine_leftWire']").text();
                    var tunnelLine_rightWire = $("td[name='tunnelLine_rightWire']").text();
                    var tunnelLine_leftWire = $("td[name='tunnelLine_leftWire']").text();
                    var constructionTeamAdvice = $("td[name='constructionTeamAdvice']").text();
                    var weatherCondition = $("td[name='weatherCondition']").text();
                    var topographicFeatures = $("td[name='topographicFeatures']").text();
                    var generalSituationFormation = $("td[name='generalSituationFormation']").text();
                    var badGeologicalSurvey = $("td[name='badGeologicalSurvey']").text();
                    var hydrogeologicalSurvey = $("td[name='hydrogeologicalSurvey']").text();
                    var dynamicParameters = $("td[name='dynamicParameters']").text();
                    var shallowBuried = $("td[name='shallowBuried']").text();
                    var plannedConstruction = $("td[name='plannedConstruction']").text();


                    $.post("/tunnel/tunnelInfo/modifyTunnelInfo.do",
                            "id=" + id + "&tunnelNumber=" + tunnelNumber + "&tunnelName=" + tunnelName +
                            "&projectNumber=" + projectNumber + "&wireLocation=" + wireLocation + "&designPeriod=" + designPeriod + "&structuralStyle=" + structuralStyle + "&tunnelDriection=" + tunnelDriection + "&startCourse_rightWire_importPort=" + startCourse_rightWire_importPort
                            + "&startCourse_rightWire_exportPort=" + startCourse_rightWire_exportPort + "&startCourse_leftWire_importPort=" + startCourse_leftWire_importPort + "&startCourse_leftWire_exportPort=" + startCourse_leftWire_exportPort +
                            "&roadHigh_rightWire_importPort=" + roadHigh_rightWire_importPort + "&roadHigh_rightWire_exportPort=" + roadHigh_rightWire_exportPort + "&roadHigh_leftWire_importPort=" + roadHigh_leftWire_importPort + "&roadHigh_leftWire_exportPort=" + roadHigh_leftWire_exportPort +
                            "&portalType_rightWire_importPort=" + portalType_rightWire_importPort + "&portalType_rightWire_exportPort=" + portalType_rightWire_exportPort + "&portalType_leftWire_importPort=" + portalType_leftWire_importPort + "&portalType_leftWire_exportPort=" + portalType_leftWire_exportPort +
                            "&interWire_tunnelHead_importPort=" + interWire_tunnelHead_importPort + "&interWire_tunnelHead_exportPort=" + interWire_tunnelHead_exportPort + "&interWire_tunnelBody=" + interWire_tunnelBody + "&parallelLine_rightWire=" + parallelLine_rightWire +
                            "&parallelLine_leftWire=" + parallelLine_leftWire + "&slopeLine_rightWire=" + slopeLine_rightWire + "&slopeLine_leftWire=" + slopeLine_leftWire + "&tunnelLine_rightWire=" + tunnelLine_rightWire + "&tunnelLine_leftWire=" + tunnelLine_leftWire
                            + "&constructionTeamAdvice=" + constructionTeamAdvice + "&weatherCondition=" + weatherCondition + "&topographicFeatures=" + topographicFeatures + "&generalSituationFormation=" + generalSituationFormation +
                            "&badGeologicalSurvey=" + badGeologicalSurvey + "&hydrogeologicalSurvey=" + hydrogeologicalSurvey + "&dynamicParameters=" + dynamicParameters + "&shallowBuried=" + shallowBuried + "&plannedConstruction=" + plannedConstruction
                            , function (data) {
                                data = parseInt(data);

                                if (data == 0) {
                                    tunnelNumber = "";
                                    if (${!empty tunnelNumber}) {
                                        tunnelNumber = $("#tunnelInfoEditFindUser").val();
                                    }

                                    location.href = "/tunnel/tunnelInfo/findTunnelInfo.do?page=1&tunnelNumber=" + tunnelNumber;

                                } else if (data == 1) {
                                    $("td[name='tunnelNumber']").text("");

                                    $("td[name='tunnelNumber']").text("placeholder", "修改失败,错误原因联系技术!!");

                                } else if (data == 2) {
                                    var tunnelNumber = $("td[name='tunnelNumber']").text();

                                    $("td[name='tunnelNumber']").text("");

                                    $("td[name='tunnelNumber']").text("placeholder", "隧道编号:" + tunnelNumber + ",已经存在!!");
                                }
                            })

                });


            });
        </script>
    </c:if>
    <c:if test="${!empty total && total!=1 && total!=0}">
</form>


<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
        <th style="vertical-align: middle;text-align: center">隧道编号</th>
        <th style="vertical-align: middle;text-align: center">隧道名称</th>
        <th style="vertical-align: middle;text-align: center">所在项目编号</th>
        <th style="vertical-align: middle;text-align: center">所在线位</th>
        <th style="vertical-align: middle;text-align: center">设计阶段</th>
        <th style="vertical-align: middle;text-align: center">结构型式</th>
        <th style="vertical-align: middle;text-align: center">隧道走向</th>
        <th style="vertical-align: middle;text-align: center">施工组织建议</th>
        <th style="vertical-align: middle;text-align: center">气候条件</th>
        <th style="vertical-align: middle;text-align: center">地形地貌</th>
        <th style="vertical-align: middle;text-align: center">地层岩性概况</th>
        <th style="vertical-align: middle;text-align: center">不良地质概况</th>
        <th style="vertical-align: middle;text-align: center">水文地质概况</th>
        <th style="vertical-align: middle;text-align: center">地震烈度及动参数</th>
        <th style="vertical-align: middle;text-align: center">浅埋偏压情况</th>
        <th style="vertical-align: middle;text-align: center">既有或规划建(构)筑物</th>
        <c:if test="${authorLevel !=2}">
            <th>操作</th>
        </c:if>

    </tr>
    </thead>
    <tbody>
    <if test="${!empty list}">

        <c:forEach items="${list}" var="list" varStatus="status">
            <tr>
                <td style="vertical-align: middle;text-align: center">${list.tunnelNumber}</td>
                <td style="vertical-align: middle;text-align: center">${list.tunnelName}</td>
                <td style="vertical-align: middle;text-align: center">${list.projectNumber}</td>
                <td style="vertical-align: middle;text-align: center">${list.wireLocation}</td>
                <td style="vertical-align: middle;text-align: center">
                    <c:if test="${list.designPeriod==0}">
                        工可
                    </c:if>
                    <c:if test="${list.designPeriod==1}">
                        初步设计
                    </c:if>
                    <c:if test="${list.designPeriod==2}">
                        施工图设计
                    </c:if>
                </td>
                <td style="vertical-align: middle;text-align: center">
                    <c:if test="${list.structuralStyle ==0}">
                        分离式
                    </c:if>
                    <c:if test="${list.structuralStyle ==1}">
                        连体式
                    </c:if>
                    <c:if test="${list.structuralStyle ==2}">
                        单洞
                    </c:if>
                </td>
                <td style="vertical-align: middle;text-align: center">${list.tunnelDriection}</td>
                <td style="vertical-align: middle;text-align: center">${list.constructionTeamAdvice}</td>
                <td style="vertical-align: middle;text-align: center">${list.weatherCondition}</td>
                <td style="vertical-align: middle;text-align: center">${list.topographicFeatures}</td>
                <td style="vertical-align: middle;text-align: center">${list.generalSituationFormation}</td>
                <td style="vertical-align: middle;text-align: center">${list.badGeologicalSurvey}</td>
                <td style="vertical-align: middle;text-align: center">${list.hydrogeologicalSurvey}</td>
                <td style="vertical-align: middle;text-align: center">${list.dynamicParameters}</td>
                <td style="vertical-align: middle;text-align: center">${list.shallowBuried}</td>
                <td style="vertical-align: middle;text-align: center">${list.plannedConstruction}</td>
                <c:if test="${authorLevel !=2}">
                    <td style="vertical-align: middle;text-align: center">
                        <a href="/tunnel/tunnelInfo/findTunnelInfo.do?id=${list.id}&tunnelNumber=${tunnelNumber}&page=${page}&pageType=edit">编辑</a>
                        <a href="/tunnel/tunnelInfo/findTunnelInfo.do?id=${list.id}&tunnelNumber=${tunnelNumber}&page=${page}&pageType=detail">详情</a>
                        <a href="javascript:del(${list.id})">删除</a>

                    </td>
                </c:if>
            </tr>
            <c:if test="${status.last}">
                <tr>
                    <c:if test="${authorLevel ==2}">
                    <td colspan='32'>
                        </c:if>
                        <c:if test="${authorLevel ==0 || authorLevel ==1}">
                    <td colspan='33'>
                        </c:if>
                        <ul class="pager">
                            <li>总记录${total }条</li>
                            <li></li>
                            <li>
                                <a href="javascript:forward(1)">首页</a>
                            </li>
                            <c:if test="${page ==1 && totalPage !=1}">
                                <li>
                                    <a href="javascript:forward(${page +1})">下一页</a>
                                </li>
                            </c:if>
                            <c:if test="${page !=1}">
                                <li>
                                    <a href="javascript:forward(${page -1})">上一页</a>
                                </li>
                                <c:if test="${page != totalPage}">
                                    <li>
                                        <a href="javascript:forward(${page +1})">下一页</a>
                                    </li>
                                </c:if>
                            </c:if>
                            <li>
                                <a href="javascript:forward(${totalPage})">尾页</a>
                            </li>


                            <li>当前页${ page}/${totalPage}</li>

                            <li></li>
                            <li>
                                <select name="pageNumber" style="width: 35px;height: 25px">
                                    <c:if test="${totalPage ==1}">
                                        <option value="1" selected>1</option>
                                    </c:if>
                                    <c:if test="${totalPage !=1}">
                                        <c:forEach varStatus="status" begin="1" end="${totalPage}">
                                            <c:if test="${page == status.index}">
                                                <option value="${status.count}" selected>${status.count}</option>
                                            </c:if>
                                            <c:if test="${page != status.index}">
                                                <option value="${status.count}">${status.count}</option>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </select>
                            </li>

                            <li><a href="javascript:forward(-1)">GO</a></li>
                        </ul>

                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </if>

    </tbody>
</table>

</c:if>
<br/>
<br/>
<br/>
<br/>
</body>
</html>
<script>

    function forward(id) {

        var page = id;

        if (id == -1) {
            page = $("select[name='pageNumber'] option:selected").val();

        }

        if (${!empty tunnelNumber}) {
            page = page + "&tunnelNumber=" + $("#findName").val();

        }

        location.href = "/tunnel/tunnelInfo/findTunnelInfo.do?page=" + page;

    }

    $(function () {


        $('#addTunnelInfonew').click(function () {

            window.location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=false";
        });


    });

    function del(id) {


        if (confirm("确定要删除吗？")) {
            var tunnelNumber = "";
            if (${!empty tunnelNumber}) {
                name = $("#findName").val();

            }

            var page = $("#page").val();
            window.location.href = "/tunnel/tunnelInfo/removerTunnelInfo.do?id=" + id + "&page=" + page + "&tunnelNumber=" + tunnelNumber;

        }


    }
</script>