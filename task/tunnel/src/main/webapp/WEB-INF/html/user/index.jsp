<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色管理</title>
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
<form class="form-inline definewidth m20" action="/tunnel/login/findUser.do" method="post">
    用户名称：
    <input type="text" name="name" id="findName" class="abc input-default" value="${name}">&nbsp;&nbsp;
    <button type="submit" class="btn btn-primary">查询</button>
    <input type="hidden" name="page" value="${page}"/>
    &nbsp;&nbsp;
    <button type="button" class="btn btn-success" id="addnew">新增用户</button>
</form>


<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
        <th>用户id</th>
        <th>用户名称</th>
        <th>真实姓名</th>
        <th>用户状态</th>
        <th>用户角色</th>
        <th>最后登录时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%--
    <if test="${empty list}">
        <tr> 没有数据</tr>
    </if>--%>
    <if test="${!empty list}">

        <c:forEach items="${list}" var="list" varStatus="status">
            <tr>
                <td>${list.id}</td>
                <td>${list.name}</td>
                <td>${list.realName}</td>
                <td>
                    <c:if test="${list.status==true}">
                        启用
                    </c:if>
                    <c:if test="${list.status==false}">
                        禁用
                    </c:if>
                </td>
                <td>
                    <c:if test="${list.authorLevel==0}">
                        超级用户
                    </c:if>
                    <c:if test="${list.authorLevel==1}">
                        高级用户
                    </c:if>
                    <c:if test="${list.authorLevel==2}">
                        普通用户
                    </c:if>

                </td>
                <td>${list.loadTime}</td>
                <td>
                    <a href="/tunnel/login/findUser.do?id=${list.id}&name=${name}">编辑</a>
                    <c:if test="${list.name!='admin'}">
                        <a href="javascript:del(${list.id})">删除</a>
                    </c:if>
                </td>
            </tr>
            <c:if test="${status.last}">
                <tr>
                    <td colspan='7'>
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

        if (${!empty name}) {
            page = page + "&name=" + $("#findName").val();

        }

        location.href = "/tunnel/login/findUser.do?page=" + page;

    }

    $(function () {


        $('#addnew').click(function () {

            window.location.href = "/tunnel/login/againMenu.do?path=user/add";
        });


    });

    function del(id) {


        if (confirm("确定要删除吗？")) {
            var name = "";
            if (${!empty name}) {
                name = $("#findName").val();

            }

            var page = $("input[name='page']").val();
            window.location.href = "/tunnel/login/removerUser.do?id=" + id + "&page=" + page + "&name=" + name;

        }


    }
</script>