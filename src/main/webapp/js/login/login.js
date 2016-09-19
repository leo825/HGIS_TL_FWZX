/**
 * 登陆验证
 * @type {{}}
 */
var Login = {
		
};

/**
 * 登陆验证
 * 
 * */
Login.submit = function() {
	var user = Login.checkLoginForm();
	if (user != null) {
		var reqUrl = URI + '/rest/login?account=' + user.account + '&password=' + user.password;
		$.ajax({
			type : 'GET',
			async : false,
			url : reqUrl,
			contentType : 'application/json',
			success : function(resp) {
				if (resp != null && resp.status == 'success') {
					Login.redirect(resp.data);
				} else {
					var errMsg;
					var msg = errMsg != null ? errMsg : '用户名或者密码不正确';
					Public.alert(msg);
					Public.debug('获取失败：' + reqUrl);
				}
			}
		});
	}

};

Login.redirect = function(user) {
	var userId = user.id;
	if (!Public.isNull(userId)) {
		var reqUrl = URI + '/rest/login/resource/' + userId;
		$.ajax({
					type : 'GET',
					async : false,
					url : reqUrl,
					contentType : 'application/json',
					success : function(resp) {
						if (resp != null && resp.status == 'success') {
							var resources = resp.data;
							//根据角色来获取权
							document.write("<form action='index.jsp' method='post' name='form1' style='display:none'>");
							document.write("<input type='text' name='id' value='"+ user.id + "'</input>");
							document.write("<input type='text' name='nickname' value='"+ user.nickname + "'</input>");
							for ( var i = 0; i < resources.length; i++) {
								document.write("<input type='text' name='resources' value='"+ resources[i].name + "'</input>");
							}
							document.write("</form>");
							document.form1.submit();
						} else {
							var errMsg;
							var msg = errMsg != null ? errMsg : '用户名或者密码不正确';
							Public.alert(msg);
							Public.debug('获取资源权限失败：' + reqUrl);
						}
					}
				});
	}

};

/**
 * 验证登陆参数
 * 
 */
Login.checkLoginForm = function() {
	var account = $('.account-input').val();
	var password = $('.password-input').val();

	if (Public.isNull(account)) {
		Public.alert('用户名不能为空');
		return null;
	} else if (Public.isNull(password)) {
		Public.alert("密码不能为空");
		return null;
	}

	var obj = {};
	obj.account = account;
	obj.password = password;
	return obj;
};

Login.logout = function() {

	window.location.href = "login.jsp";
};

document.onkeydown = function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if (e && e.keyCode == 13) { // enter 键
		Login.submit();
	}
};
