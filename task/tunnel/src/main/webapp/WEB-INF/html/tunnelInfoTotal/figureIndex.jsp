<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>附图管理</title>
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
        <button type="button" class="btn btn-success" id="figureNew">新增</button>
    </c:if>
</c:if>
<c:if test="${empty pageName}">
    <button type="button" class="btn btn-info" name="backid" id="figureEditBackid">返回列表</button>
</c:if>
<c:if test="${!empty total && total !=0}">
    <button type="button" class="btn btn-info" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=0&className=figure&excelName=附图">本页下载</a>
    </button>
    <button type="button" class="btn btn-warning" type="button">
        <a href="/tunnel/login/downloadExcel.do?downloadType=1&className=figure&tunnelNumber=${params}&excelName=附图全部">全部下载</a>
    </button>
</c:if>
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<c:if test="${total==1}">

    <input type="hidden" name="id" value="${baseDomain.id}"/>
    <form class="definewidth m20" id="editFiles">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td width="10%" class="tableleft">隧道编号</td>
                <td><input type="text" name="tunnelNumber" readonly value="${baseDomain.tunnelNumber}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">拍摄位置</td>
                <td>
                    <c:if test="${baseDomain.cameraSite==true}">
                        <input type="radio" name="cameraSite" value="true" checked/> 进口
                        <input type="radio" name="cameraSite" value="false"/> 出口
                    </c:if>
                    <c:if test="${baseDomain.cameraSite==false}">
                        <input type="radio" name="cameraSite" value="true"/> 进口
                        <input type="radio" name="cameraSite" value="false" checked/> 出口
                    </c:if>
                </td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">里程桩号</td>
                <td><input type="text" name="mileagePile" value="${baseDomain.mileagePile}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">拍摄方向</td>
                <td><input type="text" name="shootingDirection" value="${baseDomain.shootingDirection}"/></td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">拍摄时间</td>
                <td>
                    <input type="date" name="shootingTime" value="${baseDomain.shootingTime}"/>
                </td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">照片图像</td>
                <td id="displayPhotos">
                    <input type="hidden" name="photosPicture" value="${baseDomain.photosPicture}"/>
                    <input type="file" name="files" accept="image/*" multiple onchange="upload()"/>
                </td>

            </tr>


            <tr>
                <td class="tableleft"></td>
                <td>
                    <button type="button" class="btn btn-primary" type="button" id="figureEditSave">修改</button>
                    <button type="button" class="btn btn-success" type="button"><a
                            href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${baseDomain.id}&tunnelNumber=${tunnelNumber}&className=figure&page=${page}&pageType=Detail&pageName=${pageName}&params=${params}">详情</a>
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
            <th>拍摄位置</th>
            <th>里程桩号</th>
            <th>拍摄方向</th>
            <th>拍摄时间</th>
            <th>照片图像</th>


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
                        <c:if test="${list.cameraSite==true}">
                            进口
                        </c:if>
                        <c:if test="${list.cameraSite==false}">
                            出口
                        </c:if>
                    </td>
                    <td>${list.mileagePile}</td>
                    <td>${list.shootingDirection}</td>
                    <td>${list.shootingTime}</td>
                    <td>${list.photosPicture}</td>


                    <c:if test="${authorLevel !=2}">
                        <td>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&tunnelNumber=${tunnelNumber}&className=figure&page=${page}&pageType=Edit&pageName=${pageName}&params=${params}">编辑</a>
                            <a href="/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?id=${list.id}&tunnelNumber=${tunnelNumber}&className=figure&page=${page}&pageType=Detail&pageName=${pageName}&params=${params}">详情</a>
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
        location.href = " /tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?className=figure&tunnelNumber=" + number + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&params=" + $("#params").val().trim();


    }


    function del(id) {


        if (confirm("确定要删除吗？")) {

            var page = $("#page").val();
            window.location.href = "/tunnel/tunnelInfoTotal/removerTunnelInfoTotal.do?id=" + id + "&page=" + page + "&pageName=" + $("#pageName").val().trim() + "&tunnelNumber=" + number + "&className=figure&params=" + $("#params").val();


        }


    }

    $("#figureEditBackid").click(function () {


        var params = $("#params").val().trim();

        location.href = "/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=query&params=" + params;

    });


    //添加
    $("#figureNew").click(function () {


        location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/figureAdd&params=" + $("#params").val().trim();
    });
    //修改
    $(function () {

        //加载图片属性
        var paths = $("input[name='photosPicture']").val()

        if (paths != '') {
            //加载图片
            var pathArr = paths.toString().split(":");
            var content = "";

            for (var i = 0; i < pathArr.length; i++) {
                if (pathArr[i] != '' && pathArr[i].trim() != '') {
                    content += "<img  class='img-responsive center-block'  id='" + pathArr[i].split(".")[0] + "' onclick='delPhotos(\"" + pathArr[i] + "\")' src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=figure&name=" + pathArr[i] + "'/>";
                }
            }
            $("#displayPhotos").append(content);

        }


        $('#figureEditSave').click(function () {
            var id = $("input[name='id']").val();
            var tunnelNumber = $("input[name='tunnelNumber']").val();
            var cameraSite = $("input[name='cameraSite']:checked").val();
            var mileagePile = $("input[name='mileagePile']").val();
            var shootingDirection = $("input[name='shootingDirection']").val();
            var shootingTime = $("input[name='shootingTime']").val();
            var photosPicture = $("input[name='photosPicture']").val();


            $.post("/tunnel/tunnelInfoTotal/modifyTunnelInfoTotal.do", "className=figure&id=" + id + "&tunnelNumber=" + tunnelNumber + "&cameraSite=" + cameraSite + "&mileagePile=" + mileagePile + "&shootingDirection=" + shootingDirection + "&shootingTime=" + shootingTime + "&photosPicture=" + photosPicture, function (data) {

                if (data == true) {
                    forward(1);
                } else {
                    $("input[name='mileagePile']").val("");

                    $("input[name='mileagePile']").attr("placeholder", "修改失败,错误原因联系技术!!");

                }
            })

        });

    })

    function delPhotos(photoName) {
        if (confirm("确定要删除吗？")) {
            $.post("/tunnel/tunnelInfoTotal/delPhoto.do", "className=figure&name=" + photoName + "&id=" + $("input[name='id']").val(), function (data) {
                if (data == true) {
                    $("#" + photoName.split(".")[0] + "").hide();


                    //加载图片属性
                    var paths = $("input[name='photosPicture']").val()

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
                        $("input[name='photosPicture']").val(content);

                    }


                } else {
                    alert("图片删除失败!!");
                }
            });


        }

    }


    function upload() {
        var formData = new FormData($("#editFiles")[0]);
        $.ajax({
            url: '/tunnel/tunnelInfoTotal/uploadPhotos.do?className=figure',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                var originalPath = $("input[name='photosPicture']").val();
                $("input[name='photosPicture']").val(originalPath + data);
            },
            error: function () {
                alert("上传图片失败，请返回重新加载试试!!");
            }
        });
    }

</script>
