<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 岩爆风险源调查添加</title>
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
            <td width="10%" class="tableleft">风险源是否存在</td>
            <td>
                <input type="radio" name="riskSourceExists" value="true" onclick="javascript:$('#riskPro').show()"
                       checked/>存在
                <input type="radio" name="riskSourceExists" value="false" onclick="javascript:$('#riskPro').hide()"/>
                不存在
            </td>
        </tr>

        <tr id="riskPro">
            <td width="10%" class="tableleft">风险发生概率</td>
            <td>
                <select name="riskHappendProbalility">
                    <option selected value="1">几乎不可能</option>
                    <option value="2">很少发生</option>
                    <option value="3">偶然发生</option>
                    <option value="4">可能发生</option>
                    <option value="5">频繁发生</option>
                </select>
            </td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">风险源描述</td>
            <td><input type="text" name="riskDecribe"/></td>
        </tr>


        <tr>
            <td width="10%" class="tableleft">起始里程</td>
            <td><input type="text" name="startCourse" placeholder="请填写数字!!"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">终止里程</td>
            <td><input type="text" name="endCourse" placeholder="请填写数字!!"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地层岩性</td>
            <td><input type="text" name="generalSituation"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft"> 岩石单轴饱和抗压强度（Rc）</td>
            <td><input type="text" name="uniaxialSaturatedCompressiveStrengthOfRock" <%--placeholder="请填写数字!!"--%>/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">地质构造</td>
            <td><input type="text" name="geologicalStructure"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">隧道埋深</td>
            <td><input type="text" name="tunnelLine" <%--placeholder="请填写数字!!"--%>/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">最大主应力σ1</td>
            <td><input type="text" name="maxStrain" <%--placeholder="请填写数字!!"--%>/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">洞轴线与最大主应力夹角</td>
            <td><input type="text" name="tunnelAxesAndMaxStrain"/></td>
        </tr>


        <tr>

            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" id="rockOutburstRiskExamineAddSave">保存
                </button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="rockOutburstRiskExamineAddBackid">返回列表
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

        $('#rockOutburstRiskExamineAddSave').click(function () {
            var tunnelNumber = $("input[name='tunnelNumber']").val();

            var riskSourceExists = $("input[name='riskSourceExists']:checked").val();
            var riskHappendProbalility = $("select[name='riskHappendProbalility'] option:selected").val();
            var riskDecribe = $("input[name='riskDecribe']").val();


            var startCourse = $("input[name='startCourse']").val();
            var endCourse = $("input[name='endCourse']").val();
            var generalSituation = $("input[name='generalSituation']").val();
            var uniaxialSaturatedCompressiveStrengthOfRock = $("input[name='uniaxialSaturatedCompressiveStrengthOfRock']").val();
            var geologicalStructure = $("input[name='geologicalStructure']").val();
            var tunnelLine = $("input[name='tunnelLine']").val();
            var maxStrain = $("input[name='maxStrain']").val();
            var tunnelAxesAndMaxStrain = $("input[name='tunnelAxesAndMaxStrain']").val();

            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=rockOutburstRiskExamine&tunnelNumber=" + tunnelNumber + "&riskSourceExists=" + riskSourceExists
                    + "&riskHappendProbalility=" + riskHappendProbalility + "&riskDecribe=" + riskDecribe + "&startCourse=" + startCourse + "&endCourse=" + endCourse + "&generalSituation=" + generalSituation + "&uniaxialSaturatedCompressiveStrengthOfRock=" + uniaxialSaturatedCompressiveStrengthOfRock + "&geologicalStructure=" + geologicalStructure + "&tunnelLine=" + tunnelLine + "&maxStrain=" + maxStrain + "&tunnelAxesAndMaxStrain=" + tunnelAxesAndMaxStrain, function (data) {

                if (data == true) {
                    $("#rockOutburstRiskExamineAddBackid").click();
                } else {
                    $("input[name='startCourse']").val("");

                    $("input[name='startCourse']").attr("placeholder", "新增失败,错误原因联系技术!!");

                }
            })

        });


        $("#rockOutburstRiskExamineAddBackid").click(function () {
            var params = $("#params").val();
            //判断是否是页面选择进来的还是从原始页面进来的
            if (params.split(":")[2] == '1') {

                location.href = '/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=false&className=rockOutburstRiskExamine&tunnelNumber=' + params.split(":")[1].trim() + '&params=' + params.split(":")[0].trim() + ":" + params.split(":")[1].trim() + ':1'
                return;
            }

            location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=examine&params=" + params;

        });

    })
    ;
</script>
