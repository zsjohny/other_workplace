<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧辅助通道调查修改</title>
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
<div id="myAlert" class="alert alert-success">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <strong style=" width: auto;display: table;margin-left: auto;margin-right: auto;">删除相应图片时候点击相应图片即可!!</strong>
</div>
<input type="hidden" id="params" value="${params}"/>
<input type="hidden" id="pageName" value="${pageName}"/>
<input type="hidden" id="page" value="${page}"/>
<input type="hidden" name="id" value="${baseDomain.id}"/>
<form class="definewidth m20" id="assistFiles">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" name="tunnelNumber" readonly value="${baseDomain.tunnelNumber}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地形地貌</td>
            <td><input type="text" name="topographicFeatures" value="${baseDomain.topographicFeatures}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">植被条件</td>
            <td><input type="text" name="vegetationCover" value="${baseDomain.vegetationCover}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地质概况</td>
            <td><input type="text" name="roadQuality" value="${baseDomain.roadQuality}"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">不良地质概况</td>
            <td><input type="text" name="badGeologicalSurvey" value="${baseDomain.badGeologicalSurvey}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">水文条件概况</td>
            <td><input type="text" name="hydrologyCondition" value="${baseDomain.hydrologyCondition}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议洞门型式</td>
            <td>
                <select name="tunnelHeadStyle">
                    <c:if test="${baseDomain.tunnelHeadStyle==0}">
                        <option selected value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>

                    <c:if test="${baseDomain.tunnelHeadStyle==1}">
                        <option value="0">端墙式</option>
                        <option selected value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.tunnelHeadStyle==2}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option selected value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.tunnelHeadStyle==3}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option selected value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.tunnelHeadStyle==4}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option selected value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.tunnelHeadStyle==5}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option selected value="5">台阶式</option>
                        <option value="6">遮光棚式</option>
                    </c:if>
                    <c:if test="${baseDomain.tunnelHeadStyle==6}">
                        <option value="0">端墙式</option>
                        <option value="1">削竹式</option>
                        <option value="2">环框式</option>
                        <option value="3">翼墙式</option>
                        <option value="4">柱式</option>
                        <option value="5">台阶式</option>
                        <option selected value="6">遮光棚式</option>
                    </c:if>
                </select>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议刷坡坡率</td>
            <td><input type="text" name="cuttingSlope" value="${baseDomain.cuttingSlope}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td><input type="text" name="plannedConstruction" value="${baseDomain.plannedConstruction}"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">施工场地</td>
            <td><input type="text" name="constructionPlant" value="${baseDomain.constructionPlant}"/></td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">施工便道</td>
            <td><input type="text" name="constructionRoad" value="${baseDomain.constructionRoad}"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">环保水保</td>
            <td><input type="text" name="waterEnviron" value="${baseDomain.waterEnviron}"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td><input type="text" name="otherNotice" value="${baseDomain.otherNotice}"/></td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">辅助通道类型</td>
            <td>
                <c:if test="${baseDomain.accessTunnelType==0}">
                    <option selected value="0">斜井</option>
                    <option value="1">竖井</option>
                    <option value="2">横洞</option>
                    <option value="3">平行导坑</option>
                </c:if>
                <c:if test="${baseDomain.accessTunnelType==1}">
                    <option value="0">斜井</option>
                    <option selected value="1">竖井</option>
                    <option value="2">横洞</option>
                    <option value="3">平行导坑</option>
                </c:if>
                <c:if test="${baseDomain.accessTunnelType==2}">
                    <option value="0">斜井</option>
                    <option value="1">竖井</option>
                    <option selected value="2">横洞</option>
                    <option value="3">平行导坑</option>
                </c:if>
                <c:if test="${baseDomain.accessTunnelType==3}">
                    <option value="0">斜井</option>
                    <option value="1">竖井</option>
                    <option value="2">横洞</option>
                    <option selected value="3">平行导坑</option>
                </c:if>
            </td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">与主洞平面关系</td>
            <td><input type="text" name="mainTunnelRelationWithPlane"
                       value="${baseDomain.mainTunnelRelationWithPlane}"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">辅助坑道/风井与主洞平面关系示意图</td>
            <td id="displayPhotos">
                <input type="hidden" name="acccessTunnelAndAirshaftRelationWithMainTunnel"
                       value="${baseDomain.acccessTunnelAndAirshaftRelationWithMainTunnel}"/>
                <input type="file" name="files" accept="image/*" multiple onchange="upload()"/>
            </td>


        </tr>

        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="assistTunnelAddSave">保存</button>
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
                    content += "<img  class='img-responsive center-block'  id='" + pathArr[i].split(".")[0] + "' onclick='delPhotos(\"" + pathArr[i] + "\")' src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=assistTunnel&name=" + pathArr[i] + "'/>";
                }
            }
            $("#displayPhotos").append(content);

        }


        $('#assistTunnelAddSave').click(function () {

            var id = $("input[name='id']").val();
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var topographicFeatures = $("input[name='topographicFeatures']").val();
            var vegetationCover = $("input[name='vegetationCover']").val();
            var roadQuality = $("input[name='roadQuality']").val();
            var badGeologicalSurvey = $("input[name='badGeologicalSurvey']").val();
            var hydrologyCondition = $("input[name='hydrologyCondition']").val();
            var tunnelHeadStyle = $("select[name='tunnelHeadStyle'] option:selected").val();
            var cuttingSlope = $("input[name='cuttingSlope']").val();
            var plannedConstruction = $("input[name='plannedConstruction']").val();
            var constructionPlant = $("input[name='constructionPlant']").val();
            var constructionRoad = $("input[name='constructionRoad']").val();
            var waterEnviron = $("input[name='waterEnviron']").val();
            var otherNotice = $("input[name='otherNotice']").val();
            var accessTunnelType = $("select[name='accessTunnelType'] option:selected").val();
            var mainTunnelRelationWithPlane = $("input[name='mainTunnelRelationWithPlane']").val();
            var acccessTunnelAndAirshaftRelationWithMainTunnel = $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do",
                    "className=assistTunnel&id=" + id + "&tunnelNumber=" + tunnelNumber + "&topographicFeatures=" + topographicFeatures + "&vegetationCover=" +
                    vegetationCover + "&roadQuality=" + roadQuality + "&badGeologicalSurvey=" + badGeologicalSurvey + "&hydrologyCondition=" + hydrologyCondition + "&tunnelHeadStyle=" + tunnelHeadStyle + "&cuttingSlope=" + cuttingSlope
                    + "&plannedConstruction=" + plannedConstruction + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad + "&waterEnviron=" + waterEnviron
                    + "&otherNotice=" + otherNotice + "&accessTunnelType=" + accessTunnelType + "&mainTunnelRelationWithPlane=" + mainTunnelRelationWithPlane + "&acccessTunnelAndAirshaftRelationWithMainTunnel=" + acccessTunnelAndAirshaftRelationWithMainTunnel, function (data) {

                        if (data == true) {
                            $("#assistTunnelEditBackid").click();
                        } else {
                            $("input[name='topographicFeatures']").val("");

                            $("input[name='topographicFeatures']").attr("placeholder", "修改失败,错误原因联系技术!!");

                        }
                    })

        });


        $("#assistTunnelEditBackid").click(function () {

            var params = $("#params").val();
            var page = $("#page").val();
            location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=assistTunnel&tunnelNumber=" + params.split(":")[1] + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&params=" + params;

        });

    });

    function upload() {
        var formData = new FormData($("#assistFiles")[0]);
        $.ajax({
            url: '/tunnel/tunnelInfoTotal/uploadPhotos.do?className=assistTunnel',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                var originalPath = $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val();
                $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val(originalPath + data);
            },
            error: function () {
                alert("上传图片失败，请返回重新加载试试!!");
            }
        });
    }
    function delPhotos(photoName) {
        if (confirm("确定要删除吗？")) {
            $.post("/tunnel/tunnelInfoTotal/delPhoto.do", "className=assistTunnel&name=" + photoName + "&id=" + $("input[name='id']").val(), function (data) {
                if (data == true) {
                    $("#" + photoName.split(".")[0] + "").hide();


                    //加载图片属性
                    var paths = $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val()

                    if (paths != '') {
                        //加载图片
                        var pathArr = paths.toString().split(":");
                        var content = "";

                        for (var i = 0; i < pathArr.length; i++) {
                            if (pathArr[i] != '' && pathArr[i].trim() != '') {

                                if (!(pathArr[i].indexOf(photoName) > -1)) {
                                    content += pathArr[i] + ":";
                                }
                            }
                        }
                        //重新赋值
                        $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val(content);

                    }


                } else {
                    alert("图片删除失败!!");
                }
            });


        }

    }

</script>