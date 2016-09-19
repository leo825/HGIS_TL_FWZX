/**
 * 服务管理
 * @type {{}}
 */
var Service = {

};
/**
 * 初始化
 */
Service.init = function (sammy) {
    $('#side-left').load('partials/service/service_side.html', function(){
        Index.activeSideMenu();
        Service.router();
    });
};
/**
 * 路由控制
 */
Service.router = function () {
    var hash = window.location.hash;
    switch(hash){
        //服务管理
        case '#/service/server_manage': ServerManage.init(); break;
        //默认应用管理
        default: ProjectManage.init();
    }
};