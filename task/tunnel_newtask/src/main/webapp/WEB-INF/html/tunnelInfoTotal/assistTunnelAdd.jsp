<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>辅助通道调查管理</title>
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
<script>

    $(function () {
        //添加默认的项目编号
        $("input[name='tunnelNumber']").val($("#params").val().toString().split(":")[1]);

    })

</script>

<body>

<input type="hidden" id="params" value="${params}"/>
<form class="definewidth m20" id="assistFiles">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" readonly name="tunnelNumber"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地形地貌</td>
            <td><input type="text" name="topographicFeatures"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">植被条件</td>
            <td><input type="text" name="vegetationCover"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">地质概况</td>
            <td><input type="text" name="roadQuality"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">不良地质概况</td>
            <td><input type="text" name="badGeologicalSurvey"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">水文条件概况</td>
            <td><input type="text" name="hydrologyCondition"/></td>
        </tr>
        <tr>
            <td width="10%" class="tunnelHeadStyle">建议洞门型式</td>
            <td>
                <select name="tunnelHeadStyle">
                    <option selected value="0">端墙式</option>
                    <option value="1">削竹式</option>
                    <option value="2">环框式</option>
                    <option value="3">翼墙式</option>
                    <option value="4">柱式</option>
                    <option value="5">台阶式</option>
                    <option value="6">遮光棚式</option>
                </select>
            </td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">建议刷坡坡率</td>
            <td><input type="text" name="cuttingSlope"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td><input type="text" name="plannedConstruction"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">施工场地</td>
            <td><input type="text" name="constructionPlant"/></td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">施工便道</td>
            <td><input type="text" name="constructionRoad"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">环保水保</td>
            <td><input type="text" name="waterEnviron"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">其它需注意问题</td>
            <td><input type="text" name="otherNotice"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">辅助通道类型</td>
            <td>
                <select name="accessTunnelType">
                    <option selected value="0">斜井</option>
                    <option value="1">竖井</option>
                    <option value="2">横洞</option>
                    <option value="3">平行导坑</option>
                </select>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">与主洞平面关系</td>
            <td><input type="text" name="mainTunnelRelationWithPlane"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">辅助坑道/风井与主洞平面关系示意图</td>
            <td>
                <input type="hidden" name="acccessTunnelAndAirshaftRelationWithMainTunnel"/>
                <input type="file" name="files" accept="image/*" multiple onchange="upload()"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="assistTunnelAddSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="assistTunnelAddBackid">返回列表</button>
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

        $('#assistTunnelAddSave').click(function () {

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
                    "className=assistTunnel&tunnelNumber=" + tunnelNumber + "&topographicFeatures=" + topographicFeatures + "&vegetationCover=" +
                    vegetationCover + "&roadQuality=" + roadQuality + "&badGeologicalSurvey=" + badGeologicalSurvey + "&hydrologyCondition=" + hydrologyCondition + "&tunnelHeadStyle=" + tunnelHeadStyle + "&cuttingSlope=" + cuttingSlope
                    + "&plannedConstruction=" + plannedConstruction + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad + "&waterEnviron=" + waterEnviron + "&otherNotice=" + otherNotice
                    + "&accessTunnelType=" + accessTunnelType + "&mainTunnelRelationWithPlane=" + mainTunnelRelationWithPlane + "&acccessTunnelAndAirshaftRelationWithMainTunnel=" + acccessTunnelAndAirshaftRelationWithMainTunnel, function (data) {

                        if (data == true) {
                            $("#assistTunnelAddBackid").click();
                        } else {
                            $("input[name='topographicFeatures']").val("");

                            $("input[name='topographicFeatures']").attr("placeholder", "新增失败,错误原因联系技术!!");

                        }
                    })

        });


        $("#assistTunnelAddBackid").click(function () {
            var params = $("#params").val();
            //判断是否是页面选择进来的还是从原始页面进来的
            if (params.split(":")[2] == '1') {

                location.href = '/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=false&className=assistTunnel&tunnelNumber=' + params.split(":")[1].trim() + '&params=' + params.split(":")[0].trim() + ":" + params.split(":")[1].trim() + ':1'
                return;
            }

            location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=examine&params=" + params;

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
                $("input[name='acccessTunnelAndAirshaftRelationWithMainTunnel']").val(data);
            },
            error: function () {
                $("input[name='topographicFeatures']").attr("placeholder", "上传图片失败，刷新试试!!");
            }
        });
    }
</script>
