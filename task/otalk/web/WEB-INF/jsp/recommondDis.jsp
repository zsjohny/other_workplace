<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>管理控制中心</title>
    <style type="text/css">
        .searchDis {
            margin: 0 auto;
            width: 300px;
        }

        #upload {
            position: fixed;
            float: left;
            top: 214px;
            left: 867 pxpx;
            right: 650px;
            bottom: 200px;

        }
    </style>
    <script src="js/jQuery.js"></script>
    <script type="text/javascript">
        $(function () {


            $('input:radio[name="recommond"]').click(function () {

                $('#msg').html("");
            });


        });

        function cancel(e) {

            $.ajax({
                url: "recommond",
                data: "recommond=" + e + ",1&pageNum=" + $("#now").val().trim(),
                type: "post",
                success: function (data) {
                    $('#msg').html(data.msg);

                    location.href = "recommondDis?startCount="
                            + $("#now").val().trim();
                },
                error: function () {
                    $('#msg').html("网络不好刷新试试吧!");
                }


            })

        }


        function cancelAuthor(e) {

            if (confirm("此操作会取消认证,请谨慎操作!!")) {

                $.ajax({
                    url: "cancelAuthor",
                    data: "id=" + e + "&pageNum=" + $("#now").val().trim(),
                    type: "post",
                    success: function (data) {
                        $('#msg').html(data.msg);

                        setTimeout("skip()", 2000);

                    },
                    error: function () {
                        $('#msg').html("网络不好刷新试试吧!");
                    }


                })
            }


        }

        function skip() {
            location.href = "recommondDis?startCount="
                    + $("#now").val().trim();
        }

        function test() {

            var recommond = $('input:radio[name="recommond"]').is(":checked");
            if (recommond) {
                return true;
            }

            $('#msg').html("您还没选择推荐主播呢!!");
            return false;

        }

        window.onload = function () {


            var totalpage =${count};

            var nowpage = $("#now").val().trim();
            if (nowpage == 1) {

                $("#1").hide();
            }
            if (nowpage == totalpage) {

                $("#2").hide();
            }


            //初始化展示用户的部分信息
            if ("true" == $("#isQuery").val()) {

                $("#disTotal").hide();


            } else {
                $("#disSearch").hide();


            }


        }

        function page(e) {

            var page = $("#now").val().trim();

            if (!(/^\d+$/).test(page)) {

                page = 1;
            }

            switch (e) {
                case 1:
                    page = eval(page - 1);
                    break;
                case 2:
                    if (page >= $("#page").val()) {
                        page = 1;
                    }

                    break;
                case 3:
                    page = parseInt(page) + 1;

            }
            $("#now").val(page);

            if (page == 1) {
                $("#1").hide();
            } else {

                $("#1").show()

                if (page == $("#page").val()) {
                    $("#2").hide();
                } else {
                    $("#2").show();
                }

            }

            location.href = "recommondDis?startCount="
                    + page;
        }

        function query() {

            $("#startCount").val($("#now").val().trim());

            $("#form1").submit();
            return true;
        }


    </script>
</head>
<body>

<div id="disTotal">

    <form id="form1" action="queryDis" method="post">
        <div class="searchDis">
            <input type="text" name="word" required="true"
                   placeholder="请输入搜搜的关键词"/>
            <input type="hidden" name="startCount" id="startCount"/>
            <input type="button" value="搜索" onclick="return query()"/>
        </div>
    </form>
    <form name="f1" action="recommond" method="post"
          enctype="multipart/form-data">
        <table>
            <thead>
            <tr>
                <th>主播的头像</th>
                <th>主播的昵称</th>
                <th>主播手机号</th>
                <th>主播的标志</th>
                <th>是否推荐</th>
                <th><span style="color: red">取消认证</span></th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="list" items="${data}">
                <tr>
                    <td><img src="${list.url}" width="140px" height="140px"/></td>
                    <td>${list.name}</td>
                    <td>&nbsp;${list.phone}</td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;${list.author}</td>
                    <td><c:if test="${list.order==true}">
                        <span style="color: green">&nbsp;已经推荐</span>
                        <input onclick="cancel(${list.id})" type="button" value="取消">
                    </c:if> <c:if test="${list.order!=true}">
                        <input name="recommond" type="radio" value="${list.id},0"/>是</c:if></td>
                    <th><input type="button" onclick="cancelAuthor(${list.id})" value="确定取消"/></th>
                </tr>
            </c:forEach>
            <tr>
                <c:if test="${count!=0}">
                    <td colspan="2"></td>
                    <td><a id="1" href="javascript:page(1)">上一页</a></td>
                    <td>当前<input name="nowPage" type="text" size="1"
                                 value="${startPage} " id="now"/>页,<a href="javascript:page(2)">跳转</a>总共<input
                            readOnly type="text" size="1" value="${count}" id="page"/>页
                    </td>
                    <td><a id="2" href="javascript:page(3)">下一页</a></td>
                </c:if>
            </tr>
            </tbody>
</div>
<div id="upload">
    <input onclick="return test()" type="submit" value="确定推荐"/>
    </form>
</div>
<div style="margin: 0 auto; width: 300px;">
    <span id="msg" style="color: red; font-size: 20px">${msg}</span>
</div>
</table>
</form>
</div>


<!-- 展示搜索的页面 -->
<input type="hidden" id="isQuery" value="${isQuery}">

<div id="disSearch">
    <div class="searchDis">
        <a href="recommondDis">回到首页</a>
    </div>
    <form name="f1" action="recommond" method="post"
          enctype="multipart/form-data">
        <table>
            <thead>
            <tr>
                <th>主播的头像</th>
                <th>主播的昵称</th>
                <th>主播手机号</th>
                <th>主播的标志</th>
                <th>是否推荐</th>
                <th><span style="color: red">取消认证</span></th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="list" items="${data}">
                <tr>
                    <td><img src="${list.url}" width="200px" height="200px"/></td>
                    <td>${list.name}</td>
                    <td>&nbsp;${list.phone}</td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;${list.author}</td>
                    <td><c:if test="${list.order==true}">
                        <span style="color: green">&nbsp;已经推荐</span>
                        <input onclick="cancel(${list.id})" type="button" value="取消">
                    </c:if> <c:if test="${list.order!=true}">
                        <input name="recommond" type="radio" value="${list.id},0"/>是</c:if></td>
                    <th><input type="button" onclick="cancelAuthor(${list.id})" value="确定取消"/></th>
                </tr>
            </c:forEach>
            <tr>
                <c:if test="${count!=0}">
                    <td colspan="2"></td>
                    <td><a id="1" href="javascript:page(1)">上一页</a></td>
                    <td>当前<input name="nowPage" type="text" size="1"
                                 value="${startPage} " id="now"/>页,<a href="javascript:page(2)">跳转</a>总共<input
                            readOnly type="text" size="1" value="${count}" id="page"/>页
                    </td>
                    <td><a id="2" href="javascript:page(3)">下一页</a></td>
                </c:if>
            </tr>
            </tbody>
</div>
<div id="upload">
    <input onclick="return test()" type="submit" value="确定推荐"/>
    </form>
</div>
<div style="margin: 0 auto; width: 300px;">
    <span id="msg" style="color: red; font-size: 20px">${msg}</span>
</div>
</table>
</form>
</div>


</body>
</html>