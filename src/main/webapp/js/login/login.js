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
					self.location.reload();

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

	var reqUrl = URI + '/rest/logOut';
	$.ajax({
		type : 'GET',
		async : false,
		url : reqUrl,
		contentType : 'application/json',
		success : function(resp) {
			if (resp != null && resp.status == 'success') {
				self.location.reload();
			} else {
				var errMsg;
				var msg = errMsg != null ? errMsg : '注销失败了';
				Public.alert(msg);
				Public.debug('获取失败：' + reqUrl);
			}
		}
	});
};

document.onkeydown = function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if (e && e.keyCode == 13) { // enter 键
		Login.submit();
	}
};
