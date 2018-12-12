<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>隧道进出口调查管理</title>
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
        <button type="button" class="btn btn-success" id="tunnelImportPortNew">新增</button>
    </c:if>
</c:if>
<c:if test="${empty pageName}">
    <button type="button" class="btn btn-info" name="backid" id="tunnelImportPortEditBackid">返回列表</button>

</c:if>
<c:if test="${!empty total && total !=0}">
    <button type="button" class="btn btn-info" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=0&className=tunnelImportPort&excelName=隧道进出口">本页下载</a>
    </button>
    <button type="button" class="btn btn-warning" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=1&className=tunnelImportPort&tunnelNumber=${params}&excelName=隧道进出口全部">全部下载</a>
    </button>
</c:if>
<link rel=" stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<c:if test="${total==1}">
    <input type="hidden" name="id" value="${baseDomain.id}"/>
    <form class="definewidth m20">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td width="10%" class="tableleft">隧道编号</td>
                <td><input type="text" name="tunnelNumber" readonly value="${baseDomain.tunnelNumber}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">进口or出口</td>
                <td>
                    <c:if test="${baseDomain.importOrExport==true}">
                        <input type="radio" name="importOrExport" value="true" checked/> 进口
                        <input type="radio" name="importOrExport" value="false"/> 出口
                    </c:if>
                    <c:if test="${baseDomain.importOrExport==false}">
                        <input type="radio" name="importOrExport" value="true"/> 进口
                        <input type="radio" name="importOrExport" value="false" checked/> 出口
                    </c:if>
                </td>
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
                <td width="10%" class="tableleft">洞口地质概况</td>
                <td><input type="text" name="tunnelHeadQuality" value="${baseDomain.tunnelHeadQuality}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">建议刷坡坡率</td>
                <td><input type="text" name="cuttingSlope" value="${baseDomain.cuttingSlope}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">地表水影响</td>
                <td><input type="text" name="surfaceWaterInfluence" value="${baseDomain.surfaceWaterInfluence}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">建议洞门型式</td>
                <td>
                    <select name="portalType">
                        <c:if test="${baseDomain.portalType==0}">
                            <option selected value="0">端墙式</option>
                            <option value="1">削竹式</option>
                            <option value="2">环框式</option>
                            <option value="3">翼墙式</option>
                            <option value="4">柱式</option>
                            <option value="5">台阶式</option>
                            <option value="6">遮光棚式</option>
                        </c:if>

                        <c:if test="${baseDomain.portalType==1}">
                            <option value="0">端墙式</option>
                            <option selected value="1">削竹式</option>
                            <option value="2">环框式</option>
                            <option value="3">翼墙式</option>
                            <option value="4">柱式</option>
                            <option value="5">台阶式</option>
                            <option value="6">遮光棚式</option>
                        </c:if>
                        <c:if test="${baseDomain.portalType==2}">
                            <option value="0">端墙式</option>
                            <option value="1">削竹式</option>
                            <option selected value="2">环框式</option>
                            <option value="3">翼墙式</option>
                            <option value="4">柱式</option>
                            <option value="5">台阶式</option>
                            <option value="6">遮光棚式</option>
                        </c:if>
                        <c:if test="${baseDomain.portalType==3}">
                            <option value="0">端墙式</option>
                            <option value="1">削竹式</option>
                            <option value="2">环框式</option>
                            <option selected value="3">翼墙式</option>
                            <option value="4">柱式</option>
                            <option value="5">台阶式</option>
                            <option value="6">遮光棚式</option>
                        </c:if>
                        <c:if test="${baseDomain.portalType==4}">
                            <option value="0">端墙式</option>
                            <option value="1">削竹式</option>
                            <option value="2">环框式</option>
                            <option value="3">翼墙式</option>
                            <option selected value="4">柱式</option>
                            <option value="5">台阶式</option>
                            <option value="6">遮光棚式</option>
                        </c:if>
                        <c:if test="${baseDomain.portalType==5}">
                            <option value="0">端墙式</option>
                            <option value="1">削竹式</option>
                            <option value="2">环框式</option>
                            <option value="3">翼墙式</option>
                            <option value="4">柱式</option>
                            <option selected value="5">台阶式</option>
                            <option value="6">遮光棚式</option>
                        </c:if>
                        <c:if test="${baseDomain.portalType==6}">
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
                <td width="10%" class="tableleft">既有或规划建(构)筑物</td>
                <td><input type="text" name="plannedConstruction" value="${baseDomain.plannedConstruction}"/></td>
            </tr>

            <tr>
                <td width="10%" class="tableleft">洞口衔接条件</td>
                <td><input type="text" name="tunnelHeadCondition" value="${baseDomain.tunnelHeadCondition}"/></td>
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
                <td class="tableleft"></td>
                <td>
                    <button type="button" class="btn btn-primary" type="button" id="tunnelImportPortEditSave">修改
                    </button>
                    <button type="button" class="btn btn-success" type="button">
                        <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${baseDomain.id}&tunnelNumber=${tunnelNumber}&className=tunnelImportPort&page=${page}&pageType=Detail&pageName=${pageName}&params=${params}">详情</a>
                    </button>
                    <button type="button" class="btn btn-danger" type="button">
                        <a href="javascript:del(${baseDomain.id})">删除</a>
                    </button>
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
            <th>进口or出口</th>
            <th>地形地貌</th>
            <th>植被条件</th>
            <th>洞口地质概况</th>
            <th>建议刷坡坡率</th>
            <th>地表水影响</th>
            <th>建议洞门型式</th>
            <th>既有或规划建(构)筑物</th>
            <th>洞口衔接条件</th>
            <th>施工场地</th>
            <th>施工便道</th>
            <th>环保水保</th>
            <th>其它需注意问题</th>
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
                    <td>
                        <c:if test="${list.importOrExport==true}">
                            进口
                        </c:if>
                        <c:if test="${list.importOrExport==false}">
                            出口
                        </c:if>
                    </td>
                    <td>${list.topographicFeatures}</td>
                    <td>${list.vegetationCover}</td>
                    <td>${list.tunnelHeadQuality}</td>
                    <td>${list.cuttingSlope}</td>
                    <td>${list.surfaceWaterInfluence}</td>
                    <td>
                        <c:if test="${list.portalType==0}">
                            端墙式
                        </c:if>
                        <c:if test="${list.portalType==1}">
                            削竹式
                        </c:if>
                        <c:if test="${list.portalType==2}">
                            环框式
                        </c:if>
                        <c:if test="${list.portalType==3}">
                            翼墙式
                        </c:if>
                        <c:if test="${list.portalType==4}">
                            柱式
                        </c:if>
                        <c:if test="${list.portalType==5}">
                            台阶式
                        </c:if>
                        <c:if test="${list.portalType==6}">
                            遮光棚式
                        </c:if>
                    </td>
                    <td>${list.plannedConstruction}</td>
                    <td>${list.tunnelHeadCondition}</td>
                    <td>${list.constructionPlant}</td>
                    <td>${list.constructionRoad}</td>
                    <td>${list.waterEnviron}</td>
                    <td>${list.otherNotice}</td>
                    <c:if test="${authorLevel !=2}">
                        <td>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&tunnelNumber=${tunnelNumber}&className=tunnelImportPort&page=${page}&pageType=Edit&pageName=${pageName}&params=${params}">编辑</a>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&tunnelNumber=${tunnelNumber}&className=tunnelImportPort&page=${page}&pageType=Detail&pageName=${pageName}&params=${params}">详情</a>

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
        location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelImportPort&tunnelNumber=" + number + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&params=" + $("#params").val().trim();


    }


    function del(id) {


        if (confirm("确定要删除吗？")) {

            var page = $("#page").val();
            window.location.href = "/tunnel/tunnelInfoTotal/removerTunnelInfoTotal.do?id=" + id + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&tunnelNumber=" + number + "&className=tunnelImportPort&params=" + $("#params").val();


        }


    }

    $("#tunnelImportPortEditBackid").click(function () {


        var params = $("#params").val().trim();

        location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=query&params=" + params;

    });


    //添加
    $("#tunnelImportPortNew").click(function () {


        location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/tunnelImportPortAdd&params=" + $("#params").val().trim();
    });

    //修改
    $('#tunnelImportPortEditSave').click(function () {

        var id = $("input[name='id']").val();
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
                "className=tunnelImportPort&id=" + id + "&tunnelNumber=" + tunnelNumber + "&importOrExport=" + importOrExport + "&topographicFeatures=" +
                topographicFeatures + "&vegetationCover=" + vegetationCover + "&tunnelHeadQuality=" + tunnelHeadQuality + "&cuttingSlope=" + cuttingSlope + "&surfaceWaterInfluence=" + surfaceWaterInfluence + "&portalType=" + portalType
                + "&plannedConstruction=" + plannedConstruction + "&tunnelHeadCondition=" + tunnelHeadCondition + "&constructionPlant=" + constructionPlant + "&constructionRoad=" + constructionRoad
                + "&waterEnviron=" + waterEnviron + "&otherNotice=" + otherNotice, function (data) {

                    if (data == true) {
                        forward(1);
                    } else {
                        $("input[name='topographicFeatures']").val("");

                        $("input[name='topographicFeatures']").attr("placeholder", "修改失败,错误原因联系技术!!");

                    }
                })

    });


</script>
