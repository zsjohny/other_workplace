<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>隧道后台管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="/tunnel/assets/css/dpl-min.css" rel="stylesheet" type="text/css"/>
    <link href="/tunnel/assets/css/bui-min.css" rel="stylesheet" type="text/css"/>
    <link href="/tunnel/assets/css/main-min.css" rel="stylesheet" type="text/css"/>
</head>


<body>


<div class="header">

    <div class="dl-title">
        <!--<img src="/chinapost/Public/assets/img/top.png">-->
    </div>
    <div class="dl-log">欢迎您，<span class="dl-log-user">${name}</span><a
            href="/tunnel/login/quitUser.do"
            title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
</div>
<div class="content">
    <div class="dl-main-nav">
        <div class="dl-inform">
            <div class="dl-inform-title"><s class="dl-inform-icon dl-up"></s></div>
        </div>
        <ul id="J_Nav" class="nav-list ks-clear">
            <c:if test="${author==0||author==1}">
                <li class="nav-item dl-selected">
                    <div class="nav-item-inner nav-home">系统管理</div>
                </li>
            </c:if>
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-order">隧道管理</div>
            </li>

        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
</div>

<script type="text/javascript" src="/tunnel/assets/js/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="/tunnel/assets/js/bui-min.js"></script>
<script type="text/javascript" src="/tunnel/assets/js/common/main-min.js"></script>
<script type="text/javascript" src="/tunnel/assets/js/config-min.js"></script>
<script>
    var project = localStorage.getItem("project");
    var number = localStorage.getItem("number");

    BUI.use('common/main', function () {
        var config = [
            <c:if test="${author==0||author==1}">
            {
                id: 'menu',
                /* homePage: 'code',*/
                menu: [
                    {
                        id: '系统管理',
                        text: '系统管理',
                        items: [
                            <c:if test="${author==0}">
                            {id: '用户管理', text: '用户管理', href: '/tunnel/login/findUser.do?page=1'}
                            </c:if>
                            <c:if test="${author==0||author==1}">
                            ,
                            {id: '项目管理', text: '项目管理', href: '/tunnel/project/findProject.do?page=1'},
                            {id: '隧道管理', text: '隧道管理', href: '/tunnel/tunnelInfo/findTunnelInfo.do?page=1'}
                            </c:if>
                        ]
                    }

                ]
            },
            </c:if>
            {
                id: 'search',
                menu: [{
                    text: '隧道选择',
                    homePage: 'choice',
                    text: '隧道选择',
                    items: [
                        {
                            id: 'choice',
                            text: '隧道选择',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice'
                        }
                        <c:if test="${author==0||author==1}">
                        , {
                            id: 'upload',
                            text: 'Excel管理',
                            href: '/tunnel/login/againMenu.do?path=choice/upload'
                        }
                        </c:if>
                    ]
                }, {
                    text: '分表调查',
                    items: [{
                        id: 'tunnelImportPort',
                        text: '隧道进出口调查',
                        href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:tunnelImportPort'
                    },
                        {
                            id: 'tunnelShallowCover',
                            text: '隧道浅埋段调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:tunnelShallowCover'
                        }, {
                            id: 'assistTunnel',
                            text: '辅助通道调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:assistTunnel'
                        },
                        {
                            id: 'tunnelGrabageExamine',
                            text: '隧道弃渣场调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:tunnelGrabageExamine'
                        }, {
                            id: 'tunnelHeadRiskExamine',
                            text: '洞口失稳风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:tunnelHeadRiskExamine'
                        },

                        {
                            id: 'overBreakRiskExmaine',
                            text: '塌方风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:overBreakRiskExmaine'
                        },
                        {
                            id: 'surgeMudRiskExamine',
                            text: '突水涌泥风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:surgeMudRiskExamine'
                        },
                        {
                            id: 'shapeRiskExamine',
                            text: '大变形风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:shapeRiskExamine'
                        },
                        {
                            id: 'rockOutburstRiskExamine',
                            text: '岩爆风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:rockOutburstRiskExamine'
                        }, {
                            id: 'gasRiskExamine',
                            text: '瓦斯风险源现场调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:gasRiskExamine'
                        },
                        {
                            id: 'fireRiskExmaine',
                            text: '火灾风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:fireRiskExmaine'
                        },


                        {
                            id: 'trafficAccidentRiskExamine',
                            text: '交通事故风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:trafficAccidentRiskExamine'
                        },
                        {
                            id: 'envirRiskExamine',
                            text: '环境风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:envirRiskExamine'
                        }, {
                            id: 'otherRiskExamine',
                            text: '其他风险源调查',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:otherRiskExamine'
                        },
                        {
                            id: 'figure',
                            text: '附图',
                            href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=choice&params=1:1:figure'
                        }
                    ]

                },
                    {
                        text: '隧道详查',

                        items: [
                            {
                                id: '隧道查询',
                                text: '隧道查询',
                                href: '/tunnel/tunnelInfo/findAllProject.do?pageFirst=true&status=query'
                            }
                        ]
                    }


                ]
            }
        ];

        new PageUtil.MainPage({
            modulesConfig: config
        });
    })

</script>
<div style="text-align:center;">
</div>

</body>
</html>