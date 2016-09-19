/**
 * 我的会议
 * @type {{}}
 */
var MineMeet = {

};
/**
 * 初始化
 */
MineMeet.init = function () {
    $('#main-view').load('partials/meet/mine_meet.html', function(){
        MineMeet.renderForm();
        //TODO:暂时列出所有的会议
        MineMeet.loadData();
    });
};
/**
 * 渲染查询表单
 */
MineMeet.renderForm = function () {
    $('.datetimebox').datetimebox();
};
/**
 * 加载数据
 */
MineMeet.loadData = function(){
    //加载会议室表格数据
    var params = MineMeet.getSearchParams();
    var url = URI + '/rest/conference/easyui_query';
    if (params.length > 0) {
        url += '?' + params.join('&');
    }
    $('.js-project-man-table').datagrid({
        //url:'json/meet-hys-control.json',
        url: url,
        pagination:true,
        rownumbers:true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        columns:[[
            {field:'id',title:'ID',hidden:true},
            {field: 'name', title: '会议名称', width: 180},
            {field: 'beginTime', title: '开始时间', width: 130},
            {field: 'endTime', title: '结束时间', width: 130},
            {field:'status',title:'会议状态',width:100},
            {
                field: 'user', title: '预约人', width: 100, formatter: function (value, row) {
                var user = row.reserveUser;
                return user != null ? user.name : '';
            }
            },
            {
                field: 'operate', title: '操作', width: 200, formatter: function (value, row) {
                var id = row.id;
                var name = row.name;
                var html = [];
                html.push('<div class="a-btn-group">');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="MeetManage.show(\'' + id + '\');">查看</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="">会议记录</a>');
                html.push('</div>');
                return html.join('');
            }}
        ]],
        onLoadSuccess: function (data) {
            if (data.total == 0) {
                Public.showNoResult('.js-project-man-table', 'name', 6);
            }
        }
    });
};
/**
 * 获取查询参数列表
 */
MineMeet.getSearchParams = function () {
    var name = $('.js-name').val();
    var status = $('.js-status').val();
    var begin = $('.js-begin-time').datetimebox('getValue');
    var end = $('.js-end-time').datetimebox('getValue');
    var params = [];
    if (!Public.isNull(name)) {
        params.push('name=' + encodeURI(name));
    }
    if (!Public.isNull(status)) {
        params.push('status=' + encodeURI(status));
    }
    if (!Public.isNull(begin)) {
        params.push('beginTime=' + begin);
    }
    if (!Public.isNull(end)) {
        params.push('endTime=' + end);
    }
    return params;
};