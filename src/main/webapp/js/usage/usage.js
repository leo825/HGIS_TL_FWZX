/**
 * 服务管理
 * @type {{}}
 */
var Usage = {

};
/**
 * 初始化
 */
Usage.init = function (sammy) {
    $('#side-left').load('partials/usage/usage_side.html', function(){
        Index.activeSideMenu();
        Usage.router();
    });
};
/**
 * 路由控制
 */
Usage.router = function () {
    var hash = window.location.hash;
    switch(hash){
    	//调用者应用管理
        case '#/usage/usage_project_manage': UsageProjectManage.init(); break;
    	//默认调用方管理
        default: UsageManage.init();
    }
};