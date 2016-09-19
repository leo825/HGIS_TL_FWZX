<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传测试</title>
</head>
<body>

<!-- <form method="post" enctype="multipart/form-data"> -->
<!-- 	<input type="file" name="fileToUpload" id="fileToUpload"/> -->
<!-- 	<input type="button" onclick="upload()" value="上传"/> -->
<!-- </form> -->

<input type="file" name="fileToUpload" id="fileToUpload"/>
<input type="button" onclick="upload()" value="上传"/>
<script src="${ctx}/libs/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script src="${ctx}/js/upload/ajaxfileupload.js"></script>
<script>
    var URI = '${ctx}';
    function upload(){
	var url = URI+'/rest/project/add_file';
	var obj = {};
	obj.id = "1234";
	console.log(url);
	$.ajaxFileUpload({
			url : url, //用于文件上传的服务器端请求地址
			secureuri : false, //是否需要安全协议，一般设置为false
			data:obj,
			fileElementId : 'fileToUpload', //文件上传域的ID
			dataType : 'json', //返回值类型 一般设置为json
			success : function(data) //服务器成功响应处理函数
			{
				alert("成功");
			}
		});
	}
</script>
</body>
</html>