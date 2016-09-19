/**
 * 会议室日历
 * @type {{}}
 */
var MeetCalendar = {

};
/**
 * 初始化
 */
MeetCalendar.init = function () {
    $('#main-view').load('partials/meet/boardroom_calendar.html', function(){
        MeetCalendar.render();
    });
};
/**
 * 渲染页面
 */
MeetCalendar.render = function () {
    MeetCalendar.renderCalendar();
};
/**
 * 渲染日历
 */
MeetCalendar.renderCalendar = function () {
    var url = URI + '/rest/conference/calendar';
    $('#calendar').fullCalendar({
        //theme: true,
        header: {
            left: 'prev today',
            center: 'title',
            right: 'next'
        },
        lang: 'zh-cn',
        buttonIcons: true,
        weekNumbers: false,
        editable: false,
        eventLimit: true,
        events: url,
        dayClick: function (date, jsEvent, view) {
            var day = date.format();
            MeetCalendar.showSchedule(day);
        }
    });
};
/**
 * 获取某天的会议室日程
 * @param day
 */
MeetCalendar.showSchedule = function (day) {
    MeetCalendar.hideCalendar();
    MeetCalendar.renderSchedule(day);
    MeetCalendar.loadScheduleData(day);
};
/**
 * @author liux
 * 加载当天会议日程数据
 */
MeetCalendar.loadScheduleData = function (day) {

    var dayBegin = day + " 00:00:00";
    var dayEnd = day + " 23:59:59";
    var html = [];
    if (dayBegin != null && dayEnd!= null) {
        Public.getRest('/conference/query?beginTime='+dayBegin+'&endTime='+dayEnd,function (resp){
            var conferences = resp.items;
            for(var i= 0; i < conferences.length; i++){
                var conferenceBegin = new Date(conferences[i].beginTime);
                var conferenceEnd = new Date(conferences[i].endTime);
                html.push('<tr>');
                html.push('<td>'+conferences[i].name+'</td>');
                for(var j = 8 ; j <= 17; j++){
                    if(j >= conferenceBegin.getHours() && j <= conferenceEnd.getHours()){
                        html.push('<td class="bg-org"></td>');
                    }else{
                        html.push('<td class="bg-yellow"></td>');
                    }
                }
                html.push('</tr>');
            }
            $('.schedule-data').html(html.join(''));
        });

    }

    $('.js-date').html(day);
    $('.js-begin-time').datebox({
        onSelect:function(){
            MeetCalendar.loadScheduleData($('.js-begin-time').datebox('getValue'));
    }});
};

/**
 * 加载前一天的会议日程数据
 *
 * */
MeetCalendar.showPreScheduleData = function(){
    var dayNow = $('.js-date').text();
    var preDay = MeetCalendar.GetDateStr(dayNow,-1);
    MeetCalendar.loadScheduleData(preDay);
};

/**
 * 加载后一天的会议日程数据
 *
 * */
MeetCalendar.showNextScheduleData = function(){
    var dayNow = $('.js-date').text();
    var nextDay = MeetCalendar.GetDateStr(dayNow,1);
    MeetCalendar.loadScheduleData(nextDay);
};


/**
 * 获取某个日期之前或之后的日期
 *
 * */
MeetCalendar.GetDateStr = function(dayNow,addDayCount) {
    var dd = new Date(dayNow);
    dd.setDate(dd.getDate()+addDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    m = m > 9 ? m : '0' + m;
    d = d > 9 ? d : '0' + d;
    return y+"-"+m+"-"+d;
};


/**
 * 渲染日程表单
 */
MeetCalendar.renderSchedule = function (day) {
    $('.js-begin-time').datebox().datebox('setValue', day);
    MeetCalendar.loadBoardroomList('.js-schedule .js-room');
};
/**
 * 加载会议室列表数据
 * @param el
 */
MeetCalendar.loadBoardroomList = function (el) {
    Public.getRest('/boardroom', function (rs) {
        var html = [];
        html.push('<option value="">全部</option>');
        if (rs != null && rs.length > 0) {
            for (var i = 0, j = rs.length; i < j; i++) {
                var r = rs[i];
                html.push('<option value="' + r.id + '">' + r.name + '</option>');
            }
        }
        $(el).html(html.join(''));
    });
};
/**
 * 显示日历
 */
MeetCalendar.showCalendar = function () {
    $('.js-schedule').hide();
    $('#calendar').show();
};
/**
 * 隐藏日历
 */
MeetCalendar.hideCalendar = function () {
    $('#calendar').hide();
    $('.js-schedule').show();
};
/**
 * 转化数据格式
 */
MeetCalendar.convertData = function (data) {
    if (data == null || data.length == 0) {
        return null;
    }
    var _data = [];
    for (var i = 0, j = data.length; i < j; i++) {
        var item = data[i];
        var _item = {};
        _item.title = item.NUM;
        _item.start = item.BEGIN_TIME;
        _data.push(_item);
    }
    return _data;
};
/**
 * 获取当前月份
 */
MeetCalendar.getCurrentMonthStr = function () {
    var date = new Date();
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    return y + '-' + m;
};