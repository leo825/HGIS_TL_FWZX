<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%--<!DOCTYPE html>--%>
<html lang="en">
<head>

    <!-- start: Meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>服务总线管理系统</title>
    <!-- end: Meta -->

    <!-- ionicon -->
    <%--<link rel="stylesheet" href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css">--%>
    <link rel="stylesheet" href="${ctx}/libs/ionicons/2.0.1/css/ionicons.min.css"/>
    <!-- start: CSS -->
    <%--<link rel="stylesheet" href="${ctx}/libs/jquery-easyui-1.2.6/themes/default/easyui.css"/>--%>
    <%--<link rel="stylesheet" href="${ctx}/libs/jquery-easyui-1.2.6/themes/icon.css"/>--%>
    <link rel="stylesheet" href="${ctx}/libs/jquery-easyui-1.3.2/themes/default/easyui.css"/>
    <link rel="stylesheet" href="${ctx}/libs/jquery-easyui-1.3.2/themes/icon.css"/>
    <link rel="stylesheet" href="${ctx}/libs/colpick-2.0.2/css/colpick.css"/>
<!--     <link rel="stylesheet" href="${ctx}/libs/fullcalendar-2.6.1/lib/cupertino/jquery-ui.min.css"/> -->
<!--     <link rel="stylesheet" href="${ctx}/libs/fullcalendar-2.6.1/fullcalendar.min.css"/> -->
<!--     <link rel="stylesheet" href="${ctx}/libs/fullcalendar-2.6.1/fullcalendar.print.css"/> -->

    
    <link rel="stylesheet" href="${ctx}/css/common-init.css"/>
    <link rel="stylesheet" href="${ctx}/css/myicons.css"/>
    <link rel="stylesheet" href="${ctx}/css/index.css"/>
    <link rel="stylesheet" href="${ctx}/css/monitor.css"/>
    <link rel="stylesheet" href="${ctx}/css/service.css"/>
    <link rel="stylesheet" href="${ctx}/css/usage.css"/>
    <link rel="stylesheet" href="${ctx}/css/system.css"/>
    <link rel="stylesheet" href="${ctx}/css/config.css"/>
    <link rel="stylesheet" href="${ctx}/css/choose-user.css"/>
    <!-- end: CSS -->

</head>

<body>
<div class="content">
    <div class="easyui-layout" id="main">
        <div region="north" border="false" id="layout-notrh">

            <div class="header">
                <div class="header-left"></div>
                <div class="header-menu">
                    <ul>
                        <li class="active" id="service" style="display:none;">
                            <a href="#/service">服务管理</a>
                        </li>
                        <li id="monitor" style="display:none;">
                            <a href="#/monitor">运行监控</a>
                        </li>
                        <li  id="usage" style="display:none;">
                            <a href="#/usage">调用管理</a>
                        </li>
                        <li id="system" style="display:none;">
                            <a href="#/system">系统管理</a>
                        </li>
                        <li id="config" style="display:none;">
                            <a href="#/config">配置管理</a>
                        </li>
                        <!--<li>-->
                        <!--<a href="#/product">产品发布</a>-->
                        <!--</li>-->
                    </ul>
                </div>
                <div class="header-right">
                    <div class="user-info">
                        <span>用户</span>，您好
                        <a href="javascript:void(0)" class="js-logout" onclick="Login.logout();">退出</a>
                    </div>
                </div>
            </div>
        </div>
        <div region="west" expand="true" title="服务管理" id="side-left">

        </div>
        <div id="center" region="center" border="false">
            <div class="navigation">
                当前位置：<span class="nav">首页</span>
            </div>
            <div id="main-view">

            </div>
        </div>
    </div>
</div>
<!-- /main -->
<div class="footer">

</div>
<!-- /footer -->

<!-- start: JavaScript-->
<!--[if lt IE 5]>
<script src="${ctx}/libs/html5.js"></script>
<![endif]-->

