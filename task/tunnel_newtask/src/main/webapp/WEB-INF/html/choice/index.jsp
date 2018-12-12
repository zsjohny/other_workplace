<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>隧道选择</title>
    <base target="_blank"/>

    <style>
        .baseFloat {
            float: left;
        }

        .onclickExmaineProject {

        }

        .onclickExmaineNumber {

        }

    </style>
    <!--必要样式-->
    <link href="/tunnel/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/tunnel/css/city-picker.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/tunnel/js/jquery.js"></script>
    <script type="text/javascript" src="/tunnel/js/bootstrap.min.js"></script>
    <script>


        var project = localStorage.getItem("project");
        var number = localStorage.getItem("number");


        //重置
        function examineReset() {
            $("ul a").find("li").each(function () {
                $(this).removeClass("active");
            });

            //重置提交的内容
            localStorage.clear();
        }


        //填写相应表格内容
        function addTunnelInfoExmaine() {
            if (project == undefined || project == '' || project == null) {
                $("#exmaineNumber").html("<li class='list-group-item '>还没选中项目编号!!</li>");
                return;
            }

            if (number == undefined || number == '' || number == null) {
                $("#exmaineNumber").html("<li class='list-group-item '>还没选中隧道编号!!</li>");
                return;
            }
            //获取存储的内容
            localStorage.setItem("project", project);
            localStorage.setItem("number", number);

            if ($("#params").val() == '') {

                /*  $(".modal-body").html("<span style='color: red'>您已经选择好了项目,请选择相应表格!!</span>")
                 $("#showModel").click();

                 $("#GO").attr("data-dismiss", "modal");*/
                return;

            }
            var params = project + ":" + number + ":" + $("#params").val();

            location.href = "/tunnel/login/againMenu.do?path=tunnelInfoTotal/" + $("#tunnelInfo").val() + "Index" + "&params=" + params;
        }


        //默认选择触发事件
        $(function () {



            //判断是否从表格调查页面
            if ($("#params").val() == '1') {
                if (project == undefined || project == '' || number == undefined || number == '') {
                    $("#showModel").click();

                    //添加相应绑定事件
                    $("#GO").click(function () {
                        //显示内容tunnelInfo
                        location.href = '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=' + "2:2:" + $("#tunnelInfo").val().trim();
                    });
                    return;
                }

                location.href = '/tunnel/tunnelInfoTotal/findTunnelInfoTotal.do?pageName=false&className=' + $("#tunnelInfo").val() + '&tunnelNumber=' + number + '&params=' + project + ":" + number + ':1'
                return;
            }


            //显示内容
            $(".container").show();

            //添加项目触发事件
            $(".onclickExmaineProject").click(function () {

                var projectNumber = $(this).find("li").html().trim();
                //去除上面所有的class
                $(this).parent().find("a>li").each(function () {
                    $(this).removeClass("active");
                });
                //添加新的class
                $(this).find("li").addClass("active");
                /**
                 * 查找相应的隧道编号
                 */
                $.post("/tunnel/tunnelInfo/findTunnelInfoByJson.do",
                        "projectNumber=" + projectNumber, function (data) {

                            if (data.flag) {

                                var content = "";
                                var arr = data.list;
                                for (var i = 0; i < arr.length; i++) {
                                    content += "<a  calss='onclickExmaineNumber'><li class='list-group-item '> " + arr[i] + "</li></a> "

                                }
                                $("#exmaineNumber").html(content);

                                //给默认添加的存储
                                project = projectNumber;

                            } else {
                                $("#exmaineNumber").html("<li class='list-group-item '> 没有隧道编号</li>");

                                //重置提交的内容
                                project = '';
                                number = '';
                            }
                        })
            })


            //添加隧道编号触发事件
            $("#exmaineNumber").on('click', 'a', function () {

                var tunnelNumber = $(this).text().trim();

                //去除上面所有的class
                $(this).parent().find("a>li").each(function () {
                    $(this).removeClass("active");
                });
                //添加新的class
                $(this).find("li").addClass("active");

                //添加默认的信息
                number = tunnelNumber;

            })


            //默认触发
            if (project != '') {


                //添加默认的项目编号
                $("#exminePoject").find("a>li").each(function () {
                    if ($(this).text().trim() == project) {
                        $(this).addClass("active");
                    }
                })

                $.post("/tunnel/tunnelInfo/findTunnelInfoByJson.do",
                        "projectNumber=" + project, function (data) {
                            if (data.flag) {

                                var content = "";
                                var arr = data.list;

                                if (arr.length == 0) {
                                    $("#exmaineNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                                    return;
                                }

                                for (var i = 0; i < arr.length; i++) {

                                    if (arr[i] == number) {
                                        content += "<a  calss='onclickExmaineNumber'><li class='list-group-item active '> " + arr[i] + "</li></a>";

                                    } else {
                                        content += "<a  calss='onclickExmaineNumber'> <li class='list-group-item '> " + arr[i] + "</li></a>";
                                    }

                                }

                                $("#exmaineNumber").html(content);

                            } else {
                                $("#exmaineNumber").html("<li class='list-group-item '> 没有隧道编号</li>");
                            }

                        })

            }

        })


    </script>


</head>

<body>
<input type="hidden" id="tunnelInfo" value="${tunnelInfo}"/>
<input type="hidden" id="params" value="${tunnelProject}"/>
<!-- Content -->
<div class="container" style="display: none;">

    <h2 class="page-header">请依次选择:<span style="color: red">项目编号-隧道编号</span></h2>

    <nav class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand divider">请选择</a>
            </div>
            <div>
                <li class="dropdown">
                    <ul class=" nav navbar-nav">
                        <ul class="list-group baseFloat" id="exminePoject">
                            <c:if test="${!empty list}">
                                <c:forEach items="${list}" var="list" varStatus="status">
                                    <a class="onclickExmaineProject">
                                        <li class="list-group-item">
                                                ${list.projectNumber}
                                        </li>
                                    </a>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty list}">
                                <li class="list-group-item">没有项目编号!!</li>
                            </c:if>
                        </ul>
                        </li>
                        <li class="dropdown">
                            <ul class="list-group baseFloat" id="exmaineNumber">
                                <li class="list-group-item">隧道编号</li>
                            </ul>
                        </li>
                    </ul>
            </div>
            <div class="form-group">
                <button class="btn btn-warning" onclick="examineReset()" type="button">重置</button>
                <button class="btn btn-danger" onclick="addTunnelInfoExmaine()" type="button">选择</button>
            </div>
        </div>
    </nav>
    <article>

        <header>
            <mark>
                填 写 说 明
            </mark>
        </header>

        <footer>
                <p>
                    1.隧道现场调查应结合隧址区地质水文条件、埋深条件、周边建设环境，重点调查洞口条件、浅埋段以及风险源。
                </p>
                <p>
                    2.桥涵专业负责气象调查资料收集，隧道专业核实后填写。
                </p>
                <p>
                    3.地形地貌调查内容：洞口或浅埋段的自然坡度、是否存在偏压。
                </p>
                <p>
                    4.植被条件主要调查内容：植被发育条件、类型以及对隧道的影响。
                </p>
                <p>
                    5.水文条件主要调查内容：洞口或浅埋段地表水、地下水发育情况，特别是地表是否存在冲沟以及其对隧道的影响。
                </p>
                <p>

                    6.地质条件主要调查内容：地层岩性、地质构造、不良地质等等，主要由地质专业收集提供，隧道专业负责填写。
                </p>
                <p>

                    7.建（构）筑物主要调查内容：建（构）筑物与隧道关系以及隧道施工对其影响、建议处理措施（拆迁、加固）等。
                </p>
                <p>

                    8.施工场地主要调查内容：是否具备施工场地条件，施工场地布置建议。
                </p>
                <p>

                    9.施工便道主要调查内容：新建或利用既有道路长度，构筑物利用或新建情况。
                </p>
                <p>

                    10.弃渣场主要调查内容：弃渣位置、运距、容渣量、占地面积及类型、环保水保措施。

                </p>
                <p>
                    11.环保水保应注意加强隧道施工对周边环境的影响，以及周边环境的特殊要求。
                </p>
                <p>

                    12.隧道风险源调查应根据相关基础资料及工程经验，提出风险源发生概率等级，并对风险源进行定性或定量的描述。
                </p>
                <p>

                    13.隧道调查应结合相关专业意见，现场调查后如实填写，杜绝造假。
                </p>
        </footer>

    </article>
</div>

<!--tips-->
<button class="btn btn-primary btn-lg" style="display: none;" data-toggle="modal" id="showModel"
        data-target="#myModal"></button>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <%--  <h4 class="modal-title" id="myModalLabel">
                      模态框（Modal）标题
                  </h4>--%>
            </div>
            <div class="modal-body">
                还没有选择项目和选择隧道信息，是否去选择?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">NO</button>
                <button type="button" id="GO" class="btn btn-primary"> GO</button>
            </div>
        </div>
    </div>
</div>

</body>

</html>