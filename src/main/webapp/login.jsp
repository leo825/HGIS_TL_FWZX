<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<title>用户登录</title>
<link rel="stylesheet" href="${ctx}/libs/jquery-easyui-1.3.2/themes/default/easyui.css" />
<link rel="stylesheet" href="${ctx}/libs/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" href="${ctx}/css/login.css" />
</head>
<body>
	<div class="login-div">
		<table class="m-table">
			<tbody>
				<tr>
					<td colspan="4" align="center"><h1>服务总线后台管理系统</h1></td>
				</tr>
				<tr>
					<td class="td-account">账&nbsp;&nbsp;号：</td>
					<td><input class="account-input" type="text" name="account" tabIndex=1/>
					</td>
					<td rowspan="2"><button class="submit-input" type="button" onclick="Login.submit()"></button></td>
				</tr>
				<tr>
					<td class="td-password">密&nbsp;&nbsp;码：</td>
					<td><input class="password-input" type="password" name="password" tabIndex=2/></td>
				</tr>
				<tr>
					<td class="td-bottom">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</tbody>
		</table>
	</div>

<script src="${ctx}/libs/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script src="${ctx}/libs/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script src="${ctx}/libs/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script src="${ctx}/js/public.js"></script>
<script src="${ctx}/js/login/login.js"></script>
<script>
    	var URI = '${ctx}';
</script>
</body>
</html>