<%--<script src="${ctx}/libs/jquery-easyui-1.2.6/jquery-1.7.2.min.js"></script>--%>
<%--<script src="${ctx}/libs/jquery-easyui-1.2.6/jquery.easyui.min.js"></script>--%>
<%--<script src="${ctx}/libs/jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>--%>
<%--<script src="${ctx}/libs/jquery-easyui-1.2.6/datagrid-dnd.js"></script>--%>
<script src="${ctx}/libs/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script src="${ctx}/libs/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script src="${ctx}/libs/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script src="${ctx}/libs/datagrid-dnd.js"></script>
<script src="${ctx}/libs/treegrid-dnd.js"></script>
<script src="${ctx}/libs/sammy.js"></script>
<script src="${ctx}/libs/jquery.form.min.js"></script>
<script src="${ctx}/libs/colpick-2.0.2/js/colpick.js"></script>
<script src="${ctx}/libs/fullcalendar-2.6.1/lib/moment.min.js"></script>
<script src="${ctx}/libs/fullcalendar-2.6.1/fullcalendar.min.js"></script>
<script src="${ctx}/libs/fullcalendar-2.6.1/lang-all.js"></script>
<script src="${ctx}/libs/Highcharts-4.2.4/js/highcharts.js"></script>
<script src="${ctx}/libs/Highcharts-4.2.4/js/highcharts-3d.js"></script>
<script src="${ctx}/libs/Highcharts-4.2.4/js/modules/exporting.js"></script>
<script src="${ctx}/libs/laydate/laydate.js"></script>

<script src="${ctx}/js/public.js"></script>
<script src="${ctx}/js/index.js"></script>
<script src="${ctx}/js/router.js"></script>
<script src="${ctx}/js/component/org_device_tree.js"></script>
<script src="${ctx}/js/component/org_boardroom_tree.js"></script>
<script src="${ctx}/js/component/choose-user.js"></script>
<script src="${ctx}/js/service/service.js"></script>
<script src="${ctx}/js/service/project_manage.js"></script>
<script src="${ctx}/js/service/server_manage.js"></script>
<script src="${ctx}/js/usage/usage.js"></script>
<script src="${ctx}/js/usage/usage_manage.js"></script>
<script src="${ctx}/js/usage/usage_project_manage.js"></script>
<script src="${ctx}/js/system/system.js"></script>
<script src="${ctx}/js/system/operation_report.js"></script>
<script src="${ctx}/js/monitor/monitor.js"></script>
<script src="${ctx}/js/monitor/providers_statistics.js"></script>
<script src="${ctx}/js/monitor/usages_statistics.js"></script>
<script src="${ctx}/js/monitor/realtime_monitoring.js"></script>
<script src="${ctx}/js/monitor/service_tracking.js"></script>
<script src="${ctx}/js/login/login.js"></script>
<script src="${ctx}/js/upload/ajaxfileupload.js"></script>
<script src="${ctx}/js/config/config.js"></script>
<script src="${ctx}/js/config/permission_manage.js"></script>
<script src="${ctx}/js/config/user_manage.js"></script>

<!-- end: JavaScript-->



<script>
    var URI = '${ctx}';
    var RESOURCES=[];//记录资源
    $(function(){
        //初始化系统
        Index.init();
        <%
			String[] resources = null;
			String nickname = null;
			String id = null;
			try{
				id = request.getParameter("id");
		 		nickname = request.getParameter("nickname");
		 		resources =  request.getParameterValues("resources");//获取资源
        		for(int i = 0; i < resources.length; i++){
        %>		
        		RESOURCES.push("<%=resources[i]%>");
       	<%
        		}

			}catch(Exception e){
//		 		RequestDispatcher rd=request.getRequestDispatcher("/login.jsp");
//		 		rd.forward(request, response);
			}
		%>
		console.log(RESOURCES);
		
	    if (Public.isInArray("system", RESOURCES)) {
			$("#system").show();
		}
		if (Public.isInArray("usage", RESOURCES)) {
			$("#usage").show();
		}
		if (Public.isInArray("config", RESOURCES)) {
			$("#config").show();
		}
		if (Public.isInArray("monitor", RESOURCES)) {
			$("#monitor").show();
		}
		if (Public.isInArray("service", RESOURCES)) {
			$("#service").show();
		}

		$(".user-info").find("span:first-child").html("<%=nickname%>");
        $(".user-info").find("span:first-child").attr("id","<%=id%>");

    });
</script>



</body>
</html>