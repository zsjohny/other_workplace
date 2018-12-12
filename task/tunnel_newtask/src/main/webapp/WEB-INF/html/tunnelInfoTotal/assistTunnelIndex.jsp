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
<input type="hidden" id="page" value="${page}"/>
<input type="hidden" id="pageName" value="${pageName}"/>
<c:if test="${authorLevel !=2}">
    <c:if test="${!empty pageName && pageName==false}">
        <button type="button" class="btn btn-success" id="assistTunnelNew">新增</button>
    </c:if>
</c:if>
<c:if test="${empty pageName}">
    <button type="button" class="btn btn-info" name="backid" id="assistTunnelEditBackid">返回列表</button>
</c:if>
<c:if test="${!empty total && total !=0}">
    <button type="button" class="btn btn-info" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=0&className=assistTunnel&excelName=辅助通道调查调查">本页下载</a>
    </button>
    <button type="button" class="btn btn-warning" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=1&className=assistTunnel&tunnelNumber=${params}&excelName=辅助通道调查全部">全部下载</a>
    </button>
</c:if>
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<c:if test="${total==1}">
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
                        <select name="accessTunnelType">
                            <option selected value="0">斜井</option>
                            <option value="1">竖井</option>
                            <option value="2">横洞</option>
                            <option value="3">平行导坑</option>
                        </select>
                    </c:if>
                    <c:if test="${baseDomain.accessTunnelType==1}">
                        <select name="accessTunnelType">
                            <option value="0">斜井</option>
                            <option selected value="1">竖井</option>
                            <option value="2">横洞</option>
                            <option value="3">平行导坑</option>
                        </select>
                    </c:if>
                    <c:if test="${baseDomain.accessTunnelType==2}">
                        <select name="accessTunnelType">
                            <option value="0">斜井</option>
                            <option value="1">竖井</option>
                            <option selected value="2">横洞</option>
                            <option value="3">平行导坑</option>
                        </select>
                    </c:if>
                    <c:if test="${baseDomain.accessTunnelType==3}">
                        <select name="accessTunnelType">
                            <option value="0">斜井</option>
                            <option value="1">竖井</option>
                            <option value="2">横洞</option>
                            <option selected value="3">平行导坑</option>
                        </select>
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
                    <button type="button" class="btn btn-primary" type="button" id="assistTunnelAddSave">修改</button>

                    <button type="button" class="btn btn-success" type="button"><a
                            href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${baseDomain.id}&pageType=Detail&pageName=${pageName}&tunnelNumber=${tunnelNumber}&className=assistTunnel&page=${page}&params=${params}">详情</a>
                    </button>
                    <button type="button" class="btn btn-danger" type="button"><a
                            href="javascript:del(${baseDomain.id})">删除</a></button>

                </td>
            </tr>
        </table>
    </form>

</c:if>
<c:if test="${!empty total && total!=1 && total!=0}">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>ID</th>
            <th>隧道编号</th>
            <th>地形地貌</th>
            <th>植被条件</th>
            <th>地质概况</th>
            <th>不良地质概况</th>
            <th>水文条件概况</th>
            <th>建议洞门型式</th>
            <th>建议刷坡坡率</th>
            <th>既有或规划建(构)筑物</th>
            <th>施工场地</th>
            <th>施工便道</th>
            <th>环保水保</th>
            <th>其它需注意问题</th>
            <th>辅助通道类型</th>
            <th>与主洞平面关系</th>
            <th>辅助坑道/风井与主洞平面关系示意图</th>
            <c:if test="${authorLevel !=2}">
                <th>操作</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <if test="${!empty list}">

            <c:forEach items="${list}" var="list" varStatus="status">
                <tr>
                    <td>${list.id}</td>
                    <td>${list.tunnelNumber}</td>
                    <td>${list.topographicFeatures}</td>
                    <td>${list.vegetationCover}</td>
                    <td>${list.roadQuality}</td>
                    <td>${list.badGeologicalSurvey}</td>
                    <td>${list.hydrologyCondition}</td>
                    <td>
                        <c:if test="${list.tunnelHeadStyle==0}">
                            端墙式
                        </c:if>
                        <c:if test="${list.tunnelHeadStyle==1}">
                            削竹式
                        </c:if>
                        <c:if test="${list.tunnelHeadStyle==2}">
                            环框式
                        </c:if>
                        <c:if test="${list.tunnelHeadStyle==3}">
                            翼墙式
                        </c:if>
                        <c:if test="${list.tunnelHeadStyle==4}">
                            柱式
                        </c:if>
                        <c:if test="${list.tunnelHeadStyle==5}">
                            台阶式
                        </c:if>
                        <c:if test="${list.tunnelHeadStyle==6}">
                            遮光棚式
                        </c:if>
                    </td>
                    <td>${list.cuttingSlope}</td>
                    <td>${list.plannedConstruction}</td>
                    <td>${list.constructionPlant}</td>
                    <td>${list.constructionRoad}</td>
                    <td>${list.waterEnviron}</td>
                    <td>${list.otherNotice}</td>
                    <td>
                        <c:if test="${list.accessTunnelType==0}">
                            斜井
                        </c:if>
                        <c:if test="${list.accessTunnelType==1}">
                            竖井
                        </c:if>
                        <c:if test="${list.accessTunnelType==2}">
                            横洞
                        </c:if>
                        <c:if test="${list.accessTunnelType==3}">
                            平行导坑
                        </c:if>
                    </td>
                    <td>${list.mainTunnelRelationWithPlane}</td>
                    <td>${list.acccessTunnelAndAirshaftRelationWithMainTunnel}</td>

                    <c:if test="${authorLevel !=2}">
                        <td>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&pageType=Edit&pageName=${pageName}&tunnelNumber=${tunnelNumber}&className=assistTunnel&page=${page}&params=${params}">编辑</a>

                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&pageType=Detail&pageName=${pageName}&tunnelNumber=${tunnelNumber}&className=assistTunnel&page=${page}&params=${params}">详情</a>
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
    var number = localStorage.getItem("number");
    function forward(id) {

        var page = id;
        if (id == -1) {


            page = $("select[name='pageNumber'] option:selected").val();
        }
        location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=assistTunnel&tunnelNumber=" + number + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&params=" + $("#params").val().trim();


    }


    function del(id) {


        if (confirm("确定要删除吗？")) {

            var page = $("#page").val();
            window.location.href = "/tunnel/tunnelInfoTotal/removerTunnelInfoTotal.do?id=" + id + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&tunnelNumber=" + number + "&className=assistTunnel&params=" + $("#params").val();


        }


    }

    $("#assistTunnelEditBackid").click(function () {


        var params = $("#params").val().trim();

        location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=query&params=" + params;

    });


    //添加
    $("#assistTunnelNew").click(function () {


        location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/assistTunnelAdd&params=" + $("#params").val().trim();
    });
    //修改
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
                            forward(1);
                        } else {
                            $("input[name='topographicFeatures']").val("");

                            $("input[name='topographicFeatures']").attr("placeholder", "修改失败,错误原因联系技术!!");

                        }
                    })

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
