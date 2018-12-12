<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>项目管理</title>
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
<form class="form-inline definewidth m20" action="/tunnel/project/findProject.do" method="post">
    项目编号：
    <input type="text" name="projectNumber" id="findName" class="abc input-default" value="${projectNumber}">&nbsp;&nbsp;
    <input type="hidden" name="page" value="${page}"/>
    <button type="submit" class="btn btn-primary">查询</button>
    &nbsp;&nbsp;
    </c:if>
    <c:if test="${authorLevel !=2}">
        <button type="button" class="btn btn-success" id="addProjectnew">新增项目</button>
    </c:if>
    <c:if test="${!empty total && total !=0}">
        <button type="button" class="btn btn-info" type="button">
            <a href="/tunnel/login/downloadExcel.do?downloadType=0&className=project&excelName=项目管理">本页下载</a>
        </button>
        <button type="button" class="btn btn-warning" type="button">
            <a href="/tunnel/login/downloadExcel.do?downloadType=1&className=project&tunnelNumber=test:test:test&excelName=项目管理全部">全部下载</a>
        </button>
    </c:if>
    <c:if test="${total==1}">


        <form method="post" class="definewidth m20">

            <input type="hidden" name="projectId" value="${project.id}"/>
            <table class="table table-bordered table-hover definewidth m10">
                <tr>
                    <td width="10%" class="tableleft">项目编号</td>
                    <td><input type="text" name="projectNumber" value="${project.projectNumber}" onfocus="resetTips()"/>
                    </td>
                </tr>
                <tr>
                    <td width="10%" class="tableleft">项目名称</td>
                    <td><input type="text" name="projectName" value="${project.projectName}" onfocus="resetTips()"/>
                    </td>
                </tr>
                <tr>
                    <td width="10%" class="tableleft">调查人员</td>
                    <td><input type="text" name="examinePersion" value="${project.examinePersion}"
                               onfocus="resetTips()"/></td>
                </tr>
                <tr>
                    <td width="10%" class="tableleft">联系方式</td>
                    <td><input type="text" name="contactInfo" value="${project.contactInfo}" maxlength="11"
                               onfocus="resetTips()"/></td>
                </tr>
                <tr>
                    <td width="10%" class="tableleft">调查日期</td>
                    <td><input type="date" name="examineDate" value="${project.examineDate}" onfocus="resetTips()"/>

                    </td>
                </tr>
                <tr>
                    <td class="tableleft"></td>
                    <td>
                        <button type="button" class="btn btn-primary" id="userModifySave">修改</button>
                        <button type="button" class="btn btn-danger" type="button"><a
                                href="javascript:del(${project.id})">删除</a></button>

                    </td>
                </tr>
            </table>
        </form>

        <script>

            //恢复提示
            function resetTips() {
                $("input[name='projectNumber']").attr("placeholder", "");
                $("input[name='projectName']").attr("placeholder", "");
                $("input[name='examinePersion']").attr("placeholder", "");
                $("input[name='contactInfo']").attr("placeholder", "");
                $("input[name='examineDate']").attr("placeholder", "");
            }
            $('#userModifySave').click(function () {

                var id = $("input[name='projectId']").val();
                var projectNumber = $("input[name='projectNumber']").val();
                var projectName = $("input[name='projectName']").val();
                var examinePersion = $("input[name='examinePersion']").val();
                var contactInfo = $("input[name='contactInfo']").val();
                var examineDate = $("input[name='examineDate']").val();


                //检验
                if (projectNumber == "") {
                    $("input[name='projectNumber']").val("");
                    $("input[name='projectNumber']").attr("placeholder", "项目编号不得为空!!");
                    return;
                }

                if (projectName == "") {
                    $("input[name='projectName']").val("");
                    $("input[name='projectName']").attr("placeholder", "项目名称不得为空!!");
                    return;
                }

                if (examinePersion == "") {
                    $("input[name='examinePersion']").val("");
                    $("input[name='examinePersion']").attr("placeholder", "调查人员不得为空!!");
                    return;
                }

                if (contactInfo == "") {
                    $("input[name='contactInfo']").val("");
                    $("input[name='contactInfo']").attr("placeholder", "联系方式不得为空!!");
                    return;
                }


                if (projectNumber.length < 3) {
                    $("input[name='projectNumber']").val("");
                    $("input[name='projectNumber']").attr("placeholder", "项目编号不得少于3位!!");
                    return;
                }

                if (projectName.length < 3) {
                    $("input[name='projectName']").val("");
                    $("input[name='projectName']").attr("placeholder", "项目名称不得少于3位!!");
                    return;
                }

                if (examinePersion.length < 2) {
                    $("input[name='examinePersion']").val("");
                    $("input[name='examinePersion']").attr("placeholder", "调查人员不得少于2位!!");
                    return;
                }

                if (contactInfo.length < 11) {
                    $("input[name='contactInfo']").val("");
                    $("input[name='contactInfo']").attr("placeholder", "联系方式不得少于11位!!");
                    return;
                }

                if (!(/^[0-9]+$/.test(contactInfo))) {
                    $("input[name='contactInfo']").val("");
                    $("input[name='contactInfo']").attr("placeholder", "联系方式必须为纯数字!!");
                    return;
                }

                $.post("/tunnel/project/modifyProject.do",
                        "id=" + id + "&projectNumber=" + projectNumber + "&projectName=" + projectName + "&examinePersion=" +
                        examinePersion + "&contactInfo=" + contactInfo + "&examineDate=" + examineDate, function (data) {
                            data = parseInt(data);

                            if (data == 0) {
                                projectNumber = "";
                                if (${!empty projectNumber}) {
                                    projectNumber = $("#projectEditFindUser").val();
                                }

                                location.href = "/tunnel/project/findProject.do?page=1&projectNumber=" + projectNumber;

                            } else if (data == 1) {
                                $("input[name='projectNumber']").val("");

                                $("input[name='projectNumber']").attr("placeholder", "修改失败,错误原因联系技术!!");

                            } else if (data == 2) {
                                var projectNumber = $("input[name='projectNumber']").val();

                                $("input[name='projectNumber']").val("");

                                $("input[name='projectNumber']").attr("placeholder", "项目编号:" + projectNumber + ",已经存在!!");
                            }
                        })

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
        <th>项目id</th>
        <th>项目编号</th>
        <th>项目名称</th>
        <th>调查人员</th>
        <th>联系方式</th>
        <th>调查日期</th>
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
                <td>${list.projectNumber}</td>
                <td>${list.projectName}</td>
                <td>${list.examinePersion}</td>
                <td>${list.contactInfo}</td>
                <td>${list.examineDate}</td>
                <c:if test="${authorLevel !=2}">
                    <td>
                        <a href="/tunnel/project/findProject.do?id=${list.id}&projectNumber=${projectNumber}&page=${page}">编辑</a>

                        <a href="javascript:del(${list.id})">删除</a>

                    </td>
                </c:if>
            </tr>
            <c:if test="${status.last}">
                <tr>
                    <c:if test="${authorLevel ==2}">
                    <td colspan='7'>
                        </c:if>
                        <c:if test="${authorLevel ==0 || authorLevel ==1}">
                    <td colspan='8'>
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

        if (${!empty projectNumber}) {
            page = page + "&projectNumber=" + $("#findName").val();

        }

        location.href = "/tunnel/project/findProject.do?page=" + page;

    }

    $(function () {


        $('#addProjectnew').click(function () {

            window.location.href = "/tunnel/login/againMenu.do?path=project/add&params=" + $("#page").val();
        });


    });

    function del(id) {


        if (confirm("确定要删除吗？")) {
            var projectNumber = "";
            if (${!empty projectNumber}) {
                name = $("#findName").val();

            }

            var page = $("input[name='page']").val();
            window.location.href = "/tunnel/project/removerProject.do?id=" + id + "&page=" + page + "&projectNumber=" + projectNumber;

        }


    }
</script>