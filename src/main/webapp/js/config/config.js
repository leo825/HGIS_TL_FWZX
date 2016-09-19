/**
 * 配置管理
 * 
 * @type {{}}
 */
var Config = {

};
/**
 * 初始化
 */
Config.init = function() {
	$('#side-left').load('partials/config/config_side.html', function() {
		Index.activeSideMenu();
		Config.router();
	});
};
/**
 * 路由控制
 */
Config.router = function() {
	var hash = window.location.hash;
	switch (hash) {
	//权限管理
	case '#/config/permission':PermissionManage.init();break;
	//默认人员管理
	default: UserManage.init();
	}
};
