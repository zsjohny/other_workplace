<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道进出口调查添加</title>
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
<form class="definewidth m20">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">隧道编号</td>
            <td><input type="text" readonly name="tunnelNumber"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">进口or出口</td>
            <td>
                <input type="radio" name="importOrExport" value="true" checked/> 进口
                <input type="radio" name="importOrExport" value="false"/> 出口
            </td>
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
            <td width="10%" class="tableleft">洞口地质概况</td>
            <td><input type="text" name="tunnelHeadQuality"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议刷坡坡率</td>
            <td><input type="text" name="cuttingSlope" placeholder="请填写数字!!"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地表水影响</td>
            <td><input type="text" name="surfaceWaterInfluence"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">建议洞门型式</td>
            <td>
                <select name="portalType">
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
            <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
            <td><input type="text" name="plannedConstruction"/></td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">洞口衔接条件</td>
            <td><input type="text" name="tunnelHeadCondition"/></td>
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
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="tunnelImportPortAddSave">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="tunnelImportPortAddBackid">返回列表</button>
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

        $('#tunnelImportPortAddSave').click(function () {

            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var importOrExport = $("input[name='importOrExport']:checked").val();
            var topographicFeatures = $("input[name='topographicFeatures']").val();
            var vegetationCover = $("input[name='vegetationCover']").val();
            var tunnelHeadQuality = $("input[name='tunnelHeadQuality']").val();
            var cuttingSlope = $("input[name='cuttingSlope']").val();
            var surfaceWaterInfluence = $("input[name='surfaceWaterInfluence']").val();
            var portalType = $("select[name='portalType'] option:selected").val();
            var plannedConstruction = $("input[name='plannedConstruction']").val();
            var tunnelHeadCondition = $("input[name='tunnelHeadCondition']").val();
            var constructionPlant = $("input[name='constructionPlant']").val();
            var constructionRoad = $("input[name='constructionRoad']").val();
            var waterEnviron = $("input[name='waterEnviron']").val();
            var otherNotice = $("input[name='otherNotice']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do",
                    "className=tunnelImportPort&tunnelNumber=" + tunnelNumber + "&importOrExport=" + importOrExport + "&topographicFeatures=" +
                    topographicFeatures + "&vegetationCover=" + vegetationCover + "&tunnelHeadQuality=" + tunnelHeadQuality + "&cuttingSlope=" + cuttingSlope + "&surfaceWaterInfluence=" + surfaceWaterInfluence + "&portalType=" + portalType
                    + "&plannedConstruction=" + plannedConstruction + "&tunnelHeadCondition=" + tunnelHeadCondition + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad
                    + "&waterEnviron=" + waterEnviron + "&otherNotice=" + otherNotice, function (data) {

                        if (data == true) {
                            $("#tunnelImportPortAddBackid").click();
                        } else {
                            $("input[name='topographicFeatures']").val("");

                            $("input[name='topographicFeatures']").attr("placeholder", "新增失败,错误原因联系技术!!");

                        }
                    })

        });





        $("#tunnelImportPortAddBackid").click(function () {
            var params = $("#params").val(); 
     //判断是否是页面选择进来的还是从原始页面进来的
            if (params.split(":")[2] == '1') {

                location.href = '/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=false&className=tunnelImportPort&tunnelNumber=' + params.split(":")[1].trim() + '&params=' + params.split(":")[0].trim() + ":" + params.split(":")[1].trim() + ':1'
                return;
            }

            location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=examine&params=" + params;

        });

    });
</script>
