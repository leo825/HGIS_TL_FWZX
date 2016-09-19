/**
 * 运行监控
 * @type {{}}
 */
var Monitor = {

};
/**
 * 初始化
 */
Monitor.init = function (sammy) {
    $('#side-left').load('partials/monitor/monitor_side.html', function(){
        Index.activeSideMenu();
        Monitor.router();
    });
};
/**
 * 路由控制
 */
Monitor.router = function () {
    var hash = window.location.hash;
    switch(hash){
        //使用统计
        case '#/monitor/usages_statistics': UsagesStatistics.init(); break;
        //实时监控
        case '#/monitor/realtime_monitoring': RealtimeMonitoring.init(); break;
        //服务跟踪
        case '#/monitor/service_tracking': ServiceTracking.init(); break;
        //默认提供方统计
        default: ProvidersStatistics.init();
    }
};