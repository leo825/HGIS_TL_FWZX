/**
 * 系统管理
 * @type {{}}
 */
var System = {

};
/**
 * 初始化
 */
System.init = function () {
    $('#side-left').load('partials/system/system_side.html', function(){
        Index.activeSideMenu();
        System.router();
    });
};
/**
 * 路由控制
 */
System.router = function () {
    var hash = window.location.hash;
    switch(hash){
        //用户管理
        //case '#/system/user_manage': UserManage.init(); break;
        //默认值：运行报告
        default: OperationReport.init();
    }
};
