<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 洞口失稳风险源调查管理</title>
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
<input type="hidden" id="pageName" value="${pageName}"/>
<c:if test="${authorLevel !=2}">
    <c:if test="${!empty pageName && pageName==false}">
        <button type="button" class="btn btn-success" id="tunnelHeadRiskExamineNew">新增</button>
    </c:if>
</c:if>
<input type="hidden" id="page" value="${page}"/>
<c:if test="${empty pageName}">
    <button type="button" class="btn btn-info" name="backid" id="tunnelHeadRiskExamineEditBackid">返回列表</button>
</c:if>
<c:if test="${!empty total && total !=0}">
    <button type="button" class="btn btn-info" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=0&className=tunnelHeadRiskExamine&excelName=洞口失稳风险源调查">本页下载</a>
    </button>
    <button type="button" class="btn btn-warning" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=1&className=tunnelHeadRiskExamine&tunnelNumber=${params}&excelName=洞口失稳风险源调查全部">全部下载</a>
    </button>
</c:if>
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
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
                <td width="10%" class="tableleft">风险源是否存在</td>
                <td>
                    <c:if test="${baseDomain.riskSourceExists==true}">
                        <input type="radio" name="riskSourceExists" value="true"
                               onclick="javascript:$('#riskPro').show()"
                               checked/>存在
                        <input type="radio" name="riskSourceExists" value="false"
                               onclick="javascript:$('#riskPro').hide()"/>不存在
                    </c:if>
                    <c:if test="${baseDomain.riskSourceExists==false}">
                        <input type="radio" name="riskSourceExists" value="true"
                               onclick="javascript:$('#riskPro').show()"/>存在
                        <input type="radio" name="riskSourceExists" value="false" checked
                               onclick="javascript:$('#riskPro').hide()"/>不存在
                    </c:if>
                </td>
            </tr>


            <tr id="riskPro" <c:if test="${baseDomain.riskSourceExists==false}">style="display: none" </c:if>>
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
                <td width="10%" class="tableleft">植被发育</td>
                <td><input type="text" name="vegetationDeveop" value="${baseDomain.vegetationDeveop}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">地表水系</td>
                <td><input type="text" name="shallowWater" value="${baseDomain.shallowWater}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">地层岩性</td>
                <td><input type="text" name="generalSituation" value="${baseDomain.generalSituation}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">地下水</td>
                <td><input type="text" name="undergroundWater" value="${baseDomain.undergroundWater}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">地质构造</td>
                <td><input type="text" name="geologicalStructure" value="${baseDomain.geologicalStructure}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">不良地质</td>
                <td><input type="text" name="badGeological" value="${baseDomain.badGeological}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">顺层偏压</td>
                <td><input type="text" name="beddingBias" value="${baseDomain.beddingBias}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">地形偏压</td>
                <td><input type="text" name="trrainBias" value="${baseDomain.trrainBias}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">周边环境</td>
                <td><input type="text" name="arroundEnviroment" value="${baseDomain.arroundEnviroment}"/></td>
            </tr>


            <tr>
                <td class="tableleft"></td>
                <td>
                    <button type="button" class="btn btn-primary" type="button" id="tunnelHeadRiskExamineEditSave">修改
                    </button>
                    <button type="button" class="btn btn-success" type="button"><a
                            href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${baseDomain.id}&tunnelNumber=${tunnelNumber}&className=tunnelHeadRiskExamine&page=${page}&pageType=Detail&pageName=${pageName}&params=${params}">详情</a>
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
            <th>Id</th>
            <th>隧道编号</th>
            <th>风险源是否存在</th>
            <th>风险发生概率</th>
            <th>风险源描述</th>
            <th>进口or出口</th>
            <th>地形地貌</th>
            <th>植被发育</th>
            <th>地表水系</th>
            <th>地层岩性</th>
            <th>地下水</th>
            <th>地质构造</th>
            <th>不良地质</th>
            <th>顺层偏压</th>
            <th>地形偏压</th>
            <th>周边环境</th>


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
                    <td><c:if test="${list.riskSourceExists==true}">存在</c:if><c:if
                            test="${list.riskSourceExists==false}">不存在</c:if></td>
                    <td>
                        <c:if test="${list.riskSourceExists==true}">
                            <c:if test="${list.riskHappendProbalility==1}">几乎不可能</c:if>
                            <c:if test="${list.riskHappendProbalility==2}">很少发生</c:if>
                            <c:if test="${list.riskHappendProbalility==3}">偶然发生</c:if>
                            <c:if test="${list.riskHappendProbalility==4}">可能发生</c:if>
                            <c:if test="${list.riskHappendProbalility==5}">频繁发生</c:if>
                        </c:if>
                    </td>
                    <td>${list.riskDecribe}</td>
                    <td>
                        <c:if test="${list.importOrExport==true}">
                            进口
                        </c:if>
                        <c:if test="${list.importOrExport==false}">
                            出口
                        </c:if>
                    </td>
                    <td>${list.topographicFeatures}</td>
                    <td>${list.vegetationDeveop}</td>
                    <td>${list.shallowWater}</td>
                    <td>${list.generalSituation}</td>
                    <td>${list.undergroundWater}</td>
                    <td>${list.geologicalStructure}</td>
                    <td>${list.badGeological}</td>
                    <td>${list.beddingBias}</td>
                    <td>${list.trrainBias}</td>
                    <td>${list.arroundEnviroment}</td>


                    <c:if test="${authorLevel !=2}">
                        <td>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&tunnelNumber=${tunnelNumber}&className=tunnelHeadRiskExamine&page=${page}&pageType=Edit&pageName=${pageName}&params=${params}">编辑</a>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&tunnelNumber=${tunnelNumber}&className=tunnelHeadRiskExamine&page=${page}&pageType=Detail&pageName=${pageName}&params=${params}">详情</a>

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
        location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=tunnelHeadRiskExamine&tunnelNumber=" + number + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&params=" + $("#params").val().trim();


    }


    function del(id) {


        if (confirm("确定要删除吗？")) {

            var page = $("#page").val();
            window.location.href = "/tunnel/tunnelInfoTotal/removerTunnelInfoTotal.do?id=" + id + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&tunnelNumber=" + number + "&className=tunnelHeadRiskExamine&params=" + $("#params").val();


        }


    }

    $("#tunnelHeadRiskExamineEditBackid").click(function () {


        var params = $("#params").val().trim();

        location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=query&params=" + params;

    });


    //添加
    $("#tunnelHeadRiskExamineNew").click(function () {


        location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/tunnelHeadRiskExamineAdd&params=" + $("#params").val().trim();
    });

    //修改
    $('#tunnelHeadRiskExamineEditSave').click(function () {
        var id = $("input[name='id']").val();
        var tunnelNumber = $("input[name='tunnelNumber']").val();
        var riskSourceExists = $("input[name='riskSourceExists']:checked").val();
        var riskHappendProbalility = $("select[name='riskHappendProbalility'] option:selected").val();
        var riskDecribe = $("input[name='riskDecribe']").val();

        var importOrExport = $("input[name='importOrExport']:checked").val();
        var topographicFeatures = $("input[name='topographicFeatures']").val();
        var vegetationDeveop = $("input[name='vegetationDeveop']").val();
        var shallowWater = $("input[name='shallowWater']").val();
        var generalSituation = $("input[name='generalSituation']").val();
        var undergroundWater = $("input[name='undergroundWater']").val();
        var geologicalStructure = $("input[name='geologicalStructure']").val();
        var badGeological = $("input[name='badGeological']").val();
        var beddingBias = $("input[name='beddingBias']").val();
        var trrainBias = $("input[name='trrainBias']").val();
        var arroundEnviroment = $("input[name='arroundEnviroment']").val();

        $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=tunnelHeadRiskExamine&id=" + id + "&tunnelNumber=" + tunnelNumber + "&riskSourceExists=" + riskSourceExists
                + "&riskHappendProbalility=" + riskHappendProbalility + "&riskDecribe=" + riskDecribe + "&importOrExport=" + importOrExport + "&topographicFeatures=" + topographicFeatures + "&vegetationDeveop=" + vegetationDeveop + "&shallowWater=" + shallowWater + "&generalSituation=" + generalSituation + "&undergroundWater=" + undergroundWater + "&geologicalStructure=" + geologicalStructure + "&badGeological=" + badGeological + "&beddingBias=" + beddingBias + "&trrainBias=" + trrainBias + "&arroundEnviroment=" + arroundEnviroment, function (data) {

            if (data == true) {
                forward(1);
            } else {
                $("input[name='topographicFeatures']").val("");

                $("input[name='topographicFeatures']").attr("placeholder", "修改失败,错误原因联系技术!!");

            }
        })

    });


</script>
