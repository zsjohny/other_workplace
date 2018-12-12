<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道修改</title>
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
    </script>
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
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">隧道编号</td>
            <td colspan="2" contenteditable="true" name="tunnelNumber"
                onfocus="resetTips()">${tunnelInfo.tunnelNumber}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">隧道名称</td>
            <td colspan="2" contenteditable="true" name="tunnelName" onfocus="resetTips()">${tunnelInfo.tunnelName}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">所在项目编号</td>
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
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">所在线位</td>
            <td colspan="2" contenteditable="true" name="wireLocation"
                onfocus="resetTips()">${tunnelInfo.wireLocation}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">设计阶段</td>
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
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">结构型式</td>
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
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">隧道走向</td>
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
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">施工组织建议</td>
            <td colspan="2" contenteditable="true" name="constructionTeamAdvice"
                onfocus="resetTips()">${tunnelInfo.constructionTeamAdvice}</td>
        </tr>


        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">气候条件</td>
            <td colspan="2" contenteditable="true" name="weatherCondition"
                onfocus="resetTips()">${tunnelInfo.weatherCondition}</td>
        </tr>


        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">地形地貌</td>
            <td colspan="2" contenteditable="true" name="topographicFeatures"
                onfocus="resetTips()">${tunnelInfo.topographicFeatures}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">地层岩性概况</td>
            <td colspan="2" contenteditable="true" name="generalSituationFormation"
                onfocus="resetTips()">${tunnelInfo.generalSituationFormation}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">不良地质概况</td>
            <td colspan="2" contenteditable="true" name="badGeologicalSurvey"
                onfocus="resetTips()">${tunnelInfo.badGeologicalSurvey}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">水文地质概况</td>
            <td colspan="2" contenteditable="true" name="hydrogeologicalSurvey"
                onfocus="resetTips()">${tunnelInfo.hydrogeologicalSurvey}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">地震烈度及动参数
            </td>
            <td colspan="2" contenteditable="true" name="dynamicParameters"
                onfocus="resetTips()">${tunnelInfo.dynamicParameters}</td>
        </tr>

        <tr>
            <td colspan="2" width="10%" style="vertical-align: middle;text-align: center" class="tableleft">浅埋偏压情况</td>
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
                <button type="button" class="btn btn-primary" id="userModifySave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="tunnelInfoModifyBackid">返回列表</button>
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
        $('#tunnelInfoModifyBackid').click(function () {
            location.href = "/tunnel/tunnelInfo/findTunnelInfo.do?page=" + $("#page").val();

        });

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