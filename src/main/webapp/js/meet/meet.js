/**
 * 视频会议
 * @type {{}}
 */
var Meet = {

};
/**
 * 初始化
 */
Meet.init = function (sammy) {
    $('#side-left').load('partials/meet/meet_side.html', function(){
        Index.activeSideMenu();
        Meet.router();
    });
};
/**
 * 路由控制
 */
Meet.router = function () {
    var hash = window.location.hash;
    switch(hash){
        //会议管理
        case '#/meet/meet_manage': MeetManage.init(); break;
        //模板管理
        case '#/meet/template_manage': TemplateManage.init(); break;
        //我的会议
        case '#/meet/mine_meet': MineMeet.init(); break;
        //会议室日历
        case '#/meet/meet_calendar': MeetCalendar.init(); break;
        //会议控制
        case '#/meet/meet_control': MeetControl.init(); break;
        //终端控制
        case '#/meet/device_control': DeviceControl.init(); break;
        //默认值：快速建会
        default: CreateMeet.init();
    }
};